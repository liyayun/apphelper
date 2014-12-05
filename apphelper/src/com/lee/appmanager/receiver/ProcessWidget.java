package com.lee.appmanager.receiver;

import com.lee.appmanager.service.UpdateWidgetService;

import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;

/**
 * @author liyayun
 * @date 2014-11-30 ÏÂÎç6:51:13  
 */
public class ProcessWidget extends AppWidgetProvider {
	
	Intent intent;
	
	
	@Override
	public void onDeleted(Context context, int[] appWidgetIds) {
		super.onDeleted(context, appWidgetIds);
		intent = new Intent(context,UpdateWidgetService.class);
		context.stopService(intent);
	}

	@Override
	public void onEnabled(Context context) {
		super.onEnabled(context);
		intent = new Intent(context,UpdateWidgetService.class);
		context.startService(intent);
	}

}
