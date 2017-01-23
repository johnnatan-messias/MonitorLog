package service;

import service.widget.AppServiceInterface;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

public class AppConnectionService extends AppService implements
		AppServiceInterface {
	/**
	 * @see android.app.Service#onBind(Intent)
	 */
	private final IBinder connection = new ServiceBinder();

	public class ServiceBinder extends Binder {
		public AppServiceInterface getMyService() {
			return AppConnectionService.this;
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		return connection;
	}

	@Override
	public boolean getIsRunningService() {
		return isRunning();

	}
}
