package com.qf.liuyong.lotto_android.model.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/31 0031.
 */
public class ProjectResult implements Serializable {

    private String total;
    private boolean isLast;
    private String pageNo;
    private String pageSize;

    private List<ProjectListBean> projectList;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public boolean getIsLast() {
        return isLast;
    }

    public void setIsLast(boolean isLast) {
        this.isLast = isLast;
    }

    public String getPageNo() {
        return pageNo;
    }

    public void setPageNo(String pageNo) {
        this.pageNo = pageNo;
    }

    public String getPageSize() {
        return pageSize;
    }

    public void setPageSize(String pageSize) {
        this.pageSize = pageSize;
    }

    public List<ProjectListBean> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<ProjectListBean> projectList) {
        this.projectList = projectList;
    }

    public static class ProjectListBean implements Serializable{
        private String expertNum;
        private String sponsor;
        private String curMoney;
        private String leaveTime;
        private String goalMoney;
        private String praiseNum;
        private String collectNum;
        private String pId;
        private String headerUrl;
        private String updateNum;
        private String url;
        private String commentNum;
        private String projectStatus;
        private String stage;
        private String name;
        private String supportNum;
        private String progress;
        private String isFavor;
        private String isPraise;
        private String isVerify;
        private String publisherId;

        public String getPublisherId() {
            return publisherId;
        }

        public void setPublisherId(String publisherId) {
            this.publisherId = publisherId;
        }

        public String getIsVerify() {
            return isVerify;
        }

        public void setIsVerify(String isVerify) {
            this.isVerify = isVerify;
        }

        public String getProgress() {
            return progress;
        }

        public void setProgress(String progress) {
            this.progress = progress;
        }

        public String getExpertNum() {
            return expertNum;
        }

        public void setExpertNum(String expertNum) {
            this.expertNum = expertNum;
        }

        public String getSponsor() {
            return sponsor;
        }

        public void setSponsor(String sponsor) {
            this.sponsor = sponsor;
        }

        public String getCurMoney() {
            return curMoney;
        }

        public void setCurMoney(String curMoney) {
            this.curMoney = curMoney;
        }

        public String getLeaveTime() {
            return leaveTime;
        }

        public void setLeaveTime(String leaveTime) {
            this.leaveTime = leaveTime;
        }

        public String getGoalMoney() {
            return goalMoney;
        }

        public void setGoalMoney(String goalMoney) {
            this.goalMoney = goalMoney;
        }

        public String getPraiseNum() {
            return praiseNum;
        }

        public void setPraiseNum(String praiseNum) {
            this.praiseNum = praiseNum;
        }

        public String getCollectNum() {
            return collectNum;
        }

        public void setCollectNum(String collectNum) {
            this.collectNum = collectNum;
        }

        public String getpId() {
            return pId;
        }

        public void setpId(String pId) {
            this.pId = pId;
        }

        public String getHeaderUrl() {
            return headerUrl;
        }

        public void setHeaderUrl(String headerUrl) {
            this.headerUrl = headerUrl;
        }

        public String getUpdateNum() {
            return updateNum;
        }

        public void setUpdateNum(String updateNum) {
            this.updateNum = updateNum;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(String commentNum) {
            this.commentNum = commentNum;
        }

        public String getProjectStatus() {
            return projectStatus;
        }

        public void setProjectStatus(String projectStatus) {
            this.projectStatus = projectStatus;
        }

        public String getStage() {
            return stage;
        }

        public void setStage(String stage) {
            this.stage = stage;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getSupportNum() {
            return supportNum;
        }

        public void setSupportNum(String supportNum) {
            this.supportNum = supportNum;
        }

        public String getIsFavor() {
            return isFavor;
        }

        public void setIsFavor(String isFavor) {
            this.isFavor = isFavor;
        }

        public String getIsPraise() {
            return isPraise;
        }

        public void setIsPraise(String isPraise) {
            this.isPraise = isPraise;
        }
    }
}
