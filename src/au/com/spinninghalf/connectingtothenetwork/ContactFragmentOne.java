package au.com.spinninghalf.connectingtothenetwork;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class ContactFragmentOne extends SherlockFragment {
	private final static String TAG = "ContactFragmentOne";
	
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
	}
}
