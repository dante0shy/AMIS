package com.aims.room;

import java.sql.ResultSet;
import java.util.Vector;

import com.aims.DButil.DButil;

public class GetLiveOrNot {
	private ResultSet rs;
	//创建存放返回结果的Vector
	private Vector<Vector> v=new Vector<Vector>();
	//根据学号获得学生已修课程信息的方法
	public Vector getRoomLiveInfo(int index, String aparID)
	{//将Vector清空，防止有冗余记录
		v.removeAllElements();
		try
		{//查询数据库，将数据存入Vector v并返回
			rs = DButil.searchLiveOrNot(index,aparID);
			while(rs.next())
			{		
				String[] data = {"",""};
				Vector temp=new Vector();			
				for(int i = 0;i < 2;i++)
				{
					data[i] = rs.getString(i+1).trim();
					temp.add(data[i]);
				}
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
