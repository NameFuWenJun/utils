package com.fuwenjun.projectUtils.webHttp;

import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 读取配置文件工具类
 *
 */
public class PropertiesUtil {
    
    private static final Logger logger = LoggerFactory.getLogger(PropertiesUtil.class);
    
    public static String getEmailServiceAddress(){
        Properties properties=getProperties("/emailService.properties");
        String ipAndPort=properties.getProperty("emailService");
        return ipAndPort;
    }
    private static Properties getProperties(String path){
        Properties prop = new Properties();
        InputStream in = PropertiesUtil.class.getResourceAsStream(path);
        try {
            if(in == null){
                logger.error("alertplatform获取邮件配置文件失败");
                throw new RuntimeException("Cannot find emailService.properties");
            }
            prop.load(in);
            in.close();
        } catch (Exception e) {
            logger.error("获取邮件配置文件失败");
            e.printStackTrace();
        }
        return prop;
    }

}
