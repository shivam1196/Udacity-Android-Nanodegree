package com.example.shivams.bakingapp.model.internetpojo;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class BakingResponse implements Parcelable {
    @SerializedName("id")
    private double id;

    @SerializedName("name")
    private String name;

    @SerializedName("ingredients")
    public List<Ingredients> mIngredients;

    @SerializedName("steps")
    private List<Steps> mSteps;

    @SerializedName("image")
    private String image;

    @SerializedName("servings")
    private int servings;

    public BakingResponse(){

    }


    public BakingResponse(Parcel source) {
         id = source.readDouble();
         name = source.readString();
         mIngredients = new ArrayList<Ingredients>();
         source.readList(mIngredients,Ingredients.class.getClassLoader());
         mSteps = new ArrayList<Steps>();
         source.readList(mSteps,Steps.class.getClassLoader());
         servings = source.readInt();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(id);
            dest.writeString(name);
            dest.writeList(mIngredients);
            dest.writeList(mSteps);
            dest.writeInt(servings);

    }


    public static final Parcelable.Creator<BakingResponse> CREATOR = new Parcelable.Creator<BakingResponse>(){

        @Override
        public BakingResponse createFromParcel(Parcel source) {
            return new BakingResponse(source);
        }

        @Override
        public BakingResponse[] newArray(int size) {
            return new BakingResponse[0];
        }
    };

    public Double getId() {
        return id;
    }

    public void setId(Double id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Ingredients> getmIngredients() {
        return mIngredients;
    }

    public void setmIngredients(List<Ingredients> mIngredients) {
        this.mIngredients = mIngredients;
    }

    public List<Steps> getmSteps() {
        return mSteps;
    }

    public void setmSteps(List<Steps> mSteps) {
        this.mSteps = mSteps;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public int getServings() {
        return servings;
    }

    public void setServings(int servings) {
        this.servings = servings;
    }
}
