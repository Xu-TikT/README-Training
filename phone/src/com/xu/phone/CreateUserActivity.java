package com.xu.phone;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.MailTo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class CreateUserActivity extends Activity implements OnClickListener {
	
	public static final String CREATE_ACCOUNT_URL = "http://192.168.253.1:8080/ServletDemoByEclipse/NewAccount";
	public static final int MSG_CREATE_RESULT = 1;

	private EditText eUser_id;
	private EditText ePwd1;
	private EditText ePwd2;
	private RadioGroup rGender;
	private EditText eAge;
	private EditText ePhone;
	private EditText eRealName;
//	private EditText eRealName;
	
	private Button btnSubmit;
	private Button btnReset;
	
	ProgressDialog progress;
	
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch(msg.what) {
			case MSG_CREATE_RESULT:
				progress.dismiss();
				JSONObject json = (JSONObject) msg.obj;
				hanleCreateAccountResult(json);
				break;
			}
		}	
	};
	
	private void hanleCreateAccountResult(JSONObject json) {
		/*
		 *   result_code: 
		 * 0  ע��ɹ�
		 * 1  �û����Ѵ���
		 * 2 ���ݿ�����쳣
		 * */
		int result;
		try {
			result = json.getInt("result_code");
		} catch (JSONException e) {
			Toast.makeText(this, "û�л�ȡ���������Ӧ��", Toast.LENGTH_LONG).show();
			e.printStackTrace();
			return;
		}
		
		if(result == 1) {
			Toast.makeText(this, "�û����Ѵ��ڣ�", Toast.LENGTH_LONG).show();
			return;
		}
		
		if(result == 2) {
			Toast.makeText(this, "ע��ʧ�ܣ�����˳����쳣��", Toast.LENGTH_LONG).show();
			return;
		}
		
		if(result == 0) {
			Toast.makeText(this, "ע��ɹ���ǰ����½���棡", Toast.LENGTH_LONG).show();
			Intent intent = new Intent(this, Login.class);
			startActivity(intent);
			finish();
			return;
		}
		
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.create_user_activity);
		
		initViews();
	}
	
	private void initViews() {
		eUser_id = (EditText)findViewById(R.id.new_username);
		ePwd1 = (EditText)findViewById(R.id.new_password_1);
		ePwd2 = (EditText)findViewById(R.id.new_password_2);
		eRealName = (EditText) findViewById(R.id.id_create_realname);
		rGender = (RadioGroup)findViewById(R.id.new_radio_group_gender);
		eAge = (EditText)findViewById(R.id.new_age);
		ePhone = (EditText)findViewById(R.id.new_phone);
		eRealName = (EditText)findViewById(R.id.new_email);
		btnSubmit = (Button)findViewById(R.id.new_btn_submit);
		btnReset = (Button)findViewById(R.id.new_btn_reset);
		btnSubmit.setOnClickListener(this);
		btnReset.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()) {
		case R.id.new_btn_submit:
			handleCreateAccount();
			break;
		case R.id.new_btn_reset:
			handleReset();
			break;
		}
		
	}

	private void handleCreateAccount() {
		boolean isUsernameValid = checkUsername();
		if(!isUsernameValid) {
			Toast.makeText(this, "�û�������ȷ������������", Toast.LENGTH_LONG).show();
			return;
		}
		
		int pwdResult = checkPassword();
		if(pwdResult == 1) {
			Toast.makeText(this, "������������벻һ�£���ȷ�ϣ�", Toast.LENGTH_LONG).show();
			return;
		} 
		if (pwdResult == 2) {
			Toast.makeText(this, "���벻��Ϊ�գ�", Toast.LENGTH_LONG).show();
			return;
		}
		
		int isAgeValid = checkAge();
		if(isAgeValid == -1) {
			Toast.makeText(this, "���䲻��Ϊ�գ�", Toast.LENGTH_LONG).show();
			return;
		}
		if(isAgeValid == -2) {
			Toast.makeText(this, "���䳬����Χ(1~100)��", Toast.LENGTH_LONG).show();
			return;
		}
		if(isAgeValid == -3) {
			Toast.makeText(this, "�����ʽ��������벻Ҫ������ĸ�����ŵ������ַ�����", Toast.LENGTH_LONG).show();
			return;
		}
		
		if(TextUtils.isEmpty(ePhone.getText().toString())) {
			Toast.makeText(this, "������绰���룡", Toast.LENGTH_LONG).show();
			return;
		}
		
		if(TextUtils.isEmpty(eRealName.getText().toString())) {
			Toast.makeText(this, "�������ַ��", Toast.LENGTH_LONG).show();
			return;
		}
		
		createAccount();
	}

	private void createAccount() {
		progress = new ProgressDialog(this);
		progress.setCancelable(false);
		progress.setCanceledOnTouchOutside(false);
		progress.show(this, null, "ע����...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.d("test", "Start Network!");
				HttpClient httpClient = new DefaultHttpClient();
				HttpPost httpPost = new HttpPost(CREATE_ACCOUNT_URL);
				List<NameValuePair> params = new ArrayList<NameValuePair>();
				params.add(new BasicNameValuePair("username", eUser_id.getText().toString()));
				params.add(new BasicNameValuePair("password", ePwd1.getText().toString()));
				RadioButton selectedGender = (RadioButton)CreateUserActivity.this.findViewById(rGender.getCheckedRadioButtonId());
				params.add(new BasicNameValuePair("gender", 
						selectedGender.getText().toString()));
				params.add(new BasicNameValuePair("age", eAge.getText().toString()));
				params.add(new BasicNameValuePair("phone", ePhone.getText().toString()));
				params.add(new BasicNameValuePair("email", eRealName.getText().toString()));
				try {
					httpPost.setEntity(new UrlEncodedFormEntity(params, HTTP.UTF_8));
					HttpResponse httpResponse = httpClient.execute(httpPost);
					if(httpResponse.getStatusLine().getStatusCode() == 200) {
						Log.d("test", "Network OK!");
						HttpEntity entity = httpResponse.getEntity();
						String entityStr = EntityUtils.toString(entity);
						String jsonStr = entityStr.substring(entityStr.indexOf("{"));
						JSONObject json = new JSONObject(jsonStr);
						sendMessage(MSG_CREATE_RESULT, json);
						
					}
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}).start();
		
	}
	
	private boolean checkUsername() {
		String username = eUser_id.getText().toString();
		if(TextUtils.isEmpty(username)) {
			return false;
		}
		return true;
	}

	private int checkPassword() {
		/*
		 * return value:
		 * 0 password valid
		 * 1 password not equal 2 inputs
		 * 2 password empty
		 * */
		String pwd1 = ePwd1.getText().toString();
		String pwd2 = ePwd2.getText().toString();
		if(!pwd1.equals(pwd2)) {
			return 1;
		} else if(TextUtils.isEmpty(pwd1)) {
			return 2;
		} else {
			return 0;
		}
	}
	
	private int checkAge() {
		/*
		 * return value
		 * 0 ����Ϸ�
		 * -1 ����Ϊ��
		 * -2����Ϊ����
		 * -3����Ϊ����ֵ�ַ��������С��
		 * */
		int ageNum;
		String age = eAge.getText().toString();
		if(TextUtils.isEmpty(age)) {
			return -1;
		}
		try {
			ageNum = Integer.parseInt(age);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return -3;
		}
		if(ageNum <= 0 || ageNum > 100) {
			return -2;
		}
		return 0;
	}

	private void handleReset() {
		eUser_id.setText("");
		ePwd1.setText("");
		ePwd2.setText("");
		((RadioButton)(rGender.getChildAt(0))).setChecked(true);
		eAge.setText("");
		ePhone.setText("");
		eRealName.setText("");
	}
	
	private void sendMessage(int what, Object obj) {
		Message msg = Message.obtain();
		msg.what = what;
		msg.obj = obj;
		mHandler.sendMessage(msg);
	}

}