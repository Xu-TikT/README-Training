package com.xu.phone;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

//�����õĻ��ֺ͸�����
public class JieSuan extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.jiesuan);

		initView();
		initEvent();

	}

	private void initEvent() {
		
		
	}

	private void initView() {
	
		
	}
}
