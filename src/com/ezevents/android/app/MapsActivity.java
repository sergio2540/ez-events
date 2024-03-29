package com.ezevents.android.app;

import java.io.IOException;
import java.util.List;






import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;






import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity {

    GoogleMap googleMap;
    MarkerOptions markerOptions;
    LatLng latLng;
    private double lat;
    private double lon;
    
    Marker loc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.map);

	SupportMapFragment supportMapFragment = (SupportMapFragment)
		getSupportFragmentManager().findFragmentById(R.id.map);


	
	// Getting a reference to the map
	googleMap = supportMapFragment.getMap();


	googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
	googleMap.setMyLocationEnabled(true);

	double l1 = 38.737597;
	double l2 = -9.303257;
	
	CameraUpdate center=
		        CameraUpdateFactory.newLatLng(new LatLng(l1,
		                                                 l2));
		    CameraUpdate zoom=CameraUpdateFactory.zoomTo(15);

		    googleMap.moveCamera(center);
		    googleMap.animateCamera(zoom);
		    
		    
		    googleMap.addMarker(new MarkerOptions()
		        .position(new LatLng(38.737804, -9.302163))
		        .title("Campo de Futebol do IST - 968283363"));
		  
		    googleMap.addMarker(new MarkerOptions()
		        .position(new LatLng(38.737466, -9.304716))
		        .title("Pizzaria Costa - 968293563"));
		    
//		    googleMap.addMarker(new MarkerOptions()
//		        .position(new LatLng(l1+0.0005, l2+0.0003))
//		        .title("Restaurante Quasi - 968293563"));
//		    
		    
		    final LatLng C = new LatLng(l1, l2);
		    loc = googleMap.addMarker(new MarkerOptions()
                    .position(C)
                    .draggable(true));
		    
		    
	// Getting reference to btn_find of the layout activity_main
	Button btn_find = (Button) findViewById(R.id.btn_find);

	// Defining button click event listener for the find button
	OnClickListener findClickListener = new OnClickListener() {
	    @Override
	    public void onClick(View v) {
		// Getting reference to EditText to get the user input location
		EditText etLocation = (EditText) findViewById(R.id.et_location);

		// Getting user input location
		String location = etLocation.getText().toString();

		if(location!=null && !location.equals("")){
		    new GeocoderTask().execute(location);
		}
	    }
	};

	// Setting button click event listener for the find button
	btn_find.setOnClickListener(findClickListener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
	// Inflate the menu; this adds items to the action bar if it is present.
	getMenuInflater().inflate(R.menu.maps, menu);
	return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
	// Handle action bar item clicks here. The action bar will
	// automatically handle clicks on the Home/Up button, so long
	// as you specify a parent activity in AndroidManifest.xml.
	int id = item.getItemId();
	if (id == R.id.action_settings) {
	    return true;
	} else if (id == R.id.action_next_maps)
	{
	    Intent intent = getIntent();
	    intent.setClass(this, CheckListActivity.class);
	    LatLng l = loc.getPosition();
	    lat = l.latitude;
	    lon = l.longitude;
	    
	    intent.putExtra("Lat", lat);
	    intent.putExtra("Lon", lon);
	    //attemptLogin();
	    startActivity(intent);

	}
	return super.onOptionsItemSelected(item);
    }

    // An AsyncTask class for accessing the GeoCoding Web Service
    private class GeocoderTask extends AsyncTask<String, Void, List<Address>>{

	@Override
	protected List<Address> doInBackground(String... locationName) {
	    // Creating an instance of Geocoder class
	    Geocoder geocoder = new Geocoder(getBaseContext());
	    List<Address> addresses = null;

	    try {
		// Getting a maximum of 3 Address that matches the input text
		addresses = geocoder.getFromLocationName(locationName[0], 3);
	    } catch (IOException e) {
		e.printStackTrace();
	    }
	    return addresses;
	}

	@Override
	protected void onPostExecute(List<Address> addresses) {

	    if(addresses==null || addresses.size()==0){
		Toast.makeText(getBaseContext(), "No Location found", Toast.LENGTH_SHORT).show();
	    }

	    // Clears all the existing markers on the map
	    googleMap.clear();

	    // Adding Markers on Google Map for each matching address
	    for(int i=0;i<addresses.size();i++){

		Address address = addresses.get(i);

		// Creating an instance of GeoPoint, to display in Google Map
		latLng = new LatLng(address.getLatitude(), address.getLongitude());

		String addressText = String.format("%s, %s",
			address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",
				address.getCountryName());

		markerOptions = new MarkerOptions();
		markerOptions.position(latLng);
		markerOptions.title(addressText);
		lat = latLng.latitude;
		lon = latLng.longitude;
		googleMap.addMarker(markerOptions);

		// Locate the first location
		if(i==0)
		    googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
	    }
	}
    }
}