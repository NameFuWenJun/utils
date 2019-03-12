package com.fuwenjun.projectUtils;

import java.util.Scanner;

import com.fuwenjun.projectUtils.jdbc.DataBase;
import com.fuwenjun.projectUtils.jdbc.SQLUtils;

public class SqlUtilsTest {
    /**
     * 为了弄清SQlUtils报错原因
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        long start=System.currentTimeMillis();
        
        try{
            for(int i=0;i<3;i++){
            SQLUtils sqlUtils=new SQLUtils(DataBase.MYSQL, "127.0.0.1:3306", "fuwenjun", "root","root");
            String sql="SELECT  id,   title, content, blodDate    FROM fuwenjun.sinablogcrawler where id=9";
            
                sqlUtils.selectWithMap(sql);
                System.out.println(i);
                sqlUtils.close();
            }

        }catch (Exception e) {
           e.printStackTrace();
            long end=System.currentTimeMillis();
            System.out.println(end-start);
        }
        Scanner scan=new Scanner(System.in);
        while(scan.hasNext()){
            System.out.println(scan.nextLine());
        }




    }
}
