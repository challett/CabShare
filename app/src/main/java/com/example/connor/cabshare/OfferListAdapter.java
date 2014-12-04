package com.example.connor.cabshare;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;

import java.util.Map;

public class OfferListAdapter extends FirebaseListAdapter<Offer> {


    public OfferListAdapter(Query ref, Activity activity, int layout) {
        super(ref, Offer.class, layout, activity);
    }
    Firebase ref;
    String offererName;
    TextView offererText;
    /**
     * @param view
     * @param offer
     */
    @Override
    protected void populateView(View view, Offer offer) {
        String offerer = offer.getOfferer();
        offererText = (TextView)view.findViewById(R.id.offerer);
        ref = new Firebase("https://intense-torch-3362.firebaseio.com/");
        ref.child("users").child(offerer).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final Map<String, String> offererData = (Map<String, String>)snapshot.getValue();
                offererName = offererData.get("Name");
                offererText.setText(offererName);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
                System.out.println("The read failed: " + firebaseError.getMessage());
            }
        });

        Button acceptOffer = (Button)view.findViewById(R.id.acceptButton);
        acceptOffer.setTag(offerer);

        Button rejectOffer = (Button)view.findViewById(R.id.rejectButton);
        rejectOffer.setTag(offerer);
        //((TextView)view.findViewById(R.id.destination)).setText(offer.getDestination());
    }

}
