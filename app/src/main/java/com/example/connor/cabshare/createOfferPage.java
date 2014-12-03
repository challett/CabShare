package com.example.connor.cabshare;

import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;


public class createOfferPage extends ActionBarActivity {
    private AuthData authData;
    private String tempDestination;
    private EditText destination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_offer_page);
        authData = com.example.connor.cabshare.MyActivity.getInstance();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_offer_page, menu);
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
        }
        return super.onOptionsItemSelected(item);
    }

    public void viewMap(View v) {
        Intent map = new Intent(createOfferPage.this, MapsActivity.class);
        startActivity(map);
    }

    public void submit_offer(View v) {
        destination = (EditText) findViewById(R.id.destination);
        tempDestination = destination.getEditableText().toString();
        Map<String, String> newOffer = new HashMap<String, String>();
        newOffer.put("destination", tempDestination);
        Firebase ref = new Firebase("https://intense-torch-3362.firebaseio.com/");
        ref.child("Offers").child(authData.getUid()).setValue(newOffer);
        String startLocation = getCurrentLocation();
        Intent viewOfferMenu = new Intent(createOfferPage.this, OfferMenuPage.class);
        startActivity(viewOfferMenu);
    }

    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    public String getCurrentLocation() {
        String location = new String();
        AppLocationService appLocationService = new AppLocationService(
                createOfferPage.this);
        Location gpsLocation = appLocationService
                .getLocation(LocationManager.GPS_PROVIDER);
        if (gpsLocation != null) {
            double latitude = gpsLocation.getLatitude();
            double longitude = gpsLocation.getLongitude();
            location = Double.toString(latitude) + " " + Double.toString(longitude);
        }
        return location;
    }
}
