package au.com.spinninghalf.connectingtothenetwork;

/*TODO
1. Add in an update button with loading animation
2. fix connection timeout issue
3. ui layout fix for gig list i.e. the borders for each item
4. get rid of first screen/change so you dont enter in url
*/


import android.app.Activity;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;


public class GigListActivity extends Activity {
	public static final String ROW_ID = "row_id"; // Intent extra key
	public static final String DEBUG_TAG = "HttpExample";
	private static final String NO_XML_CONTENT = "N/A";
	private static final String ERROR = "Error";
	private static final String IO_ERROR = "IO Error";
	private static final String XPP_ERROR = "XmlPullParser Error";
	private String stringUrl = null; 
	
	private TextView idTextView;
	
	
	private ListView listGiglist;
	private CursorAdapter gigAdapter; // adapter for ListView
	//private ListView contactListView; // the ListActivity's ListView
	DatabaseConnector dbc;
	
	//private final static int MAX_NO_OF_GIGS = 1000;
	//private final static int noOfGigElements = 7; //gig_id, author, date, description, show, price, tixUrl
	private static int gigCounter;
	//public String[][] gigsArray = new String[MAX_NO_OF_GIGS][noOfGigElements];
	
	//private SpinningHalfApplication spinningHalfApplication;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gigtimeline);
        
        //find your views
        listGiglist = (ListView) findViewById(R.id.listGigTimeline);
        
        //contactListView = getListView(); // get the built-in ListView
        listGiglist.setOnItemClickListener(viewGigListener);      

        // map each gigs's show name to a TextView in the ListView layout
        String[] from = new String[] { "show" };
        int[] to = new int[] { R.id.gigTextView };
        
        gigAdapter = new SimpleCursorAdapter(
           GigListActivity.this, R.layout.gig_list_item, null, from, to);
        listGiglist.setAdapter(gigAdapter); // set contactView's adapter
        
        idTextView = (TextView) findViewById(R.id.idTextView);
        
        //get info from the intent which launched this activity
        Intent startIntent = getIntent();
        stringUrl = startIntent.getStringExtra(HttpExampleActivity.GIG_LIST_URL_KEY);
        
        // start the backgroung service which will update the gig list
        startService(new Intent(this, GigUpdaterService.class));
        
        new DownloadWebpageText().execute(stringUrl);
        
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	
    	
    }
  //Uses AsyncTask to create a task away from the main  UI thread. This task
    //takes a URL string and uses it to create an HttpUrlConnection. Once the 
    //connection has been established, the AsyncTask downloads the contents of the 
    //webpage as an InputStream. Finally, the InputStream is converted into a string, 
    //which is displayed in the UI thread by the onPostExecute method.
    private class DownloadWebpageText extends AsyncTask<String, Void, Cursor> {
    	
    	//get reference to the hosting class GigListActivity in order to call getApplication()
    	GigListActivity gigListActivity = GigListActivity.this;
    	
    	@Override
    	protected Cursor doInBackground(String...urls) {
    		
    		//dbc = new DatabaseConnector(GigListActivity.this);
    		
    		//get the Application object
    		SpinningHalfApplication spinningHalfApplication = (SpinningHalfApplication) gigListActivity.getApplication();
    				
    		// get a DatabaseConnector object using the Application object. 
    		dbc = spinningHalfApplication.getDatabaseConnector();
    		
    		dbc.deleteAll(); //use to reset the "gigs" table back to being empty
    		
    		
    		
    		//params comes from the execute() call: params[0] is the url.
   			return spinningHalfApplication.getDownloadUrlAndParse(urls[0]);
    	/*	
    	}   catch (IOException e) {
   	    		Log.d(DEBUG_TAG, "IOException : in doInBackground method." + e);
   	    		e.printStackTrace();
   	    		//IO_ERROR;
   	    		return dbc.getErrorMsgInCursorForm(IO_ERROR);//return error;
   	    	} catch (XmlPullParserException xe) {
   	    		Log.d(DEBUG_TAG, "XmlPullParserException : Unable to retrieve web page. URL may be invalid." + xe);
   	    		xe.printStackTrace();
   	    		//XPP_ERROR;
   	    		return dbc.getErrorMsgInCursorForm(XPP_ERROR);
   	    		//return error;
   	    	} 
   	    	
   	    	*/
    		 
    	}
    	//onPostExecute displays the results of the AsyncTask.
    	@Override
    	protected void onPostExecute(Cursor cursor) {
    		
    		super.onPostExecute(cursor);
    		
    		cursor.moveToFirst();
			int show_index = cursor.getColumnIndex("show");
			String errorMessage = cursor.getString(show_index);
			
    		if(errorMessage == IO_ERROR || errorMessage == XPP_ERROR) {
    			//display a msg that there was an error with the download.
    			idTextView.setText(errorMessage);
    		} else {
    			Log.d(DEBUG_TAG, "in onPostExecute." + "number of rows in cursor = " + cursor.getCount());
    			gigAdapter.changeCursor(cursor);
    			dbc.close();
    			
    		}
    		//cursor.close(); //?
    	}
    }
    
    
//NOT USED-----------------------------------------------------------------------------------------------------
    //given a URL, establishes an HttpURLConnection and retrieves
    //that web page content as an InputStream, which it returns
    //as a string.
    private Cursor downloadUrl(String myUrl) throws IOException, XmlPullParserException {
    	InputStream is = null;
    	gigCounter = 0; //holds the number of gigs read from the xml webservice. used in gigsArray[][]
    	Log.i(DEBUG_TAG, "NOT FROM HERE!!!!!!!");
    	
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
    					Cursor gigIdCursor = dbc.getGigIdColumnVals(); //a cursor with 1 column, namely "gig_id" for every gig in the android db.
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
    					//insert the new gig details into the database
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
    		return dbc.getAllGigs();
    	}   finally {
    		Log.i(DEBUG_TAG, "in finally block");
    		Log.i(DEBUG_TAG, "gigCounter is= " + gigCounter);
    		if (is != null) {
    			is.close();
    		}
    	}
    }
//NOT USED-----------------------------------------------------------------------------------------------------
 
    // event listener that responds to the user touching a contact's name
    // in the ListView
    OnItemClickListener viewGigListener = new OnItemClickListener() 
    {
       @Override
       public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
          long arg3) 
       {
          // create an Intent to launch the ViewContact Activity
          Intent viewGig = 
             new Intent(GigListActivity.this, ViewGig.class);
          
          // pass the selected contact's row ID as an extra with the Intent
          viewGig.putExtra(ROW_ID, arg3);
          startActivity(viewGig); // start the ViewContact Activity
       } // end method onItemClick
    }; // end viewContactListener
}
