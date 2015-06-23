package com.xu.phone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class Personal extends Activity implements OnClickListener {

	public TextView Muser_jifen;//积分
	public TextView Muser_id;
	public TextView Muser_name;
	public Button Mzhuxiao;
	public LinearLayout Mmes;
	public LinearLayout Moldbuy;
	private static int jifen = Login.jifen;//数据库中的积分
//	int old_jifen = 500;
	public static int user_jifen = jifen +Shop_Detail.getJifen;//总积分
	public static int add_jifen=Shop_Detail.getJifen;//获得的积分

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.personal);

		initView();
		initEvent();

	}

	//
	public void initView() {
		Muser_jifen = (TextView) findViewById(R.id.id_personal_jifen);
		Muser_id = (TextView) findViewById(R.id.id_personal_id);
		Mzhuxiao = (Button) findViewById(R.id.id_PerButton_zhuxiao);
		Mmes = (LinearLayout) findViewById(R.id.id_personal_mymessage);
		Moldbuy = (LinearLayout) findViewById(R.id.id_personal_mycard);
		Muser_name = (TextView) findViewById(R.id.id_personal_realname);
	}

	//
	public void initEvent() {
//		user_jifen = jifen+add_jifen;
		Log.i("jifen",user_jifen+"");
		Muser_jifen.setText(Login.jifen+Shop_Detail.getJifen+"");
		Muser_id.setText("用户ID:"+Login.userid);
		Mzhuxiao.setOnClickListener(this);
		Muser_name.setText(Login.realname);
		Mmes.setOnClickListener(this);
		Moldbuy.setOnClickListener(this);
	}

	//
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			// onBackPressed();
			Intent intent = new Intent(Personal.this, MainActivity.class);
			startActivity(intent);
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			return false;
		}

		return super.onKeyDown(keyCode, event);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_PerButton_zhuxiao:
			
			Intent intent1 = new Intent(Personal.this, Login.class);
			startActivity(intent1);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			break;
		case R.id.id_personal_mymessage:
			Intent intent2 = new Intent(Personal.this, MyMes.class);
			startActivity(intent2);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);

			break;
		case R.id.id_personal_mycard:
			Intent intent3 = new Intent(Personal.this, OldBuy.class);
			startActivity(intent3);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			break;
		default:
			break;
		}

	}
}
