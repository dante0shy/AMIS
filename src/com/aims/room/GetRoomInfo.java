package com.aims.room;

import java.sql.ResultSet;
import java.util.Vector;

import com.aims.DButil.DButil;

public class GetRoomInfo {
	private ResultSet rs;
	private String aparID;
	//������ŷ��ؽ����Vector
	private Vector<Vector> v=new Vector<Vector>();
	//����ѧ�Ż��ѧ�����޿γ���Ϣ�ķ���
	public Vector getAllRoomInfo(String keyWord)
	{//��Vector��գ���ֹ�������¼
		v.removeAllElements();
		try
		{//��ѯ���ݿ⣬�����ݴ���Vector v������
			rs = DButil.searchAllRoomInfo(keyWord,aparID);
			while(rs.next())
			{			
				String[] data = {"","����","����","����","����"};//stuID,stuName,collName,deptName,className,roomID					
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
						data[i] = "����";
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
