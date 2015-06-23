package com.xu.phone;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;
/**
 * @author XU
 * @param url 发送请求的URL
 * @return 服务器响应字符串
 * @throws Exception
 */
public class HttpUtil {
	
	//创建HttpClient对象
	public static HttpClient httpClient = new DefaultHttpClient();
	public static final String BASE_URL = "http://10.0.2.2:9080/ServletDemoByEclipse";
	 //
	public static boolean isOnline = true ;//判断是否联网
	
	
	
	public static String getRequest(final String url) throws Exception {
		
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);//连接超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);//数据传输超时
		
		FutureTask<String> task = new FutureTask<String>(new Callable<String>(){

			@Override
			public String call()  {
				//创建HttpGet对象
				HttpGet get = new HttpGet(url);
				//发送GET请求
				HttpResponse httpResponse = null;
				try {
					httpResponse = httpClient.execute(get);
//					HttpParams params = new BasicHttpParams();
//					HttpConnectionParams.setConnectionTimeout(params, 5000);//设置连接超时
//					HttpConnectionParams.setSoTimeout(params, 10000);//设置请求超时
				} catch (ClientProtocolException e) {
					//网络无连接
//					isOnline = false;
					Log.e("http","连false超时");
					e.printStackTrace();
				} catch (IOException e) {
//					isOnline = false;
					Log.e("http","连接超时");
					e.printStackTrace();
				}
				//如果服务器成功返回响应
				if(httpResponse.getStatusLine().getStatusCode()==200){
					//获取服务器响应字符串
					String result = null;
					try {
						result = EntityUtils.toString(httpResponse.getEntity());
					} catch (ParseException e) {
						
						e.printStackTrace();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					return result;
					
				}else{
					throw new RuntimeException("请求失败");
				}
			}
			
			
			
		});
		new Thread(task).start();
		return task.get();
		
	}
	
	
	
	/**
	 * @param url 发送url请求
	 * @param params 请求参数
	 * @return 服务器响应字符串
	 * @throws Exception
	 */
	public static String postRequest(final String url,final Map<String,String> rawParams,Context context) throws Exception {
		
		if (HttpUtil.isNetworkConnected(context) !=false) {
		FutureTask<String> task = new FutureTask<String>(new Callable<String>(){

			@Override
			public String call()  {
				//创建HttpPost对象
				HttpPost post = new HttpPost(url);
				//如果传递参数比较多，可以对传递的参数进行封装
				//POST需要通过NameValuePair类来设置参数名称和它对应的值
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				for(String key:rawParams.keySet()){
					//封装请求参数
					params.add(new BasicNameValuePair(key,rawParams.get(key)));
					
					
				}
				//设置请求参数
				try {
					post.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//发送post请求
				HttpResponse httpResponse = null;
				try {
					httpResponse = httpClient.execute(post);
//					HttpParams params1 = new BasicHttpParams();
//					HttpConnectionParams.setConnectionTimeout(params1, 5000);//设置连接超时
//					HttpConnectionParams.setSoTimeout(params1, 10000);//设置请求超时
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e("http","连接超时");
					e.printStackTrace();
				}
				
				//如果服务器成功返回响应
				if(httpResponse.getStatusLine().getStatusCode()==200){
					//获取服务器响应字符串
					String result = null;
					try {
						result = EntityUtils.toString(httpResponse.getEntity());
					} catch (ParseException e) {
						
						e.printStackTrace();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					
					
					return result;
					
				}
				return null;
			}
			
			
			
		});
		
		new Thread(task).start();
		return task.get();
		} else {
			Toast.makeText(context,"你没有联网",Toast.LENGTH_SHORT).show();
		}
		return null;
	}
	
	
	/**
	 * 检查网络连接状态
	 * 
	 * */
	public static boolean isNetworkConnected(Context context) {
		if (context != null) {
			ConnectivityManager mConnectivityManager = (ConnectivityManager) context
					.getSystemService(Context.CONNECTIVITY_SERVICE);
			NetworkInfo mNetworkInfo = mConnectivityManager
					.getActiveNetworkInfo();
			if (mNetworkInfo != null) {



				return mNetworkInfo.isAvailable();
			}
		}
		return false;
	}
	
}
