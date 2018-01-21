package com.mustafa.udacityprojects.bakingapp.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * This class represents a step.
 */
public class Step {
    @SerializedName("id")
    @Expose
    private int mId;
    @SerializedName("shortDescription")
    @Expose
    private String mShortDescription;
    @SerializedName("description")
    @Expose
    private String mDescription;
    @SerializedName("videoURL")
    @Expose
    private String mVideoUrl;
    @SerializedName("thumbnailURL")
    @Expose
    private String mThumbnailUrl;

    public Step(int id, String shortDescription, String description, String videoUrl,
                String thumbnailUrl) {
        mId = id;
        mShortDescription = shortDescription;
        mDescription = description;
        mVideoUrl = videoUrl;
        mThumbnailUrl = thumbnailUrl;
    }

    public int getId() {
        return mId;
    }

    public void setId(int mId) {
        this.mId = mId;
    }

    public String getShortDescription() {
        return mShortDescription;
    }

    public void setShortDescription(String mShortDescription) {
        this.mShortDescription = mShortDescription;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String mDescription) {
        this.mDescription = mDescription;
    }

    public String getVideoUrl() {
        return mVideoUrl;
    }

    public void setVideoUrl(String mVideoUrl) {
        this.mVideoUrl = mVideoUrl;
    }

    public String getThumbnailUrl() {
        return mThumbnailUrl;
    }

    public void setThumbnailUrl(String mThumbnailUrl) {
        this.mThumbnailUrl = mThumbnailUrl;
    }
}
