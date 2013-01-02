package au.com.spinninghalf.connectingtothenetwork;

import com.google.analytics.tracking.android.EasyTracker;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;

public class ManagementLeikographyActivity extends Activity {
	public final static String TAG = "ManagementLeikographyActivity";
	public final static String ARG_SELECTED_IMAGE = "au.com.spinninghalf.connectingtothenetwork.ManagementLeikographyActivity.selectedimage";
	public final static String ARG_SELECTED_IMAGE_POSITION = "au.com.spinninghalf.connectingtothenetwork.ManagementLeikographyActivity.selectedposition";
	public final static String LEIKOGRAPHY_FACEBOOK_URI = "https://www.facebook.com/leikography";
		
	
	// references to our images
    public Integer[] mThumbIds = {
            R.drawable.management_leikography, R.drawable.management_leikography1,
            R.drawable.management_leikography2, R.drawable.management_leikography3
    };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.management_leikography);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		Log.i(TAG, "in onStart()");
		
		EasyTracker.getInstance().activityStart(this); // Add this method.
		
		Button leikographyButton = (Button) findViewById(R.id.managementLeikographyFacebook);
		leikographyButton.setOnClickListener(leikographyButtonListener);
		
		GridView gridview = (GridView) findViewById(R.id.managementLeikographyGridView);
        gridview.setAdapter(new ManagementLeikographyImageAdapter(this));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	
            	Intent fullScreenImageIntent = new Intent(ManagementLeikographyActivity.this, ManagementLeikographyFullScreenImage.class);
            	
            	//put extra data into the Intent. Which is the id of the particular image selected in the GridView
            	fullScreenImageIntent.putExtra(ManagementLeikographyActivity.ARG_SELECTED_IMAGE, mThumbIds[position]);
            	
            	fullScreenImageIntent.putExtra(ManagementLeikographyActivity.ARG_SELECTED_IMAGE_POSITION, position);
            	
                
            	//launch the activity
            	ManagementLeikographyActivity.this.startActivity(fullScreenImageIntent);
            	
            }
        });
		
		
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		EasyTracker.getInstance().activityStop(this); // Add this method.
	}
	
	public OnClickListener leikographyButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementLeikographyActivity.LEIKOGRAPHY_FACEBOOK_URI));
			startActivity(intent);
		}
		
	};

}
