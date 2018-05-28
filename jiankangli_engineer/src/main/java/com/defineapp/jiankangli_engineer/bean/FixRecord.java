package com.defineapp.jiankangli_engineer.bean;

import java.util.List;

public class FixRecord {
	public String code;
	public String msg;
	public Data data;
	public class Data{
		public String faultType;
		public  List<RecrodItem> list;
//		public String temporaryNum;
//		public String totalScore;
//		public String useflag;
//		public String workOrderId;
		
	}
}
