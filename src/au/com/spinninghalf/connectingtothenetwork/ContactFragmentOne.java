package au.com.spinninghalf.connectingtothenetwork;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class ContactFragmentOne extends SherlockFragment {
	private final static String TAG = "ContactFragmentOne";
	public final static String SPINNING_HALF_FACEBOOK_URI = "http://www.facebook.com/pages/Spinning-Half/227917577236619";
	public final static String SPINNING_HALF_TWITTER_URI = "https://twitter.com/spinninghalf";
	public final static String SPINNING_HALF_YOUTUBE_URI = "http://www.youtube.com/user/spinninghalfstudios?feature=mhee";
	public final static String SPINNING_HALF_WEBSITE_URI = "http://www.spinninghalf.com.au";
	
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
		
		//find all the button references
		Button facebookButton = (Button) getActivity().findViewById(R.id.contactFacebookButton);
		Button twitterButton = (Button) getActivity().findViewById(R.id.contactTwitterButton);
		Button youtubeButton = (Button) getActivity().findViewById(R.id.contactYoutubeButton);
		Button spinningHalfButton = (Button) getActivity().findViewById(R.id.contactSpinningHalfButton);
		
		//assign listeners to each button
		facebookButton.setOnClickListener(facebookButtonListener);
		twitterButton.setOnClickListener(twitterButtonListener);
		youtubeButton.setOnClickListener(youtubeButtonListener);
		spinningHalfButton.setOnClickListener(spinningHalfButtonListener);
	}
	
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
