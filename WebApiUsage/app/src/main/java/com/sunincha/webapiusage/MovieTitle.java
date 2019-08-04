package com.sunincha.webapiusage;

public class MovieTitle {
    private String mTitle;
    private String mLanguage;
    private int mYearOfRelease;
    private String mRating;
    private String mGenre;

    public MovieTitle(String title, String language, int year, String rating, String genre) {
        this.mTitle = title;
        this.mLanguage = language;
        this.mYearOfRelease = year;
        this.mRating = rating;
        this.mGenre = genre;
    }

    public String getTile() {
        return this.mTitle;
    }

    public String getRating() {
        return this.mRating;
    }

    public int getYearOfRelease() {
        return this.mYearOfRelease;
    }

    public String getGenre() {
        return this.mGenre;
    }

    public String getLanguage() {
        return this.mLanguage;
    }

}
