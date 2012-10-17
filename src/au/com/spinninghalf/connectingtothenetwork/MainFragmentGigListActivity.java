package au.com.spinninghalf.connectingtothenetwork;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class MainFragmentGigListActivity extends FragmentActivity
    implements GigListFragment.OnGigListSelectedListener {

	private final String TAG = "MainFragmentActivity";
	private static final String ARG_ID = "id";
	private SpinningHalfApplication sha;
	private long selectedGigId = -1;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.gig_listing);
		
		sha = (SpinningHalfApplication)getApplication();
		
		// Check whether the activity is using the layout version with
        // the  FrameLayout. If so, we must add the first fragment
        if (findViewById(R.id.gig_guide_fragment_container) != null) {
        	
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            // Create an instance of GigListFragment
            GigListFragment firstFragment = new GigListFragment();
            
            // In case this activity was started with special instructions from an Intent,
            // pass the Intent's extras to the fragment as arguments
            firstFragment.setArguments(getIntent().getExtras());

            // Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.gig_guide_fragment_container, firstFragment).commit();
        }
        
        
        //We are then in the DualPane layout...Capture the ViewGig fragment from the activity layout
        ViewGigFragment viewGigFrag = (ViewGigFragment)
                getSupportFragmentManager().findFragmentById(R.id.view_gig_fragment);
        //if activity is restarted and there is previous user saved data in the bundle i.e. a gig selected
        //call the gig view fragment to update itself.
        if (savedInstanceState != null && savedInstanceState.getLong(ARG_ID) != -1) {
        	if (viewGigFrag != null) {
        		viewGigFrag.updateGigView(savedInstanceState.getLong(ARG_ID));
        	}
    	}
	}
	
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		
		//pass in the selected gig id to the bundle of data saved when activity is destroyed.
		outState.putLong(ARG_ID, selectedGigId);
	}
	

	
	
	//implementation of the single method from GigListFragment inner interface.
	//used in communicating user selections made in the GigListFragment to the Activity.
	public void onGigSelected(long id) {
		// The user selected the show name of a gig from the GigListFragment
		this.selectedGigId = id;

        // Capture the viewGig fragment from the activity layout
        ViewGigFragment viewGigFrag = (ViewGigFragment)
                getSupportFragmentManager().findFragmentById(R.id.view_gig_fragment);

        if (viewGigFrag != null) {
            // If article frag is available, we're in two-pane layout...

            // Call a method in the ViewGigFragment to update its content
            viewGigFrag.updateGigView(id);

        } else {
            // If the frag is not available, we're in the one-pane layout and must swap frags...

            // Create fragment and give it an argument for the selected article
            ViewGigFragment newFragment = new ViewGigFragment();
            Bundle args = new Bundle();
            args.putLong(ViewGigFragment.ARG_ID, id);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            // Replace whatever is in the fragment_container view with this fragment,
            // and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.gig_guide_fragment_container, newFragment);
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        }
	}

}
