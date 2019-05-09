package com.xy.utility;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;

public class HttpURLConnectionUtil {
    private static Logger logger = LoggerFactory.getLogger(HttpURLConnectionUtil.class);
    private static Charset charset = Charset.forName("UTF-8");

    public static HttpURLConnection sendMethodEncrypt(String url, Map<String, String> reqHeaders, byte[] reqBodyBytes) {
        HttpURLConnection conn = null;
        OutputStream out = null;
        try {
            // 创建HttpURLConnection
            URL realUrl = new URL(url);
            conn = (HttpURLConnection) realUrl.openConnection();
            // 设置HttpURLConnection参数
            conn.setRequestMethod("POST");// 设定POST请求，默认为GET请求
            conn.addRequestProperty("encrypt","2");
            conn.addRequestProperty("encrypt","6");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(60000);// 设置连接主机超时MS
            conn.setReadTimeout(60000);// 设置从主机读取数据超时MS
            logger.info("RequestHeaders:"+conn.getRequestProperties().toString());
            if (reqHeaders != null) {// 设置请求消息头
                Iterator<Map.Entry<String, String>> it = reqHeaders.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            conn.connect();

            if (reqBodyBytes != null) {// 设置请求消息体
                out = conn.getOutputStream();// 创建输出流对象，此处会隐含的进行connect
                out.write(reqBodyBytes);// String转化为Byte数组，向对象输出流写入数据
                out.flush();// 刷新对象输出流
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }
    /**
     * 1.读取URL 2.将URL转变为URL类对象 3.打开URL连接
     *
     * @param url
     * @return
     */
    public static HttpURLConnection sendMethod(String url, Map<String, String> reqHeaders, byte[] reqBodyBytes) {
        HttpURLConnection conn = null;
        OutputStream out = null;
        try {
            // 创建HttpURLConnection
            URL realUrl = new URL(url);
            conn = (HttpURLConnection) realUrl.openConnection();
            // 设置HttpURLConnection参数
            conn.setRequestMethod("POST");// 设定POST请求，默认为GET请求
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(60000);// 设置连接主机超时MS
            conn.setReadTimeout(60000);// 设置从主机读取数据超时MS
            //conn.connect();
            if (reqHeaders != null) {// 设置请求消息头
                Iterator<Map.Entry<String, String>> it = reqHeaders.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            if (reqBodyBytes != null) {// 设置请求消息体
                out = conn.getOutputStream();// 创建输出流对象，此处会隐含的进行connect
                out.write(reqBodyBytes);// String转化为Byte数组，向对象输出流写入数据
                out.flush();// 刷新对象输出流
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static HttpURLConnection sendMethod(String url, Map<String, String> reqHeaders, String reqBody) {
        HttpURLConnection conn = null;
        OutputStream out = null;
        try {
            // 创建HttpURLConnection
            URL realUrl = new URL(url);
            conn = (HttpURLConnection) realUrl.openConnection();
            // 设置HttpURLConnection参数
            conn.setRequestMethod("POST");// 设定POST请求，默认为GET请求
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setConnectTimeout(60000);// 设置连接主机超时MS
            conn.setReadTimeout(60000);// 设置从主机读取数据超时MS
            // conn.connect();
            if (reqHeaders != null) {// 设置请求消息头
                Iterator<Map.Entry<String, String>> it = reqHeaders.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry<String, String> entry = it.next();
                    conn.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            if (reqBody != null) {// 设置请求消息体
                out = conn.getOutputStream();// 创建输出流对象，此处会隐含的进行connect
                out.write(reqBody.getBytes("UTF-8"));// String转化为Byte数组，向对象输出流写入数据
                out.flush();// 刷新对象输出流
            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static byte[] receiveMethod(HttpURLConnection conn) {
        InputStream is = null;
        try {
            is = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return getRespBody(is);
    }

    public static byte[] getRespBody(InputStream is) {
        byte[] buffer = new byte[1024 * 2];
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        DataInputStream dis = new DataInputStream(is);
        try {
            int len = -1;
            while ((len = dis.read(buffer)) > 0) {
                bos.write(buffer, 0, len);
            }
            buffer = bos.toByteArray();
            return buffer;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            close(dis, bos);
        }
    }

    public static void close(Closeable... closeses) {
        try {
            for (Closeable close : closeses) {
                if (null != close) {
                    close.close();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
