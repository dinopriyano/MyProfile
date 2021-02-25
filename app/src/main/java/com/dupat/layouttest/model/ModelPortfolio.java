package com.dupat.layouttest.model;

public class ModelPortfolio {

    String nama,desc;
    int icon;
    int[] ss;

    public ModelPortfolio(String nama, String desc, int icon, int[] ss) {
        this.nama = nama;
        this.desc = desc;
        this.icon = icon;
        this.ss = ss;
    }

    public String getNama() {
        return nama;
    }

    public String getDesc() {
        return desc;
    }

    public int getIcon() {
        return icon;
    }

    public int[] getSs() {
        return ss;
    }
}
