package com.aims.room;

import java.sql.ResultSet;
import java.util.Vector;

import com.aims.DButil.DButil;

public class GetLiveOrNot {
	private ResultSet rs;
	//������ŷ��ؽ����Vector
	private Vector<Vector> v=new Vector<Vector>();
	//����ѧ�Ż��ѧ�����޿γ���Ϣ�ķ���
	public Vector getRoomLiveInfo(int index, String aparID)
	{//��Vector��գ���ֹ�������¼
		v.removeAllElements();
		try
		{//��ѯ���ݿ⣬�����ݴ���Vector v������
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