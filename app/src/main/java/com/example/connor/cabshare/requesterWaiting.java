package com.example.connor.cabshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.AuthData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class requesterWaiting extends Activity {
    public String offerer;
    private Firebase ref = new Firebase("https://intense-torch-3362.firebaseio.com/");
    AuthData authData = MyActivity.getInstance();
    private TextView declinedText;
    private Button goBack;
    private TextView acceptedText;
    private Button offerButton;
    private ValueEventListener listener;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requester_waiting);
        declinedText = (TextView)findViewById(R.id.declinedTextView);
        goBack = (Button)findViewById(R.id.backButton);
        offerer = viewOffersPage.getInstance();
        acceptedText = (TextView)findViewById(R.id.acceptedTextView);
        offerButton = (Button)findViewById(R.id.offerButton);
        TextView awaitingText = (TextView)findViewById(R.id.awaitingText);
        awaitingText.setText("Awaiting response from " + offerer);
        listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final Object waitingOn = snapshot.child("AwaitResponse").getValue();
                final Object approved = snapshot.child("Approved").getValue();
                if (waitingOn != null) {
                    declinedText.setVisibility(View.GONE);
                    goBack.setVisibility(View.GONE);
                }
                else{
                    declinedText.setVisibility(View.VISIBLE);
                    goBack.setVisibility(View.VISIBLE);
                }
                if (approved == null){
                    acceptedText.setVisibility(View.GONE);
                    offerButton.setVisibility(View.GONE);
                }else{
                    acceptedText.setVisibility(View.VISIBLE);
                    offerButton.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        };
        ref.child("Requests").child(authData.getUid()).addValueEventListener(listener);
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        ref.child("Requests").child(authData.getUid()).removeEventListener(listener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.requester_waiting, menu);
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

    public void backToOffers(View v){
        Intent returnToOfferPage = new Intent(requesterWaiting.this, viewOffersPage.class);
        startActivity(returnToOfferPage);
    }

    public void goToOfferMenu(View v){
        Map<String, String> newPassenger = new HashMap<String, String>();
        newPassenger.put("name:", authData.getUid());
        ref.child("Offers").child(offerer).child("Passengers").child(authData.getUid()).setValue(newPassenger);
        Intent goToOfferMenuPage = new Intent(requesterWaiting.this, OfferMenuPage.class);
        startActivity(goToOfferMenuPage);
    }
}
