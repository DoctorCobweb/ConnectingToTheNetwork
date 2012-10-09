package au.com.spinninghalf.connectingtothenetwork;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;

import java.io.IOException;

public class HttpExampleActivity extends Activity {
	
	private static final String DEBUG_TAG = "HttpExample";
	public static final String GIG_LIST_URL_KEY = "au.com.spinninghalf.connectingtothenetwork.giglisturl";
	private static final String SPINNINGHALF_GIGLIST_WEBSERVICE = "http://www.spinning-half-jersey-jaxrs.appspot.com/rest/gigs";
	//private EditText urlText;
	private TextView idTextView;
	private Button goButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        //urlText = (EditText) findViewById(R.id.myUrl);
        goButton = (Button) findViewById(R.id.myOnlyButton);
        
        
        idTextView = (TextView) findViewById(R.id.idTextView);
        
        
        //register the listener for the button.
        goButton.setOnClickListener(youClickedMeListener);
    }
    
    public OnClickListener youClickedMeListener = new OnClickListener() {
    	//when user clicks button, call AsyncTask.
    	//Before attempting to fetch the URL, makes sure that there is network connection
    	@Override
    	public void onClick(View view) {
    		
    		//Get the URL from the UI's text field.
    		//String stringUrl = urlText.getText().toString();
    		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    		if (networkInfo != null && networkInfo.isConnected()) {
    			Intent startGigList = new Intent(getApplicationContext(), GigListActivity.class);
    			startGigList.putExtra(GIG_LIST_URL_KEY, SPINNINGHALF_GIGLIST_WEBSERVICE);
    			startActivity(startGigList);
    		} else {
    			idTextView.setText("No network connection available");
    		}
    	}
    };
}