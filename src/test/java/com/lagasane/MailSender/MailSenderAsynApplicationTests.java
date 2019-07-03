package com.lagasane.MailSender;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MailSenderAsynApplicationTests {

	@Autowired
	private EmailSender emailSender;
	
	private static final Logger log = LogManager.getLogger(MailSenderAsynApplicationTests.class);

	@Test
	public void contextLoads() {
		log.debug("----@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@ mail -----");
		sendMail();
	}
	
	@Test
	private void sendMail() {
		log.debug("----###################################### mail -----");
		List<String>  listOfError2      = Collections.synchronizedList(new ArrayList<String>());
		try {
			String[] target = {"lagasane@gmail.com", "lagasane@live.fr"};
	        
	        
			//retrieve template mail via file application-mail.properties
			//String content = emailTemplate.replace("{{Name}}", "Mr Lagasane");
			
			//retrieve template mail via file in ClassPathResource
	        File templateFile = new ClassPathResource("templates/sendMailConnexion.html").getFile();
	        String templatEmail = Files.asCharSource(templateFile, Charsets.UTF_8).read();
	        
	        //Formating email
			String content = templatEmail.replace("{{Name}}", "Mr Lagasane");
			
	        //Joint attachement file
	        List <Object> attachments = new ArrayList<Object> ();
	        attachments.add(new ClassPathResource("Filemail.pdf"));
	        
			emailSender.send(target, "For test Title", content, attachments);
			
		} catch (Exception e2) {
			listOfError2.add(e2.getMessage());
			e2.printStackTrace();
		}
	}

}
