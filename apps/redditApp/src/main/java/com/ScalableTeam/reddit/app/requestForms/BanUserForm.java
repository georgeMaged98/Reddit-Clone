package com.ScalableTeam.reddit.app.requestForms;

public class BanUserForm {
    private String modId;
    private String requestedBanUserId;
    private String redditId;

    public String getModId() {
        return modId;
    }

    public void setModId(String modId) {
        this.modId = modId;
    }

    public String getRequestedBanUserId() {
        return requestedBanUserId;
    }

    public void setRequestedBanUserId(String requestedBanUserId) {
        this.requestedBanUserId = requestedBanUserId;
    }

    public String getRedditId() {
        return redditId;
    }

    public void setRedditId(String redditId) {
        this.redditId = redditId;
    }
}
