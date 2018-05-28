package com.defineapp.jiankangli_engineer.bean;

import java.util.List;

/**
 * Created by 李浩 on 2017/5/16.
 */

public class OrderStatusBean {


    /**
     * code : success
     * data : {"accStatus":"","accessoryName":"","addType":"2","appointTime":"","area":"海南海口龙华区","auditCount":1,"auditStatus":1,"beforeEngineer":"李浩&nbsp;&nbsp;2017-05-16 17:36:29","bookTime":"2017-05-16 17:37~17:37","closeTime":"","count":0,"deviceBrand":"西门子","deviceModel":"ACUSONS 2000","deviceName":"西门子US","deviceNo":"7000000006","devicePhone":"13307552188","deviceStatus":"运行","deviceType":"US","endTime":"2017-05-17 09:26:43","engineerId":401,"engineerIsNews":"","engineerName":"李浩","engineerPhone":"13755794643","evaluationContent":"","extendedTime":"","faultType":3,"helpWorkId":871,"hospitaAddress":"海南省海口市龙昆南路100号","hospitalName":"解放军第187中心医院","htNo":"","id":876,"isExtend":"","isNews":"","keepTime":"2016-06-01~2017-05-31","name":"海英主任","orderNo":"20160927003(5)","orderStatus":3,"orderStatusChinese":"正在维修","pics":"","readFlag":"1","remark":"第一次保养","repairLog":"","repairName":"海英主任","repairPersonId":1,"repairPhone":"","repairStatus":"保养","repairType":"技术保","reportTime":"2016-10-26 18:27:00","score1":"","score2":"","score3":"","score4":"","score5":"","score6":"","sectionName":"","startTime":"2017-05-16 17:36:43","stopRemark":"","tWorkOrderPics":[],"temporaryNum":"13307552188","totalScore":"","travelTime":4.5,"type":"","useflag":"1","webIsNews":"","workHours":0,"workOrderId":876}
     * msg : 成功
     * time : 2017-05-17 09:26:43
     */

    private String code;
    private DataBean data;
    private String msg;
    private String time;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public static class DataBean {
        /**
         * accStatus :
         * accessoryName :
         * addType : 2
         * appointTime :
         * area : 海南海口龙华区
         * auditCount : 1
         * auditStatus : 1
         * beforeEngineer : 李浩&nbsp;&nbsp;2017-05-16 17:36:29
         * bookTime : 2017-05-16 17:37~17:37
         * closeTime :
         * count : 0
         * deviceBrand : 西门子
         * deviceModel : ACUSONS 2000
         * deviceName : 西门子US
         * deviceNo : 7000000006
         * devicePhone : 13307552188
         * deviceStatus : 运行
         * deviceType : US
         * endTime : 2017-05-17 09:26:43
         * engineerId : 401
         * engineerIsNews :
         * engineerName : 李浩
         * engineerPhone : 13755794643
         * evaluationContent :
         * extendedTime :
         * faultType : 3
         * helpWorkId : 871
         * hospitaAddress : 海南省海口市龙昆南路100号
         * hospitalName : 解放军第187中心医院
         * htNo :
         * id : 876
         * isExtend :
         * isNews :
         * keepTime : 2016-06-01~2017-05-31
         * name : 海英主任
         * orderNo : 20160927003(5)
         * orderStatus : 3
         * orderStatusChinese : 正在维修
         * pics :
         * readFlag : 1
         * remark : 第一次保养
         * repairLog :
         * repairName : 海英主任
         * repairPersonId : 1
         * repairPhone :
         * repairStatus : 保养
         * repairType : 技术保
         * reportTime : 2016-10-26 18:27:00
         * score1 :
         * score2 :
         * score3 :
         * score4 :
         * score5 :
         * score6 :
         * sectionName :
         * startTime : 2017-05-16 17:36:43
         * stopRemark :
         * tWorkOrderPics : []
         * temporaryNum : 13307552188
         * totalScore :
         * travelTime : 4.5
         * type :
         * useflag : 1
         * webIsNews :
         * workHours : 0
         * workOrderId : 876
         */

        private String accStatus;
        private String accessoryName;
        private String addType;
        private String appointTime;
        private String area;
        private int auditCount;
        private int auditStatus;
        private String beforeEngineer;
        private String bookTime;
        private String closeTime;
        private int count;
        private String deviceBrand;
        private String deviceModel;
        private String deviceName;
        private String deviceNo;
        private String devicePhone;
        private String deviceStatus;
        private String deviceType;
        private String endTime;
        private int engineerId;
        private String engineerIsNews;
        private String engineerName;
        private String engineerPhone;
        private String evaluationContent;
        private String extendedTime;
        private int faultType;
        private int helpWorkId;
        private String hospitaAddress;
        private String hospitalName;
        private String htNo;
        private int id;
        private String isExtend;
        private String isNews;
        private String keepTime;
        private String name;
        private String orderNo;
        private int orderStatus;
        private String orderStatusChinese;
        private String pics;
        private String readFlag;
        private String remark;
        private String repairLog;
        private String repairName;
        private int repairPersonId;
        private String repairPhone;
        private String repairStatus;
        private String repairType;
        private String reportTime;
        private String score1;
        private String score2;
        private String score3;
        private String score4;
        private String score5;
        private String score6;
        private String sectionName;
        private String startTime;
        private String stopRemark;
        private String temporaryNum;
        private String totalScore;
        private double travelTime;
        private String type;
        private String useflag;
        private String webIsNews;
        private int workHours;
        private int workOrderId;
        private List<?> tWorkOrderPics;

        public String getAccStatus() {
            return accStatus;
        }

        public void setAccStatus(String accStatus) {
            this.accStatus = accStatus;
        }

        public String getAccessoryName() {
            return accessoryName;
        }

        public void setAccessoryName(String accessoryName) {
            this.accessoryName = accessoryName;
        }

        public String getAddType() {
            return addType;
        }

        public void setAddType(String addType) {
            this.addType = addType;
        }

        public String getAppointTime() {
            return appointTime;
        }

        public void setAppointTime(String appointTime) {
            this.appointTime = appointTime;
        }

        public String getArea() {
            return area;
        }

        public void setArea(String area) {
            this.area = area;
        }

        public int getAuditCount() {
            return auditCount;
        }

        public void setAuditCount(int auditCount) {
            this.auditCount = auditCount;
        }

        public int getAuditStatus() {
            return auditStatus;
        }

        public void setAuditStatus(int auditStatus) {
            this.auditStatus = auditStatus;
        }

        public String getBeforeEngineer() {
            return beforeEngineer;
        }

        public void setBeforeEngineer(String beforeEngineer) {
            this.beforeEngineer = beforeEngineer;
        }

        public String getBookTime() {
            return bookTime;
        }

        public void setBookTime(String bookTime) {
            this.bookTime = bookTime;
        }

        public String getCloseTime() {
            return closeTime;
        }

        public void setCloseTime(String closeTime) {
            this.closeTime = closeTime;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public String getDeviceBrand() {
            return deviceBrand;
        }

        public void setDeviceBrand(String deviceBrand) {
            this.deviceBrand = deviceBrand;
        }

        public String getDeviceModel() {
            return deviceModel;
        }

        public void setDeviceModel(String deviceModel) {
            this.deviceModel = deviceModel;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        public String getDeviceNo() {
            return deviceNo;
        }

        public void setDeviceNo(String deviceNo) {
            this.deviceNo = deviceNo;
        }

        public String getDevicePhone() {
            return devicePhone;
        }

        public void setDevicePhone(String devicePhone) {
            this.devicePhone = devicePhone;
        }

        public String getDeviceStatus() {
            return deviceStatus;
        }

        public void setDeviceStatus(String deviceStatus) {
            this.deviceStatus = deviceStatus;
        }

        public String getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(String deviceType) {
            this.deviceType = deviceType;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public int getEngineerId() {
            return engineerId;
        }

        public void setEngineerId(int engineerId) {
            this.engineerId = engineerId;
        }

        public String getEngineerIsNews() {
            return engineerIsNews;
        }

        public void setEngineerIsNews(String engineerIsNews) {
            this.engineerIsNews = engineerIsNews;
        }

        public String getEngineerName() {
            return engineerName;
        }

        public void setEngineerName(String engineerName) {
            this.engineerName = engineerName;
        }

        public String getEngineerPhone() {
            return engineerPhone;
        }

        public void setEngineerPhone(String engineerPhone) {
            this.engineerPhone = engineerPhone;
        }

        public String getEvaluationContent() {
            return evaluationContent;
        }

        public void setEvaluationContent(String evaluationContent) {
            this.evaluationContent = evaluationContent;
        }

        public String getExtendedTime() {
            return extendedTime;
        }

        public void setExtendedTime(String extendedTime) {
            this.extendedTime = extendedTime;
        }

        public int getFaultType() {
            return faultType;
        }

        public void setFaultType(int faultType) {
            this.faultType = faultType;
        }

        public int getHelpWorkId() {
            return helpWorkId;
        }

        public void setHelpWorkId(int helpWorkId) {
            this.helpWorkId = helpWorkId;
        }

        public String getHospitaAddress() {
            return hospitaAddress;
        }

        public void setHospitaAddress(String hospitaAddress) {
            this.hospitaAddress = hospitaAddress;
        }

        public String getHospitalName() {
            return hospitalName;
        }

        public void setHospitalName(String hospitalName) {
            this.hospitalName = hospitalName;
        }

        public String getHtNo() {
            return htNo;
        }

        public void setHtNo(String htNo) {
            this.htNo = htNo;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getIsExtend() {
            return isExtend;
        }

        public void setIsExtend(String isExtend) {
            this.isExtend = isExtend;
        }

        public String getIsNews() {
            return isNews;
        }

        public void setIsNews(String isNews) {
            this.isNews = isNews;
        }

        public String getKeepTime() {
            return keepTime;
        }

        public void setKeepTime(String keepTime) {
            this.keepTime = keepTime;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
        }

        public int getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(int orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getOrderStatusChinese() {
            return orderStatusChinese;
        }

        public void setOrderStatusChinese(String orderStatusChinese) {
            this.orderStatusChinese = orderStatusChinese;
        }

        public String getPics() {
            return pics;
        }

        public void setPics(String pics) {
            this.pics = pics;
        }

        public String getReadFlag() {
            return readFlag;
        }

        public void setReadFlag(String readFlag) {
            this.readFlag = readFlag;
        }

        public String getRemark() {
            return remark;
        }

        public void setRemark(String remark) {
            this.remark = remark;
        }

        public String getRepairLog() {
            return repairLog;
        }

        public void setRepairLog(String repairLog) {
            this.repairLog = repairLog;
        }

        public String getRepairName() {
            return repairName;
        }

        public void setRepairName(String repairName) {
            this.repairName = repairName;
        }

        public int getRepairPersonId() {
            return repairPersonId;
        }

        public void setRepairPersonId(int repairPersonId) {
            this.repairPersonId = repairPersonId;
        }

        public String getRepairPhone() {
            return repairPhone;
        }

        public void setRepairPhone(String repairPhone) {
            this.repairPhone = repairPhone;
        }

        public String getRepairStatus() {
            return repairStatus;
        }

        public void setRepairStatus(String repairStatus) {
            this.repairStatus = repairStatus;
        }

        public String getRepairType() {
            return repairType;
        }

        public void setRepairType(String repairType) {
            this.repairType = repairType;
        }

        public String getReportTime() {
            return reportTime;
        }

        public void setReportTime(String reportTime) {
            this.reportTime = reportTime;
        }

        public String getScore1() {
            return score1;
        }

        public void setScore1(String score1) {
            this.score1 = score1;
        }

        public String getScore2() {
            return score2;
        }

        public void setScore2(String score2) {
            this.score2 = score2;
        }

        public String getScore3() {
            return score3;
        }

        public void setScore3(String score3) {
            this.score3 = score3;
        }

        public String getScore4() {
            return score4;
        }

        public void setScore4(String score4) {
            this.score4 = score4;
        }

        public String getScore5() {
            return score5;
        }

        public void setScore5(String score5) {
            this.score5 = score5;
        }

        public String getScore6() {
            return score6;
        }

        public void setScore6(String score6) {
            this.score6 = score6;
        }

        public String getSectionName() {
            return sectionName;
        }

        public void setSectionName(String sectionName) {
            this.sectionName = sectionName;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getStopRemark() {
            return stopRemark;
        }

        public void setStopRemark(String stopRemark) {
            this.stopRemark = stopRemark;
        }

        public String getTemporaryNum() {
            return temporaryNum;
        }

        public void setTemporaryNum(String temporaryNum) {
            this.temporaryNum = temporaryNum;
        }

        public String getTotalScore() {
            return totalScore;
        }

        public void setTotalScore(String totalScore) {
            this.totalScore = totalScore;
        }

        public double getTravelTime() {
            return travelTime;
        }

        public void setTravelTime(double travelTime) {
            this.travelTime = travelTime;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getUseflag() {
            return useflag;
        }

        public void setUseflag(String useflag) {
            this.useflag = useflag;
        }

        public String getWebIsNews() {
            return webIsNews;
        }

        public void setWebIsNews(String webIsNews) {
            this.webIsNews = webIsNews;
        }

        public int getWorkHours() {
            return workHours;
        }

        public void setWorkHours(int workHours) {
            this.workHours = workHours;
        }

        public int getWorkOrderId() {
            return workOrderId;
        }

        public void setWorkOrderId(int workOrderId) {
            this.workOrderId = workOrderId;
        }

        public List<?> getTWorkOrderPics() {
            return tWorkOrderPics;
        }

        public void setTWorkOrderPics(List<?> tWorkOrderPics) {
            this.tWorkOrderPics = tWorkOrderPics;
        }
    }
}
