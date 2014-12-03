package com.example.connor.cabshare;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
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

import java.util.HashMap;
import java.util.Map;

public class MainMenuPage extends ActionBarActivity {
    private AuthData authData;
    private Button offerCab;
    private ValueEventListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu_page);
        offerCab = (Button)findViewById(R.id.offerButton);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_menu_page, menu);
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

    public void editProfileClicked(View v){
        Intent redirectToEditProfile = new Intent(MainMenuPage.this, EditProfilePage.class);
        startActivity(redirectToEditProfile);
    }

    public void requestClick(View v){
        Intent req = new Intent(MainMenuPage.this, createRequestPage.class);
        startActivity(req);
    }

    public void offerClick(View v){
            Intent offer = new Intent(MainMenuPage.this, createOfferPage.class);
            startActivity(offer);


    }

    public void existingOfferClick(View v){
        //TODO: implement this
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Firebase ref = new Firebase("https://intense-torch-3362.firebaseio.com/");
        ref.unauth();
        this.finish();
    }
}
