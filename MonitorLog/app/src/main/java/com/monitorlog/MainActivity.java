package com.monitorlog;

import java.io.File;

import service.AppConnectionService;
import service.AppConnectionService.ServiceBinder;
import service.widget.AppServiceInterface;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.lg.sentimentalanalysis.MethodCreator;

public class MainActivity extends Activity implements ServiceConnection,
		OnItemSelectedListener {
	private String filenameIn = "datasets" + File.separator
			+ "pang_movie_sem_score.txt";
	private static String TAG = MainActivity.class.getSimpleName();
	@SuppressWarnings("unused")
	private AppServiceInterface appServiceInterface;
	final ServiceConnection serviceConnection = this;

	private Button buttonStart, buttonStop;
	private boolean isServiceRunning = false;
	private TextView statusTextView;
	private Spinner methodsSpinner;
	private Spinner datasetSpinner;
	//private String status = "Running\n";
	public static int methodId = 0;
	public static int dataset = 1000000;
	private Context context;
	private BroadcastReceiver mReceiver;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		android.provider.Settings.System.putInt(getContentResolver(),
				android.provider.Settings.System.SCREEN_BRIGHTNESS,
				0);

		context = this;
		methodsSpinner = (Spinner) findViewById(R.id.methods_spinner);
		methodsSpinner.setOnItemSelectedListener(this);
		datasetSpinner = (Spinner) findViewById(R.id.dataset_spinner);
		datasetSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				Toast.makeText(
						parent.getContext(),
						"Dataset Selected : "
								+ parent.getItemAtPosition(position).toString(),
						Toast.LENGTH_SHORT).show();

				String methodName = parent.getItemAtPosition(position).toString();
				if (methodName.compareTo("10") == 0)
					dataset = 10;
				else if(methodName.compareTo("100") == 0)
					dataset = 100;
				else if(methodName.compareTo("1K") == 0)
					dataset = 1000;
				else if(methodName.compareTo("10K") == 0)
					dataset = 10000;
				else if(methodName.compareTo("100K") == 0)
					dataset = 100000;
				else if(methodName.compareTo("1M") == 0)
					dataset = 1000000;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		buttonStart = (Button) findViewById(R.id.button_start);
		buttonStop = (Button) findViewById(R.id.button_stop);
		statusTextView = (TextView) findViewById(R.id.status);
		statusTextView.setText("Stopped");
		MethodCreator.getInstance().assets = getAssets();
		MethodCreator.context = getApplicationContext();
		mReceiver = new BroadcastReceiver() {
			@Override
			public void onReceive(Context context, Intent intent) {
				actionClickStop();
			}
		};
		registerReceiver(mReceiver, new IntentFilter("stopOSMonitor"));
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (isServiceRunning) {
			unbindService(serviceConnection);
			Log.i(TAG, "Service Finished");
			Toast.makeText(this, "Service Finished", Toast.LENGTH_SHORT).show();
		}
		unregisterReceiver(mReceiver);
		sendBroadcast(new Intent("stopOSMonitor"));
	}

	public void onClickStartMyService(View v) {
		statusTextView.setText("Running");
		new Thread(new Runnable() {
			@Override
			public void run() {
				Log.i(TAG, "Sending broadcast");
				sendBroadcast(new Intent("startOSMonitor"));
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					Log.e(TAG, e.getMessage());
					e.printStackTrace();
				}
				bindService(new Intent(context, AppConnectionService.class),
						serviceConnection, Context.BIND_AUTO_CREATE);
			}
		}).start();

		buttonStart.setEnabled(false);
		buttonStop.setEnabled(true);
		isServiceRunning = true;

		Log.i(TAG, "Service started...");
		Toast.makeText(this, "Service started...", Toast.LENGTH_SHORT).show();
	}

	public void onClickStopMyService(View v) {
		sendBroadcast(new Intent("stopOSMonitor"));
		actionClickStop();
	}

	public void playAlarm(){
		try {
			Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
			final Ringtone r = RingtoneManager.getRingtone(this, notification);
			r.play();
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						Thread.sleep(11000);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					r.stop();
				}
			}).start();

		}catch(Exception e){
			Log.e(TAG, e.getMessage());
			e.getStackTrace();
		}
	}

	public void actionClickStop(){
		buttonStart.setEnabled(true);
		buttonStop.setEnabled(false);
		isServiceRunning = false;
		statusTextView.setText("Stopped");
		playAlarm();
		unbindService(serviceConnection);
		Log.i(TAG, "Service finished");
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		Toast.makeText(
				parent.getContext(),
				"Method Selected : "
						+ parent.getItemAtPosition(position).toString(),
				Toast.LENGTH_SHORT).show();

		String methodName = parent.getItemAtPosition(position).toString();
		if (methodName.compareTo("Afinn") == 0)
			methodId = 1;
		else if (methodName.compareTo("Emolex") == 0)
			methodId = 2;
		else if (methodName.compareTo("Emoticons") == 0)
			methodId = 3;
		else if (methodName.compareTo("EmoticonsDS") == 0)
			methodId = 4;
		else if (methodName.compareTo("HappinessIndex") == 0)
			methodId = 5;
		else if (methodName.compareTo("MPQA") == 0)
			methodId = 6;
		else if(methodName.compareTo("NRCHashtag") == 0)
			methodId = 7;
		else if (methodName.compareTo("OpinionLexicon") == 0)
			methodId = 8;
		else if (methodName.compareTo("PanasT") == 0)
			methodId = 9;
		else if (methodName.compareTo("Sann") == 0)
			methodId = 10;
		else if (methodName.compareTo("Sasa") == 0)
			methodId = 11;
		else if (methodName.compareTo("SenticNet") == 0)
			methodId = 12;
		else if (methodName.compareTo("SentiStrength") == 0)
			methodId = 14;
		else if (methodName.compareTo("SentiWordNet") == 0)
			methodId = 15;
		else if (methodName.compareTo("SoCal") == 0)
			methodId = 16;
		else if (methodName.compareTo("Stanford") == 0)
			methodId = 17;
		else if (methodName.compareTo("Umigon") == 0)
			methodId = 18;
		else if (methodName.compareTo("Vader") == 0)
			methodId = 19;
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		Log.i(TAG, "Service is connected...");
		ServiceBinder binder = (ServiceBinder) service;
		appServiceInterface = binder.getMyService();
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		Log.i(TAG, "Service has been disconnected...");
		appServiceInterface = null;
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}
}