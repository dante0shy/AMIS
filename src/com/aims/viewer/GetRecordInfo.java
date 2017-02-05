package com.aims.viewer;

import java.sql.ResultSet;
import java.util.Vector;

import com.aims.DButil.DButil;

public class GetRecordInfo {
	private ResultSet rs;
	//������ŷ��ؽ����Vector
	private Vector<Vector> v=new Vector<Vector>();
	//����ѧ�Ż��ѧ�����޿γ���Ϣ�ķ���
	public Vector getAllRecordInfo(String apar_id,String[] dateKeyWord)
	{//��Vector��գ���ֹ�������¼
		v.removeAllElements();
		try
		{//��ѯ���ݿ⣬�����ݴ���Vector v������
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
