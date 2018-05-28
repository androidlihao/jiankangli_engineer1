package com.defineapp.jiankangli_engineer.bean;

import java.util.List;

/**
 * Created by 李浩 on 2017/5/10.
 */

public class fixRecordBackInfo {


    /**
     * code : success
     * msg : 操作成功
     * data : {"deviceNo":"9000000001","workOrderId":951,"orderNo":"20170516004","reportTime":"2017-04-18 11:10:57","engineerId":401,"engineerName":"李浩","accessoryName":"说说","faultType":0,"startTime":"2017-05-16-16-13,2017-05-16-16-13,2017-05-17-10-08,2017-05-17-10-15","endTime":"2017-05-16-16-13,2017-05-16-16-13,2017-05-17-10-08,2017-05-17-10-15","travelToTime":2.5,"travelBackTime":3.5,"equipmentStatus":1,"content":"哼哼唧唧","createTime":"2017-05-17 10:14:36","timeList":[{"startTime":"2017-05-16-16-13","endTime":"2017-05-16-16-13"},{"startTime":"2017-05-16-16-13","endTime":"2017-05-16-16-13"},{"startTime":"2017-05-17-10-08","endTime":"2017-05-17-10-08"},{"startTime":"2017-05-17-10-15","endTime":"2017-05-17-10-15"}],"repairLogsId":765}
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
         * deviceNo : 9000000001
         * workOrderId : 951
         * orderNo : 20170516004
         * reportTime : 2017-04-18 11:10:57
         * engineerId : 401
         * engineerName : 李浩
         * accessoryName : 说说
         * faultType : 0
         * startTime : 2017-05-16-16-13,2017-05-16-16-13,2017-05-17-10-08,2017-05-17-10-15
         * endTime : 2017-05-16-16-13,2017-05-16-16-13,2017-05-17-10-08,2017-05-17-10-15
         * travelToTime : 2.5
         * travelBackTime : 3.5
         * equipmentStatus : 1
         * content : 哼哼唧唧
         * createTime : 2017-05-17 10:14:36
         * timeList : [{"startTime":"2017-05-16-16-13","endTime":"2017-05-16-16-13"},{"startTime":"2017-05-16-16-13","endTime":"2017-05-16-16-13"},{"startTime":"2017-05-17-10-08","endTime":"2017-05-17-10-08"},{"startTime":"2017-05-17-10-15","endTime":"2017-05-17-10-15"}]
         * repairLogsId : 765
         */

        private String deviceNo;
        private int workOrderId;
        private String orderNo;
        private String reportTime;
        private int engineerId;
        private String engineerName;
        private String accessoryName;
        private int faultType;
        private String startTime;
        private String endTime;
        private double travelToTime;
        private double travelBackTime;
        private int equipmentStatus;
        private String content;
        private String createTime;
        private int repairLogsId;
        private List<TimeListBean> timeList;

        public String getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(String deviceNo) {
            this.deviceNo = deviceNo;
        }

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

        public String getAccessoryName() {
            return accessoryName;
        }

        public void setAccessoryName(String accessoryName) {
            this.accessoryName = accessoryName;
        }

        public int getFaultType() {
            return faultType;
        }

        public void setFaultType(int faultType) {
            this.faultType = faultType;
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

        public double getTravelToTime() {
            return travelToTime;
        }

        public void setTravelToTime(double travelToTime) {
            this.travelToTime = travelToTime;
        }

        public double getTravelBackTime() {
            return travelBackTime;
        }

        public void setTravelBackTime(double travelBackTime) {
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

        public String getCreateTime() {
            return createTime;
        }

        public void setCreateTime(String createTime) {
            this.createTime = createTime;
        }

        public int getRepairLogsId() {
            return repairLogsId;
        }

        public void setRepairLogsId(int repairLogsId) {
            this.repairLogsId = repairLogsId;
        }

        public List<TimeListBean> getTimeList() {
            return timeList;
        }

        public void setTimeList(List<TimeListBean> timeList) {
            this.timeList = timeList;
        }

        public static class TimeListBean {
            /**
             * startTime : 2017-05-16-16-13
             * endTime : 2017-05-16-16-13
             */

            private String startTime;
            private String endTime;

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
        }
    }
}
