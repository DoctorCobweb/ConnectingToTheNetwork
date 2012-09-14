package au.com.spinninghalf.connectingtothenetwork;

import android.app.Activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class GigListActivity extends Activity {
	private static final String DEBUG_TAG = "HttpExample";
	private static final String NO_XML_CONTENT = "N/A";
	private static final String ERROR = "Error";
	private static final String IO_ERROR = "IO Error";
	private static final String XPP_ERROR = "XmlPullParser Error";
	private String stringUrl = null; 
	
	private TextView idTextView;
	private TextView authorTextView;
	private TextView dateTextView;
	private TextView descriptionTextView;
	private TextView showTextView;
	private TextView priceTextView;
	private TextView tixUrlTextView;
	
	private final static int MAX_NO_OF_GIGS = 1000;
	private final static int noOfGigElements = 7; //gig_id, author, date, description, show, price, tixUrl
	
	final String[][] gigsArray = new String[MAX_NO_OF_GIGS][noOfGigElements];
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        idTextView = (TextView) findViewById(R.id.idTextView);
        authorTextView = (TextView) findViewById(R.id.authorTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        showTextView = (TextView) findViewById(R.id.showTextView);
        priceTextView = (TextView) findViewById(R.id.priceTextView);
        tixUrlTextView = (TextView) findViewById(R.id.tixUrlTextView);
        
        Intent startIntent = getIntent();
        stringUrl = startIntent.getStringExtra(HttpExampleActivity.GIG_LIST_URL_KEY);
        
        new DownloadWebpageText().execute(stringUrl);
    }
    
  //Uses AsyncTask to create a task away from the main  UI thread. This task
    //takes a URL string and uses it to create an HttpUrlConnection. Once the 
    //connection has been established, the AsyncTask downloads the contents of the 
    //webpage as an InputStream. Finally, the InputStream is converted into a string, 
    //which is displayed in the UI thread by the onPostExecute method.
    private class DownloadWebpageText extends AsyncTask<String, Void, String[][]> {
    	@Override
    	protected String[][] doInBackground(String...urls) {
    		
    		//params comes from the execute() call: params[0] is the url.
    		try {
    			return downloadUrl(urls[0]);
    		} catch (IOException e) {
    			Log.d(DEBUG_TAG, "IOException : in doInBackground method." + e);
    			String[][] error = new String[1][1];
    			error[0][0] = IO_ERROR;
    			return error;
    		} catch (XmlPullParserException xe) {
    			Log.d(DEBUG_TAG, "XmlPullParserException : Unable to retrieve web page. URL may be invalid." + xe);
    			String[][] error = new String[1][1];
    			error[0][0] = XPP_ERROR;
    			return error;
    			
    		}
    	}
    	//onPostExecute displays the results of the AsyncTask.
    	@Override
    	protected void onPostExecute(String[][] result) {
    		//if there was an error caused by not downloading material, the ArrayList.size() will be = 1
    		//check to see if this is the case, else a connection was successful and ArrayList.size() = len
    		if(result[0][0] == IO_ERROR) {
    			idTextView.setText(result[0][0]);
    			authorTextView.setText(ERROR);
	    		showTextView.setText(ERROR);
	    		dateTextView.setText(ERROR);
	    		descriptionTextView.setText(ERROR);
	    		priceTextView.setText(ERROR);
	    		tixUrlTextView.setText(ERROR);
    		} else {
    			idTextView.setText(result[0][0]);
	    		authorTextView.setText(result[0][1]);
	    		showTextView.setText(result[0][2]);
	    		dateTextView.setText(result[0][3]);
	    		descriptionTextView.setText(result[0][4]);
	    		priceTextView.setText(result[0][5]);
	    		tixUrlTextView.setText(result[0][6]);
    		}
    	}
    }
    
    //given a URL, establishes an HttpURLConnection and retrieves
    //that web page content as an InputStream, which it returns
    //as a string.
    private String[][] downloadUrl(String myUrl) throws IOException, XmlPullParserException {
    	InputStream is = null;
    	int gigCounter = 0; //holds the number of gigs read from the xml webservice. used in gigsArray[][]
    	
    	try {
    		URL url = new URL(myUrl);
    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    		conn.setReadTimeout(10000); /* milliseconds */
    		conn.setConnectTimeout(15000); /* milliseconds */
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
    		String currentTag1 = null;
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
    				
    					
    					gigsArray[gigCounter][0] = gig_id;
    					gigsArray[gigCounter][1] = author;
    					gigsArray[gigCounter][2] = show;
    					gigsArray[gigCounter][3] = date;
    					gigsArray[gigCounter][4] = description;
    					gigsArray[gigCounter][5] = price;
    					gigsArray[gigCounter][6] = tixUrl;
    					
    					gigCounter++;
    				}
    			}
    			//Log.i(DEBUG_TAG, "before next()");
    			eventType = xpp.next();
    			//Log.i(DEBUG_TAG, Integer.toString(eventType));
    			//currentTag1 = xpp.getName();
    			//Log.i(DEBUG_TAG, "currentTag1: "+ currentTag1);
    		}
    		return gigsArray;
    		
    		/*
    		//convert Input Stream into a string.
    		String contentAsString = readIt(is, len);
    		
    		String[] contentAsStringArray[] = new String[];
    		return contentAsStringArray[];
    		*/
    		
    		
    	//Makes sure the InputStream is closed after the app	
    	//is finished using it.	
    	}  finally {
    		Log.i(DEBUG_TAG, "in finally block");
    		if (is != null) {
    			is.close();
    		}
    	}
    	
    }
    
    //not used at this stage.
    //Reads an InputStream and converts it into a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
    	Reader reader = null;
    	reader = new InputStreamReader(stream, "UTF-8");
    	char[] buffer = new char[len];
    	reader.read(buffer);
    	return new String(buffer);
    }
}
