package com.heima.common.aliyun;

import com.alibaba.fastjson.JSON;
import com.baidu.aip.contentcensor.AipContentCensor;
import com.baidu.aip.contentcensor.EImgType;
import com.heima.common.constants.ExamineResult;
import lombok.Getter;
import lombok.Setter;
import org.json.JSONObject;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "aliyun")
public class GreenImageScan {
    public  String APP_ID;
    public  String API_KEY;
    public  String SECRET_KEY;

    public ExamineResult imageScan(byte[] file) throws Exception {
        AipContentCensor client = new AipContentCensor(APP_ID, API_KEY, SECRET_KEY);
        JSONObject response = response = client.imageCensorUserDefined(file, null);;
        ExamineResult result = JSON.parseObject(response.toString(), ExamineResult.class);
        return result;
    }
}