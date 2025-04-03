/*  메시지 대화 창
 * 
 * 
 * 
 * 
 */

package com.example.userapp;

import java.text.*;
import java.util.*;

import org.apache.http.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;
import org.apache.http.util.*;

import com.example.userapp.SetPhoneNumber.*;

import android.os.*;
import android.app.Activity;
import android.content.*;
import android.database.*;
import android.database.sqlite.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.widget.AdapterView.*;

public class Chat extends Activity {
	
	//리스트 뷰를 구성하는 리스트뷰와 어댑터 변수
	private ExamAdapter m_adapter = null;
	private ListView m_list=null;
	
	EditText editText; //전화번호 쓰는 곳
	
	final String DB_CHAT = "chat"; //채팅 테이블
	
	SQLiteDatabase db_chat; //sqlite db

	//ExamData 객체를 관리하는 ArrayList 객체를 생성한다.
	ArrayList<ExamData> data_list = new ArrayList<ExamData>();
	
	//Handler handler;




	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.chat);
		
		editText = (EditText) findViewById(R.id.talkEditText);
		
		
		//사용자 정의 어댑터 객체를 생성한다.
		m_adapter=new ExamAdapter(data_list);
		
		// 리스트를 얻어서 어댑터를 설정한다.
        m_list = (ListView) findViewById(R.id.listView1);
        m_list.setAdapter(m_adapter);
        m_list.setDivider(null);
/*
        m_list.setOnItemClickListener(new OnItemClickListener() { //리스트뷰의 아이템 클릭시 내용 삭제

			@Override
			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {

				TextView tv = (TextView) v;

				String text = tv.getText().toString();

				db_chat.delete(DB_CHAT, "msg='" + text + "'", null);
				

				updateMsgDB();
			}
		});
  */      
        db_chat = new DatabaseHelper(this).getWritableDatabase();
        
        updateMsgDB();
     /*   
        handler = new Handler(){
            //메시지를 받아서 처리하는 핸들러 객체를 정의한다.
            //핸드러는 자동으로 현재 UI쓰레드에 연결된다.
            public void handleMessage(String msg) { //메시지를 처리
               
            	updateMsgDB();
            }//handleMessage
        }; 
*/


        
    // UpdateDBRun udbr = new UpdateDBRun(handler);
	// udbr.start();
  
	}

	public void mOnClick(View v) { // 버튼클릭시
		ExamData data = null;

		int view = v.getId();

		if (view == R.id.btn_1) { //인터폰으로 메시지 보내는 부분
			String text = editText.getText().toString();
			data=new ExamData(0,text); //param1 = 타입, param2 = 메시지
			editText.setText("");
			
			if(text.length()==0){
				return;
			}
			
			ContentValues give = new ContentValues();
			give.put("type", "0");
			give.put("msg", text);
			db_chat.insert(DB_CHAT,"null",give);
			
			//메시지를 인터폰으로 전송하는 쓰레드. 주석 풀고 쓰면 됨
			SendRun sdr = new SendRun(text);
			sdr.start();
			
			updateMsgDB();
			
			// Intent imgdownintent = new Intent("downimage");
			// startActivityForResult(imgdownintent,ImageSetCode);
			// startActivity(imgdownintent);
		} else if (view == R.id.btn_2) { //방문자로부터 메시지 받을때. 삭제 예정
			String text = "누구세요";
			data=new ExamData(0,text); //param1 = 타입, param2 = 메시지
			editText.setText("");
			
			if(text.length()==0){
				return;
			}
			
			ContentValues give = new ContentValues();
			give.put("type", "0");
			give.put("msg", text);
			db_chat.insert(DB_CHAT,"null",give);
			
			//메시지를 인터폰으로 전송하는 쓰레드. 주석 풀고 쓰면 됨
			SendRun sdr = new SendRun(text);
			sdr.start();
			
			updateMsgDB();
			/*String msg_visit = "택배요";		
			data=new ExamData(1,msg_visit); //param1 = 타입, param2 = 메시지
			
			if(msg_visit.length()==0){
				return;
			}
			
			ContentValues give = new ContentValues();
			give.put("type", "1");
			give.put("msg", msg_visit);
			db_chat.insert(DB_CHAT,"null",give);
			
			updateMsgDB();*/
		} 
		
		// 어댑터에 데이터를 추가한다.
        //m_adapter.add(data);
        // 추가된 영역으로 스크롤을 이동시킨다.
        m_list.smoothScrollToPosition(m_adapter.getCount() - 1);   

	}
	public class SendRun extends Thread{ //메시지를 인터폰으로 전송하는 쓰레드
		private String msg; //보낼 메시지
		
		public SendRun(String msg){ //쓰레드 생성 시 메시지를 넘겨받음
			this.msg=msg;
		}
		public void run(){
			try
			{
			        HttpClient client = new DefaultHttpClient();  

			        String getURL = "http://220.68.69.206/gcmtest/push.jsp?device=B&key=CHAT&msg="+msg;

			        HttpGet get = new HttpGet(getURL);

			        HttpResponse responseGet = client.execute(get);  

			        HttpEntity resEntityGet = responseGet.getEntity();
			        
			        
			        Log.d("test","메시지 전송 성공");
			        if (resEntityGet != null)
			        {  
			                // 결과를 처리합니다.
			                Log.i("RESPONSE", EntityUtils.toString(resEntityGet));
			        }
			}
			catch (Exception e)
			{
			        e.printStackTrace();
			}

		}
	}
	/*
	public class UpdateDBRun extends Thread{ // 대화창 업데이트 스레드.. 이상함
		Handler handler;
		public UpdateDBRun(Handler handler){
			this.handler=handler;
		}
		public void run(){
			while(true){
			handler.sendEmptyMessage(0);
			}
		}
	}
	*/
	public void updateMsgDB(){
		data_list.clear();
		
		ExamData data = null;
		
		String[] col1 = {"type"};
		String[] col2 = {"msg"};
		
		Cursor c = db_chat.query(DB_CHAT, col1, null, null, null, null, null);
		Cursor c2 = db_chat.query(DB_CHAT, col2, null, null, null, null, null);
		
		if(c.getCount()<=0){
			
			return;
		}
		
		c.moveToFirst();
		c2.moveToFirst();
		
		while(!c.isAfterLast()){
			int type1 = c.getInt(0);
			String msg1 = c2.getString(0);
			data= new ExamData(type1,msg1);
			m_adapter.add(data);
			
			c.moveToNext();
			c2.moveToNext();
			
		}
		
		m_list.smoothScrollToPosition(m_adapter.getCount() - 1); 
		
		c.close();
		c2.close();
		
	}
	private class DatabaseHelper extends SQLiteOpenHelper { //전화번호 데이터베이스 초기화

		public DatabaseHelper(Context context) {

			super(context, DB_CHAT, null, 2);

		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + DB_CHAT + " (_id INTEGER PRIMARY KEY AUTOINCREMENT,type INT, msg TEXT);"); //전화번호테이블
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		}
	}
	// BaseAdapter 를 상속하여 어댑터 클래스를 재정의한다.
    private class ExamAdapter extends BaseAdapter 
    {
        private LayoutInflater m_inflater = null;
        // ExamData 객체를 관리하는 ArrayList
        private ArrayList<ExamData> m_data_list;
        
        public ExamAdapter(ArrayList<ExamData> items)
        {
            m_data_list = items;    
            // 인플레이터를 얻는다.
            m_inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        
        // ArrayList 에 ExamData 객체를 추가하는 메서드
        public void add(ExamData parm_data)
        {
            m_data_list.add(parm_data);
            // 데이터가 변화됨을 알려준다.
            notifyDataSetChanged();
        }
 
        // 어댑터에서 참조하는 ArrayList 가 가진 데이터의 개수를 반환하는 함수
        @Override
        public int getCount() 
        {
            return m_data_list.size();
        }
        
        // 인자로 넘어온 값에 해당하는 데이터를 반환하는 함수
        @Override
        public ExamData getItem(int position) 
        {
            return m_data_list.get(position);
        }
        
        // 인자로 넘어온 값에 해당하는 행 ID 를 반환하는 메서드
        @Override
        public long getItemId(int position) 
        {
            return position;
        }
        
        // 인자로 넘어온 값에 해당하는 뷰의 타입을 반환하는 메서드
        @Override
        public int getItemViewType(int position)
        {
            return m_data_list.get(position).type;
        }
        // getView 메서드로 생성될 수 있는 뷰의 수를 반환하는 메서드
        @Override
        public int getViewTypeCount()
        {
            return 3;
        }
 
        // 각 항목에 출력될 뷰를 구성하여 반환하는 메서드
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View view = null;
            // 해당 항목의 뷰 타입을 얻는다.
            int type = getItemViewType(position);
            
            // convertView 뷰는 어댑터가 현재 가지고 있는 해당 항목의 뷰객체이다.
            // null 이 넘어오는 경우에만 새로 생성하고, 그렇지않은 경우에는 그대로 사용한다.
            if(convertView == null) {
                // 타입에 따라 각기 다른 XML 리소스로 뷰를 생성한다.
                switch(type) {
                    case 0 :
                        view = m_inflater.inflate(R.layout.user_talk, null); 
                        break;
                    case 1 :
                        view = m_inflater.inflate(R.layout.visitor_talk, null); 
                        break;

                }
            } else {
                view = convertView;
            }
            // 요청하는 항목에 해당하는 데이터 객체를 얻는다.
            ExamData data = m_data_list.get(position);
            
            // 데이터가 존재하는 경우
            if(data != null) {
                // 뷰 타입에 따라 값을 설정한다.
                if(type == 0) {
                    TextView  msg_tv = null;                 
                    
                    msg_tv = (TextView) view.findViewById(R.id.user_msg);
                   
                    
                    // 이름, 메세지, 시간정보를 텍스트뷰에 설정한다.
                    
                    msg_tv.setText(data.data1);
                   
                } else if(type == 1) {
                    TextView msg_tv = null;                   
                    msg_tv = (TextView) view.findViewById(R.id.visitor_msg);
                    
                    // 메세지, 시간정보를 텍스트뷰에 설정한다.
                    msg_tv.setText(data.data1);
                                      
                } else if(type == 2) {
                    // 날짜정보를 텍스트뷰에 설정한다.
                    ((TextView)view).setText(data.data1);
                }
            }
            // 뷰를 반환한다.
            return view;            
        }
    }




	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
