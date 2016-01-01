package com.github.sumantics.p1moviesapp;

public class Review {
    String author;
    String content;
    String url;

    public Review(String author, String content, String url) {
        this.author = author;
        this.content = content;
        this.url = url;
    }

    @Override
    public String toString() {
        return author+":"+url;
    }
}
