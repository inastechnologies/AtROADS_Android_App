package com.inas.atroads.views.Activities;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.inas.atroads.R;
import com.inas.atroads.util.localData.BaseActivity;
import com.inas.atroads.views.Fragment.OfferFragment;
import com.inas.atroads.views.Fragment.OnGoingRidesFragment;
import com.inas.atroads.views.Fragment.PastRidesFragment;
import com.inas.atroads.views.Fragment.UpadteFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.inas.atroads.util.Utilities.isNetworkAvailable;

public class YourRidesActivity extends BaseActivity {

    private Context mContext;
    private Toolbar toolbar;
    Address address;
    LatLng currentlatLng;
    Location mLastLocation;
    LocationRequest mLocationRequest;
    LocationManager mLocationManager;
    private String provider;
    private static String TAG = "YourRidesActivity";
    FusedLocationProviderClient fusedLocationProviderClient;
    FrameLayout simpleFrameLayout;
    private TabLayout tabs;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();

        //assining layout
        setContentView(R.layout.activity_your_ride);

        isNetworkAvailable(YourRidesActivity.this);
        /* intializing and assigning ID's */
        initViews();

        /* Navigation's and using the views */
      //  setViews();

    }

    private void initViews() {

        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.you_ride));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              onBackPressed();
            }
        });
        viewPager = findViewById(R.id.viewpager);

        // load the fragments when the tab is selected or page is swiped
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(2);
        tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
    }

    /**
     * * Layout manager that allows the user to flip left and right
     * through pages of data.  You supply an implementation of a
     * {PagerAdapter} to generate the pages that the view shows.
     *
     * @param viewPager
     */

    private void setupViewPager(ViewPager viewPager) {

        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new OnGoingRidesFragment(), getString(R.string.OnGoing));
        adapter.addFragment(new PastRidesFragment(), getString(R.string.PastRides));
        viewPager.setAdapter(adapter);

    }

    /**
     * * Implementation of {PagerAdapter} that
     * represents each page as a {@link Fragment} that is persistently
     * kept in the fragment manager as long as the user can return to the page.
     */

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {

            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitleList.get(position);
        }
    }



//    private void setViews() {
//        // get the reference of FrameLayout and TabLayout
//        simpleFrameLayout = (FrameLayout) findViewById(R.id.simpleFrameLayout);
//        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
//// Create a new Tab named "First"
//        TabLayout.Tab firstTab = tabLayout.newTab();
//        firstTab.setText("ONGOING"); // set the Text for the first Tab
////        firstTab.setIcon(R.drawable.ic_launcher); // set an icon for the
//// first tab
//        tabLayout.addTab(firstTab); // add  the tab at in the TabLayout
//// Create a new Tab named "Second"
//        TabLayout.Tab secondTab = tabLayout.newTab();
//        secondTab.setText("PAST RIDES"); // set the Text for the second Tab
////        secondTab.setIcon(R.drawable.ic_launcher); // set an icon for the second tab
//        tabLayout.addTab(secondTab); // add  the tab  in the TabLayout
////        tabLayout.getTabAt(1);
////        TabLayout.Tab tab = tabLayout.getTabAt(1);
////        tab.select();
//
//// perform setOnTabSelectedListener event on TabLayout
//        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
//            @Override
//            public void onTabSelected(TabLayout.Tab tab) {
//// get the current selected tab's position and replace the fragment accordingly
//                Fragment fragment = null;
//                switch (tab.getPosition()) {
//                    case 0:
//                        fragment = new OnGoingRidesFragment();
//                        break;
//                    case 1:
//                        fragment = new PastRidesFragment();
//                        break;
//                }
//                FragmentManager fm = getSupportFragmentManager();
//                FragmentTransaction ft = fm.beginTransaction();
//                ft.replace(R.id.simpleFrameLayout, fragment);
////                ft.add(R.id.simpleFrameLayout, fragment);
//                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
//                ft.commit();
//            }
//
//            @Override
//            public void onTabUnselected(TabLayout.Tab tab) {
//
//            }
//
//            @Override
//            public void onTabReselected(TabLayout.Tab tab) {
//
//            }
//        });
//    }

    @Override
    public void onBackPressed() {
//        Intent intent = new Intent(YourRidesActivity.this, HomeScreen.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//        finish();
        super.onBackPressed();
    }
}
