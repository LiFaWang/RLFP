package net.huansi.com.rlfp.bean;

import huansi.net.qianjingapp.entity.WsData;

/**
 * Created by Administrator on 2017/7/7 0007.
 * 人力分派的头部筛选的所需数据
 */

public class AssignTopFilterBaseDataBean extends WsData {

    /**
     *订单号
     * [
     {
     "SORDERNO": "SO17040001",
     "SSTYLENO": "test",
     "SLOTNO": "test",
     "DDELIVERYDATE": "2017/4/16 0:00:00",
     "IORDERQTY": "600",
     "ICUTQTY": "60",
     "IUPQTY": "",
     "IDOWNQTY": "5"
     }
     ]
     */
    /**
     * {
     "STATUS": "0",
     "DATA": [
     {
     "SORDERNO": "SO17040001",
     "SSTYLENO": "test",
     "SPRODUCTNO": "49481"
     }
     ]
     }
     */

    public String SORDERNO="";//单号
    public String SSTYLENO="";//款号
    public String SPRODUCTNO="";//批次号



}
