package huansi.net.qianjingapp.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Administrator on 2016/4/27 0027.
 */
public class TimeUtils {

    public static final int START = 1;//开始
    public final static int STOP = 2;//结束


    public static final int TODAY_INDEX=3;//今天
    public static final int TOMORROW_INDEX=4;//明天
    public static final int DAY_AFTER_TOMORROW_INDEX=5;//后天
    public static final int OTHER_INDEX=6;//其他日期
    public static final int YESTERDAY_INDEX=7;//昨天

    public static final String TIME_FORMATE_MINUS="yyyy-MM-dd HH:mm:ss";
    public static final String TIME_FORMATE_SLASH="yyyy/MM/dd HH:mm:ss";
    public static final String TIME_FORMATE_MINUS2="yyyy-M-dd";
    public static final String TIME_FORMATE_MINUS3="mm";
//    private TimeUtil(){}
//    private static TimeUtil timeUtil;
//    public static TimeUtil getInstance(){
//        if(timeUtil==null){
//            timeUtil=new TimeUtil();
//        }
//        return timeUtil;
//    }

    /**
     * 获得本周开始结束时间
     */
    public static String getTimeThisWeek(int index) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 0) day = 7;
        calendar.setTime(new Date());
        //今天是星期天
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -day + 2 - 7);
        } else {
            calendar.add(Calendar.DATE, -day + 2);
        }

        switch (index) {
            case START:
                //周一
                return format.format(calendar.getTime());
            case STOP:
                //周日
                return format.format(new Date());
            default:
                return null;
        }
    }

    /**
     * 获得上一周开始结束时间
     */
    public static String getTimeLaskWeek(int index) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 0) day = 7;
        //今天是星期天
        calendar.setTime(new Date());
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -day + 2 - 7 - 7);
        } else {
            calendar.add(Calendar.DATE, -day + 2 - 7);
        }
        switch (index) {
            case START:
                //周一
                return format.format(calendar.getTime());
            case STOP:
                //周日
                return format.format(format.parse(format.format(calendar.getTime())).getTime() + 6 * 24 * 3600 * 1000);
            default:
                return null;

        }
    }

    /**
     * 获得下一周开始结束时间
     */
    public static String getTimeNextWeek(int index) throws ParseException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        if (day == 0) day = 7;
        //今天是星期天
        calendar.setTime(new Date());
        if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -day + 2 - 7 +7);
        } else {
            calendar.add(Calendar.DATE, -day + 2 + 7);
        }
        switch (index) {
            case START:
                //周一
                return format.format(calendar.getTime());
            case STOP:
                //周日
                return format.format(format.parse(format.format(calendar.getTime())).getTime() + 6 * 24 * 3600 * 1000);
            default:
                return null;

        }
    }

    /**
     * 获得本月开始结束时间
     */
    public static String getTimeThisMonth(int index) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        switch (index) {
            case START:
                //一号
                return format.format(calendar.getTime());
            case STOP:
                //今天
                return format.format(new Date());
            default:
                return null;

        }
    }

    /**
     * 获得本月月初和月末
     */
    public static String getTimeThisMonthAll(int index) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, 1);
        switch (index) {
            case START:
                //一号
                return format.format(calendar.getTime());
            case STOP:
                //月末
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                return format.format(calendar.getTime());
            default:
                return null;

        }
    }

    /**
     * 获得上月开始结束时间
     */
    public static String getTimeLastMonth(int index) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -1);
        calendar.set(Calendar.DATE, 1);
        switch (index) {
            case START:
                //一号
                return format.format(calendar.getTime());
            case STOP:
                //月末
                calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
                return format.format(calendar.getTime());
            default:
                return null;

        }
    }
//
//    /**
//     * 四个时间全部显示灰色(上周、本周、上月、本月)
//     */
//    public static void showProbTimeNormal(Resources resources, Button... btns) {
//        for (int i = 0; i < btns.length; i++) {
//            btns[i].setBackground(resources.getDrawable(R.drawable.shape_rd_normal));
//            btns[i].setTextColor(resources.getColor(R.color.blue));
//        }
////        btnAttenRecordThisWeek.setBackground(getResources().getDrawable(R.drawable.shape_rd_normal));
////        btnAttenRecordLastWeek.setBackground(getResources().getDrawable(R.drawable.shape_rd_normal));
////        btnAttenRecordThisMonth.setBackground(getResources().getDrawable(R.drawable.shape_rd_normal));
////        btnAttenRecordLastMonth.setBackground(getResources().getDrawable(R.drawable.shape_rd_normal));
////
////        btnAttenRecordThisWeek.setTextColor(getResources().getColor(R.color.blue));
////        btnAttenRecordLastWeek.setTextColor(getResources().getColor(R.color.blue));
////        btnAttenRecordThisMonth.setTextColor(getResources().getColor(R.color.blue));
////        btnAttenRecordLastMonth.setTextColor(getResources().getColor(R.color.blue));
//    }

//    /**
//     * 显示日历的popupwindow
//     */
//    public static void showPopCal(PopupWindow popupWindow, DatePicker calendarView, Context context, final Button btn, final String time) {
////        View view= LayoutInflater.from(context).inflate(R.layout.calendar_pop,null);
////        popupWindow=new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
////        calendarView= (DatePicker) view.findViewById(R.id.calendarView);
//        if (time.isEmpty())
//            calendarView.setDate(Integer.valueOf(getCurDate("/").split(" ")[0].split("/")[0]),
//                    Integer.valueOf(getCurDate("/").split(" ")[0].split("/")[1]));
//        else
//            calendarView.setDate(Integer.valueOf(time.split("-")[0]), Integer.valueOf(time.split("-")[1]));
////        switch (clickViewId) {
////            case R.id.btnAttenRecordStartTime:
////                if(startTime.isEmpty())
////                    calendarView.setDate(Integer.valueOf(getCurDate("/").split(" ")[0].split("/")[0]),
////                            Integer.valueOf(getCurDate("/").split(" ")[0].split("/")[1]));
////                else
////                    calendarView.setDate(Integer.valueOf(startTime.split("-")[0]), Integer.valueOf(startTime.split("-")[1]));
////                break;
////            case R.id.btnAttenRecordStopTime:
////                if(stopTime.isEmpty())
////                    calendarView.setDate(Integer.valueOf(getCurDate("/").split(" ")[0].split("/")[0]),
////                            Integer.valueOf(getCurDate("/").split(" ")[0].split("/")[1]));
////                else
////                    calendarView.setDate(Integer.valueOf(stopTime.split("-")[0]), Integer.valueOf(stopTime.split("-")[1]));
////                break;
////        }
//        calendarView.setMode(DPMode.SINGLE);
////        calendarView.setOnDatePickedListener(new DatePicker.OnDatePickedListener() {
////            @Override
////            public void onDatePicked(String date) {
////               time=date;
////                btn.setText(date);
//////                switch (clickViewId){
//////                    case R.id.btnAttenRecordStartTime:
//////                        startTime=date;
//////                        btnAttenRecordShowStartTime.setText(startTime);
//////                        break;
//////                    case R.id.btnAttenRecordStopTime:
//////                        stopTime=date;
//////                        btnAttenRecordShowStopTime.setText(stopTime);
//////                        break;
//////
//////                }
////                if(popupWindow!=null){
////                    popupWindow.dismiss();
////                }
////            }
////        });
//        PopupUtil.getInstance().showPop(popupWindow, (Activity) context);
//        popupWindow.showAtLocation(btn, Gravity.CENTER, 0, 0);
//    }


    //得到当前操作时间
    public static String getCurDate(String timeType) {
        Date now = new Date();
        SimpleDateFormat dateFormat = null;
        if (timeType.equals("-")) {
            dateFormat = new SimpleDateFormat(TIME_FORMATE_MINUS);
        }else if(timeType.equals("--")){
            dateFormat = new SimpleDateFormat(TIME_FORMATE_MINUS2);
        } else if(timeType.equals("mm")) {
            dateFormat = new SimpleDateFormat(TIME_FORMATE_MINUS3);
        } else
        {
            dateFormat = new SimpleDateFormat(TIME_FORMATE_SLASH);
        }
        String sCurDate = dateFormat.format(now);
        return sCurDate;
    }

    /**
     * 得到当前操作时间(精确到毫秒)
     */
    public static String getCurMillDate() {
        Date now = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:sss");
        String sCurDate = dateFormat.format(now);
        return sCurDate;
    }

    /**
     * 获得指定时间的long类型的毫秒数
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static long getTime(String time, String timeType) throws ParseException {
        SimpleDateFormat dateFormat = null;
        if (timeType.equals("-")) {
            dateFormat = new SimpleDateFormat(TIME_FORMATE_MINUS);
        } else {
            dateFormat = new SimpleDateFormat(TIME_FORMATE_SLASH);
        }
        return dateFormat.parse(time).getTime();
    }

    /**
     * 获得Date相对应的字符串的时间
     *
     * @param date
     * @return
     */
    public static String getTime(Date date, String timeType) {
        SimpleDateFormat dateFormat = null;
        if (timeType.equals("-")) {
            dateFormat = new SimpleDateFormat(TIME_FORMATE_MINUS);
        } else {
            dateFormat = new SimpleDateFormat(TIME_FORMATE_SLASH);
        }
        return dateFormat.format(date);
    }

    /**
     * 通过字符串的时间获得一个Date
     *
     * @param time
     * @return
     * @throws ParseException
     */
    public static Date getDate(String time, String timeType) throws ParseException {
        SimpleDateFormat dateFormat = null;
        if (timeType.equals("-")) {
            dateFormat = new SimpleDateFormat(TIME_FORMATE_MINUS);
        } else {
            dateFormat = new SimpleDateFormat(TIME_FORMATE_SLASH);
        }
        return dateFormat.parse(time);
    }

    /**
     * 通过long类型的时间获得所需的时间
     *
     * @return
     */
    public static String getDate(long time, String timeType) {
        Date now = new Date(time);
        SimpleDateFormat dateFormat = null;
        if (timeType.equals("-")) {
            dateFormat = new SimpleDateFormat(TIME_FORMATE_MINUS);
        } else {
            dateFormat = new SimpleDateFormat(TIME_FORMATE_SLASH);
        }
        return dateFormat.format(now);
    }

    /**
     * 获得当前时间的毫秒数
     *
     * @return
     */
    public static long getCurrentTime() {
        Date date = new Date(System.currentTimeMillis());
        return date.getTime();
    }

    /**
     * 判断是不是今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(Date date) {
        Date dToday = new Date();
        Calendar cToday = Calendar.getInstance();
        cToday.setTime(dToday);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return cToday.get(Calendar.YEAR) == calendar.get(Calendar.YEAR) &&
                cToday.get(Calendar.MONTH) == calendar.get(Calendar.MONTH) &&
                cToday.get(Calendar.DAY_OF_MONTH) == calendar.get(Calendar.DAY_OF_MONTH);
    }

    /**
     * 获取当前时间，精确到毫秒
     *
     * @return
     */
    public static long getCurrentMillTime() throws ParseException {
        Date date = new Date();
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss:sss");
        return format.parse(format.format(date)).getTime();
    }

//    /**
//     * 获得上一次刷新的时间
//     *
//     * @param context
//     * @return
//     */
//    public static String getLastUpdate(Context context) {
//        String sLastUpdateDate = context.getResources().getString(R.string.xlistview_header_last_time) + getCurDate("/");
//        return sLastUpdateDate;
//    }


//    /**
//     * 返回与当前时间的时间差
//     *
//     * @return
//     */
//    public static String getTimeDifference(String time, String timeType, Context context) throws ParseException {
//        String showTime = "";
//        SimpleDateFormat dateFormat = null;
//        if (timeType.equals("-")) {
//            dateFormat = new SimpleDateFormat(TIME_FORMATE_MINUS);
//        } else {
//            dateFormat = new SimpleDateFormat(TIME_FORMATE_SLASH);
//        }
//        //当前的时间（秒数）
//        long current = System.currentTimeMillis();
//        long t = dateFormat.parse(time).getTime();
//        long minus = (current - t) / 1000;
//        //1分钟以内
//        if (minus < 60) {
//            showTime = context.getResources().getString(R.string.time_just);
//            //1分钟~1小时
//        } else if (minus > 59 && minus < 3600) {
//            showTime = (minus / 60) + context.getResources().getString(R.string.time_minute_before);
//            //1小时~24小时
//        } else if (minus > 3599 && minus < 86400) {
//            showTime = (minus / 3600) + context.getResources().getString(R.string.time_hour_before);
//            //24小时~48小时
//        } else if (minus > 86399 && minus < 172800) {
//            showTime = context.getResources().getString(R.string.time_yesterday);
//            //48小时~1年
//        } else if (minus > 172799 && minus < 31536000) {
//            String[] arr = time.split(" ");
//            String[] arrTime = arr[0].split(timeType);
//            showTime = arrTime[1] + context.getResources().getString(R.string.time_month) +
//                    arrTime[2] + context.getResources().getString(R.string.time_day);
//        } else {
//            //>1年
//            String[] arr = time.split(" ");
//            String[] arrTime = arr[0].split(timeType);
//            showTime = arrTime[0] + context.getResources().getString(R.string.time_year)
//                    + arrTime[1] + context.getResources().getString(R.string.time_month) + arrTime[2]
//                    + context.getResources().getString(R.string.time_day);
//        }
//        return showTime;
//    }

    /**
     * 格式是MM-dd HH:mm
     *
     * @return
     */
    public static String getTimeNoYear() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA).format(new Date());
    }

    /**
     * 将秒数转换字符串（00：00）
     *
     * @return
     */
    public static String getDurByString(int seconds) {
        String dur = "";
        long min = seconds / 60;
        if (min < 10) {
            dur += "0" + min + ":";
        } else {
            dur += min + ":";
        }
        long s = seconds % 60;
        if (s < 10) {
            dur += "0" + s;
        } else {
            dur += s;
        }
        return dur;
    }

//    /**
//     * 将日期转为今天,明天和天数
//     *
//     * @return
//     */
//    public static String dateToTimeLeft(String time, Context context) throws ParseException {
//        Date date = null;
//        if(time.isEmpty()) return "";
//        SimpleDateFormat dateFormat = null;
//        if (time.contains("/")) {
//            dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
//        } else {
//            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        }
//        date=dateFormat.parse(time);
//        Calendar calDate = Calendar.getInstance();
//
//        Calendar today = Calendar.getInstance();    //今天
//
//        today.set(Calendar.YEAR, calDate.get(Calendar.YEAR));
//        today.set(Calendar.MONTH, calDate.get(Calendar.MONTH));
//        today.set(Calendar.DAY_OF_MONTH, calDate.get(Calendar.DAY_OF_MONTH));
//        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
//        today.set(Calendar.HOUR_OF_DAY, 0);
//        today.set(Calendar.MINUTE, 0);
//        today.set(Calendar.SECOND, 0);
//
//
//        Calendar tomorrow = Calendar.getInstance();//明天
//        tomorrow.set(Calendar.YEAR, calDate.get(Calendar.YEAR));
//        tomorrow.set(Calendar.MONTH, calDate.get(Calendar.MONTH));
//        tomorrow.set(Calendar.DAY_OF_MONTH, calDate.get(Calendar.DAY_OF_MONTH) + 1);
//        tomorrow.set(Calendar.HOUR_OF_DAY, 0);
//        tomorrow.set(Calendar.MINUTE, 0);
//        tomorrow.set(Calendar.SECOND, 0);
//
//
//        Calendar tomorrowPlus = Calendar.getInstance();//后天
//        tomorrowPlus.set(Calendar.YEAR, calDate.get(Calendar.YEAR));
//        tomorrowPlus.set(Calendar.MONTH, calDate.get(Calendar.MONTH));
//        tomorrowPlus.set(Calendar.DAY_OF_MONTH, calDate.get(Calendar.DAY_OF_MONTH) + 2);
//        tomorrowPlus.set(Calendar.HOUR_OF_DAY, 0);
//        tomorrowPlus.set(Calendar.MINUTE, 0);
//        tomorrowPlus.set(Calendar.SECOND, 0);
//
//        calDate.setTime(date);
//
////        long timeDate = calDate.getTimeInMillis();
////        long timeToday = today.getTimeInMillis();
////        long betweenDays = Math.abs(timeToday - timeDate) / (1000 * 3600 * 24);
//
//        //说明任务的到期时间是昨天或者之前
//        if (calDate.before(today)) {
//            return  context.getResources().getString(R.string.expire) +betweenDays(calDate,today)+
//                    context.getResources().getString(R.string.day);
////            return context.getResources().getString(R.string.expire) + Integer.parseInt(betweenDays + "") +
////                    context.getResources().getString(R.string.day);
//        }
//
//        //说明没到期
//        if (calDate.after(today) && calDate.before(tomorrow)) {
//            return context.getResources().getString(R.string.today);
//        } else if (calDate.before(tomorrowPlus) && calDate.after(tomorrow)) {
//            return context.getResources().getString(R.string.tomorrow);
//        } else {
//            return context.getResources().getString(R.string.besides)+betweenDays(today,calDate)+context.getResources().getString(R.string.day);
//        }
//
//    }


    private static int betweenDays(Calendar beginDate, Calendar endDate) {
        if (beginDate.get(Calendar.YEAR) == endDate.get(Calendar.YEAR)) {
            int i1=endDate.get(Calendar.DAY_OF_YEAR);
            int i2=beginDate.get(Calendar.DAY_OF_YEAR);
            return i1 - i2;
        } else {
            if (beginDate.getTimeInMillis() < endDate.getTimeInMillis()) {
                int days = beginDate.getActualMaximum(Calendar.DAY_OF_YEAR)
                        - beginDate.get(Calendar.DAY_OF_YEAR)
                        + endDate.get(Calendar.DAY_OF_YEAR);
                for (int i = beginDate.get(Calendar.YEAR) + 1; i < endDate
                        .get(Calendar.YEAR); i++) {
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, i);
                    days += c.getActualMaximum(Calendar.DAY_OF_YEAR);
                }
                return days;
            } else {
                int days = endDate.getActualMaximum(Calendar.DAY_OF_YEAR)
                        - endDate.get(Calendar.DAY_OF_YEAR)
                        + beginDate.get(Calendar.DAY_OF_YEAR);
                for (int i = endDate.get(Calendar.YEAR) + 1; i < beginDate
                        .get(Calendar.YEAR); i++) {
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, i);
                    days += c.getActualMaximum(Calendar.DAY_OF_YEAR);
                }
                return days;
            }
        }
    }

    /**
     * 是不是今年的
     * @param time
     * @return
     */
    public static String isThisYear(String time) throws ParseException {
        Calendar currentCalendar= Calendar.getInstance();
        currentCalendar.setTime(new Date());

        Calendar calendar= Calendar.getInstance();
        Date date= getDate(time,time.contains("/")?"/":"-");
        calendar.setTime(date);
        if(calendar.get(Calendar.YEAR)==currentCalendar.get(Calendar.YEAR)){
            return time.split(" ")[0].subSequence(5,time.split(" ")[0].length()).toString();
        }else {
            return time.split(" ")[0];
        }
    }


    /**
     * 不含年份和秒
     * @return
     */
    public static String getTimeNoSecAndYear(String time){
        String[] strsObject=time.split(" ");
        String[] dateStrs=strsObject[0].contains("/")?strsObject[0].split("/"):strsObject[0].split("-");
        String[] timeStrs=null;
        if(strsObject.length>1) {
            timeStrs = strsObject[1].split(":");
        }
        //说明本身就不含年和秒
        if(dateStrs[0].length()<3&&
                (timeStrs==null||timeStrs.length==2)) return time;
        StringBuilder sbTime=new StringBuilder();
        for(int i=0;i<dateStrs.length;i++){
            if(i==0) continue;
            sbTime.append(dateStrs[i]+"-");
        }
        if(!sbTime.toString().isEmpty()){
            sbTime=sbTime.delete(sbTime.length()-1,sbTime.length());
            sbTime.append(" ");
        }
        if(timeStrs!=null){
            for(int i=0;i<timeStrs.length;i++){
                if(i!=timeStrs.length-1){
                    sbTime.append(timeStrs[i]+":");
                }
            }
        }
        if(sbTime.toString().contains(":")) sbTime=sbTime.delete(sbTime.length()-1,sbTime.length());
        return sbTime.toString();
    }

    /**
     * 通过日期得到今天明天还是后天
     * @return
     */
    public static int getStrByDate(String time) throws ParseException {
        Date date = null;
        if (time.isEmpty()) return OTHER_INDEX;
        SimpleDateFormat dateFormat;
        if (time.contains("/")) {
            dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        } else {
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
        date = dateFormat.parse(time);
        Calendar calDate = Calendar.getInstance();


        Calendar yesterday = Calendar.getInstance();    //今天

        yesterday.set(Calendar.YEAR, calDate.get(Calendar.YEAR));
        yesterday.set(Calendar.MONTH, calDate.get(Calendar.MONTH));
        yesterday.set(Calendar.DAY_OF_MONTH, calDate.get(Calendar.DAY_OF_MONTH)-2);
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        yesterday.set(Calendar.HOUR_OF_DAY, 23);
        yesterday.set(Calendar.MINUTE, 59);
        yesterday.set(Calendar.SECOND, 59);

        Calendar today = Calendar.getInstance();    //今天

        today.set(Calendar.YEAR, calDate.get(Calendar.YEAR));
        today.set(Calendar.MONTH, calDate.get(Calendar.MONTH));
        today.set(Calendar.DAY_OF_MONTH, calDate.get(Calendar.DAY_OF_MONTH)-1);
        //  Calendar.HOUR——12小时制的小时数 Calendar.HOUR_OF_DAY——24小时制的小时数
        today.set(Calendar.HOUR_OF_DAY, 23);
        today.set(Calendar.MINUTE, 59);
        today.set(Calendar.SECOND, 59);


        Calendar tomorrow = Calendar.getInstance();//明天
        tomorrow.set(Calendar.YEAR, calDate.get(Calendar.YEAR));
        tomorrow.set(Calendar.MONTH, calDate.get(Calendar.MONTH));
        tomorrow.set(Calendar.DAY_OF_MONTH, calDate.get(Calendar.DAY_OF_MONTH));
        tomorrow.set(Calendar.HOUR_OF_DAY, 23);
        tomorrow.set(Calendar.MINUTE, 59);
        tomorrow.set(Calendar.SECOND, 59);


        Calendar tomorrowPlus = Calendar.getInstance();//后天
        tomorrowPlus.set(Calendar.YEAR, calDate.get(Calendar.YEAR));
        tomorrowPlus.set(Calendar.MONTH, calDate.get(Calendar.MONTH));
        tomorrowPlus.set(Calendar.DAY_OF_MONTH, calDate.get(Calendar.DAY_OF_MONTH) + 1);
        tomorrowPlus.set(Calendar.HOUR_OF_DAY, 23);
        tomorrowPlus.set(Calendar.MINUTE, 59);
        tomorrowPlus.set(Calendar.SECOND, 59);


        Calendar tomorrowDoublePlus = Calendar.getInstance();//大后天
        tomorrowDoublePlus.set(Calendar.YEAR, calDate.get(Calendar.YEAR));
        tomorrowDoublePlus.set(Calendar.MONTH, calDate.get(Calendar.MONTH));
        tomorrowDoublePlus.set(Calendar.DAY_OF_MONTH, calDate.get(Calendar.DAY_OF_MONTH) + 2);
        tomorrowDoublePlus.set(Calendar.HOUR_OF_DAY, 23);
        tomorrowDoublePlus.set(Calendar.MINUTE, 59);
        tomorrowDoublePlus.set(Calendar.SECOND, 59);
        calDate.setTime(date);


//        long timeDate = calDate.getTimeInMillis();
//        long timeToday = today.getTimeInMillis();
//        long betweenDays = Math.abs(timeToday - timeDate) / (1000 * 3600 * 24);
//
//        //说明任务的到期时间是昨天或者之前
//        if (calDate.before(today)) {
//            return  context.getResources().getString(R.string.expire) +betweenDays(calDate,today)+
//                    context.getResources().getString(R.string.day);
////            return context.getResources().getString(R.string.expire) + Integer.parseInt(betweenDays + "") +
////                    context.getResources().getString(R.string.day);
//        }

        //说明没到期
        if(calDate.after(yesterday)&&calDate.before(today)){
            return YESTERDAY_INDEX;
        } else if (calDate.after(today) && calDate.before(tomorrow)) {
            return TODAY_INDEX;
        } else if (calDate.before(tomorrowPlus) && calDate.after(tomorrow)) {
            return TOMORROW_INDEX;
        } else if (calDate.before(tomorrowDoublePlus) && calDate.after(tomorrowPlus)) {
            return DAY_AFTER_TOMORROW_INDEX;
        } else {
            return OTHER_INDEX;
        }
    }


}
