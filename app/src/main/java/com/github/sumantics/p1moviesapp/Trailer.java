package com.github.sumantics.p1moviesapp;

public class Trailer {
    String trailerURL;
    String trailerName;

    public Trailer(String trailerURL, String trailerName) {
        this.trailerURL = trailerURL;
        this.trailerName = trailerName;
    }

    public String getTrailerURL() {
        return trailerURL;
    }

    public void setTrailerURL(String trailerURL) {
        this.trailerURL = trailerURL;
    }

    @Override
    public String toString() {
        return trailerName+":"+trailerURL;
    }
}
