package com.fuwenjun.projectUtils.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

public class DatasourceUtil {
	
	private static DruidDataSource dataSource;
	
	//保存数据库连接参数
	private static ConcurrentHashMap<Map<String,Object>,DruidDataSource> dbConfig = new ConcurrentHashMap<>();
	
	private static ReentrantLock lock = new ReentrantLock();
	
	private DatasourceUtil(){
		
	}
	
	public static final DruidDataSource getDataSource(Map<String,Object> databaseInfo) throws Exception {
		if(isExist(databaseInfo))
			return dbConfig.get(databaseInfo);
		lock.lock();
		if(!isExist(databaseInfo)){
			createDataSource(databaseInfo);
		}
		lock.unlock();
		return dbConfig.get(databaseInfo);
	}
	
	//抛出连接异常，让客户端，服务端各自处理
	private static void createDataSource(Map<String,Object> databaseInfo) throws Exception{
		dataSource = (DruidDataSource) DruidDataSourceFactory
				.createDataSource(DruidProperties.getInstance().getProperties(databaseInfo));
		dbConfig.put(databaseInfo,dataSource);
	}
	
	private static boolean isExist(Map<String,Object> databaseInfo){
		for(Map<String,Object> key: dbConfig.keySet()){
			if(key.equals(databaseInfo))
				return true;
		}
		return false;
	}
	
    public static void main(String[] args) throws Exception {
    	Map<String,Object> info = new HashMap<String,Object>();
    	info.put(DatabaseInfoConstants.DATATYPE, DataBase.MYSQL);
		info.put(DatabaseInfoConstants.IPANDPORT, "172.22.4.51:3306");
		info.put(DatabaseInfoConstants.DATABASE, "supportpj");
		info.put(DatabaseInfoConstants.USERNAME, "supportpj");
		info.put(DatabaseInfoConstants.PASSWORD, "supportpj@lz");
		DruidDataSource datasource = DatasourceUtil.getDataSource(info);
		Connection con = datasource.getConnection();
		String sql = "select count(*) from ap_assign_strategy";
		Statement sts = con.createStatement();
		ResultSet rs = sts.executeQuery(sql);
		if (rs.next()) {
			System.out.print(rs.getInt(1));
		}
    }
	

}
