package com.fuwenjun.projectUtils.jdbc;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DruidProperties {
	
	private static final Logger logger = LoggerFactory.getLogger(DruidProperties.class);
	
	private static DruidProperties druidProperties;
	
	private DruidProperties(){
		
	}
	
	public static DruidProperties getInstance(){
		if(druidProperties == null){
			synchronized(DruidProperties.class){
				if(druidProperties == null){
					druidProperties = new DruidProperties();
				}
			}
		}
		return druidProperties;
	}
	
	public Properties getProperties(Map<String,Object> databaseInfo){
		Properties prop = new Properties();
		InputStream in = null;
		if(databaseInfo.get(DatabaseInfoConstants.DATATYPE) == DataBase.MYSQL){
			in = System.class.getResourceAsStream("/mysql-druid.properties");
			if(in == null){
               throw new RuntimeException("Cannot find mysql-druid.properties");
            }
		}
		if(databaseInfo.get(DatabaseInfoConstants.DATATYPE) == DataBase.ORACLE){
			in = System.class.getResourceAsStream("/oracle-druid.properties");
			if(in == null){
				throw new RuntimeException("Cannot find oracle-druid.properties");
			}
		}
		try {
			prop.load(in);
			setProperties(prop,databaseInfo);
			logger.info("数据库连接参数：{},{},{}",
					new Object[]{prop.getProperty("url"),prop.getProperty("username"),prop.getProperty("password")});
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return prop;
	}
	
	/**
	 * 修改配置文件
	 * @param prop
	 * @param url
	 * @param username
	 * @param password
	 */
	private static void setProperties(Properties prop, Map<String,Object> databaseInfo){
		prop.put("url", prop.getProperty("url")
				.replace("{{ipAndPort}}", databaseInfo.get(DatabaseInfoConstants.IPANDPORT).toString())
				.replace("{{database}}", databaseInfo.get(DatabaseInfoConstants.DATABASE).toString()));
		prop.put("username", prop.getProperty("username")
				.replace("{{username}}", databaseInfo.get(DatabaseInfoConstants.USERNAME).toString()));
		prop.put("password", prop.getProperty("password")
				.replace("{{password}}", databaseInfo.get(DatabaseInfoConstants.PASSWORD).toString()));
		
	}
	
	public static void main(String[] args) {
		Map<String,Object> info = new HashMap<String,Object>();
		info.put(DatabaseInfoConstants.DATATYPE, DataBase.MYSQL);
		info.put(DatabaseInfoConstants.IPANDPORT, "172.22.4.51:3306");
		info.put(DatabaseInfoConstants.DATABASE, "supportpj");
		info.put(DatabaseInfoConstants.USERNAME, "supportpj");
		info.put(DatabaseInfoConstants.PASSWORD, "supportpj@lz");
		DruidProperties.getInstance().getProperties(info);
	}
}
