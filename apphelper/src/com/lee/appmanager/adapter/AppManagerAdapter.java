package com.lee.appmanager.adapter;

import java.util.List;

import com.lee.appmanager.R;
import com.lee.appmanager.domain.AppInfo;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author liyayun
 * @date 2014-11-30 下午6:49:19  
 */
public class AppManagerAdapter extends BaseAdapter {

	private static final String TAG = "AppManagerAdapter";
	private List<AppInfo> appinfos;
	private Context context;
	private static ImageView iv;
	private static TextView tv;

	public AppManagerAdapter(List<AppInfo> appinfos, Context context) {
		this.appinfos = appinfos;
		this.context = context;
	}

	public int getCount() {
		// TODO Auto-generated method stub
		return appinfos.size();
	}

	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return appinfos.get(position);
	}

	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	/**
	 * View convertView (转化view对象 ,历史view对象的缓存) convertview 就是拖动的时候被回收掉的view对象
	 */
	public View getView(int position, View convertView, ViewGroup parent) {
		AppInfo info = appinfos.get(position);
		View view;
		if (convertView == null) {
			Log.i(TAG,"通过资源文件 创建view对象");
			view = View.inflate(context, R.layout.app_item, null);
		}else{
			Log.i(TAG,"使用历史缓存view对象");
			view = convertView;
		}
		iv = (ImageView) view.findViewById(R.id.iv_app_icon);
		tv = (TextView) view.findViewById(R.id.tv_app_name);
		iv.setImageDrawable(info.getIcon());
		tv.setText(info.getAppName());
		return view;

	}

}
