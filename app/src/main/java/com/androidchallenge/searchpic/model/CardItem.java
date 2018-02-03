package com.androidchallenge.searchpic.model;

/**
 * Created by waghup on 1/30/18.
 */

public class CardItem {
    private String cardItemTitle;
    private String cardImageUrl;

    public CardItem (String cardItemTitle,String cardImageUrl){
        this.cardItemTitle = cardItemTitle;
        this.cardImageUrl = cardImageUrl;
    }

    public String getCardImageUrl() {
        return cardImageUrl;
    }

    public void setCardImageUrl(String cardImageUrl) {
        this.cardImageUrl = cardImageUrl;
    }

    public String getCardItemTitle() {
        return cardItemTitle;
    }

    public void setCardItemTitle(String cardItemTitle) {
        this.cardItemTitle = cardItemTitle;
    }
}

