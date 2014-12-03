package com.example.connor.cabshare;

import android.app.Activity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.firebase.client.Query;

public class RequestListAdapter extends FirebaseListAdapter<Request> {


    public RequestListAdapter(Query ref, Activity activity, int layout) {
        super(ref, Request.class, layout, activity);
    }

    /**
     * @param view
     * @param request
     */
    @Override
    protected void populateView(View view, Request request) {
        String requester = request.getRequester();
        TextView requesterText = (TextView)view.findViewById(R.id.requester);
        requesterText.setText(requester);
        ((TextView)view.findViewById(R.id.destination)).setText(request.getDestination());

        Button acceptRequest = (Button)view.findViewById(R.id.acceptButton);
        acceptRequest.setTag(requesterText.getText());

        Button rejectRequest = (Button)view.findViewById(R.id.rejectButton);
        rejectRequest.setTag(requesterText.getText());
    }
}
