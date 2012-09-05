package com.nmbb.oplayer.ui.vitamio;

import io.vov.utils.Log;
import io.vov.vitamio.VitamioConnection;

import java.lang.ref.WeakReference;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.WindowManager;

import com.yixia.vitamio.library.R;

public class InitActivity extends Activity {
	public static final String FROM_ME = "fromVitamioInitActivity";
	public static final String EXTRA_MSG = "EXTRA_MSG";
	public static final String EXTRA_FILE = "EXTRA_FILE";
	private ProgressDialog mPD;

	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		new AsyncTask<Object, Object, Object>() {
			protected void onPreExecute() {
				mPD = new ProgressDialog(InitActivity.this);
				mPD.setCancelable(false);
				mPD.setMessage(getString(getIntent().getIntExtra(EXTRA_MSG, R.string.vitamio_init_decoders)));
				mPD.show();
			}

			@Override
			protected Object doInBackground(Object... params) {
				VitamioConnection.initNativeLibs(getApplicationContext(), getIntent().getIntExtra(EXTRA_FILE, R.raw.libarm), new VitamioConnection.OnNativeLibsInitedListener() {
					@Override
					public void onNativeLibsInitCompleted(String libPath) {
						Log.d("Native libs inited at " + libPath);
						uiHandler.sendEmptyMessage(0);
					}
				});
				return null;
			}
		}.execute();
	}

	private Handler uiHandler = new Handler() {
		public void handleMessage(Message msg) {
			mPD.dismiss();
			Intent src = getIntent();
			Intent i = new Intent();
			i.setClassName(src.getStringExtra("package"), src.getStringExtra("className"));
			i.setData(src.getData());
			i.putExtras(src);
			i.putExtra(FROM_ME, true);
			startActivity(i);
			finish();
		}
	};
}
