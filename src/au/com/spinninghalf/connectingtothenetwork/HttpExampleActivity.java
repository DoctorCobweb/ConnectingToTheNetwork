package au.com.spinninghalf.connectingtothenetwork;

/*TODO
 * 1. Add in network availability checks before downloading xml gig guide
 * 2. 
 */

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;


import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.app.ActionBar.Tab;

public class HttpExampleActivity extends SherlockFragmentActivity implements GigListFragment.OnGigListSelectedListener {
	
	private static final String TAG = "HttpExampleActivity";
	public static final String GIG_LIST_URL_KEY = "au.com.spinninghalf.connectingtothenetwork.giglisturl";
	public static final String ARG_SELECTED_GIG_ID = "HttpExampleActivity_selected_gig_id";
	public static final String ARG_SELECTED_GIG_POSITION = "HttpExampleActivity_selected_gig_position";
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
	TabListenerTablet2<NewsFragmentOne, NewsFragmentTwo> newsTabListenerTablet;
	TabListenerTablet2<RehearsalsFragmentOne, RehearsalsFragmentTwo> rehearsalsTabListenerTablet;
	TabListenerTablet1<GigListFragment, ViewGigFragment> gigGuideTabListenerTablet;
	TabListenerTablet2<ManagementFragmentOne, ManagementFragmentTwo> managementTabListenerTablet;
	TabListenerTablet2<ServicesFragmentOne, ServicesFragmentTwo> servicesTabListenerTablet;
	TabListenerTablet2<ContactFragmentOne, ContactFragmentTwo> contactTabListenerTablet;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Log.i(TAG, "in onCreate()");
        
        shapp = SpinningHalfApplication.getInstance();
        
        View fragmentContainer = findViewById(R.id.MainFragmentContainer);
        
        ActionBar actionBar = getSupportActionBar();
		getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		
		
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
		        (this, R.id.MainFragmentContainer, GigListFragment.class);
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
		      newsTabListenerTablet = new TabListenerTablet2< NewsFragmentOne, NewsFragmentTwo>
		         (this, R.id.TabletFragmentContainer1, R.id.TabletFragmentContainer2, 
		        		 NewsFragmentOne.class, NewsFragmentTwo.class);
		      newsTab.setText("News")
		      		.setContentDescription("News updates")
		      		.setTabListener(newsTabListenerTablet);
		      actionBar.addTab(newsTab);
		      
		      
		      //TAB 2: Create and add the REHEARSALS tab
		      Tab rehearsalsTab = actionBar.newTab();
		      rehearsalsTabListenerTablet = new TabListenerTablet2<RehearsalsFragmentOne, RehearsalsFragmentTwo>
		         (this,  R.id.TabletFragmentContainer1, R.id.TabletFragmentContainer2,
		        		 RehearsalsFragmentOne.class, RehearsalsFragmentTwo.class);
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
		      managementTabListenerTablet = new TabListenerTablet2<ManagementFragmentOne, ManagementFragmentTwo>
		         (this,  R.id.TabletFragmentContainer1, R.id.TabletFragmentContainer2,
		        		 ManagementFragmentOne.class, ManagementFragmentTwo.class);
		      managementTab.setText("Management")
		      		.setContentDescription("Management updates")
		      		.setTabListener(managementTabListenerTablet);
		      actionBar.addTab(managementTab);
		      
		      //TAB 5: Create and add the SERVICES tab
		      Tab servicesTab = actionBar.newTab();
		      servicesTabListenerTablet = new TabListenerTablet2<ServicesFragmentOne, ServicesFragmentTwo>
		         (this,  R.id.TabletFragmentContainer1, R.id.TabletFragmentContainer2,
		        		 ServicesFragmentOne.class, ServicesFragmentTwo.class);
		      servicesTab.setText("Services")
		      		.setContentDescription("Services info")
		      		.setTabListener(servicesTabListenerTablet);
		      actionBar.addTab(servicesTab);
		      
		      //TAB 6: Create and add the CONTACT tab
		      Tab contactTab = actionBar.newTab();
		      contactTabListenerTablet = new TabListenerTablet2<ContactFragmentOne, ContactFragmentTwo>
		         (this,  R.id.TabletFragmentContainer1, R.id.TabletFragmentContainer2,
		        		 ContactFragmentOne.class, ContactFragmentTwo.class);
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
            //If the viewGigFrag is not available, we're in the MOBILE layout and must swap frags.
        	
            //Create fragment and give it an argument for the selected article
            ViewGigFragment newFragment = new ViewGigFragment();
            Bundle args = new Bundle();
            args.putLong(ViewGigFragment.ARG_SELECTED_GIG_ID, id);
            args.putInt(ViewGigFragment.ARG_SELECTED_GIG_POSITION, position);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            //Replace whatever is in the fragment_container view with this fragment,
            //and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.MainFragmentContainer, newFragment, "ViewGigTag");
            transaction.addToBackStack(null);

            //Commit the transaction
            transaction.commit();
        }
	}
    
	
	  private static String ACTION_BAR_INDEX = "ACTION_BAR_INDEX";

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
	    		ft.detach(newsTabListenerMobile.fragment);
	    	}
	    	if (rehearsalsTabListenerMobile.fragment != null) {
	    		ft.detach(rehearsalsTabListenerMobile.fragment);
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
	    
	    //Save the current Action Bar tab selection
    	int actionBarIndex = getSupportActionBar().getSelectedTab().getPosition();
    	SharedPreferences.Editor editor = getPreferences(Activity.MODE_PRIVATE).edit();
    	editor.putInt(ACTION_BAR_INDEX, actionBarIndex);
    	//editor.apply(); //this is only for API 9 and greater.
    	editor.commit();
		 
	    super.onSaveInstanceState(outState);
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
	    			getSupportFragmentManager().findFragmentByTag(NewsFragmentOne.class.getName());
	    	newsTabListenerTablet.fragment2 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(NewsFragmentTwo.class.getName());
	    	rehearsalsTabListenerTablet.fragment1 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(RehearsalsFragmentOne.class.getName());
	    	rehearsalsTabListenerTablet.fragment2 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(RehearsalsFragmentTwo.class.getName());
	    	servicesTabListenerTablet.fragment1 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(ServicesFragmentOne.class.getName());
	    	servicesTabListenerTablet.fragment2 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(ServicesFragmentTwo.class.getName());
	    	gigGuideTabListenerTablet.fragment1 = (SherlockListFragment)
	    			getSupportFragmentManager().findFragmentByTag(GigListFragment.class.getName());
	    	gigGuideTabListenerTablet.fragment2 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(ViewGigFragment.class.getName());
	    	contactTabListenerTablet.fragment1 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(ContactFragmentOne.class.getName());
	    	contactTabListenerTablet.fragment2 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(ContactFragmentTwo.class.getName());
	    	managementTabListenerTablet.fragment1 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(ManagementFragmentOne.class.getName());
	    	managementTabListenerTablet.fragment2 = (SherlockFragment)
	    			getSupportFragmentManager().findFragmentByTag(ManagementFragmentTwo.class.getName());
	    	
	    }
	    
	    //Restore the previous Action Bar tab selection.    
	    SharedPreferences sp = getPreferences(Activity.MODE_PRIVATE);
	    int actionBarIndex = sp.getInt(ACTION_BAR_INDEX, 0);
	    
	    //setSelectedNavigationItem() will call the onTabReselected() callback for the associated TabListener
	    //located at actionBarIndex in the ActionBar
	    getSupportActionBar().setSelectedNavigationItem(actionBarIndex);
	    
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
	  
	    public TabListenerMobileList(FragmentActivity activity, int fragmentContainer, 
	                       Class<T> fragmentClass) {
	  
	      this.activity = activity;
	      this.fragmentContainer = fragmentContainer;
	      this.fragmentClass = fragmentClass;
	    }
	  
	    //Called when a new tab has been selected
	    public void onTabSelected(Tab tab, FragmentTransaction ft) {
	    	ViewGigFragment vg = (ViewGigFragment) getSupportFragmentManager().findFragmentByTag("ViewGigTag");
	    	Log.i(TAG, "g'day from onTabSelected in TabListenerMobileList");
	    	
	    	
	        if (fragment == null) {
	      	    Log.i(TAG, "g'day from onTabSelected in TabListenerMobileList. GigListFragment == null");
	    	    String fragmentName = fragmentClass.getName();
	    	    fragment = (SherlockListFragment) Fragment.instantiate(activity, fragmentName); 
	    	    ft.add(fragmentContainer, fragment, fragmentName);
	        } else if (vg != null && fragment != null) {
	    	    Log.i(TAG, "g'day from onTabSelected in TabListenerMobileList. ViewGigFragment != null and GigListFragment != null");
	    	    
	    	    
	    	    String fragmentName = fragmentClass.getName();
	    	    fragment = (SherlockListFragment) Fragment.instantiate(activity, fragmentName); 
	    	    ft.add(fragmentContainer, fragment, fragmentName);
	    	    
	    	    ft.detach(vg);
	    	    ft.attach(fragment);
	    	    //ft.addToBackStack(null);
	        } else {
	    	    Log.i(TAG, "g'day from onTabSelected in TabListenerMobileList. GigListFragment != null");
	    	    ft.attach(fragment);
	        }
	    }
	  
	    //Called on the currently selected tab when a different tag is
	    //selected. 
	    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	    	
	    	Log.i(TAG, "g'day from onTabUnselected in TabListenerMobileList");
	    	
	    	ViewGigFragment vg = (ViewGigFragment) getSupportFragmentManager().findFragmentByTag("ViewGigTag");
	    	if (fragment != null){
	    		ft.detach(fragment);
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
	    	/*
	    	if (fragment != null && vg != null){
	    		Log.i(TAG, "g'day from onTabReselected in TabListenerMobileList. fragment!= null and vg != null");
	    		ft.remove(vg);
	    		//ft.attach(fragment);
	    		ft.add(fragmentContainer, fragment, fragmentClass.getName());
	    		if (vg.isAdded()) {
	    			Log.i(TAG, "in onTabReselected in TabListenerMobileList. vg is added!!!");
	    		}
	    		if (fragment.isAdded()) {
	    			Log.i(TAG, "in onTabReselected in TabListenerMobileList. fragment is added!!!");
	    		}
	    	}
	    	*/
	    	
	    	
	    	//if we were to uncomment the if-block below, then after the screen rotation, which will onTabReselected() via 
	    	//setSelectedNavigationItem() in onRestoreInstanceState and onResume(), the ViewGigFragment will be detached.
	    	//and thus will leave the screen blank.
	    	/*
	    	if (vg != null) {
	    		ft.detach(vg);
	    		if (vg.isAdded()) {
	    			Log.i(TAG, "in onTabReselected in TabListenerMobileList. vg is added!!!");
	    		}
	    	}
	    	*/
	    	
	    	
	    	
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