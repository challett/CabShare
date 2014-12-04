package com.example.connor.cabshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.text.DecimalFormat;
import java.util.Map;


public class Checkout extends Activity {

    private TextView inform;
    private Firebase ref = new Firebase("https://intense-torch-3362.firebaseio.com/");
    private static Double YOUPAYNOW;
    private String offerer = OfferMenuPage.getInstance();
    private Map<String, Double> userData;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        inform = (TextView)findViewById(R.id.inform);
        inform.setVisibility(View.GONE);
        ref.child("Offers").child(offerer).child("pricetopay").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                userData = (Map<String, Double>) snapshot.getValue();

                if (userData == null) {
                    inform.setVisibility(View.VISIBLE);
                    inform.setText("We are experiencing heavy loads, please try again");
                } else {
                    YOUPAYNOW = userData.get("pricetopay");
                    DecimalFormat df = new DecimalFormat("#.##");
                    inform.setText(df.format(YOUPAYNOW).toString());
                    inform.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.checkout, menu);
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

    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
    }

    public void getFare(View v){
        Intent i = new Intent(Checkout.this, MainMenuPage.class);
        startActivity(i);

    }

}
