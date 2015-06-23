package com.xu.phone;

import java.util.ArrayList;



import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends BaseAdapter {

	public static ArrayList<ApkEntity> goods_list;
	private LayoutInflater inflater;
	private Context context;

	public MyAdapter(Context context, ArrayList<ApkEntity> apk_list) {
		this.goods_list = apk_list;
		this.inflater = LayoutInflater.from(context);
		this.context = context;
	}

	public void onDateChange(ArrayList<ApkEntity> apk_list) {
		this.goods_list = apk_list;
		this.notifyDataSetChanged();
	}

	@Override
	public int getCount() {

		return goods_list.size();
	}

	@Override
	public Object getItem(int position) {

		return goods_list.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ApkEntity entity = goods_list.get(position);
		ViewHolder holder;
		// SquaredImageView view = (SquaredImageView) convertView;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.item_shop, null);
			// 绑定控件
			// holder.mTitle = (TextView) convertView
			// .findViewById(R.id.id_item_text_yongjin);
			holder.mGoods_name = (TextView) convertView
					.findViewById(R.id.id_item_shop_name);
			holder.mGoods_jiage = (TextView) convertView
					.findViewById(R.id.id_item_shop_jiage);
//			holder.mrs_area = (TextView) convertView
//					.findViewById(R.id.id_itemdoor_area);
//			holder.mrs_door = (TextView) convertView
//					.findViewById(R.id.id_itemdoor_rsdoor);
//			holder.imgview = (ImageView) convertView
//					.findViewById(R.id.id_itemdoor_img);
			convertView.setTag(holder);// 将设置好的布局保存到缓存中，并将其设置在Tag里，以便后面方便取出Tag
		} else {
			holder = (ViewHolder) convertView.getTag();

		}
//		String url = entity.getUrl();
//		Picasso.with(context).load(url).placeholder(R.drawable.isloading)
//		.error(R.drawable.loaderror).into(holder.imgview);
		// Log.i("Picasso",url);
		// 把参数传给对应的控件
//		holder.mGoods_id.setText(entity.getGoods_id());
		holder.mGoods_name.setText(entity.getGoods_name());
		holder.mGoods_jiage.setText(entity.getGoods_jiage());
//		holder.mGoods_jifen.setText(entity.getGoods_jifen());
//		holder.mGoods_count.setText(entity.getGoods_count());
//		holder.mGoods_miaosu.setText(entity.getGoods_miaosu());
		
		// holder.mTitle.setText(entity.getText_jiage());

		return convertView;
	}

	// ViewHolder类是一个静态类，可以缓存数据的视图，加快UI的响应速度
	class ViewHolder {
		// private TextView mTitle;
		private TextView mGoods_id; // 
		private TextView mGoods_name; // 
		private TextView mGoods_jiage; // 
		private TextView mGoods_jifen; //
		private TextView mGoods_count; //
		private TextView mGoods_miaosu; //
	}




}
