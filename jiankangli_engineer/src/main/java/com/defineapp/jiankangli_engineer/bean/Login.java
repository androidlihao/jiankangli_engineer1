package com.defineapp.jiankangli_engineer.bean;

public class Login {
	private String code;
	private String msg;
	private String time;
	
	private Data data;

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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public Data getData() {
		return data;
	}

	public void setData(Data data) {
		this.data = data;
	}

	public class Data {
		private String headPicUrl;
		private String hospitalName;
		private String id;
		private String phoneNumber;
		private String engineerIsNows;
		private String messCount;
		public String getEngineerIsNows() {
			return engineerIsNows;
		}

		public void setEngineerIsNows(String engineerIsNows) {
			this.engineerIsNows = engineerIsNows;
		}

		public String getMessCount() {
			return messCount;
		}

		public void setMessCount(String messCount) {
			this.messCount = messCount;
		}


		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public String getRemark() {
			return remark;
		}

		public void setRemark(String remark) {
			this.remark = remark;
		}

		public String getStatus() {
			return status;
		}

		public void setStatus(String status) {
			this.status = status;
		}

		public String getUseFlag() {
			return useFlag;
		}

		public void setUseFlag(String useFlag) {
			this.useFlag = useFlag;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}

		private String password;
		private String remark;
		private String status;
		private String useFlag;
		private String userId;
		private String userName;
		private String engineerType;
		

		public String getEngineerType() {
			return engineerType;
		}

		public void setEngineerType(String engineerType) {
			this.engineerType = engineerType;
		}

		public String getHeadPicUrl() {
			return headPicUrl;
		}

		public void setHeadPicUrl(String headPicUrl) {
			this.headPicUrl = headPicUrl;
		}

		public String getHospitalName() {
			return hospitalName;
		}

		public void setHospitalName(String hospitalName) {
			this.hospitalName = hospitalName;
		}


		public String getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(String phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		public String getUserId() {
			return userId;
		}

		public void setUserId(String userId) {
			this.userId = userId;
		}
	}

}
