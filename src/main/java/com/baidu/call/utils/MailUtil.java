package com.baidu.call.utils;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;


public class MailUtil {
    private static final Logger logger = Logger.getLogger(MailUtil.class);

    /**
     * @param from         发件人
     * @param toEmail      收件人
     * @param toName       收件人名字
     * @param subject      邮件主题
     * @param mailTextBody 邮件文字内容
     * @param mailHtmlBody 邮件页面内容
     * @return
     */
    public static boolean sendTo(String from, String toEmail, String toName, String subject, String mailTextBody, String mailHtmlBody) {
        try {
            // new一个发送简单邮件
            HtmlEmail email = new HtmlEmail();
            // 设置字符输出格式
            email.setCharset("UTF-8");
            // 设置邮件服务器smtp.qq.com
            email.setHostName(ReadConfig.readConfig("email", "host"));
            if (HisenUtils.notEmpty(from)) {
                email.setFrom(from);
            } else {
                email.setFrom(ReadConfig.readConfig("email", "from"));
            }
            email.addTo(toEmail, toName);
            email.setSubject(subject);
            email.setHtmlMsg(mailHtmlBody);
            email.setTextMsg(mailTextBody);
            email.send();
            return true;
        } catch (EmailException e1) {
            logger.error("邮件发送失败" + e1.getMessage());
            return false;
        }
    }


    @Async
    public static boolean sendTextTo(String from, String toEmail, String toName, String subject, String mailTextBody) {
        try {
            // new一个发送简单邮件
            SimpleEmail email = new SimpleEmail();
            // 设置字符输出格式
            email.setCharset("UTF-8");
            // 设置邮件服务器smtp.qq.com
            email.setHostName(ReadConfig.readConfig("email", "host"));
            if (HisenUtils.notEmpty(from)) {
                email.setFrom(from);
            } else {
                email.setFrom(ReadConfig.readConfig("email", "from"));
            }
            email.addTo(toEmail, toName);
            email.setSubject(subject);
            email.setMsg(mailTextBody);
            email.send();
            return true;
        } catch (EmailException e1) {
            logger.error("邮件发送失败" + e1.getMessage());
            return false;
        }
    }

    public static void main(String[] args) {
        String applyMsg = "<p style='margin:0;padding:0;line-height:20px;'>您好！</p>";
        applyMsg += "<p style='text-indent:35px;margin:0;padding:0;line-height:20px;'>当前 ServiceDesk(IT服务台) 中出现异常情况，现<span style='color:red;font-weight:bold;'>通知</span>于您 详细情况如下：</p><br>";
        applyMsg += "<p style='text-indent:35px;margin:0;padding:0;line-height:20px;'>异常类型：<span style='color:red;'>响应时长</span></p>";
        applyMsg += "<p style='text-indent:35px;margin:0;padding:0;line-height:20px;'>超出时长：<span style='color:red;'>30分钟</span></p>";
        applyMsg += "<p style='text-indent:35px;margin:0;padding:0;line-height:20px;'>工单编号：<span style='color:blue;'>201711170002</span></p>";
        applyMsg += "<p style='text-indent:35px;margin:0;padding:0;line-height:20px;'>故障类型：<span style='color:blue;'>桌面服务</span></p>";
        applyMsg += "<p style='text-indent:35px;margin:0;padding:0;line-height:20px;'>故障细分：<span style='color:blue;'>软件安装/卸载</span></p>";
        applyMsg += "<p style='text-indent:35px;margin:0;padding:0;line-height:20px;'>故障标题：<span style='color:blue;'>软件不能正常卸载</span></p>";
        applyMsg += "<p style='text-indent:35px;margin:0;padding:0;line-height:20px;'>故障内容：<span style='color:blue;'>我的电脑今天装一款360杀毒软件，目前不能卸载啦！</span></p><br><br>";
        applyMsg += "<p style='text-indent:35px;margin:0;padding:0;line-height:20px;'>望您接到此<span style='color:red;font-weight:bold;'>通知/提醒</span>后，请慎重处理！</p>";
        applyMsg += "<p style='text-indent:35px;margin:0;padding:0;line-height:20px;'>该邮件发送自-IT服务系统，请勿直接回复！<a href='http://172.16.207.133:8088' title='IT服务台'>http://172.16.207.133:8088(平台网址)</a></p>";
        sendToHtml("chenyafei01@sh.baidu.com", "chenyafei01@sh.baidu.com", "闫丽", "测试邮件", applyMsg);
    }


    @Async
    public static boolean sendToHtml(String from, String toEmail, String toName, String subject, String mailHtmlBody) {
        try {
            // new一个发送简单邮件
            HtmlEmail email = new HtmlEmail();
            // 设置字符输出格式
            email.setCharset("UTF-8");
            // 设置邮件服务器smtp.qq.com
            email.setHostName(ReadConfig.readConfig("email", "host"));
            if (HisenUtils.notEmpty(from)) {
                email.setFrom(from);
            } else {
                email.setFrom(ReadConfig.readConfig("email", "from"));
            }
            email.addTo(toEmail, toName);
            email.setSubject(subject);
            email.setHtmlMsg(mailHtmlBody);
            email.send();
            return true;
        } catch (EmailException e1) {
            logger.error("邮件发送失败" + e1.getMessage());
            return false;
        }
    }
}
