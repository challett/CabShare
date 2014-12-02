package com.example.connor.cabshare;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;

import java.util.HashMap;
import java.util.Map;


public class FirstTimeLogin extends Activity {
    private AuthData authData;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_login);
        authData = com.example.connor.cabshare.MyActivity.getInstance();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.first_time_login, menu);
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

    public void SubmitUserData(View V){
        EditText Name = (EditText)findViewById(R.id.nameEditText);
        String tempName = Name.getEditableText().toString();
        Map<String, String> map = new HashMap<String, String>();
        map.put("Name", tempName);
        Firebase ref = new Firebase("https://intense-torch-3362.firebaseio.com/");
        ref.child("users").child(authData.getUid()).setValue(map);
        Intent i = new Intent(FirstTimeLogin.this, MainMenuPage.class);
        startActivity(i);
        }
}
