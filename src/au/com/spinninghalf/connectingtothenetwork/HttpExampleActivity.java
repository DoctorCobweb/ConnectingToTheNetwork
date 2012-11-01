package au.com.spinninghalf.connectingtothenetwork;

/*TODO
 * 1. Add in network availability checks before downloading xml gig guide
 * 2. 
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
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
	private TextView idTextView;
	//private Button goButton;
	
	SpinningHalfApplication shapp;
	
	TabListenerMobile<NewsFragmentOne> newsTabListenerMobile;
	TabListenerMobile<RehearsalsFragmentOne>rehearsalsTabListenerMobile;
	TabListenerMobileList<GigListFragment> gigGuideTabListenerMobile;
	TabListenerMobile<ManagementFragmentOne>managementTabListenerMobile;
	TabListenerMobile<ServicesFragmentOne>servicesTabListenerMobile; 
	TabListenerMobile<ContactFragmentOne>contactTabListenerMobile; 
	
	
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
		}
 }
    
	//implementation of the single method from GigListFragment inner interface.
	//used in communicating user selections made in the GigListFragment to the Activity.
	public void onGigSelected(long id) {
		//The user selected the show name of a gig from the GigListFragment
		shapp.setSelectedGigId(id);

        //Capture the viewGig fragment from the activity layout
        ViewGigFragment viewGigFrag = (ViewGigFragment)
                getSupportFragmentManager().findFragmentById(R.id.TabletFragmentContainer2);

        if (viewGigFrag != null) {
            //If article frag is available, we're in two-pane layout...

            //Call a method in the ViewGigFragment to update its content
            viewGigFrag.updateGigView(id);

        } else {
            //If the frag is not available, we're in the one-pane layout and must swap frags...
        	
            //Create fragment and give it an argument for the selected article
            ViewGigFragment newFragment = new ViewGigFragment();
            Bundle args = new Bundle();
            args.putLong(ViewGigFragment.ARG_ID, id);
            newFragment.setArguments(args);
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

            //Replace whatever is in the fragment_container view with this fragment,
            //and add the transaction to the back stack so the user can navigate back
            transaction.replace(R.id.MainFragmentContainer, newFragment);
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
	      
	    if (!tabletLayout) {
	      // Save the current Action Bar tab selection
	      int actionBarIndex = getSupportActionBar().getSelectedTab().getPosition();
	      SharedPreferences.Editor editor = getPreferences(Activity.MODE_PRIVATE).edit();
	      editor.putInt(ACTION_BAR_INDEX, actionBarIndex);
	      //editor.apply(); //this is only for API 9 and greater.
	      editor.commit();
	      
	      // Detach each of the Fragments
	      FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
	      if (newsTabListenerMobile.fragment != null)
	        ft.detach(newsTabListenerMobile.fragment);
	      if (gigGuideTabListenerMobile.fragment != null)
	        ft.detach(gigGuideTabListenerMobile.fragment);
	      ft.commit();
	    } 

	    super.onSaveInstanceState(outState);
	  }

	  @Override
	  public void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);

	    View fragmentContainer = findViewById(R.id.MainFragmentContainer); 
	    boolean tabletLayout = fragmentContainer == null;
	      
	    if (!tabletLayout) {
	      // Find the recreated Fragments and assign them to their associated Tab Listeners.
	      newsTabListenerMobile.fragment = (SherlockFragment)
	        getSupportFragmentManager().findFragmentByTag(NewsFragmentOne.class.getName());
	      gigGuideTabListenerMobile.fragment = (SherlockListFragment)
	        getSupportFragmentManager().findFragmentByTag(GigListFragment.class.getName());

	      // Restore the previous Action Bar tab selection.    
	      SharedPreferences sp = getPreferences(Activity.MODE_PRIVATE);
	      int actionBarIndex = sp.getInt(ACTION_BAR_INDEX, 0);
	      getSupportActionBar().setSelectedNavigationItem(actionBarIndex);
	    }
	  }
	  
	  @Override 
	  public void onResume() {
	    super.onResume();
	    View fragmentContainer = findViewById(R.id.MainFragmentContainer); 
	    boolean tabletLayout = fragmentContainer == null;
	      
	    if (!tabletLayout) {
	      SharedPreferences sp = getPreferences(Activity.MODE_PRIVATE);
	      int actionBarIndex = sp.getInt(ACTION_BAR_INDEX, 0);
	      getSupportActionBar().setSelectedNavigationItem(actionBarIndex);
	    }
	  }
	  
    
//-----------------------------------------------------------------------------------------------------------------------------------
    
    //////////////////////////////////////////////////////////////////////
    //                 TAB LISTENER DEFINITIONS                         //
    //////////////////////////////////////////////////////////////////////

	//listener for mobile layout and basic fragment class
	public static class TabListenerMobile<T extends SherlockFragment> implements ActionBar.TabListener {
		  
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
		  
		    // Called when a new tab has been selected
		    public void onTabSelected(Tab tab, FragmentTransaction ft) {
		      if (fragment == null) {
		        String fragmentName = fragmentClass.getName();
		        fragment = (SherlockFragment) Fragment.instantiate(activity, fragmentName); 
		        ft.add(fragmentContainer, fragment, fragmentName);
		      } else
		        ft.attach(fragment);
		    }
		  
		    // Called on the currently selected tab when a different tag is
		    // selected. 
		    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		      if (fragment != null)
		        ft.detach(fragment);
		    } 
		  
		    // Called when the selected tab is selected.
		    public void onTabReselected(Tab tab, FragmentTransaction ft) {
		      if (fragment != null)
		        ft.attach(fragment);
		    }
	  }
	
	//listener for mobile layout and list fragment class
	public static class TabListenerMobileList<T extends SherlockListFragment> implements ActionBar.TabListener {

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
	  
	    // Called when a new tab has been selected
	    public void onTabSelected(Tab tab, FragmentTransaction ft) {
	      if (fragment == null) {
	        String fragmentName = fragmentClass.getName();
	        fragment = (SherlockListFragment) Fragment.instantiate(activity, fragmentName); 
	        ft.add(fragmentContainer, fragment, fragmentName);
	      } else
	        ft.attach(fragment);
	    }
	  
	    // Called on the currently selected tab when a different tag is
	    // selected. 
	    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
	      if (fragment != null)
	        ft.detach(fragment);
	    } 
	  
	    // Called when the selected tab is selected.
	    public void onTabReselected(Tab tab, FragmentTransaction ft) {
	      if (fragment != null)
	        ft.attach(fragment);
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
		  
		    // Called when a new tab has been selected
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
		  
		    // Called on the currently selected tab when a different tag is
		    // selected. 
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
		  
		    // Called when the selected tab is selected.
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
			  
			    // Called when a new tab has been selected
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
			  
			    // Called on the currently selected tab when a different tag is
			    // selected. 
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
			  
			    // Called when the selected tab is selected.
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
	
    public OnClickListener youClickedMeListener = new OnClickListener() {
    	//when user clicks button, call AsyncTask.
    	//Before attempting to fetch the URL, makes sure that there is network connection
    	@Override
    	public void onClick(View view) {
    		
    		//Get the URL from the UI's text field.
    		//String stringUrl = urlText.getText().toString();
    		ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
    		NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
    		if (networkInfo != null && networkInfo.isConnected()) {
    			Intent startGigList = new Intent(getApplicationContext(), MainFragmentGigListActivity.class);
    			startGigList.putExtra(GIG_LIST_URL_KEY, SpinningHalfApplication.SPINNINGHALF_GIGLIST_WEBSERVICE);
    			startActivity(startGigList);
    		} else {
    			idTextView.setText("No network connection available");
    		}
    	}
    };
}