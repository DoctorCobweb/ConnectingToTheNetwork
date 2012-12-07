package au.com.spinninghalf.connectingtothenetwork;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.app.Application;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

public class SpinningHalfApplication extends Application {
	public static final String TAG = SpinningHalfApplication.class.getSimpleName();
	public static final String SPINNINGHALF_GIGLIST_WEBSERVICE = "http://www.spinning-half-jersey-jaxrs.appspot.com/rest/gigs";
	
	private DatabaseConnector databaseConnector;
	private DownloadAndParseGigs d_p_g;
	private boolean serviceRunning;
	private boolean downloadFinished = false;
	private long selectedGigId = -1;
	private int selectedGigPosition = -1;
	private boolean wasViewingGig = false;
	private boolean hasDeviceTelephonyCapabilities = false;
	private boolean updateGigGuideDatabase = false;
	
	private static SpinningHalfApplication singleton;
	
	
	//Returns the application instance. It is a SINGLETON
	public static SpinningHalfApplication getInstance() {
		return singleton;
	}
	
	public boolean getUpdateGigGuideDatabase() {
		return this.updateGigGuideDatabase;
	}
	
	public void setUpdateGigGuideDatabase(boolean updateGigGuideDatabase) {
		this.updateGigGuideDatabase = updateGigGuideDatabase;
	}
	
	public boolean getTelephonyCapability() {
		return this.hasDeviceTelephonyCapabilities;
	}
	
	public void setTelephonyCapability(boolean hasDeviceTelephonyCapabilities) {
		this.hasDeviceTelephonyCapabilities = hasDeviceTelephonyCapabilities;
	}
	
	public boolean getWasViewingGig() {
		return this.wasViewingGig;
	}
	
	public void setWasViewingGig(boolean wasViewingGig) {
		this.wasViewingGig = wasViewingGig;
	}
	
	
	public long getSelectedGigId () {
		return selectedGigId;
	}
	
	
	public void setSelectedGigId(long id) {
		selectedGigId = id;
	}
	
	
	public int getSelectedGigPosition () {
		return selectedGigPosition;
	}
	
	
	public void setSelectedGigPosition (int position) {
		selectedGigPosition = position;
	}
	
	
	public void setDownloadFinished(boolean downloadFinished) {
		this.downloadFinished = downloadFinished;
	}

	
	public boolean getDownloadFinished() {
		return this.downloadFinished;
	}
	
	
	//returns the DatabaseConnector
	public DatabaseConnector getDatabaseConnector() {
		if (databaseConnector == null) {
			databaseConnector = new DatabaseConnector(this);
		}
		return databaseConnector;
	}
	
	
	//returns the DownloadAndParseGigs instance
	public synchronized DownloadAndParseGigs getDownloadAndParseGigsInstance() {
		if (d_p_g == null) {
			d_p_g = new DownloadAndParseGigs();
		}
		return d_p_g;
	}
	
	
	@Override
	public void onCreate() {
		super.onCreate();
		singleton = this;
		Log.i(TAG, "onCreate");
		//new DownloadGigs().execute(SpinningHalfApplication.SPINNINGHALF_GIGLIST_WEBSERVICE);
	}

	
	private class DownloadGigs extends AsyncTask<String, Void, Void> {
		
		@Override
		protected Void doInBackground(String...value) {
			
			//start from a fresh clean database. NOT OPTIMAL. MUST FIX.
			getDatabaseConnector().deleteAll();
			//start downloading the gigs as soon as the application is started.
			//it is in the application singleton because we only want to do this ONCE.
			//It's a time expensive operation
			downloadGigsParseAndCreateDatabase(value[0]);
			return null;
		}
		
		@Override
		protected void onPostExecute(Void values) {
			super.onPostExecute(values);
			setDownloadFinished(true);
		}
	}

	
	public void downloadGigsParseAndCreateDatabase(String url) {
		try {
			getDownloadAndParseGigsInstance().downloadAndParse_VoidVersion(url, getDatabaseConnector());
		} 
		catch (XmlPullParserException e) {
  	    	Log.d(TAG, "XmlPullParserException : Unable to retrieve web page. URL may be invalid." + e);
  	    	e.printStackTrace();
		} catch (IOException e) {
	    	Log.d(TAG, "IOException : in doInBackground method." + e);
	    	e.printStackTrace();
		}
	}
	
	
	public synchronized void  updateAllGigsCursor() {
		DownloadAndParseGigs d_p_g = new DownloadAndParseGigs();
		databaseConnector = this.getDatabaseConnector();
		
		try {
			d_p_g.downloadGigsUpdate(SpinningHalfApplication.SPINNINGHALF_GIGLIST_WEBSERVICE, databaseConnector);
		} 
		catch (XmlPullParserException e) {
   	    		Log.d(TAG, "XmlPullParserException : Unable to retrieve web page. URL may be invalid." + e);
   	    		e.printStackTrace();
		} catch (IOException e) {
	    		Log.d(TAG, "IOException : in doInBackground method." + e);
	    		e.printStackTrace();
		}
	}
	
	@Override 
	public void onTerminate() {
		super.onTerminate();
		Log.i(TAG, "onTerminate");
	}
	
	
	public boolean isServiceRunning() {
		return serviceRunning;
	}
	
	
	public void setServiceRunning(boolean serviceRunning) {
		this.serviceRunning = serviceRunning;
	}
}
