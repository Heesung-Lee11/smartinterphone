<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page import="java.io.*" %>

<%
	String device=request.getParameter("device");
	String regID=request.getParameter("regID");
	
	String filename="IDList.txt";
	try{
		String filePath=
				application.getRealPath(filename);

        BufferedReader reader = new BufferedReader(new FileReader(filePath));

        String data="";
		String temp1="";
		String temp2="";
        
		if((data=reader.readLine())!=null)
		{
			temp1=data;
			if((data=reader.readLine())!=null)
			{
				temp2=data;	
			}
		}
		reader.close();
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
		
		if(!temp2.equals(""))			//A,B디바이스 둘다 존재
		{
			if(device.equals("A"))				//
			{
				writer.write(device+":"+regID);
				writer.newLine();
				writer.write(temp2);
			} 
			else if(device.equals("B"))
			{
				writer.write(temp1);
				writer.newLine();
				writer.write(device+":"+regID);
			}
		}
		else if(!temp1.equals(""))
		{
			if(temp1.startsWith("A"))		//A디바이스만 존재
			{
				if(device.equals("A"))
				{
					writer.write(device+":"+regID);
				}
				else if(device.equals("B"))
				{
					writer.write(temp1);
					writer.newLine();
					writer.write(device+":"+regID);
				}
			}
			else if(temp1.startsWith("B"))		//B디바이스만존재
			{
				if(device.equals("A"))
				{
					writer.write(device+":"+regID);
					writer.newLine();
					writer.write(temp1);
				}
				else if(device.equals("B"))
				{
					writer.write(device+":"+regID);
				}
			}
		}
		else writer.write(device+":"+regID);
		
		writer.close();
	}catch(IOException ioe){
		ioe.printStackTrace();
	}
%>