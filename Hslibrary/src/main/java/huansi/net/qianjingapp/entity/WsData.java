package huansi.net.qianjingapp.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by quanm on 2015-11-17.
 */
public class WsData implements WsEntity{
    public String SSTATUS;
    public String SMESSAGE;
    public List<WsEntity> LISTWSDATA;

    public WsData() {
        this.LISTWSDATA = new ArrayList<>();
    }

}
