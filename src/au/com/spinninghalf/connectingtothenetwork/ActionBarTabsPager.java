/*
 * Copyright (C) 2011 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.com.spinninghalf.connectingtothenetwork;


import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Tracker;

//import au.com.spinninghalf.connectingtothenetwork.R;




/**
 * This demonstrates the use of action bar tabs and how they interact
 * with other action bar features.
 */

public class ActionBarTabsPager extends SherlockFragmentActivity
	implements GigListFragment.OnGigListSelectedListener, GigListFragment.OnGigListRefreshedListener{
	private static final String TAG = "ActionBarTabsPager";
	public static final String GIG_LIST_URL_KEY = "au.com.spinninghalf.connectingtothenetwork.giglisturl";
	public static final String ARG_SELECTED_GIG_ID = "HttpExampleActivity_selected_gig_id";
	public static final String ARG_SELECTED_GIG_POSITION = "HttpExampleActivity_selected_gig_position";
	private static String ACTION_BAR_INDEX = "ACTION_BAR_INDEX";
	
	private long _selectedGigId = -1;
	private int _selectedGigPosition = -1;
	private static final long EASYTRACKER_REFRESH_BUTTON = 1;
	
	SpinningHalfApplication shapp;
	
    ViewPager mViewPager;
    TabsAdapter mTabsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //getWindow().setWindowAnimations(android.R.anim.slide_in_left);
        //overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        
        shapp = SpinningHalfApplication.getInstance();
        
        PackageManager pm = getPackageManager();
        
        //determine whether the device has telephony capabilities i.e. can make a phone call
        boolean telephonySupported = 
        		pm.hasSystemFeature(PackageManager.FEATURE_TELEPHONY);
        // set the result to the application variable 'hasDeviceTelephonyCapabilities'.
        shapp.setTelephonyCapability(telephonySupported);

        mViewPager = new ViewPager(this);
        mViewPager.setId(R.id.pager);
        setContentView(mViewPager);

        final ActionBar bar = getSupportActionBar();
        bar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        bar.setDisplayOptions(0, ActionBar.DISPLAY_SHOW_TITLE);

        mTabsAdapter = new TabsAdapter(this, mViewPager);
        
        mTabsAdapter.addTab(bar.newTab().setText("News"),
                NewsFragmentOne.class, null, "NEWS_TAG");
        mTabsAdapter.addTab(bar.newTab().setText("Rehearsals"),
        		RehearsalsFragmentOne.class, null, "REHEARSALS_TAG");
        mTabsAdapter.addTab(bar.newTab().setText("Gig Guide"),
        		GigListFragment.class, null, "GIG_LIST_TAG");
        mTabsAdapter.addTab(bar.newTab().setText("Management"),
        		ManagementFragmentOne.class, null, "MANAGEMENT_TAG");
        mTabsAdapter.addTab(bar.newTab().setText("Services"),
        		ServicesFragmentOne.class, null, "SERVICES_TAG");
        mTabsAdapter.addTab(bar.newTab().setText("Contact"),
        		ContactFragmentOne.class, null, "CONTACT_TAG");
        

        if (savedInstanceState != null) {
            bar.setSelectedNavigationItem(savedInstanceState.getInt("tab", 0));
        }
    }
    
	@Override
	  public void onStart() {
	    super.onStart();
	    EasyTracker.getInstance().activityStart(this); // Add this method.
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
	    //if (findViewById(R.id.MainFragmentContainer) != null) {	
        	
            //Create an instance of GigListFragment
            //GigListFragment refreshedFragment = new GigListFragment();
            Log.i(TAG, "in onGigListRefreshed() and below making new GigListFragment with a refreshed database.");
            
            Tracker myTracker = EasyTracker.getTracker();
            myTracker.trackEvent("ui_action", "button_press", "onGigListRefreshed_method_call", null);
            
            mTabsAdapter.onPageSelected(2);
            
            //mTabsAdapter.instantiateItem(mViewPager, 2);
            /*
            //Add the fragment to the 'fragment_container' FrameLayout
            getSupportFragmentManager().beginTransaction()
                    //.add(R.id.gig_guide_fragment_container, firstFragment).commit();
            		.replace(R.id.pager, refreshedFragment, "GIG_GUIDE_TAG").commit();
            		*/
	}
	
	@Override
	public void onStop() {
		super.onStop();
		
		EasyTracker.getInstance().activityStop(this); // Add this method.
	}
	  

    @SuppressLint("NewApi")
	@Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("tab", getSupportActionBar().getSelectedNavigationIndex());
    }

    /**
     * This is a helper class that implements the management of tabs and all
     * details of connecting a ViewPager with associated TabHost.  It relies on a
     * trick.  Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show.  This is not sufficient for switching
     * between pages.  So instead we make the content part of the tab host
     * 0dp high (it is not shown) and the TabsAdapter supplies its own dummy
     * view to show as the tab content.  It listens to changes in tabs, and takes
     * care of switch to the correct paged in the ViewPager whenever the selected
     * tab changes.
     */
    public class TabsAdapter extends FragmentStatePagerAdapter
            implements ActionBar.TabListener, ViewPager.OnPageChangeListener {
        private final Context mContext;
        private final ActionBar mActionBar;
        private final ViewPager mViewPager;
        private final ArrayList<TabInfo> mTabs = new ArrayList<TabInfo>();
        private String mTag;

        final class TabInfo {
            private final Class<?> clss;
            private final Bundle args;

            TabInfo(Class<?> _class, Bundle _args) {
                clss = _class;
                args = _args;
            }
        }

        public TabsAdapter(SherlockFragmentActivity activity, ViewPager pager) {
            super(activity.getSupportFragmentManager());
            mContext = activity;
            mActionBar = activity.getSupportActionBar();
            mViewPager = pager;
            mViewPager.setAdapter(this);
            mViewPager.setOnPageChangeListener(this);
            
        }

        public void addTab(ActionBar.Tab tab, Class<?> clss, Bundle args, String mTag) {
            TabInfo info = new TabInfo(clss, args);
            this.mTag = mTag;
            tab.setTag(info);
            tab.setTabListener(this);
            mTabs.add(info);
            mActionBar.addTab(tab);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mTabs.size();
        }

        @Override
        public Fragment getItem(int position) {
        	Log.i(TAG, "HELLO FROM getItem!!!");
            TabInfo info = mTabs.get(position);
            return Fragment.instantiate(mContext, info.clss.getName(), info.args);
        }
        
        @Override
        public int getItemPosition(Object object) {
        	return POSITION_NONE;
        }
        
        /*
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
        	Log.i(TAG, "HELLO FROM instantiateItem!!!");
        	View v = layoutInflater.inflate(R.layout.gig_listing);
        	((ViewPager) container).addView(new GigListFragment(), 0);
        	
        	return new GigListFragment();
        }
        */
        

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
        	Log.i(TAG, "HELLO FROM onPageSelected!!!");
            mActionBar.setSelectedNavigationItem(position);
        }

        @Override
        public void onPageScrollStateChanged(int state) {
        }

        @Override
        public void onTabSelected(Tab tab, FragmentTransaction ft) {
        	Log.i(TAG, "HELLO FROM onTabSelected!!!");
            Object tag = tab.getTag();
            for (int i=0; i<mTabs.size(); i++) {
                if (mTabs.get(i) == tag) {
                    mViewPager.setCurrentItem(i, true);
                }
            }
        }

        @Override
        public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        	Log.i(TAG, "HELLO FROM onTabUnselected!!!");
        }

        @Override
        public void onTabReselected(Tab tab, FragmentTransaction ft) {
        	Log.i(TAG, "HELLO FROM onTabReselected!!!");
        	
        	//SherlockListFragment mFragment = (SherlockListFragment) getSupportFragmentManager().findFragmentByTag(mTag); 
        	
        	notifyDataSetChanged();
        	
        	/*
        	//THIS IS CRASHING THE APP AFTER REFRESHING THE GIG LIST.
        	//results in the old giglistfragment over the top of the new one (i think). 
        	//the old one has an empty cursor, hence viewing a gig is empty textviews.
        	if(shapp.getUpdateGigGuideDatabase()) {
        		Log.i(TAG, "HELLO FROM onTabReselected + we are updating the gig guide!!!");
        		//Create an instance of GigListFragment
                //GigListFragment refreshedFragment = new GigListFragment();
                //getSupportFragmentManager().beginTransaction()
        		//  .replace(R.id.pager, refreshedFragment, "GIG_LIST_TAG");
                 //.replace(R.id.MainFragmentContainer, refreshedFragment).commit();
        		
        		
        		super.destroyItem(mViewPager, 2, mViewPager);
        		super.instantiateItem(mViewPager, 2);
        		
        		//mViewPager.setCurrentItem(2);
        	}
        	*/
        }
    }
    
    /*
    public class WebViewPager extends ViewPager {
        public WebViewPager(Context context, AttributeSet attrs) {
            super(context, attrs);
        }

        @Override
        protected boolean canScroll(View v, boolean checkV, int dx, int x, int y) {
            if (v instanceof ExtendedWebView) {
                return ((ExtendedWebView) v).canScrollHor(-dx);
            } else {
                return super.canScroll(v, checkV, dx, x, y);
            }
        }
    }
    */
}

