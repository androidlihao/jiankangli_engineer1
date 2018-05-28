package com.defineapp.jiankangli_engineer.bean;


import java.util.List;

/**
 * Created by 李浩 on 2017/5/12.
 */

public class fixRecordInfo {

    /**
     * code : success
     * msg : 成功
     * data : [{"orderNo":"20160514002","deviceNo":"7000000006","reportTime":"2016-05-14 16:40:33","engineerName":"刘德坤","accessoryName":"","endTime":"2016-05-15 14:20","content":"查看报错，报线圈偏压短路。换用其它八通道线圈工作正常，检查线圈插头和插座无异常。关闭偏压使用能，扫描正常。拆开头部线圈，发现有铜螺钉松脱，重新紧固螺钉后扫描正常，但提示偏压配置失败，重启机器后恢复正常。不再报错。","createTime":"2016-05-15 15:01:29","startTime":"2016-05-15 0:25","faultType":0,"equipmentStatus":0,"travelToTime":0,"travelBackTime":0,"timeList":[{"startTime":"2016-05-15 0:25","endTime":"2016-05-15 14:20"}]}]
     */

    private String code;
    private String msg;
    private List<DataBean> data;

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

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * orderNo : 20160514002
         * deviceNo : 7000000006
         * reportTime : 2016-05-14 16:40:33
         * engineerName : 刘德坤
         * accessoryName :
         * endTime : 2016-05-15 14:20
         * content : 查看报错，报线圈偏压短路。换用其它八通道线圈工作正常，检查线圈插头和插座无异常。关闭偏压使用能，扫描正常。拆开头部线圈，发现有铜螺钉松脱，重新紧固螺钉后扫描正常，但提示偏压配置失败，重启机器后恢复正常。不再报错。
         * createTime : 2016-05-15 15:01:29
         * startTime : 2016-05-15 0:25
         * faultType : 0
         * equipmentStatus : 0
         * travelToTime : 0
         * travelBackTime : 0
         * timeList : [{"startTime":"2016-05-15 0:25","endTime":"2016-05-15 14:20"}]
         */

        private String orderNo;
        private String deviceNo;
        private String reportTime;
        private String engineerName;
        private String accessoryName;
        private String endTime;
        private String content;
        private String createTime;
        private String startTime;
        private int faultType;
        private int equipmentStatus;
        private double travelToTime;
        private double travelBackTime;
        private List<TimeListBean> timeList;

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public String getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(String deviceNo) {
            this.deviceNo = deviceNo;
        }

        public String getReportTime() {
            return reportTime;
        }

        public void setReportTime(String reportTime) {
            this.reportTime = reportTime;
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

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
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

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public int getFaultType() {
            return faultType;
        }

        public void setFaultType(int faultType) {
            this.faultType = faultType;
        }

        public int getEquipmentStatus() {
            return equipmentStatus;
        }

        public void setEquipmentStatus(int equipmentStatus) {
            this.equipmentStatus = equipmentStatus;
        }

        public double getTravelToTime() {
            return travelToTime;
        }

        public void setTravelToTime(int travelToTime) {
            this.travelToTime = travelToTime;
        }

        public double getTravelBackTime() {
            return travelBackTime;
        }

        public void setTravelBackTime(int travelBackTime) {
            this.travelBackTime = travelBackTime;
        }

        public List<TimeListBean> getTimeList() {
            return timeList;
        }

        public void setTimeList(List<TimeListBean> timeList) {
            this.timeList = timeList;
        }

        public static class TimeListBean {
            /**
             * startTime : 2016-05-15 0:25
             * endTime : 2016-05-15 14:20
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
