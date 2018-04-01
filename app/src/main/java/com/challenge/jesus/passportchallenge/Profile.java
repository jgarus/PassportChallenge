package com.challenge.jesus.passportchallenge;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.firebase.database.Exclude;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jesus on 9/14/17.
 *
 */

public class Profile implements Parcelable {

    @Exclude
    String key;

    //What we need Id, background color, gender, name, age, profile image, hobbies

    private String background_color, gender, name, image;
    private List<String> hobbies;
    private int age;
    private long _id;

    public Profile(){

    }

    public Profile(String background_color, String gender, String name, String image, List<String> hobbies, long _id, int age) {
        this.background_color = background_color;
        this.gender = gender;
        this.name = name;
        this.image = image;
        this.hobbies = hobbies;
        this._id = _id;
        this.age = age;
    }

    public String getBackground_color() {
        return background_color;
    }

    public void setBackground_color(String background_color) {
        this.background_color = background_color;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public List<String> getHobbies() {
        return hobbies;
    }

    public void setHobbies(List<String> hobbies) {
        this.hobbies = hobbies;
    }

    public long get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(background_color);
        dest.writeString(gender);
        dest.writeString(name);
        dest.writeString(image);
        dest.writeList(hobbies);
        dest.writeLong(_id);
        dest.writeInt(age);
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    protected Profile(Parcel in) {
        background_color = in.readString();
        gender = in.readString();
        name = in.readString();
        image = in.readString();
        hobbies = in.createStringArrayList();
        _id = in.readInt();
        age = in.readInt();
    }

    //Testing
    @Exclude
    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("background_color", background_color);
        result.put("gender", gender);
        result.put("name", name);
        result.put("image", image);
        result.put("hobbies", hobbies);
        result.put("_id", _id);
        result.put("age", age);

        return result;
    }
}
