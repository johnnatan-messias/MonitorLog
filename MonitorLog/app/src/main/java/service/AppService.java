package service;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.lg.sentimentalanalysis.Method;
import com.lg.sentimentalanalysis.MethodCreator;
import com.monitorlog.MainActivity;

public class AppService extends Service {
	/**
	 * @see android.app.Service#onBind(Intent)
	 */
	private static String TAG = AppService.class.getSimpleName();
	private boolean running = false;
	private Method method;
	private Thread thread;

	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "Service Created");
		setRunning(true);
		thread = new Thread(new Runnable() {
			@Override
			public void run() {

				try {
					method = MethodCreator.getInstance()
							.createMethod(MainActivity.methodId);
					sendBroadcast(new Intent("runrunrun"));
					Thread.sleep(5000);
					runService();
				} catch (InterruptedException e) {
					Log.e(TAG, e.getMessage());
					e.printStackTrace();
				}
			}
		});
		thread.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		setRunning(false);
		if(thread !=null){
			Thread.currentThread().interrupt();
			thread = null;
		}
		Log.i(TAG, "Service Destroyed");
	}

	private void runService() {
		setRunning(true);
		Log.i(TAG, "Start Method");
		new Thread(new Runnable() {
			@Override
			public void run() {
				method.analyseFile("datasets/file_" + MainActivity.dataset + ".txt");

				Log.i(TAG, "Finished Method");
				setRunning(false);
				sendBroadcast(new Intent("stopOSMonitor"));
			}
		}).start();
		//for (int i = 0; i < 4; i ++){
		//	method.analyseFile("datasets/file_" + i + ".txt");
		//	method.analyseFile("datasets/file_" + i + ".txt");
		//	method.analyseFile("datasets/file_" + i + ".txt");
		//	method.analyseFile("datasets/file_" + i + ".txt");
		//}

		//RunAllMethods runAllMethods = new RunAllMethods();
		//runAllMethods.execute();
	}


	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	protected boolean isRunning() {
		return this.running;
	}

	private void setRunning(boolean running) {
		this.running = running;
	}

	private class RunAllMethods extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params){
			Log.i(TAG, "Start Method");
			//for (int i = 0; i < 4; i ++){
			//	method.analyseFile("datasets/file_" + i + ".txt");
			//	method.analyseFile("datasets/file_" + i + ".txt");
			//	method.analyseFile("datasets/file_" + i + ".txt");
			//	method.analyseFile("datasets/file_" + i + ".txt");
			//}
			method.analyseFile("datasets/file_" + MainActivity.dataset + ".txt");

			Log.i(TAG, "Finished Method");
			setRunning(false);
			sendBroadcast(new Intent("stopOSMonitor"));

			return true;
		}

		@Override
		protected void onPostExecute(Boolean ok ) {

		}
	}
}
