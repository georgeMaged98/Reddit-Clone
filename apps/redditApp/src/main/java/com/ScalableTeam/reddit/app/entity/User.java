package com.ScalableTeam.reddit.app.entity;

import com.arangodb.springframework.annotation.ArangoId;
import com.arangodb.springframework.annotation.Document;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.Date;
import java.util.HashMap;

@Document("users")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class User {
    @Id // db document field: _key
    private String userNameId;

    @ArangoId // db document field: _id
    private String arangoId;
    private String email;
    private String password;
    private String profilePhotoLink;
    private HashMap<String,Boolean> followedChannels;

    private HashMap<String,Boolean>followedUsers;
    private Date earliestTime;
    private Date latestTime;

    private String latestReadPostId;



    public Date getLatestTime() {
        return latestTime;
    }

    public void setLatestTime(Date latestTime) {
        this.latestTime = latestTime;
    }

    public HashMap<String,Boolean> getFollowedChannels() {
        return followedChannels;
    }

    public void setFollowedChannels(HashMap<String,Boolean> followedChannels) {
        this.followedChannels = followedChannels;
    }

    public HashMap<String,Boolean> getFollowedUsers() {
        return followedUsers;
    }

    public void setFollowedUsers(HashMap<String,Boolean> followedUsers) {
        this.followedUsers = followedUsers;
    }


    public String getUserNameId() {
        return userNameId;
    }

    public void setUserNameId(String userNameId) {
        this.userNameId = userNameId;
    }

    public String getArangoId() {
        return arangoId;
    }

    public void setArangoId(String arangoId) {
        this.arangoId = arangoId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfilePhotoLink() {
        return profilePhotoLink;
    }

    public void setProfilePhotoLink(String profilePhotoLink) {
        this.profilePhotoLink = profilePhotoLink;
    }
    public Date getEarliestTime() {
        return earliestTime;
    }

    public void setEarliestTime(Date earliestTime) {
        this.earliestTime = earliestTime;
    }
    public String getLatestReadPostId() {
        return latestReadPostId;
    }

    public void setLatestReadPostId(String latestReadPostId) {
        this.latestReadPostId = latestReadPostId;
    }
    @Override
    public String toString(){
        return "userNameId: "+userNameId;
    }
}
