package com.example.connor.cabshare;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.Map;

public class RequestListAdapter extends FirebaseListAdapter<Request> {

    private Firebase ref;
    public RequestListAdapter(Query ref, Activity activity, int layout) {
        super(ref, Request.class, layout, activity);
    }
    private String requesterName;
    private String requester;
    private TextView requesterText;
    /**
     * @param view
     * @param request
     */
    @Override
    protected void populateView(View view, Request request) {
        requester = request.getRequester();
        requesterText = (TextView)view.findViewById(R.id.requester);
        ref = new Firebase("https://intense-torch-3362.firebaseio.com/");
        ref.child("users").child(requester).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final Map<String, String> requesterData = (Map<String, String>)snapshot.getValue();
                requesterName = requesterData.get("Name");
                requesterText.setText(requesterName);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        ((TextView)view.findViewById(R.id.destination)).setText(request.getDestination());

        Button acceptRequest = (Button)view.findViewById(R.id.acceptButton);
        acceptRequest.setTag(requester);

        Button rejectRequest = (Button)view.findViewById(R.id.rejectButton);
        rejectRequest.setTag(requester);
    }
}
