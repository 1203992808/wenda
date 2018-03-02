package com.example.util;

import freemarker.template.Template;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.util.Map;
import java.util.Properties;

@Service
public class MailSender implements InitializingBean{
    private static final Logger logger = LoggerFactory.getLogger(MailSender.class);
    private JavaMailSenderImpl mailSender;
    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;
    public boolean sendWithHTMLTemplate(String to, String subject, String templatePath, Map<String,Object> model){
       try {
          // String nick=MimeUtility.encodeText("阿璋");
           InternetAddress from=new InternetAddress("15764241054@163.com");
           MimeMessage message=mailSender.createMimeMessage();
           MimeMessageHelper messageHelper=new MimeMessageHelper(message);

           Template template=freeMarkerConfigurer.getConfiguration().getTemplate(templatePath);
           String htmlText = FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
           messageHelper.setTo(to);
           messageHelper.setFrom(from);
           messageHelper.setSubject(subject);
           messageHelper.setText(htmlText,true);
           mailSender.send(message);
           return true;

       }catch(Exception e){
           logger.error("错误"+e.getMessage());

       }
       return false;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        mailSender=new JavaMailSenderImpl();

        mailSender.setUsername("15764241054@163.com");
        mailSender.setPassword("13666370029sh");

       mailSender.setHost("smtp.163.com");
      //  mailSender.setHost("smtp.qq.com");
       // mailSender.setHost("smtp.qq.com");
        mailSender.setPort(994);
        mailSender.setProtocol("smtps");
        mailSender.setDefaultEncoding("utf8");
        Properties javaMailProperties = new Properties();
        javaMailProperties.put("mail.smtp.ssl.enable", true);
        javaMailProperties.put("mail.smtp.auth", true);
        //javaMailProperties.put("mail.smtp.starttls.enable", true);
        mailSender.setJavaMailProperties(javaMailProperties);


    }
}
