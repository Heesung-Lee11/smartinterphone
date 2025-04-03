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
	 String regID=""; //메시지를 보낼 대상들
	
	 String MESSAGE_ID = String.valueOf(Math.random() % 100 + 1);	 //메시지 고유 ID

	 
	 
	 
	 boolean SHOW_ON_IDLE = true;	 //기기가 활성화 상태일때 보여줄것인지

	 int LIVE_TIME = 100;	 //기기가 비활성화 상태일때 GCM가 메시지를 유효화하는 시간

	 int RETRY = 3;	 //메시지 전송실패시 재시도 횟수

	 String simpleApiKey = "AIzaSyC86MLTnYHVBuZjiIbvQM8hzNjNs9-MwuI";	 //가이드 1때 받은 키

	 String gcmURL = "https://android.googleapis.com/gcm/send";		 //푸쉬를 요청할 구글 주소

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
 				out.print("푸쉬 성공!!");
 			}
 		}
	 }catch (Exception e) {
		e.printStackTrace();
	 }
%>
