package com.helio.myreelty.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.helio.myreelty.network.models.UserModel;
import com.helio.myreelty.video_player.util.DateConvector;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fedir on 19.02.2016.
 */
public class HouseModel  implements Parcelable {

    public static String FOR_SALE = "For Sale";
    public static String NOT_AVAILABLE = "Not available";


    @SerializedName("user")
    @Expose
    public UserModel user;
    @SerializedName("liked")
    @Expose
    public boolean liked;
    @SerializedName("likes_count")
    @Expose
    public Integer likesCount;
    @SerializedName("bookmarked")
    @Expose
    public boolean bookmarked;
    @SerializedName("comments_count")
    @Expose
    public Integer commentsCount;
    @SerializedName("pictures")
    @Expose
    public PicturesModel pictures;
    @SerializedName("files")
    @Expose
    public List<FilesModel> files = new ArrayList<>();
    @SerializedName("zipcode")
    @Expose
    public String zipcode;
    @SerializedName("video_url")
    @Expose
    public String videoUrl;
    @SerializedName("user_id")
    @Expose
    public String state;
    @SerializedName("square")
    @Expose
    public Double square;
    @SerializedName("property_type")
    @Expose
    public String propertyType;
    @SerializedName("price")
    @Expose
    public Double price;
    @SerializedName("location")
    @Expose
    public double [] location = new double[2];
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("full_address")
    @Expose
    public String fullAddress;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("country")
    @Expose
    public String country;
    @SerializedName("city")
    @Expose
    public String city;
    @SerializedName("beds")
    @Expose
    public Integer beds;
    @SerializedName("baths")
    @Expose
    public Integer baths;
    @SerializedName("availability")
    @Expose
    public Boolean availability;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("created_at")
    @Expose
    public String createdAt;

    protected HouseModel(Parcel in) {
        liked = in.readByte() != 0;
        bookmarked = in.readByte() != 0;
        zipcode = in.readString();
        videoUrl = in.readString();
        state = in.readString();
        square = in.readDouble();
        propertyType = in.readString();
        fullAddress = in.readString();
        description = in.readString();
        country = in.readString();
        city = in.readString();
        address = in.readString();
        location = in.createDoubleArray();
        beds = in.readInt();
        baths = in.readInt();
        files = in.readArrayList(FilesModel.class.getClassLoader());
        availability = in.readByte() != 0;
        price = in.readDouble();
        id = in.readInt();
        likesCount = in.readInt();
        commentsCount = in.readInt();
    }

    public static final Creator<HouseModel> CREATOR = new Creator<HouseModel>() {
        @Override
        public HouseModel createFromParcel(Parcel in) {
            return new HouseModel(in);
        }

        @Override
        public HouseModel[] newArray(int size) {
            return new HouseModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (liked ? 1 : 0));
        dest.writeByte((byte) (bookmarked ? 1 : 0));
        dest.writeString(zipcode);
        dest.writeString(videoUrl);
        dest.writeString(state);
        dest.writeDouble(square);
        dest.writeString(propertyType);
        dest.writeString(fullAddress);
        dest.writeString(description);
        dest.writeString(country);
        dest.writeString(city);
        dest.writeString(address);
        dest.writeDoubleArray(location);
        dest.writeInt(beds);
        dest.writeInt(baths);
        dest.writeList(files);
        dest.writeByte((byte) (availability ? 1 : 0));
        dest.writeDouble(price);
        dest.writeInt(id);
        dest.writeInt(likesCount);
        dest.writeInt(commentsCount);

    }

    public HouseModel() {
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isBookmarked() {
        return bookmarked;
    }

    public void setBookmarked(boolean bookmarked) {
        this.bookmarked = bookmarked;
    }

    public HouseModel(UserModel user, PicturesModel pictures, List<FilesModel> files, String zipcode, String videoUrl, String state, Double square, String propertyType, Double price, double [] location, Integer id, String fullAddress, String description, String country, String city, Integer beds, Integer baths, Boolean availability, String address, boolean bookmarked, boolean liked, Integer likesCount, Integer commentsCount) {
        this.user = user;
        this.pictures = pictures;
        this.files = files;
        this.zipcode = zipcode;
        this.videoUrl = videoUrl;
        this.state = state;
        this.square = square;
        this.propertyType = propertyType;
        this.price = price;
        this.location = location;
        this.id = id;
        this.fullAddress = fullAddress;
        this.description = description;
        this.country = country;
        this.city = city;
        this.beds = beds;
        this.baths = baths;
        this.availability = availability;
        this.address = address;
        this.bookmarked = bookmarked;
        this.liked = liked;
        this.likesCount = likesCount;
        this.commentsCount = commentsCount;
    }

    public String getCreatedAt() {
        return DateConvector.formatDateFromString(createdAt);
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "HouseModel{" +
                "pictures=" + pictures +
                ", files=" + files +
                ", zipcode='" + zipcode + '\'' +
                ", videoUrl='" + videoUrl + '\'' +
                ", state='" + state + '\'' +
                ", square='" + square + '\'' +
                ", propertyType='" + propertyType + '\'' +
                ", price=" + price +
                ", location=" + location +
                ", id=" + id +
                ", fullAddress='" + fullAddress + '\'' +
                ", description='" + description + '\'' +
                ", country='" + country + '\'' +
                ", city='" + city + '\'' +
                ", beds=" + beds +
                ", baths=" + baths +
                ", availability=" + availability +
                ", address='" + address + '\'' +
                '}';
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public UserModel getUser() {
        return user;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getCommentsCount() {
        return commentsCount;
    }

    public void setCommentsCount(Integer commentsCount) {
        this.commentsCount = commentsCount;
    }

    public PicturesModel getPictures() {
        return pictures;
    }

    public void setPictures(PicturesModel pictures) {
        this.pictures = pictures;
    }

    public List<FilesModel> getFiles() {
        return files;
    }

    public void setFiles(List<FilesModel> files) {
        this.files = files;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Double getSquare() {
        return square;
    }

    public void setSquare(Double square) {
        this.square = square;
    }

    public String getPropertyType() {
        return propertyType;
    }

    public void setPropertyType(String propertyType) {
        this.propertyType = propertyType;
    }

    public int getPrice() {
        return price.intValue();
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public double[] getLocation() {
        return location;
    }

    public void setLocation(double[] location) {
        this.location = location;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Integer getBeds() {
        return beds;
    }

    public void setBeds(Integer beds) {
        this.beds = beds;
    }

    public Integer getBaths() {
        return baths;
    }

    public void setBaths(Integer baths) {
        this.baths = baths;
    }

    public Boolean getAvailability() {
        return availability;
    }

    public void setAvailability(Boolean availability) {
        this.availability = availability;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }


}
