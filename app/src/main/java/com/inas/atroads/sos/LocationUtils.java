package com.inas.atroads.sos;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.inas.atroads.services.CustomApplication;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.inas.atroads.services.CustomApplication.geoCoder;

public class LocationUtils
{
	private static final String TAG = LocationUtils.class.getSimpleName();
	@NonNull
	public static String getMyLocation(AppCompatActivity mActivity)
	{
		Location myLocation = new Location("");
		String address = "";
		if((mActivity != null) && ! (mActivity.isFinishing()))
		{
			if(ActivityCompat.checkSelfPermission(mActivity.getApplicationContext(),Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mActivity.getApplicationContext(),Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
			{
				myLocation = new Location("");
				ActivityCompat.requestPermissions(mActivity,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
			}
			else
			{
				GPSTracker gps = new GPSTracker(mActivity);
				// Check if GPS enabled
				if(gps.canGetLocation())
				{
					myLocation = new Location("");//provider name is unnecessary
					myLocation.setLatitude(gps.getLatitude());//your coords of course
					myLocation.setLongitude(gps.getLongitude());
					Log.v(TAG, "Location = " + myLocation);
					
					
					address = getAddress(gps.getLatitude(),gps.getLongitude(),mActivity);
//					address = GetAddressFromLatLng(gps.getLatitude(),gps.getLongitude());
					return address;
				}
				else
				{
					myLocation = new Location("");
					// Can't getInstance location.
					// GPS or network is not enabled.
					// Ask user to enable GPS/network in settings.
					gps.showSettingsAlert();
				}
			}
		}
		return address;
	}



	/****************************START OF GetAddressFromLatLng**************************/
	/**
	 *
	 * @param latitude
	 * @param longitude
	 * @return
	 */
	public static String GetAddressFromLatLng(Double latitude, Double longitude)
	{
		//Geocoder geocoder = null;
		List<Address> addresses;
		String address = "";
		//Geocoder geocoder = new Geocoder(HomeMapsActivity.this, Locale.getDefault());

//		Geocoder geocoder = CustomApplication.geoCoder;
		try {
			addresses = geoCoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
			address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
			String city = addresses.get(0).getLocality();
			String state = addresses.get(0).getAdminArea();
			String country = addresses.get(0).getCountryName();
			String postalCode = addresses.get(0).getPostalCode();
			String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
		} catch (IOException e) {
			e.printStackTrace();
		}
		Log.i(TAG, "GetAddressFromLatLng: "+address);

		return address;
	}


	/****************************END OF GetAddressFromLatLng**************************/




	public static String getAddress(double lat, double lng, AppCompatActivity mActivity)
	{
		String add = "";
//		Geocoder geocoder = new Geocoder(mActivity,Locale.getDefault());
		try {
			List<Address> addresses = geoCoder.getFromLocation(lat,lng,1);
			Address obj = addresses.get(0);
			add = obj.getAddressLine(0);
			add = add + "\n" + obj.getCountryName();
			add = add + "\n" + obj.getCountryCode();
			add = add + "\n" + obj.getAdminArea();
			add = add + "\n" + obj.getPostalCode();
			add = add + "\n" + obj.getSubAdminArea();
			add = add + "\n" + obj.getLocality();
			add = add + "\n" + obj.getSubThoroughfare();
			
			Log.v("IGA", "Address" + add);
			// Toast.makeText(this, "Address=>" + add,
			// Toast.LENGTH_SHORT).show();
			
			// TennisAppActivity.showDialog(add);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Toast.makeText(mActivity,e.getMessage(),Toast.LENGTH_SHORT).show();
		}
		return add;
	}
}


