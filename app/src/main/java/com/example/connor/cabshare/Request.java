package com.example.connor.cabshare;

public class Request {

    private String destination;
    private String requester;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Request() { }

    Request(String destination, String requester) {
        this.destination = destination;
        this.requester = requester;
    }

    public String getDestination() {
        return destination;
    }

    public String getRequester() {
        return requester;
    }
}
