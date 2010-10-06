package czzz.app.rssreader;

import java.io.InputStream;



import czzz.app.rssreader.data.OPMLfeed;
import czzz.app.rssreader.sax.OPMLHandler;
import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

public class Load extends Activity {
		private OPMLHandler hand=null; 		
		private static OPMLfeed feed = null;

	
	 public void onCreate(Bundle icicle) {
	        super.onCreate(icicle);
	        //requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.load);
	       new task().execute("");
	       
	 }
	 
	 private OPMLfeed getFeed()
	    {
	    	try
	    	{
	    		InputStream in ;
	    		try
	    		{
	    		in =this.openFileInput("op1x.xml");  
	    		
	    		}
	    		catch (Exception e)
	    		{
	    			in =getResources().openRawResource(R.raw.op);
	    		}
	    		hand=new OPMLHandler(in);
	           return hand.getFeed();
	    	}
	    	catch (Exception ee)
	    	{
	    		return null;
	    	}
	    }
		private class task extends AsyncTask<String, String, String> {

			// ��ִ̨�еĺ�ʱ���񣬽��ղ��������ؽ��
			// ��onPostExecute()ִ���꣬�ں�̨�߳��б�ϵͳ����
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				// ������������ݣ��͸�onProgressUpdate�Ը��½���
			     feed=getFeed();
		        MyGobal.setFeed(feed);
		       MyGobal.setHand(hand);
			   return "";

			}

			// ����ִ�н�������UI�߳��б�ϵͳ����
			// һ��������ʾ�����Ѿ�ִ�н���
			@Override
			protected void onPostExecute(String result) {
				try
				{
					//Thread.sleep(5000);
				Intent intent=new Intent();
			     intent.setClass(Load.this, ActiveOPML.class);
			     Load.this.startActivity(intent);
			     finish();
				}
				catch(Exception e)
				{
					e.getMessage();
				//Log.println(priority, tag, msg)	
				}
			      // TODO Auto-generated method stub
				super.onPostExecute(result);
			
			
			}

			// ����ִ�У���UI�߳��б�ϵͳ����
			// һ��������UI�в���һ��������
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
		
			}

			// ���½�����������յ�������Ϣ����UI�߳��б�ϵͳ����
			@Override
			protected void onProgressUpdate(String... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);

			}

		}


}
