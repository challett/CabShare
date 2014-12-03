package com.example.connor.cabshare;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.AuthData;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.util.HashMap;
import java.util.Map;

public class EditProfilePage extends ActionBarActivity {

    private AuthData authData;
    private String newName;
    private EditText name;
    private Button submit;

    private Firebase ref = new Firebase("https://intense-torch-3362.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile_page);

        authData = com.example.connor.cabshare.MyActivity.getInstance();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.edit_profile_page, menu);
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

    public void submitChanges(View v){
        submit = (Button)findViewById(R.id.submitButton);
        name = (EditText)findViewById(R.id.newName);
        newName = name.getText().toString();

        Map<String, String> newChanges = new HashMap<String, String>();
        newChanges.put("Name", newName);

        ref.child("users").child(authData.getUid()).setValue(newChanges);

    }
}
