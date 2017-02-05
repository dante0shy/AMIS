package com.aims.DButil;

import java.util.*;
import java.util.regex.Pattern;
import java.sql.Date;
import java.sql.*;
import java.io.*;
import java.awt.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
public class DButil
{
	private static final String AIMShost = "localhost:1433" ;
	private static final String userName = "sa";//asenwang
	private static final String userPasw = "shi4712ak4712";	//admin
	private static final String driverName = "com.microsoft.sqlserver.jdbc.SQLServerDriver";	//声明驱动类字符串
	private static String dbURL = "jdbc:sqlserver://"+AIMShost+";DatabaseName=AIMS";	//声明数据库连接字符串
	private static Connection con = null;				//声明数据库连接对象引用
	private static Statement stat = null;				//声明语句对象引用
	private static PreparedStatement psInsert = null;	//声明预编译语句对象引用
	private static ResultSet rs = null;					//声明结果集对象引用
	private static Vector picInfo = new Vector<>();
	public static String Power = "1";
	public static String personID ="";
	public final static String Student =	"1";
	public final static String Admin =		"2";
	public final static String SuperAdmin = "3";
	public static Connection getConnection()			//得到数据库连接的方法
	{
		try
		{
			Class.forName(driverName);//加载驱动类
			con = DriverManager.getConnection(dbURL,userName,userPasw);//得到连接
			System.out.println("OK");
		}
		catch(Exception e){e.printStackTrace();}
		return con;//返回连接
	}
	
	public static Connection getConnectionMaster()			//得到数据库连接的方法
	{
		try
		{
			String url ="jdbc:sqlserver://"+AIMShost+";DatabaseName=master";
			Class.forName(driverName);//加载驱动类
			con = DriverManager.getConnection(url,userName,userPasw);//得到连接
			System.out.println("OK");
		}
		catch(Exception e){e.printStackTrace();}
		return con;//返回连接
	}
	
	public static void closeCon()//关闭数据库连接的方法
	{
		try
		{
			if(rs!=null){rs.close(); rs=null;}//如果结果集不为空关闭结果集并赋值null
			if(stat!=null){stat.close(); stat=null;}//如果语句对象不为空关闭语句对象并赋值null
			if(con!=null){con.close(); con=null;}//如果连接不为空关闭连接并赋值null				
		}
		catch(Exception e){e.printStackTrace();}
	}
	
	public static String getStuLiveRoom(String stu_id)
	{
		String liveRoom = "0 0";
		String sql = "select ap_id,room_id from Live where stu_id = '"+stu_id+"';";
		con = getConnection();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			System.out.println(rs);
			if(rs.next())
			{
				liveRoom = rs.getString(1).trim()+" "+rs.getString(2).trim();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			closeCon();
		}
		return liveRoom;
	}
	
	public static String getAdminManaApar(String admin_id)
	{
		String manaApar = "0";
		String sql = "select ap_id from Management where admin_id = '"+admin_id+"';";
		con = getConnection();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			System.out.println(rs);
			if(rs.next())
			{
				manaApar = rs.getString(1).trim();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally{
			closeCon();
		}
		return manaApar;
	}
	public static ResultSet searchStuInfo(String person_id,String way,String sRseult)
	{
		String sql = "select stu_id,stu_name,coll_name,dept_name,class_name,case stu_sex when '1' then '男' when '0' then '女' end" +
				",nativeplace,home_addr,format(stu_birth,'yyyy-MM-dd'),stu_phone,College.coll_id,Dept.dept_id,Class.class_id"+
				" from Student,College,Dept,Class where "+way+" like '%"+person_id+
				"%' and Student.coll_id = College.coll_id and Student.dept_id = Dept.dept_id" +
				" and Student.class_id = Class.class_id";
		String sort =" order by "+sRseult+";";
		if(!sRseult.equals(""))
			sql = sql + sort;
		con = getConnection();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			System.out.println(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		return rs;
	}
	public static Image getPic(String picID)
	{
		String sql ="select Pic from PIC where ID = '"+picID+"'";
		System.out.println(sql);
		Image i=null;//声明Image对象引用
		try
		{
			con = getConnection();//得到数据库连接
			stat=con.createStatement();//创建语句对象
			rs=stat.executeQuery(sql);//执行SQL语句
			if(rs.next())
			{
				byte[] buff=rs.getBytes(1);//得到图像数据
				if(buff!=null)//如果数据存在
				{
					i=(new ImageIcon(buff)).getImage();//转换成ImageIcon对象
				} 				
			}
			else
				i = getPic("N");
		}
		catch(Exception e)
		{
			e.printStackTrace();//打印异常信息
		}
		finally{
			closeCon();
		}//关闭数据库连接
		return i;
	}
	
	public static void insertPic(String pic_id,String fileName)
	{
		String sql = "insert Pic(ID,PIC) values(?,?);";
		con = getConnection();
		try {	
			psInsert = con.prepareStatement(sql);
			File file = new File(fileName);
			byte[] b = new byte[(int)file.length()];
			FileInputStream fin=new FileInputStream(file);
			fin.read(b);
			fin.close();//读取文件存于byte数组中并关闭输入流	
			psInsert.setString(1,pic_id);
			psInsert.setBytes(2,b);
 			psInsert.execute();
			psInsert.close();//执行更新并关闭语句
		} catch (SQLException e) {
			updatePic(pic_id, fileName);
		} catch (Exception e) {
			System.out.println("error insert pic");
		} finally{
			closeCon();
		}
	}
	
	public static void updatePic(String person_id ,String fileName)
	{
		String sql = "update Pic set PIC = ? where ID = '"+person_id+"'";
		con = getConnection();
		try {
			System.out.println("pic update in");
			psInsert = con.prepareStatement(sql);
			File file = new File(fileName);
			byte[] b = new byte[(int)file.length()];
			FileInputStream fin=new FileInputStream(file);
			fin.read(b);
			fin.close();//读取文件存于byte数组中并关闭输入流					
 			psInsert.setBytes(1,b);
 			psInsert.execute();
			psInsert.close();//执行更新并关闭语句
		} catch (SQLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			System.out.println("error update pic");
		}
		finally{
			closeCon();
		}
	}
	
	public static void updateStuInfo(Vector<String> vector){
		String[] stuinfo =new String[vector.size()]; 
		for(int i = 0;i < vector.size();i++)
		{
			stuinfo[i] = vector.get(i);
			System.out.println(i+"---"+stuinfo[i]);
		}
		String sql ="update Student set stu_id = ?,stu_name = ?,coll_id = ?,dept_id = ?," +
					"class_id = ?,stu_sex = ?,nativeplace = ?,home_addr = ?," +
					"stu_birth = ?,stu_phone = ? where stu_id = '"+stuinfo[0]+"';";
//					"update Student set coll_id = ? where coll_id = '"+stuinfo[10]+"';" +
//					"update Student set dept_id = ? where dept_id = '"+stuinfo[11]+"';" +
//					"update Student set class_id = ? where class_id = '"+stuinfo[12]+"';" +
//					"update Student set  stu_sex = ?,nativeplace = ?,home_addr = ?," +
//					"stu_birth = ?,stu_phone = ? where stu_id = '"+stuinfo[0]+"';";
		try {
			con = getConnection();
			psInsert = con.prepareStatement(sql);
			System.out.println(sql);
			if(stuinfo[5].equals("男"))
				psInsert.setString(6, "1");
			else if(stuinfo[5].equals("女"))
				psInsert.setString(6, "0");
			else{
				JOptionPane.showMessageDialog(null, "性别需为\"男|女\"");
				return ;
			}
			if(!isMobileNumber(stuinfo[9]))
			{
				JOptionPane.showMessageDialog(null, "需填正确的手机号码");
				return ;
			}
			else
				psInsert.setString(10,stuinfo[9]);
			String date = vector.get(8);
			psInsert.setDate(9,Date.valueOf(date));
			System.out.println(date);
			int i = 1;
			for(;i < 3;i++)
			{
				psInsert.setString(i, stuinfo[i-1]);
			}
			for(;i < 6;i++)
			{
				System.out.println((7+i)+"<--->"+stuinfo[7+i]);
				psInsert.setString(i, stuinfo[7+i]);
			}
			for(i = 7;i < 9;i++)
				psInsert.setString(i,stuinfo[i-1]);
			psInsert.execute();
			psInsert.close();//执行更新并关闭语句
			JOptionPane.showMessageDialog(null, "修改成功");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "请检查出生日期是否填写正确");
		} finally{
			closeCon();
		}
	}
	private static boolean isLandlineNumber(String input){
		String regex = "^(010|02\\d|0[3-9]\\d{2})?\\d{6,8}$";
		return Pattern.matches(regex,input);
	}
	private static boolean isMobileNumber(String input){
		String regex = "1[3|5|7|8|][0-9]{9}";
		return Pattern.matches(regex,input);
	}
	public static ResultSet getLoginInfo(String person_id)
	{	
		String sql="select * from Pasw where ID='"+person_id+"';";
		System.out.println(sql);
		con = getConnection();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			System.out.println(rs+" getLogin in");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static boolean loginSys(String person_id)
	{
		String sql = "update Pasw set online = '1' where ID = '"+person_id+"'";
		try {
			con = getConnection();
			stat = con.createStatement();
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			return false;
		} finally{
			closeCon();
		}
		return true;
	}
	public static boolean exitSys(String person_id)
	{
		String sql = "update Pasw set online = '0' where ID = '"+ person_id +"'";
		try {
			con = getConnection();
			stat = con.createStatement();
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally
		{
			closeCon();
		}
		return true;
	}
	
	public static boolean updatePasw(String person_id,String newPwd)
	{
		String sql="update Pasw set pasw='"+newPwd+"'"+
	            " where ID='"+person_id+"';";
		try {
			con = getConnection();
			stat = con.createStatement();
			stat.executeUpdate(sql);
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		} finally
		{
			closeCon();
		}
		return true;
	}
	public static ResultSet searchRoomInfoWay2(String roomKeyWord) {
		String sql = "select ap_name,Room.room_id,elec_cons,Room.ap_id,room_note from Live,Room,Apartment"+
				" where Live.stu_id = '"+roomKeyWord+"' and Live.ap_id = Apartment.ap_id"+
					" and Live.ap_id = Room.ap_id and Live.room_id = Room.room_id;";
		System.out.println(sql);
		con = getConnection();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			System.out.println(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static ResultSet searchRoomInfoWay1(String roomKeyWord) {
		String[] s = roomKeyWord.trim().split(" ");
		String sql = "Select ap_name,room_id,elec_cons,Room.ap_id,room_note from Room,Apartment " +
					  " where Apartment.ap_id = '"+s[0]+"'and room_id = '"+s[1]+"' and Room.ap_id = Apartment.ap_id ";		
		System.out.println(sql);
		con = getConnection();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			System.out.println(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static Image reSizePic(Image Pic,JPanel jPanel,int width,int height)
	{
		int pw = Pic.getWidth(jPanel);//得到此人图像的宽度
		int ph = Pic.getHeight(jPanel);//得到此人图像的高度
		if(pw>ph)
		{//宽度大于高度，图像进行缩放
			Pic = Pic.getScaledInstance(width,width*ph/pw,Image.SCALE_SMOOTH);			
		}
		else
		{//高度大于宽度，图像进行缩放
			Pic = Pic.getScaledInstance(height*pw/ph,height,Image.SCALE_SMOOTH);				
		}
		return Pic;
	}
	public static void setPic(Image Pic,JLabel jlPhoto,JPanel jPanel,int width,int height)//设置个人图像显示
	{
//		final int width=150;//显示图像的JLabel的宽度
//		final int height=170;//显示图像的JLabel的高度
		if(Pic!=null)
		{//如果此联系人上传了图像
			Pic = reSizePic(Pic,jPanel,width,height);
			jlPhoto.setIcon(new ImageIcon(Pic));//将图像显示到JLabel
			jlPhoto.setHorizontalAlignment(JLabel.CENTER);//设置图片水平居中显示
			jlPhoto.setVerticalAlignment(JLabel.CENTER);//设置图片垂直方向居中显示
		}
		else
		{//如果图像为空，则不显示
			jlPhoto.setIcon(new ImageIcon("./"));
		}	
	}
	public static ResultSet searchAparInfo(String aparID) {
		String sql = "select ap_name,ap_addr,ap_note,ap_id from Apartment"+
					 " where ap_id = '"+aparID+"';";		
		System.out.println(sql);
		con = getConnection();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			System.out.println(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static Vector choosePic(JPanel jpanel) {
		picInfo.clear();
		String FileName = "";
		JFileChooser jfc = null;
		jfc = new JFileChooser(".//");
		FileNameExtensionFilter filter = new FileNameExtensionFilter(
		        "*.jpg *.jpeg *.gif *.png", "jpg", "jpeg", "png","gif");
		jfc.setFileFilter(filter);
		int result = jfc.showOpenDialog(jpanel); 
		if (result == JFileChooser.APPROVE_OPTION) {
			FileName = jfc.getSelectedFile().getAbsolutePath();
			File pic = new File(FileName);
			ImageIcon icon = new ImageIcon(FileName);
			Image icon1 = icon.getImage();
			if(pic.length()/1024.0 > 2048)//判断图片大小
				JOptionPane.showMessageDialog(null,"图片大小超出2M");
			else if(icon.getIconHeight()<170 || icon.getIconWidth()<150)
				JOptionPane.showMessageDialog(null,"图片尺寸过小");
			else if(icon.getIconHeight()>2048 || icon.getIconWidth()>2048)
				JOptionPane.showMessageDialog(null,"图片尺寸过大");
			else
			{
				Image Pic = new ImageIcon(FileName).getImage();
				Pic = reSizePic(Pic,new JPanel(), 170, 150);
				int request = JOptionPane.showConfirmDialog(null, "用此图片替换原图？", "图片", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE, new ImageIcon(Pic));
				if(request == JOptionPane.YES_OPTION)	
				{
					picInfo.add(icon1);
					picInfo.add(FileName);
				}
			}
		}
		return picInfo;
	}
	
	public static ResultSet searchAllRoomInfo(String keyWord,String aparID) {
		String sql = "select Room.room_id,stu_id from Room left join Live on Room.ap_id = Live.ap_id " +
				"and Room.room_id = Live.room_id where Room.room_id like '%"+keyWord+"%' and Room.ap_id = '"+aparID+"';";

		System.out.println(sql);
		con = getConnection();
		try {
			stat = con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
			rs = stat.executeQuery(sql);
			System.out.println(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static ResultSet getAparInfo()
	{
		String sql = "select ap_name,ap_id from Apartment order by ap_id desc;";
		System.out.println(sql);
		con = getConnection();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			System.out.println(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static ResultSet getCollegeInfo()
	{
		String sql = "select coll_name,coll_id from College order by coll_name asc;";
		System.out.println(sql);
		con = getConnection();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			System.out.println(rs);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static String createAparID(String apar_id) {
		System.out.println("create id in");
		String sql = "select max(convert(int,view_id)) from Viewer where ap_id ='"+apar_id+"';";
		String view_id = "";
		con = getConnection();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			if(rs.next())
			{
				int id = rs.getInt(1);
				id++;
				String ID = String.valueOf(id);
				System.out.println(id);
				if(ID == null)
				{
					view_id ="00000";			
				}
				else
				{
					for(int i = ID.length();i < 5;i++)
					{
						view_id += "0";
					}
					view_id = view_id + id;
				}
			}
		} catch (SQLException e) {
			view_id ="00000";
		}
		System.out.println(view_id);
		return view_id;
	}
	
	public static boolean isStuInThisApar(String stu_id,String apar_id)//stu_id apar_id 
	{
		boolean check = true;
		System.out.println("check in");
		String sql = "select stu_id from Live where ap_id = '"+apar_id+"' and stu_id = '"+stu_id+"';";
		System.out.println(sql);
		con = getConnection();
		try {
			System.out.println("try in");
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			System.out.println("query done");
			if(!rs.next())
			{
				check = false;
			}
		} catch (SQLException e) {
		} 
		return check;
	}
	
	public static boolean updateViewInfo(Vector<String> viewInfo) {
		String[] viewinfo =new String[viewInfo.size()]; 
		for(int i = 0;i < viewInfo.size();i++)
		{
			viewinfo[i] = viewInfo.get(i);
		}
		String sql = "insert into Viewer values(?,?,?,?,?,?);";
//		String sql = "insert Viewer(ap_id,view_id,view_stuid,view_name,view_event,view_stime) " +
//				"values(?,?,?,?,?,?);";
		con = getConnection();
		try{
			psInsert = con.prepareStatement(sql);
			for(int i = 0;i < viewinfo.length;i++)
			{
				psInsert.setString(i+1, viewinfo[i]);
				System.out.println(viewinfo[i]);
			}
			psInsert.execute();
			psInsert.close();//执行更新并关闭语句
			JOptionPane.showMessageDialog(null,"登记成功");
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,"登记失败");
			return false;
		} finally{
			closeCon();
		}
	}
	public static ResultSet searchRecordInfo(String apar_id, String[] dateKeyWord) {
		String sql = "select * from Viewer where ap_id = '"+apar_id+"' and format(view_stime,'yyyy-MM-dd') >= '"+dateKeyWord[0]+"' " +
					 "and format(view_stime,'yyyy-MM-dd') <= '"+dateKeyWord[1]+"';";
		con = getConnection();
		System.out.println(sql);
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public static void updateRoomInfo(Vector<String> vector){
		String[] roominfo =new String[vector.size()]; 
		for(int i = 0;i < vector.size();i++)
		{
			roominfo[i] = vector.get(i);
			System.out.println(roominfo[i]);
		}
		String sql = "update Room set elec_cons = ?, room_note = ? where ap_id = '"+roominfo[0]+"' and room_id = '"+roominfo[2]+"';";
		System.out.println(sql);			
		try {
			con = getConnection();
			psInsert = con.prepareStatement(sql);
			psInsert.setString(1,roominfo[3]);
			psInsert.setString(2,roominfo[4]);
			psInsert.execute();
			psInsert.close();//执行更新并关闭语句
			JOptionPane.showMessageDialog(null, "修改成功");
		} catch (Exception e) {
			
		} finally{
			closeCon();
		}
	}
	
	
	public static void updateAparInfo(Vector<String> vector) {
		String[] aparinfo =new String[vector.size()]; 
		for(int i = 0;i < vector.size();i++)
		{
			aparinfo[i] = vector.get(i);
			System.out.println(aparinfo[i]);
		}
		String sql = "update Apartment set ap_addr = ?, ap_note = ? where ap_id = '"+aparinfo[0]+"';";
		System.out.println(sql);			
		try {
			con = getConnection();
			psInsert = con.prepareStatement(sql);
			psInsert.setString(1,aparinfo[2]);
			psInsert.setString(2,aparinfo[3]);
			psInsert.execute();
			psInsert.close();//执行更新并关闭语句
			JOptionPane.showMessageDialog(null, "修改成功");
		} catch (Exception e) {
			
		} finally{
			closeCon();
		}
	}
	
	public static boolean addApar(Vector<String> vector) {
		String[] aparinfo =new String[vector.size()]; 
		for(int i = 0;i < vector.size();i++)
		{
			aparinfo[i] = vector.get(i);
			System.out.println(aparinfo[i]);
		}
		String sql = "insert into Apartment values(?,?,?,?);";
		System.out.println(sql);			
		try {
			con = getConnection();
			psInsert = con.prepareStatement(sql);
			for(int i=0;i<aparinfo.length;i++){
				psInsert.setString(i+1,aparinfo[i]);
			}
			psInsert.setString(4,"");
			psInsert.execute();
			psInsert.close();//执行更新并关闭语句
			JOptionPane.showMessageDialog(null, "添加成功");
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "楼栋信息存在重复");
			return false;
		} finally{
			closeCon();
		}
	}
	
	public static ResultSet searchAdminInfo(String admin_id) {
		String sql = "select admin_id,admin_name,admin_job,case admin_sex when '1' then '男' when '0' then '女' end " +
					 ",nativeplace,format(admin_birth,'yyyy-MM-dd'),admin_phone from Admin where admin_id = '"+admin_id+"';";
		con = getConnection();
		System.out.println(sql);
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static void updateAdminInfo(Vector<String> vector) {
		String[] admininfo =new String[vector.size()]; 
		for(int i = 0;i < vector.size();i++)
		{
			admininfo[i] = vector.get(i);
			System.out.println(admininfo[i]);
		}
		String sql = "update Admin set admin_id = ?, admin_name = ?, admin_job =?,admin_sex = ?, " +
					 "nativeplace = ?,admin_birth = ?,admin_phone = ? where admin_id = '"+admininfo[0]+"';";
		System.out.println(sql);			
		try {
			con = getConnection();
			psInsert = con.prepareStatement(sql);
			if(admininfo[3].equals("男"))
				psInsert.setString(4, "1");
			else if(admininfo[3].equals("女"))
				psInsert.setString(4, "0");
			else{
				JOptionPane.showMessageDialog(null, "性别需为\"男|女\"");
				return ;
			}
			System.out.println(admininfo[6]);
			if(!isLandlineNumber(admininfo[6]))
			{
				JOptionPane.showMessageDialog(null, "需填正确的座机号码");
				return ;
			}
			String date = vector.get(5);
			psInsert.setDate(6,Date.valueOf(date));
			System.out.println(date);
			for(int i = 0;i <= 6;i++){
				System.out.println(vector.get(i));
				if(i == 3||i == 5)	continue;
				psInsert.setString(i+1,vector.get(i));
			}
				
			psInsert.execute();
			psInsert.close();//执行更新并关闭语句
			JOptionPane.showMessageDialog(null, "修改成功");
		} catch (Exception e) {
			
		} finally{
			closeCon();
		}
	}
	public static ResultSet searchAllCollegeInfo() {
		String sql = "select * from College order by coll_id";
		con = getConnection();
		System.out.println(sql);
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public static ResultSet searchAllDeptInfo(String coll_id) {
		String sql = "select dept_id,dept_name from Dept where coll_id = '"+coll_id+"' order by dept_id";
		con = getConnection();
		System.out.println(sql);
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public static boolean addCollege(String id,String name) {
		String sql = "insert into College values(?,?);";
		try {
			con = getConnection();
			psInsert = con.prepareStatement(sql);
			psInsert.setString(1, id);
			psInsert.setString(2, name);
			psInsert.execute();
			psInsert.close();//执行更新并关闭语句
			JOptionPane.showMessageDialog(null, "添加成功");
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "院系已经存在");
			return false;
		} finally{
			closeCon();
		}
	}
	
	public static String createCollegeID() {
		System.out.println("create id in");
		String sql = "select max(convert(int,coll_id)) from College;";
		String coll_id = "";
		con = getConnection();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			if(rs.next())
			{
				int id = rs.getInt(1);
				id++;
				String ID = String.valueOf(id);
				System.out.println(id);
				if(ID == null)
				{
					coll_id ="00";			
				}
				else
				{
					for(int i = ID.length();i < 2;i++)
					{
						coll_id += "0";
					}
					coll_id = coll_id + id;
				}
			}
		} catch (SQLException e) {
			coll_id ="00";
		}
		System.out.println(coll_id);
		return coll_id;
	}
	public static void updateCollege(String id, String name) {
		String sql = "update College set coll_name = ? where coll_id = '"+ id +"';";
		try {
			con = getConnection();
			psInsert = con.prepareStatement(sql);
			psInsert.setString(1, name);
			psInsert.execute();
			psInsert.close();//执行更新并关闭语句
			JOptionPane.showMessageDialog(null, "修改成功");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "院系已经存在");
		} finally{
			closeCon();
		}
	}
	public static void deleteCollege(String id) {
		String sql = "delete from College where coll_id = '"+ id +"';";
		try {
			con = getConnection();
			stat = con.createStatement();
			stat.executeUpdate(sql);
			JOptionPane.showMessageDialog(null, "删除成功");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "删除失败,现有数据被引用");
		} finally{
			closeCon();
		}
	}

	public static String createDeptID() {
		System.out.println("create id in");
		String sql = "select max(convert(int,dept_id)) from Dept;";
		String dept_id = "";
		System.out.println(sql);
		con = getConnection();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			if(rs.next())
			{
				int id = rs.getInt(1);
				System.out.println("id ="+id);
				id++;
				String ID = String.valueOf(id);
				System.out.println(id);
				if(ID == null)
				{
					dept_id ="0000";			
				}
				else
				{
					for(int i = ID.length();i < 4;i++)
					{
						dept_id += "0";
					}
					dept_id = dept_id + id;
				}
			}
		} catch (SQLException e) {
			dept_id ="0000";
		}
		System.out.println(dept_id);
		return dept_id;
	}
	
	public static void addDept(String coll_id, String id, String name) {
		String sql = "insert into Dept values(?,?,?);";
		try {
			con = getConnection();
			psInsert = con.prepareStatement(sql);
			psInsert.setString(1, coll_id);
			psInsert.setString(2, id);
			psInsert.setString(3, name);
			psInsert.execute();
			psInsert.close();//执行更新并关闭语句
			JOptionPane.showMessageDialog(null, "添加成功");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "专业已经存在");
		} finally{
			closeCon();
		}
	}
	public static void deleteDept(String dept_id) {
		String sql = "delete from Dept where dept_id = '"+ dept_id +"';";
		System.out.println(sql);
		try {
			con = getConnection();
			stat = con.createStatement();
			stat.executeUpdate(sql);
			JOptionPane.showMessageDialog(null, "删除成功");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "删除失败,现有数据被引用");
		} finally{
			closeCon();
		}
	}
	public static void updateDept(String dept_id, String name) {
		String sql = "update Dept set dept_name =? where dept_id = '"+ dept_id +"';";
		try {
			con = getConnection();
			psInsert = con.prepareStatement(sql);
			psInsert.setString(1, name);
			psInsert.execute();
			psInsert.close();//执行更新并关闭语句
			JOptionPane.showMessageDialog(null, "修改成功");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "专业已经存在");
		} finally{
			closeCon();
		}
	}
	public static ResultSet getDeptInfo(String coll_id) {
		String sql = "select dept_id,dept_name from Dept where coll_id = '"+coll_id+"' order by dept_id";
		con = getConnection();
		System.out.println(sql);
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public static ResultSet searchClassInfo(String dept_id) {
		String sql = "select class_id,class_name from Class where dept_id = '"+dept_id+"' order by class_id";
		con = getConnection();
		System.out.println(sql);
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public static void deleteClass(String class_id) {
		String sql = "delete from Class where class_id = '"+class_id+"';";
		System.out.println(sql);
		try {
			con = getConnection();
			stat = con.createStatement();
			stat.executeUpdate(sql);
			JOptionPane.showMessageDialog(null, "删除成功");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "删除失败,现有数据被引用");
		} finally{
			closeCon();
		}
	}
	public static void addClass(String coll_id,String dept_id, String class_id) {
		String sql = "insert into Class values(?,?,?,?);";
		String class_name = class_id.substring(6, 10) +"班";
		System.out.println(coll_id+dept_id+class_id+class_name);
		try {
			con = getConnection();
			psInsert = con.prepareStatement(sql);
			psInsert.setString(1, coll_id);
			psInsert.setString(2, dept_id);
			psInsert.setString(3, class_id);
			psInsert.setString(4, class_name);
			psInsert.execute();
			psInsert.close();//执行更新并关闭语句
			JOptionPane.showMessageDialog(null, "添加成功");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "班级已经存在");
		} finally{
			closeCon();
		}
	}
	public static void updateClass(String old_class_id, String new_class_id) {
		String sql = "update Class set class_id =?,class_name = ? where class_id = '"+old_class_id+"';";
		String class_name = new_class_id.subSequence(6, 10) +"班";
		System.out.println(sql);
		try {
			con = getConnection();
			psInsert = con.prepareStatement(sql);
			psInsert.setString(1, new_class_id);
			psInsert.setString(2, class_name);
			psInsert.execute();
			psInsert.close();//执行更新并关闭语句
			JOptionPane.showMessageDialog(null, "修改成功");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "班级已经存在");
		} finally{
			closeCon();
		}
	}
	public static ResultSet searchAllAparInfo() {
		String sql = "select * from Apartment order by ap_id";
		con = getConnection();
		System.out.println(sql);
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public static void deleteApar(String apar_id) {
		String sql = "delete from Apartment where ap_id = '"+apar_id+"';";
		System.out.println(sql);
		try {
			con = getConnection();
			stat = con.createStatement();
			stat.executeUpdate(sql);
			JOptionPane.showMessageDialog(null, "删除成功");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "删除失败,现有数据被引用");
		} finally{
			closeCon();
		}
	}
	public static ResultSet searchRoomLiveInfo(String aparID) {
		String sql = "select room_id from Room where ap_id = '"+aparID+"';";
		con = getConnection();
		System.out.println(sql);
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	
	public static ResultSet searchRoomLiveNum(String aparID,String room_id) {
		String sql = "select count(*) from Live where ap_id = '"+aparID+"' and room_id = '"+room_id+"' " +
					 "group by room_id;";
		con = getConnection();
		System.out.println(sql);
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public static ResultSet searchLiveOrNot(int i, String aparID) {
		String sql = "";
		if(i == 1)
		{
			sql = "select stu_id,stu_name from Student where stu_id not in " +
				  "(select stu_id from Live);";
		}
		else
		{
			sql = "select stu_id,stu_name from Student where stu_id in " +
				  "(select stu_id from Live where ap_id like '%"+aparID+"%');";
		}
		con = getConnection();
		System.out.println(sql);
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public static void addRoom(String apar_id) {
		String room_id = createRoomId(apar_id);
		System.out.println(apar_id);
		String sql = "insert into Room values(?,?,?,?);";
		System.out.println(sql+" "+room_id+apar_id);
		try {
			con = getConnection();
			psInsert = con.prepareStatement(sql);
			psInsert.setString(1, apar_id);
			psInsert.setString(2, room_id);
			psInsert.setString(3, "");
			psInsert.setString(4, "");
			psInsert.execute();
			psInsert.close();//执行更新并关闭语句
			JOptionPane.showMessageDialog(null, "添加成功");
		} catch (Exception e) {
		} finally{
			closeCon();
		}
	}
	private static String createRoomId(String apar_id) {
		String sql = "select max(convert(int,room_id)) from Room where ap_id = '"+apar_id+"';";
		System.out.println(apar_id);
		String room_id = "";
		con = getConnection();
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
			if(rs.next())
			{
				int id = rs.getInt(1);
				int floor = id/100;
				if(floor == 0)
					floor++;
				int roomId = id%100;
				if(roomId < 30)
					roomId++;
				else
				{
					floor++;
					roomId = 1;
				}
				if(roomId < 9)
					room_id = floor +"0"+roomId;
				else
					room_id = floor +""+roomId;
			}
		} catch (SQLException e) {
			room_id = "101";
		}
		System.out.println(room_id);
		return room_id;
	}
	public static void deleteRoom(String apar_id, String room_id) {
		String sql = "delete from Room where ap_id = '"+apar_id+"' and room_id = '"+room_id+"';";
		System.out.println(sql);
		try {
			con = getConnection();
			stat = con.createStatement();
			stat.executeUpdate(sql);
			JOptionPane.showMessageDialog(null, "删除成功");
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "删除失败,现有数据被引用");
		} finally{
			closeCon();
		}
	}
	public static ResultSet getLiveInfo(String stu_id) {
		String sql = "select ap_name,Live.room_id,stu_id from Apartment,Live where stu_id = '"+stu_id+"' " +
					 "and Apartment.ap_id = Live.ap_id;";
		con = getConnection();
		System.out.println(sql);
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public static boolean deleteFromLive(String stu_id) {
		String sql = "delete from Live where stu_id = '"+stu_id+"';";
		System.out.println(sql);
		try {
			con = getConnection();
			stat = con.createStatement();
			stat.executeUpdate(sql);
			JOptionPane.showMessageDialog(null, "退住办理成功");
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "退住办理失败,现有数据被引用");
			return false;
		} finally{
			closeCon();
		}
	}
	public static boolean addLive(String ap_id, String room_id, String stuid) {
		ResultSet rs1 = searchRoomLiveNum(ap_id,room_id);
		String sql = "insert into Live values(?,?,?);";
		try {
			if(rs1.next())
			{
				if(rs.getInt(1) >= 4)
				{
					JOptionPane.showMessageDialog(null, "人数达到上限,请选择其他房间");
					return false;
				}
			}
			con = getConnection();
			psInsert = con.prepareStatement(sql);
			psInsert.setString(1, ap_id);
			psInsert.setString(2, room_id);
			psInsert.setString(3, stuid);
			psInsert.execute();
			psInsert.close();//执行更新并关闭语句
			JOptionPane.showMessageDialog(null, "添加成功");
			return true;
		} catch (Exception e) {
			return false;
		} finally{
			closeCon();
		}
	}
	public static ResultSet searchClassStuInfo(String class_id) {
		String sql = "select stu_id,stu_name,case stu_sex when '1' then '男' when '0' then '女' end from Student where class_id = '"+class_id+"';";
		con = getConnection();
		System.out.println(sql);
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public static boolean deleteStudent(String deleteStuId) {
		deleteFromLive(deleteStuId);
		String sql = "delete from Student where stu_id = '"+deleteStuId+"';" +
					 "delete from Pic where ID = '"+deleteStuId+"';" +
					 "delete from Pasw where ID = '"+deleteStuId+"';";
		System.out.println(sql);
		try {
			con = getConnection();
			stat = con.createStatement();
			stat.executeUpdate(sql);
			JOptionPane.showMessageDialog(null, "学生删除成功");
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "学生删除失败,现有数据被引用");
			return false;
		} finally{
			closeCon();
		}
	}
	public static boolean addStudent(Vector<String> stuinfo) {
		String sql = "insert into Student values(?,?,?,?,?,?,?,?,?,?);"+
				 	 "insert into Pasw values(?,?,?,?)";
		try {
			con = getConnection();
			psInsert = con.prepareStatement(sql);
			int i = 0;
			for(;i < stuinfo.size();i++)
				psInsert.setString(i+1, stuinfo.get(i));
			for(;i<10;i++)
				psInsert.setString(i+1, "");
			psInsert.setString(++i, stuinfo.get(0));
			psInsert.setString(++i, stuinfo.get(0));
			psInsert.setString(++i, Student);
			psInsert.setString(++i, "0");
			psInsert.execute();
			psInsert.close();//执行更新并关闭语句
			JOptionPane.showMessageDialog(null, "添加成功");
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "该学生已存在");
			return false;
		} finally{
			closeCon();
		}
	}
	public static ResultSet searchAllAdminInfo(int index) {
		String sql = "";
		if(index == 1)
		{
			sql = "select Admin.admin_id,admin_name,case admin_sex when '1' then '男' when '0' then '女' end,ap_id " +
					"from Admin left join Management on Admin.admin_id = Management.admin_id " +
					"where Admin.admin_id not in (select admin_id from Management);";
		}
		else
		{
			sql = "select Admin.admin_id,admin_name,case admin_sex when '1' then '男' when '0' then '女' end,ap_id " +
					"from Admin left join Management on Admin.admin_id = Management.admin_id " +
					"where Admin.admin_id in (select admin_id from Management);";
					  
		}
		con = getConnection();
		System.out.println(sql);
		try {
			stat = con.createStatement();
			rs = stat.executeQuery(sql);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public static boolean addAdmin(Vector<String> adminInfo) {
		String sql = "insert into Admin values(?,?,?,?,?,?,?);" +
					 "insert into Pasw values(?,?,?,?)";
		try {
			con = getConnection();
			psInsert = con.prepareStatement(sql);
			int i = 0;
			for(;i < adminInfo.size();i++)
				psInsert.setString(i+1, adminInfo.get(i));
			for(;i<7;i++)
				psInsert.setString(i+1, "");
			psInsert.setString(++i, adminInfo.get(0));
			psInsert.setString(++i, adminInfo.get(0));
			psInsert.setString(++i, Admin);
			psInsert.setString(++i, "0");
			psInsert.execute();
			psInsert.close();//执行更新并关闭语句
			JOptionPane.showMessageDialog(null, "添加成功");
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "该管理员已存在");
			return false;
		} finally{
			closeCon();
		}
	}
	public static boolean deleteAdmin(String selectedAdminID) {
		deleteFromManagement(selectedAdminID);
		String sql = "delete from Admin where admin_id = '"+selectedAdminID+"';";
		System.out.println(sql);
		try {
			con = getConnection();
			stat = con.createStatement();
			stat.executeUpdate(sql);
			JOptionPane.showMessageDialog(null, "管理员删除成功");
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "管理员删除失败,现有数据被引用");
			return false;
		} finally{
			closeCon();
		}
	}
	public static boolean deleteFromManagement(String selectedAdminID) {
		String sql = "delete from Management where admin_id = '"+selectedAdminID+"';";
		System.out.println(sql);
		try {
			con = getConnection();
			stat = con.createStatement();
			stat.executeUpdate(sql);
			JOptionPane.showMessageDialog(null, "离职办理成功");
			return true;
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "离职办理失败,现有数据被引用");
			return false;
		} finally{
			closeCon();
		}
	}
	public static boolean addManagement(String apar_id,String adminID) {
		String sql = "insert into Management values(?,?);";
		try {
			con = getConnection();
			psInsert = con.prepareStatement(sql);
			psInsert.setString(1, apar_id);
			psInsert.setString(2, adminID);
			psInsert.execute();
			psInsert.close();//执行更新并关闭语句
			JOptionPane.showMessageDialog(null, "添加成功");
			return true;
		} catch (Exception e) {
			return false;
		} finally{
			closeCon();
		}
	}

	public static void backup(String fileName) {
		try {
			if(!fileName.endsWith(".bak"))
				fileName+=".bak";
			System.out.println("DB"+fileName);
			String sql = "backup database AIMS to disk = '"+fileName+"'";
			con = getConnection();
			psInsert = con.prepareStatement(sql);
			psInsert.execute();
			psInsert.close();
			JOptionPane.showMessageDialog(null,"备份成功");
		} catch (SQLException e) {
			e.printStackTrace();
		}	
	}
	public static void restore(String fileName) {
		try {
			con = getConnectionMaster();
			CallableStatement cs = con.prepareCall("{call kill_restore(?,?)}");
			cs.setString(1, "AIMS");
			cs.setString(2, fileName);
			cs.execute();
			JOptionPane.showMessageDialog(null,"恢复成功");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}
