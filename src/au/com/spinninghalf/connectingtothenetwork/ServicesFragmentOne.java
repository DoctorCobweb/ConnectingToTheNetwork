package au.com.spinninghalf.connectingtothenetwork;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.actionbarsherlock.app.SherlockFragment;

public class ServicesFragmentOne extends SherlockFragment {
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.services_one_fragment, container, false);
		return view;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		//get references to all the buttons
		Button functionPackagesButton = (Button) getActivity().findViewById(R.id.servicesFunctionPackagesButton);
		Button publicityButton = (Button) getActivity().findViewById(R.id.servicesPublicityButton);
		Button distributionButton = (Button) getActivity().findViewById(R.id.servicesDistributionButton);
		Button trainingButton = (Button) getActivity().findViewById(R.id.servicesTrainingButton);
		Button photographyButton = (Button) getActivity().findViewById(R.id.servicesPhotographyButton);
		Button designButton = (Button) getActivity().findViewById(R.id.servicesDesignButton);
		
		//assign listeners to the buttons
		functionPackagesButton.setOnClickListener(functionPackagesButtonListener);
		publicityButton.setOnClickListener(publicityButtonListener);
		distributionButton.setOnClickListener(distributionButtonListener);
		trainingButton.setOnClickListener(trainingButtonListener);
		photographyButton.setOnClickListener(photographyButtonListener);
		designButton.setOnClickListener(designButtonListener);
		
		
	}
	
	
	public OnClickListener functionPackagesButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), ServicesFunctionPackagesActivity.class);
			startActivity(intent);
		}
	};
	
	public OnClickListener publicityButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), ServicesPublicityActivity.class);
			startActivity(intent);
		}
	};
	
	public OnClickListener distributionButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), ServicesDistributionActivity.class);
			startActivity(intent);
		}
	};

	public OnClickListener trainingButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), ServicesTrainingActivity.class);
			startActivity(intent);
		}
	};
	
	public OnClickListener photographyButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), ServicesPhotographyActivity.class);
			startActivity(intent);
		}
	};
	
	public OnClickListener designButtonListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			Intent intent = new Intent(getActivity(), ServicesDesignActivity.class);
			startActivity(intent);
		}
	};
}