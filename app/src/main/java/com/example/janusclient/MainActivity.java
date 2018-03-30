package com.example.janusclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.dnion.P2PSDK;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;


public class MainActivity extends Activity {
//		extends ActionBarActivity {
	EditText addressEt;
	EditText roomEt;
	EditText displayEt;
    private Handler _mainHandler = new Handler(Looper.getMainLooper());
    private int captureCounter = 0;


	static final String TAG = "MainActivity";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);


		addressEt = (EditText)findViewById(R.id.addressTv);
		roomEt = (EditText)findViewById(R.id.roomTv);
		displayEt = (EditText)findViewById(R.id.displayTv);
		addressEt.setText("ws://120.55.57.69:7400/");

		roomEt.setText("805");
		displayEt.setText("Huawei7");
		Button joinBtn = (Button)findViewById(R.id.joinRtn);
		joinBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				String strRoom = roomEt.getText().toString();
				String display = displayEt.getText().toString();
				P2PSDK.getInstance().waitPair();
			}
		});
		
		Button connectBtn = (Button)findViewById(R.id.connectBtn);
		connectBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				String url = addressEt.getText().toString();
				P2PSDK.getInstance().connect(url, "15201822887", "1234qwer");

                /*
				// video capture probe
				for (int i = 0; i < 50; i++) {
                    _mainHandler.postDelayed(()->{
                        JanusGateway.getInstance().startPreview();
                        JanusGateway.getInstance().stopPreview();
                        Log.i("VSDK", "Capture Loop:" + captureCounter);
                        captureCounter++;
                    }, 2000);
				}
				*/
			}
		});
	}

//	@Override
//	public boolean onCreateOptionsMenu(Menu menu) {
//		// Inflate the menu; this adds items to the action bar if it is present.
//		getMenuInflater().inflate(R.menu.main, menu);
//		return true;
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings) {
			return true;
		}   
		return super.onOptionsItemSelected(item);  
	}

	@Subscribe(threadMode=ThreadMode.MAIN)
	public void onEvent(JanusMessages.OnConnectionSuccess event) {
		Log.w(TAG, "[FATAL]OnConnectionSuccess ");
	}

	@Subscribe(threadMode=ThreadMode.MAIN)
	public void onEvent(JanusMessages.OnConnectionFailed event) {
		Log.w(TAG, "[FATAL]OnConnectionFailed ");
	}

	@Subscribe(threadMode=ThreadMode.MAIN)
	public void onEvent(JanusMessages.OnChatStart event){
		Log.w(TAG, "[FATAL]OnChatStart ");

		Intent i = new Intent(MainActivity.this, RoomActivity.class);
		startActivity(i);
	}

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
 