package au.com.spinninghalf.connectingtothenetwork;

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

public class ManagementFragmentOne extends SherlockFragment {
	public static final String CAST_IRON_PINATA_FACEBOOK_URI ="http://www.facebook.com/castironpinata";
	public static final String THE_SOULENIKOES_FACEBOOK_URI ="http://www.facebook.com/pages/The-Soulenikoes/49572892044";
	public static final String TOM_MILEK_FACEBOOK_URI ="http://www.facebook.com/tommilek";
	public static final String THE_UNIVERSAL_FACEBOOK_URI ="http://www.facebook.com/theuniversalrock";
	public static final String CAPTAIN_GROOVE_FACEBOOK_URI ="http://www.facebook.com/cngroove";
	public static final String APPLY_FACEBOOK_URI ="http://www.facebook.com/spinning.half";
	
	
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.management_one_fragment, container, false);
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		//get references to all the buttons
		Button castIronPinataButton = (Button) getActivity().findViewById(R.id.managementCastIronPinataButton);
		Button theSoulenikoesButton = (Button) getActivity().findViewById(R.id.managementTheSoulenikoesButton);
		Button tomMilekButton = (Button) getActivity().findViewById(R.id.managementTomMilekButton);
		Button theUniversalButton = (Button) getActivity().findViewById(R.id.managementTheUniversalButton);
		Button captainGrooveButton = (Button) getActivity().findViewById(R.id.managementCaptainGrooveButton);
		Button applyButton = (Button) getActivity().findViewById(R.id.managementApplyButton);
		
		//assign listeners to the buttons
		castIronPinataButton.setOnClickListener(castIronPinataButtonListener);
		theSoulenikoesButton.setOnClickListener(theSoulenikoesButtonListener);
		tomMilekButton.setOnClickListener(tomMilekButtonListener);
		theUniversalButton.setOnClickListener(theUniversalButtonListener);
		captainGrooveButton.setOnClickListener(captainGrooveListener);
		applyButton.setOnClickListener(applyButtonListener);
		
		
	}
	
	
	public OnClickListener castIronPinataButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent castIronPinataFacebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementFragmentOne.CAST_IRON_PINATA_FACEBOOK_URI));
			startActivity(castIronPinataFacebookIntent);
		}
	};
	
	public OnClickListener theSoulenikoesButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent theSoulenikoesFacebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementFragmentOne.THE_SOULENIKOES_FACEBOOK_URI));
			startActivity(theSoulenikoesFacebookIntent);
		}
	};
	
	public OnClickListener tomMilekButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent tomMilekFacebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementFragmentOne.TOM_MILEK_FACEBOOK_URI));
			startActivity(tomMilekFacebookIntent);
		}
	};

	public OnClickListener theUniversalButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent theUniversalFacebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementFragmentOne.THE_UNIVERSAL_FACEBOOK_URI));
			startActivity(theUniversalFacebookIntent);
		}
	};
	
	public OnClickListener captainGrooveListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent captainGrooveFacebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementFragmentOne.CAPTAIN_GROOVE_FACEBOOK_URI));
			startActivity(captainGrooveFacebookIntent);
		}
	};
	
	public OnClickListener applyButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent applyFacebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(ManagementFragmentOne.APPLY_FACEBOOK_URI));
			startActivity(applyFacebookIntent);
		}
	};
}