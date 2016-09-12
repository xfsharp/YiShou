package com.xf.yishou.entity;

import java.util.List;

/**
 * Created by xsp on 2016/9/12.
 */
public class Category {
    private int sortcount;
    private String sortkey;
    private List<Secondary> sortval;

    public Category(int sortcount, String sortkey, List<Secondary> sortval) {
        this.sortcount = sortcount;
        this.sortkey = sortkey;
        this.sortval = sortval;
    }

    public int getSortcount() {
        return sortcount;
    }

    public void setSortcount(int sortcount) {
        this.sortcount = sortcount;
    }

    public String getSortkey() {
        return sortkey;
    }

    public void setSortkey(String sortkey) {
        this.sortkey = sortkey;
    }

    public List<Secondary> getSortval() {
        return sortval;
    }

    public void setSortval(List<Secondary> sortval) {
        this.sortval = sortval;
    }

    @Override
    public String toString() {
        return "Category [sortcount=" + sortcount + ", sortkey=" + sortkey
                + ", sortval=" + sortval + "]";
    }

    public static class Secondary{

        private String sortname;
        private int sortnum;

        public Secondary(String sortname, int sortnum) {
            this.sortname = sortname;
            this.sortnum = sortnum;
        }

        public String getSortname() {
            return sortname;
        }

        public void setSortname(String sortname) {
            this.sortname = sortname;
        }

        public int getSortnum() {
            return sortnum;
        }

        public void setSortnum(int sortnum) {
            this.sortnum = sortnum;
        }

        @Override
        public String toString() {
            return "Secondary [sortname=" + sortname + ", sortnum=" + sortnum
                    + "]";
        }
    }
}
