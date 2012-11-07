package au.com.spinninghalf.connectingtothenetwork;

//import com.actionbarsherlock.app.SherlockListFragment;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.app.SherlockActivity;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class GigListFragment extends SherlockListFragment {
	OnGigListSelectedListener mCallback;
	
	public static final String ROW_ID = "row_id"; // Intent extra key
	public static final String TAG = "GigListFragment";
	private static final String ERROR = "Error";
	private static final String IO_ERROR = "IO Error";
	private static final String XPP_ERROR = "XmlPullParser Error";
	
	// map each gigs's show name to a TextView in the ListView layout
    public static String[] from = new String[] { "show" };
    public static int[] to = new int[] { android.R.id.text1 }; 
	DatabaseConnector dbc;
	private Cursor cursor = null;
	int layout;
	
	private static final String ARG_SELECTED_GIG_ID = "GigListFragment_selected_gig_id";
	private static final String ARG_SELECTED_GIG_POSITION = "GigListFragment_selected_gig_position";
	private long _selectedGigId = -1;
	private int _selectedGigPosition = -1;
	
	private SpinningHalfApplication shapp;

	
	//the container activity must implement this interface in order for this
	//fragment to communicate to it user events/selections.
	public interface OnGigListSelectedListener {
		public void onGigSelected(long id, int position);
	}

	
	@Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        
        Log.i(TAG, "in onAttach()");

        //This makes sure that the container activity has implemented
        //the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnGigListSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
	
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        Log.i(TAG, "in onCreate()");
        
        shapp = SpinningHalfApplication.getInstance();
        
        //We need to use a different list item layout for devices older than Honeycomb
        this.layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
        
        if(savedInstanceState != null) {
        	this._selectedGigId = savedInstanceState.getLong(ARG_SELECTED_GIG_ID);
        	this._selectedGigPosition = savedInstanceState.getInt(ARG_SELECTED_GIG_POSITION);
        	
        	Log.i(TAG, "in onCreate() and savedInstanceState is != null. selectedGigId = " + this._selectedGigId 
        			+ " selectedGigPosition " + this._selectedGigPosition);
        	
        	return;
        }
    }

	
    @Override
    public void onStart() {
        super.onStart();
        
        Log.i(TAG, "in onStart()");

        //When in two-pane layout, set the listview to highlight the selected list item
        //(We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.view_gig_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
        
        ConnectivityManager cm = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = cm.getActiveNetworkInfo();
        
        if (networkInfo != null && networkInfo.isAvailable()) {
        	
        	//if the gig guide has already been downloaded, use the database version. dont re-download it!
        	if (SpinningHalfApplication.getInstance().getDownloadFinished()) {
        		SpinningHalfApplication _shapp = SpinningHalfApplication.getInstance();
        		DatabaseConnector _dbc = _shapp.getDatabaseConnector();
        		cursor = _dbc.getAllGigs();
        		setListAdapter(new SimpleCursorAdapter(getActivity(), layout, cursor, from, to)); // set contactView's adapter
        	} else {
        		//download a fresh copy of the gig guide
        		Log.i(TAG, "in onStart() and there is a network connection available. DOWNLOADING THE GIG GUIDE CONTENT.");
        		new DownloadWebpageText().execute(SpinningHalfApplication.SPINNINGHALF_GIGLIST_WEBSERVICE);
        	}
        } else {
        	Toast.makeText(getActivity(), "No Network Connection available. Please try again when there is one", Toast.LENGTH_LONG).show();
        }
   		
        //check to see if the gigs have already been downloaded, parsed and put into the database
        //from the onCreate() method in SpinningHalfApplication. 
        //Otherwise, start downloading again...not optimal, i know. a TODO
   		/*
        if (!shapp.getDownloadFinished()) {
        	Log.i(TAG, "in onStart(). HAVENT_FINISHED_ORIGINAL_GIGLIST_DOWNLOAD: The downloading in SpinningHalfApplication's " +
        			"onCreate() has not finished. Start Again with download.");
    	    new DownloadWebpageText().execute(SpinningHalfApplication.SPINNINGHALF_GIGLIST_WEBSERVICE);
        } 
        
         else {
        	Log.i(TAG,"in onCreate(). SUCCESSFUL_ORIGINAL_GIGLIST_DOWNLOAD: Using SpinningHalfApplication's downloaded gig content.");
	   		//DatabaseConnector dbc = shapp.getDatabaseConnector();
	   		cursor = dbc.getAllGigs();
	   		setListAdapter(new SimpleCursorAdapter(getActivity(), layout, cursor, from, to)); // set contactView's adapter
   		 
	   		if (shapp.getSelectedGigId() != -1) {
	    		Log.i(TAG, "in onStart() and after setListAdapter(). selectedGigId is " 
	    				+ shapp.getSelectedGigId() + " selectedGigPosition " + shapp.getSelectedGigPosition());
	       	    // Set the item as checked to be highlighted when in two-pane layoutshapp.getSelectedGigId()
	            //getListView().setItemChecked(_selectedGigPosition, true);
	            getListView().setItemChecked(shapp.getSelectedGigPosition(), true);
	        }
       }
       */
    }
    
    
    @Override
    public void onPause() {
    	super.onPause();
    	
    	Log.i(TAG, "in onPause");
    	
    	
    	if ( cursor != null && cursor.moveToFirst()) {
    		Log.i(TAG, "in onPause and closing cursor");
    		this.cursor.close();
    		
    		if (this.cursor.isClosed()) {
    			Log.i(TAG, "in onPause() and cursor is closed");
    		}
    	}
    	SpinningHalfApplication.getInstance().getDatabaseConnector().close();
    	
    }
    
    
    @Override 
    public void onResume() {
    	super.onResume();
    	
    	//this._selectedGigId = -1;
    	Log.i(TAG, "in onResume()");
    }

    
    @Override
    public void onStop() {
    	super.onStop();
    	
    	Log.i(TAG, "in onStop()");
    	
    	if ( cursor != null && cursor.moveToFirst()) {
    		Log.i(TAG, "in onStop() and closing cursor");
    		this.cursor.close();
    		
    		if (this.cursor.isClosed()) {
    			Log.i(TAG, "in onStop() and cursor is closed");
    		}
    	}
    	SpinningHalfApplication.getInstance().getDatabaseConnector().close();
    	
    }
    
    @Override
    public void onDestroyView() {
    	super.onDestroyView();
    	
    	Log.i(TAG, "in onDestroyView()");
    	
    	if ( cursor != null && cursor.moveToFirst()) {
    		Log.i(TAG, "in onDestroyView() and closing cursor");
    		this.cursor.close();
    		
    		if (this.cursor.isClosed()) {
    			Log.i(TAG, "in onDestroyView() and cursor is closed");
    		}
    	}
    	SpinningHalfApplication.getInstance().getDatabaseConnector().close();
    }
    
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	
    	Log.i(TAG, "in onDestroy()");
    	
    	if ( cursor != null && cursor.moveToFirst()) {
    		Log.i(TAG, "in onDestroy() and closing cursor");
    		this.cursor.close();
    		
    		if (this.cursor.isClosed()) {
    			Log.i(TAG, "in onDestroy() and cursor is closed");
    		}
    	}
    	SpinningHalfApplication.getInstance().getDatabaseConnector().close();
    }

    
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	
    	this._selectedGigId = id;
    	this._selectedGigPosition = position;
    	
    	//shapp.setSelectedGigId(id);
    	//shapp.setSelectedGigPosition(position);
    	
        //Notify the parent activity of selected item
        mCallback.onGigSelected(id, position);
        
        //Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
        Log.i(TAG, "in onListItemClick and selectedGigPosition = " + this._selectedGigPosition);
    }
	
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	
    	Log.i(TAG, "in onSaveInstanceState()");

    	
    	outState.putLong(GigListFragment.ARG_SELECTED_GIG_ID, this._selectedGigId);
    	outState.putInt(GigListFragment.ARG_SELECTED_GIG_POSITION, this._selectedGigPosition);
    	
    	if ( cursor != null && cursor.moveToFirst()) {
    		this.cursor.close();
    	}
    	SpinningHalfApplication.getInstance().getDatabaseConnector().close();
    }

    
    //Uses AsyncTask to create a task away from the main  UI thread. This task
    //takes a URL string and uses it to create an HttpUrlConnection. Once the 
    //connection has been established, the AsyncTask downloads the contents of the 
    //webpage as an InputStream. Finally, the InputStream is converted into a string, 
    //which is displayed in the UI thread by the onPostExecute method.
    private class DownloadWebpageText extends AsyncTask<String, Void, Cursor> {
    	//private final ProgressBar progress;
    	
    	//public DownloadWebpageText(final ProgressBar progress) {
    	//	this.progress = progress;
    	//}
    	
    	
    	@Override
    	protected Cursor doInBackground(String...urls) {
    		Log.i(TAG, "in doInBackground");
    		
    		//this calls onProgressUpdate() to begin the ProgressBar circle animation.
    		//publishProgress();
    		
    		Cursor cursor;
    		DatabaseConnector dbc = new DatabaseConnector(getActivity());
    		dbc.open();
    		dbc.deleteAll();
    		DownloadAndParseGigs d_p_g = new DownloadAndParseGigs();
    		cursor = d_p_g.downloadAllGigs(SpinningHalfApplication.SPINNINGHALF_GIGLIST_WEBSERVICE, dbc);
    		GigListFragment.this.cursor = cursor;
    		
    		return cursor;
    	}
    	
    	
    	@Override
    	protected void onProgressUpdate(Void... values) {
    		super.onProgressUpdate();
    		
    		//make the ProgressBar circle appear. Hey Presto.
    		//progress.setVisibility(View.VISIBLE);
    	}
    	
    	
    	//onPostExecute displays the results of the AsyncTask.
    	@SuppressWarnings("deprecation")
		@Override
    	protected void onPostExecute(Cursor cursor) {
    		super.onPostExecute(cursor);
    		Log.i(TAG, "in onPostExecute");
    		
    		String errorMessage = "";
    		int gig_show_index;
    		
    		//get rid of progress circle once you have the cursor.
    		//progress.setVisibility(View.GONE);
    		
    		if (cursor.moveToFirst()) {
    			Log.d(TAG, "in onPostExecute." + "Cursor is non-empty");
    			cursor.moveToFirst();
    			
    			gig_show_index = cursor.getColumnIndex(from[0]);
        		errorMessage = cursor.getString(gig_show_index);
    		}
    		
    		
    		if(errorMessage == IO_ERROR || errorMessage == XPP_ERROR) {
    			Toast.makeText(getActivity(), "IO ERROR or XPP ERROR. Please try again", Toast.LENGTH_LONG).show();
    		} else {
    			Log.d(TAG, "in onPostExecute. " + "Number of rows in cursor = " + cursor.getCount());
    			Log.d(TAG, "in onPostExecute. " + "First cursor(show) " + cursor.getString(cursor.getColumnIndex(from[0])));
    			Log.d(TAG, "in onPostExecute. " + "Layout id = " + layout);
    			Log.d(TAG, "in onPostExecute. " + "before setListAdapter");
    			
    		    setListAdapter(new SimpleCursorAdapter(getActivity(), layout, cursor, from, to)); // set contactView's adapter
    		    SpinningHalfApplication.getInstance().setDownloadFinished(true);
    		    Log.d(TAG, "in onPostExecute. " + "after setListAdapter");
    		    
    		    
    		    //you can only start altering the Items in the listview AFTER the setListAdapter has created the listview.
    		    //otherwise if you try to before it has been created, even in say onResume(), you get NullPointerException!
    		    ///LEARNT THE HARDWAY
    		    if (GigListFragment.this._selectedGigId != -1) {
    	    		Log.i(TAG, "in onPostExecute() and after setListAdapter(). selectedGigId is " 
    	    				+ GigListFragment.this._selectedGigId
    	    				+ " selectedGigPosition " + GigListFragment.this._selectedGigPosition);
    	    		
    	       	    //Set the item as checked to be highlighted when in two-pane layout
    	            getListView().setItemChecked(GigListFragment.this._selectedGigPosition, true);
    	        }
    	    }
    	}
    }
}
