package com.xu.phone;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

public class MyMes extends Activity{

	private TextView Mname;
	private TextView Mage;
	private TextView Maddr;
	private TextView Mdengji;
	//
	private String name;
	private String age;
	private String addr;
	private String dengji;
	private int jifen = Login.jifen;
	
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.mymessage);

		initEvent();
		initView();
		

	}

	private void initEvent() {
		Mname = (TextView) findViewById(R.id.id_mym_name);
		Mage = (TextView) findViewById(R.id.id_mym_age);
		Maddr = (TextView) findViewById(R.id.id_mym_addr);
		
		Mdengji = (TextView) findViewById(R.id.id_mym_dengji);
		
	}

	private void initView() {
		Mname.setText(Login.realname);
		Mage.setText(Login.age);
		Maddr.setText(Login.addr);
		if(jifen<=500){
			dengji = "1 ¼¶";
		}else if(jifen>500&&jifen<=1500){
			dengji = "2 ¼¶";
		}else if(jifen>1500&&jifen<=5000){
			dengji = "3 ¼¶";
		}
		Mdengji.setText(dengji);
	}
	
	
}
