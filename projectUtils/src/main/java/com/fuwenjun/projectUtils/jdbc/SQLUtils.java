package com.fuwenjun.projectUtils.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.alibaba.druid.pool.DruidDataSource;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 使用了Dbutils工具类操作数据库连接
 * @author fuwenjun01
 *
 */
public class SQLUtils {
    private Connection connection;
    private DruidDataSource druidDataSource;
    /**
     * @param type:数据库种类,目前有MYSQL及ORACLE 2类
     * @param url :数据库连接地址
     * @param userName 数据库用户名
     * @param password 数据库密码
     * @throws Exception 
     */
    public SQLUtils(DataBase type,String ipAndPort,String database,String userName,String password) throws Exception {
      init(type, ipAndPort, database,userName, password);
    }
    private void init(DataBase type,String ipAndPort,String database,String userName,String password) throws Exception{
       // DataSource dataSource=new DataSource(url, userName, password); 10.11,shine,注释
            //connection=dataSource.getConnect(type); 10.11,shine,注释
           // druidDataSource=dataSource.getDataSource(); 10.11,shine,注释
        	Map<String,Object> info = new HashMap<String,Object>();
    		info.put(DatabaseInfoConstants.DATATYPE, type);
    		info.put(DatabaseInfoConstants.IPANDPORT, ipAndPort);
    		info.put(DatabaseInfoConstants.DATABASE, database);
    		info.put(DatabaseInfoConstants.USERNAME, userName);
    		info.put(DatabaseInfoConstants.PASSWORD, password);
    		druidDataSource = DatasourceUtil.getDataSource(info);
    		connection = druidDataSource.getConnection();
    }
    /**
     * 获取数据库连接
     * @return
     */
    public Connection getConnection() {
        try {
            if(connection.isClosed()){
                connection=druidDataSource.getConnection();
            }
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return connection;
    }
    
    /**
     * 查询一组数据
     * @param sql  :查询语句
     * @return
     */
    public List<Map<String, Object>> selectWithList(String sql){
        List<Map<String, Object>>resultMap=new ArrayList<Map<String, Object>>();
        QueryRunner queryRunner=new QueryRunner(druidDataSource);
        try {
            resultMap=queryRunner.query(sql, new MapListHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultMap;
    }
    /**
     * 查询一组数据带有参数
     * @param sql  :查询语句,参数放入condition数组中
     * @return
     */
    public List<Map<String, Object>> selectWithListAndCondition(String sql,String [] conditions){
        List<Map<String, Object>>resultMap=new ArrayList<Map<String, Object>>();
        QueryRunner queryRunner=new QueryRunner(druidDataSource);
        try {
            resultMap=queryRunner.query(sql,conditions,new MapListHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultMap;
    }
    /**
     * 查询单条数据
     * @param sql
     * @return
     */
    public Map<String, Object> selectWithMap(String sql){
        Map<String, Object>resultMap=new HashMap<String, Object>();
        QueryRunner queryRunner=new QueryRunner(druidDataSource);
        try {
            resultMap=queryRunner.query(sql, new MapHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultMap;
    }
    /**
     * 查询单条数据带有参数
     * @param sql,参数放入数组之中
     * @return
     */
    public Map<String, Object> selectWithMapAndCondition(String sql,String [] conditions){
        Map<String, Object>resultMap=new HashMap<String, Object>();
        QueryRunner queryRunner=new QueryRunner(druidDataSource);
        try {
            resultMap=queryRunner.query(sql, conditions,new MapHandler());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultMap;
    }
    /**
     * 该upadata包含插入以及更新及其他功能
     * @param sql
     * @param conditions
     * @return
     */
    public int update(String sql,Object [] conditions){
        QueryRunner queryRunner=new QueryRunner(druidDataSource);
        int result=0;
        try {
            result=queryRunner.update(sql, conditions);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    
    /**
     * 含有事务的插入语句
     * @param jsonArray  jsonArray为一个Json数组
     * 里面为JSONObject的数组对象,object格式为:
     * {"sql":"***","param":[*,*,*]}
     * @return  执行成功返回1,执行失败返回0
     */
    public int insertWithTrransaction(JSONArray jsonArray){
        QueryRunner queryRunner=new QueryRunner();
        try {
            if(connection.isClosed()){
                connection=druidDataSource.getConnection();
            }
            connection.setAutoCommit(false);
            for(int i=0;i<jsonArray.size();i++){
                JSONObject sqlObject=jsonArray.getJSONObject(i);
                String sql=sqlObject.getString("sql");
                JSONArray params= (JSONArray) sqlObject.get("param");
                Object [] param=new Object[params.size()];
                for(int j=0;j<params.size();j++){
                    param[j]=params.get(j);
                }
                queryRunner.update(connection, sql, param);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            try {
                connection.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            return 0;
        }finally {
            try {
                connection.commit();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return 1;
    }
    /**
     * sql操作执行完毕以后关闭driud数据连接
     * @throws SQLException
     */
    public void close() throws SQLException{
        if(connection!=null&&!connection.isClosed()){
            connection.close();
        }
        if(druidDataSource!=null&&!druidDataSource.isClosed()){
            druidDataSource.close();
        }
    }
}
