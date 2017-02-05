package com.aims.room;

import java.sql.ResultSet;
import java.util.Vector;

import com.aims.DButil.DButil;

public class GetRoomInfo {
	private ResultSet rs;
	private String aparID;
	//创建存放返回结果的Vector
	private Vector<Vector> v=new Vector<Vector>();
	//根据学号获得学生已修课程信息的方法
	public Vector getAllRoomInfo(String keyWord)
	{//将Vector清空，防止有冗余记录
		v.removeAllElements();
		try
		{//查询数据库，将数据存入Vector v并返回
			rs = DButil.searchAllRoomInfo(keyWord,aparID);
			while(rs.next())
			{			
				String[] data = {"","暂无","暂无","暂无","暂无"};//stuID,stuName,collName,deptName,className,roomID					
				Vector temp=new Vector();
				String roomID = rs.getString(1);
				String stuID = rs.getString(2);
				int j = 0;
				data[j] = roomID;
				data[++j] = stuID;
				while(rs.next())
				{
					if(roomID.equals(rs.getString(1).trim())){
						j++;
						data[j] = rs.getString(2);
						System.out.println(j+" "+data[j]);
					}
					else
						break;
				}
				rs.previous();
				for(int i = 0;i < 5;i++)
				{
					if(data[i] == null)
						data[i] = "暂无";
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
	public GetRoomInfo(String aparID)
	{
		this.aparID = aparID;
	}
}
