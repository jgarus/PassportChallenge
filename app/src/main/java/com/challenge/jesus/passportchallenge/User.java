package com.challenge.jesus.passportchallenge;

import java.util.List;

/**
 * Created by jesus on 9/14/17.
 *
 */

public class User {

    //What we need Id, background color, gender, name, age, profile image, hobbies

    private String background_color, gender, name, image;
    private List<String> hobbies;
    private int _id, age;

    public User(){
    }

    public User(String background_color, String gender, String name, String image, List<String> hobbies, int _id, int age) {
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

    public int get_id() {
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

}
