package com.fuwenjun.projectUtils.jdbc1;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import com.fuwenjun.projectUtils.jdbc.DataBase;

public class Test {
    public static void TranscationTest() throws Exception{
        SQL_Utils sql_Utils=new SQL_Utils(DataBase.MYSQL, "127.0.0.1:3306/fuwenjun", "root", "root");
     //   SQL_Utils sql_Utils1=new SQL_Utils(DataBase.ORACLE, "127.0.0.1:1521:orcl", "scott", "tiger");
        String sql2="INSERT INTO fuwenjun.test_month_201901 (id, NAME, age) values (9,'fwj',2)";
        String sql1="INSERT INTO fuwenjun.test_month_201901 (id, NAME, age) values (8,'fwj',2)";
            try {
                for(int i=30;i<100;i++){
                    sql_Utils.saveOrUpdate("INSERT INTO fuwenjun.test_month_201901 (id, NAME, age) values ("+i+",'fwj',2)", null);
                    Thread.sleep(30000);
                }
                
            } catch (Exception e) {
                e.printStackTrace();
            }
/*        new Thread(()->{
            for(int i=0;i<4;i++){
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                sql_Utils.autoClose();
            }
        }).start();
*/     //   System.out.println(sql_Utils.getDruidConifg()==sql_Utils1.getDruidConifg());
        /*String sql="INSERT INTO fuwenjun.test_month_201901 (id, NAME, age) values (1,'fwj',2)";
        String sql1="INSERT INTO fuwenjun.test_month_201901 (id, NAME, age) values (2,'fwj',2)";
   
        SQLStatement stat1=new SQLStatement(sql);
        SQLStatement stat2=new SQLStatement(sql1);
       
        List<SQLStatement> stats=new ArrayList<>();
        stats.add(stat1);
        stats.add(stat2);
        sql_Utils.saveOrUpdateWithTransaction(stats);*/
                
    }
    
    
    public static void main(String[] args) throws Exception {
        TranscationTest();
    }
}
