package au.com.spinninghalf.connectingtothenetwork;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import android.database.Cursor;
import android.util.Log;

public class DownloadAndParseGigs {
	private static final String TAG = DownloadAndParseGigs.class.getSimpleName();
	public static final String DEBUG_TAG = "HttpExample";
	private static final String NO_XML_CONTENT = "N/A";
	private DatabaseConnector dbc;
	private Cursor allGigsCursor; // {_id, show} columns
	
	public Cursor getAllGigsCursor() {
		return allGigsCursor;
	}
	
	//given a URL, establishes an HttpURLConnection and retrieves
    //all the gigs listed in goggle datastore online
    //Also, for each gig it adds the details into the android database.
	//returns a Cursor with {_id, show} columns
    public Cursor downloadGigs(String myUrl, DatabaseConnector _dbc) throws IOException, XmlPullParserException {
    	downloadAndParse(myUrl, _dbc);
    	return getAllGigsCursor();
    }
    
  //given a URL, establishes an HttpURLConnection and retrieves
    //all the gigs listed in goggle datastore online
    //Also, for each gig it adds the details into the android database.
	//returns a Cursor with {_id, show} columns
    public void downloadGigs2(String myUrl, DatabaseConnector _dbc) throws IOException, XmlPullParserException {
    	downloadAndParse(myUrl, _dbc);
    	allGigsCursor.close();
    }
	
	public void downloadAndParse(String myUrl, DatabaseConnector _dbc) throws IOException, XmlPullParserException {
		InputStream is = null;
    	dbc = _dbc;
    	int gigCounter = 0; //holds the number of gigs read from the xml webservice. used in gigsArray[][]
    	Log.i(TAG, "using downloadUrl in DownloadAndParseGig");
    	
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
    					
    					//a cursor with 1 column, namely "gig_id" for every gig in the android db.
    					Cursor gigIdCursor = dbc.getGigIdColumnVals(); 
    					gigIdCursor.moveToFirst();
    					int gig_id_index = gigIdCursor.getColumnIndex("gig_id");
    					for (int i = 0; i < dbc.getGigIdColumnVals().getCount(); i++) {
    						if (gigIdCursor.getString(gig_id_index) == gig_id) {
    							flag = true;
    						}
    						gigIdCursor.moveToNext(); //increment to the next row in the cursor.
    					}
    					//gigIdCursor.close(); //?
    					if (!flag) {
    					//insert the new gig DETAILS into the database
    					dbc.insertGig(gig_id, author, show, date, description, price, tixUrl);
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
    		allGigsCursor = dbc.getAllGigs();
    		
    	}   finally {
    		//allGigsCursor.close();
    		Log.i(DEBUG_TAG, "in finally block");
    		Log.i(DEBUG_TAG, "gigCounter is= " + gigCounter);
    		if (is != null) {
    			is.close();
    		}
    	}
	}
}
