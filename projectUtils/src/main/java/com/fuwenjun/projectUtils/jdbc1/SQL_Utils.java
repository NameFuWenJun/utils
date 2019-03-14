package com.fuwenjun.projectUtils.jdbc1;
/**
 * 优化版查询工具
 * @author fuwenjun01
 *
 */

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidPooledConnection;
import com.fuwenjun.projectUtils.jdbc.DataBase;

public class SQL_Utils {

    private DruidDataSource dataSource=null;
    //数据库连接信息
    private DruidConifg druidConifg=null;
    //默认的连接移除时间
    private long removeAbandonedTimeout=300000l;
    
    private void init(DruidConifg config) throws Exception{
        druidConifg=config;
        dataSource=ConnectDatabase.getDruidDataSource(config);

    }
    public SQL_Utils(DruidConifg config) throws Exception {
        init(config);
    }
    public SQL_Utils(DataBase type,String url,String username,String password) throws Exception{
        DruidConifg config=new DruidConifg(type, url, username, password);
        init(config);
    }

    /**
     * 该upadata包含插入以及更新及其他功能
     * @param sql 具体sql语句
     * @param conditions sql条件
     * @return
     * @throws Exception 
     */
    public int saveOrUpdate(String sql,Object ...conditions ) throws Exception{
        cheackDataSource();
        QueryRunner queryRunner=new QueryRunner(dataSource);
        int result=queryRunner.update(sql,conditions);
        return result;
    }

    public List<Integer> saveOrUpdateWithTransaction(List<SQLStatement> statemets) throws Exception{
        cheackDataSource();
        List<Integer> res=new ArrayList<>();
        QueryRunner queryRunner=new QueryRunner();
        Connection connection=dataSource.getConnection();
        connection.setAutoCommit(false);
        try{
            for(SQLStatement statement:statemets){
                try {
                    if(statement.getParams()!=null){
                        res.add(queryRunner.update(connection, statement.getSql(), statement.getParams()));
                    }else{
                        res.add(queryRunner.update(connection, statement.getSql()));
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    try {
                        connection.rollback();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        }finally {
            try {
                connection.commit();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return res;
    }


    /**
     * 查询一组数据
     * @param sql  :查询语句
     * @return
     * @throws Exception 
     */
    public List<Map<String, Object>> selectWithList(String sql) throws Exception{
        cheackDataSource();
        List<Map<String, Object>>resultMap=new ArrayList<Map<String, Object>>();
        QueryRunner queryRunner=new QueryRunner(dataSource);
        resultMap=queryRunner.query(sql, new MapListHandler());
        return resultMap;
    }

    /**
     * 查询一组数据带有参数
     * @param sql  :查询语句,参数放入condition数组中
     * @return 
     * @throws Exception 
     */
    public List<Map<String, Object>> selectWithListAndCondition(String sql,Object ... conditions) throws Exception{
        cheackDataSource();
        List<Map<String, Object>>resultMap=new ArrayList<Map<String, Object>>();
        QueryRunner queryRunner=new QueryRunner(dataSource);
        resultMap=queryRunner.query(sql,new MapListHandler(),conditions);
        return resultMap;
    }



    private void cheackDataSource() throws Exception{
        //数据连接池如果被关闭,可能被意外关闭
        if(dataSource.isClosed()){
            //重新初始化DataSource
            init(druidConifg);
        }
    }


    public DruidConifg getDruidConifg() throws Exception {
        cheackDataSource();
        return druidConifg;
    }
    /**
     * sql操作执行完毕以后关闭driud数据连接
     * @throws SQLException
     */
    public void close() throws SQLException{
        if(dataSource!=null&&!dataSource.isClosed()){
            dataSource.close();
        }
    }
    
}
