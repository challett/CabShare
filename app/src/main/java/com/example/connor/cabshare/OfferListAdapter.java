package com.example.connor.cabshare;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.firebase.client.Query;

public class OfferListAdapter extends FirebaseListAdapter<Offer> {


    public OfferListAdapter(Query ref, Activity activity, int layout) {
        super(ref, Offer.class, layout, activity);
    }

    /**
     * @param view
     * @param offer
     */
    @Override
    protected void populateView(View view, Offer offer) {
        String offerer = offer.getOfferer();
        TextView offererText = (TextView)view.findViewById(R.id.offerer);
        offererText.setText(offerer);

        Button acceptOffer = (Button)view.findViewById(R.id.acceptButton);
        acceptOffer.setTag(offererText.getText());

        Button rejectOffer = (Button)view.findViewById(R.id.rejectButton);
        rejectOffer.setTag(offererText.getText());
        //((TextView)view.findViewById(R.id.destination)).setText(offer.getDestination());
    }

}
