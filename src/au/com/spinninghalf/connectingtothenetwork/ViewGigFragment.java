package au.com.spinninghalf.connectingtothenetwork;

/*TODO
 * 1. add a splash screen to the view gig frag when user opens gig guide for the first time.
 *    had probs with having to re inflate a new view when needing to view a gig after selecting a gig.
 *    got null pointer exception because the i.e. showTextView was not present due to not inflating it in onCreateView for the first time.
 */


//import com.actionbarsherlock.app.SherlockFragment;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class ViewGigFragment extends SherlockFragment {
	private final static String TAG = "ViewGigFragment";
	final static String ARG_SELECTED_GIG_ID = "ViewGigFragment_selected_gig_id";
	final static String ARG_SELECTED_GIG_POSITION = "ViewGigFragment_selected_gig_position";
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
    public void onAttach(Activity activity) {
    	super.onAttach(activity);
    	
    	Log.i(TAG,"in onAttach()");
    	
    }
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	super.onCreate(savedInstanceState);
    	
    	shapp = SpinningHalfApplication.getInstance();
    	dbc = shapp.getDatabaseConnector();
    	
    	Log.i(TAG, "in onCreate()");
    	
    	if (savedInstanceState != null) {
    		return;
    	}
    }
    
    
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
		
		Log.i(TAG, "in onCreateView()");
		
        //If activity recreated (such as from screen rotate), restore
        //the previous article selection set by onSaveInstanceState().
        //This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
        	this._selectedGigId = savedInstanceState.getLong(ViewGigFragment.ARG_SELECTED_GIG_ID);
        	this._selectedGigPosition = savedInstanceState.getInt(ViewGigFragment.ARG_SELECTED_GIG_POSITION);
        	defaultShowString = savedInstanceState.getString(ViewGigFragment.ARG_SHOW);
        	defaultDateString = savedInstanceState.getString(ViewGigFragment.ARG_DATE);
        	defaultDescriptionString = savedInstanceState.getString(ViewGigFragment.ARG_DESCRIPTION);
        	defaultPriceString = savedInstanceState.getString(ViewGigFragment.ARG_PRICE);
        	defaultTixUrlString = savedInstanceState.getString(ViewGigFragment.ARG_TIX_URL); 
        	//Log.i(TAG, "in onCreateView() + and checking savedInstanceState existence, _selectedGigId " + Long.toString(this._selectedGigId));
        	Log.i(TAG, "in onCreateView and savedInstanceState != null");
        }
        //Inflate the layout for this fragment
        return inflater.inflate(R.layout.view_gig, container, false);
    }

	
    @Override
    public void onStart() {
        super.onStart();
        
        Log.i(TAG, "in onStart()");
        
        showTextView = (TextView) getActivity().findViewById(R.id.showTextView);
	    dateTextView = (TextView) getActivity().findViewById(R.id.dateTextView);
	    descriptionTextView = (TextView) getActivity().findViewById(R.id.descriptionTextView);
	    priceTextView = (TextView) getActivity().findViewById(R.id.priceTextView);
	    tixUrlTextView = (TextView) getActivity().findViewById(R.id.tixUrlTextView); 
	    

        //During startup, check if there are arguments passed to the fragment.
        //onStart is a good place to do this because the layout has already been
        //applied to the fragment at this point so we can safely call the method
        //below that sets the article text.
        Bundle args = getArguments();
        
        if (args != null) {
        	long mSelectedGigId = args.getLong(ViewGigFragment.ARG_SELECTED_GIG_ID);
            int mSelectedGigPosition = args.getInt(ViewGigFragment.ARG_SELECTED_GIG_POSITION);
            //Set article based on argument passed in
        	Log.i(TAG, "in onStart() and  ARG_ID is " + Long.toString(mSelectedGigId));
            updateGigView(mSelectedGigId, mSelectedGigPosition);
        } 
        /*
            else if (this._selectedGigId != -1) {
        	Log.i(TAG, "in onStart(), args is equal to null");
            //Set article based on saved instance state defined during onCreateView
            updateGigView(this._selectedGigId);
        }
        */
        
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
	          ViewGigFragment.this.showTextView.setText(result.getString(showIndex));
	          ViewGigFragment.this.dateTextView.setText(result.getString(dateIndex));
	          ViewGigFragment.this.descriptionTextView.setText(result.getString(descriptionIndex));
	          ViewGigFragment.this.priceTextView.setText(result.getString(priceIndex));
	          ViewGigFragment.this.tixUrlTextView.setText(result.getString(tixUrlIndex));
	    
	          result.close(); //close the result cursor
	          Log.i(TAG, "closing Database in ViewGigFragment");
	          dbc.close(); //close database connection
	          
	          if (!dbc.databaseOpen) {
	         	 Log.i(TAG, "***database is CLOSED***");
	          }
          } else {
        	  //fill TextViews with the strings saved in the Bundle data passed into onCreate().
        	  ViewGigFragment.this.showTextView.setText(defaultShowString);
        	  ViewGigFragment.this.dateTextView.setText(defaultDateString);
        	  ViewGigFragment.this.descriptionTextView.setText(defaultDescriptionString);
        	  ViewGigFragment.this.priceTextView.setText(defaultPriceString);
        	  ViewGigFragment.this.tixUrlTextView.setText(defaultTixUrlString);
          
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
        outState.putLong(ViewGigFragment.ARG_SELECTED_GIG_ID, this._selectedGigId);
        outState.putInt(ViewGigFragment.ARG_SELECTED_GIG_POSITION, this._selectedGigPosition);
        
        
        //if there has not been a gig selected then updateGigView has NOT been called!
        //which implies that the TextViews references have not been found/assigned.
        //if we were not to check this condition then running methods on the TextViews would
        //be throwing NullPointerExceptions, which is exactly what happened. Ha.
        if (this._selectedGigId != -1) {
        //save the text fields in the TextViews to the Bundle.
        outState.putString(ViewGigFragment.ARG_SHOW, (ViewGigFragment.this.showTextView.getText()).toString());
        outState.putString(ViewGigFragment.ARG_DATE, (ViewGigFragment.this.dateTextView.getText()).toString());
        outState.putString(ViewGigFragment.ARG_DESCRIPTION, (ViewGigFragment.this.descriptionTextView.getText()).toString());
        outState.putString(ViewGigFragment.ARG_PRICE, (ViewGigFragment.this.priceTextView.getText()).toString());
        outState.putString(ViewGigFragment.ARG_TIX_URL, (ViewGigFragment.this.tixUrlTextView.getText()).toString());
        }
        
        Log.i(TAG, "in onSaveInstanceState, _selectedGigId is " + Long.toString(this._selectedGigId));
        
        //FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
    }
}
