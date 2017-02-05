package com.aims.viewer;

import java.sql.ResultSet;
import java.util.Vector;

import com.aims.DButil.DButil;

public class GetRecordInfo {
	private ResultSet rs;
	//创建存放返回结果的Vector
	private Vector<Vector> v=new Vector<Vector>();
	//根据学号获得学生已修课程信息的方法
	public Vector getAllRecordInfo(String apar_id,String[] dateKeyWord)
	{//将Vector清空，防止有冗余记录
		v.removeAllElements();
		try
		{//查询数据库，将数据存入Vector v并返回
			rs = DButil.searchRecordInfo(apar_id,dateKeyWord);
			while(rs.next())
			{			
				Vector temp=new Vector();
				String[] data = {"","","","",""};//ap_id,stu_id	
				for(int i = 0;i < 5;i++)
				{
					data[i] = rs.getString(i+2).trim();
				}
				System.out.println(data[4]);
				for(int i = 0;i < 4;i++)
				{
					temp.add(data[i]);
				}
				temp.add(data[4].split("\\.")[0]);
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
