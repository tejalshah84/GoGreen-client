package model;

import java.io.Serializable;

public class User implements Serializable {
    private int userId;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String roleType;
    private String interestArea;
    private String city;
    private String state;
    private int followersNum; // Should be array of users or user id
    private int followingNum; // Should be array of users or user id
    private String imageURL;

    public User() {
        username = "";
        firstName = "";
        lastName = "";
        roleType = "";
        interestArea = "";
        city = "";
        state = "";
        followersNum = 0;
        followingNum = 0;
        imageURL ="";
    }

    public User(int uid, String fn, String ln){
        this.userId=uid;
        this.firstName=fn;
        this.lastName=ln;
    }

    public User(int uid, String fn, String ln, String rt){
        this.userId=uid;
        this.firstName=fn;
        this.lastName=ln;
        this.roleType=rt;
    }

    public User(int uid, String fn, String ln, String rt, String ia, String pic){
        this.userId=uid;
        this.firstName=fn;
        this.lastName=ln;
        this.roleType=rt;
        this.interestArea=ia;
        this.imageURL=pic;
    }

    public User(String username, String firstName, String lastName, String roleType,String interestArea, String city, String state, String imageURL) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleType = roleType;
        this.interestArea = interestArea;
        this.city = city;
        this.state = state;
        this.followersNum = 0;
        this.followingNum = 0;
        this.imageURL =imageURL;
    }



    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    public String getLastName() {
        return lastName;
    }
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    public String getRoleType() {
        return roleType;
    }
    public void setRoleId(String roleType) {
        this.roleType = roleType;
    }
    public String getInterestArea() {
        return interestArea;
    }
    public void setInterestArea(String interestArea) {
        this.interestArea = interestArea;
    }
    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }
    public String getState() {
        return state;
    }
    public void setState(String state) {
        this.state = state;
    }
    public int getFollowersNum() {
        return followersNum;
    }
    public void setFollowersNum(int followersNum) {
        this.followersNum = followersNum;
    }
    public int getFollowingNum() {
        return followingNum;
    }
    public void setFollowingNum(int followingNum) {
        this.followingNum = followingNum;
    }
    public String getImageURL() {
        return imageURL;
    }
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    @Override
    public String toString() {
        return "User [userId=" + userId + ", username=" + username + ", password=" + password + ", firstName="
                + firstName + ", lastName=" + lastName + ", roleType=" + roleType + ", userInterest=" + interestArea
                + ", city=" + city + ", state=" + state + ", followersNum=" + followersNum + ", followingNum="
                + followingNum + ", imageURL=" + imageURL + "]";
    }
}