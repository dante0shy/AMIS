package com.aims.admin;

import java.sql.ResultSet;
import java.util.Vector;

import com.aims.DButil.DButil;

public class GetAdminInfo {
	private ResultSet rs;
	//������ŷ��ؽ����Vector
	private Vector<Vector> v=new Vector<Vector>();
	//����ѧ�Ż��ѧ�����޿γ���Ϣ�ķ���
	public Vector getAdminInfo(int index)
	{//��Vector��գ���ֹ�������¼
		v.removeAllElements();
		try
		{//��ѯ���ݿ⣬�����ݴ���Vector v������
			rs = DButil.searchAllAdminInfo(index);
			while(rs.next())
			{		
				String[] data = {"","","","null"};
				Vector temp=new Vector();
				for(int i = 0;i < data.length;i++)
				{
					if(rs.getString(i+1) != null)
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
