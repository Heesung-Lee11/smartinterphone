<%@ page language="java" contentType="text/html; charset=EUC-KR"

    pageEncoding="EUC-KR"%>

<%@ page import="com.google.android.gcm.server.*" %>

<%@ page import="java.io.*" %>

<%@ page import="java.net.URLEncoder" %>

<%
	 request.setCharacterEncoding("EUC-KR");	 
	 String device=request.getParameter("device");
	 String key=request.getParameter("key");
	 String msg=request.getParameter("msg");
	 String msg_kr=null;
	 String regID=""; //�޽����� ���� ����
	
	 String MESSAGE_ID = String.valueOf(Math.random() % 100 + 1);	 //�޽��� ���� ID

	 
	 
	 
	 boolean SHOW_ON_IDLE = true;	 //��Ⱑ Ȱ��ȭ �����϶� �����ٰ�����

	 int LIVE_TIME = 100;	 //��Ⱑ ��Ȱ��ȭ �����϶� GCM�� �޽����� ��ȿȭ�ϴ� �ð�

	 int RETRY = 3;	 //�޽��� ���۽��н� ��õ� Ƚ��

	 String simpleApiKey = "AIzaSyC86MLTnYHVBuZjiIbvQM8hzNjNs9-MwuI";	 //���̵� 1�� ���� Ű

	 String gcmURL = "https://android.googleapis.com/gcm/send";		 //Ǫ���� ��û�� ���� �ּ�

	 String filename="IDList.txt";
	 
	 try {

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
 		
 		if(!temp1.equals(""))
 		{
 			if(!temp2.equals(""))
 			{
 				if(device.startsWith("A"))
 				{
 					regID=temp1.substring(temp1.indexOf(":")+1);
 				}
 				else if(device.startsWith("B"))
 				{
 					regID=temp2.substring(temp2.indexOf(":")+1);
 				}

 				msg_kr=URLEncoder.encode(msg,"EUC-KR");
 						
 		        Sender sender = new Sender(simpleApiKey);

 				Message message = new Message.Builder()

 				//.collapseKey(MESSAGE_ID)

 				.delayWhileIdle(SHOW_ON_IDLE)

 				.timeToLive(LIVE_TIME)

 				.addData(key,msg)

 				.build();

 				Result result = sender.send(message,regID,RETRY);
 				out.print("msg:"+msg+"&&&&msg_kr:"+msg_kr);
 				out.print("Ǫ�� ����!!");
 			}
 		}
	 }catch (Exception e) {
		e.printStackTrace();
	 }
%>
