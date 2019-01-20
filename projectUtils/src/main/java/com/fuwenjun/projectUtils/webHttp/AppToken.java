package com.fuwenjun.projectUtils.webHttp;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
/**
 * 生成唯一密钥工具类
 * @author fuwenjun01
 *
 */
public class AppToken {
	//公共密钥客户端不会知道
    public static String SECRET="Application";
 
    public static  String  createToken() throws UnsupportedEncodingException {
        //签名发布时间
        Long iatDate=new Date().getTime();
        System.out.println(iatDate);
 
        Map<String,Object> map=new HashMap<String, Object>();
        map.put("alg","HS256");//设置算法 为HS256
        map.put("typ","JWT");//设置类型为JWT
        String token=JWT.create().withHeader(map)
                .sign(Algorithm.HMAC256(SECRET + iatDate.toString()+"inspur"+new Random().nextInt(999999999))).substring(41,61);//用公共密钥加密
       return token;
    }
 
    public static void main(String[] args) throws UnsupportedEncodingException {
    	for (int i =0;i<10;i++){
	    	 String token=createToken();
	         System.out.println("Token:"+token);
    	}
	}
       
}