package com.xu.phone;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import com.xu.phone.MyListView.IReflashListener;

public class MainActivity extends Activity implements OnClickListener,OnItemClickListener,IReflashListener {

	private static String name; //商品名
	private static String jiage;//商品价格
	public static int page;
	public static int page_total;
	public static int rs_size;
	public static int itemPosition;// 对应的item的位置
	
	static JSONObject jobc;
	static int old_page_size = 0;
	public static boolean isLogin = false;//isLogin?
	
	private static TextView Mname;
	private static TextView Mjiage;
	private static ImageButton Muser;
	
	public static ArrayList list_goods_name = new ArrayList();
	public static ArrayList list_goods_jiage = new ArrayList();
	
	private MyListView listview;
	static MyAdapter adapter;
	static Map<String, String> map = new HashMap<String, String>();
	static ArrayList<ApkEntity> shop_list = new ArrayList<ApkEntity>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.shop);
        
        initView();
        TheThread();
        initEvents();
        
    }
    

	public Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if ((Boolean) msg.obj == true) {
				ShowListView(shop_list);
			}
		};
	};

	public void TheThread() {
		new Thread() {
			@Override
			public void run() {
				super.run();
				getDoorList();
				Message msg = new Message();
				msg.obj = true;
				handler.sendMessage(msg);
			}
		}.start();
	}

	public void ShowListView(ArrayList<ApkEntity> shop_list) {
		
		

		adapter = new MyAdapter(this, shop_list);
		adapter.notifyDataSetChanged();
		listview.setAdapter(adapter);
		
		listview.setOnItemClickListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position,
			long arg3) {

		itemPosition = position;
		// 避免header获得点击事件
		if (itemPosition != 0 && HttpUtil.isNetworkConnected(this) != false) {
			Intent intent = new Intent(MainActivity.this, Shop_Detail.class);
			startActivity(intent);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
		} else {
			Toast.makeText(this, "你没有联网,无法点击查看", Toast.LENGTH_SHORT).show();

		}

	}

	private void getDoorList() {

		map.put("page", page + "");
		testjobc(map, this);
		list_goods_name.add("商品 1");
		list_goods_name.add("商品 2");
		list_goods_name.add("商品 3");
		list_goods_name.add("商品 4");
		list_goods_name.add("商品 5");
		list_goods_name.add("商品 6");
		list_goods_name.add("商品 7");
		list_goods_name.add("商品 8");
		list_goods_name.add("商品 9");
		list_goods_jiage.add("35");
		list_goods_jiage.add("50");
		list_goods_jiage.add("60");
		list_goods_jiage.add("80");
		list_goods_jiage.add("20");
		list_goods_jiage.add("110");
		list_goods_jiage.add("89");
		list_goods_jiage.add("77");
		list_goods_jiage.add("98");
		
		// 从上次取值的地方开始继续取值
		for (int i = 0; i < 9; i++) {

			ApkEntity entity = new ApkEntity();
			entity.setGoods_name(list_goods_name.get(i).toString());
			entity.setGoods_jiage(list_goods_jiage.get(i).toString());

			MainActivity.shop_list.add(entity);
//
		}


	}


	//
	static void testjobc(Map map, Context context) {
		try {
			String url = "http://172.20.10.2:8080/ServletDemoByEclipse/servlet/goodsList";
			jobc = new JSONObject(HttpUtil.getRequest(url));// 返回的json数据对象

			
			
			
			
			
			
			
			jiage = jobc.getString("goods_id");
			name = jobc.getString("goods_name");
			
			Log.i("name",name);
			
//			Mname.setText(name);
//			Mjiage.setText(jiage);



			

		} catch (JSONException e) {

			e.printStackTrace();
		} catch (Exception e) {

			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_top_user_img:
			if(isLogin==true){
			Intent intent2 = new Intent(MainActivity.this,
					Personal.class);
			startActivity(intent2);
			overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			}else{
				Intent intent2 = new Intent(MainActivity.this,
						Login.class);
				startActivity(intent2);
				overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
			}

			break;
		default:
			break;
		}

	}

	//
	private void initView() {

		listview = (MyListView) findViewById(R.id.id_listView_shop);
		Mname = (TextView) findViewById(R.id.id_shop_goodsname);
		Mjiage = (TextView) findViewById(R.id.id_shop_goodsjiage);
		Muser = (ImageButton) findViewById(R.id.id_top_user_img);
	}

	//
	private void initEvents() {
		
		Muser.setOnClickListener(this);
		
	}

	@Override
	public void onReflash() {
		// 获取最新数据
//		shop_list.clear();
		getDoorList();
		// 通知界面显示
//		ShowListView(shop_list);
		Log.i("reflash", "ok");
		// 通知listview 刷新数据完毕；
//		((DoorListView) listview).reflashComplete();

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		super.onKeyDown(keyCode, event);
		if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
			dialog();
			return false;
		}
		return false;
	}

	protected void dialog() {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setMessage("确定要退出吗?");
		builder.setTitle("提示");
		builder.setPositiveButton("确认",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						MainActivity.this.finish();
					}
				});
		builder.setNegativeButton("取消",
				new android.content.DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		builder.create().show();
	}
    
    
}
