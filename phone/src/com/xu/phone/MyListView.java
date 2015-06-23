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
 * �Զ���listview �����������غ�����ˢ�¹���
 * 
 * @author XU
 * 
 */
public class MyListView extends ListView implements OnScrollListener {
	//
	public static View footer;// �ײ�����
	public static View header;// ��������

	// footer
	int totalItemCount;// ������
	int lastVisibleItem;// ���һ���ɼ���item

	static boolean isLoading;// ���ڼ���

	// header

	int firstVisibleItem;// ��ǰ��һ���ɼ���item��λ�ã�
	int scrollState;// listview ��ǰ����״̬��
	static boolean isRemark;// ��ǣ���ǰ����listview������µģ�
	int startY;// ����ʱ��Yֵ��
	static int headerHeight;// ���������ļ��ĸ߶ȣ�
	static int state;// ��ǰ��״̬��
	final int NONE = 0;// ����״̬��
	final int PULL = 1;// ��ʾ����״̬��
	final int RELESE = 2;// ��ʾ�ͷ�״̬��
	final int REFLASHING = 3;// ˢ��״̬��
	IReflashListener iReflashListener;// ˢ�����ݵĽӿ�

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
	 * ��ӵײ�������ʾ���ֵ�listview
	 * 
	 * 
	 */
	private void initView_load(Context context) {

		LayoutInflater inflater = LayoutInflater.from(context);
		footer = inflater.inflate(R.layout.footer_layout, null);
		footer.findViewById(R.id.id_load_layout).setVisibility(View.GONE);// һ��ʼ������footer

		this.addFooterView(footer);
		this.setOnScrollListener(this);

	}

	/**
	 * 
	 * ��ʼ�����棬��Ӷ��������ļ���listview
	 * 
	 * @param context
	 */
	private void inintView_ReFlash(Context context) {
		LayoutInflater inflater = LayoutInflater.from(context);
		header = inflater.inflate(R.layout.header_layout, null);
		// header.findViewById(R.id.id_header_layout).setVisibility(View.GONE);//
		// һ��ʼ������header

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
		// �����ײ���ʼ����
		if (totalItemCount == lastVisibleItem
				&& scrollState == SCROLL_STATE_IDLE
				&& MainActivity.page != MainActivity.page_total) {
			if (!isLoading
					&& HttpUtil.isNetworkConnected(getContext()) != false) {
				isLoading = true;
				MyListView.footer.findViewById(R.id.id_load_layout)
						.setVisibility(View.VISIBLE);
				// ���ظ�������
				// ��������
				int strPage = MainActivity.page + 1;// intתstring
				Log.i("strPage", "" + strPage);
//				MainActivity.map.put("id", DetailOfBuildings.rs_nid);
				MainActivity.map.put("page", strPage + "");
				MainActivity.testjobc(MainActivity.map, getContext());
				//
				onLoad();
				// ��̬������ͼ�е�����

				MainActivity.adapter.notifyDataSetChanged();

			}else {
				Toast.makeText(getContext(), "��û������,�޷����ظ���", Toast.LENGTH_SHORT)
				.show();
	}

		} else if (totalItemCount == lastVisibleItem
				&& scrollState == SCROLL_STATE_IDLE
				&& MainActivity.page == MainActivity.page_total) {
			Toast.makeText(getContext(), "���һҳ��", Toast.LENGTH_SHORT).show();
		} 

		//
		this.scrollState = scrollState;
	}

	/**
	 * ��������
	 */

	// ������һҳ
	public void onLoad() {

		// ���ϴ�ȡֵ�ĵط���ʼ����ȡֵ
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
//				+ MainActivity.rs_size; // ÿ�μ��غ󶼸���old�е�ֵ
//		Log.i("old_page_size", MainActivity.old_page_size + "");

	}

	/**
	 * �������
	 */
	public static void loadIsComplete() {
		isLoading = false;
		MyListView.footer.findViewById(R.id.id_load_layout).setVisibility(
				View.GONE);
	}

	/**
	 * ����ˢ��
	 * 
	 */
	/*
	 * ֪ͨ�����֣�ռ�õĿ��ߣ�
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
	 * ����header ���� �ϱ߾ࣻ
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
			// ������ڽ��涥�˰���
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
				// �����������ݣ�
				reflashViewByState();
				if (HttpUtil.isNetworkConnected(getContext()) != false) {
					clearAllData();
					iReflashListener.onReflash();
//					MainActivity.doorAdapter.notifyDataSetChanged();
				} else {
					Toast.makeText(getContext(), "��û������,�޷�ˢ��",
							Toast.LENGTH_SHORT).show();
				}
				// ��ɼ���
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
	 * �ж��ƶ����̲�����
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
	 * ���ݵ�ǰ״̬���ı������ʾ��
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
			tip.setText("��������ˢ�£�");
			arrow.clearAnimation();
			arrow.setAnimation(anim1);
			break;
		case RELESE:
			arrow.setVisibility(View.VISIBLE);
			progress.setVisibility(View.GONE);
			tip.setText("�ɿ�����ˢ�£�");
			arrow.clearAnimation();
			arrow.setAnimation(anim);
			break;
		case REFLASHING:
			MyListView.topPadding(50);
			arrow.setVisibility(View.GONE);
			progress.setVisibility(View.VISIBLE);
			tip.setText("����ˢ��...");
			arrow.clearAnimation();

			break;
		}
	}

	/**
	 * ��ȡ�����ݣ�
	 */
	public void reflashComplete() {
		state = NONE;
		isRemark = false;
		reflashViewByState();
		TextView lastupdatetime = (TextView) MyListView.header
				.findViewById(R.id.id_header_update_time);
		SimpleDateFormat format = new SimpleDateFormat("MM��dd�� hh:mm:ss");
		Date date = new Date(System.currentTimeMillis());
		String time = format.format(date);
		lastupdatetime.setText("�������:" + time);
	}

	public void setInterface(IReflashListener iReflashListener) {
		this.iReflashListener = iReflashListener;
	}

	/**
	 * ˢ�����ݽӿ�
	 * 
	 * @author XU
	 */
	public interface IReflashListener {
		public void onReflash();
	}

	// �����������
	public void clearAllData() {

//		MainActivity.list_rs_pic_url.clear();
//		MainActivity.list_rs_door.clear();
//		MainActivity.list_rs_area.clear();
//		MainActivity.list_rs_title.clear();
//		MainActivity.list_rs_id.clear();
//		MainActivity.page = 0;
//		MainActivity.old_page_size = 0;// ����ȡֵ��ʼ��λ��
//		MainActivity.doorAdapter.notifyDataSetChanged();

	}

}

