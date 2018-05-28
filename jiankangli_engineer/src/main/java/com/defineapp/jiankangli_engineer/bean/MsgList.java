package com.defineapp.jiankangli_engineer.bean;

import java.util.List;

public class MsgList {
	public String code;
	public String msg;
	public String time;
	public Data data;
	public class Data{
		
		public List <MsgListItem>list;
	}
}
