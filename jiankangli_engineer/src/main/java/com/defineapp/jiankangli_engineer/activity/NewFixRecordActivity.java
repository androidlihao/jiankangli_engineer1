package com.defineapp.jiankangli_engineer.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.defineapp.jiankangli_engineer.R;
import com.defineapp.jiankangli_engineer.adapter.MyAdapter;
import com.defineapp.jiankangli_engineer.adapter.fixRecordListAdapter;
import com.defineapp.jiankangli_engineer.bean.RecrodItem;
import com.defineapp.jiankangli_engineer.bean.firstfixRecordBackInfo;
import com.defineapp.jiankangli_engineer.bean.fixRecordBackInfo;
import com.defineapp.jiankangli_engineer.bean.fixRecordInfo;
import com.defineapp.jiankangli_engineer.listener.getOnClickPostion;
import com.defineapp.jiankangli_engineer.view.DatePickerTimePopWindow;
import com.defineapp.jiankangli_engineer.view.ExpandableHeightListView;
import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import static android.content.ContentValues.TAG;

/**
 * Created by 李浩 on 2017/5/10.
 */
@SuppressWarnings("deprecation")
public class NewFixRecordActivity extends Activity  implements getOnClickPostion {
    /**
     * 返回控件
     */
    private ImageView iv_back;

    /**
     * 展示记录listview
     */
    private ExpandableHeightListView ed_listView;

    /**
     * 上报时间
     */


    /**
     * 维修人名字
     */
    private TextView fix_person;

    /**
     * 申请的控件
     */
    private TextView want_parts;

    /**
     * 工单号
     */
    private TextView order_no;

    /**
     * 书写记录的控件
     */
    private EditText et_remark;

    /**
     * 保存控件
     */
    private View tv_save;

    /**
     * 本地文件
     */
    private SharedPreferences sp;

    /**
     * 存放订单详情集合
     */
    private List<RecrodItem> list;

    /**
     * 订单号
     */
    private String orderNo;

    /**
     * 设备号
     */
    private String deviceNo;
    private String engineerName;
    private String accessoryName;

    /**
     * loading动画控件
     */
    private RelativeLayout rl_wait;
    private ImageView progressBar1;
    private AnimationDrawable animationDrawable;

    private LinearLayout ll_order;

    /**
     * 获取维修记录地址
     */
    private final String record_url = Globle.NET_URL + "engineer/getRepairLogsList.do";

    /**
     * 保存维修记录地址
     */
    private String save_change_url = Globle.NET_URL + "engineer/sendRepairNote.do";
    /*
    * 回显工单详情地址
    * */
    private String back_show_url=Globle.NET_URL+"engineer/getRepairLogsByWorderId.do";
    /**
     * 存放订单详情集合
     */
    private List<RecrodItem> mList;

    /**
     * 订单状态
     */
    private String state;

    private String workOrderId;

    private String engineerId;

    private String reportTime;

    private String orderStatus;
    private TextView tv_remark;
    private boolean falg;

    private String arr_start[];
    private String arr_end[];
    private TextView fault_kind;
    private RadioGroup radioGroup_id;
    private TextView tv4;
    private TextView tv_fix_time;
    private TextView tv_device_state;
    private TextView tv5;

    /**
     * 获得一个自定义对话框
     *
     * @return
     */
    private View view_pop1;

    private ScrollView sc;


    private DatePickerTimePopWindow popWindow;
    private TextView keep_time;
    private ExpandableHeightListView lv_fix_time_id;
    private RadioButton rb_fault_id;
    private RadioButton rb_part_fault_id;
    private RadioButton rb_normal_id;
    private EditText et_backtime_id;
    private EditText et_gotime_id;
    private List<fixRecordBackInfo.DataBean.TimeListBean> timeList;
    private MyAdapter myAdapter;
    private View tv_add_item_id;
    private int faultType;
    private int repairLogsId;
    private List<String> starttimeList;
    private List<String> endtimeList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_fix_record_activity);
        //初始化界面控件实例
        initView();
        //判断当前工单的状态
        initStatus();
        setOnClickListeners();
        //获取回显示数据
        getBackShowInfo();
        //对于listview执行删减增加
        //获取维修记录
        getRecordData();

    }

    private void initStatus() {

        if (Integer.parseInt(orderStatus) >= 4){//维修完成的话
             tv_save.setVisibility(View.GONE);
             ll_order.setVisibility(View.GONE);
        }else{
            tv_save.setVisibility(View.VISIBLE);
            ll_order.setVisibility(View.VISIBLE);
        }
    }

    private void getRecordData() {
        try {
            JSONObject jsonObject=new JSONObject();
            jsonObject.put("userId", sp.getString("userId", -1 + ""));
            jsonObject.put("orderId", workOrderId);
            jsonObject.put("deviceNo", deviceNo);
            Log.i(TAG, "getRecordData: "+jsonObject);
            String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
            getData(jsonString,record_url,2);//请求的接口为维修记录接口
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void setOnClickListeners() {
        tv_add_item_id.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        tv_save.setOnClickListener(this);
    }

    private void getBackShowInfo() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("userId", sp.getString("userId", -1 + ""));
            jsonObject.put("workOrderId", workOrderId);
            Log.i(TAG, "getBackShowInfo: "+jsonObject);
            String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
            getData(jsonString,back_show_url,1);
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private void initView() {
        //title view
        tv_save = (TextView) findViewById(R.id.tv_save);//保存按钮
        iv_back = (ImageView) findViewById(R.id.iv_back);//返回键
        keep_time = (TextView) findViewById(R.id.keep_time);//报修时间控件
        fix_person = (TextView) findViewById(R.id.fix_person);//维修工程师
        want_parts = (TextView) findViewById(R.id.want_parts);//申请配件
        fault_kind = (TextView) findViewById(R.id.fault_kind);//故障种类
        tv_fix_time = (TextView) findViewById(R.id.tv_fix_time);//维修时间
        //往返时间
        et_backtime_id = (EditText) findViewById(R.id.et_backtime_id);
        et_gotime_id = (EditText) findViewById(R.id.et_gotime_id);
        setPricePoint(et_backtime_id);
        setPricePoint(et_gotime_id);

        //设备状态
        tv_device_state = (TextView) findViewById(R.id.tv_device_state);
        radioGroup_id = (RadioGroup) findViewById(R.id.radioGroup_id);
        rb_fault_id = (RadioButton) findViewById(R.id.rb_fault_id);
        rb_part_fault_id = (RadioButton) findViewById(R.id.rb_part_fault_id);
        rb_normal_id = (RadioButton) findViewById(R.id.rb_normal_id);
        //维修时间控件
         lv_fix_time_id= (ExpandableHeightListView) findViewById(R.id.lv_fix_time_id);
        //维修记录描述

        et_remark = (EditText) findViewById(R.id.et_remark);

        tv_remark = (TextView) findViewById(R.id.tv_remark);
        //发现添加按钮
        tv_add_item_id = findViewById(R.id.tv_add_item_id);
        tv4 = (TextView) findViewById(R.id.tv4);
        tv5 = (TextView) findViewById(R.id.tv5);
        //加载动画布局
        rl_wait = (RelativeLayout) findViewById(R.id.rl_wait);
        progressBar1 = (ImageView) findViewById(R.id.progressBar1);
        progressBar1.setImageResource(R.drawable.loading);
        animationDrawable = (AnimationDrawable) progressBar1.getDrawable();
        //正在记录的工单号布局
        ll_order = (LinearLayout) findViewById(R.id.ll_order);
        order_no = (TextView) findViewById(R.id.order_no);
        //所有工单记录
        ed_listView = (ExpandableHeightListView) findViewById(R.id.ed_list_view);
        //正在滑动布局m
        sc = (ScrollView) findViewById(R.id.sc);
        sc.smoothScrollTo(0, 20);
        //获取sp中的存储数据
        sp = getSharedPreferences("config", 0);
        //初始化数据源
        timeList=new LinkedList<>();
        fixRecordBackInfo.DataBean.TimeListBean timeListBean=new fixRecordBackInfo.DataBean.TimeListBean();
        timeListBean.setEndTime("请选择结束时间");timeListBean.setStartTime("请选择开始时间");
        timeList.add(timeListBean);
        myAdapter = new MyAdapter(timeList,R.layout.fix_time_item,this);
        lv_fix_time_id.setAdapter(myAdapter);//绑定适配器
        //获取上一个界面传过来的数据
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        //设置从上个界面传过来的数据
        deviceNo = (String) extras.get("deviceNo");
        state = (String) extras.get("state");
        orderStatus = String.valueOf(extras.get("orderStatus"));//工单状态
        Log.i(TAG, "orderStatus: "+orderStatus);
        orderNo = (String) extras.get("orderNo");
        workOrderId = String.valueOf(extras.get("workOrderId"));
        //faultType = (int) extras.get("faultType");
        engineerId = (String) extras.get("engineerId");
        engineerName = (String) extras.get("engineerName");
        accessoryName = (String) extras.get("accessoryName");
        reportTime = (String) extras.get("reportTime");
        order_no.setText(orderNo);
        keep_time.setText(reportTime);
        fix_person.setText(engineerName);
        want_parts.setText(accessoryName);
    }
    private void getData(String jsonString, String url, final int i) {

        rl_wait.setVisibility(View.VISIBLE);//获取网路数据，提示用户正在加载中
        animationDrawable.start();//动画开始加载
        RequestParams params = new RequestParams();
        params.add("jsonString", jsonString);
        params.setContentEncoding("UTF-8");

        AsyncHttpClient asyncHttpClient = new AsyncHttpClient();
        asyncHttpClient.post(url, params, new TextHttpResponseHandler() {

            @Override
            public void onFailure(int arg0, Header[] arg1, String arg2, Throwable arg3) {
                rl_wait.setVisibility(View.INVISIBLE);
                animationDrawable.stop();
                Toast.makeText(getApplicationContext(), "网络或服务器异常", Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onSuccess(int arg0, Header[] arg1, String arg2) {
                rl_wait.setVisibility(View.INVISIBLE);
                animationDrawable.stop();
                try {
                    JSONObject json = new JSONObject(arg2);
                    String code = (String) json.get("code");
                    String msg = (String) json.get("msg");
                    Log.i(TAG, "content: "+i);
                    Log.i("parts_record", "content:" + arg2);
                    Log.i("parts_record", "code:" + code);
                    Log.i("parts_record", "msg:" + msg);
                    switch (code){
                        case "success":
                            switch (i){
                                case 3:
                                    Toast.makeText(getApplicationContext(), "保存成功", Toast.LENGTH_SHORT).show();
                                    setResult(2);
                                    finish();
                                    break;
                                case 2://维修记录
                                    Gson gsons=new Gson();
                                    fixRecordInfo fixRecordInfo=gsons.fromJson(arg2,fixRecordInfo.class);
                                    List<fixRecordInfo.DataBean> fixList=fixRecordInfo.getData();
                                    fixRecordListAdapter adapter=new fixRecordListAdapter(fixList,R.layout.fix_record_item);
                                    ed_listView.setAdapter(adapter);
                                    break;
                                case 1://如果请求的是回显接口，那么开始解析数据
                                    try {
                                        Gson gson = new Gson();
                                        Log.i(TAG, "onSuccess: "+arg2);
                                        fixRecordBackInfo frInfo=gson.fromJson(arg2,fixRecordBackInfo.class);
                                        int equipmentStatus=frInfo.getData().getEquipmentStatus();
                                        repairLogsId = frInfo.getData().getRepairLogsId();
                                        notifyDataSetChangeds(frInfo.getData().getTimeList());
                                        faultType=frInfo.getData().getFaultType();
                                        switch (faultType){
                                            case 1:
                                                fault_kind.setText("软件");
                                                break;
                                            case 2:
                                                fault_kind.setText("硬件");
                                                break;
                                            case 3:
                                                fault_kind.setText("软件 硬件");
                                                break;
                                            default:
                                                break;
                                        }
                                        et_gotime_id.setText(frInfo.getData().getTravelToTime()+"");
                                        et_backtime_id.setText(frInfo.getData().getTravelBackTime()+"");
                                        et_remark.setText(frInfo.getData().getContent());
                                        switch (equipmentStatus){
                                            case 1:
                                                radioGroup_id.check(rb_fault_id.getId());
                                                break;
                                            case 2:
                                                radioGroup_id.check(rb_part_fault_id.getId());
                                                break;
                                            case 3:
                                                radioGroup_id.check(rb_normal_id.getId());
                                                break;
                                            default:
                                                break;
                                        }
                                    }catch (Exception e){
                                        Log.i(TAG, "Exception: "+e);
                                    }
                                break;
                            }

                            break;
                        case "error":
                            Toast.makeText(NewFixRecordActivity.this, msg, Toast.LENGTH_SHORT).show();
                            break;
                        case "110":
                            Log.i(TAG, "i="+i+arg2);
                            try {
                                switch (i){
                                    case 1:
                                        Gson gson = new Gson();
                                        firstfixRecordBackInfo frInfo=gson.fromJson(arg2,firstfixRecordBackInfo.class);
                                        faultType=frInfo.getData().getFaultType();
                                        et_gotime_id.setText("");
                                        et_backtime_id.setText("");
                                        switch (faultType){
                                            case 1:
                                                fault_kind.setText("软件");
                                                break;
                                            case 2:
                                                fault_kind.setText("硬件");
                                                break;
                                            case 3:
                                                fault_kind.setText("软件 硬件");
                                                break;
                                            default:
                                                break;
                                        }
                                        break;
                                }
                            }catch (Exception e){
                                e.printStackTrace();
                            }

                            break;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_add_item_id:
                //给数据源添加一条数据
                additem(0);
                break;
            case R.id.iv_back:

                finish();
                break;
            case R.id.tv_save:
                //开始执行保存操作
                saveOrchange();
                break;
            default:
                Log.i(TAG, "nothing: ");
                break;
        }
    }

    private void saveOrchange() {
        //判断时间
        if (TextUtils.isEmpty(et_remark.getText().toString())) {
            Toast.makeText(getApplicationContext(), "请输入内容", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.i(TAG, "saveOrchange: "+timeList.size()+timeList.get(0).getStartTime());
        //判断数据源中每条数据
        if (starttimeList!=null){
            starttimeList.clear();
            starttimeList=null;
        }
        if (endtimeList!=null){
            endtimeList.clear();
            endtimeList=null;
        }
        starttimeList = new LinkedList<>();
        endtimeList = new LinkedList<>();
        for (fixRecordBackInfo.DataBean.TimeListBean timebean:timeList){
             if (timebean.getStartTime().trim().equals("请选择开始时间")){
                 Toast.makeText(this, "请选择开始时间", Toast.LENGTH_SHORT).show();
                 return;
             }
            if (timebean.getEndTime().trim().equals("请选择结束时间")){
                Toast.makeText(this, "请选择结束时间", Toast.LENGTH_SHORT).show();
                return;
            }
            String endtime=timebean.getEndTime().trim();
            String starttime=timebean.getStartTime().trim();

            if (endtime.contains(":")||endtime.contains(" ")){
                endtime=endtime.replace(" ","-");
                endtime=endtime.replace(":","-");
            }
            if (starttime.contains(":")||starttime.contains(" ")){
                starttime=starttime.replace(" ","-");
                starttime=starttime.replace(":","-");
            }
             String[] starttimes = starttime.split("-");
             String[] endtimes = endtime.split("-");
//
            int endyear= Integer.parseInt(endtimes[0]);
            int startyear= Integer.parseInt(starttimes[0]);
//
            int endmouth=Integer.parseInt(endtimes[1]);
            int startmouth=Integer.parseInt(starttimes[1]);
//
            int startday=Integer.parseInt(starttimes[2]);
            int endday=Integer.parseInt(endtimes[2]);
//
            int starthour=Integer.parseInt(starttimes[3]);
            int endhour=Integer.parseInt(endtimes[3]);
//
            int startmin=Integer.parseInt(starttimes[4]);
            int endmin=Integer.parseInt(endtimes[4]);
            Log.i(TAG, "year: "+startyear+endyear);
            Log.i(TAG, "mouth: "+startmouth+endmouth);
            Log.i(TAG, "day: "+startday+endday);
            Log.i(TAG, "hour: "+starthour+endhour);
            Log.i(TAG, "min: "+startmin+endmin);
            if(startyear>endyear){
                Toast.makeText(getApplicationContext(), "请选择正确时间",Toast.LENGTH_SHORT ).show();
                return;
            }else if(startyear==endyear){
                if(startmouth>endmouth){
                    Toast.makeText(getApplicationContext(), "请选择正确时间", Toast.LENGTH_SHORT).show();
                    return;
                }else if(startmouth==endmouth){
                    if(startday>endday){
                        Toast.makeText(getApplicationContext(), "请选择正确时间", Toast.LENGTH_SHORT).show();
                        return;
                    }else if(startday==endday){
                        if(starthour>endhour){
                            Toast.makeText(getApplicationContext(), "请选择正确时间", Toast.LENGTH_SHORT).show();
                            return;
                        }else if(starthour==endhour){
                            if(startmin>endmin){
                                Toast.makeText(getApplicationContext(), "请选择正确时间", Toast.LENGTH_SHORT).show();
                                return;
                            }else{
                                addtime(starttime,endtime);
                            }
                        }else {
                            addtime(starttime,endtime);
                        }
                    }else{
                        addtime(starttime,endtime);
                    }
                }else{
                    addtime(starttime,endtime);
                }
            }else {
                addtime(starttime,endtime);
            }
        }
        Log.i(TAG, "list: "+ starttimeList);
        if (TextUtils.isEmpty(et_gotime_id.getText().toString().trim())){
            Toast.makeText(this, "请输入去往时间", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(et_backtime_id.getText().toString().trim())){
            Toast.makeText(this, "请输入返回时间", Toast.LENGTH_SHORT).show();
            return;
        }
        save(starttimeList, endtimeList);
    }

    private void addtime(String starttime, String endtime) {
        Log.i(TAG, "addtime: "+starttime+endtime);
        String[] starttimes = starttime.split("-");
        String[] endtimes = endtime.split("-");
//
        String endyear= endtimes[0];
        String startyear= starttimes[0];
//
        String endmouth=endtimes[1];
        String startmouth=starttimes[1];
//
        String startday=starttimes[2];
        String endday=endtimes[2];
//
        String starthour=starttimes[3];
        String endhour=endtimes[3];
//
        String startmin=starttimes[4];
        String endmin=endtimes[4];

        starttimeList.add(startyear+"-"+startmouth+"-"+startday+" "+starthour+":"+startmin);
        endtimeList.add(endyear+"-"+endmouth+"-"+endday+" "+endhour+":"+endmin);
    }


    private void save(List<String> starttimeList, List<String> endtimeList) {
        JSONObject jsonObject = new JSONObject();
        try {
            JSONArray starttimeArray=new JSONArray();
            for (int i=0;i<starttimeList.size();i++){
                starttimeArray.put(i,starttimeList.get(i));
            }
            jsonObject.put("startTime",starttimeArray);

            JSONArray endtimeArray=new JSONArray();
            for (int i=0;i<endtimeList.size();i++){
                endtimeArray.put(i,endtimeList.get(i));
            }
            jsonObject.put("endTime",endtimeArray);
            jsonObject.put("travelToTime",et_gotime_id.getText().toString());
            jsonObject.put("travelBackTime",et_backtime_id.getText().toString());
            jsonObject.put("repairLogsId",repairLogsId);
            jsonObject.put("userId", sp.getString("userId", ""));
            jsonObject.put("deviceNo", deviceNo);
            jsonObject.put("workOrderId", workOrderId);
            jsonObject.put("orderNo", orderNo);
            jsonObject.put("content", et_remark.getText().toString());
            jsonObject.put("engineerId", engineerId);
            jsonObject.put("engineerName", engineerName);
            jsonObject.put("accessoryName", accessoryName);
            jsonObject.put("reportTime", reportTime);
            jsonObject.put("faultType",faultType);//TODO 故障种类
            int a=0;
            switch (radioGroup_id.getCheckedRadioButtonId()){
                case R.id.rb_fault_id:
                    a=1;
                    break;
                case R.id.rb_part_fault_id:
                    a=2;
                    break;
                case R.id.rb_normal_id:
                    a=3;
                    break;
            }
            jsonObject.put("equipmentStatus",a);//设备状态
            Log.i(TAG, "jsonsf: "+jsonObject);
            String jsonString = Base64.encodeToString(jsonObject.toString().getBytes(), Base64.NO_WRAP);
            showPopwindow1(getPopView1(jsonString));
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    private View getPopView1(final String jsonString) {

        view_pop1 = getLayoutInflater().inflate(R.layout.dialog_item, null);
        TextView tv_title = (TextView) view_pop1.findViewById(R.id.tv_title);
        TextView tv_content = (TextView) view_pop1.findViewById(R.id.tv_content);
        TextView tv_yes = (TextView) view_pop1.findViewById(R.id.tv_yes);
        TextView tv_no = (TextView) view_pop1.findViewById(R.id.tv_no);
        tv_title.setText("提示");
        if (!TextUtils.isEmpty(state) && state.equals("等待维修")) {
            tv_content.setText("是否保存维修记录？工单将变为正在维修状态。");
        } else {
            tv_content.setText("是否保存维修记录？");
        }
        tv_yes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menuWindow1.dismiss();
                getData(jsonString,save_change_url,3);
            }
        });
        tv_no.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                menuWindow1.dismiss();
            }
        });

        return view_pop1;
    }
    private PopupWindow menuWindow1;
    private void showPopwindow1(View view) {
        menuWindow1 = new PopupWindow(view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        menuWindow1.setFocusable(true);
        menuWindow1.setBackgroundDrawable(new BitmapDrawable());
        menuWindow1.showAtLocation(view, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.5f);
        menuWindow1.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                menuWindow1 = null;
                backgroundAlpha(1f);
            }
        });
    }
    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        getWindow().setAttributes(lp);
    }
    /**
     * 调出结束时间控件
     */
    private void dialogTime(final TextView view, final int postion) {
        if (engineerId.equals(sp.getString("userId", ""))&&Integer.parseInt(orderStatus) < 4) {
            Date date = new Date();
            DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
            popWindow = new DatePickerTimePopWindow(this, df.format(date));//弹出时间控件
            WindowManager.LayoutParams lp = getWindow().getAttributes();//获取窗口属性
            lp.alpha = 0.7f;//设置透明度
            getWindow().setAttributes(lp);//设置属性
            popWindow.showAtLocation(findViewById(R.id.root), Gravity.BOTTOM, 0, 0);//在最后当前界面的最下面弹出
            popWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    WindowManager.LayoutParams lp = getWindow().getAttributes();
                    lp.alpha = 1f;
                    getWindow().setAttributes(lp);
                }
            });

            popWindow.setClickListener(new DatePickerTimePopWindow.ClickListener() {//点击确定的时候，直接显示出来

                @Override
                public void getDataListener(String data) {
                    String[] split = data.split("-");
                    Calendar c = Calendar.getInstance();
                    int mYear = c.get(Calendar.YEAR); // 获取当前年份
                    int mMonth = c.get(Calendar.MONTH) + 1;// 获取当前月份
                    // （系统获取的约人为0-11）所以+1
                    int mDay = c.get(Calendar.DAY_OF_MONTH);// 获取当前月份的日期号码

                    String[] split2 = data.split("-");

                    if(view.getId()==R.id.start_time){
                        arr_start=split2;
                    }else{
                        arr_end=split2;
                    }

                    StringBuffer sb = new StringBuffer();
                    sb.append(split2[0]).append("-").append(split2[1]).append("-").append(split2[2])
                            .append(" ").append(split2[3]).append(":").append(split2[4]);
                    view.setText(sb.toString());
                    switch (view.getId()){
                        case R.id.start_time:
                            timeList.get(postion).setStartTime(sb.toString());//修改数据源
                            break;
                        case R.id.end_time:
                            timeList.get(postion).setEndTime(sb.toString());//修改数据源
                            break;
                    }
                    for (fixRecordBackInfo.DataBean.TimeListBean timeListBean:timeList){
                        Log.i(TAG, "getDataListener: "+timeListBean.getStartTime());
                    }
                    if (Integer.parseInt(split[0]) > mYear) {

                    } else {

//						view.setText(data);
                        // if (Integer.parseInt(split[1]) > mMonth) {
                        //
                        // view.setText(data);
                        //
                        // } else if (Integer.parseInt(split[1]) == mMonth) {
                        //
                        // if (Integer.parseInt(split[2]) < mDay) {
                        //
                        // Toast.makeText(getApplicationContext(), "日期选择错误",
                        // Toast.LENGTH_SHORT).show();
                        //
                        // } else {
                        //
                        //
                        // }
                        //
                        // } else {
                        // Toast.makeText(getApplicationContext(), "日期选择错误",
                        // Toast.LENGTH_SHORT).show();
                        // }
                    }
                    if (!TextUtils.isEmpty(data)) {
                        popWindow.dismiss();
                    }

                }
            });
        }
    }
    public void notifyDataSetChangeds(List<fixRecordBackInfo.DataBean.TimeListBean> list){
        if (timeList==null){
            timeList=new LinkedList<>();
        }else{
            timeList.clear();
        }
        if (list.size()<=0){
            return;
        }
        for(fixRecordBackInfo.DataBean.TimeListBean timeListbean:list){
            timeList.add(timeListbean);
        }
        myAdapter.notifyDataSetChanged();
    }
    public void additem(int i){
        if (timeList==null){
            timeList=new LinkedList<>();
        }
        switch (i){
            case 0://添加
                fixRecordBackInfo.DataBean.TimeListBean timeListBean=new fixRecordBackInfo.DataBean.TimeListBean();
                timeListBean.setEndTime("请选择结束时间   ");
                timeListBean.setStartTime("请选择开始时间   ");
                timeList.add(timeListBean);
                myAdapter.notifyDataSetChanged();
                break;
            case 1://先判断长度

                break;
        }

    }


    @Override
    public void getPostion(int postion,View view) {
        switch (view.getId()){
            case R.id.start_time:
                dialogTime((TextView) view,postion);
                break;
            case R.id.end_time:
                dialogTime((TextView) view,postion);
                break;
            case R.id.tv_delete_item_id:
                if (timeList.size()>1){//删除
                    timeList.remove(postion);
                    myAdapter.notifyDataSetChanged();
                }else{
                    Toast.makeText(this, "至少应该有一条维修时间！", Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }
    public void setPricePoint(final EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 1) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 2);
                        editText.setText(s);
                        editText.setSelection(s.length());
                    }
                }
                if (s.toString().trim().substring(0).equals(".")) {//如果输入的是.
                    s = "0" + s;
                    editText.setText(s);
                    editText.setSelection(2);
                }

                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1,2).equals(".")) {
                        editText.setText(s.subSequence(0, 1));
                        editText.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub

            }

        });

    }
    public static void main(String arg[]){
        List<int[]> list=new LinkedList<>();
        int a[]=new int[]{1,2};
        int b[]=new int[]{2,5};
        int c[]=new int[]{7,2};
        int d[]=new int[]{1,2};
        list.add(a);
        list.add(b);
        list.add(c);
        list.add(d);
        list.add(d);
        list.add(d);
        for (int i=0;i<list.size();i++){
            int[] e = list.get(i);
            if (e[0]>e[1]){
                return;
            }
           System.out.print(i);
        }
    }
}
