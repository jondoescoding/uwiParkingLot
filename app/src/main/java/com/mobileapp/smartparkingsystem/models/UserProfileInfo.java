package com.mobileapp.smartparkingsystem.models;

public class UserProfileInfo {
    private String id;
    private String userName;
    private String email;
    private String photoUrl;
    private String phoneNo;





    private static UserProfileInfo userProfileInfo;

    private UserProfileInfo(){

    }

    public static UserProfileInfo getInstance(){
        if(userProfileInfo ==null){
            userProfileInfo = new UserProfileInfo();
        }
        return userProfileInfo;
    }





    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }


    public static UserProfileInfo getUserProfileInfo() {
        return userProfileInfo;
    }

    public static void setUserProfileInfo(UserProfileInfo userProfileInfo) {
        UserProfileInfo.userProfileInfo = userProfileInfo;
    }
}
