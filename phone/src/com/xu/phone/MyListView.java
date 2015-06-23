package com.xu.phone;



import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 
 * 自定义listview 增加上拉加载和下拉刷新功能
 * 
 * @author XU
 * 
 */
public class MyListView extends ListView implements OnScrollListener {
	//
	public static View footer;// 底部布局
	public static View header;// 顶部布局

	// footer
	int totalItemCount;// 总数量
	int lastVisibleItem;// 最后一个可见的item

	static boolean isLoading;// 正在加载

	// header

	int firstVisibleItem;// 当前第一个可见的item的位置；
	int scrollState;// listview 当前滚动状态；
	static boolean isRemark;// 标记，当前是在listview最顶端摁下的；
	int startY;// 摁下时的Y值；
	static int headerHeight;// 顶部布局文件的高度；
	static int state;// 当前的状态；
	final int NONE = 0;// 正常状态；
	final int PULL = 1;// 提示下拉状态；
	final int RELESE = 2;// 提示释放状态；
	final int REFLASHING = 3;// 刷新状态；
	IReflashListener iReflashListener;// 刷新数据的接口

	//
	public MyListView(Context context) {
		super(context);
		initView_load(context);
		inintView_ReFlash(context);
	}

	//
	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView_load(context);
		inintView_ReFlash(context);
	}

	//
	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView_load(context);
		inintView_ReFlash(context);
	}

	/**
	 * 
	 * 添加底部加载提示布局到listview
	 * 
	 * 
	 */
	private void initView_load(Context context) {

		LayoutInflater inflater = LayoutInflater.from(context);
		footer = inflater.inflate(R.layout.footer_layout, null);
		footer.findViewById(R.id.id_load_layout).setVisibility(View.GONE);// 一开始先隐藏footer

		this.addFooterView(footer);
		this.setOnScrollListener(this);

	}

	/**
	 * 
	 * 初始化界面，添加顶部布局文件到listview
	 * 
	 * @param context
	 */
	private void inintView_ReFlash(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		header = inflater.inflate(R.layout.header_layout, null);
		// header.findViewById(R.id.id_header_layout).setVisibility(View.GONE);//
		// 一开始先隐藏header

		measureView(header);
		headerHeight = header.getMeasuredHeight();
		Log.i("tag", "headerHeight = " + headerHeight);
		topPadding(-headerHeight);
		this.addHeaderView(header);
		this.setOnScrollListener(this);

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {

		this.lastVisibleItem = firstVisibleItem + visibleItemCount;
		this.totalItemCount = totalItemCount;

		this.firstVisibleItem = firstVisibleItem;
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// 滑到底部开始加载
		if (totalItemCount == lastVisibleItem
				&& scrollState == SCROLL_STATE_IDLE
				&& MainActivity.page != MainActivity.page_total) {
			if (!isLoading
					&& HttpUtil.isNetworkConnected(getContext()) != false) {
				isLoading = true;
				MyListView.footer.findViewById(R.id.id_load_layout)
						.setVisibility(View.VISIBLE);
				// 加载更多数据
				// 请求网络
				int strPage = MainActivity.page + 1;// int转string
				Log.i("strPage", "" + strPage);
//				MainActivity.map.put("id", DetailOfBuildings.rs_nid);
				MainActivity.map.put("page", strPage + "");
				MainActivity.testjobc(MainActivity.map, getContext());
				//
				onLoad();
				// 动态更新视图中的数据

				MainActivity.adapter.notifyDataSetChanged();

			}else {
				Toast.makeText(getContext(), "你没有联网,无法加载更多", Toast.LENGTH_SHORT)
				.show();
	}

		} else if (totalItemCount == lastVisibleItem
				&& scrollState == SCROLL_STATE_IDLE
				&& MainActivity.page == MainActivity.page_total) {
			Toast.makeText(getContext(), "最后一页了", Toast.LENGTH_SHORT).show();
		} 

		//
		this.scrollState = scrollState;
	}

	/**
	 * 上拉加载
	 */

	// 加载下一页
	public void onLoad() {

		// 从上次取值的地方开始继续取值
//		for (int i = MainActivity.old_page_size; i < MainActivity.list_rs_id
//				.size(); i++) {
//			// Log.i("ForJson.list_rs_id.size()",""+ForJson.list_rs_id.size());
//			ApkEntity entity = new ApkEntity();
//			entity.setDoor_title(MainActivity.list_rs_door.get(i).toString());
//			entity.setDoor_area(MainActivity.list_rs_area.get(i).toString());
//			entity.setDoor_door(MainActivity.list_rs_title.get(i).toString());
//
//			entity.setUrl(MainActivity.list_rs_pic_url.get(i).toString());
//			MainActivity.door_list.add(entity);
//
//		}

		loadIsComplete();

//		MainActivity.old_page_size = MainActivity.old_page_size
//				+ MainActivity.rs_size; // 每次加载后都更新old中的值
//		Log.i("old_page_size", MainActivity.old_page_size + "");

	}

	/**
	 * 加载完毕
	 */
	public static void loadIsComplete() {
		isLoading = false;
		MyListView.footer.findViewById(R.id.id_load_layout).setVisibility(
				View.GONE);
	}

	/**
	 * 下拉刷新
	 * 
	 */
	/*
	 * 通知父布局，占用的宽，高；
	 * 
	 * @param view
	 */
	private void measureView(View view) {
		ViewGroup.LayoutParams p = view.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}
		int width = ViewGroup.getChildMeasureSpec(0, 0, p.width);
		int height;
		int tempHeight = p.height;
		if (tempHeight > 0) {
			height = MeasureSpec.makeMeasureSpec(tempHeight,
					MeasureSpec.EXACTLY);
		} else {
			height = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		view.measure(width, height);
	}

	/**
	 * 设置header 布局 上边距；
	 * 
	 * @param topPadding
	 */
	static void topPadding(int topPadding) {
		header.setPadding(header.getPaddingLeft(), topPadding,
				header.getPaddingRight(), header.getPaddingBottom());
		header.invalidate();
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {

		// TODO Auto-generated method stub
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			// 如果是在界面顶端按下
			if (firstVisibleItem == 0) {
				isRemark = true;
				startY = (int) ev.getY();
			}
			break;

		case MotionEvent.ACTION_MOVE:
			onMove(ev);
			break;
		case MotionEvent.ACTION_UP:
			if (state == RELESE) {
				state = REFLASHING;
				// 加载最新数据；
				reflashViewByState();
				if (HttpUtil.isNetworkConnected(getContext()) != false) {
					clearAllData();
					iReflashListener.onReflash();
//					MainActivity.doorAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(getContext(), "你没有联网,无法刷新",
							Toast.LENGTH_SHORT).show();
				}
				// 完成加载
				reflashComplete();
			} else if (state == PULL) {
				state = NONE;
				isRemark = false;
				reflashViewByState();
			}
			break;
		}
		return super.onTouchEvent(ev);
	}

	/**
	 * 判断移动过程操作；
	 * 
	 * @param ev
	 */
	private void onMove(MotionEvent ev) {
		if (!isRemark) {
			return;
		}
		int tempY = (int) ev.getY();
		int space = tempY - startY;
		int topPadding = space - MyListView.headerHeight;
		switch (state) {
		case NONE:
			if (space > 0) {
				state = PULL;
				reflashViewByState();
			}
			break;
		case PULL:
			MyListView.topPadding(topPadding);
			if (space > MyListView.headerHeight + 30
					&& scrollState == SCROLL_STATE_TOUCH_SCROLL) {
				state = RELESE;
				reflashViewByState();
			}
			break;
		case RELESE:
			MyListView.topPadding(topPadding);
			if (space < MyListView.headerHeight + 30) {
				state = PULL;
				reflashViewByState();
			} else if (space <= 0) {
				state = NONE;
				isRemark = false;
				reflashViewByState();
			}
			break;
		}
	}

	/**
	 * 根据当前状态，改变界面显示；
	 */
	private void reflashViewByState() {
		TextView tip = (TextView) MyListView.header
				.findViewById(R.id.id_header_tip);
		ImageView arrow = (ImageView) MyListView.header
				.findViewById(R.id.id_header_arrow_img);
		ProgressBar progress = (ProgressBar) MyListView.header
				.findViewById(R.id.id_header_progress);
		RotateAnimation anim = new RotateAnimation(0, 180,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim.setDuration(500);
		anim.setFillAfter(true);
		RotateAnimation anim1 = new RotateAnimation(180, 0,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f,
				RotateAnimation.RELATIVE_TO_SELF, 0.5f);
		anim1.setDuration(500);
		anim1.setFillAfter(true);
		switch (state) {
		case NONE:
			arrow.clearAnimation();
			MyListView.topPadding(-MyListView.headerHeight);
			break;

		case PULL:
			arrow.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			tip.setText("下拉可以刷新！");
			arrow.clearAnimation();
			arrow.setAnimation(anim1);
			break;
		case RELESE:
			arrow.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			tip.setText("松开可以刷新！");
			arrow.clearAnimation();
			arrow.setAnimation(anim);
			break;
		case REFLASHING:
			MyListView.topPadding(50);
			arrow.setVisibility(View.GONE);
			progress.setVisibility(View.VISIBLE);
			tip.setText("正在刷新...");
			arrow.clearAnimation();

			break;
		}
	}

	/**
	 * 获取完数据；
	 */
	public void reflashComplete() {
		state = NONE;
		isRemark = false;
		reflashViewByState();
		TextView lastupdatetime = (TextView) MyListView.header
				.findViewById(R.id.id_header_update_time);
		SimpleDateFormat format = new SimpleDateFormat("MM月dd日 hh:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		String time = format.format(date);
		lastupdatetime.setText("最近更新:" + time);
	}

	public void setInterface(IReflashListener iReflashListener) {
		this.iReflashListener = iReflashListener;
	}

	/**
	 * 刷新数据接口
	 * 
	 * @author XU
	 */
	public interface IReflashListener {
		public void onReflash();
	}

	// 清空所有数据
	public void clearAllData() {

//		MainActivity.list_rs_pic_url.clear();
//		MainActivity.list_rs_door.clear();
//		MainActivity.list_rs_area.clear();
//		MainActivity.list_rs_title.clear();
//		MainActivity.list_rs_id.clear();
//		MainActivity.page = 0;
//		MainActivity.old_page_size = 0;// 重置取值开始的位置
//		MainActivity.doorAdapter.notifyDataSetChanged();

	}

}

