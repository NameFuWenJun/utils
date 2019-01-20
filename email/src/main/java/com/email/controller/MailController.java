package com.email.controller;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.util.SystemPropertyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * 这是一个邮件控制类
 * @author Administrator
 *
 */
@RequestMapping("sendMail")
@RestController
public class MailController {

	@Autowired
	private JavaMailSender mailSender;
	@RequestMapping(value="/sendSimpleMailByPost",method=RequestMethod.POST)
	public int sendSimpleMailByPost(String to,String title,String message){
		SimpleMailMessage email = new SimpleMailMessage();
		email.setFrom("572313160@qq.com");
		email.setTo(to);
		email.setSubject(title);
		email.setText(message);
		mailSender.send(email);
		return 1;
	}
	@RequestMapping(value="/sendSimpleMailByGet",method=RequestMethod.GET)
	public int sendSimpleMailByGet(String to,String title,String message){
		SimpleMailMessage email = new SimpleMailMessage();
		email.setFrom("572313160@qq.com");
		email.setTo(to);
		email.setSubject(title);
		email.setText(message);
		mailSender.send(email);
		return 1;
	}
	@RequestMapping(value="/sendHtilMailByPost",method=RequestMethod.POST)
	public int sendHtmlMailByPost(String to,String title,String message){
		System.out.println(to);
		MimeMessage email = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(email, true);
			helper.setFrom("572313160@qq.com");
			helper.setTo(to);
			helper.setSubject(title);
			helper.setText(message,true);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mailSender.send(email);
		return 1;

	}
	@RequestMapping(value="/sendHtilMailByGet",method=RequestMethod.GET)
	public int sendHtmlMailByGet(String to,String title,String message){
		System.out.println(to);
		MimeMessage email = mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(email, true);
			helper.setFrom("572313160@qq.com");
			helper.setTo(to);
			helper.setSubject(title);
			helper.setText(message,true);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		mailSender.send(email);
		return 1;
	}
}
