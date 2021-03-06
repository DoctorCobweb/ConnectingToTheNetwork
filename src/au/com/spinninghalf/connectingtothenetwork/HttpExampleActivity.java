package au.com.spinninghalf.connectingtothenetwork;

/*TODO
 * 1. Add in network availability checks before downloading xml gig guide
 * 2. 
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;

public class HttpExampleActivity extends SherlockFragmentActivity 
		implements GigListFragment.OnGigListSelectedListener, GigListFragment.OnGigListRefreshedListener {
	
	private static final String TAG = "HttpExampleActivity";
	public static final String GIG_LIST_URL_KEY = "au.com.spinninghalf.connectingtothenetwork.giglisturl";
	public static final String ARG_SELECTED_GIG_ID = "HttpExampleActivity_selected_gig_id";
	public static final String ARG_SELECTED_GIG_POSITION = "HttpExampleActivity_selected_gig_position";
	private static String ACTION_BAR_INDEX = "ACTION_BAR_INDEX";
	private long _selectedGigId = -1;
	private int _selectedGigPosition = -1;
	
	SpinningHalfApplication shapp;
	
	//TabListeners for MOBILE layout
	TabListenerMobile<NewsFragmentOne> newsTabListenerMobile;
	TabListenerMobile<RehearsalsFragmentOne>rehearsalsTabListenerMobile;
	TabListenerMobileList<GigListFragment> gigGuideTabListenerMobile; //be careful! Look, this is a different version of the Mobile TabListener.
	TabListenerMobile<ManagementFragmentOne>managementTabListenerMobile;
	TabListenerMobile<ServicesFragmentOne>servicesTabListenerMobile; 
	TabListenerMobile<ContactFragmentOne>contactTabListenerMobile; 
	
	//Tablisteners for TABLET layout
	//Tablistener1 has ListFragment + Fragment generics
	//Tablistener2 has Fragment + Fragment generics
	TabListenerTablet2<NewsFragmentTwo, NewsFragmentThree> newsTabListenerTablet;
	TabListenerTablet2<RehearsalsFragmentTwo, RehearsalsFragmentThree> rehearsalsTabListenerTablet;
	TabListenerTablet1<GigListFragment, ViewGigFragment> gigGuideTabListenerTablet;
	TabListenerTablet2<ManagementFragmentTwo, ManagementFragmentThree> managementTabListenerTablet;
	TabListenerTablet2<ServicesFragmentTwo, ServicesFragmentThree> servicesTabListenerTablet;
	TabListenerTablet2<ContactFragmentTwo, ContactFragmentThree> contactTabListenerTablet;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.i(TAG, "in onCreate()");
        
        shapp = SpinningHalfApplication.getInstance();
        
        PackageManager pm = getPackageManager();
        
        //determine whether the device has telephony capabilities i.e. can make a phone call
        boolean telephonySupported = 
        		pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
        // set the result to the application variable 'hasDeviceTelephonyCapabilities'.
        shapp.setTelephonyCapability(telephonySupported);
        
        View fragmentContainer = findViewById(R.id.MainFragmentContainer);
        
        ActionBar actionBar = getSupportActionBar();
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		//actionBar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);
		
		
		//use tablet navigation if the list and gig fragments are both available
		boolean tabletLayout = fragmentContainer == null;
		
		if(!tabletLayout) {
			  //using MOBILE layout version of main.xml
			
			  //TAB 1: Create and add the NEWS tab
		      Tab newsTab = actionBar.newTab();
		      newsTabListenerMobile = new TabListenerMobile<NewsFragmentOne>
		         (this, R.id.MainFragmentContainer, NewsFragmentOne.class);
		      newsTab.setText("News")
		      		.setContentDescription("News updates")
		      		.setTabListener(newsTabListenerMobile);
		      actionBar.addTab(newsTab);
		      
		      //TAB 2: Create and add the REHEARSALS tab
		      Tab rehearsalsTab = actionBar.newTab();
		      rehearsalsTabListenerMobile = new TabListenerMobile<RehearsalsFragmentOne>
		         (this, R.id.MainFragmentContainer, RehearsalsFragmentOne.class);
		      rehearsalsTab.setText("Rehearsals")
		      		.setContentDescription("Rehearsals information.")
		      		.setTabListener(rehearsalsTabListenerMobile);
		      actionBar.addTab(rehearsalsTab);
		      
		      //TAB 3: Create and add the GIG GUIDE tab
		      Tab gigGuideTab = actionBar.newTab();
		      gigGuideTabListenerMobile = new TabListenerMobileList<GigListFragment>
		        (this, R.id.MainFragmentContainer, "GIG_GUIDE_TAG", GigListFragment.class);
		      gigGuideTab.setText("Gig Guide")
		             .setContentDescription("Information on gigs.")
		             .setTabListener(gigGuideTabListenerMobile);
		      actionBar.addTab(gigGuideTab);
		      
		      //TAB 4: Create and add the MANAGEMENT tab
		      Tab managementTab = actionBar.newTab();
		      managementTabListenerMobile = new TabListenerMobile<ManagementFragmentOne>
		         (this, R.id.MainFragmentContainer, ManagementFragmentOne.class);
		      managementTab.setText("Management")
		      		.setContentDescription("Management updates")
		      		.setTabListener(managementTabListenerMobile);
		      actionBar.addTab(managementTab);
		      
		      //TAB 5: Create and add the SERVICES tab
		      Tab servicesTab = actionBar.newTab();
		      servicesTabListenerMobile = new TabListenerMobile<ServicesFragmentOne>
		         (this, R.id.MainFragmentContainer, ServicesFragmentOne.class);
		      servicesTab.setText("Services")
		      		.setContentDescription("Services info")
		      		.setTabListener(servicesTabListenerMobile);
		      actionBar.addTab(servicesTab);
		      
		      //TAB 6: Create and add the CONTACT tab
		      Tab contactTab = actionBar.newTab();
		      contactTabListenerMobile = new TabListenerMobile<ContactFragmentOne>
		         (this, R.id.MainFragmentContainer, ContactFragmentOne.class);
		      contactTab.setText("Contact Us")
		      		.setContentDescription("Our Contact infomation")
		      		.setTabListener(contactTabListenerMobile);
		      actionBar.addTab(contactTab);
		      
		      //if we are restored from a previous state we should do nothing.
		      //Otherwise we may end up with overlapping fragments.
		      if (savedInstanceState != null) {
		    	  Log.i(TAG, "in onCreate(), MOBILE Layout and restoring from a previous state.");
		    	  return;
		      }
		} else {
			  //in TABLET version of main.xml
			
		      //TAB 1: Create and add the NEWS tab
		      Tab newsTab = actionBar.newTab();
		      newsTabListenerTablet = new TabListenerTablet2< NewsFragmentTwo, NewsFragmentThree>
		         (this, R.id.TabletFragmentContainer1, R.id.TabletFragmentContainer2, 
		        		 NewsFragmentTwo.class, NewsFragmentThree.class);
		      newsTab.setText("News")
		      		.setContentDescription("News updates")
		      		.setTabListener(newsTabListenerTablet);
		      actionBar.addTab(newsTab);
		      
		      
		      //TAB 2: Create and add the REHEARSALS tab//In case this activity was started with special instructions from an Intent,
	            //pass the Intent's extras to the fragment as arguments:
	            //firstFragment.setArguments(getIntent().getExtras());
		      Tab rehearsalsTab = actionBar.newTab();
		      rehearsalsTabListenerTablet = new TabListenerTablet2<RehearsalsFragmentTwo, RehearsalsFragmentThree>
		         (this,  R.id.TabletFragmentContainer1, R.id.TabletFragmentContainer2,
		        		 RehearsalsFragmentTwo.class, RehearsalsFragmentThree.class);
		      rehearsalsTab.setText("Rehearsals")
		      		.setContentDescription("Rehearsals information.")
		      		.setTabListener(rehearsalsTabListenerTablet);
		      actionBar.addTab(rehearsalsTab);
		      
		      
		      //TAB 3: Create and add the GIG GUIDE tab
		      Tab gigGuideTab = actionBar.newTab();
		      getSupportActionBar().setSelectedNavigationItem(0);
		      gigGuideTabListenerTablet = new TabListenerTablet1<GigListFragment,ViewGigFragment >
		        (this, R.id.TabletFragmentContainer1, R.id.TabletFragmentContainer2, 
		        		GigListFragment.class, ViewGigFragment.class);
		      gigGuideTab.setText("Gig Guide")
		             .setContentDescription("Information on gigs")
		             .setTabListener(gigGuideTabListenerTablet);
		      actionBar.addTab(gigGuideTab);
		      
		    //TAB 4: Create and add the MANAGEMENT tab
		      Tab managementTab = actionBar.newTab();
		      managementTabListenerTablet = new TabListenerTablet2<ManagementFragmentTwo, ManagementFragmentThree>
		         (this,  R.id.TabletFragmentContainer1, R.id.TabletFragmentContainer2,
		        		 ManagementFragmentTwo.class, ManagementFragmentThree.class);
		      managementTab.setText("Management")
		      		.setContentDescription("Management updates")
		      		.setTabListener(managementTabListenerTablet);
		      actionBar.addTab(managementTab);
		      
		      //TAB 5: Create and add the SERVICES tab"";
		      Tab servicesTab = actionBar.newTab();
		      servicesTabListenerTablet = new TabListenerTablet2<ServicesFragmentTwo, ServicesFragmentThree>
		         (this,  R.id.TabletFragmentContainer1, R.id.TabletFragmentContainer2,
		        		 ServicesFragmentTwo.class, ServicesFragmentThree.class);
		      servicesTab.setText("Services")
		      		.setContentDescription("Services info")
		      		.setTabListener(servicesTabListenerTablet);
		      actionBar.addTab(servicesTab);
		      
		      //TAB 6: Create and add the CONTACT tab
		      Tab contactTab = actionBar.newTab();
		      contactTabListenerTablet = new TabListenerTablet2<ContactFragmentTwo, ContactFragmentThree>
		         (this,  R.id.TabletFragmentContainer1, R.id.TabletFragmentContainer2,
		        		 ContactFragmentTwo.class, ContactFragmentThree.class);
		      contactTab.setText("Contact Us")
		      		.setContentDescription("Our Contact infomation")
		      		.setTabListener(contactTabListenerTablet);
		      actionBar.addTab(contactTab);
		      
		      if(savedInstanceState != null) {
		    	  Log.i(TAG, "in onCreate(), TABLET Layout and restoring from a previous state.");
		    	  return;
		      }
		}
 }
    
	//implementation of the single method from GigListFragment inner interface.
	//used in communicating user selections made in the GigListFragment to the Activity.
	public void onGigSelected(long id, int position) {
		this._selectedGigId = id;
		this._selectedGigPosition = position;
		
        //Capture the viewGig fragment from the activity layout
        ViewGigFragment viewGigFrag = (ViewGigFragment)
                getSupportFragmentManager().findFragmentById(R.id.TabletFragmentContainer2);

        if (viewGigFrag != null) {
            //If viewGigFrag is available, we're in TABLET layout
            //Call a method in the ViewGigFragment to update its content
            viewGigFrag.updateGigView(id, position);

        } else {
        	//Start a new activity to display the selected gig
        	Intent viewGigIntent = new Intent(this, ViewGigActivity.class);
        	viewGigIntent.putExtra(ViewGigActivity.EXTRA_SELECTED_GIG_POSITION, position);
        	viewGigIntent.putExtra(ViewGigActivity.EXTRA_SELECTED_GIG_ID, id);
        	startActivity(viewGigIntent);
        }
	}
    
	public void onGigListRefreshed() {
		//add in refreshing code.
		shapp.setUpdateGigGuideDatabase(true);
		Log.i(TAG, "in onGigListRefreshed() at the TOP new GigListFragment with a refreshed database.");
		
		//Check whether the activity is using the layout version with
        //the  FrameLayout. If so, we must add the first fragment
		//if (findViewById(R.id.gig_guide_fragment_container) != null) {
	    if (findViewById(R.id.MainFragmentContainer) != null) {	
        	
            //Create an instance of GigListFragment
            GigListFragment refreshedFragment = new GigListFragment();
            Log.i(TAG, "in onGigListRefreshed() and below making new GigListFragment with a refreshed database.");
            

            //Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    //.add(R.id.gig_guide_fragment_container, firstFragment).commit();
            		.replace(R.id.MainFragmentContainer, refreshedFragment, "GIG_GUIDE_TAG").commit();
        }
	}
	  

	  @Override
	  public void onSaveInstanceState(Bundle outState) {
	    View fragmentContainer = findViewById(R.id.MainFragmentContainer); 
	    boolean tabletLayout = fragmentContainer == null;
	    
	    Log.i(TAG, "in onSaveInstanceState()");
	    
	    outState.putLong(HttpExampleActivity.ARG_SELECTED_GIG_ID, this._selectedGigId);
	    outState.putInt(HttpExampleActivity.ARG_SELECTED_GIG_POSITION, this._selectedGigPosition);
	    
	    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	      
	    if (!tabletLayout) {
	    	//Detach any fragments which are attached in MOBILE layout. 
	    	if (newsTabListenerMobile.fragment != null) {
	    		Log.i(TAG, "in onSaveInstanceState. newsTabListenerMobile.fragment  != null");
	    		ft.detach(newsTabListenerMobile.fragment);
	    		if (newsTabListenerMobile.fragment.isDetached()) {
	    			Log.i(TAG, "in onSaveInstanceState. newsTabListenerMobile.fragment  != null and has been successfully detached.");
	    		}
	    		
	    	}
	    	if (rehearsalsTabListenerMobile.fragment != null) {
	    		Log.i(TAG, "in onSaveInstanceState. rehearsalsTabListenerMobile.fragment  != null");
	    		ft.detach(rehearsalsTabListenerMobile.fragment);
	    		if (rehearsalsTabListenerMobile.fragment.isDetached()) {
	    			Log.i(TAG, "in onSaveInstanceState. rehearsalsTabListenerMobile.fragment  != null and has been successfully detached.");
	    		}
	    	}
	    	if (gigGuideTabListenerMobile.fragment != null) {
	    		ft.detach(gigGuideTabListenerMobile.fragment);
	    	}
	    	if (managementTabListenerMobile.fragment != null) {
	    		ft.detach(managementTabListenerMobile.fragment);
	    	}
	    	if (servicesTabListenerMobile.fragment != null) {
	    		ft.detach(servicesTabListenerMobile.fragment);
	    	}
	    	if (contactTabListenerMobile.fragment != null) {
	    		ft.detach(contactTabListenerMobile.fragment);
	    	}
	      
		 } else {  
			//Detach any fragment which are attached in TABLET layout
			if (newsTabListenerTablet.fragment1 != null || newsTabListenerTablet.fragment2 != null) {
				ft.detach(newsTabListenerTablet.fragment1);
				ft.detach(newsTabListenerTablet.fragment2);
			}
			if (rehearsalsTabListenerTablet.fragment1 != null || rehearsalsTabListenerTablet.fragment2 != null) {
				ft.detach(rehearsalsTabListenerTablet.fragment1);
				ft.detach(rehearsalsTabListenerTablet.fragment2);
			}
			if (gigGuideTabListenerTablet.fragment1 != null || gigGuideTabListenerTablet.fragment2 != null) {
				ft.detach(gigGuideTabListenerTablet.fragment1);
				ft.detach(gigGuideTabListenerTablet.fragment2);
			}
			if (managementTabListenerTablet.fragment1 != null || managementTabListenerTablet.fragment2 != null) {
				ft.detach(managementTabListenerTablet.fragment1);
				ft.detach(managementTabListenerTablet.fragment2);
			}
			if (servicesTabListenerTablet.fragment1 != null || servicesTabListenerTablet.fragment2 != null) {
				ft.detach(servicesTabListenerTablet.fragment1);
				ft.detach(servicesTabListenerTablet.fragment2);
			}
			if (contactTabListenerTablet.fragment1 != null || contactTabListenerTablet.fragment2 != null) {
				ft.detach(contactTabListenerTablet.fragment1);
				ft.detach(contactTabListenerTablet.fragment2);
			}
		 }
	    
	    //remember to commit the fragment transactions!
	    ft.commit();
	    
	    //Save the current Action Bar tab selection
    	int actionBarIndex = getSupportActionBar().getSelectedTab().getPosition();
    	SharedPreferences.Editor editor = getPreferences(Activity.MODE_PRIVATE).edit();
    	editor.putInt(ACTION_BAR_INDEX, actionBarIndex);
    	//editor.apply(); //this is only for API 9 and greater.
    	editor.commit();
		 
	    super.onSaveInstanceState(outState);
	  }

	  
	  @Override 
	  public void onPause() {
		  super.onPause();
		  
	  }

	  
	  @Override
	  public void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    
	    Log.i(TAG, "in onRestoreInstanceState()");
	    
	    if (savedInstanceState != null) {
	    	this._selectedGigId = savedInstanceState.getLong(HttpExampleActivity.ARG_SELECTED_GIG_ID);
	    	this._selectedGigPosition = savedInstanceState.getInt(HttpExampleActivity.ARG_SELECTED_GIG_POSITION);
	    	Log.i(TAG, "in onRestoreInstanceState() savedInstanceState != null. And _selectedGigId = " 
	    			+ this._selectedGigId + " and _selectedGigPosition = " 
	    			+ this._selectedGigPosition);
	    }

	    View fragmentContainer = findViewById(R.id.MainFragmentContainer); 
	    boolean tabletLayout = fragmentContainer == null;
	    
	    if (!tabletLayout) {
	    	//Find the recreated Fragments and assign them to their associated Tab Listeners.
	    	newsTabListenerMobile.fragment = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(NewsFragmentOne.class.getName());
	    	rehearsalsTabListenerMobile.fragment = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(RehearsalsFragmentOne.class.getName());
	    	servicesTabListenerMobile.fragment = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(ServicesFragmentOne.class.getName());
	    	gigGuideTabListenerMobile.fragment = (SherlockListFragment)
	    			getSupportFragmentManager().findFragmentByTag(GigListFragment.class.getName());
	    	contactTabListenerMobile.fragment = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(ContactFragmentOne.class.getName());
	    	managementTabListenerMobile.fragment = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(ManagementFragmentOne.class.getName());
	    	
	    	SherlockFragment viewGigFragment = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(ViewGigFragment.class.getName());
	    	
	    	//make sure to add the ViewGigFragment back to the screen if it was showing when there was a screen rotation.
	    	if (viewGigFragment != null) {
	    		onGigSelected(savedInstanceState.getLong(ARG_SELECTED_GIG_ID), savedInstanceState.getInt(ARG_SELECTED_GIG_POSITION));
	    	}
	    	
	    } else {
	    	//we are in tablet layout. assign the tabs to their tab listeners
	    	newsTabListenerTablet.fragment1 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(NewsFragmentTwo.class.getName());
	    	newsTabListenerTablet.fragment2 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(NewsFragmentThree.class.getName());
	    	rehearsalsTabListenerTablet.fragment1 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(RehearsalsFragmentTwo.class.getName());
	    	rehearsalsTabListenerTablet.fragment2 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(RehearsalsFragmentThree.class.getName());
	    	servicesTabListenerTablet.fragment1 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(ServicesFragmentTwo.class.getName());
	    	servicesTabListenerTablet.fragment2 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(ServicesFragmentThree.class.getName());
	    	gigGuideTabListenerTablet.fragment1 = (SherlockListFragment)
	    			getSupportFragmentManager().findFragmentByTag(GigListFragment.class.getName());
	    	gigGuideTabListenerTablet.fragment2 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(ViewGigFragment.class.getName());
	    	contactTabListenerTablet.fragment1 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(ContactFragmentTwo.class.getName());
	    	contactTabListenerTablet.fragment2 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(ContactFragmentThree.class.getName());
	    	managementTabListenerTablet.fragment1 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(ManagementFragmentTwo.class.getName());
	    	managementTabListenerTablet.fragment2 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(ManagementFragmentThree.class.getName());
	    	
	    }
	  }
	  
	  @Override 
	  public void onResume() {
	    super.onResume();
	    
	    Log.i(TAG, "in onResume()");
	    
	    
	    View fragmentContainer = findViewById(R.id.MainFragmentContainer); 
	    boolean tabletLayout = fragmentContainer == null;
	  
	    if (!tabletLayout) {
	      SharedPreferences sp = getPreferences(Activity.MODE_PRIVATE);
	      int actionBarIndex = sp.getInt(ACTION_BAR_INDEX, 0);
	      
	      //setSelectedNavigationItem() will call the onTabReselected() callback for the associated TabListener
		  //located at actionBarIndex in the ActionBar
	      getSupportActionBar().setSelectedNavigationItem(actionBarIndex);
	      
	    }
	  }
	  
	  @Override
	  public void onStop() {
		  super.onStop();
		  
		  Log.i(TAG, "in onStop()");
	  }
	  
	  
	  @Override
	  public void onDestroy() {
		  super.onDestroy();
		  
		  Log.i(TAG, "in onDestroy()");
	  }
	  
    
//-----------------------------------------------------------------------------------------------------------------------------------
    
    //////////////////////////////////////////////////////////////////////
    //                     TAB LISTENER DEFINITIONS                     //
    //////////////////////////////////////////////////////////////////////

	//listener for mobile layout and basic fragment class
	public class TabListenerMobile<T extends SherlockFragment> implements ActionBar.TabListener {
		  
		    private SherlockFragment fragment;
		    private FragmentActivity activity;
		    private Class<T> fragmentClass;
		    private int fragmentContainer;
		  
		    public TabListenerMobile(FragmentActivity activity, int fragmentContainer, 
		                       Class<T> fragmentClass) {
		  
		      this.activity = activity;
		      this.fragmentContainer = fragmentContainer;
		      this.fragmentClass = fragmentClass;
		    }
		  
		    //Called when a new tab has been selected
		    public void onTabSelected(Tab tab, FragmentTransaction ft) {
		      if (fragment == null) {
		        String fragmentName = fragmentClass.getName();
		        fragment = (SherlockFragment) Fragment.instantiate(activity, fragmentName); 
		        ft.add(fragmentContainer, fragment, fragmentName);
		      } else
		        ft.attach(fragment);
		    }
		  
		    //Called on the currently selected tab when a different tag is
		    //selected. 
		    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		    	
		      if (fragment != null) {
		        ft.detach(fragment);
		      }
		    } 
		  
		    //Called when the selected tab is selected.
		    public void onTabReselected(Tab tab, FragmentTransaction ft) {
		      if (fragment != null)
		        ft.attach(fragment);
		    }
	  }
	
	//listener for mobile layout and LIST fragment class
	public class TabListenerMobileList<T extends SherlockListFragment> implements ActionBar.TabListener {

	    private SherlockListFragment fragment;
	    private FragmentActivity activity;
	    private Class<T> fragmentClass;
	    private int fragmentContainer;
	    private String mTag;
	  
	    public TabListenerMobileList(FragmentActivity activity, int fragmentContainer, 
	                       String tag, Class<T> fragmentClass) {
	  
	      this.activity = activity;
	      this.fragmentContainer = fragmentContainer;
	      this.fragmentClass = fragmentClass;
	      this.mTag = tag;
	    }
	  
	    //Called when a new tab has been selected
	    public void onTabSelected(Tab tab, FragmentTransaction ft) {
	    	ViewGigFragment vg = (ViewGigFragment) getSupportFragmentManager().findFragmentByTag("ViewGigTag");
	    	Log.i(TAG, "g'day from onTabSelected in TabListenerMobileList");
	    	
	    	SherlockListFragment mFragment = (SherlockListFragment) this.activity.getSupportFragmentManager().findFragmentByTag(mTag);
	    	
	        if (mFragment == null) {
	      	    Log.i(TAG, "g'day from onTabSelected in TabListenerMobileList. GigListFragment == null");
	    	    String fragmentName = fragmentClass.getName();
	    	    fragment = (SherlockListFragment) Fragment.instantiate(activity, fragmentName); 
	    	    ft.add(fragmentContainer, fragment, fragmentName);
	        } else if (vg != null && mFragment != null) {
	    	    Log.i(TAG, "g'day from onTabSelected in TabListenerMobileList. ViewGigFragment != null and GigListFragment != null");
	        } else {
	    	    Log.i(TAG, "g'day from onTabSelected in TabListenerMobileList. GigListFragment != null");
	    	    ft.attach(mFragment);
	        }
	    }
	  
	    //Called on the currently selected tab when a different tag is
	    //selected. 
	    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	    	
	    	Log.i(TAG, "g'day from onTabUnselected in TabListenerMobileList");
	    	SherlockListFragment mFragment = (SherlockListFragment) this.activity.getSupportFragmentManager().findFragmentByTag(mTag); 
	    	
	    	ViewGigFragment vg = (ViewGigFragment) getSupportFragmentManager().findFragmentByTag("ViewGigTag");
	    	
	    	if(fragment != null){
	    		ft.detach(fragment);
	    	}
	    	
	    	if (mFragment != null){
	    		Log.i(TAG, "g'day from onTabUnselected in TabListenerMobileList AND fragment != null");
	    		Log.i(TAG, "g'day from onTabUnselected in TabListenerMobileList, fragment != null, tagName = " + fragment.getTag());
	    		ft.detach(mFragment);
	    		
	    		if(fragment.isDetached()) {
		    		Log.i(TAG, "g'day from onTabUnselected in TabListenerMobileList AND fragment IS DETATCHED.");
		    	}
	    	}
	    	//you must detach the ViewGigFragment if user selects another Tab whilst viewing ViewGigFragment.
	    	if (vg != null) {
	    		ft.detach(vg);
	    	}
	    }
	  
	    //Called when the selected tab is selected.
	    public void onTabReselected(Tab tab, FragmentTransaction ft) {
	    	Log.i(TAG, "g'day from onTabReselected in TabListenerMobileList");
	    	
	    	ViewGigFragment vg = (ViewGigFragment) getSupportFragmentManager().findFragmentByTag("ViewGigTag");
	    	
	    	if(fragment != null && vg != null) {
	    		
	    		Log.i(TAG, "g'day from onTabReselected in TabListenerMobileList. fragment!= null and vg != null");
	    		
	    	} else if(fragment != null) {
	    		ft.attach(fragment);
	    	}
	    }
  }
	
	
	//STRUCTURE: Listfragment + Fragment. tab listener for when in tablet mode and using _2_ fragments in the container.
	public static class TabListenerTablet1<T extends SherlockListFragment, U extends SherlockFragment> implements ActionBar.TabListener {
		  
		    private SherlockListFragment fragment1;
		    private SherlockFragment fragment2;
		    private FragmentActivity activity;
		    private Class<T> fragmentClass1;
		    private Class<U> fragmentClass2;
		    private int fragmentContainer1;
		    private int fragmentContainer2;
		  
		    public TabListenerTablet1(FragmentActivity activity, int fragmentContainer1, int fragmentContainer2, 
		                       Class<T> fragmentClass1, Class<U> fragmentClass2) {
		  
		          this.activity = activity;
		          this.fragmentContainer1 = fragmentContainer1;
		          this.fragmentContainer2 = fragmentContainer2;
		          this.fragmentClass1 = fragmentClass1;
		          this.fragmentClass2 = fragmentClass2;
		    }
		  
		    //Called when a new tab has been selected
		    public void onTabSelected(Tab tab, FragmentTransaction ft) {
		    	
		      //check if both fragments are null. If so, create them and add to container.
		      if (fragment1 == null && fragment2 == null) {
		          String fragmentName1 = fragmentClass1.getName();
		          fragment1 = (SherlockListFragment) Fragment.instantiate(activity, fragmentName1); 
		          ft.add(fragmentContainer1, fragment1, fragmentName1);
		        
		          String fragmentName2 = fragmentClass2.getName();
		          fragment2 = (SherlockFragment) Fragment.instantiate(activity, fragmentName2); 
		          ft.add(fragmentContainer2, fragment2, fragmentName2);
		          
		          Log.i(TAG, "in TabListenerTablet:onTabSelected 1st if block");
		          Log.i(TAG, "in TabListenerTablet:onTabSelected 1st if block. fragmentContainer1 = " + fragmentContainer1);
		          Log.i(TAG, "in TabListenerTablet:onTabSelected 1st if block. fragmentContainer2 = " + fragmentContainer2);
		          
		      } else if (fragment1 == null) {
		    	  
		    	  //fragment2 is already instantiated so dont recreate it
		    	  String fragmentName1 = fragmentClass1.getName();
			      fragment1 = (SherlockListFragment) Fragment.instantiate(activity, fragmentName1); 
			      ft.add(fragmentContainer1, fragment1, fragmentName1);
			      
			      //ft.add(fragmentContainer, fragment2);
			      ft.attach(fragment2);
			      Log.i(TAG, "in TabListenerTablet:onTabSelected 2nd if block");
		      } else if (fragment2 == null) {
		    	  
		    	  //fragment1 is already instantiated so dont recreate it.
		    	  String fragmentName2 = fragmentClass2.getName();
			      fragment2 = (SherlockFragment) Fragment.instantiate(activity, fragmentName2); 
			      ft.add(fragmentContainer2, fragment2, fragmentName2);
			      
			      //ft.add(fragmentContainer, fragment1);
			      ft.attach(fragment1);
			      Log.i(TAG, "in TabListenerTablet:onTabSelected 3rd if block");
		      } else {
		    	  
		    	  //both fragments are already instantiated.
		          ft.attach(fragment1);
		          ft.attach(fragment2);
		          Log.i(TAG, "in TabListenerTablet:onTabSelected 4th if block");
		      }
		    }
		  
		    //Called on the currently selected tab when a different tag is
		    //selected. 
		    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
			      if (fragment1 != null && fragment2 != null) {
			        ft.detach(fragment1);
			        ft.detach(fragment2);
			        Log.i(TAG, "in TabListenerTablet:onTabUnselected. In 1st if block");
			      } else if (fragment1 != null) {
			    	  ft.detach(fragment1);
			    	  Log.i(TAG, "in TabListenerTablet:onTabUnselected. In 2st if block");
			      } else if (fragment2 != null) {
			    	  ft.detach(fragment2);
			    	  Log.i(TAG, "in TabListenerTablet:onTabUnselected. In 3st if block");
			      }
		    } 
		  
		    //Called when the selected tab is selected.
		    public void onTabReselected(Tab tab, FragmentTransaction ft) {
			      if (fragment1 != null && fragment2 != null) {
			        ft.attach(fragment1);
			        ft.attach(fragment2);
			      } else if (fragment1 != null) {
			    	  ft.attach(fragment1);
			      } else if (fragment2 != null) {
			    	  ft.attach(fragment2);
			      }
		    }
	}
	
	//STRUCTURE: Fragment + Fragment. tab listener for when in tablet mode and using _2_ fragments in the container. 
		public static class TabListenerTablet2<T extends SherlockFragment, U extends SherlockFragment> implements ActionBar.TabListener {
			  
			    private SherlockFragment fragment1;
			    private SherlockFragment fragment2;
			    private FragmentActivity activity;
			    private Class<T> fragmentClass1;
			    private Class<U> fragmentClass2;
			    private int fragmentContainer1;
			    private int fragmentContainer2;
			  
			    public TabListenerTablet2(FragmentActivity activity, int fragmentContainer1, int fragmentContainer2, 
			                       Class<T> fragmentClass1, Class<U> fragmentClass2) {
			  
			          this.activity = activity;
			          this.fragmentContainer1 = fragmentContainer1;
			          this.fragmentContainer2 = fragmentContainer2;
			          this.fragmentClass1 = fragmentClass1;
			          this.fragmentClass2 = fragmentClass2;
			    }
			  
			    //Called when a new tab has been selected
			    public void onTabSelected(Tab tab, FragmentTransaction ft) {
			    	
			      //check if both fragments are null. If so, create them and add to container.
			      if (fragment1 == null && fragment2 == null) {
			          String fragmentName1 = fragmentClass1.getName();
			          fragment1 = (SherlockFragment) Fragment.instantiate(activity, fragmentName1); 
			          ft.add(fragmentContainer1, fragment1, fragmentName1);
			        
			          String fragmentName2 = fragmentClass2.getName();
			          fragment2 = (SherlockFragment) Fragment.instantiate(activity, fragmentName2); 
			          ft.add(fragmentContainer2, fragment2, fragmentName2);
			          
			          Log.i(TAG, "in TabListenerTablet:onTabSelected 1st if block");
			          Log.i(TAG, "in TabListenerTablet:onTabSelected 1st if block. fragmentContainer1 = " + fragmentContainer1);
			          Log.i(TAG, "in TabListenerTablet:onTabSelected 1st if block. fragmentContainer2 = " + fragmentContainer2);
			          
			      } else if (fragment1 == null) {
			    	  
			    	  //fragment2 is already instantiated so dont recreate it
			    	  String fragmentName1 = fragmentClass1.getName();
				      fragment1 = (SherlockFragment) Fragment.instantiate(activity, fragmentName1); 
				      ft.add(fragmentContainer1, fragment1, fragmentName1);
				      
				      //ft.add(fragmentContainer, fragment2);
				      ft.attach(fragment2);
				      Log.i(TAG, "in TabListenerTablet:onTabSelected 2nd if block");
			      } else if (fragment2 == null) {
			    	  
			    	  //fragment1 is already instantiated so dont recreate it.
			    	  String fragmentName2 = fragmentClass2.getName();
				      fragment2 = (SherlockFragment) Fragment.instantiate(activity, fragmentName2); 
				      ft.add(fragmentContainer2, fragment2, fragmentName2);
				      
				      //ft.add(fragmentContainer, fragment1);
				      ft.attach(fragment1);
				      Log.i(TAG, "in TabListenerTablet:onTabSelected 3rd if block");
			      } else {
			    	  
			    	  //both fragments are already instantiated.
			          ft.attach(fragment1);
			          ft.attach(fragment2);
			          Log.i(TAG, "in TabListenerTablet:onTabSelected 4th if block");
			      }
			    }
			  
			    //Called on the currently selected tab when a different tag is
			    //selected. 
			    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
				      if (fragment1 != null && fragment2 != null) {
				        ft.detach(fragment1);
				        ft.detach(fragment2);
				        Log.i(TAG, "in TabListenerTablet:onTabUnselected. In 1st if block");
				      } else if (fragment1 != null) {
				    	  ft.detach(fragment1);
				    	  Log.i(TAG, "in TabListenerTablet:onTabUnselected. In 2st if block");
				      } else if (fragment2 != null) {
				    	  ft.detach(fragment2);
				    	  Log.i(TAG, "in TabListenerTablet:onTabUnselected. In 3st if block");
				      }
			    } 
			  
			    //Called when the selected tab is selected.
			    public void onTabReselected(Tab tab, FragmentTransaction ft) {
				      if (fragment1 != null && fragment2 != null) {
				        ft.attach(fragment1);
				        ft.attach(fragment2);
				      } else if (fragment1 != null) {
				    	  ft.attach(fragment1);
				      } else if (fragment2 != null) {
				    	  ft.attach(fragment2);
				      }
			    }
		}
//--------------------------------------------------------------------------------------------------------------------------------
}