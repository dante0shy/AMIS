package com.aims.welcome;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class GetIp {

	public static void main(String[] args) throws Exception {
		System.out.println(GetIp.getWebIp());
	}

	public static String getWebIp() {
		try {

			URL url = new URL("http://1111.ip138.com/ic.asp");
			BufferedReader br = new BufferedReader(new InputStreamReader(
					url.openStream()));
			StringBuffer sb = new StringBuffer("");
			String s = "";
			String webContent = "";
			while ((s = br.readLine()) != null) {
				sb.append(s + "rn");
			}
			br.close();
			webContent = sb.toString();
			int start = webContent.indexOf("<center>") + 8;
			int end = webContent.indexOf("</center>");
			if (start < 0 || end < 0) {
				return null;
			}
			String mainContent = webContent.substring(start, end);
			int start1 = mainContent.indexOf("[")+1;
			int end1   = mainContent.indexOf("]");
			String ip  = mainContent.substring(start1,end1);
			System.out.println(ip);
			int start2 = mainContent.lastIndexOf("£º")+1;
			int end2   = mainContent.lastIndexOf("ÊÐ")+1;
			String location = mainContent.substring(start2,end2);
			System.out.println(location);
			return ip+" "+location;
		} catch (Exception e) {
			e.printStackTrace();
			return "0";
		}
	}
}