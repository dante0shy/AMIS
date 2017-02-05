package com.aims.student;

import java.sql.ResultSet;
import java.util.Vector;

import com.aims.DButil.DButil;

public class GetStuInfo {
	private ResultSet rs;
	//创建存放返回结果的Vector
	private Vector<Vector> v=new Vector<Vector>();
	//根据学号获得学生信息
	public Vector getAllStuInfo(String keyWord,String way,String sRseult)
	{//将Vector清空，防止有冗余记录
		v.removeAllElements();
		try
		{//查询数据库，将数据存入Vector v并返回
			rs = DButil.searchStuInfo(keyWord,way,sRseult);
			while(rs.next())
			{			
				Vector temp=new Vector();
				String[] data = {"","","","","","待定"};//stuID,stuName,collName,deptName,className,roomID	
				for(int i = 0;i < 5;i++)
				{
					data[i] = rs.getString(i+1).trim();
				}
				ResultSet rs2 = DButil.searchRoomInfoWay2(data[0]);
				if(rs2.next())
				{					
					data[5] = rs2.getString(2).trim();
					rs2.close();
				}
				for(int i = 0;i <= 5;i++)
				{
					temp.add(data[i]);
				}
				v.add(temp);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return v;
	}
	
}
