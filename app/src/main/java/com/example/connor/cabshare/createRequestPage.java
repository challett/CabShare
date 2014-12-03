package com.example.connor.cabshare;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class createRequestPage extends ActionBarActivity {

    private AuthData authData;
    private String tempreqDestination;
    private String tempreqStart;
    private EditText reqStart;
    private EditText reqDestination;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_request_page);

        authData = com.example.connor.cabshare.MyActivity.getInstance();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.create_request_page, menu);
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

    public void viewMap(View v){
        Intent map = new Intent(createRequestPage.this, RequestMapsActivity.class);
        startActivity(map);
    }
    public void submit_request(View v){

        reqStart = (EditText)findViewById(R.id.reqStart);
        reqDestination = (EditText)findViewById(R.id.reqDestination);

        tempreqStart = reqStart.getEditableText().toString();
        tempreqDestination = reqDestination.getEditableText().toString();

        Map<String, String> newOffer = new HashMap<String, String>();
        newOffer.put("Start", tempreqStart);
        newOffer.put("destination", tempreqDestination);
        Firebase ref = new Firebase("https://intense-torch-3362.firebaseio.com/");
        ref.child("Requests").child(authData.getUid()).setValue(newOffer);

        Intent seeOffers = new Intent (createRequestPage.this, viewOffersPage.class);
        startActivity(seeOffers);

    }

    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
    }
}
