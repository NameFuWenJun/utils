package com.fuwenjun.projectUtils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.sql.SQLUtils;
import com.fuwenjun.projectUtils.webHttp.HttpUtil;

public class MailTest {
	public static void main(String[] args) {
	    String to="15061885056@163.com";
		String title="html测试";                                                                                                                                                                                
		String message="<html><head>                                                                                                                                                                            "
				+"<base target='_blank'>                                                                                                                                                                                        "
				+"<style type='text/css'>                                                                                                                                                                                       "
				+"::-webkit-scrollbar{ display: none; }                                                                                                                                                                         "
				+"</style>                                                                                                                                                                                                      "
				+"<style id='cloudAttachStyle' type='text/css'>                                                                                                                                                                 "
				+"#divNeteaseBigAttach, #divNeteaseBigAttach_bak{display:none;}                                                                                                                                                 "
				+"</style>                                                                                                                                                                                                      "
				+"                                                                                                                                                                                                              "
				+"</head>                                                                                                                                                                                                       "
				+"<body tabindex='0' role='listitem'>                                                                                                                                                                           "
				+"                                                                                                                                                                                                              "
				+"                                                                                                                                                                                                              "
				+"                                                                                                                                                                                                              "
				+"                                                                                                                                                                                                              "
				+"                                                                                                                                                                                                              "
				+"                                                                                                                                                                                                              "
				+"  <style type='text/css'>.view{padding:0;word-wrap:break-word;cursor:text;height:90%;}body{margin:8px;font-family:sans-serif;font-size:16px;}p{margin:5px 0;}</style>                                         "
				+"                                                                                                                                                                                                              "
				+"  <style id='tablesort'>table.sortEnabled tr.firstRow th,table.sortEnabled tr.firstRow td{padding-right:20px;background-repeat: no-repeat;background-position: center right;   background-image:url           "
				+ "(http://localhost:8080/alertPlatform/skins/js/emailTemplate/themes/default/images/sortable.png);}</style>                                                                                                    "
				+"  <style id='table'>.selectTdClass{background-color:#edf5fa !important}table.noBorderTable td,table.noBorderTable th,table.noBorderTable caption{border:1px dashed #ddd !important}table{margin-bottom:10px;  "
				+ "border-collapse:collapse;display:table;}td,th{padding: 5px 10px;border: 1px solid #DDD;}caption{border:1px dashed #DDD;border-bottom:0;padding:3px;text-align:center;}th{border-top:1px solid #BBB;background"
				+ "lor:#F7F7F7;}table tr.firstRow th{border-top-width:2px;}.ue-table-interlace-color-single{ background-color: #fcfcfc; } .ue-table-interlace-color-double{ background-color: #f7faff; }td p{margin:0;padding  "
				+ ":0;}</style>                                                                                                                                                                                                 "
				+"  <style id='pagebreak'>.pagebreak{display:block;clear:both !important;cursor:default !important;width: 100% !important;margin:0;}</style>                                                                    "
				+"  <style id='pre'>pre{margin:.5em 0;padding:.4em .6em;border-radius:8px;background:#f8f8f8;}</style>                                                                                                          "
				+"  <style type='text/css'>body{background-color:#ffffff; background-image:; background-repeat:repeat; background-position:0% 0%; height:311px; }</style>                                                       "
				+"                                                                                                                                                                                                              "
				+"                                                                                                                                                                                                              "
				+"  <p>这里是样例的模板解析<br></p>                                                                                                                                                                             "
				+"  <table>                                                                                                                                                                                                     "
				+"   <tbody>                                                                                                                                                                                                    "
				+"    <tr class='firstRow'>                                                                                                                                                                                     "
				+"     <td width='234' valign='top' style='word-break: break-all;'>样例数据</td>                                                                                                                                "
				+"     <td width='234' valign='top' style='word-break: break-all;'>下载数</td>                                                                                                                                  "
				+"     <td width='234' valign='top' style='word-break: break-all;'>空值数</td>                                                                                                                                  "
				+"    </tr>                                                                                                                                                                                                     "
				+"    <tr>                                                                                                                                                                                                      "
				+"     <td width='234' valign='top' style='word-break: break-all;'>testData</td>                                                                                                                                "
				+"     <td width='234' valign='top' style='word-break: break-all;'>213</td>                                                                                                                                     "
				+"     <td width='234' valign='top' style='word-break: break-all;'>123</td>                                                                                                                                     "
				+"    </tr>                                                                                                                                                                                                     "
				+"   </tbody>                                                                                                                                                                                                   "
				+"  </table>                                                                                                                                                                                                    "
				+"  <p>下面是测试表格数据</p>                                                                                                                                                                                   "
				+"  <p></p><table><tbody><tr><th>测试</th><th>ces</th></tr><tr><td>test1</td><td>test1</td></tr><tr><td>test2</td><td>test2</td></tr></tbody></table>                                                           "
				+"  <p><br></p>                                                                                                                                                                                                 "
				+"  <p><br></p>                                                                                                                                                                                                 "
				+"  <p><br></p>                                                                                                                                                                                                 "
				+"  <p><br></p>                                                                                                                                                                                                 "
				+"  <p><br></p>                                                                                                                                                                                                 "
				+"                                                                                                                                                                                                              "
				+"                                                                                                                                                                                                              "
				+"                                                                                                                                                                                                              "
				+"<style type='text/css'>                                                                                                                                                                                       "
				+"body{font-size:14px;font-family:arial,verdana,sans-serif;line-height:1.666;padding:0;margin:0;overflow:auto;white-space:normal;word-wrap:break-word;min-height:100px}                                         "
				+"td, input, button, select, body{font-family:Helvetica, 'Microsoft Yahei', verdana}                                                                                                                            "
				+"pre {white-space:pre-wrap;white-space:-moz-pre-wrap;white-space:-pre-wrap;white-space:-o-pre-wrap;word-wrap:break-word;width:95%}                                                                             "
				+"th,td{font-family:arial,verdana,sans-serif;line-height:1.666}                                                                                                                                                 "
				+"img{ border:0}                                                                                                                                                                                                "
				+"header,footer,section,aside,article,nav,hgroup,figure,figcaption{display:block}                                                                                                                               "
				+"blockquote{margin-right:0px}                                                                                                                                                                                  "
				+"</style>                                                                                                                                                                                                      "
				+"                                                                                                                                                                                                              "
				+"                                                                                                                                                                                                              "
				+"                                                                                                                                                                                                              "
				+"                                                                                                                                                                                                              "
				+"                                                                                                                                                                                                              "
				+"                                                                                                                                                                                                              "
				+"<style id='ntes_link_color' type='text/css'>a,td a{color:#064977}</style>                                                                                                                                     "
				+"                                                                                                                                                                                                              "
				+"</body></html>'                                                                                                                                                                                               ";
		try {
			to=URLEncoder.encode(to, "UTF-8");
			title=URLEncoder.encode(title, "UTF-8");
			message=URLEncoder.encode(message,"UTF-8" );
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String query="to="+to+"&title="+title+"&message="+message;
		HttpUtil.sendPost("http://127.0.0.1:8081/sendMail/sendHtilMailByPost", query);
	}
}
