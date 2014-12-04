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


public class ProfileCreationPage extends ActionBarActivity {
    private Firebase myFirebaseRef = new Firebase("https://intense-torch-3362.firebaseio.com/");
    private Button submit;
    private Button cancel;

    private EditText email;
    private EditText pass;
    private EditText name;
    private EditText picURL;
    private EditText username;

    private String tempEmail;
    private String tempUser;
    private String tempPass;
    private String tempName;
    private String tempPicURL;
    private String tempUsername;
    private AuthData authData;


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Map<String, String> options = new HashMap<String, String>();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_creation_page);
        authData = com.example.connor.cabshare.MyActivity.getInstance();
        submit = (Button)findViewById(R.id.Submit);
        cancel = (Button)findViewById(R.id.Cancel);

    }

    public void onBackPressed(){
        super.onBackPressed();
        this.finish();
    }

    public void submitClick(View v){
        email = (EditText)findViewById(R.id.destination);
        tempEmail = email.getText().toString();
        pass = (EditText)findViewById(R.id.editText3);
        tempPass = pass.getEditableText().toString();
        name = (EditText)findViewById(R.id.editText2);
        tempName = name.getEditableText().toString();
        picURL = (EditText)findViewById(R.id.radiusEditText);
        tempPicURL = picURL.getEditableText().toString();


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
        Map<String, String> newUser = new HashMap<String, String>();
        newUser.put("Name", tempName);
        newUser.put("Email", tempEmail);
        newUser.put("picURL", tempPicURL);
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
