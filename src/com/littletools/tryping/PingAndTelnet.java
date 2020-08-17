package com.littletools.tryping;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author yangc
 * 测试IP和端口号是否能够连通
 * */
public class PingAndTelnet {
    public static String ip = "172.25.0.2";
    public static int port = 9000;

    public static void main(String[] args) throws Exception {
        /**开一个线程，每隔两分钟测试一次端口和ip*/
        Thread t = new Thread(){
            @Override
            public void run() {
                SimpleDateFormat sdf =new SimpleDateFormat("yyyy-MM-dd HH:mm:ss" );
                while (true){
                    String str = sdf.format(new Date()) + ":" + ping(ip) + "," + telnet(ip,port,2000);
                    System.out.println(str);
                    toTXT(str);
                    try {
                        Thread.sleep(120000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        t.start();
    }

    public static boolean ping(String ipAddress) {
        int timeOut =  3000 ;  //超时应该在3钞以上
        boolean status = false;
        try{
            status = InetAddress.getByName(ipAddress).isReachable(timeOut);
        }catch (Exception e){
            e.printStackTrace();
        }
        // 当返回值是true时，说明host是可用的，false则不可。
        return status;
    }
    public static boolean telnet(String hostname, int port, int timeout){
        Socket socket = new Socket();
        boolean isConnected = false;
        try {
            socket.connect(new InetSocketAddress(hostname, port), timeout); // 建立连接
            isConnected = socket.isConnected(); // 通过现有方法查看连通状态
//            System.out.println(isConnected);    // true为连通
        } catch (IOException e) {
            return false;        // 当连不通时，直接抛异常，异常捕获即可
        }finally{
            try {
                socket.close();   // 关闭连接
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return isConnected;
    }
    public static void toTXT(String str){
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter("d:/checkNCServerLog.txt",true));
            bw.append(str);
            bw.append("\r\n");
            bw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                bw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
