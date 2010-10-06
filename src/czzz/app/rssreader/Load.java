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

			// 后台执行的耗时任务，接收参数并返回结果
			// 当onPostExecute()执行完，在后台线程中被系统调用
			@Override
			protected String doInBackground(String... params) {
				// TODO Auto-generated method stub
				// 在这里产生数据，送给onProgressUpdate以更新界面
			     feed=getFeed();
		        MyGobal.setFeed(feed);
		       MyGobal.setHand(hand);
			   return "";

			}

			// 任务执行结束后，在UI线程中被系统调用
			// 一般用来显示任务已经执行结束
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

			// 最先执行，在UI线程中被系统调用
			// 一般用来在UI中产生一个进度条
			@Override
			protected void onPreExecute() {
				// TODO Auto-generated method stub
				super.onPreExecute();
		
			}

			// 更新界面操作，在收到更新消息后，在UI线程中被系统调用
			@Override
			protected void onProgressUpdate(String... values) {
				// TODO Auto-generated method stub
				super.onProgressUpdate(values);

			}

		}


}
