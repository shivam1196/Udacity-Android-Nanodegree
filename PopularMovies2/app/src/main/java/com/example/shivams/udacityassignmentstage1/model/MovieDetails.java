package com.example.shivams.udacityassignmentstage1.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MovieDetails implements Parcelable {
    private String mMovieName;
    private String mMovieReleaseDate;
    private String mMovieRating;
    private String mMoviePoster;
    private String mMovieDescription;
    private String mId;
    private String mMovieBackdrop;

    public MovieDetails(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mMovieName);
        dest.writeString(mMovieReleaseDate);
        dest.writeString(mMovieRating);
        dest.writeString(mMoviePoster);
        dest.writeString(mMovieDescription);
        dest.writeString(mMovieBackdrop);
        dest.writeString(mId);



    }
    public  MovieDetails(Parcel parcel){
        mMovieName = parcel.readString();
        mMovieReleaseDate=parcel.readString();
        mMovieRating=parcel.readString();
        mMoviePoster=parcel.readString();
        mMovieDescription=parcel.readString();
        mMovieBackdrop= parcel.readString();
        mId = parcel.readString();


    }

    public static final Parcelable.Creator<MovieDetails> CREATOR = new Parcelable.Creator<MovieDetails>(){
        @Override
        public MovieDetails[] newArray(int size) {
            return new MovieDetails[0];
        }

        @Override
        public MovieDetails createFromParcel(Parcel source) {
            return new MovieDetails(source);
        }

    };

    public String getmMovieName() {
        return mMovieName;
    }

    public void setmMovieName(String mMovieName) {
        this.mMovieName = mMovieName;
    }

    public String getmMovieReleaseDate() {
        return mMovieReleaseDate;
    }

    public void setmMovieReleaseDate(String mMovieReleaseDate) {
        this.mMovieReleaseDate = mMovieReleaseDate;
    }

    public String getmMovieRating() {
        return mMovieRating;
    }

    public void setmMovieRating(String mMovieRating) {
        this.mMovieRating = mMovieRating;
    }

    public String getmMoviePoster() {
        return mMoviePoster;
    }

    public void setmMoviePoster(String mMoviePoster) {
        this.mMoviePoster = mMoviePoster;
    }

    public String getmMovieDescription() {
        return mMovieDescription;
    }

    public void setmMovieDescription(String mMovieDescription) {
        this.mMovieDescription = mMovieDescription;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmMovieBackdrop() {
        return mMovieBackdrop;
    }

    public void setmMovieBackdrop(String mMovieBackdrop) {
        this.mMovieBackdrop = mMovieBackdrop;
    }
}
