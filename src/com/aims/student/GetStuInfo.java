package com.aims.student;

import java.sql.ResultSet;
import java.util.Vector;

import com.aims.DButil.DButil;

public class GetStuInfo {
	private ResultSet rs;
	//������ŷ��ؽ����Vector
	private Vector<Vector> v=new Vector<Vector>();
	//����ѧ�Ż��ѧ����Ϣ
	public Vector getAllStuInfo(String keyWord,String way,String sRseult)
	{//��Vector��գ���ֹ�������¼
		v.removeAllElements();
		try
		{//��ѯ���ݿ⣬�����ݴ���Vector v������
			rs = DButil.searchStuInfo(keyWord,way,sRseult);
			while(rs.next())
			{			
				Vector temp=new Vector();
				String[] data = {"","","","","","����"};//stuID,stuName,collName,deptName,className,roomID	
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
