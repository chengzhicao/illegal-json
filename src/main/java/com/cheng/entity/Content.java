package com.cheng.entity;

import com.cheng.illegaljson.CompareSelect;
import com.cheng.illegaljson.IllegalJsonDeserializer;
import com.google.gson.annotations.JsonAdapter;

import java.util.List;

@JsonAdapter(value = IllegalJsonDeserializer.class)
public class Content {
    private String status;
    private String message;
    private List<DataBean> data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public class DataBean {
        private long id;
        @CompareSelect({"newsInfo", "specialInfo"})
        private NewsInfoBean newsInfo;
        private FreshnewsInfoBean freshnewsInfo;
        private int contentType;
        private String createTime;
        private String showType;

        public long getId() {
            return id;
        }

        public void setId(long id) {
            this.id = id;
        }

        public NewsInfoBean getNewsInfo() {
            return newsInfo;
        }

        public void setNewsInfo(NewsInfoBean newsInfo) {
            this.newsInfo = newsInfo;
        }

        public FreshnewsInfoBean getFreshnewsInfo() {
            return freshnewsInfo;
        }

        public void setFreshnewsInfo(FreshnewsInfoBean freshnewsInfo) {
            this.freshnewsInfo = freshnewsInfo;
        }

        public int getContentType() {
            return contentType;
        }

        public void setContentType(int contentType) {
            this.contentType = contentType;
        }

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public String getShowType() {
            return showType;
        }

        public void setShowType(String showType) {
            this.showType = showType;
        }
    }
}

