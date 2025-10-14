package com.example.demo.utils;

import com.example.demo.models.News;

public class PayloadRequestNew {
    private TokenInfo authToken;
    private News news;


    public News getNews() {
        return news;
    }


    public void setNews(News news) {
        this.news = news;
    }


    public String getAuthTokenId() {
        return authToken.getId();
    }

    public void setAuthToken(TokenInfo authToken) {
        this.authToken = authToken;
    }
}
