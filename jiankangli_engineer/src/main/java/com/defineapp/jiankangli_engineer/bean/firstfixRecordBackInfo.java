package com.defineapp.jiankangli_engineer.bean;

import java.util.List;

/**
 * Created by 李浩 on 2017/5/16.
 */

public class firstfixRecordBackInfo {


    /**
     * code : 110
     * msg : 操作成功
     * data : {"workOrderId":937,"orderNo":"20170117006","reportTime":"2017-01-17 10:04:09","engineerId":401,"engineerName":"李浩","deviceNo":"2000000006","accessoryName":null,"faultType":3,"timeList":[],"repairLogsId":"","travelToTime":"","travelBackTime":"","equipmentStatus":0,"content":"","startTime":"","endTime":""}
     */

    private String code;
    private String msg;
    private DataBean data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * workOrderId : 937
         * orderNo : 20170117006
         * reportTime : 2017-01-17 10:04:09
         * engineerId : 401
         * engineerName : 李浩
         * deviceNo : 2000000006
         * accessoryName : null
         * faultType : 3
         * timeList : []
         * repairLogsId :
         * travelToTime :
         * travelBackTime :
         * equipmentStatus : 0
         * content :
         * startTime :
         * endTime :
         */

        private int workOrderId;
        private String orderNo;
        private String reportTime;
        private int engineerId;
        private String engineerName;
        private String deviceNo;
        private Object accessoryName;
        private int faultType;
        private String repairLogsId;
        private String travelToTime;
        private String travelBackTime;
        private int equipmentStatus;
        private String content;
        private String startTime;
        private String endTime;
        private List<?> timeList;

        public int getWorkOrderId() {
            return workOrderId;
        }

        public void setWorkOrderId(int workOrderId) {
            this.workOrderId = workOrderId;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getReportTime() {
            return reportTime;
        }

        public void setReportTime(String reportTime) {
            this.reportTime = reportTime;
        }

        public int getEngineerId() {
            return engineerId;
        }

        public void setEngineerId(int engineerId) {
            this.engineerId = engineerId;
        }

        public String getEngineerName() {
            return engineerName;
        }

        public void setEngineerName(String engineerName) {
            this.engineerName = engineerName;
        }

        public String getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(String deviceNo) {
            this.deviceNo = deviceNo;
        }

        public Object getAccessoryName() {
            return accessoryName;
        }

        public void setAccessoryName(Object accessoryName) {
            this.accessoryName = accessoryName;
        }

        public int getFaultType() {
            return faultType;
        }

        public void setFaultType(int faultType) {
            this.faultType = faultType;
        }

        public String getRepairLogsId() {
            return repairLogsId;
        }

        public void setRepairLogsId(String repairLogsId) {
            this.repairLogsId = repairLogsId;
        }

        public String getTravelToTime() {
            return travelToTime;
        }

        public void setTravelToTime(String travelToTime) {
            this.travelToTime = travelToTime;
        }

        public String getTravelBackTime() {
            return travelBackTime;
        }

        public void setTravelBackTime(String travelBackTime) {
            this.travelBackTime = travelBackTime;
        }

        public int getEquipmentStatus() {
            return equipmentStatus;
        }

        public void setEquipmentStatus(int equipmentStatus) {
            this.equipmentStatus = equipmentStatus;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public List<?> getTimeList() {
            return timeList;
        }

        public void setTimeList(List<?> timeList) {
            this.timeList = timeList;
        }
    }
}
