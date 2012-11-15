package au.com.spinninghalf.connectingtothenetwork;

/*
this activity is used for displaying the gig details when using MOBILE layout. i had trouble with overlapping fragments when trying
to display the gig details using fragment transactions. i.e. when the screen was rotated, sometimes there was either a
blank screen OR had overlapping fragments OR when navigating backwards the gigList did not display.
better to start a new activity, send it the selected gig id & position and let the activity lifecycle handle screen rotations.
seems to a lot more simpler, and work. 
*/

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

public class ViewGigActivity extends Activity {
	public static final String EXTRA_SELECTED_GIG_POSITION = "au.com.spinninghalf.connectingtothenetwork.viewgigactivity.extraselectedgigposition";
	public static final String EXTRA_SELECTED_GIG_ID = "au.com.spinninghalf.connectingtothenetwork.viewgigactivity.extraselectedgigid";
	private final static String TAG = "ViewGigActivity";
	final static String ARG_SELECTED_GIG_ID = "ViewGigActivity_selected_gig_id";
	final static String ARG_SELECTED_GIG_POSITION = "ViewGigActivity_selected_gig_position";
	final static String ARG_INIT = "init";
	final static String ARG_SHOW = "show_text";
	final static String ARG_DATE = "date_text";
	final static String ARG_DESCRIPTION = "description_text";
	final static String ARG_PRICE = "price_text";
	final static String ARG_TIX_URL = "tix_url_text";
	int mInit = -1;
	TextView showTextView;
    TextView dateTextView;
    TextView descriptionTextView;
    TextView priceTextView;
    TextView  tixUrlTextView;
    private long _selectedGigId = -1;
    private int _selectedGigPosition = -1;
    
    
    //used for setting the Bundle strings of the textViews when using a savedInstanceState
    private String defaultShowString = "";
    private String defaultDateString = "";
    private String defaultDescriptionString = "";
    private String defaultPriceString = "";
    private String defaultTixUrlString = "";
    
    public SpinningHalfApplication shapp;
    DatabaseConnector dbc;
    
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.view_gig_activity_main);
		
		shapp = SpinningHalfApplication.getInstance();
		dbc = shapp.getDatabaseConnector();
		
		//TextView textView1 = (TextView) findViewById(R.id.showTextView);
		//TextView textView2 = (TextView) findViewById(R.id.dateTextView);
		
		//textView1.setText(Integer.toString(position));
		//textView2.setText(Long.toString(id));
		
		showTextView = (TextView) findViewById(R.id.showTextView);
	    dateTextView = (TextView) findViewById(R.id.dateTextView);
	    descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
	    priceTextView = (TextView) findViewById(R.id.priceTextView);
	    tixUrlTextView = (TextView) findViewById(R.id.tixUrlTextView); 
		
		Bundle extras = getIntent().getExtras();
		
		int position = extras.getInt(ViewGigActivity.EXTRA_SELECTED_GIG_POSITION);
		long id = extras.getLong(ViewGigActivity.EXTRA_SELECTED_GIG_ID);
		
		updateGigView(id, position);
	}
	
	@Override
	public void onPause() {
		super.onPause();
	   	
		Log.i(TAG,"in onPause()");
	    	
	}
	    
	@Override
	public void onStop() {
	    super.onStop();
	    	
	    Log.i(TAG,"in onStop()");
	    }    

	    
	public void updateGigView(long id, int position) {
		this._selectedGigId = id;
		this._selectedGigPosition = position;
			
		Log.i(TAG, "in updateGigView(), id = " + id + " and position = " + position);
			
		if(showTextView == null) {
			Log.i(TAG, "in updateGigView " + "showTextView is null");
		}
		    
		new LoadGigDetailsTask().execute(id);
	}
		
		
	//performs database query outside GUI thread
	private class LoadGigDetailsTask extends AsyncTask<Long, Object, Cursor> 
	{
	    //perform the database access
	    @Override
	    protected Cursor doInBackground(Long... params)
	    {
	    	dbc.open();
	        Log.i(TAG, "in LoadGigDetailsTask and in doInBackground");
	        Cursor cursor = dbc.getOneContact(params[0]);
	        if (!cursor.moveToFirst()) {
	        	Log.i(TAG, "in LoadGigDetailsTask and in doInBackground. Cursor is EMPTY");
	        }
	        //get a cursor containing all data on given entry
	        return cursor;
	    } 

	    //use the Cursor returned from the doInBackground method
	    @Override
	    protected void onPostExecute(Cursor result)
	    {
	    	super.onPostExecute(result);
	          
	        if(!result.moveToFirst()) {
	        	Log.i(TAG, "in onPostExecute and the cursor passed in is null. EEEEEKKKKK");
	        }
	         
	        //move to the first item 
	        if (result.moveToFirst()) { 
	    
		        //get the column index for each data item
		        int showIndex = result.getColumnIndex("show");
		        int dateIndex = result.getColumnIndex("date");
		        int descriptionIndex = result.getColumnIndex("description");
		        int priceIndex = result.getColumnIndex("price");
		        int tixUrlIndex = result.getColumnIndex("tixUrl");
		    
		        //fill TextViews with the retrieved data
		        showTextView.setText(result.getString(showIndex));
		        dateTextView.setText(result.getString(dateIndex));
		        descriptionTextView.setText(result.getString(descriptionIndex));
		        priceTextView.setText(result.getString(priceIndex));
		        tixUrlTextView.setText(result.getString(tixUrlIndex));
		    
		        result.close(); //close the result cursor
		        Log.i(TAG, "closing Database in ViewGigFragment");
		        dbc.close(); //close database connection
		          
		        if (!dbc.databaseOpen) {
		        	Log.i(TAG, "***database is CLOSED***");
		         }
	        } else {
	        	//fill TextViews with the strings saved in the Bundle data passed into onCreate().
	        	showTextView.setText(defaultShowString);
	        	dateTextView.setText(defaultDateString);
	        	descriptionTextView.setText(defaultDescriptionString);
	        	priceTextView.setText(defaultPriceString);
	        	tixUrlTextView.setText(defaultTixUrlString);
	          
		        result.close();
		        dbc.close();
	        } 
	    } //end method onPostExecute
    } //end class LoadContactTask
		
		
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
	        
	    SpinningHalfApplication.getInstance().setWasViewingGig(true);

	    //Save the current article selection in case we need to recreate the fragment
	    //outState.putLong(ARG_ID, mCurrentId);
	    outState.putLong(ViewGigActivity.ARG_SELECTED_GIG_ID, this._selectedGigId);
	    outState.putInt(ViewGigActivity.ARG_SELECTED_GIG_POSITION, this._selectedGigPosition);
	        
	        
	    //if there has not been a gig selected then updateGigView has NOT been called!
	    //which implies that the TextViews references have not been found/assigned.
	    //if we were not to check this condition then running methods on the TextViews would
	    //be throwing NullPointerExceptions, which is exactly what happened. Ha.
	    if (this._selectedGigId != -1) {
	    //save the text fields in the TextViews to the Bundle.
	    outState.putString(ViewGigActivity.ARG_SHOW, (showTextView.getText()).toString());
	    outState.putString(ViewGigActivity.ARG_DATE, (dateTextView.getText()).toString());
	    outState.putString(ViewGigActivity.ARG_DESCRIPTION, (descriptionTextView.getText()).toString());
	    outState.putString(ViewGigActivity.ARG_PRICE, (priceTextView.getText()).toString());
	    outState.putString(ViewGigActivity.ARG_TIX_URL, (tixUrlTextView.getText()).toString());
	    }
	        
	    Log.i(TAG, "in onSaveInstanceState, _selectedGigId is " + Long.toString(this._selectedGigId));
	        
	}	
}
