package com.rent.service;

import com.rent.entity.User;
import javax.mail.internet.MimeMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

// todo separate email interface and implementation
// todo create message from file (map cache)
// todo use FieldInsertedText, check FreeMarker
// see for spring boot mailing: https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/mail.html (Java Mail)
// see for spring boot mailing: https://docs.spring.io/spring-framework/docs/5.3.x/reference/html/integration.html#mail (Java Mail / Jakarta Mail 1.6)

@Service
public class EmailService {
    private final Log log = LogFactory.getLog(this.getClass());

    // todo should get from properties (rent service email address)
	@Value("${spring.mail.username}")
	private String MESSAGE_FROM;
	
	private JavaMailSender javaMailSender;

	@Autowired
	public void setJavaMailSender(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
	}


	public void sendMessage(User user) {
/*
		reminder:
		SimpleMailMessage message = null;
*/
		MimeMessage message = javaMailSender.createMimeMessage();
		String email = user.getEmail();
		String activation = user.getActivation();
		
		try {
/*
			reminder (SimpleMailMessage):
			message = new SimpleMailMessage();
			message.setFrom(MESSAGE_FROM);
			message.setTo(email);
			message.setSubject("Sikeres regisztrálás");
*/
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			helper.setFrom(MESSAGE_FROM);
			helper.setTo(email);
			helper.setSubject("Sikeres regisztrálás");
			String msgContent = "Kedves " + user.getFullName() + "! <br/><br/> Köszönjük, hogy regisztráltál az oldalunkra!<br/><br/>" +
                                            "Regisztrációdat <a href=\"https://localhost:8443/activation/" + activation + "\">ide</a> kattintva aktiválhatod.";
                        helper.setText(msgContent, true);
			javaMailSender.send(message);
			
		} catch (Exception e) {
			log.error("Hiba e-mail küldéskor az alábbi címre: " + email + "  " + e);
		}
		

	}
	
	
}
