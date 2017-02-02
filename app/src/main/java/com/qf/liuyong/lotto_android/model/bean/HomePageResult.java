package com.qf.liuyong.lotto_android.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/30 0030.
 */
public class HomePageResult implements Serializable{

    private List<NoticeListBean> noticeList;
    private List<BannerListBean> bannerList;
    private List<IndustryListBean> industryList;
    private List<StageListBean> stageList;
    private List<SortListBean> sortList;
    private List<String> msgList;

    public List<NoticeListBean> getNoticeList() {
        return noticeList;
    }

    public void setNoticeList(List<NoticeListBean> noticeList) {
        this.noticeList = noticeList;
    }

    public List<BannerListBean> getBannerList() {
        return bannerList;
    }

    public void setBannerList(List<BannerListBean> bannerList) {
        this.bannerList = bannerList;
    }

    public List<IndustryListBean> getIndustryList() {
        return industryList;
    }

    public void setIndustryList(List<IndustryListBean> industryList) {
        this.industryList = industryList;
    }

    public List<StageListBean> getStageList() {
        return stageList;
    }

    public void setStageList(List<StageListBean> stageList) {
        this.stageList = stageList;
    }

    public List<SortListBean> getSortList() {
        return sortList;
    }

    public void setSortList(List<SortListBean> sortList) {
        this.sortList = sortList;
    }

    public List<String> getMsgList() {
        return msgList;
    }

    public void setMsgList(List<String> msgList) {
        this.msgList = msgList;
    }

    public static class NoticeListBean implements Serializable{
        private String pid;
        private String position;
        private String type;
        private String url;
        private String gotoUrl;

        public String getPid() {
            return pid;
        }

        public void setPid(String pid) {
            this.pid = pid;
        }

        public String getPosition() {
            return position;
        }

        public void setPosition(String position) {
            this.position = position;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getGotoUrl() {
            return gotoUrl;
        }

        public void setGotoUrl(String gotoUrl) {
            this.gotoUrl = gotoUrl;
        }
    }

    public static class BannerListBean implements Serializable{
        private String type;
        private String url;

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class IndustryListBean implements Serializable{
        private String name;
        private String type;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public static class StageListBean implements Serializable{
        private String name;
        private String state;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }
    }

    public static class SortListBean implements Serializable{
        private String stage;
        private List<ValListBean> valList;

        public String getStage() {
            return stage;
        }

        public void setStage(String stage) {
            this.stage = stage;
        }

        public List<ValListBean> getValList() {
            return valList;
        }

        public void setValList(List<ValListBean> valList) {
            this.valList = valList;
        }

        public static class ValListBean implements Serializable{
            private String sortVal;
            private String sortName;

            public String getSortVal() {
                return sortVal;
            }

            public void setSortVal(String sortVal) {
                this.sortVal = sortVal;
            }

            public String getSortName() {
                return sortName;
            }

            public void setSortName(String sortName) {
                this.sortName = sortName;
            }
        }
    }
}
