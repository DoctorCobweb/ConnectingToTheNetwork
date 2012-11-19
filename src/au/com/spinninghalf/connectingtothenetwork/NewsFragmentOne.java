package au.com.spinninghalf.connectingtothenetwork;

//TODO
//reattach listeners to the buttons after screen rotation



import android.view.View;
import android.view.View.OnClickListener;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class NewsFragmentOne extends SherlockFragment {
	public final static String SPINNING_HALF_FACEBOOK_URI = "http://www.facebook.com/pages/Spinning-Half/227917577236619";
	public final static String SPINNING_HALF_TWITTER_URI = "https://twitter.com/spinninghalf";
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.news_one_fragment, container, false);
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		
		Button facebookButton = (Button) getActivity().findViewById(R.id.newsFragmentOneFacebookButton);
		facebookButton.setOnClickListener(newsFacebookButtonListener);
		
		Button twitterButton = (Button) getActivity().findViewById(R.id.newsFragmentOneTwitterButton);
		twitterButton.setOnClickListener(newsTwitterButtonListener);
	}
	
	
	
//-------------------  BUTTON LISTENERS------------------------------------------------------------------------------------------
	
	public OnClickListener newsFacebookButtonListener = new OnClickListener(){
		
		@Override
		public void onClick(View view) {
			Intent facebookIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NewsFragmentOne.SPINNING_HALF_FACEBOOK_URI));
			startActivity(facebookIntent);
		}
	};
	
	public OnClickListener newsTwitterButtonListener = new OnClickListener(){
		
		@Override
		public void onClick(View view) {
			Intent twitterIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(NewsFragmentOne.SPINNING_HALF_TWITTER_URI));
			startActivity(twitterIntent);
		}
	};
	
//---------------------------------------------------------------------------------------------------------------------------------
	
	
}
