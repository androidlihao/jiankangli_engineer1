package com.defineapp.jiankangli_engineer.bean;

public class PersonCenter {
	public String code;
	public String msg;
	public String time;
	public Data data;

	public class Data {
		public CreateDate createDate;
		public String equipmentName;
		public String headPicUrl;
		public String hospitalName;
		public String id;
		public String name;
		public String password;
		public String phoneNumber;
		public String servicePhone;
		public String useflag;
		public String userId;

		public class CreateDate {

			public String data;
			public String day;
			public String hours;
			public String minutes;
			public String month;
			public String nanos;
			public String seconds;
			public String time;
			public String timezoneOffset;
			public String year;
		}

	}
}
