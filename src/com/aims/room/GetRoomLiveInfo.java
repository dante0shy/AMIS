package com.aims.room;

import java.sql.ResultSet;
import java.util.Vector;

import com.aims.DButil.DButil;

public class GetRoomLiveInfo {
	private ResultSet rs1;
	private ResultSet rs2;
	//创建存放返回结果的Vector
	private Vector<Vector> v=new Vector<Vector>();
	//根据学号获得学生已修课程信息的方法
	public Vector getRoomLiveInfo(String aparID)
	{//将Vector清空，防止有冗余记录
		v.removeAllElements();
		try
		{//查询数据库，将数据存入Vector v并返回
			rs1 = DButil.searchRoomLiveInfo(aparID);
			while(rs1.next())
			{		
				String[] data = {"","0"};
				Vector temp=new Vector();			
				data[0] = rs1.getString(1).trim();
				temp.add(data[0]);
				rs2 = DButil.searchRoomLiveNum(aparID,data[0]);
				if(rs2.next())
					data[1] = rs2.getString(1).trim();
				temp.add(data[1]);
				v.add(temp);
			}
			System.out.println(v);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return v;
	}
}