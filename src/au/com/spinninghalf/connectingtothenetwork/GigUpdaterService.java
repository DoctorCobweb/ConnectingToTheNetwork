package au.com.spinninghalf.connectingtothenetwork;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class GigUpdaterService extends Service {
	private static final String TAG = "GigUpdaterService";
	static final int DELAY = 30000; // a minute
	private boolean runFlag = false;
	private Updater updater;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		this.updater = new Updater();
		
		Log.d(TAG, "onCreated");
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		super.onStartCommand(intent, flags, startId);
		
		this.runFlag = true;
		this.updater.start();
		
		Log.d(TAG, "***onStartCommand***");
		return START_STICKY;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		
		this.runFlag = false;
		this.updater.interrupt();
		this.updater = null;
		
		Log.d(TAG, "***onDestroyed***");
	}
	
	/**
	 * Thread that performs the actual update from the online service
	 */
	private class Updater extends Thread {
		
		public Updater() {
			super("GigUpdaterService-Updater");
		}
		
		@Override
		public void run() {
			GigUpdaterService gigUpdaterService = GigUpdaterService.this;
			while (gigUpdaterService.runFlag) {
				Log.d(TAG, "***GigUpdaterService running***");
				
				try {
					Log.d(TAG, "***GigUpdaterService ran***");
					Thread.sleep(DELAY);
				} catch (InterruptedException e) {
					gigUpdaterService.runFlag = false;
				}
			}
		}
	} // Updater
	}
