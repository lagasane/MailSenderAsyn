package com.lagasane.MailSender;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

//import org.apache.logging.log4j.Logger;
//import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Component
@EnableAsync
public class EmailSender {

	private static final Logger log = LogManager.getLogger(EmailSender.class);

	@Autowired
	JavaMailSender javaMailSender;
	
	
	@Async
	public void send(String[] target,String title, String content) throws InterruptedException  {
		log.debug("----send mail -----");
		System.out.println("----send mail -----");
		List<String>  listOfError1      = Collections.synchronizedList(new ArrayList<String>());
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message);
	        log.debug("########### title "+title);
	    	try {
				helper.setText(content, true);
				helper.setTo(target);
	            helper.setSubject(title);
	            
	            javaMailSender.send(message);
	        } catch (MessagingException e) {
	            e.printStackTrace();
	        }
	    	
		} catch (Exception e2) {
			listOfError1.add(e2.getMessage());
			e2.printStackTrace();
		}
		
		log.info("----End send mail -----");
		System.out.println("----End send mail -----");
		
	}
	
	@Async
	public void send(String[] target,String title, String content, List <Object> attachments) throws InterruptedException  {
		log.info("----send mail -----");
		System.out.println("----send mail -----");
		
		List<String>  listOfError1      = Collections.synchronizedList(new ArrayList<String>());
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
	        MimeMessageHelper helper = new MimeMessageHelper(message, true);
	    	try {
				helper.setText(content, true);
				helper.setTo(target);
	            helper.setSubject(title);
	            
	            for (Object attachment: attachments) {
	                File file = ((ClassPathResource) attachment).getFile();
	                helper.addAttachment(file.getName(), file);
	            }
	            //Send mail
	            javaMailSender.send(message);
	        } catch (MessagingException e) {
	            e.printStackTrace();
	        }
	    	
		} catch (Exception e2) {
			listOfError1.add(e2.getMessage());
			e2.printStackTrace();
		}
		log.info("----End send mail -----");
		System.out.println("----send mail -----");
		
	}
	
	
	//Configuration du thread d'execution
	@Bean
    public Executor asyncExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(2); 			//Nombre de theard simultané
        executor.setMaxPoolSize(2);  			//Nombre Max de theard simultané
        executor.setQueueCapacity(500);  		//Nombre max de la file d'attente
        executor.setThreadNamePrefix("MailSendAsync-");   //prefix du nom du theard
        executor.initialize();
        return executor;
    }
}
