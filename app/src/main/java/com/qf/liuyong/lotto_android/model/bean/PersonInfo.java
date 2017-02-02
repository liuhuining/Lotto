package com.qf.liuyong.lotto_android.model.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/1/30 0030.
 */
public class PersonInfo {

    private int emailValidated;
    private String inviter;
    private int collectCount;
    private String investTotal;
    private String sex;
    private int supportCount;
    private int stateAudit;
    private Object cellPhone;
    private String identityPositive;
    private String workposition;
    private String empCode;
    private Object contacts;
    private String feedback;
    private String subTotal;
    private String identityNegative;
    private int msgCount;
    private int identityValidated;
    private int waitCount;
    private String identify;
    private int fill;
    private int subCount;
    private String portrait;
    private String school;
    private String email;
    private String address;
    private String realName;
    private String introduction;
    private String mobile;
    private int identityValidatedBack;
    private List<ListEntity> list;
    private String investmentOption;
    private int expertAuditState;
    private int expertCommentState;
    private String userId;

    public String getInvestmentOption() {
        return investmentOption;
    }

    public void setInvestmentOption(String investmentOption) {
        this.investmentOption = investmentOption;
    }
    public void setUserId(String userId) {
        this.userId = userId;
    }


    public void setEmailValidated(int emailValidated) {
        this.emailValidated = emailValidated;
    }

    public void setInviter(String inviter) {
        this.inviter = inviter;
    }

    public void setCollectCount(int collectCount) {
        this.collectCount = collectCount;
    }

    public void setInvestTotal(String investTotal) {
        this.investTotal = investTotal;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public void setSupportCount(int supportCount) {
        this.supportCount = supportCount;
    }

    public void setStateAudit(int stateAudit) {
        this.stateAudit = stateAudit;
    }

    public void setCellPhone(Object cellPhone) {
        this.cellPhone = cellPhone;
    }

    public void setIdentityPositive(String identityPositive) {
        this.identityPositive = identityPositive;
    }

    public void setWorkposition(String workposition) {
        this.workposition = workposition;
    }

    public void setEmpCode(String empCode) {
        this.empCode = empCode;
    }

    public void setContacts(Object contacts) {
        this.contacts = contacts;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public void setIdentityNegative(String identityNegative) {
        this.identityNegative = identityNegative;
    }

    public void setMsgCount(int msgCount) {
        this.msgCount = msgCount;
    }

    public void setIdentityValidated(int identityValidated) {
        this.identityValidated = identityValidated;
    }

    public void setIdentityValidatedBack(int identityValidatedBack) {
        this.identityValidatedBack = identityValidatedBack;
    }

    public void setWaitCount(int waitCount) {
        this.waitCount = waitCount;
    }

    public void setIdentify(String identify) {
        this.identify = identify;
    }

    public void setFill(int fill) {
        this.fill = fill;
    }

    public void setExpertAuditState(int expertAuditState) {
        this.expertAuditState = expertAuditState;
    }

    public void setExpertCommentState(int expertCommentState) {
        this.expertCommentState = expertCommentState;
    }
    public void setSubCount(int subCount) {
        this.subCount = subCount;
    }

    public void setPortrait(String portrait) {
        this.portrait = portrait;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setList(List<ListEntity> list) {
        this.list = list;
    }

    public int getEmailValidated() {
        return emailValidated;
    }

    public String getInviter() {
        return inviter;
    }

    public int getCollectCount() {
        return collectCount;
    }

    public String getInvestTotal() {
        return investTotal;
    }

    public String getSex() {
        return sex;
    }

    public int getSupportCount() {
        return supportCount;
    }

    public int getStateAudit() {
        return stateAudit;
    }

    public Object getCellPhone() {
        return cellPhone;
    }

    public String getIdentityPositive() {
        return identityPositive;
    }

    public String getWorkposition() {
        return workposition;
    }

    public String getEmpCode() {
        return empCode;
    }

    public Object getContacts() {
        return contacts;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public String getIdentityNegative() {
        return identityNegative;
    }

    public int getMsgCount() {
        return msgCount;
    }

    public int getIdentityValidated() {
        return identityValidated;
    }

    public int getIdentityValidatedBack() {
        return identityValidatedBack;
    }

    public int getWaitCount() {
        return waitCount;
    }

    public String getIdentify() {
        return identify;
    }

    public int getFill() {
        return fill;
    }

    public int getExpertAuditState() {
        return expertAuditState;
    }

    public int getExpertCommentState() {
        return expertCommentState;
    }

    public int getSubCount() {
        return subCount;
    }

    public String getPortrait() {
        return portrait;
    }

    public String getSchool() {
        return school;
    }

    public String getEmail() {
        return email;
    }

    public String getUserId() {
        return userId;
    }


    public String getAddress() {
        return address;
    }

    public String getRealName() {
        return realName;
    }

    public String getIntroduction() {
        return introduction;
    }

    public String getMobile() {
        return mobile;
    }

    public List<ListEntity> getList() {
        return list;
    }

    public static class ListEntity {
        private Object id;
        private String name;
        private String smallurl;
        private String bigurl;
        private Object status;
        private Object data;
        private Object weight;


        public void setId(Object id) {
            this.id = id;
        }

        public void setName(String name) {
            this.name = name;
        }

        public void setSmallurl(String smallurl) {
            this.smallurl = smallurl;
        }

        public void setBigurl(String bigurl) {
            this.bigurl = bigurl;
        }

        public void setStatus(Object status) {
            this.status = status;
        }

        public void setData(Object data) {
            this.data = data;
        }

        public void setWeight(Object weight) {
            this.weight = weight;
        }

        public Object getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public String getSmallurl() {
            return smallurl;
        }

        public String getBigurl() {
            return bigurl;
        }

        public Object getStatus() {
            return status;
        }

        public Object getData() {
            return data;
        }

        public Object getWeight() {
            return weight;
        }
    }
}
