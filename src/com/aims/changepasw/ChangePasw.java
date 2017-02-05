package com.aims.changepasw;

import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.*;
import com.aims.DButil.DButil;
public class ChangePasw extends JLabel implements ActionListener
{ 
	
	private static final long serialVersionUID = -4203520323357532827L;
	private String person_id;
	//������Ϣ��ʾ��ǩ����
	private JLabel[] jLabel={
			new JLabel("�û���"),new JLabel("ԭʼ����"),
			new JLabel("������"),new JLabel("ȷ��������")
			};
	private JLabel[] jLabel1={
			new JLabel(""),new JLabel(""),
			new JLabel(""),new JLabel("")
			};
	//�����û��������
	private JTextField userID=new JTextField();
	//�������������
	private JPasswordField[] jPaswField={
			new JPasswordField(),
	        new JPasswordField(),
	        new JPasswordField()
			};
	//����������ť
	private JButton[] jButton={new JButton("ȷ��"),new JButton("����")};
	//������                          
	public ChangePasw(String person_id)
	{ 
		this.person_id = person_id;
	   //��ʼ��ҳ��
		this.initialFrame();
		//Ϊ��ťע�������
		this.addListener();
	}
	public void addListener()
	{   //Ϊ�ı���ע�������
	    userID.addActionListener(this);
	    //Ϊ�����ע�������
	    jPaswField[0].addActionListener(this);
	    jPaswField[1].addActionListener(this);
	    jPaswField[2].addActionListener(this);
		userID.setText(person_id);
		userID.setEditable(false);
		
	    //Ϊ��ťע�������
		jButton[0].addActionListener(this);
		jButton[1].addActionListener(this);
	}
	//��ʼ��ҳ��ķ���
	public void initialFrame()
	{   //��Ϊ�ղ���
		this.setLayout(null);
		//���ؼ�������Ӧ��λ��
		for(int i=0;i<jLabel.length;i++)
		{
			jLabel[i].setBounds(230,170+50*i,150,30);
			this.add(jLabel[i]);
			jLabel1[i].setBounds(480,170+50*i,150,30);
			this.add(jLabel1[i]);
			if(i==0)
			{
				userID.setBounds(330,170+50*i,150,30);
				this.add(userID);
			}
			else
			{
				jPaswField[i-1].setBounds(330,170+50*i,150,30);
				this.add(jPaswField[i-1]);
			}
			this.setOpaque(false);
		}
		for(int i = 0;i < jButton.length;i++)
		{
			jButton[i].setBounds(230+i*150,380,100,30);
			jButton[i].setFocusPainted(false);
			jButton[i].setOpaque(false);
			this.add(jButton[i]);
		}
		
	}
	//ʵ��ActionListener�ӿ��еķ���
	public void actionPerformed(ActionEvent e)
	{
		
		if(e.getSource()==userID)//�����û������س���
		{
			jPaswField[0].requestFocus(true);
		}
		else if(e.getSource()==jPaswField[0])//����ԭʼ���벢�س���
		{
			jPaswField[1].requestFocus(true);
		}
		else if(e.getSource()==jPaswField[1])//���������벢�س���
		{
			jPaswField[2].requestFocus(true);
		}
		else if(e.getSource()==jPaswField[2])//����ȷ�������벢�س���
		{
			jButton[0].requestFocus(true);
		}
		else if(e.getSource()==jButton[1])
		{////�������ð�ť�Ĵ������
		     //��������Ϣ���
			resetPaswfileds();
		}
		else if(e.getSource()==jButton[0])
		{
			//����ȷ�ϰ�ť�Ĵ������
		    //�����ж������ʽ������ʽ�ַ���
			String patternStr="[0-9a-zA-Z]{6,12}";
			String user_id=userID.getText().trim();//��ȡ�û���
			if(user_id.equals(""))
			{//�û���Ϊ��
				JOptionPane.showMessageDialog(this,"�������û���",
				                "����",JOptionPane.ERROR_MESSAGE);
				return;
			}
			String oldPwd=new String(jPaswField[0].getPassword()).trim();//��ȡԭʼ����
			if(oldPwd.equals(""))
			{//ԭʼ����Ϊ��
				JOptionPane.showMessageDialog(this,"������ԭʼ����",
				                  "����",JOptionPane.ERROR_MESSAGE);
				return;
			}
			else
			{
				ResultSet rs = DButil.getLoginInfo(person_id);
				try {
					if(rs.next())
					{
						if(!rs.getString(2).equals(oldPwd))
						{
							jLabel1[1].setText("ԭʼ�����������");
							return;
						}
						else
							jLabel1[1].setText("");
					}
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
			String newPwd=new String(jPaswField[1].getPassword()).trim();//��ȡ������
			if(newPwd.equals(""))
			{//������Ϊ��
				JOptionPane.showMessageDialog(this,"������������",
				                "����",JOptionPane.ERROR_MESSAGE);
				return;
			}
			if(!newPwd.matches(patternStr))
			{//�����벻���ϸ�ʽ
				JOptionPane.showMessageDialog(this,
				                  "����ֻ����6��12λ����ĸ������",
				                  "����",JOptionPane.ERROR_MESSAGE);
				return;
			}
			String newPwd1=new String(jPaswField[2].getPassword()).trim();//��ȡȷ������
			if(!newPwd.equals(newPwd1))
			{
				this.jLabel1[3].setText("ȷ�������������벻��");
				return;
			}
			else
			{
				this.jLabel1[3].setText("");
			}
			try
			{   //��ʼ�����ݿ����Ӳ���������
				if(DButil.updatePasw(user_id, newPwd))
				{//���ĳɹ�
					resetPaswfileds();
					JOptionPane.showMessageDialog(this,"�����޸ĳɹ�",
					           "��ʾ",JOptionPane.INFORMATION_MESSAGE);
				}
				else
				{//����ʧ��
					JOptionPane.showMessageDialog(this,
					      "�޸�ʧ�ܣ����������û����������Ƿ���ȷ",
					      "����",JOptionPane.ERROR_MESSAGE);
				}
			}
			catch(Exception e1){
				e1.printStackTrace();
			}
		}
	}
	private void resetPaswfileds() {
		for(int i=0;i<jPaswField.length;i++)
		{
			jPaswField[i].setText("");
		}
	}
	public void setFocus()
	{
		userID.requestFocus(true);
	}
	
	public static void main(String[] args)
	{
		ChangePasw cpt=new ChangePasw("U201215072");
		JFrame jframe=new JFrame();
		jframe.add(cpt);
		jframe.setBounds(70,20,700,650);
		jframe.setVisible(true);
	}
}