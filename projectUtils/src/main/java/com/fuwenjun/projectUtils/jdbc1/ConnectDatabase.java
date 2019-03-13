package com.fuwenjun.projectUtils.jdbc1;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;


import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.fuwenjun.projectUtils.jdbc.DataBase;

/**
 * 初始化数据库连接池
 * @author fuwenjun01
 *
 */
public class ConnectDatabase {
    private static final String ORACLE_PRE="jdbc:oracle:thin:@";
    private static final String MYSQL_PRE="jdbc:mysql://";
    private static final String MYSQL_DRIVER="com.mysql.jdbc.Driver";
    private static final String ORACLE_DRIVER="oracle.jdbc.driver.OracleDriver";
    //已经保存过的数据库连接
    private static ConcurrentHashMap<DruidConifg, DruidDataSource> savedDataSource=new ConcurrentHashMap<>();
    /**
     * 
     * @param prop 
     * @param databaseInfo :用户传过来的数据库连接信息
     */
    private static void setProperties(Properties prop, DruidConifg druidConifg){
        String url="";
        String driverclass="";
        if(DataBase.MYSQL.equals(druidConifg.getDatabase_type())){
            url=MYSQL_PRE+druidConifg.getJdbc_url();
            driverclass=MYSQL_DRIVER;
        }else{
            url=ORACLE_PRE+druidConifg.getJdbc_url();
            driverclass=ORACLE_DRIVER;
        }
        prop.put("url", prop.getProperty("url")
                .replace("{{jdbc_url}}",url));
        prop.put("username", prop.getProperty("username")
                .replace("{{username}}", druidConifg.getUserName()));
        prop.put("password", prop.getProperty("password")
                .replace("{{password}}", druidConifg.getPassword()));
        prop.put("driverClassName", prop.getProperty("driverClassName")
                .replace("{{driverClass}}",driverclass));
        
    }
    
    private static boolean isExist(DruidConifg databaseInfo){
        for(DruidConifg key: savedDataSource.keySet()){
            if(key.equals(databaseInfo)&&!savedDataSource.get(key).isClosed())
                return true;
        }
        return false;
    }
    
    public static DruidDataSource getDruidDataSource(DruidConifg info) throws Exception{
        Properties prop=new Properties();
        InputStream in = null;
        in = ConnectDatabase.class.getResourceAsStream("/druid.properties");
        if(in == null){
            throw new RuntimeException("Cannot find druid.properties");
        }
        prop.load(in);
        setProperties(prop,info);
        if(isExist(info)){
            //如果存在的话返回存在的DataSource
            return savedDataSource.get(info);
        }else{
            DruidDataSource druidDataSource= (DruidDataSource) DruidDataSourceFactory.createDataSource(prop);
            savedDataSource.put(info, druidDataSource);
            return druidDataSource;
        }
    }
    
}
