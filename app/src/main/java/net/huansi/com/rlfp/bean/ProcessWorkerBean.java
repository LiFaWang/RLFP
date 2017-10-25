package net.huansi.com.rlfp.bean;

import huansi.net.qianjingapp.entity.WsData;

/**
 * Created by Tony on 2017/8/24.
 * 15:49
 */

public class ProcessWorkerBean extends WsData {
    /*
    {
    "IHDRID": "1056",
    "IIDEN": "1654",
    "SWORKTEAMNAME": "2线",
    "ICUPROCEDUREID": "3475",
    "SPARTNAME": "分包、验片、配片",
    "SEMPLOYEENAMECN": "杜新荣",
    "SPICTUREPATH": "",
    "SGRADENAME": "",
    "ISEQ": "276"
}
     */
    public String SWORKTEAMNAME="";//生产线名
    public String ICUPROCEDUREID="";//
    public String SPARTNAME="";//工序名
    public String SEMPLOYEENAMECN="";//员工名
    public String IHDRID="";//
    public String IIDEN="";//
    public String SPICTUREPATH="";//图片路径
    public String SGRADENAME="";//工序等级
    public String ISEQ="";//员工等级
    public boolean isSelected;
}