package com.cheng.entity;

import com.cheng.illegaljson.Select;

public class NewsInfoBean {
    private String releaseTime;
    private String shoulderTitle;
    private String careStatus;
    @Select("origin.sourceName")
    private String sourceName;

    public String getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(String releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getShoulderTitle() {
        return shoulderTitle;
    }

    public void setShoulderTitle(String shoulderTitle) {
        this.shoulderTitle = shoulderTitle;
    }

    public String getCareStatus() {
        return careStatus;
    }

    public void setCareStatus(String careStatus) {
        this.careStatus = careStatus;
    }

    public String getSourceName() {
        return sourceName;
    }

    public void setSourceName(String sourceName) {
        this.sourceName = sourceName;
    }
}
