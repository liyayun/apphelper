package com.lee.appmanager.ui;

import com.lee.appmanager.R;
import com.lee.appmanager.util.MD5Encoder;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author liyayun
 * @date 2014-11-30 下午6:52:27  
 */
public class SetPassActivity extends Activity implements OnClickListener{
	private static final String TAG = "SetPassActivity";
	private SharedPreferences sp;
	private TextView tv_set_confirm;
	private TextView tv_set_cancel;
	private EditText et_old_pass;
	private EditText et_new_pass;
	private EditText et_confirm_pass;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_set_pass);
		sp=getSharedPreferences("config", Context.MODE_PRIVATE);
		tv_set_cancel=(TextView) findViewById(R.id.tv_set_cancel);
		tv_set_confirm=(TextView) findViewById(R.id.tv_set_confirm);
		et_old_pass=(EditText) findViewById(R.id.et_old_pass);
		et_new_pass=(EditText) findViewById(R.id.et_new_pass);
		et_confirm_pass=(EditText) findViewById(R.id.et_confirm_pass);
		
		tv_set_cancel.setOnClickListener(this);
		tv_set_confirm.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_set_cancel:
			Log.i(TAG, "取消");
			finish();
			break;

		case R.id.tv_set_confirm:
			Log.i(TAG, "确定");
			String oldPass=et_old_pass.getText().toString().trim();
			String realPass=sp.getString("applockPass", "");
			if (realPass.equals(MD5Encoder.md5Encode(oldPass))) {
				String newPass=et_new_pass.getText().toString().trim();
				if(!"".equals(newPass)){
					String confirmPass=et_confirm_pass.getText().toString().trim();
					if(newPass.equals(confirmPass)){
						Editor editor=sp.edit();
						editor.putString("applockPass", MD5Encoder.md5Encode(newPass));
						editor.commit();
						Toast.makeText(this, "修改成功!", Toast.LENGTH_LONG).show();
						finish();
					}else{
						Toast.makeText(this, "两次密码不一致!", Toast.LENGTH_LONG).show();
					}
				}else{
					Toast.makeText(this, "密码不能为空!", Toast.LENGTH_LONG).show();
				}
			}else{
				Toast.makeText(this, "原密码出入错误!", Toast.LENGTH_LONG).show();
			}
			break;
		}
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.set_pass, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.item_exit) {
			finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


}
