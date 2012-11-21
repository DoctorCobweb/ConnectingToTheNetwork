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

public class ManagementCastIronPinataActivity extends Activity {
	public static final String CAST_IRON_PINATA_SOUNDCLOUD = "http://www.triplejunearthed.com/CastIronPinata";
	public static final String CAST_IRON_PINATA_YOUTUBE = "http://www.youtube.com/user/CASTIRONPINATA?feature=watch";
	public static final String CAST_IRON_PINATA_FACEBOOK = "http://www.facebook.com/castironpinata";
	
	
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
		setContentView(R.layout.management_cast_iron_pinata);
		
		//find references to each button
		Button informationButton = (Button) findViewById(R.id.managementCastIronPinataButton1);
		Button listenButton = (Button) findViewById(R.id.managementCastIronPinataButton2);
		Button watchButton = (Button) findViewById(R.id.managementCastIronPinataButton3);
		Button socialButton = (Button) findViewById(R.id.managementCastIronPinataButton4);
		
		//attach the OnClickListeners to each button.
		informationButton.setOnClickListener(informationButtonListener);
		listenButton.setOnClickListener(listenButtonListener);
		watchButton.setOnClickListener(watchButtonListener);
		socialButton.setOnClickListener(socialButtonListener);
	}
	
	public OnClickListener informationButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(ManagementCastIronPinataActivity.this, ManagementCastIronPinataInformationActivity.class);
			startActivity(intent);
		}
	};
	
	public OnClickListener listenButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementCastIronPinataActivity.CAST_IRON_PINATA_SOUNDCLOUD));
			startActivity(intent);
		}
	};
	
	public OnClickListener watchButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementCastIronPinataActivity.CAST_IRON_PINATA_YOUTUBE));
			startActivity(intent);
		}
	};
	
	public OnClickListener socialButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementCastIronPinataActivity.CAST_IRON_PINATA_FACEBOOK));
			startActivity(intent);
		}
	};

}
