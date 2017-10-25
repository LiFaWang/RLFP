package net.huansi.com.rlfp.bean;

import huansi.net.qianjingapp.entity.WsData;

/**
 * Created by Tony on 2017/8/24.
 * 18:10
 */

public class ClassGroupBean extends WsData {
    /*
    {
    "SWORKTEAMNAME": "3线",
    "SEMPLOYEENO": "320300006068",
    "SEMPLOYEENAMECN": "石晓宁",
    "SWORKTYPE": "",
    "GPIC": "",
    "SGRADENAME": ""
}
     */
    public String SWORKTEAMNAME="";
    public String SEMPLOYEENO="";
    public String SEMPLOYEENAMECN="";
    public String SWORKTYPE="";
    public String SGRADENAME="";
    public String GPIC="";
    public boolean isSelected;

    @Override
    public String toString() {
        return "ClassGroupBean{" +
                "SWORKTEAMNAME='" + SWORKTEAMNAME + '\'' +
                ", SEMPLOYEENO='" + SEMPLOYEENO + '\'' +
                ", SEMPLOYEENAMECN='" + SEMPLOYEENAMECN + '\'' +
                ", SWORKTYPE='" + SWORKTYPE + '\'' +
                ", GPIC='" + GPIC + '\'' +
                '}';
    }
}
