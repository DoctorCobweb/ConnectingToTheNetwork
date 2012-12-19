package au.com.spinninghalf.connectingtothenetwork;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ServicesFunctionPackagesActivity extends Activity {
	public final static String TAG = "ServicesFunctionPackagesActivity";
	public final static String SERVICES_FUNCTION_PACKAGES_URI = "http://www.spinninghalf.com.au";
	public final static String ARG_SELECTED_IMAGE = "au.com.spinninghalf.connectingtothenetwork.ServicesFunctionPackagesActivity.selectedimage";
	public final static String ARG_SELECTED_IMAGE_POSITION = "au.com.spinninghalf.connectingtothenetwork.ServicesFunctionPackagesActivity.selectedposition";
	
	// references to our images
    public Integer[] mThumbIds = {
            R.drawable.function_packages, R.drawable.function_packages1,
            R.drawable.function_packages2
    };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.services_function_packages);
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		Log.i(TAG, "in onStart()");
		
		 EasyTracker.getInstance().activityStart(this); // Add this method.
		 
		GridView gridview = (GridView) findViewById(R.id.servicesFunctionPackageGridView);
        gridview.setAdapter(new ServicesFunctionPackagesImageAdapter(this));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	
            	Intent fullScreenImageIntent = new Intent(ServicesFunctionPackagesActivity.this, ServicesFunctionPackagesFullScreenImage.class);
            	
            	//put extra data into the Intent. Which is the id of the particular image selected in the GridView
            	fullScreenImageIntent.putExtra(ServicesFunctionPackagesActivity.ARG_SELECTED_IMAGE, mThumbIds[position]);
            	
            	fullScreenImageIntent.putExtra(ServicesFunctionPackagesActivity.ARG_SELECTED_IMAGE_POSITION, position);
            	
                
            	//launch the activity
            	ServicesFunctionPackagesActivity.this.startActivity(fullScreenImageIntent);
            	
            }
        });
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		EasyTracker.getInstance().activityStop(this); // Add this method.
	}
}
