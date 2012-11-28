package au.com.spinninghalf.connectingtothenetwork;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class ManagementCaptainGrooveActivity extends Activity {
	public static final String CAPTAIN_GROOVE_SOUNDCLOUD = "http://www.triplejunearthed.com/CaptainGroove";
	public static final String CAPTAIN_GROOVE_YOUTUBE = "http://www.youtube.com/user/madigroove?feature=watch";
	public static final String CAPTAIN_GROOVE_FACEBOOK = "https://www.facebook.com/cngroove";
	
	
	//private ArrayList<String> bandLinkCategories;
	//private ArrayAdapter<String> bandLinkCategoriesArrayAdapter;
	//private static final String[] bandLinkCategories = {"Information", "Listen", "Watch", "Social" };
	Button informationButton;
	Button listenButton;
	Button watchButton;
	Button socialButton;
	
	@Override 
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.management_captain_groove);
		
		//find references to each button
		Button informationButton = (Button) findViewById(R.id.managementCaptainGrooveButton1);
		Button listenButton = (Button) findViewById(R.id.managementCaptainGrooveButton2);
		Button watchButton = (Button) findViewById(R.id.managementCaptainGrooveButton3);
		Button socialButton = (Button) findViewById(R.id.managementCaptainGrooveButton4);
		
		//attach the OnClickListeners to each button.
		informationButton.setOnClickListener(informationButtonListener);
		listenButton.setOnClickListener(listenButtonListener);
		watchButton.setOnClickListener(watchButtonListener);
		socialButton.setOnClickListener(socialButtonListener);
	}
	
	public OnClickListener informationButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ManagementCaptainGrooveActivity.this, ManagementCaptainGrooveInformationActivity.class);
			startActivity(intent);
		}
	};
	
	public OnClickListener listenButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementCaptainGrooveActivity.CAPTAIN_GROOVE_SOUNDCLOUD));
			startActivity(intent);
		}
	};
	
	public OnClickListener watchButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementCaptainGrooveActivity.CAPTAIN_GROOVE_YOUTUBE));
			startActivity(intent);
		}
	};
	
	public OnClickListener socialButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementCaptainGrooveActivity.CAPTAIN_GROOVE_FACEBOOK));
			startActivity(intent);
		}
	};

}