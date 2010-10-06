package czzz.app.rssreader;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;



import android.R.integer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.AndroidCharacter;
import android.text.Layout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Display;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector.OnGestureListener;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import czzz.app.rssreader.data.OPMLfeed;
import czzz.app.rssreader.data.RSSFeed;
import czzz.app.rssreader.sax.OPMLHandler;
import java.lang.reflect.Field;



public class ActiveOPML extends CustomWindow  implements OnItemClickListener ,  OnTouchListener 
	{
		public View textEntryView1;
		private GestureDetector detector;//触摸监听实例
		OPMLHandler hand= MyGobal.getHand();   
		private  OPMLfeed feed = MyGobal.getFeed(); 
	    	
		public final String tag = "RSSReader";
		
		private ViewFlipper flipper;
		public String mess="";
		public ActiveOPML() {   
			detector = new GestureDetector(new MySimpleGesture());   
	    }  
		private class MySimpleGesture extends SimpleOnGestureListener {   
	        // 双击的第二下Touch down时触发    
	        public boolean onDoubleTap(MotionEvent e) {   
	            Log.i("MyGesture", "onDoubleTap");   
	            return super.onDoubleTap(e);   
	        }   
	           
	        // 双击的第二下Touch down和up都会触发，可用e.getAction()区分   
	        public boolean onDoubleTapEvent(MotionEvent e) {   
	            Log.i("MyGesture", "onDoubleTapEvent");   
	            return super.onDoubleTapEvent(e);   
	        }   
	           
	        // Touch down时触发    
	        public boolean onDown(MotionEvent e) {   
	         Log.i("MyGesture", "onDown");
	            return super.onDown(e);   
	        }   
	           
	        // Touch了滑动一点距离后，up时触发   
	        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {   
	           Log.i("MyGesture", "onFling");   
	        	
	        	if (e2.getX() - e1.getX() > 50 && Math.abs(velocityX) > 50)  
		         {
	        		
					if ( hand.setFeed(-1,false))
					{
		    		 feed =hand.getFeed();
		    		 addView();
		    		 //flipper.setInAnimation(AnimationUtils.loadAnimation(context, id).loadAnimation(this, R.anim.push_right_in));
						flipper.setInAnimation(AnimationUtils.loadAnimation(ActiveOPML.this, R.anim.push_right_in));
						flipper.setOutAnimation(AnimationUtils.loadAnimation(ActiveOPML.this, R.anim.push_right_out));
						//showActive();
						unregisterForContextMenu((ListView) flipper.getCurrentView().findViewById(R.id.itemlist));  
						
						flipper.showNext();
						return true;
					}
		         }
				 
				return false;
	          //  return super.onFling(e1, e2, velocityX, velocityY);   
	        }   
	           
	        // Touch了不移动一直Touch down时触发   
	        public void onLongPress(MotionEvent e) {   
	            Log.i("MyGesture", "onLongPress");   
	            super.onLongPress(e);   
	        }   
	           
	        // Touch了滑动时触发   
	        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {   
	            Log.i("MyGesture", "onScroll");   
	            return super.onScroll(e1, e2, distanceX, distanceY);   
	        }   
	           
	        /*  
	         * Touch了还没有滑动时触发  
	         * (1)onDown只要Touch Down一定立刻触发  
	         * (2)Touch Down后过一会没有滑动先触发onShowPress再触发onLongPress  
	         * So: Touch Down后一直不滑动，onDown -> onShowPress -> onLongPress这个顺序触发。  
	         */  
	        public void onShowPress(MotionEvent e) {   
	           Log.i("MyGesture", "onShowPress");   
	            super.onShowPress(e);   
	        }   
	  
	        /*  
	         * 两个函数都是在Touch Down后又没有滑动(onScroll)，又没有长按(onLongPress)，然后Touch Up时触发  
	         * 点击一下非常快的(不滑动)Touch Up: onDown->onSingleTapUp->onSingleTapConfirmed  
	         * 点击一下稍微慢点的(不滑动)Touch Up: onDown->onShowPress->onSingleTapUp->onSingleTapConfirmed   
	         */    
	        public boolean onSingleTapConfirmed(MotionEvent e) {   
                // return false;
	        	Log.i("MyGesture", "onSingleTapConfirmed");   
	            return super.onSingleTapConfirmed(e);   
	        }   
	        public boolean onSingleTapUp(MotionEvent e) {   
	            Log.i("MyGesture", "onSingleTapUp");   
	            return super.onSingleTapUp(e);   
	        //return false;
	        }   
	    }   
	

		
		

	    public void onCreate(Bundle icicle) {
	    	super.onCreate(icicle);
	    	setContentView(R.layout.opml);
	    	//this.htitle.setText("旺财阅读");
	        //detector = new GestureDetector(this);//初始化触摸探测    
	        flipper =(ViewFlipper)findViewById(R.id.ViewFlipper01);
		
	        // hand=MyGobal.getHand();
	       // feed = getFeed();
	        addView();
	       
//			Button backbutton = (Button) findViewById(R.id.back);
//			backbutton.setOnClickListener(new Button.OnClickListener() {
//				public void onClick(View v) {
//					if ( hand.setFeed(-1,false))
//					{
//		    		 feed =hand.getFeed();
//		    		 if (feed!=null)
//		    			 htitle.setText(feed.getTitle());
//		    		 showListView();
//				    
//					}
//				}
//			});
	    }

		private void addView()
		{
			LayoutInflater inflater = getLayoutInflater();
			try
			{
				for (int i=0;i<flipper.getDisplayedChild();i++)
				{
				flipper.removeViewAt(i);
				}
				//flipper.removeViewAt(2);
			
			}
			catch(Exception e){}
		
						View layout = inflater.inflate(R.layout.opmlclone,null);
						showListView(layout);
						flipper.addView(layout);
						
					

	

			//	TextView text　=　(TextView)　layout.findViewById(R.id.text);　
		}

	    
	    
		@Override
		protected Dialog onCreateDialog(int id) {
			switch (id)
			{
				case 0:
					return buildAddRss(this);
				case 1:
					return buildAlert(this);
				case 2:
					return buildAlert1(this);
				case 3:
					return buildAlert2(this);	
			}
			return null;
		}
	    
	    private Dialog buildAlert(Context context) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setMessage("\n"+mess);
			builder.setTitle("旺财提示");
			builder.setPositiveButton(R.string.alert_dialog_ok,null);
		
			return builder.create();

		}
	    
	    private Dialog buildAlert1(Context context) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setIcon(android.R.drawable.ic_dialog_info);
			builder.setTitle("旺财提示");
			builder.setMessage("\n确认要退出旺财阅读吗？");
			builder.setPositiveButton(R.string.alert_dialog_ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							System.exit(0);
					//		android.os.Process.killProcess(android.os.Process.myPid());
						}
					});
		  builder.setNegativeButton(R.string.alert_dialog_cancel,null); 
			return builder.create();
		}

	    private Dialog buildAlert2(Context context) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setIcon(android.R.drawable.ic_delete);
			builder.setTitle("旺财提示");
			builder.setMessage("所有添加的频道都会删除！\n确认恢复默认频道？");
			builder.setPositiveButton(R.string.alert_dialog_ok,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							restoreDefault();
						}
					});
		  builder.setNegativeButton(R.string.alert_dialog_cancel,null); 
			return builder.create();
		}
	    
		private Dialog buildAddRss(Context context) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setCancelable(false);
			builder.setTitle("新建频道");
			LayoutInflater factory = LayoutInflater.from(this);
	        textEntryView1 = factory.inflate(R.layout.addrssdiaglog, null);
			builder.setView(textEntryView1);
			builder.setPositiveButton(R.string.alert_dialog_ok,
					new DialogInterface.OnClickListener() {
						public void onClick(final DialogInterface dialog, int whichButton) {
						    final String type;
							boolean isrss = ((RadioButton) textEntryView1.findViewById(R.id.myRadioButton1)).isChecked();
						    if (isrss)
						    	type="rss";
						    else
						    	type="opml";
						    final String url=String.valueOf(((EditText)textEntryView1.findViewById(R.id.EditUrl)).getText());
						    final String title=String.valueOf(((EditText)textEntryView1.findViewById(R.id.EditTitle)).getText());
							final String mshow="mShowing";
						    if (url.equals("") || title.equals(""))
							{
							 try {
								 Field field = dialog.getClass() .getSuperclass().getDeclaredField(mshow); 
								 field.setAccessible(true); 
								 // 将mShowing变量设为false，表示对话框已关闭 
								 field.set(dialog, false);
								 dialog.dismiss();
								 } 
							      catch (Exception e) { } 
							      removeDialog(1);
							    mess="请输入网址和标题";   
							    showDialog(1);
								return;
							}
							else
							{
							   final ProgressDialog myProgressDialog = ProgressDialog.show(ActiveOPML.this,     
				                         "请稍后", "正在获取网络数据", true);
						        myProgressDialog.setOnKeyListener(new OnKeyListener(){  
						        	@Override
						        	public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
						        		 // TODO Auto-generated method stub  
						        	    if ((keyCode == KeyEvent.KEYCODE_BACK)){  
						        	    	dialog.dismiss();
						        	    }  
						        		return false;
						        	}  
						        	              });  
				               new AsyncTask<Void, Void, Boolean>(){
				                   @Override
				                   protected void onPostExecute(Boolean result) {
				                	   if (result)
										{
											 try {
												 Field field = dialog.getClass() .getSuperclass().getDeclaredField(mshow); 
												 field.setAccessible(true); 
												 // 将mShowing变量设为false，表示对话框已关闭 
												 field.set(dialog,true );
												  dialog.dismiss();
												 } 
											      catch (Exception e) { } 
										      feed =hand.getFeed();
										 	 flipper.setInAnimation(null);
											 flipper.setOutAnimation(null);
										      addView();
										      flipper.showNext();
										      try {
												FileOutputStream os = openFileOutput("op1x.xml", MODE_PRIVATE);
												String str =hand.xmltoStr();
												  
												  hand.Write (os,str);
										      } catch (FileNotFoundException e) {
												// TODO Auto-generated catch block
												e.printStackTrace();
											}  
											 
										}
										else
										{ 	removeDialog(1);
											mess="网络故障 或地址有误，请稍后再试！";
											showDialog(1);
										}
				   					myProgressDialog.dismiss();
				                   }
								@Override
								protected Boolean doInBackground(Void... arg0) {
									 return hand.addElement(type, url, title);
								}
								
				               }.execute();
							
								
								
							}
						}
					});
			builder.setNegativeButton(R.string.alert_dialog_cancel,
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int whichButton) {
							String mshow="mShowing"; 
							try {
								 Field field = dialog.getClass() .getSuperclass().getDeclaredField(mshow); 
								 field.setAccessible(true); 
								 // 将mShowing变量设为false，表示对话框已关闭 
								 field.set(dialog, true);
								 dialog.dismiss(); 
								 } 
							      catch (Exception e) { } 
						}
					});
			return builder.create();
		}
	    @Override
	    public boolean onContextItemSelected(MenuItem item) {
	    	AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
	    	  switch (item.getItemId()) {
	    	  case 0:
	    		  go(info.position);
	    	  case 1:
	    		    deleteNode(info.position);
	    	    return true;
	    	  case 2:
	    	    return true;
	    	  default:
	    	    return super.onContextItemSelected(item);
	    	  }
	    }
		
	    private void deleteNode(int position) {
			if (hand.delElement(position))
			{
				 feed =hand.getFeed();
				 this.flipper.setInAnimation(null);
					this.flipper.setOutAnimation(null);
					
			     addView();
			     flipper.showNext();
				try {
					FileOutputStream os = openFileOutput("op1x.xml", MODE_PRIVATE);
					String str =hand.xmltoStr();
					  hand.Write (os,str);
			      } catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}  
			}
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
	    
	    @Override
		public boolean onOptionsItemSelected(MenuItem item) {
			switch (item.getItemId())
			{
			case 0:
				removeDialog(0);
				showDialog(0);
				break;
			case 1:
				removeDialog(3);
				showDialog(3);
				break;
			}	
			return true;
			
	    }

	    private void restoreDefault(){
			this.deleteFile("op1x.xml");
			feed = getFeed();
			
			 this.flipper.setInAnimation(null);
				this.flipper.setOutAnimation(null);
	        addView();
	        flipper.showNext();
		}

		@Override
		public boolean onCreateOptionsMenu(Menu menu) {
	    	menu.add(0, 0,0,"添加频道").setIcon(android.R.drawable.ic_menu_add);
	    	menu.add(0, 1,0,"恢复默认数据").setIcon(android.R.drawable.ic_menu_revert);
	    	menu.add(0, 2,0,"帮助").setIcon(android.R.drawable.ic_menu_help);
	    	menu.add(0, 3,0,"关于旺财").setIcon(R.drawable.icon);
			return true;
		}

//	    private void showListView() 
//	    {
//	        ListView itemlist = (ListView) findViewById(R.id.itemlist);     
//	        registerForContextMenu((ListView)findViewById(R.id.itemlist));    
//
//	        if (feed == null)
//	        {
//	        	setTitle("访问的OMPL无效");
//	        	return;
//	        }
//	        SimpleAdapter adapter = new SimpleAdapter(this,   feed.getAllItemsForListView(),
//	       		 R.layout.listitem1, new String[] { "title" },
//	       		 new int[] { R.id.ItemTitle });
//	        itemlist.setAdapter(adapter);
//	     
//	       itemlist.setOnItemClickListener(this);
//	        itemlist.setSelection(0);
//	       // TextView tv=(TextView)findViewById(R.id.TextView01);
//	       // tv.setText((CharSequence)feed.getTitle());
//	        htitle.setText((CharSequence)feed.getTitle());
//	        itemlist.setOnTouchListener(new OnTouchListener() {              
//	        	   public boolean onTouch(View v, MotionEvent event) {   
//	        	        return detector.onTouchEvent(event);   
//	        	    }   
//	        	});  
//	    }
		

	    private void  showListView(View v) 
	    {
	        ListView itemlist = (ListView) v.findViewById(R.id.itemlist);  
	        //ListView itemlist = new MyListView(this);
	    	 registerForContextMenu(itemlist);
	    		 
	        if (feed == null)
	        {
	        	this.htitle.setText("访问的OMPL无效");
	        	return ;
	        }
	        SimpleAdapter adapter = new SimpleAdapter(this,   feed.getAllItemsForListView(),
	       		 R.layout.listitem1, new String[] { "title" },
	       		 new int[] { R.id.ItemTitle });
	        itemlist.setAdapter(adapter);
	     
	       itemlist.setOnItemClickListener(this);
	        itemlist.setSelection(0);
	       // TextView tv=(TextView)findViewById(R.id.TextView01);
	       // tv.setText((CharSequence)feed.getTitle());
	        htitle.setText((CharSequence)feed.getTitle());
	        itemlist.setOnTouchListener(new OnTouchListener() {              
	        	   public boolean onTouch(View v, MotionEvent event) {   
	        	        return detector.onTouchEvent(event);   
	        	    }   
	        	});
	        return;
	       
	    }
	   
	    
	    @Override
		public boolean onTouchEvent(MotionEvent event) {
//	    	  if (event.getAction() == MotionEvent.ACTION_UP) {   
//	              Log.i("MyGesture", "MotionEvent.ACTION_UP");   
//	          }   
	          return detector.onTouchEvent(event);   
 
		}

		
	    private void go(int position)
        {
        	if(feed.getItem(position).getXmlUrl().equals(""))
	    	 {
	    		 hand.setFeed(position,true);
	    		 feed =hand.getFeed();
	    		 addView();
					flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
					flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
					//showActive();
					flipper.showNext();
				
	    	 }
	    	 else
	    	 {
	    		 Intent itemintent = new Intent(this,mainActive.class);
	        	 Bundle b = new Bundle();
	        	 b.putString("url",feed.getItem(position).getXmlUrl());
	        	 itemintent.putExtra("android.intent.extra.rss", b);
	             startActivityForResult(itemintent, 0);
	    	 }


        	
        }


		


		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			go(arg2);
						// TODO Auto-generated method stub
			
		}



		@Override
		public void onCreateContextMenu(ContextMenu menu, View v,
				ContextMenuInfo menuInfo) {
			AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
	    	
			//  super.onCreateContextMenu(menu, v, menuInfo);
			  menu.setHeaderTitle(hand.getFeed().getItem(info.position).getTitle());
			  menu.add(0,0,0,"阅读").setIcon(android.R.drawable.ic_menu_info_details);
			  menu.add(0, 1, 0, "删除").setIcon(android.R.drawable.ic_menu_delete);
			  menu.add(0, 2, 0,  "返回").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
			// TODO Auto-generated method stub
			super.onCreateContextMenu(menu, v, menuInfo);
		}






		@Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
			 if (keyCode == KeyEvent.KEYCODE_BACK) {
				 if ( hand.setFeed(-1,false))
					{
		    		 feed =hand.getFeed();
		    		 addView();
						this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
						this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
						//showActive();
						this.flipper.showNext();
		    		 return false;
				    
					}
				 else
				 {
					 showDialog(2);
					 return false;
			//		 return super.onKeyDown(keyCode, event);
				 }
		       

		       }

			
			return super.onKeyDown(keyCode, event);
		}

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			return false;
		}
		
		
	    
	}

