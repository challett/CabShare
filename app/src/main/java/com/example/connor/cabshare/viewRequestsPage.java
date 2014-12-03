package com.example.connor.cabshare;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
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
import android.widget.Toast;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.example.connor.cabshare.MyActivity;
import com.firebase.client.ValueEventListener;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class viewRequestsPage extends ListActivity {

    private String username;
    private Firebase ref;
    private ValueEventListener connectedListener;
    private RequestListAdapter requestListAdapter;
    private AuthData authData;

    private String requester;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_requests_page);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.view_requests_page, menu);
        return true;
    }
    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = getListView();
        // Tell our list adapter that we only want 50 messages at a time
        authData = com.example.connor.cabshare.MyActivity.getInstance();
        ref = new Firebase("https://intense-torch-3362.firebaseio.com/").child("Offers").child(authData.getUid()).child("Requests");

        requestListAdapter = new RequestListAdapter(ref.limit(50), this, R.layout.matched_request);
        listView.setAdapter(requestListAdapter);
        requestListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(requestListAdapter.getCount() - 1);
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
        requestListAdapter.cleanup();
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
        requestListAdapter.cleanup();
        super.onBackPressed();
        this.finish();
    }

    public void acceptRequest(View v){
        Button acceptButton = (Button)v.findViewById(R.id.acceptButton);
        requester = acceptButton.getTag().toString();
        Firebase ref2 = new Firebase("https://intense-torch-3362.firebaseio.com/");
        Map<String, String> newOffer = new HashMap<String, String>();
        newOffer.put("Approved", authData.getUid());
        ref2.child("Requests").child(requester).child("Approved").setValue(newOffer);
        ref2.child("Offers").child(authData.getUid()).child("Requests").child(requester).setValue(null);

        Context context = getApplicationContext();
        CharSequence text = "User has been added to your offer!";
        int duration = Toast.LENGTH_LONG;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    public void rejectRequest(View v){
        Button rejectButton = (Button)v.findViewById(R.id.rejectButton);
        requester = rejectButton.getTag().toString();
        Firebase ref2 = new Firebase("https://intense-torch-3362.firebaseio.com/");
        ref2.child("Offers").child(authData.getUid()).child("Requests").child(requester).setValue(null);
        ref2.child("Requests").child(requester).child("AwaitResponse").setValue(null);
    }
}
