package au.com.spinninghalf.connectingtothenetwork;

import android.app.Activity;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SimpleCursorAdapter;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GigListFragment extends ListFragment {
	OnGigListSelectedListener mCallback;
	
	public static final String ROW_ID = "row_id"; // Intent extra key
	public static final String TAG = "GigListFragment";
	private static final String ERROR = "Error";
	private static final String IO_ERROR = "IO Error";
	private static final String XPP_ERROR = "XmlPullParser Error";
	private String stringUrl = null; 
	// map each gigs's show name to a TextView in the ListView layout
    public static String[] from = new String[] { "show" };
    public static int[] to = new int[] { android.R.id.text1 }; 
	//private TextView idTextView;
	//private SimpleCursorAdapter gigAdapter; // adapter for ListView
	DatabaseConnector dbc;
	private Cursor cursor = null;
	int layout;
	long selectedGigId = -1;
	long _selectedGigId = -1;
	int _selectedGigPosition = -1;
	int selectedGigPosition = -1;
	private static final String ARG_ID = "GigListFragment_id";
	private static final String ARG_POS = "GigListFragment_position";
	
	
	//the container activity must implement this interface in order for this
	//fragment to communicate to it user events/selections.
	public interface OnGigListSelectedListener {
		public void onGigSelected(long id);
	}
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // We need to use a different list item layout for devices older than Honeycomb
        this.layout = Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB ?
                android.R.layout.simple_list_item_activated_1 : android.R.layout.simple_list_item_1;
        
        if(savedInstanceState != null) {
        	_selectedGigId = savedInstanceState.getLong(ARG_ID);
        	_selectedGigPosition = savedInstanceState.getInt(ARG_POS);
        	Log.i(TAG, "in onCreate() and savedInstanceState is != null. _selectedGigId = " + _selectedGigId + " _selectedGigPosition " + _selectedGigPosition);
        }

        
        new DownloadWebpageText().execute(SpinningHalfApplication.SPINNINGHALF_GIGLIST_WEBSERVICE);
    }

    @Override
    public void onStart() {
        super.onStart();

        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.view_gig_fragment) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
        
        
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception.
        try {
            mCallback = (OnGigListSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }
    
    @Override 
    public void onResume() {
    	super.onResume();
    }
    
    @Override
    public void onDestroy() {
    	super.onDestroy();
    	
    	this.cursor.close();
    	this.dbc.close();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
    	this._selectedGigId = id;
    	this._selectedGigPosition = position;
        // Notify the parent activity of selected item
        mCallback.onGigSelected(id);
        
        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
        Log.i(TAG, "selectedGigPosiont = " + _selectedGigPosition);
    }
	
    
    @Override
    public void onSaveInstanceState(Bundle outState) {
    	super.onSaveInstanceState(outState);
    	
    	outState.putLong(ARG_ID, _selectedGigId);
    	outState.putInt(ARG_POS, _selectedGigPosition);
    }
	
  //Uses AsyncTask to create a task away from the main  UI thread. This task
    //takes a URL string and uses it to create an HttpUrlConnection. Once the 
    //connection has been established, the AsyncTask downloads the contents of the 
    //webpage as an InputStream. Finally, the InputStream is converted into a string, 
    //which is displayed in the UI thread by the onPostExecute method.
    private class DownloadWebpageText extends AsyncTask<String, Void, Cursor> {
    	
    	//get reference to the hosting class GigListActivity in order to call getApplication()
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
    		dbc.deleteAll();
    		DownloadAndParseGigs d_p_g = new DownloadAndParseGigs();
    		cursor = d_p_g.downloadAllGigs(SpinningHalfApplication.SPINNINGHALF_GIGLIST_WEBSERVICE, dbc);
    		GigListFragment.this.cursor = cursor;
    		GigListFragment.this.dbc = dbc;
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
    		//GigListFragment.this.cursor = cursor;
    		Log.i(TAG, "in onPostExecute");
    		
    		//get rid of progress circle once you have the cursor.
    		//progress.setVisibility(View.GONE);
    		
    		cursor.moveToFirst();
    		int show_index = cursor.getColumnIndex(from[0]);
    		String errorMessage = cursor.getString(show_index);
			
    		if(errorMessage == IO_ERROR || errorMessage == XPP_ERROR) {
    			//display a msg that there was an error with the download.
    			//idTextView.setText(errorMessage);
    		} else {
    			Log.d(TAG, "in onPostExecute. " + "Number of rows in cursor = " + cursor.getCount());
    			Log.d(TAG, "in onPostExecute. " + "First cursor(show) " + cursor.getString(show_index));
    			Log.d(TAG, "in onPostExecute. " + "Layout id = " + layout);
    			
    			if (cursor.moveToFirst()) {
    				Log.d(TAG, "in onPostExecute." + "Cursor is non-empty");
    			}
    			Log.d(TAG, "in onPostExecute. " + "before setListAdapter");
    		    setListAdapter(new SimpleCursorAdapter(getActivity(), layout, cursor, from, to)); // set contactView's adapter
    		    Log.d(TAG, "in onPostExecute. " + "after setListAdapter");
    		    
    		    
    		    //you can only start altering the Items in the listview AFTER the setListAdapter has created the listview.
    		    //otherwise if you try to before it has been created, even in say onResume(), you get NullPointerException!
    		    ///LEARNT THE HARDWAY
    		    if (_selectedGigId != -1) {
    	    		Log.i(TAG, "in onPostExecute() and after setListAdapter(). _selectedGigId is " + _selectedGigId + " _selectedGigPosition " + _selectedGigPosition);
    	       	 // Set the item as checked to be highlighted when in two-pane layout
    	            getListView().setItemChecked(_selectedGigPosition, true);
    	       }
    		}
    	}
    }
}
