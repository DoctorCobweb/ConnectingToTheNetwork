package au.com.spinninghalf.connectingtothenetwork;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;

import com.actionbarsherlock.app.SherlockFragment;

public class ContactFragmentOne extends SherlockFragment {
	private final static String TAG = "ContactFragmentOne";
	public final static String SPINNING_HALF_FACEBOOK_URI = "http://www.facebook.com/pages/Spinning-Half/227917577236619";
	public final static String SPINNING_HALF_TWITTER_URI = "https://twitter.com/spinninghalf";
	public final static String SPINNING_HALF_YOUTUBE_URI = "http://www.youtube.com/user/spinninghalfstudios?feature=mhee";
	public final static String SPINNING_HALF_WEBSITE_URI = "http://www.spinninghalf.com.au";
	
	SpinningHalfApplication shapp;
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		
		Log.i(TAG, "in onAttach()");
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		Log.i(TAG, "in onCreate()");
	}
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.contact_one_fragment, container, false);
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		Log.i(TAG, "in onStart()");
		
		shapp = SpinningHalfApplication.getInstance();
		
		//find all the button references
		Button headOfficePhoneNumberButton = (Button) getActivity().findViewById(R.id.contactHeadOfficePhoneNumberButton);
		Button headOfficeEmailButton = (Button) getActivity().findViewById(R.id.contactHeadOfficeEmailButton);
		Button rehearsalsPhoneNumberButton1 = (Button) getActivity().findViewById(R.id.contactRehearsalsPhoneNumberButton1);
		Button rehearsalsPhoneNumberButton2 = (Button) getActivity().findViewById(R.id.contactRehearsalsPhoneNumberButton2);
		Button rehearsalsEmailButton = (Button) getActivity().findViewById(R.id.contactRehearsalsEmailButton);
		
		//only assign listeners which will intent a call if the device has telephony capabilities.
		if(shapp.getTelephonyCapability()){
			//assign listeners to each button
			headOfficePhoneNumberButton.setOnClickListener(headOfficePhoneNumberButtonListener);
			headOfficeEmailButton.setOnClickListener(headOfficeEmailButtonListener);
			rehearsalsPhoneNumberButton1.setOnClickListener(rehearsalsPhoneNumberButton1Listener);
			rehearsalsPhoneNumberButton2.setOnClickListener(rehearsalsPhoneNumberButton2Listener);
			rehearsalsEmailButton.setOnClickListener(rehearsalsEmailButtonListener);
		}
		
		Button facebookButton = (Button) getActivity().findViewById(R.id.contactFacebookButton);
		Button twitterButton = (Button) getActivity().findViewById(R.id.contactTwitterButton);
		Button youtubeButton = (Button) getActivity().findViewById(R.id.contactYoutubeButton);
		Button spinningHalfButton = (Button) getActivity().findViewById(R.id.contactSpinningHalfButton);
		
		facebookButton.setOnClickListener(facebookButtonListener);
		twitterButton.setOnClickListener(twitterButtonListener);
		youtubeButton.setOnClickListener(youtubeButtonListener);
		spinningHalfButton.setOnClickListener(spinningHalfButtonListener);
		
		
	}
	
	public OnClickListener headOfficePhoneNumberButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0352221186"));
			startActivity(intent);
		}
	};
	
	public OnClickListener headOfficeEmailButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try{
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("plain/text");
				
				intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"info@spinninghalf.com.au"});
				intent.putExtra(Intent.EXTRA_SUBJECT, "GENERAL INQUIRY (from Android App)");
				intent.putExtra(Intent.EXTRA_TEXT, "G'day Spinning Half,");
				startActivity(intent);
				} catch(ActivityNotFoundException e) {
					Log.i(TAG, "email Activity not found.");
				}
		}
	};
	
	public OnClickListener rehearsalsPhoneNumberButton1Listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0352221186"));
			startActivity(intent);
		}
	};
	
	public OnClickListener rehearsalsPhoneNumberButton2Listener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:0421866977"));
			startActivity(intent);
		}
	};
	
	public OnClickListener rehearsalsEmailButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try{
				Intent intent = new Intent(Intent.ACTION_SEND);
				intent.setType("plain/text");
				
				intent.putExtra(Intent.EXTRA_EMAIL, new String[] {"bookings@spinninghalf.com.au"});
				intent.putExtra(Intent.EXTRA_SUBJECT, "REHEARSAL INQUIRY (from Android App)");
				intent.putExtra(Intent.EXTRA_TEXT, "G'day Spinning Half,");
				startActivity(intent);
				} catch(ActivityNotFoundException e) {
					Log.i(TAG, "email Activity not found.");
				}
		}
	};
	
	public OnClickListener facebookButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ContactFragmentOne.SPINNING_HALF_FACEBOOK_URI));
			startActivity(intent);
		}
	};
	
	public OnClickListener twitterButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ContactFragmentOne.SPINNING_HALF_TWITTER_URI));
			startActivity(intent);
		}
	};
	
	public OnClickListener youtubeButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ContactFragmentOne.SPINNING_HALF_YOUTUBE_URI));
			startActivity(intent);
		}
	};
	
	public OnClickListener spinningHalfButtonListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(ContactFragmentOne.SPINNING_HALF_WEBSITE_URI));
			startActivity(intent);
		}
	};
}
