package com.example.connor.cabshare;

public class Offer {

    private String destination;
    private String offerer;

    // Required default constructor for Firebase object mapping
    @SuppressWarnings("unused")
    private Offer() { }

    Offer(String destination, String offerer) {
        this.destination = destination;
        this.offerer = offerer;
    }

    public String getDestination() {
        return destination;
    }

    public String getOfferer() {
        return offerer;
    }
}
