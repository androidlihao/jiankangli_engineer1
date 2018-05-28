package com.defineapp.jiankangli_engineer.bean;

import java.util.List;

public class OrderDetils2 {


	/**
	 * code : success
	 * msg : 成功
	 * data : {"workOrderId":452,"orderNo":"20160504002","name":"","devicePhone":"","deviceName":"GE新光系列","deviceModel":"optima MR360","deviceNo":"","repairType":"","repairName":"胡红伟","temporaryNum":"","reportTime":"2016-05-04 11:41:43","bookTime":"2016-05-04 11:30~13:30","hospitalName":"河南省驻马店市平舆县人民医院","hospitaAddress":"河南省驻马店市平舆县健康路93号","sectionName":"","picUrl":"","accessoryStatus":"","servicerName":"","remark":"液氦压力持续升高","orderStatus":5,"auditStatus":0,"engineerId":315,"engineerName":"张银锋","accessoryName":"","repairPhone":"13723095088","faultType":0,"tWorkOrderPics":[],"accStatus":0}
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
		 * workOrderId : 452
		 * orderNo : 20160504002
		 * name :
		 * devicePhone :
		 * deviceName : GE新光系列
		 * deviceModel : optima MR360
		 * deviceNo :
		 * repairType :
		 * repairName : 胡红伟
		 * temporaryNum :
		 * reportTime : 2016-05-04 11:41:43
		 * bookTime : 2016-05-04 11:30~13:30
		 * hospitalName : 河南省驻马店市平舆县人民医院
		 * hospitaAddress : 河南省驻马店市平舆县健康路93号
		 * sectionName :
		 * picUrl :
		 * accessoryStatus :
		 * servicerName :
		 * remark : 液氦压力持续升高
		 * orderStatus : 5
		 * auditStatus : 0
		 * engineerId : 315
		 * engineerName : 张银锋
		 * accessoryName :
		 * repairPhone : 13723095088
		 * faultType : 0
		 * tWorkOrderPics : []
		 * accStatus : 0
		 */

		private int workOrderId;
		private String orderNo;
		private String name;
		private String devicePhone;
		private String deviceName;
		private String deviceModel;
		private String deviceNo;
		private String repairType;
		private String repairName;
		private String temporaryNum;
		private String reportTime;
		private String bookTime;
		private String hospitalName;
		private String hospitaAddress;
		private String sectionName;
		private String picUrl;
		private String accessoryStatus;
		private String servicerName;
		private String remark;
		private int orderStatus;
		private int auditStatus;
		private int engineerId;
		private String engineerName;
		private String accessoryName;
		private String repairPhone;
		private int faultType;
		private int accStatus;
		private String projectCode;

		public String getProjectCode() {
			return projectCode;
		}

		public void setProjectCode(String projectCode) {
			this.projectCode = projectCode;
		}

		private List<?> tWorkOrderPics;

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

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDevicePhone() {
			return devicePhone;
		}

		public void setDevicePhone(String devicePhone) {
			this.devicePhone = devicePhone;
		}

		public String getDeviceName() {
			return deviceName;
		}

		public void setDeviceName(String deviceName) {
			this.deviceName = deviceName;
		}

		public String getDeviceModel() {
			return deviceModel;
		}

		public void setDeviceModel(String deviceModel) {
			this.deviceModel = deviceModel;
		}

		public String getDeviceNo() {
			return deviceNo;
		}

		public void setDeviceNo(String deviceNo) {
			this.deviceNo = deviceNo;
		}

		public String getRepairType() {
			return repairType;
		}

		public void setRepairType(String repairType) {
			this.repairType = repairType;
		}

		public String getRepairName() {
			return repairName;
		}

		public void setRepairName(String repairName) {
			this.repairName = repairName;
		}

		public String getTemporaryNum() {
			return temporaryNum;
		}

		public void setTemporaryNum(String temporaryNum) {
			this.temporaryNum = temporaryNum;
		}

		public String getReportTime() {
			return reportTime;
		}

		public void setReportTime(String reportTime) {
			this.reportTime = reportTime;
		}

		public String getBookTime() {
			return bookTime;
		}

		public void setBookTime(String bookTime) {
			this.bookTime = bookTime;
		}

		public String getHospitalName() {
			return hospitalName;
		}

		public void setHospitalName(String hospitalName) {
			this.hospitalName = hospitalName;
		}

		public String getHospitaAddress() {
			return hospitaAddress;
		}

		public void setHospitaAddress(String hospitaAddress) {
			this.hospitaAddress = hospitaAddress;
		}

		public String getSectionName() {
			return sectionName;
		}

		public void setSectionName(String sectionName) {
			this.sectionName = sectionName;
		}

		public String getPicUrl() {
			return picUrl;
		}

		public void setPicUrl(String picUrl) {
			this.picUrl = picUrl;
		}

		public String getAccessoryStatus() {
			return accessoryStatus;
		}

		public void setAccessoryStatus(String accessoryStatus) {
			this.accessoryStatus = accessoryStatus;
		}

		public String getServicerName() {
			return servicerName;
		}

		public void setServicerName(String servicerName) {
			this.servicerName = servicerName;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public int getOrderStatus() {
			return orderStatus;
		}

		public void setOrderStatus(int orderStatus) {
			this.orderStatus = orderStatus;
		}

		public int getAuditStatus() {
			return auditStatus;
		}

		public void setAuditStatus(int auditStatus) {
			this.auditStatus = auditStatus;
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

		public String getRepairPhone() {
			return repairPhone;
		}

		public void setRepairPhone(String repairPhone) {
			this.repairPhone = repairPhone;
		}

		public int getFaultType() {
			return faultType;
		}

		public void setFaultType(int faultType) {
			this.faultType = faultType;
		}

		public int getAccStatus() {
			return accStatus;
		}

		public void setAccStatus(int accStatus) {
			this.accStatus = accStatus;
		}

		public List<?> getTWorkOrderPics() {
			return tWorkOrderPics;
		}

		public void setTWorkOrderPics(List<?> tWorkOrderPics) {
			this.tWorkOrderPics = tWorkOrderPics;
		}
	}
}
