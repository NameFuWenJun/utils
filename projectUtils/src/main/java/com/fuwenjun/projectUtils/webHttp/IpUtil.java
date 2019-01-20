package com.fuwenjun.projectUtils.webHttp;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 获取ip地址工具类
 * @author fuwenjun01
 *
 */
public class IpUtil {
    private static Logger log = LoggerFactory.getLogger(IpUtil.class);
    public static boolean isWindowsOS() {
        boolean isWindowsOS = false;
        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") > -1) {
            isWindowsOS = true;
        }
        return isWindowsOS;
    }
    public static String getLocalIP() {
        if (isWindowsOS()) {
            try {
                return InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                log.warn("获取windows Ip 失败");
                e.printStackTrace();
            }
        } else {
            try {
                return getLinuxLocalIp();
            } catch (SocketException e) {
                // TODO Auto-generated catch block
                log.warn("获取服务器 Ip 失败");
                e.printStackTrace();
            }
        }
        log.warn("获取 Ip 失败");
        return "127.0.0.1";
    }
    private static String getLinuxLocalIp() throws SocketException {
        String ip = "";
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                String name = intf.getName();
                if (!name.contains("docker") && !name.contains("lo")) {
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress()) {
                            String ipaddress = inetAddress.getHostAddress().toString();
                            if (!ipaddress.contains("::") && !ipaddress.contains("0:0:") && !ipaddress.contains("fe80")) {
                                ip = ipaddress;
                            }
                        }
                    }
                }
            }
        } catch (SocketException ex) {
            ip = "127.0.0.1";
            log.warn("获取服务器 Ip失败");
            ex.printStackTrace();
        }
        return ip;
    }
}
