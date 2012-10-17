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
import android.util.Log;

public class SpinningHalfApplication extends Application {
	public static final String TAG = SpinningHalfApplication.class.getSimpleName();
	private static final String NO_XML_CONTENT = "N/A";
	private static final String XPP_ERROR = "XmlPullParser Error";
	private static final String IO_ERROR = "IO Error";
	public static final String SPINNINGHALF_GIGLIST_WEBSERVICE = "http://www.spinning-half-jersey-jaxrs.appspot.com/rest/gigs";
	
	private DatabaseConnector databaseConnector;
	private DownloadAndParseGigs d_p_g;
	private boolean serviceRunning;
	
	@Override
	public void onCreate() {
		super.onCreate();
		Log.i(TAG, "onCreate");
	}
	
	@Override 
	public void onTerminate() {
		super.onTerminate();
		Log.i(TAG, "onTerminate");
	}
	
	
	public DatabaseConnector getDatabaseConnector() {
		if (databaseConnector == null) {
			databaseConnector = new DatabaseConnector(this);
		}
		return databaseConnector;
	}
	
	public synchronized DownloadAndParseGigs getDownloadAndParseGigsInstance() {
		if (d_p_g == null) {
			d_p_g = new DownloadAndParseGigs();
		}
		return d_p_g;
	}
	
	public synchronized void  updateAllGigsCursor() {
		DownloadAndParseGigs d_p_g = new DownloadAndParseGigs();
		databaseConnector = this.getDatabaseConnector();
		
		try {
			d_p_g.downloadGigsUpdate(SpinningHalfApplication.SPINNINGHALF_GIGLIST_WEBSERVICE, databaseConnector);
			//allGigsCursor = d_p_g.getAllGigsCursor();
			//databaseConnector.close(); // needed?
		} 
		catch (XmlPullParserException e) {
   	    		Log.d(TAG, "XmlPullParserException : Unable to retrieve web page. URL may be invalid." + e);
   	    		e.printStackTrace();
   	    		//XPP_ERROR;
   	    		//return databaseConnector.getErrorMsgInCursorForm(XPP_ERROR);
   	    		//return error;
		} catch (IOException e) {
	    		Log.d(TAG, "IOException : in doInBackground method." + e);
	    		e.printStackTrace();
	    		//IO_ERROR;
	    		//return databaseConnector.getErrorMsgInCursorForm(IO_ERROR);
	    		//return error
		}
		
		//Cursor cursor = getDownloadUrlAndParse(SpinningHalfApplication.SPINNINGHALF_GIGLIST_WEBSERVICE);
		
	}
	
	/*
	public Cursor getAllGigsCursor() {
		return allGigsCursor;
	}
	*/
	
	public boolean isServiceRunning() {
		return serviceRunning;
	}
	
	public void setServiceRunning(boolean serviceRunning) {
		this.serviceRunning = serviceRunning;
	}
	
//NOT USED-----------------------------------------------------------------------------------------------------
	
	//given a URL, establishes an HttpURLConnection and retrieves
    //that web page content as an InputStream, which it returns
    //as a string.
	//  private Cursor downloadUrl(String myUrl) throws IOException, XmlPullParserException {
//NOT USED-----------------------------------------------------------------------------------------------------

}
