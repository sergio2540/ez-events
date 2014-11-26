/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ezevents.android.app;

import java.util.ArrayList;
import java.util.List;

import com.google.android.gms.games.internal.api.NotificationsImpl;



import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemLongClickListener;



public class MainActivity extends FragmentActivity implements ActionBar.TabListener {

	public static Intent intent;
	public static ListView notificationsListView;
	public static List<String> notificationsList = new ArrayList<String>();
	public static Context cont;
	public static List<String> myEventsList;
	public static ArrayAdapter<String> myEventsListAdapter;
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_actions, menu);
		return super.onCreateOptionsMenu(menu);
	}



	/* The click listner for ListView in the navigation drawer */
	private class DrawerItemClickListener implements ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
			selectItem(position);
		}
	}
	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		// Handle presses on the action bar items
		int id = item.getItemId();
		if (id == R.id.action_create_event){
			create_event();
			return true;
		}
		else if (id ==  R.id.action_settings){
			//	    //openSettings();
			return true;
		}
		else
			return super.onOptionsItemSelected(item);
	}




	private void create_event() {
		Intent intent = getIntent();
		intent.setClass(this, EventFirstStepActivity.class);

		//attemptLogin();
		startActivity(intent);

	}


	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
	 * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
	 * derivative, which will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	AppSectionsPagerAdapter mAppSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will display the three primary sections of the app, one at a
	 * time.
	 */
	ViewPager mViewPager;


	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;

	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	private String[] mPlanetTitles;


	private void selectItem(int position) {
		// update the main content by replacing fragments
		//Fragment fragment = new PlanetFragment();
		Bundle args = new Bundle();
		//args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
		//fragment.setArguments(args);

		android.app.FragmentManager fragmentManager = getFragmentManager();
		//fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

		// update selected item and title, then close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mPlanetTitles[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		cont = this;
		intent = getIntent();

		setContentView(R.layout.activity_main);

		mTitle = mDrawerTitle = getTitle();
		mPlanetTitles = getResources().getStringArray(R.array.planets_array);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// set a custom shadow that overlays the main content when the drawer opens
		//mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow, GravityCompat.START);

		// set up the drawer's list view with items and click listener
		mDrawerList.setAdapter(new ArrayAdapter<String>(this,
				R.layout.drawer_list_item, mPlanetTitles));

		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// ActionBarDrawerToggle ties together the the proper interactions
		// between the sliding drawer and the action bar app icon
		mDrawerToggle = new ActionBarDrawerToggle(
				this,                  /* host Activity */
				mDrawerLayout,         /* DrawerLayout object */
				R.drawable.ic_drawer,  /* nav drawer image to replace 'Up' caret */
				R.string.drawer_open,  /* "open drawer" description for accessibility */
				R.string.drawer_close  /* "close drawer" description for accessibility */
				) {
			@Override
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
			}
		};

		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);



		// Create the adapter that will return a fragment for each of the three primary sections
		// of the app.
		mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(getSupportFragmentManager());

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();

		// Specify that the Home/Up button should not be enabled, since there is no hierarchical
		// parent.
		//actionBar.setHomeButtonEnabled(false);

		// Specify that we will be displaying tabs in the action bar.
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Set up the ViewPager, attaching the adapter and setting up a listener for when the
		// user swipes between sections.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mAppSectionsPagerAdapter);
		mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When swiping between different app sections, select the corresponding tab.
				// We can also use ActionBar.Tab#select() to do this if we have a reference to the
				// Tab.
				actionBar.setSelectedNavigationItem(position);
			}
		});

		//        // For each of the sections in the app, add a tab to the action bar.
		//        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
		//            // Create a tab with text corresponding to the page title defined by the adapter.
		//            // Also specify this Activity object, which implements the TabListener interface, as the
		//            // listener for when this tab is selected.
		//            
		//        }

		String sec1 = "My Events";
		String sec2 = "Popular Events";
		String sec3 = "Notifications";

		actionBar.addTab(
				actionBar.newTab()
				.setText(sec1)
				.setTabListener(this));

		actionBar.addTab(
				actionBar.newTab()
				.setText(sec2)
				.setTabListener(this));

		actionBar.addTab(
				actionBar.newTab()
				.setText(sec3)
				.setTabListener(this));


	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
	}

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
	 * sections of the app.
	 */
	public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

		public AppSectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int i) {
			switch (i) {
			case 0:
				// The first section of the app is the most interesting -- it offers
				// a launchpad into the other demonstrations in this example application
				return new LaunchpadSectionFragment();
			case 2:
				// The first section of the app is the most interesting -- it offers
				// a launchpad into the other demonstrations in this example application.
				return new InvitedSectionFragment();

			default:
				// The other sections of the app are dummy placeholders.
				Fragment fragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
				fragment.setArguments(args);
				return fragment;
			}
		}

		@Override
		public int getCount() {
			return 3;
		}

		//        @Override
		//        public int getCount() {
		//            return 5;
		//        }

		//        @Override
		//        public CharSequence getPageTitle(int position) {
		//            return "Section " + (position + 1);
		//        }
	}

	/**
	 * A fragment that launches other parts of the demo application.
	 */
	public static class LaunchpadSectionFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);

			ListView myEventsListview = (ListView)rootView.findViewById(R.id.my_events);

			myEventsList = new ArrayList<String>();
			myEventsList.add("Festa de aniversário do Gui");
			myEventsList.add("Jogo de Futebol na Alameda");
			myEventsList.add("Jantar de Despedida da Joana");
			myEventsListAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.event, myEventsList);
			new AsyncFetchMyEvents().execute();

			myEventsListview.setAdapter(myEventsListAdapter);
			
			myEventsListview.setOnItemClickListener(new OnItemClickListener() {


			    @Override
			    public void onItemClick(AdapterView<?> adapter, View view, int index, long arg3) {
			    	
			    	String event = myEventsList.get(index);
			    	new AsyncGetTodo(intent.getStringExtra("Username")).execute(event);

			


	
			    }
			});


			// Demonstration of a collection-browsing activity.
			//            rootView.findViewById(R.id.demo_collection_button)
			//                    .setOnClickListener(new View.OnClickListener() {
			//                        @Override
			//                        public void onClick(View view) {
			//                            Intent intent = new Intent(getActivity(), CollectionDemoActivity.class);
			//                            startActivity(intent);
			//                        }
			//                    });
			//
			//            // Demonstration of navigating to external activities.
			//            rootView.findViewById(R.id.demo_external_activity)
			//                    .setOnClickListener(new View.OnClickListener() {
			//                        @Override
			//                        public void onClick(View view) {
			//                            // Create an intent that asks the user to pick a photo, but using
			//                            // FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, ensures that relaunching
			//                            // the application from the device home screen does not return
			//                            // to the external activity.
			//                            Intent externalActivityIntent = new Intent(Intent.ACTION_PICK);
			//                            externalActivityIntent.setType("image/*");
			//                            externalActivityIntent.addFlags(
			//                                    Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			//                            startActivity(externalActivityIntent);
			//                        }
			//                    });



			return rootView;
		}
	}

	public static ArrayAdapter<String> notificationsAdapter;
	
	public static class InvitedSectionFragment extends Fragment {

		

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
		    
			View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);
			
			
			
			notificationsListView = (ListView) rootView.findViewById(R.id.my_events);
			notificationsAdapter = new ArrayAdapter<String>(this.getActivity(), R.layout.event, notificationsList);
			notificationsAdapter.notifyDataSetChanged();
			
			notificationsListView.setOnItemClickListener(new OnItemClickListener() {


			    @Override
			    public void onItemClick(AdapterView<?> adapter, View view, int index, long arg3) {
			    	
			    	String notification = notificationsList.get(index);
			    	new AsyncGetTodo(intent.getStringExtra("Username")).execute(notification);

			


	
			    }
			});

			
			
			notificationsList.clear();
			
			notificationsListView.setAdapter(notificationsAdapter);
			
			new AsyncFetch().execute(intent.getStringExtra("Username"));
			


			return rootView;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {

		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
			Bundle args = getArguments();
			((TextView) rootView.findViewById(android.R.id.text1)).setText(
					getString(R.string.dummy_section_text, args.getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
	}
}
