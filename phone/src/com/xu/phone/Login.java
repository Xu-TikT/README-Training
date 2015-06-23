package com.xu.phone;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends Activity implements OnClickListener {

	private EditText loginUsername;
	private EditText loginPassword;
	private Button loginButton;
//	private Button createButton;
	//
	public static int jifen;
	public static String realname;
	public static String userid;
	public static String addr;
	public static String age;
	public static String dengji;
	//
	JSONObject jobc;
	private ProgressDialog loginProgress;
	public static Map<String,String> map_per = new HashMap();

	public static final int MSG_LOGIN_RESULT = 0;

	public String serverUrl = "http://172.20.10.2:8080/ServletDemoByEclipse/LoginMes";

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case MSG_LOGIN_RESULT:
//				loginProgress.dismiss();
				JSONObject json = (JSONObject) msg.obj;
				handleLoginResult(json);
				break;
			}
		};
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.user_login);

		initViews();
	}

	private void initViews() {
		loginUsername = (EditText) findViewById(R.id.id_login_name);
		loginPassword = (EditText) findViewById(R.id.id_user_pass);
		loginButton = (Button) findViewById(R.id.id_user_login);
//		createButton = (Button) findViewById(R.id.id_user_reg);

		loginButton.setOnClickListener(this);
//		createButton.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_user_login:
			handleLogin();
			
			break;
//		case R.id.id_user_reg:
//			handleCreateCount();
//			break;
		default:
			break;
		}

	}

	private void handleLogin() {
		String user_id = loginUsername.getText().toString();
		String password = loginPassword.getText().toString();
		login(user_id, password);
	}

	
	private void login(final String user_id, final String password) {
//		 loginProgress = new ProgressDialog(this);
//		 loginProgress.setCancelable(false);
//		 loginProgress.setCanceledOnTouchOutside(false);
//		 ProgressDialog.show(this, null, "登陆中...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.d("test", "start network!");
				
				// }// 返回的json数据对象
				HttpClient client = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(serverUrl);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				Map<String, String> map1 = new HashMap();
				map1.put("id", user_id);
				params.add(new BasicNameValuePair("id", user_id));
				params.add(new BasicNameValuePair("password", password));

				HttpResponse httpResponse = null;
				Log.d("test", "client OK!");
				Log.i("test", params.toString());//查看输出的参数
				try {
					
					httpPost.setEntity(new UrlEncodedFormEntity(params,
							HTTP.UTF_8));
					Log.i("test", "1");
					httpResponse = client.execute(httpPost);
					Log.i("test", "2");
					//网络请求
					if (httpResponse.getStatusLine().getStatusCode() == 200) {
						Log.d("test", "network OK!");

						HttpEntity entity = httpResponse.getEntity();
						String entityString = EntityUtils.toString(entity);

						String jsonString = entityString.substring(entityString
								.indexOf("{"));
						//Log.d("test", "entity = " + jsonString);

						JSONObject jobc = new JSONObject(jsonString);
						JSONArray jarr = jobc.getJSONArray("user");
						for(int i=0;i<jarr.length();i++){
						JSONObject json = jarr.getJSONObject(i);
						if(json.getString("user_id").equals(user_id)){
							//jie xi JSON 
							jifen = json.getInt("jifen");
							userid = json.getString("user_id");
							realname = json.getString("name");
							addr = json.getString("addr");
							age = json.getString("age");
							if(jifen<=500){
								dengji = "0.99 折";
							}else if(jifen>500&&jifen<=1500){
								dengji = "0.98 折";
							}else if(jifen>1500&&jifen<=5000){
								dengji = "0.97 折";
							}
							sendMessage(MSG_LOGIN_RESULT, json);
							Log.d("test", "json = " + json);
							
						}else{
//							//用户名不存在
//							Log.d("test", "error");
//							Map<String,Integer> map_err = new HashMap();
//							map_err.put("result_code", 2);
//							sendMessage(MSG_LOGIN_RESULT, new JSONObject(map_err));
//							handleLoginResult(new JSONObject(map_err));
						}
						
						}
						

					} else {
						Log.i("test", httpResponse.getStatusLine()
								.getStatusCode() + "");
					}
				} catch (UnsupportedEncodingException e) {
					Log.d("test", "UnsupportedEncodingException");
					e.printStackTrace();
				} catch (ClientProtocolException e) {
					Log.d("test", "ClientProtocolException");
					e.printStackTrace();
				} catch (IOException e) {
					Log.d("test", "IOException");

					e.printStackTrace();
				}catch (JSONException e) {
					Log.d("test", "JSONException");

					e.printStackTrace();
				}
			}
		}).start();

	}

	private void handleCreateCount() {
		Intent intent = new Intent(this, CreateUserActivity.class);
		startActivity(intent);
		finish();
	}

	private void handleLoginResult(JSONObject json) {
		/*
		 * login_result: -1：登陆失败，未知错误！ 0: 登陆成功！ 1：登陆失败，用户名或密码错误！ 2：登陆失败，用户名不存在！
		 */
		int resultCode = -1;
		try {
			resultCode = json.getInt("result_code");
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		switch (resultCode) {
		case 0:
			//登录成功
			MainActivity.isLogin = true;
			onLoginSuccess(json);
			break;
		case 1:
			Toast.makeText(this, "用户名或密码错误！", Toast.LENGTH_LONG).show();
			break;
		case 2:
			Toast.makeText(this, "用户名不存在！", Toast.LENGTH_LONG).show();
			break;
		case -1:
		default:
			Toast.makeText(this, "登陆失败！未知错误！", Toast.LENGTH_LONG).show();
			break;
		}
	}

	private void onLoginSuccess(JSONObject json) {
		if(Shop_Detail.fromSD==false){
		Intent intent = new Intent(Login.this, Personal.class);
		startActivity(intent);
		overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

		try {
			
			map_per.put("user_id",json.getString("user_id").toString());
			Log.i("id",json.getString("user_id").toString());
//			intent.putExtra("user_id", json.getString("user_id"));
//			intent.putExtra("gender", json.getString("gender"));
//			intent.putExtra("age", json.getInt("age"));
//			intent.putExtra("phone", json.getString("phone"));
//			intent.putExtra("email", json.getString("email"));
		} catch (JSONException e) {
			
			e.printStackTrace();
		}
		startActivity(intent);
		finish();
		}else{
			//登录后返回SD
			Intent intent = new Intent(Login.this, Shop_Detail.class);
			startActivity(intent);
			Shop_Detail.fromSD = false;
		}
	}

	private void sendMessage(int what, Object obj) {
		
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = obj;
		mHandler.sendMessage(msg);
		Log.i("sendMessage","OK");
	}

}
