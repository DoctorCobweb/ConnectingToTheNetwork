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
	private static final String NO_XML_CONTENT = "N/A";
	private static final String XPP_ERROR = "XmlPullParser Error";
	private static final String IO_ERROR = "IO Error";
	
	
	//given a URL, establishes an HttpURLConnection and retrieves
    //all the gigs listed in goggle datastore online
    //Also, for each gig it adds the details into the android database.
	//returns a Cursor with {_id, show} columns
    public Cursor downloadAllGigs(String myUrl, DatabaseConnector dbc) {
    	
    	try {
			return downloadAndParse(SpinningHalfApplication.SPINNINGHALF_GIGLIST_WEBSERVICE, dbc);
		} 
		catch (XmlPullParserException e) {
   	       	Log.d(TAG, "XmlPullParserException : Unable to retrieve web page. URL may be invalid." + e);
   	    	e.printStackTrace();
   	    	return dbc.getErrorMsgInCursorForm(XPP_ERROR);
		} catch (IOException e) {
	    	Log.d(TAG, "IOException : in doInBackground method." + e);
	    	e.printStackTrace();
	    	return dbc.getErrorMsgInCursorForm(IO_ERROR);
		}
    }
	
	public Cursor downloadAndParse(String myUrl, DatabaseConnector dbc) throws IOException, XmlPullParserException {
		InputStream is = null;
    	int gigCounter = 0; //holds the number of gigs read from the xml webservice. used in gigsArray[][]
    	Log.i(TAG, "in downloadAndParseGig");
    	
    	try {
    		URL url = new URL(myUrl);
    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    		conn.setReadTimeout(0); /* in milliseconds. A default time of 0 will disable read timeouts. */
    		conn.setConnectTimeout(0); /* milliseconds. A defaukt time of 0 will disable connect timeouts. */
    		conn.setRequestMethod("GET");
    		conn.setDoInput(true);
    		//Starts the query
    		conn.connect();
    		int response = conn.getResponseCode();
    		Log.d(TAG, "This response is: " + response);
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
    		String venue = null;
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
    					Log.i(TAG, "Start of " + currentTag + " , id = " + gig_id);
    				}
    				Log.i(TAG, currentTag);
    			} else if (eventType == XmlPullParser.TEXT) {
    				if ("author".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "" ) {
    					    author = xpp.getText();
    					} else {
    						author = NO_XML_CONTENT;
    					}
    				} else if ( "date".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "") {
    					    date = xpp.getText();
    					} else {
    						date = NO_XML_CONTENT;
    					}
    				} else if ( "venue".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "") {
    					    venue = xpp.getText();
    					} else {
    						venue = NO_XML_CONTENT;
    					}
    				} else if ("description".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "") {
    					    description = xpp.getText();
    					} else {
    						description = NO_XML_CONTENT;
    					}
    				} else if ("show".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "") {
    				        show = xpp.getText();
    					} else {
    						show = NO_XML_CONTENT;
    					}
    				} else if ("price".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "") {
    					    price = xpp.getText();
    					} else {
    						price = NO_XML_CONTENT;
    					}
    				} else if ("tixUrl".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "") {
    					    tixUrl = xpp.getText();
    					} else {
    						tixUrl = NO_XML_CONTENT;
    					}
    				}
    			} else if (eventType == XmlPullParser.END_TAG) {
    				if ("gig".equals(xpp.getName())) {
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
    					if (!flag) {
    					//insert the new gig DETAILS into the database
    					dbc.insertGig(gig_id, author, show, date, venue, description, price, tixUrl);
    					}
    					
    					//increment the gig counter
    					gigCounter++;
    				}
    			}
    			eventType = xpp.next();
    		}
    		return dbc.getAllGigs();
    		
    	}   finally {
    		Log.i(TAG, "in finally block");
    		Log.i(TAG, "gigCounter is= " + gigCounter);
    		if (is != null) {
    			is.close();
    		}
    	}
	}
	
	
	
	public void downloadAndParse_VoidVersion(String _url, DatabaseConnector dbc) throws IOException, XmlPullParserException {
		InputStream is = null;
    	int gigCounter = 0; //holds the number of gigs read from the xml webservice. used in gigsArray[][]
    	Log.i(TAG, "in downloadAndParse_VoidVersion");
    	
    	try {
    		URL url = new URL(_url);
    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    		conn.setReadTimeout(0); /* in milliseconds. A default time of 0 will disable read timeouts. */
    		conn.setConnectTimeout(0); /* milliseconds. A defaukt time of 0 will disable connect timeouts. */
    		conn.setRequestMethod("GET");
    		conn.setDoInput(true);
    		//Starts the query
    		conn.connect();
    		int response = conn.getResponseCode();
    		Log.d(TAG, "This response is: " + response);
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
    		String venue = null;
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
    					Log.i(TAG, "Start of " + currentTag + " , id = " + gig_id);
    				}
    				Log.i(TAG, currentTag);
    			} else if (eventType == XmlPullParser.TEXT) {
    				if ("author".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "" ) {
    					    author = xpp.getText();
    					} else {
    						author = NO_XML_CONTENT;
    					}
    					//Log.i(TAG, author + " 2");
    				} else if ( "date".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "") {
    					    date = xpp.getText();
    					} else {
    						date = NO_XML_CONTENT;
    					}
    					//Log.i(TAG, date + " 3");
    				} else if ( "venue".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "") {
    					    venue = xpp.getText();
    					} else {
    						venue = NO_XML_CONTENT;
    					}
    				} else if ("description".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "") {
    					    description = xpp.getText();
    					} else {
    						description = NO_XML_CONTENT;
    					}
    					//Log.i(TAG, description + " 4");
    				} else if ("show".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "") {
    				        show = xpp.getText();
    					} else {
    						show = NO_XML_CONTENT;
    					}
    					//Log.i(TAG, show + " 5");
    				} else if ("price".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "") {
    					    price = xpp.getText();
    					} else {
    						price = NO_XML_CONTENT;
    					}
    					//Log.i(TAG, price + " 6");
    				} else if ("tixUrl".equals(currentTag)) {
    					if (xpp.getText() != null && xpp.getText() != "") {
    					    tixUrl = xpp.getText();
    					} else {
    						tixUrl = NO_XML_CONTENT;
    					}
    					//Log.i(TAG, tixUrl + " 7");
    				}
    			} else if (eventType == XmlPullParser.END_TAG) {
    				//Log.i(TAG, "in end tag");
    				if ("gig".equals(xpp.getName())) {
    					//Log.i(TAG, "HI from end /"gig/" element");
    					boolean flag = false; //set to true if the gig already has been inserted into the db.
    					
    					//a cursor with 1 column, namely "gig_id" for every gig in the android db.
    					Cursor gigIdCursor = dbc.getGigIdColumnVals();
    					
    					
    					if (gigIdCursor.moveToFirst()) {
	    					gigIdCursor.moveToFirst();
	    					int gig_id_index = gigIdCursor.getColumnIndex("gig_id");
	    					for (int i = 0; i < dbc.getGigIdColumnVals().getCount(); i++) {
	    						if (gigIdCursor.getString(gig_id_index) == gig_id) {
	    							flag = true;
	    						}
	    						gigIdCursor.moveToNext(); //increment to the next row in the cursor.
	    					}
	    					gigIdCursor.close(); 
    					}
    					
    					if (!flag) {
    					//insert the new gig DETAILS into the database
    					dbc.insertGig(gig_id, author, show, date, venue, description, price, tixUrl);
    					}
    					
    					//increment the gig counter
    					gigCounter++;
    				}
    			}
    			//Log.i(TAG, "before next()");
    			eventType = xpp.next();
    			//Log.i(TAG, Integer.toString(eventType));
    			//currentTag1 = xpp.getName();
    			//Log.i(TAG, "currentTag1: "+ currentTag1);
    		}
    		return;
    		
    	}   finally {
    		//allGigsCursor.close();
    		Log.i(TAG, "in finally block");
    		Log.i(TAG, "gigCounter is= " + gigCounter);
    		if (is != null) {
    			is.close();
    		}
    	}
	}
	
	//given a URL, establishes an HttpURLConnection and retrieves
    //all the gigs listed in goggle datastore online
    //Also, for each gig it adds the details into the android database.
	//returns a Cursor with {_id, show} columns
    public void downloadGigsUpdate(String myUrl, DatabaseConnector dbc) throws IOException, XmlPullParserException {
    	downloadAndParse(myUrl, dbc);
    }
}
