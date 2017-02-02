package com.qf.liuyong.lotto_android.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/1/28 0028.
 */
public class Hometab implements Serializable{
    private List<TabBarListBean> tabBarList;

    public List<TabBarListBean> getTabBarList() {
        return tabBarList;
    }

    public void setTabBarList(List<TabBarListBean> tabBarList) {
        this.tabBarList = tabBarList;
    }

    public static class TabBarListBean implements Serializable {
        private String select3x;
        private String normal3x;
        private String name;
        private long updateTime;

        public String getSelect3x() {
            return select3x;
        }

        public void setSelect3x(String select3x) {
            this.select3x = select3x;
        }

        public String getNormal3x() {
            return normal3x;
        }

        public void setNormal3x(String normal3x) {
            this.normal3x = normal3x;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public long getUpdateTime() {
            return updateTime;
        }

        public void setUpdateTime(long updateTime) {
            this.updateTime = updateTime;
        }
    }
}
