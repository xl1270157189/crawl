package com.etoak.crawl.util;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * @author xulei
 * @date 2019/7/15 9:49
 * Description:发送邮件
 */
public class Mail {

    //发件人地址
    public static String senderAddress = "1270157189@qq.com";

    //发件人账户密码
    public static String senderPassword = "jegorarcuutkhfcb";


    /**
     * 发送文本邮件
     * @param recipientAddress 收件人地址
     * @param subject 标题
     * @param text 内容
     */
    public static void SendMailText(String recipientAddress,String subject,String text){

        if(recipientAddress.length()<1){
            System.out.println("请传入收件人地址");
            return;
        }
        try {
            Properties properties = new Properties();
            // 连接协议
            properties.put("mail.transport.protocol", "smtp");
            // 主机名
            properties.put("mail.smtp.host", "smtp.qq.com");
            // 端口号
            properties.put("mail.smtp.port", 465);
            properties.put("mail.smtp.auth", "true");
            // 设置是否使用ssl安全连接 ---一般都使用
            properties.put("mail.smtp.ssl.enable", "true");
            // 设置是否显示debug信息 true 会在控制台显示相关信息
            properties.put("mail.debug", "true");
            // 得到回话对象
            Session session = Session.getInstance(properties);
            // 获取邮件对象
            Message message = new MimeMessage(session);
            // 设置发件人邮箱地址
            message.setFrom(new InternetAddress(senderAddress));
            // 设置收件人邮箱地址
            String [] recipientAddresss = recipientAddress.split(",");
            Address [] internetAddress = new Address[recipientAddresss.length];
            for(int i = 0;i<recipientAddresss.length;i++){
                internetAddress[i] = new InternetAddress(recipientAddresss[i]);
            }
            message.setRecipients(Message.RecipientType.TO,internetAddress);
            // 设置邮件标题
            message.setSubject(subject);
            // 设置邮件内容
            message.setText(text);
            // 得到邮差对象
            Transport transport = session.getTransport();
            // 连接自己的邮箱账户 密码为QQ邮箱开通的stmp服务后得到的客户端授权码
            transport.connect(senderAddress, senderPassword);
            // 发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        }catch (Exception e){
            System.out.println("发送邮件错误"+e);
        }

    }

    //main 方法入口
    public static void main(String[] args) {
        String adderss = "849437676@qq.com,1270157189@qq.com";
        String subject = "测试";
        String test = "测试";
        SendMailText(adderss,subject,test);
    }

}
