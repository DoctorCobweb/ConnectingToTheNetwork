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
	final static String ARG_ID = "id";
	final static String ARG_INIT = "init";
	long mCurrentId = -1;
	int mInit = -1;
	TextView showTextView;
    TextView dateTextView;
    TextView descriptionTextView;
    TextView priceTextView;
    TextView  tixUrlTextView;
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, 
        Bundle savedInstanceState) {
		
		//first time user has opened the gig listing page
		//set the ViewGigFragment to a splash screen
		//if(savedInstanceState == null || savedInstanceState.getInt(ARG_INIT) == -1 ) {
		//	mInit = 1;
		//	return inflater.inflate(R.layout.view_gig_splash_screen, container, false);
		//}

        // If activity recreated (such as from screen rotate), restore
        // the previous article selection set by onSaveInstanceState().
        // This is primarily necessary when in the two-pane layout.
        if (savedInstanceState != null) {
        	mCurrentId = savedInstanceState.getLong(ARG_ID);
        }

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.view_gig, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
        Bundle args = getArguments();
        if (args != null) {
            // Set article based on argument passed in
            updateGigView(args.getLong(ARG_ID));
        } else if (mCurrentId != -1) {
            // Set article based on saved instance state defined during onCreateView
            updateGigView(mCurrentId);
        }
    }
	
	public void updateGigView(long id) {
		//Activity activity;
		// get the TextViews
	    showTextView = (TextView) getActivity().findViewById(R.id.showTextView);
	    dateTextView = (TextView) getActivity().findViewById(R.id.dateTextView);
	    descriptionTextView = (TextView) getActivity().findViewById(R.id.descriptionTextView);
	    priceTextView = (TextView) getActivity().findViewById(R.id.priceTextView);
	    tixUrlTextView = (TextView) getActivity().findViewById(R.id.tixUrlTextView);
		
	    if(showTextView == null) {
	    Log.i("in updateGigView ","showTextView is null");
	    }
	    
		new LoadContactTask().execute(id);
		
		mCurrentId = id;
	}
	
	// performs database query outside GUI thread
	   private class LoadContactTask extends AsyncTask<Long, Object, Cursor> 
	   {
	      DatabaseConnector databaseConnector = 
	         new DatabaseConnector(getActivity());

	      // perform the database access
	      @Override
	      protected Cursor doInBackground(Long... params)
	      {
	         databaseConnector.open();
	         
	         // get a cursor containing all data on given entry
	         return databaseConnector.getOneContact(params[0]);
	      } // end method doInBackground

	      // use the Cursor returned from the doInBackground method
	      @Override
	      protected void onPostExecute(Cursor result)
	      {
	         super.onPostExecute(result);
	   
	         result.moveToFirst(); // move to the first item 
	   
	         // get the column index for each data item
	         int showIndex = result.getColumnIndex("show");
	         int dateIndex = result.getColumnIndex("date");
	         int descriptionIndex = result.getColumnIndex("description");
	         int priceIndex = result.getColumnIndex("price");
	         int tixUrlIndex = result.getColumnIndex("tixUrl");
	   
	         // fill TextViews with the retrieved data
	         showTextView.setText(result.getString(showIndex));
	         dateTextView.setText(result.getString(dateIndex));
	         descriptionTextView.setText(result.getString(descriptionIndex));
	         priceTextView.setText(result.getString(priceIndex));
	         tixUrlTextView.setText(result.getString(tixUrlIndex));
	   
	         result.close(); // close the result cursor
	         databaseConnector.close(); // close database connection
	      } // end method onPostExecute
	   } // end class LoadContactTask
	
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putLong(ARG_ID, mCurrentId);
        //outState.putInt(ARG_INIT, mInit);
    }

}
