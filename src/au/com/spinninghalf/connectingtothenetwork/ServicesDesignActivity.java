package au.com.spinninghalf.connectingtothenetwork;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class ServicesDesignActivity extends Activity {
	public final static String TAG = "ServicesDesignActivity";
	public final static String SERVICES_DESIGN_URI = "http://www.spinninghalf.com.au";
	public final static String ARG_SELECTED_IMAGE = "au.com.spinninghalf.connectingtothenetwork.ServicesDesignActivity.selectedimage";
	public final static String ARG_SELECTED_IMAGE_POSITION = "au.com.spinninghalf.connectingtothenetwork.ServicesDesignActivity.selectedposition";
	
	// references to our images
    public Integer[] mThumbIds = {
            R.drawable.design, R.drawable.design1,
            R.drawable.design2, R.drawable.design3
    };
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.services_design);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		Log.i(TAG, "in onStart()");
		
		GridView gridview = (GridView) findViewById(R.id.servicesDesignGridView);
        gridview.setAdapter(new ServicesDesignImageAdapter(this));

        gridview.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
            	
            	Intent fullScreenImageIntent = new Intent(ServicesDesignActivity.this, ServicesDesignFullScreenImage.class);
            	
            	//put extra data into the Intent. Which is the id of the particular image selected in the GridView
            	fullScreenImageIntent.putExtra(ServicesDesignActivity.ARG_SELECTED_IMAGE, mThumbIds[position]);
            	
            	fullScreenImageIntent.putExtra(ServicesDesignActivity.ARG_SELECTED_IMAGE_POSITION, position);
            	
                
            	//launch the activity
            	ServicesDesignActivity.this.startActivity(fullScreenImageIntent);
            	
            }
        });
		
		
	}


}
