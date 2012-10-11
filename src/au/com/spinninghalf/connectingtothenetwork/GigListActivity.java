package au.com.spinninghalf.connectingtothenetwork;

/*TODO
1. Add in an update button with loading animation
2. fix connection timeout issue
3. ui layout fix for gig list i.e. the borders for each item
4. get rid of first screen/change so you dont enter in url
*/


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.util.Log;


public class GigListActivity extends Activity {
	public static final String ROW_ID = "row_id"; // Intent extra key
	public static final String DEBUG_TAG = "GigListActivity";
	private static final String ERROR = "Error";
	private static final String IO_ERROR = "IO Error";
	private static final String XPP_ERROR = "XmlPullParser Error";
	private String stringUrl = null; 
	
	// map each gigs's show name to a TextView in the ListView layout
    public static String[] from = new String[] { "show" };
    public static int[] to = new int[] { R.id.gigTextView };
	
	private TextView idTextView;
	private ListView listGiglist;
	private SimpleCursorAdapter gigAdapter; // adapter for ListView
	DatabaseConnector dbc;
	//Cursor allGigsCursor;
	private SpinningHalfApplication spinningHalfApplication;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gigtimeline);
        
        //find your views
        listGiglist = (ListView) findViewById(R.id.listGigTimeline);
        
        //contactListView = getListView(); // get the built-in ListView
        listGiglist.setOnItemClickListener(viewGigListener);
        
        spinningHalfApplication = (SpinningHalfApplication) getApplication();
        
        //gigAdapter = new SimpleCursorAdapter(GigListActivity.this, R.layout.gig_list_item, null, from, to);
        //listGiglist.setAdapter(gigAdapter); // set contactView's adapter
        
        idTextView = (TextView) findViewById(R.id.idTextView);
        
        //get info from the intent which launched this activity
        Intent startIntent = getIntent();
        stringUrl = startIntent.getStringExtra(HttpExampleActivity.GIG_LIST_URL_KEY);
        
        //stop the background service which will update the gig list
        stopService(new Intent(this, GigUpdaterService.class));
        
        final ProgressBar progress = (ProgressBar)findViewById(R.id.progressBarGigActivity);
        
        new DownloadWebpageText(progress).execute(stringUrl);
    }
    
    @Override
    protected void onResume() {
    	super.onResume();
    	//getAllGigs
    	//spinningHalfApplication.getDatabaseConnector().getAllGigs();
    }
    
    @Override
    protected void onPause() {
    	super.onPause();
    }
    
    @Override
    protected void onStop() {
    	super.onStop();
    	//start the background service which will update the gig list
        //startService(new Intent(this, GigUpdaterService.class));
    	//allGigsCursor.close();
    }
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	//start the background service which will update the gig list
        //startService(new Intent(this, GigUpdaterService.class));
    	//allGigsCursor.close();
    }
    
    //Uses AsyncTask to create a task away from the main  UI thread. This task
    //takes a URL string and uses it to create an HttpUrlConnection. Once the 
    //connection has been established, the AsyncTask downloads the contents of the 
    //webpage as an InputStream. Finally, the InputStream is converted into a string, 
    //which is displayed in the UI thread by the onPostExecute method.
    private class DownloadWebpageText extends AsyncTask<String, Void, Cursor> {
    	
    	//get reference to the hosting class GigListActivity in order to call getApplication()
    	GigListActivity gigListActivity = GigListActivity.this;
    	private final ProgressBar progress;
    	
    	public DownloadWebpageText(final ProgressBar progress) {
    		this.progress = progress;
    	}
    	
    	@Override
    	protected Cursor doInBackground(String...urls) {
    		Log.i(DEBUG_TAG, "in doInBackground");
    		
    		publishProgress();
    				
    		// get a DatabaseConnector object using the Application object. 
    		dbc = spinningHalfApplication.getDatabaseConnector();
    		
    		dbc.deleteAll(); //use to reset the "gigs" table back to being empty
    		//allGigsCursor = spinningHalfApplication.getDownloadUrlAndParse(urls[0]);
    		
    		//params comes from the execute() call: params[0] is the url.
   			return spinningHalfApplication.getDownloadUrlAndParse(urls[0]);
    	}
    	
    	@Override
    	protected void onProgressUpdate(Void... values) {
    		super.onProgressUpdate();
    		progress.setVisibility(View.VISIBLE);
    	}
    	
    	//onPostExecute displays the results of the AsyncTask.
    	@Override
    	protected void onPostExecute(Cursor cursor) {
    		super.onPostExecute(cursor);
    		Log.i(DEBUG_TAG, "in onPostExecute");
    		
    		//get rid of progress circle once you have the cursor
    		progress.setVisibility(View.GONE);
    		
    		spinningHalfApplication = (SpinningHalfApplication) getApplication();
    			
    		//use LOADER instead of startManagingCursor as its deprecated. 
    		//startManagingCursor.(gigAdapter);
    			
    		cursor.moveToFirst();
    		int show_index = cursor.getColumnIndex("show");
    		String errorMessage = cursor.getString(show_index);
			
    		if(errorMessage == IO_ERROR || errorMessage == XPP_ERROR) {
    			//display a msg that there was an error with the download.
    			idTextView.setText(errorMessage);
    		} else {
    			Log.d(DEBUG_TAG, "in onPostExecute." + "number of rows in cursor = " + cursor.getCount());
    			gigAdapter = new SimpleCursorAdapter(
    		        GigListActivity.this, R.layout.gig_list_item, cursor, from, to);
    				listGiglist.setAdapter(gigAdapter); // set contactView's adapter
    				        
    				//cursor.close(); //close the cursor
    				//Log.d(DEBUG_TAG, "in onPostExecute. JUST BEFORE close() database");
    				spinningHalfApplication.getDatabaseConnector().close(); //close the database
    				//cursor.close(); //close the cursor
    				//gigAdapter.changeCursor(cursor);
    				//dbc.close();
    		}
    		//cursor.close(); //?
    	}
    }
 
    // event listener that responds to the user touching a contact's name in the ListView
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
