package com.heima.schedule.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.heima.common.constants.ScheduleConstants;
import com.heima.common.redis.CacheService;
import com.heima.model.schedule.dtos.Task;
import com.heima.model.schedule.pojos.Taskinfo;
import com.heima.model.schedule.pojos.TaskinfoLogs;
import com.heima.schedule.mapper.TaskinfoLogsMapper;
import com.heima.schedule.mapper.TaskinfoMapper;
import com.heima.schedule.service.TaskService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@Slf4j
public class TaskServiceImpl implements TaskService {
    @Override
    public long addTask(Task task) {
        //1.添加任务到数据库中
        boolean success=addTaskToDb(task);
        if(success){
            //2.添加任务到redis
            addTaskToCache(task);
        }
        return task.getTaskId();
    }

    /**
     * 取消任务
     * @param taskId
     * @return
     */
    @Override
    public boolean cancelTask(long taskId) {
        boolean falg=false;
        //删除任务，更新任务日志
        Task task=updateDb(taskId,ScheduleConstants.CANCELLED);
        //删除redis的数据
        if(task!=null){
            removeTaskFromCache(task);
            falg=true;
        }
        return falg;
    }

    @Override
    public Task poll(int type, int priority) {
        Task task=null;
      try{
          String key=type+"-"+priority;
          //从redis中拉取任务
          String task_json = cacheService.lRightPop(ScheduleConstants.TOPIC + key);
          if(StringUtils.isNotBlank(task_json)){
             task = JSON.parseObject(task_json, Task.class);
              //修改数据库信息
              updateDb(task.getTaskId(),ScheduleConstants.EXECUTED);
          }
      }catch (Exception e){
          e.printStackTrace();
          log.error("poll task exception");
      }

        return task;
    }

    /**
     * 删除redis的数据
     * @param task
     */
    private void removeTaskFromCache(Task task) {
        String key = task.getTaskType() + "-" + task.getPriority();
        if(task.getExecuteTime()<=System.currentTimeMillis()){
            cacheService.lRemove(ScheduleConstants.TOPIC+key,0,JSON.toJSONString(task));
        }else{
            cacheService.zRemove(ScheduleConstants.FUTURE+key,JSON.toJSONString(task));
        }
    }

    /**
     * 删除任务，更新任务日志
     * @param taskId
     * @param status
     * @return
     */
    private Task updateDb(long taskId, int status) {
        Task task=null;
        try{
            taskinfoMapper.deleteById(taskId);
            TaskinfoLogs taskinfoLogs = taskinfoLogsMapper.selectById(taskId);
            taskinfoLogs.setStatus(status);
            taskinfoLogsMapper.updateById(taskinfoLogs);
             task=new Task();
            BeanUtils.copyProperties(taskinfoLogs,task);
            task.setExecuteTime(taskinfoLogs.getExecuteTime().getTime());
        }catch (Exception e){
            log.error("task canel exception taskId={}",taskId);
        }
        return task;
    }

    @Autowired
    private CacheService cacheService;
    /**
     * 添加任务到redis
     * @param task
     */
    private void addTaskToCache(Task task) {
        String key = task.getTaskType() + "-" + task.getPriority();
        //获取5分钟后的时间
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        long nextScheduleTime = calendar.getTimeInMillis();
        //2.1如果任务的执行时间小于等于当前时间，存到list
        if (task.getExecuteTime() <= System.currentTimeMillis()) {
            cacheService.lLeftPush(ScheduleConstants.TOPIC + key, JSON.toJSONString(task));
        } else if (task.getExecuteTime() <= nextScheduleTime) {
            //2.2如果任务的执行时间大于当前时间&&小于等于预设时间，存到zset
            cacheService.zAdd(ScheduleConstants.FUTURE+key, JSON.toJSONString(task),task.getExecuteTime());
        }
    }
    @Autowired
    private TaskinfoMapper taskinfoMapper;
    @Autowired
    private TaskinfoLogsMapper taskinfoLogsMapper;
    /**
     * 添加任务到数据库中
     * @param task
     * @return
     */
    private boolean addTaskToDb(Task task){
        boolean flag=false;
        //保存任务表
     try{
         Taskinfo taskinfo=new Taskinfo();
         BeanUtils.copyProperties(task,taskinfo);
         taskinfo.setExecuteTime(new Date(task.getExecuteTime()));
         taskinfoMapper.insert(taskinfo);
         //设置taskId
         task.setTaskId(taskinfo.getTaskId());
         //保存任务日志表
         TaskinfoLogs taskinfoLogs=new TaskinfoLogs();
         BeanUtils.copyProperties(taskinfo,taskinfoLogs);
         taskinfoLogs.setVersion(1);
         taskinfoLogs.setStatus(ScheduleConstants.SCHEDULED);
         taskinfoLogsMapper.insert(taskinfoLogs);
         flag=true;
     }catch (Exception e){
         e.printStackTrace();
     }
     return flag;
    }

    /**
     * 未来数据刷新
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void refresh(){
        String token = cacheService.tryLock("FUTRUE_TASK_SYNC", 1000 * 30);
        if(StringUtils.isNotBlank(token)){
            log.info("未来数据定时刷新");
            Set<String> futureKeys = cacheService.scan(ScheduleConstants.FUTURE + "*");
            for (String futureKey : futureKeys) {
                String topicKey=ScheduleConstants.TOPIC+futureKey.split(ScheduleConstants.FUTURE)[1];
                Set<String> tasks = cacheService.zRangeByScore(futureKey, 0, System.currentTimeMillis());
                if(!tasks.isEmpty()){
                    cacheService.refreshWithPipeline(futureKey,topicKey,tasks);
                    log.info("成功将"+futureKey+"刷新到了"+topicKey);
                }
            }
        }
    }

    /**
     * 数据库任务定时同步到redis中
     */
    @PostConstruct
    @Scheduled(cron = "0 */5 * * * ?")
    public void reloadData(){
        //清理缓存数据
        clearCache();

        //查询符合条件的任务 小于未来5分钟的数据
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 5);
        List<Taskinfo> taskinfoList = taskinfoMapper.selectList(Wrappers.<Taskinfo>lambdaQuery().lt(Taskinfo::getExecuteTime, calendar.getTime()));

        //把任务添加到redis
        if(taskinfoList!=null&&taskinfoList.size()>0){
            for (Taskinfo taskinfo : taskinfoList) {
                Task task=new Task();
                BeanUtils.copyProperties(taskinfo,task);
                task.setExecuteTime(taskinfo.getExecuteTime().getTime());
                addTaskToCache(task);
            }
        }
        log.info("数据库的任务同步到了redis");
    }

    /**
     * 清理缓存数据
     */
    public void clearCache(){
        Set<String> topicKeys = cacheService.scan(ScheduleConstants.TOPIC + "*");
        Set<String> futureKeys = cacheService.scan(ScheduleConstants.FUTURE+"*");
        cacheService.delete(topicKeys);
        cacheService.delete(futureKeys);
    }
}
