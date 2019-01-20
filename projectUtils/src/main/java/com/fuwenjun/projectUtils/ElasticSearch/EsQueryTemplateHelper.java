package com.fuwenjun.projectUtils.ElasticSearch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


/**
 * 
 * @author maolh
 *
 */
public class EsQueryTemplateHelper {
	
	//读取模板文件
	public static String load(String fileName) throws IOException{
		InputStream  is = EsQueryTemplateHelper.class.getResourceAsStream(fileName);
		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuffer content = new StringBuffer();
		try {
			String line = null;
			while((line = reader.readLine())!=null){
				content.append(line);
				content.append("\r\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			is.close();
		}
		return content.toString();
	}

}
