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
 * @param url ���������URL
 * @return ��������Ӧ�ַ���
 * @throws Exception
 */
public class HttpUtil {
	
	//����HttpClient����
	public static HttpClient httpClient = new DefaultHttpClient();
	public static final String BASE_URL = "http://10.0.2.2:9080/ServletDemoByEclipse";
	 //
	public static boolean isOnline = true ;//�ж��Ƿ�����
	
	
	
	public static String getRequest(final String url) throws Exception {
		
		httpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);//���ӳ�ʱ
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10000);//���ݴ��䳬ʱ
		
		FutureTask<String> task = new FutureTask<String>(new Callable<String>(){

			@Override
			public String call()  {
				//����HttpGet����
				HttpGet get = new HttpGet(url);
				//����GET����
				HttpResponse httpResponse = null;
				try {
					httpResponse = httpClient.execute(get);
//					HttpParams params = new BasicHttpParams();
//					HttpConnectionParams.setConnectionTimeout(params, 5000);//�������ӳ�ʱ
//					HttpConnectionParams.setSoTimeout(params, 10000);//��������ʱ
				} catch (ClientProtocolException e) {
					//����������
//					isOnline = false;
					Log.e("http","��false��ʱ");
					e.printStackTrace();
				} catch (IOException e) {
//					isOnline = false;
					Log.e("http","���ӳ�ʱ");
					e.printStackTrace();
				}
				//����������ɹ�������Ӧ
				if(httpResponse.getStatusLine().getStatusCode()==200){
					//��ȡ��������Ӧ�ַ���
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
					throw new RuntimeException("����ʧ��");
				}
			}
			
			
			
		});
		new Thread(task).start();
		return task.get();
		
	}
	
	
	
	/**
	 * @param url ����url����
	 * @param params �������
	 * @return ��������Ӧ�ַ���
	 * @throws Exception
	 */
	public static String postRequest(final String url,final Map<String,String> rawParams,Context context) throws Exception {
		
		if (HttpUtil.isNetworkConnected(context) !=false) {
		FutureTask<String> task = new FutureTask<String>(new Callable<String>(){

			@Override
			public String call()  {
				//����HttpPost����
				HttpPost post = new HttpPost(url);
				//������ݲ����Ƚ϶࣬���ԶԴ��ݵĲ������з�װ
				//POST��Ҫͨ��NameValuePair�������ò������ƺ�����Ӧ��ֵ
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				for(String key:rawParams.keySet()){
					//��װ�������
					params.add(new BasicNameValuePair(key,rawParams.get(key)));
					
					
				}
				//�����������
				try {
					post.setEntity(new UrlEncodedFormEntity(params,"utf-8"));
				} catch (UnsupportedEncodingException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				//����post����
				HttpResponse httpResponse = null;
				try {
					httpResponse = httpClient.execute(post);
//					HttpParams params1 = new BasicHttpParams();
//					HttpConnectionParams.setConnectionTimeout(params1, 5000);//�������ӳ�ʱ
//					HttpConnectionParams.setSoTimeout(params1, 10000);//��������ʱ
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					Log.e("http","���ӳ�ʱ");
					e.printStackTrace();
				}
				
				//����������ɹ�������Ӧ
				if(httpResponse.getStatusLine().getStatusCode()==200){
					//��ȡ��������Ӧ�ַ���
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
			Toast.makeText(context,"��û������",Toast.LENGTH_SHORT).show();
		}
		return null;
	}
	
	
	/**
	 * �����������״̬
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
