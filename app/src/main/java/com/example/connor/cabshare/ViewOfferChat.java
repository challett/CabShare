package com.example.connor.cabshare;

        import android.app.ListActivity;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.database.DataSetObserver;
        import android.os.Bundle;
        import android.view.KeyEvent;
        import android.view.View;
        import android.view.inputmethod.EditorInfo;
        import android.widget.*;

        import com.firebase.client.AuthData;
        import com.firebase.client.DataSnapshot;
        import com.firebase.client.Firebase;
        import com.firebase.client.FirebaseError;
        import com.firebase.client.ValueEventListener;

        import java.util.Map;
        import java.util.Random;

public class ViewOfferChat extends ListActivity {
    private static final String FIREBASE_URL = "https://intense-torch-3362.firebaseio.com/";


    private AuthData authData;
    private Firebase ref;
    private Firebase ref2;
    private ValueEventListener connectedListener;
    private ChatListAdapter chatListAdapter;
    private static String username;
    private String offerer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_offer_chat);
        ref2 = new Firebase(FIREBASE_URL);
        authData = com.example.connor.cabshare.MyActivity.getInstance();
        username = authData.getUid();

        offerer = viewOffersPage.getInstance();
        try{
            System.out.println(offerer);
        }catch (NullPointerException p){
            offerer = authData.getUid();
        }

        // Setup our Firebase ref
        ref = new Firebase(FIREBASE_URL).child("Offers").child(offerer).child("chat");

        // Setup our input methods. Enter key on the keyboard or pushing the send button
        EditText inputText = (EditText)findViewById(R.id.messageInput);
        inputText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                    sendMessage();
                }
                return true;
            }
        });

        findViewById(R.id.sendButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Setup our view and list adapter. Ensure it scrolls to the bottom as data changes
        final ListView listView = getListView();
        // Tell our list adapter that we only want 50 messages at a time
        chatListAdapter = new ChatListAdapter(ref.limit(50), this, R.layout.chat_message, username);
        listView.setAdapter(chatListAdapter);
        chatListAdapter.registerDataSetObserver(new DataSetObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                listView.setSelection(chatListAdapter.getCount() - 1);
            }
        });
    }

    @Override
    public void onStop() {
        super.onStop();
        ref.getRoot().child(".info/connected").removeEventListener(connectedListener);
        chatListAdapter.cleanup();
    }
    @Override
    public void onBackPressed(){
        super.onBackPressed();
        chatListAdapter.cleanup();
    }

    private void sendMessage() {
        EditText inputText = (EditText)findViewById(R.id.messageInput);
        String input = inputText.getText().toString();
        if (!input.equals("")) {
            // Create our 'model', a Chat object
            Chat chat = new Chat(input, username);
            // Create a new, auto-generated child of that chat location, and save our chat data there
            ref.push().setValue(chat);
            inputText.setText("");
        }
    }
}
