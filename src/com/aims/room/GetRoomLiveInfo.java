package com.aims.room;

import java.sql.ResultSet;
import java.util.Vector;

import com.aims.DButil.DButil;

public class GetRoomLiveInfo {
	private ResultSet rs1;
	private ResultSet rs2;
	//������ŷ��ؽ����Vector
	private Vector<Vector> v=new Vector<Vector>();
	//����ѧ�Ż��ѧ�����޿γ���Ϣ�ķ���
	public Vector getRoomLiveInfo(String aparID)
	{//��Vector��գ���ֹ�������¼
		v.removeAllElements();
		try
		{//��ѯ���ݿ⣬�����ݴ���Vector v������
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