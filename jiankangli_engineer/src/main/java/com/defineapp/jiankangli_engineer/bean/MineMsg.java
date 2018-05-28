package com.defineapp.jiankangli_engineer.bean;

import java.util.List;


public class MineMsg {
	public String code;
	public String msg;
	public String time;
	public Data data;
	public class Data{
		
		public List <MineMsgItem>list;
	}
}
