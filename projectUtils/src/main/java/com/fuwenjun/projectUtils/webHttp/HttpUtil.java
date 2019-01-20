package com.fuwenjun.projectUtils.webHttp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 发送http请求工具类
 * @author fuwenjun01
 *
 */
public class HttpUtil {
    private static Logger log = LoggerFactory.getLogger(HttpUtil.class);
    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        long start = System.currentTimeMillis();
        String finalUrl = url;
        int queryTime = 0;
        try {
            if(param!=null) {
                if(finalUrl.indexOf("?")==-1) {
                    finalUrl = finalUrl + "?";
                }
                finalUrl = finalUrl + URLEncoder.encode(param, "UTF-8").replace("%3D","=").replace("%26", "&");
            }
            //			log.info(url + "开始");
            URL realUrl = new URL(finalUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(50*1000);

            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            queryTime++;
        } catch (Exception e) {
            e.printStackTrace();
            log.warn("发送GET请求出现异常！" + e + ":"+finalUrl);
            long reStart = System.currentTimeMillis();
            try {
                //等待0.5s再次访问
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            sendGetRetry(finalUrl);
            queryTime++;
            log.error(url+"二次请求耗时"+(System.currentTimeMillis() - reStart));
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                //				e2.printStackTrace();
            }
            //			log.info("请求结束");
            long cost = System.currentTimeMillis() - start;
            log.info("HttpUtil接口"+url+"耗时"+cost+"---次数"+queryTime+"参数"+param);
            //TODO 调用记录、耗时保存到数据库
        }
        return result;
    }

    /**
     * 向指定URL发送GET方法的请求
     * 
     * @param url
     *            发送请求的URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return URL 所代表远程资源的响应结果
     */
    public static String sendGetByCookie(String url, String param, String cookie, String codeF) {
        String result = "";
        BufferedReader in = null;
        long start = System.currentTimeMillis();
        String finalUrl = url;
        int queryTime = 0;
        try {
            if(param!=null) {
                if(finalUrl.indexOf("?")==-1) {
                    finalUrl = finalUrl + "?";
                }
                finalUrl = finalUrl + URLEncoder.encode(param, "UTF-8").replace("%3D","=").replace("%26", "&");
            }
            //			log.info(url + "开始");
            URL realUrl = new URL(finalUrl);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("Cookie", cookie);
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setConnectTimeout(50*1000);
            connection.setReadTimeout(50*1000);

            // 建立实际的连接
            connection.connect();

            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName(codeF)));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
            queryTime++;
        } catch (Exception e) {
            log.warn("发送GET请求出现异常！" + e + ":"+finalUrl);
            long reStart = System.currentTimeMillis();
            try {
                //等待0.5s再次访问
                Thread.sleep(500);
            } catch (InterruptedException e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
            result = sendGetRetry(finalUrl);
            queryTime++;
            log.warn(url+"二次请求耗时"+(System.currentTimeMillis() - reStart));
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                //				e2.printStackTrace();
            }
            //			log.info("请求结束");
            long cost = System.currentTimeMillis() - start;
            log.info("HttpUtil接口"+url+"耗时"+cost+"---次数"+queryTime+"参数"+param);
            //TODO 调用记录、耗时保存到数据库
        }
        return result;
    }


    /**
     * UnknownHostException重试访问
     */
    private static String sendGetRetry(String url) {
        String result = "";
        BufferedReader in = null;
        try {
            if(url.startsWith("http://i.yjapi.com/")) {
                url = url.replaceFirst("i.yjapi.com", "114.55.187.168");
            }
            URL realUrl = new URL(url);

            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setConnectTimeout(1500);
            // 建立实际的连接
            connection.connect();
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.warn(url + "fail again " + e);
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            log.info(url + " 二次请求结束");
        }
        return result;
    }
    /**
     * 向指定 URL 发送POST方法的请求
     * 
     * @param url
     *            发送请求的 URL
     * @param param
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
     * @return 所代表远程资源的响应结果
     */
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");

            conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.warn("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }
    public static String urlEncode(String mes){
        try {
            mes=URLEncoder.encode(mes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mes;
    }

    public static String sendPostByCookie(String url, String param, String cookie, String codeF, java.net.Proxy proxy) throws Exception {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";

        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection conn = (proxy != null) 
                    ? (HttpURLConnection) realUrl.openConnection(proxy) 
                            : (HttpURLConnection) realUrl.openConnection();//(HttpURLConnection) realUrl.openConnection();
                    // 设置通用的请求属性
                    conn.setRequestProperty("accept", "*/*");
                    conn.setRequestProperty("connection", "Keep-Alive");
                    conn.setRequestProperty("Cookie", cookie);
                    conn.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                    // 发送POST请求必须设置如下两行
                    conn.setDoOutput(true);
                    conn.setDoInput(true);
                    conn.setConnectTimeout(5000);
                    conn.setReadTimeout(5000);
                    // 获取URLConnection对象对应的输出流
                    out = new PrintWriter(conn.getOutputStream());
                    // 发送请求参数
                    out.print(param);
                    // flush输出流的缓冲
                    out.flush();
                    // 定义BufferedReader输入流来读取URL的响应
                    //in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    in = new BufferedReader(new InputStreamReader(conn.getInputStream(), Charset.forName(codeF)));
                    String line;
                    while ((line = in.readLine()) != null) {
                        result += line;
                    }
        } catch (Exception e) {
            log.warn("发送 POST 请求出现异常！" + e);
            e.printStackTrace();
            throw e;
        }
        // 使用finally块来关闭输出流、输入流
        finally {
            try {
                if (out != null) {
                    out.close();
                }
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return result;
    }

    public static String sendGet(String url, String param, String accept) {
        String result = "";
        BufferedReader in = null;
        try {
            if(param!=null) {
                if(url.indexOf("?")==-1) {
                    url = url + "?";
                }
                url = url + URLEncoder.encode(param, "UTF-8").replace("%3D","=").replace("%26", "&");
                //				url = url + param;
            }
            log.info(url);
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            HttpURLConnection connection = (HttpURLConnection) realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", accept);
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            connection.setConnectTimeout(1500);
            connection.setUseCaches(false);
            //connection.setReadTimeout(1800000);
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            //			Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            //			for (Entry e:map.entrySet()) {
            //				log.info(e.getKey() + "--->" + e.getValue());
            //			}
            //查看http状态
            //map.get(null);
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            log.warn("发送GET请求出现异常！" + e);
            e.printStackTrace();
        }
        // 使用finally块来关闭输入流
        finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        return result;
    }

}
