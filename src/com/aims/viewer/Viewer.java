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
	   //初始化页面
		this.initialFrame();
		//为按钮注册监听器
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
	                 "您确认要退出系统吗？","退出",
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

	//初始化页面的方法
	public void initialFrame()
	{   
		this.add(jsp);
		jsp.setDividerSize(2);
		jsp.setDividerLocation(250);
		jsp.setEnabled(false);
		this.setTitle("访问登记");
		Image image=new ImageIcon("./images/AIMS.png").getImage();  
		this.setIconImage(image);
		Dimension screenSize = Toolkit.
	             getDefaultToolkit().getScreenSize();
		int centerX=screenSize.width/2;
		int centerY=screenSize.height/2;
		int w=1150;//本窗体宽度
		int h=650;//本窗体高度
		//设置窗体出现在屏幕中央
		this.setBounds(centerX-w/2,centerY-h/2-30,w,h);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public static void main(String[] args)
	{
		new Viewer();
	}
}
