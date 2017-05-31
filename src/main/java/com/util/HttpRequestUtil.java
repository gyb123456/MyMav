package com.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class HttpRequestUtil {
	
	/*测试方法*/
	public static void test() {
		String url = "http://file.api.weixin.qq.com/cgi-bin/media/get";
		String param = "access_token=ACCESS_TOKEN&media_id=MEDIA_ID";
        //发送 GET 请求
        String get=sendGet(url, param);
        System.out.println(get);
        
        //发送 POST 请求
        String post=sendPost(url, param);;
        System.out.println(post);
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
    public static String sendGet(String url, String param) {
        String result = "";
        BufferedReader in = null;
        try {
            String urlNameString = url + "?" + param;
            URL realUrl = new URL(urlNameString);
            // 打开和URL之间的连接
            URLConnection connection = realUrl.openConnection();
            // 设置通用的请求属性
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 建立实际的连接
            connection.connect();
            // 获取所有响应头字段
            Map<String, List<String>> map = connection.getHeaderFields();
            // 遍历所有的响应头字段
            for (String key : map.keySet()) {
                System.out.println(key + "--->" + map.get(key));
            }
            // 定义 BufferedReader输入流来读取URL的响应
            in = new BufferedReader(new InputStreamReader(
                    connection.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送GET请求出现异常！" + e);
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
            URLConnection conn = realUrl.openConnection();
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
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.close();
                }
                if(in!=null){
                    in.close();
                }
            }
            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }  
    
  /*本项目用此方法下载图片，返回图片名称*/  
  public static String download(String urlString,String savePath) throws IOException{  
//	  urlString =  "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=o459FEdpKn5NybBGIKb3IScGcONN1Uve6Zvv_64gnf4zfrg-QhtaO6btPP-adYN_e_VGaNVFzqgI1O16ff0o7i1kViu-9eJY3Gjdzh5CnynGc7djLOVW3Ydfj17el5d0GJNdAEARUY&media_id=6UN56C7xULoNOo4eVS1_eBX0nQaC6sSGsZlnzU8i1RxUYOp5UQrUeX9r3rS8Jlfu";
	  System.out.println("urlString===="+urlString);
	  InputStream is = null;  
	  String imageName = "";
	  
	  
      // 构造URL  
      /* URL url = new URL(urlString);  
      // 打开连接  
      URLConnection con = url.openConnection();  
      //设置请求超时为5s  
      con.setConnectTimeout(5*1000);  
      // 输入流  
      is = con.getInputStream(); 
      Map<String, List<String>> headerFields = con.getHeaderFields(); */
      URL urlGet = new URL(urlString);  
      HttpURLConnection http = (HttpURLConnection) urlGet  
              .openConnection();  
      http.setRequestMethod("GET"); // 必须是get方式请求  
      http.setRequestProperty("Content-Type",  
              "application/x-www-form-urlencoded");  
      http.setDoOutput(true);  
      http.setDoInput(true);  
      System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒  
      System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒  
      http.connect();  
      // 获取文件转化为byte流  
      is = http.getInputStream();
      Map<String, List<String>> headerFields = http.getHeaderFields();
      
      
      
      Map<String, String> map = new HashMap<String, String>();
	  Set<Entry<String, List<String>>> entrySet = headerFields.entrySet();
	  Iterator<Entry<String, List<String>>> iterator = entrySet.iterator();
	  while(iterator.hasNext()) {
			Entry<String, List<String>> next = iterator.next();
			String key=next.getKey();
			List<String> value = next.getValue();
			if(key==null)
				System.out.println(value.toString());
			else{
				System.out.println(key+":"+value.toString());
				map.put(key, value.toString());
			}
	  }
	  if(map.get("Content-Type").equals("[text/plain]")){
		  System.out.println("media_id无效=================");
		  return null;
	  }
      
	  
	  
	  
	// 1K的数据缓冲  
      byte[] bs = new byte[1024];  
      // 读取到的数据长度  
      int len;  
      // 输出的文件流  
     File sf=new File(savePath);  
     if(!sf.exists()){  
         sf.mkdirs();  
     }  
     Date date = new Date();
	 long times = date.getTime();
     imageName = String.valueOf(times) + ".jpg";  
     OutputStream os = new FileOutputStream(sf.getPath()+"/"+imageName);  
      // 开始读取  
      while ((len = is.read(bs)) != -1) {  
        os.write(bs, 0, len);  
      }  
      // 完毕，关闭所有链接  
      os.close();  
      is.close();  
  	  System.out.println("imageName ======"+imageName);
      return imageName;
  } 
    
    
}
