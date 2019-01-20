package com.email;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 还没配数据库
 * @author fuwenjun01
 *
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class EmailApplication {
	public static void main(String[] args) {
	    SpringApplication.run(EmailApplication.class, args);
	}
}
