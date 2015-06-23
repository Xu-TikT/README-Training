package com.xu.phone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class Shop_Detail extends Activity implements OnClickListener {

	private ImageButton Muser;
	private Button Mbuy;
	public static int getJifen = 0;
public static boolean fromSD = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.shop_detail);

		initView();
		initEvent();

	}

	//
	public void initView() {

		Muser = (ImageButton) findViewById(R.id.id_top_user_img);
		Mbuy = (Button) findViewById(R.id.id_SDButton_buy);

	}

	//
	public void initEvent() {

		Muser.setOnClickListener(this);
		Mbuy.setOnClickListener(this);
	}

	//
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_top_user_img:
			if (MainActivity.isLogin == true) {
				Intent intent2 = new Intent(Shop_Detail.this, Personal.class);
				startActivity(intent2);
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
			} else {
				Intent intent2 = new Intent(Shop_Detail.this, Login.class);
				startActivity(intent2);
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
			}
			break;
		case R.id.id_SDButton_buy:
			if (MainActivity.isLogin == true) {
				getJifen = getJifen+15;
			Toast.makeText(this, "购买成功,享受"+Login.dengji, Toast.LENGTH_SHORT).show();
//			Personal.user_jifen = Personal.user_jifen+getJifen;
			}else {
				fromSD = true;
				Toast.makeText(this, "请先登录", Toast.LENGTH_SHORT).show();
				Intent intent2 = new Intent(Shop_Detail.this, Login.class);
				startActivity(intent2);
				overridePendingTransition(R.anim.in_from_right,
						R.anim.out_to_left);
			}
			break;
		default:
			break;
		}

	}

	//
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			onBackPressed();
			overridePendingTransition(android.R.anim.slide_in_left,
					android.R.anim.slide_out_right);
			return false;
		}

		return super.onKeyDown(keyCode, event);

	}

}
