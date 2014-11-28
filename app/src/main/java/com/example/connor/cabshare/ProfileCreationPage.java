package com.example.connor.cabshare;

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
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


public class ProfileCreationPage extends ActionBarActivity {

    private Button submit;
    private Button cancel;

    private EditText email;
    private EditText pass;

    private String tempEmail;
    private String tempUser;
    private String tempPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation_page);

        submit = (Button)findViewById(R.id.Submit);
        cancel = (Button)findViewById(R.id.Cancel);






    }

    public void submitClick(View v){
        email = (EditText)findViewById(R.id.editText);
        tempEmail = email.getText().toString();
        pass = (EditText)findViewById(R.id.editText3);
        tempPass = pass.getEditableText().toString();
        Firebase myFirebaseRef = new Firebase("https://intense-torch-3362.firebaseio.com/");
        myFirebaseRef.createUser(tempEmail, tempPass, new Firebase.ResultHandler() {
            @Override
            public void onSuccess() {
                // user was created
            }
            @Override
            public void onError(FirebaseError firebaseError) {
                // there was an error
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.profile_creation_page, menu);
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

    public void postToFirebase(){


    }
}
