package au.com.spinninghalf.connectingtothenetwork;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.actionbarsherlock.app.SherlockFragment;

public class RehearsalsFragmentOne extends SherlockFragment {
	public final static String TAG = "RehearsalsFragmentOne";
	public final static String REHEARSALS_URI = "http://www.spinninghalf.com.au";
	public final static String ARG_SELECTED_IMAGE = "au.com.spinninghalf.connectingtothenetwork.RehearsalsFragmentOne.selectedimage";
	public final static String ARG_SELECTED_IMAGE_POSITION = "au.com.spinninghalf.connectingtothenetwork.RehearsalsFragmentOne.selectedposition";
	
	// references to our images
    public Integer[] mThumbIds = {
            R.drawable.rehearsals, R.drawable.rehearsals1,
            R.drawable.rehearsals2, R.drawable.rehearsals3,
            
    };
	
	@Override
	public void onCreate(Bundle onSavedInstanceState) {
		super.onCreate(onSavedInstanceState);
		
		Log.i(TAG, "in onCreate()");
		
		
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		
		Log.i(TAG, "in onCreateView()");
		
		View view = inflater.inflate(R.layout.rehearsals_one_fragment, container, false);
		return view;
	}
	
	
	@Override
	public void onStart() {
		super.onStart();
		
		Log.i(TAG, "in onStart()");
		
		GridView gridview = (GridView) getActivity().findViewById(R.id.rehearsalsGridView);
        gridview.setAdapter(new RehearsalsImageAdapter(getActivity()));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	
            	Intent fullScreenImageIntent = new Intent(getActivity(), RehearsalsFullScreenImage.class);
            	
            	//put extra data into the Intent. Which is the id of the particular image selected in the GridView
            	fullScreenImageIntent.putExtra(RehearsalsFragmentOne.ARG_SELECTED_IMAGE, mThumbIds[position]);
            	
            	fullScreenImageIntent.putExtra(ARG_SELECTED_IMAGE_POSITION, position);
            	
                
            	//launch the activity
            	RehearsalsFragmentOne.this.startActivity(fullScreenImageIntent);
            	
            }
        });
		
	}
	
	
	
	
}