package com.xu.phone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.view.View;

public class DialogUtil {
	
	/**
	 * ������Ϣ�Ի���
	 * @author XU
	 * @param ctx
	 * @param msg
	 * @param goHome
	 */
	
	
	

	//����һ����ʾ��Ϣ�ĶԻ���
	public static void showDialog(final Context ctx,String msg,boolean goHome)
	{
		//����һ��AlertDialog.Builder����
		AlertDialog.Builder builder = new AlertDialog.Builder(ctx).setMessage(msg).setCancelable(false);
		if(goHome){
			builder.setPositiveButton("ȷ��",new OnClickListener(){

				@Override
				public void onClick(DialogInterface dialog, int which) {
//					Intent i = new Intent(ctx,PersonalCenter.class);
//					i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//					ctx.startActivity(i);
					
					
				}
				
			});
			
			
		}
		else
		{
			builder.setPositiveButton("ȷ��",null);
		}
		builder.create().show();
		
		
		
	}
	//����һ����ʾָ������ĶԻ���
	public static void showDialog(Context ctx,View view)
	{
		new AlertDialog.Builder(ctx).setView(view).setCancelable(false).setPositiveButton("ȷ��",null).create().show();
		
		
		
		
	}
	
	
	
	
	
}
