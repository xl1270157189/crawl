package com.etoak.crawl.util;

import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;

import java.io.IOException;

/**
 * @author xulei
 * @date 2019/7/12 9:56
 * Description: 发送短信
 */
public class SMS {

    // 短信应用SDK AppID
    static int appid = 1400229862; // 1400开头

    // 短信应用SDK AppKey
    static String appkey = "2eca9edf3a60b17f34148544dc64063a";

    // 需要发送短信的手机号码
    static String[] phoneNumbers = {"18324121337","18896019750"};

//    // 短信模板ID，需要在短信应用中申请
//    int templateId = 371974; // NOTE: 这里的模板ID`7839`只是一个示例，真实的模板ID需要在短信控制台中申请
//
//    // 签名
//    String smsSign = "腾讯云"; // NOTE: 这里的签名"腾讯云"只是一个示例，真实的签名需要在短信控制台中申请，另外签名参数使用的是`签名内容`，而不是`签名ID`


    public static void sendSmss(){
        try {
            SmsMultiSender msender = new SmsMultiSender(appid, appkey);
            SmsMultiSenderResult result =  msender.send(0, "86", phoneNumbers,
                    "您当前正在体验小说更新短信服务，您追踪的小说已更新，如非本人操作，请忽略此短信。", "", "");
            System.out.println(result);
        } catch (HTTPException e) {
            // HTTP 响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // JSON 解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络 IO 错误
            e.printStackTrace();
        }
    }

}
