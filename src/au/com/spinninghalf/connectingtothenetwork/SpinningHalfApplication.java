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
	private static final String TAG = SpinningHalfApplication.class.getSimpleName();
	public static final String DEBUG_TAG = "HttpExample";
	private static final String NO_XML_CONTENT = "N/A";
	private static final String XPP_ERROR = "XmlPullParser Error";
	private static final String IO_ERROR = "IO Error";
	
	private DatabaseConnector databaseConnector;
	
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
	
	//TODO
	public synchronized int fetchGigUpdates() {
		Log.i(TAG, "fetching gig updates");
		int count = 0;
		
		return count;
	}
	
	public Cursor getDownloadUrlAndParse(String url) {
		DownloadAndParseGigs d_p_g = new DownloadAndParseGigs();
		databaseConnector = this.getDatabaseConnector();
		try {
			Cursor cursor = d_p_g.downloadUrl(url, databaseConnector);
			return cursor;
		} 
		catch (XmlPullParserException e) {
   	    		Log.d(DEBUG_TAG, "XmlPullParserException : Unable to retrieve web page. URL may be invalid." + e);
   	    		e.printStackTrace();
   	    		//XPP_ERROR;
   	    		return databaseConnector.getErrorMsgInCursorForm(XPP_ERROR);
   	    		//return error;
		} catch (IOException e) {
	    		Log.d(DEBUG_TAG, "IOException : in doInBackground method." + e);
	    		e.printStackTrace();
	    		//IO_ERROR;
	    		return databaseConnector.getErrorMsgInCursorForm(IO_ERROR);
	    		//return error
		}
	}
	
//NOT USED-----------------------------------------------------------------------------------------------------
	
	//given a URL, establishes an HttpURLConnection and retrieves
    //that web page content as an InputStream, which it returns
    //as a string.
	
    public Cursor downloadUrl(String myUrl) throws IOException, XmlPullParserException {
    	InputStream is = null;
    	int gigCounter = 0; //holds the number of gigs read from the xml webservice. used in gigsArray[][]
    	Log.i(TAG, "using downloadUrl in SpinningHalfApplication");
    	
    	try {
    		URL url = new URL(myUrl);
    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    		conn.setReadTimeout(15000); /* milliseconds */
    		conn.setConnectTimeout(20000); /* milliseconds */
    		conn.setRequestMethod("GET");
    		conn.setDoInput(true);
    		//Starts the query
    		conn.connect();
    		int response = conn.getResponseCode();
    		Log.d(DEBUG_TAG, "This response is: " + response);
    		is = conn.getInputStream();
    		
    		//parse the xml
    		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
    		factory.setNamespaceAware(true);
    		XmlPullParser xpp = factory.newPullParser();
    		
    		xpp.setInput(is, null);
    		
    		int eventType = xpp.getEventType();
    		String currentTag = null;
    		//String currentTag1 = null;
    		//Integer id = null;
    		String gig_id = null;
    		String author = null;
    		String date = null;
    		String description = null;
    		String show = null;
    		String price = null;
    		String tixUrl = null;
    		
    		
    		
    		while (eventType != XmlPullParser.END_DOCUMENT) {
    			if (eventType == XmlPullParser.START_TAG) {
    				//get the 'name' of the current xml element: '<name>' or '</name>' 
    				currentTag = xpp.getName();
    				if ("gig".equals(currentTag)) {
    					gig_id = xpp.getAttributeValue(0);
    					Log.i(DEBUG_TAG, "Start of " + currentTag + " , id = " + gig_id);
    				}
    				Log.i(DEBUG_TAG, currentTag);
    			} else if (eventType == XmlPullParser.TEXT) {
    				if ("author".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "" ) {
    					    author = xpp.getText();
    					} else {
    						author = NO_XML_CONTENT;
    					}
    					//Log.i(DEBUG_TAG, author + " 2");
    				} else if ( "date".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "") {
    					    date = xpp.getText();
    					} else {
    						date = NO_XML_CONTENT;
    					}
    					//Log.i(DEBUG_TAG, date + " 3");
    				} else if ("description".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "") {
    					    description = xpp.getText();
    					} else {
    						description = NO_XML_CONTENT;
    					}
    					//Log.i(DEBUG_TAG, description + " 4");
    				} else if ("show".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "") {
    				        show = xpp.getText();
    					} else {
    						show = NO_XML_CONTENT;
    					}
    					//Log.i(DEBUG_TAG, show + " 5");
    				} else if ("price".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "") {
    					    price = xpp.getText();
    					} else {
    						price = NO_XML_CONTENT;
    					}
    					//Log.i(DEBUG_TAG, price + " 6");
    				} else if ("tixUrl".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "") {
    					    tixUrl = xpp.getText();
    					} else {
    						tixUrl = NO_XML_CONTENT;
    					}
    					//Log.i(DEBUG_TAG, tixUrl + " 7");
    				}
    			} else if (eventType == XmlPullParser.END_TAG) {
    				//Log.i(DEBUG_TAG, "in end tag");
    				if ("gig".equals(xpp.getName())) {
    					
    					//Log.i(DEBUG_TAG, "HI from end /"gig/" element");
    					
    					
    					boolean flag = false; //set to true if the gig already has been inserted into the db.
    					
    					Cursor gigIdCursor = this.getDatabaseConnector().getGigIdColumnVals(); //a cursor with 1 column, namely "gig_id" for every gig in the android db.
    					gigIdCursor.moveToFirst();
    					int gig_id_index = gigIdCursor.getColumnIndex("gig_id");
    					for (int i = 0; i < this.getDatabaseConnector().getGigIdColumnVals().getCount(); i++) {
    						if (gigIdCursor.getString(gig_id_index) == gig_id) {
    							flag = true;
    						}
    						gigIdCursor.moveToNext(); //increment to the next row in the cursor.
    					}
    					//gigIdCursor.close(); //?
    					if (!flag) {
    					//insert the new gig details into the database
    					this.getDatabaseConnector().insertGig(gig_id, author, show, date, description, price, tixUrl);
    					}
    					
    					//increment the gig counter
    					gigCounter++;
    				}
    			}
    			//Log.i(DEBUG_TAG, "before next()");
    			eventType = xpp.next();
    			//Log.i(DEBUG_TAG, Integer.toString(eventType));
    			//currentTag1 = xpp.getName();
    			//Log.i(DEBUG_TAG, "currentTag1: "+ currentTag1);
    		}   		
    		return this.getDatabaseConnector().getAllGigs();
    	}   finally {
    		Log.i(DEBUG_TAG, "in finally block");
    		Log.i(DEBUG_TAG, "gigCounter is= " + gigCounter);
    		if (is != null) {
    			is.close();
    		}
    	}
    }
	
//NOT USED-----------------------------------------------------------------------------------------------------

}
