package au.com.spinninghalf.connectingtothenetwork;

/*TODO
 * 1. add a splash screen to the view gig frag when user opens gig guide for the first time.
 *    had probs with having to re inflate a new view when needing to view a gig after selecting a gig.
 *    got null pointer exception because the i.e. showTextView was not present due to not inflating it in onCreateView for the first time.
 */


import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class ViewGigFragment extends Fragment {
	private final static String TAG = "ViewGigFrament";
	final static String ARG_ID = "id";
	final static String ARG_INIT = "init";
	final static String ARG_SHOW = "show_text";
	final static String ARG_DATE = "date_text";
	final static String ARG_DESCRIPTION = "description_text";
	final static String ARG_PRICE = "price_text";
	final static String ARG_TIX_URL = "tix_url_text";
	long mCurrentId = -1;
	int mInit = -1;
	TextView showTextView;
    TextView dateTextView;
    TextView descriptionTextView;
    TextView priceTextView;
    TextView  tixUrlTextView;
    
    
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
    	
    	shapp = SpinningHalfApplication.getInstance();
    	dbc = shapp.getDatabaseConnector();
    	
    	Log.i(TAG, "in onCreate()");
    }
    
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
		
        //If activity recreated (such as from screen rotate), restore
        //the previous article selection set by onSaveInstanceState().
        //This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
        	//mCurrentId = savedInstanceState.getLong(ARG_ID);
        	mCurrentId = shapp.getSelectedGigId();
        	//mCurrentId = shapp.getSelectedGigId();
        	defaultShowString = savedInstanceState.getString(ARG_SHOW);
        	defaultDateString = savedInstanceState.getString(ARG_DATE);
        	defaultDescriptionString = savedInstanceState.getString(ARG_DESCRIPTION);
        	defaultPriceString = savedInstanceState.getString(ARG_PRICE);
        	defaultTixUrlString = savedInstanceState.getString(ARG_TIX_URL); 
        	Log.i(TAG, "in onCreateView() + and checking savedInstanceState existence " + Long.toString(mCurrentId));
        }
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.view_gig, container, false);
    }

	
    @Override
    public void onStart() {
        super.onStart();

        //During startup, check if there are arguments passed to the fragment.
        //onStart is a good place to do this because the layout has already been
        //applied to the fragment at this point so we can safely call the method
        //below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            //Set article based on argument passed in
        	Log.i(TAG, "in onStart() and  ARG_ID is " + Long.toString(args.getLong(ARG_ID)));
            //updateGigView(args.getLong(ARG_ID));
            updateGigView(args.getLong(ARG_ID));
        } else if (shapp.getSelectedGigId() != -1) {
        	Log.i(TAG, "in onStart(), args is equal to null");
            //Set article based on saved instance state defined during onCreateView
            updateGigView(shapp.getSelectedGigId());
        }
        Log.i(TAG, "in onStart() and at end of it.");
    }

    
    @Override
    public void onPause() {
    	super.onPause();
    }

    
	public void updateGigView(long id) {
		showTextView = (TextView) getActivity().findViewById(R.id.showTextView);
	    dateTextView = (TextView) getActivity().findViewById(R.id.dateTextView);
	    descriptionTextView = (TextView) getActivity().findViewById(R.id.descriptionTextView);
	    priceTextView = (TextView) getActivity().findViewById(R.id.priceTextView);
	    tixUrlTextView = (TextView) getActivity().findViewById(R.id.tixUrlTextView); 
		
		
	    if(showTextView == null) {
	    Log.i(TAG, "in updateGigView " + "showTextView is null");
	    }
	    
	    Log.i(TAG, "in updateGigView(), id is " + id);
		new LoadContactTask().execute(id);
		
		mCurrentId = id;
	}
	
    //performs database query outside GUI thread
    private class LoadContactTask extends AsyncTask<Long, Object, Cursor> 
    {
       //perform the database access
       @Override
       protected Cursor doInBackground(Long... params)
       {
          dbc.open();
          Log.i(TAG, "in doInBackground");
          Cursor cursor = dbc.getOneContact(params[0]);
          if (!cursor.moveToFirst()) {
         	 Log.i(TAG, "in doInBackground and cursor is EMPTY");
          }
          //get a cursor containing all data on given entry
          return cursor;
       } //end method doInBackground

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
	          Log.i(TAG, "closing Database in ViewGigFragnent");
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

        //Save the current article selection in case we need to recreate the fragment
        //outState.putLong(ARG_ID, mCurrentId);
        outState.putLong(ARG_ID, shapp.getSelectedGigId());
        
        
        //if there has not been a gig selected then updateGigView has NOT been called!
        //which implies that the TextViews references have not been found/assigned.
        //if we were not to check this condition then running methods on the TextViews would
        //be throwing NullPointerExceptions, which is exactly what happened. Ha.
        if (shapp.getSelectedGigId() != -1) {
        //save the text fields in the TextViews to the Bundle.
        outState.putString(ARG_SHOW, (showTextView.getText()).toString());
        outState.putString(ARG_DATE, (dateTextView.getText()).toString());
        outState.putString(ARG_DESCRIPTION, (descriptionTextView.getText()).toString());
        outState.putString(ARG_PRICE, (priceTextView.getText()).toString());
        outState.putString(ARG_TIX_URL, (tixUrlTextView.getText()).toString());
        }
        
        Log.i(TAG, "in onSaveInstanceState, mCurrentId is " + Long.toString(shapp.getSelectedGigId()));
    }
}
