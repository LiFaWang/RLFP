package huansi.net.qianjingapp.utils;

import android.content.Context;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;

import java.util.Map;


import huansi.net.qianjingapp.constant.Constant;

import static huansi.net.qianjingapp.utils.SPHelper.RLFP_IP;


public class WebServices {
	/**
	 * 服务器的类型
	 */
	public enum WebServiceType{
		RLFP_SERVICE,//人力分派
		HS_SERVICE_LATER,//环思服务器
	}

	private String FEndPoint = "";
	private static final String NAME_SPACE ="http://tempuri.org/";
	private String FCheckCode = "";

	/**
	 *
	 * @param serviceTpe
     */
	public WebServices(WebServiceType serviceTpe, Context context) {
		switch (serviceTpe){
			//环思服务器
			case RLFP_SERVICE:
				FEndPoint  ="http://"+SPHelper.getLocalData(context, RLFP_IP,String.class.getName(),"").toString()+ Constant.RLFPWebservice.SERVICE_IP_SUFFIX;
				FCheckCode = Constant.RLFPWebservice.CHECK_CODE;
				break;
			case HS_SERVICE_LATER:
				FEndPoint = Constant.HSWebservice.SERVICE_IP;
				FCheckCode = Constant.HSWebservice.CHECK_CODE;
				break;
		}
	}


	public String getData(String functionName, Map<String,String> parameter) {
	    // 命名空间xx
	    String nameSpace = NAME_SPACE;
	    // 调用的方法名称  
	    String methodName = functionName;
	    // EndPoint  
	    String endPoint = FEndPoint;
	    // SOAP Action  
	    String soapAction = nameSpace + methodName;
	 
	    // 指定WebService的命名空间和调用的方法名  
	    SoapObject rpc = new SoapObject(nameSpace, methodName);  
	 
	    // 设置需调用WebService接口需要传入的两个参数mobileCode、userId  
//	    rpc.addProperty("sCheckCode", FCheckCode);
//	    rpc.addProperty("sUserNo", sUserNo);
//	    rpc.addProperty("sPassword", sPassword);
		String sParmName="";
		String sParaValue="";
		rpc.addProperty("sCheckCode", FCheckCode);
		if(parameter!=null) {
			for (Map.Entry<String, String> entry : parameter.entrySet()) {
				sParmName = entry.getKey();
				sParaValue = entry.getValue();
				rpc.addProperty(sParmName, sParaValue);
			}
		}
		// 生成调用WebService方法的SOAP请求信息,并指定SOAP的版本
	    SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER10);
	    envelope.bodyOut = rpc;
	    // 设置是否调用的是dotNet开发的WebService  
	    envelope.dotNet = true;
	    // 等价于envelope.bodyOut = rpc;
	    envelope.setOutputSoapObject(rpc);
	    HttpTransportSE transport = new HttpTransportSE(endPoint,1000*60);
	    try {
	        // 调用WebService  
	        transport.call(soapAction, envelope);
			// 获取返回的数据
			SoapObject object = (SoapObject) envelope.bodyIn;
//			SoapObject object=envelope.sObject.getProperty("return");
//			SoapObject soReturn = (SoapObject)object.getProperty("return");
//			soReturn.getProperty("insurer").toString();
			String result = "";
			// 获取返回的结果
			if(object!=null) {
				result = object.getProperty(0).toString();
			}
			return (result==null||result.isEmpty()||result.equals("anyType{}"))?"":result;
	    } catch (Exception e) {
	        e.printStackTrace();
			return "";
	    }
	}
}
