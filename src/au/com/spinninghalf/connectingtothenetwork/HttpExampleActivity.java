package au.com.spinninghalf.connectingtothenetwork;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

public class HttpExampleActivity extends Activity {
	private static final String DEBUG_TAG = "HttpExample";
	private EditText urlText;
	private TextView idTextView;
	private TextView authorTextView;
	private TextView dateTextView;
	private TextView descriptionTextView;
	private TextView headlineTextView;
	private TextView priceTextView;
	private TextView tixUrlTextView;
	private Button goButton;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        urlText = (EditText) findViewById(R.id.myUrl);
        goButton = (Button) findViewById(R.id.myOnlyButton);
        idTextView = (TextView) findViewById(R.id.idTextView);
        authorTextView = (TextView) findViewById(R.id.authorTextView);
        dateTextView = (TextView) findViewById(R.id.dateTextView);
        descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
        headlineTextView = (TextView) findViewById(R.id.headlineTextView);
        priceTextView = (TextView) findViewById(R.id.priceTextView);
        tixUrlTextView = (TextView) findViewById(R.id.tixUrlTextView);
        
        //register the listener for the button.
        goButton.setOnClickListener(youClickedMeListener);
    }
    
    public OnClickListener youClickedMeListener = new OnClickListener() {
    	//when user clicks button, call AsyncTask.
    	//Before attempting to fetch the URL, makes sure that there is network connection
    	@Override
    	public void onClick(View view) {
    		
    		//Get the URL from the UI's text field.
    		String stringUrl = urlText.getText().toString();
    		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    		if (networkInfo != null && networkInfo.isConnected()) {
    			new DownloadWebpageText().execute(stringUrl);
    		} else {
    			idTextView.setText("No network connection available");
    		}
    	}
    };
    
    //Uses AsyncTask to create a task away from the main  UI thread. This task
    //takes a URL string and uses it to create an HttpUrlConnection. Once the 
    //connection has been established, the AsyncTask downloads the contents of the 
    //webpage as an InputStream. Finally, the InputStream is converted into a string, 
    //which is displayed in the UI thread by the onPostExecute method.
    private class DownloadWebpageText extends AsyncTask<String, Void, ArrayList<String>> {
    	@Override
    	protected ArrayList<String> doInBackground(String...urls) {
    		
    		//params comes from the execute() call: params[0] is the url.
    		try {
    			return downloadUrl(urls[0]);
    		} catch (IOException e) {
    			ArrayList<String> error = new ArrayList<String>(1);
    			error.add("IOException : Unable to retrieve web page. URL may be invalid.");
    			return error;
    		} catch (XmlPullParserException xe) {
    			ArrayList<String> error = new ArrayList<String>(1);
    			error.add("XmlPullParserException : Unable to retrieve web page. URL may be invalid.");
    			return error;
    			
    		}
    	}
    	//onPostExecute displays the results of the AsyncTask.
    	@Override
    	protected void onPostExecute(ArrayList<String> result) {
    		idTextView.setText(result.get(0));
    		authorTextView.setText(result.get(1));
    		dateTextView.setText(result.get(2));
    		descriptionTextView.setText(result.get(3));
    		headlineTextView.setText(result.get(4));
    		priceTextView.setText(result.get(5));
    		tixUrlTextView.setText(result.get(6));
    	}
    }
    
    //given a URL, establishes an HttpURLConnection and retrieves
    //that web page content as an InputStream, which it returns
    //as a string.
    private ArrayList<String> downloadUrl(String myUrl) throws IOException, XmlPullParserException {
    	InputStream is = null;
    	final ArrayList<String> results = new ArrayList<String>(1000);
    	//Only display the first 500 characters of the retrieved 
    	//web page content.
    	int len = 1000;
    	
    	try {
    		URL url = new URL(myUrl);
    		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    		conn.setReadTimeout(1000); /* milliseconds */
    		conn.setConnectTimeout(15000); /* milliseconds */
    		conn.setRequestMethod("GET");
    		conn.setDoInput(true);
    		//Starts the query
    		conn.connect();
    		int response = conn.getResponseCode();
    		Log.d(DEBUG_TAG, "This response is: " + response);
    		is = conn.getInputStream();
    		
    		//parse the xml
    		XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
    		factory.setNamespaceAware(true);
    		XmlPullParser xpp = factory.newPullParser();
    		
    		xpp.setInput(is, null);
    		
    		int eventType = xpp.getEventType();
    		String currentTag = null;
    		String currentTag1 = null;
    		//Integer id = null;
    		String gig_id = null;
    		String author = null;
    		String date = null;
    		String description = null;
    		String show = null;
    		String price = null;
    		String tixUrl = null;
    		
    		while (eventType != XmlPullParser.END_DOCUMENT) {
    			if (eventType == XmlPullParser.START_TAG) {
    				currentTag = xpp.getName();
    				
    				if ("gig".equals(currentTag)) {
    					Log.i(DEBUG_TAG, "In gig start tag? " + currentTag);
    					//gig_id = xpp.getAttributeValue(0);
    					gig_id = xpp.getAttributeValue(0);
    					Log.i(DEBUG_TAG, "in the gig element: " + gig_id);
    				}
    				Log.i(DEBUG_TAG, currentTag);
    			} else if (eventType == XmlPullParser.TEXT) {
    				if ("author".equals(currentTag)) {
    					author = xpp.getText();
    					//Log.i(DEBUG_TAG, "first_authore");
    					//Log.i(DEBUG_TAG, author + " 2");
    				} else if ( "date".equals(currentTag)) {
    					if (xpp.getText() != null) {
    					date = xpp.getText();
    					}
    					//Log.i(DEBUG_TAG, "first_date");
    					//Log.i(DEBUG_TAG, date + " 3");
    				} else if ("description".equals(currentTag)) {
    					if (xpp.getText() != null) {
    				        //Log.i(DEBUG_TAG, "1: " + xpp.getText());
    				        //Log.i(DEBUG_TAG, "2: " + xpp.getText());
    					    description = xpp.getText();
    					}
    					//Log.i(DEBUG_TAG, "first_description");
    					//Log.i(DEBUG_TAG, description + " 4");
    				} else if ("show".equals(currentTag)) {
    					if (xpp.getText() != null) {
    				    show = xpp.getText();
    					}
    					//Log.i(DEBUG_TAG, "first_show");
    					//Log.i(DEBUG_TAG, show + " 5");
    				} else if ("price".equals(currentTag)) {
    					if (xpp.getText() != null) {
    					price = xpp.getText();
    					}
    					//Log.i(DEBUG_TAG, "first_price");
    					//Log.i(DEBUG_TAG, price + " 6");
    				} else if ("tixUrl".equals(currentTag)) {
    					if (xpp.getText() != null) {
    					tixUrl = xpp.getText();
    					}
    					//Log.i(DEBUG_TAG, "first_tixurl");
    					//Log.i(DEBUG_TAG, tixUrl + " 7");
    				}
    			} else if (eventType == XmlPullParser.END_TAG) {
    				//Log.i(DEBUG_TAG, "in end tag");
    				if ("gig".equals(xpp.getName())) {
    					//Log.i(DEBUG_TAG, tixUrl + "hi");
    					results.add(gig_id);
    					results.add(author);
    					results.add(date);
    					results.add(description);
    					results.add(show);
    					results.add(price);
    					results.add(tixUrl);
    				}
    			}
    			//Log.i(DEBUG_TAG, "before next()");
    			eventType = xpp.next();
    			//Log.i(DEBUG_TAG, Integer.toString(eventType));
    			//currentTag1 = xpp.getName();
    			//Log.i(DEBUG_TAG, "currentTag1: "+ currentTag1);
    		}
    		return results;
    		
    		/*
    		//convert Input Stream into a string.
    		String contentAsString = readIt(is, len);
    		
    		String[] contentAsStringArray[] = new String[];
    		return contentAsStringArray[];
    		*/
    	//Makes sure the InputStream is closed after the app	
    	//is finished using it.	
    	}  finally {
    		if (is != null) {
    			is.close();
    		}
    	}
    	
    }
    
    //Reads an InputStream and converts it into a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
    	Reader reader = null;
    	reader = new InputStreamReader(stream, "UTF-8");
    	char[] buffer = new char[len];
    	reader.read(buffer);
    	return new String(buffer);
    }
}