package com.heima.common.aliyun;

import com.alibaba.fastjson.JSON;

import com.baidu.aip.contentcensor.AipContentCensor;
import com.heima.common.constants.ExamineResult;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.*;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "aliyun")
public class GreenTextScan {
    public   String APP_ID;
    public   String API_KEY;
    public   String SECRET_KEY;

    public  ExamineResult TextScan(String text) throws Exception {
        AipContentCensor client = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);
        JSONObject response = client.textCensorUserDefined(text);
        ExamineResult result = JSON.parseObject(response.toString(), ExamineResult.class);
        return result;
    }
}