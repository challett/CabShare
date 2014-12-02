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

/**
 * This application demos the use of the Firebase Login feature. It currently supports logging in
 * with Google, Facebook, Twitter, Email/Password, and Anonymous providers.
 * <p/>
 * The methods in this class have been divided into sections based on providers (with a few
 * general methods).
 * <p/>
 * Email/Password is provided using {@link com.firebase.client.Firebase}
 */
public class MyActivity extends ActionBarActivity{

    private TextView mLoggedInStatusTextView;
    private ProgressDialog mAuthProgressDialog;
    private Firebase ref;
    private static AuthData authData;
    public static AuthData authData2;
    private static final String TAG = "CabShare";

    private EditText email;
    private EditText pass;

    /***************************************
     *               PASSWORD              *
     ***************************************/
    private Button mPasswordLoginButton;
    private Button newUser;

    public static synchronized AuthData getInstance(){
        authData2 = authData;
        return authData2;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);

        email = (EditText)findViewById(R.id.destination);
        pass = (EditText)findViewById(R.id.editText2);
        newUser = (Button)findViewById(R.id.editProfile);
        newUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(MyActivity.this, ProfileCreationPage.class);
                startActivity(myIntent);
            }
        });

        mPasswordLoginButton = (Button)findViewById(R.id.login_with_password);
        mPasswordLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginWithPassword();

            }
        });




        /***************************************
         *               GENERAL               *
         ***************************************/
        mLoggedInStatusTextView = (TextView)findViewById(R.id.login_status);

        /* Create the SimpleLogin class that is used for all authentication with Firebase */
        String firebaseUrl = getResources().getString(R.string.firebase_url);
        Firebase.setAndroidContext(getApplicationContext());
        ref = new Firebase(firebaseUrl);

        /* Setup the progress dialog that is displayed later when authenticating with Firebase */
        mAuthProgressDialog = new ProgressDialog(this);
        mAuthProgressDialog.setTitle("Loading");
        mAuthProgressDialog.setMessage("Authenticating with Firebase...");
        mAuthProgressDialog.setCancelable(false);
        mAuthProgressDialog.show();

        /* Check if the user is authenticated with Firebase already. If this is the case we can set the authenticated
         * user and hide hide any login buttons */
        ref.addAuthStateListener(new Firebase.AuthStateListener() {
            @Override
            public void onAuthStateChanged(AuthData authData) {
                mAuthProgressDialog.hide();
                setAuthenticatedUser(authData);
            }
        });
    }

    /**
     * This method fires when any startActivityForResult finishes. The requestCode maps to
     * the value passed into startActivityForResult.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Map<String, String> options = new HashMap<String, String>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* If a user is currently authenticated, display a logout menu */
        if (this.authData != null) {
            getMenuInflater().inflate(R.menu.main, menu);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_logout) {
            logout();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Unauthenticate from Firebase and from providers where necessary.
     */
    private void logout() {
        if (this.authData != null) {
            /* logout of Firebase */
            ref.unauth();
            /* Update authenticated user and show login buttons */
            setAuthenticatedUser(null);
        }
    }

    /**
     * This method will attempt to authenticate a user to firebase given an oauth_token (and other
     * necessary parameters depending on the provider)
     */
    private void authWithFirebase(final String provider, Map<String, String> options) {
        if (options.containsKey("error")) {
            showErrorDialog(options.get("error"));
        } else {
            mAuthProgressDialog.show();
            if (provider.equals("twitter")) {
                // if the provider is twitter, we pust pass in additional options, so use the options endpoint
                ref.authWithOAuthToken(provider, options, new AuthResultHandler(provider));
            } else {
                // if the provider is not twitter, we just need to pass in the oauth_token
                ref.authWithOAuthToken(provider, options.get("oauth_token"), new AuthResultHandler(provider));
            }
        }
    }

    /**
     * Once a user is logged in, take the authData provided from Firebase and "use" it.
     */
    private void setAuthenticatedUser(AuthData authData) {
        if (authData != null) {
            /* Hide all the login buttons */
            //mPasswordLoginButton.setVisibility(View.GONE);//replace with main menu intent
            //newUser.setVisibility(View.GONE);
            Intent callMain = new Intent(MyActivity.this, MainMenuPage.class);  //prepare the intent to redirect from logging in to main menu
            startActivity(callMain);    //redirect to main menu


            mLoggedInStatusTextView.setVisibility(View.VISIBLE);
        } else {
            /* No authenticated user show all the login buttons */
            mPasswordLoginButton.setVisibility(View.VISIBLE);
            mLoggedInStatusTextView.setVisibility(View.GONE);
            newUser.setVisibility(View.VISIBLE);
        }
        this.authData = authData;
        /* invalidate options menu to hide/show the logout button */
        supportInvalidateOptionsMenu();
    }

    /**
     * Show errors to users
     */
    private void showErrorDialog(String message) {
        new AlertDialog.Builder(this)
                .setTitle("Error")
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /**
     * Utility class for authentication results
     */
    private class AuthResultHandler implements Firebase.AuthResultHandler {

        private final String provider;

        public AuthResultHandler(String provider) {
            this.provider = provider;
        }
        private Object userData;
        @Override
        public void onAuthenticated(AuthData authData) {
            mAuthProgressDialog.hide();
            Log.i(TAG, provider + " auth successful");
            setAuthenticatedUser(authData);
            final String userID = authData.getUid();
            ref.child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    userData = snapshot.getValue();
                    if (userData == null) {
                        Map<String, String> map = new HashMap<String, String>();
                        map.put("Test Data", "Hello World IM NOT WORKING IF YOU SEE ME IN DBx2");
                        ref.child("users").child(userID).setValue(map);
                    }
                }

                @Override
                public void onCancelled(FirebaseError firebaseError) {
                    System.out.println("The read failed: " + firebaseError.getMessage());
                }
            });

        }

        @Override
        public void onAuthenticationError(FirebaseError firebaseError) {
            mAuthProgressDialog.hide();
            showErrorDialog(firebaseError.toString());
        }
    }



    public void onConnectionSuspended(int i) {
        // ignore
    }



    /***************************************
     *               PASSWORD              *
     ***************************************/
    public void loginWithPassword() {
        String tempEmail = email.getEditableText().toString();
        String tempPass = pass.getEditableText().toString();


        mAuthProgressDialog.show();
        ref.authWithPassword(tempEmail, tempPass, new AuthResultHandler("password"));

    }




}

