package com.lee.appmanager.adapter;

import java.util.List;

import com.lee.appmanager.R;
import com.lee.appmanager.domain.AppInfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @author liyayun
 * @date 2014-11-30 下午6:49:15  
 */
public class AppLockAdapter extends BaseAdapter {

//	private static final String TAG = "AppManagerAdapter";
	private List<AppInfo> appinfos;
	private Context context;

	public AppLockAdapter(List<AppInfo> appinfos, Context context) {
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null; 
		final AppInfo info = appinfos.get(position);
		if (convertView == null) {
//			Log.i(TAG,"通过资源文件 创建view对象");
			convertView = View.inflate(context, R.layout.lock_status_item, null);
			holder = new ViewHolder();
			holder.app_icon=(ImageView) convertView.findViewById(R.id.iv_app_icon);
			holder.app_name=(TextView) convertView.findViewById(R.id.tv_app_name);
			holder.app_flag=(TextView) convertView.findViewById(R.id.tv_app_flag);
			
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.app_icon.setImageDrawable(info.getIcon());
		holder.app_name.setText(info.getAppName());
		if (info.isSystemApp()) {
			holder.app_flag.setText("系统应用");
		}else{
			holder.app_flag.setText("用户应用");
		}
		return convertView;
	}
	
	private static class ViewHolder {    
		ImageView app_icon;
		TextView app_name;
		TextView app_flag; 
	} 

}
