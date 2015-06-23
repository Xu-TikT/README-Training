package com.xu.phone;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class OldBuy extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.old_buy);

		initEvent();
		initView();
		

	}

	private void initView() {
		// TODO Auto-generated method stub
		
	}

	private void initEvent() {
		// TODO Auto-generated method stub
		
	}
}
