package com.example.connor.cabshare;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.database.DataSetObserver;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.example.connor.cabshare.MyActivity;
import com.firebase.client.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class viewOffersPage extends ListActivity {

    private Firebase ref;
    private ValueEventListener connectedListener;
    private OfferListAdapter offerListAdapter;
    private AuthData authData;

    private static String offerer;
    public static String offerer2;

    public static synchronized String getInstance(){
        offerer2 = offerer;
        return offerer2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offers_page);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_offers_page, menu);
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = getListView();
        // Tell our list adapter that we only want 50 messages at a time
        authData = com.example.connor.cabshare.MyActivity.getInstance();
        ref = new Firebase("https://intense-torch-3362.firebaseio.com/").child("Requests").child(authData.getUid()).child("MatchedOfferID");

        offerListAdapter = new OfferListAdapter(ref.limit(50), this, R.layout.matched_offer);
        listView.setAdapter(offerListAdapter);
        offerListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(offerListAdapter.getCount() - 1);

            }
        });


    }



    @Override
    public void onStop() {
        super.onStop();
        offerListAdapter.cleanup();
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

    @Override
    public void onBackPressed(){
        offerListAdapter.cleanup();
        super.onBackPressed();
        this.finish();
    }

    //Add Button functionalities here!

    public void acceptOffer(View v){
        Button acceptButton = (Button)v.findViewById(R.id.acceptButton);
        offerer = acceptButton.getTag().toString();

        Map<String, String> AskToJoin = new HashMap<String, String>();
        Map<String, String> NowWait = new HashMap<String, String>();
        AskToJoin.put("Offerer", offerer);
        NowWait.put(offerer,offerer);
        Firebase ref2 = new Firebase("https://intense-torch-3362.firebaseio.com/");
        ref2.child("Requests").child(authData.getUid()).child("AskToJoin").setValue(AskToJoin);
        ref2.child("Requests").child(authData.getUid()).child("AwaitResponse").setValue(NowWait);
        Intent wait = new Intent(viewOffersPage.this, requesterWaiting.class);
        startActivity(wait);

    }

    public void rejectOffer(View v){
        Button rejectButton = (Button)v.findViewById(R.id.rejectButton);
        offerer = rejectButton.getTag().toString();
        Firebase ref3 = new Firebase("https://intense-torch-3362.firebaseio.com/");
        ref3.child("Requests").child(authData.getUid()).child("MatchedOfferID").child(offerer).setValue(null);
    }

}
