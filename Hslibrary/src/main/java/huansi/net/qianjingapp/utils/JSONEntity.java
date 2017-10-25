package huansi.net.qianjingapp.utils;



import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

import huansi.net.qianjingapp.entity.WsData;
import huansi.net.qianjingapp.entity.WsEntity;


/**
 * Created by quanm on 2015-11-17.
 */
public class JSONEntity {
    private JSONEntity(){}
    public static WsData GetWsData(String sData, String className) {
        WsData wsData = new WsData();
        Class cls = null;
        Class clsSuper = null;
        try {
            cls = Class.forName(className);//对应Spring ->bean -->class
            clsSuper = cls.getSuperclass();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        //----------------------------利用反射开始---------------------------------------------------
        Map<String,String> map = new TreeMap<>();  //存放所有的属性值；
        Field[] fields =  cls.getDeclaredFields();
        for(Field field:fields) {
            if(!field.getName().equals("$change")) {
                map.put(field.getName(), "0");
            }
        }
        Field[] fieldsSupper = clsSuper.getDeclaredFields();
        for(Field field:fieldsSupper) {
            String sSuperPropertyName =field.getName();
            if(!(sSuperPropertyName.equalsIgnoreCase("SSTATUS")
                    ||sSuperPropertyName.equalsIgnoreCase("SMESSAGE")
                    ||sSuperPropertyName.equalsIgnoreCase("LISTWSDATA")))
                if(!sSuperPropertyName.equals("$change")) {
                    map.put(field.getName(), "1");
                }
        }
        Object obj=null;
        //----------------------------利用反射结束---------------------------------------------------
        try {
            JSONObject objData =new  JSONObject(sData);
            String sStatus = objData.getString("STATUS").toString();
            wsData.SSTATUS = sStatus;
            wsData.SMESSAGE = "";
            sData = objData.getString("DATA");

            JSONArray arr = new JSONArray(sData);
            if(sStatus.equalsIgnoreCase("0"))
            {
                //----------------------------利用反射开始---------------------------------------------------
                Field fd=null;
                WsEntity entity;
                try {
                    for(int i=0;i<arr.length();i++) {
                        obj = cls.newInstance();
                        JSONObject line = arr.getJSONObject(i);
                        for (Map.Entry<String, String> entry : map.entrySet()) {
                            String sKey = entry.getKey();
                            String sValue = entry.getValue().trim();
                            if(sValue.equalsIgnoreCase("0")) {
                                fd = cls.getDeclaredField(sKey);
                            } else {
                                fd = cls.getSuperclass().getDeclaredField(sKey);
                            }
                            fd.setAccessible(true);

                            if(line.has(sKey)) {
                                String objValue = line.getString(sKey).trim();
                                fd.set(obj, objValue);
                            }
                        }
                        entity = (WsEntity)obj;
                        wsData.LISTWSDATA.add(entity);
                    }
                }
                catch (Exception ex) {
                    ex.printStackTrace();
                }
                //----------------------------利用反射结束---------------------------------------------------
            } else {
                JSONObject message = new JSONObject(arr.getJSONObject(0).toString());
                String sMessage = message.getString("MESSAGE");
                wsData.SMESSAGE = sMessage;
            }
        } catch (JSONException e) {
            e.printStackTrace();
            String sMessage1 = e.getMessage().toString();
            wsData.SMESSAGE = sMessage1;
        }
        return wsData;
    }

}

