package com.aims.viewer;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

import com.aims.DButil.DButil;
public class Viewer extends JFrame 
{ 
	/**
	 * 
	 */
	private static final long serialVersionUID = 3764204389310742875L;

	static {
		try {
			UIManager.setLookAndFeel(
			         UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
	}
	private JPanel jp1 = new Register();
	private JPanel jp2 = new Record();
	private JSplitPane jsp = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,jp1,jp2){
		private static final long serialVersionUID = 7736386233887142381L;

		public void paintComponent(Graphics g) {
			 String path = "./images/background.jpg";
			 super.paintComponent(g);
			 ImageIcon img = new ImageIcon(path);
			 g.drawImage(img.getImage(), 0, 0, null);
			 }
	};
	
	public Viewer()
	{ 
	   //��ʼ��ҳ��
		this.initialFrame();
		//Ϊ��ťע�������
		this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		this.addWindowListener(new WindowAdapter(){ 
		    public void windowClosing(WindowEvent e){ 
		    	quitSystem();
		    } 
		    
		});
	}
	
	public void quitSystem()
	{
		int i = JOptionPane.showConfirmDialog(jsp,
	                 "��ȷ��Ҫ�˳�ϵͳ��","�˳�",
	                  JOptionPane.YES_NO_OPTION,
	                   JOptionPane.QUESTION_MESSAGE);
		System.out.println("iiiiii"+i);
  		if(i == JOptionPane.YES_OPTION)
  		{
  			System.exit(0);
  		}
  		else
  			return;
	}

	//��ʼ��ҳ��ķ���
	public void initialFrame()
	{   
		this.add(jsp);
		jsp.setDividerSize(2);
		jsp.setDividerLocation(250);
		jsp.setEnabled(false);
		this.setTitle("���ʵǼ�");
		Image image=new ImageIcon("./images/AIMS.png").getImage();  
		this.setIconImage(image);
		Dimension screenSize = Toolkit.
	             getDefaultToolkit().getScreenSize();
		int centerX=screenSize.width/2;
		int centerY=screenSize.height/2;
		int w=1150;//��������
		int h=650;//������߶�
		//���ô����������Ļ����
		this.setBounds(centerX-w/2,centerY-h/2-30,w,h);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new Viewer();
	}
}
