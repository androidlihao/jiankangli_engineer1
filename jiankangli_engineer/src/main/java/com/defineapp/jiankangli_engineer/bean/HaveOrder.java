package com.defineapp.jiankangli_engineer.bean;

import java.util.List;

public class HaveOrder {
	public String code;
	public String msg;
	public String time;
	public Data data;
	public class Data{
		
		public String deviceNo ;
		public String type ;
		public String orderNo ;
		public List <OrderDetils>list ;
		public Object deviceInfo;
	}
}
