package czzz.app.rssreader;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.reflect.Field;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.SimpleAdapter;
import android.widget.ViewFlipper;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import czzz.app.rssreader.data.OPMLfeed;
import czzz.app.rssreader.sax.OPMLHandler;

public class ActiveOPML extends CustomWindow implements OnItemClickListener,OnTouchListener 
{
	public View textEntryView1;
	private GestureDetector detector;// 触摸监听实例
	OPMLHandler hand;
	private OPMLfeed feed;
	public final String tag = "RSSReader";
	private ViewFlipper flipper;
	public String mess = "";
	
	public ActiveOPML() 
	{
		detector = new GestureDetector(new MySimpleGesture());
	}

	public void onCreate(Bundle icicle) 
	{
		super.onCreate(icicle);
		setContentView(R.layout.opml);
		flipper = (ViewFlipper) findViewById(R.id.ViewFlipper01);
		addfirstview();
		new task().execute("");
	}

	private void addfirstview() 
	{
		LayoutInflater inflater = getLayoutInflater();
		View layout = inflater.inflate(R.layout.load, null);
		flipper.addView(layout);
	}

	private void addView() 
	{
		LayoutInflater inflater = getLayoutInflater();
		try 
		{
				for (int i = 0; i < flipper.getDisplayedChild(); i++) 
				{
					flipper.removeViewAt(i);
				}
		} catch (Exception e) {}
		View layout = inflater.inflate(R.layout.opmlclone, null);
		showListView(layout);
		flipper.addView(layout);
	}

	@Override
	protected Dialog onCreateDialog(int id) 
	{
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
	
	private Dialog buildAlert(Context context) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setMessage("\n" + mess);
		builder.setTitle("旺财提示");
		builder.setPositiveButton(R.string.alert_dialog_ok, null);
		return builder.create();
	}

	private Dialog buildAlert1(Context context) 
	{
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_dialog_info);
		builder.setTitle("旺财提示");
		builder.setMessage("\n确认要退出旺财阅读吗？");
		builder.setPositiveButton(R.string.alert_dialog_ok,
				new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton) 
					{
						System.exit(0);
					}
				});
		builder.setNegativeButton(R.string.alert_dialog_cancel, null);
		return builder.create();
	}

	private Dialog buildAlert2(Context context) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setIcon(android.R.drawable.ic_delete);
		builder.setTitle("旺财提示");
		builder.setMessage("所有添加的频道都会删除！\n确认恢复默认频道？");
		builder.setPositiveButton(R.string.alert_dialog_ok,
				new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton) 
					{
						restoreDefault();
					}
				});
		builder.setNegativeButton(R.string.alert_dialog_cancel, null);
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
				new DialogInterface.OnClickListener() 
				{
					public void onClick(final DialogInterface dialog,
							int whichButton) 
					{
						final String type;
						boolean isrss = ((RadioButton) textEntryView1.findViewById(R.id.myRadioButton1)).isChecked();
						if (isrss)
							type = "rss";
						else
							type = "opml";
						final String url = String.valueOf(((EditText) textEntryView1.findViewById(R.id.EditUrl)).getText());
						final String title = String.valueOf(((EditText) textEntryView1.findViewById(R.id.EditTitle)).getText());
						final String mshow = "mShowing";
						if (url.equals("") || title.equals("")) 
						{
							try 
							{
								Field field = dialog.getClass().getSuperclass().getDeclaredField(mshow);
								field.setAccessible(true);
								// 将mShowing变量设为false，表示对话框已关闭	
								field.set(dialog, false);
								dialog.dismiss();
							} catch (Exception e) {}
							removeDialog(1);
							mess = "请输入网址和标题";
							showDialog(1);
							return;
						} 
						else 
						{
							final ProgressDialog myProgressDialog = ProgressDialog.show(ActiveOPML.this, "请稍后", "正在获取网络数据",true);
							myProgressDialog.setOnKeyListener(new OnKeyListener() 
															{
																@Override
																public boolean onKey(DialogInterface dialog,int keyCode, KeyEvent event) 
																{
																	if ((keyCode == KeyEvent.KEYCODE_BACK)) {
																		dialog.dismiss();
																	}
																	return false;
																}
															});
							new AsyncTask<Void, Void, Boolean>() 
							{
								@Override
								protected void onPostExecute(Boolean result) 
								{
									if (result) 
									{
										try 
										{
											Field field = dialog.getClass().getSuperclass().getDeclaredField(mshow);
											field.setAccessible(true);
											// 将mShowing变量设为false，表示对话框已关闭
											field.set(dialog, true);
											dialog.dismiss();
										} catch (Exception e) {}
										feed = hand.getFeed();
										flipper.setInAnimation(null);
										flipper.setOutAnimation(null);
										addView();
										flipper.showNext();
										try 
										{
											FileOutputStream os = openFileOutput("op1x.xml", MODE_PRIVATE);
											String str = hand.xmltoStr();
											hand.Write(os, str);
										} 
										catch (FileNotFoundException e) 
										{
											// CZZZ DO SOMETING
										}

									} else {
										removeDialog(1);
										mess = "网络故障 或地址有误，请稍后再试！";
										showDialog(1);
									}
									myProgressDialog.dismiss();
								}
								@Override
								protected Boolean doInBackground(Void... arg0) 
								{
									return hand.addElement(type, url, title);
								}
							}.execute();

						}
					}
				});
		builder.setNegativeButton(R.string.alert_dialog_cancel,
				new DialogInterface.OnClickListener() 
				{
					public void onClick(DialogInterface dialog, int whichButton) 
					{
						String mshow = "mShowing";
						try 
						{
							//ZZ DOSOMETING CHANGE A CLASS
							Field field = dialog.getClass().getSuperclass().getDeclaredField(mshow);
							field.setAccessible(true);
							// 将mShowing变量设为false，表示对话框已关闭
							field.set(dialog, true);
							dialog.dismiss();
						} 
						catch (Exception e) {}
					}
				});
		return builder.create();
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) 
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) item.getMenuInfo();
		switch (item.getItemId()) 
		{
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

	private void deleteNode(int position) 
	{
		if (hand.delElement(position)) 
		{
			feed = hand.getFeed();
			this.flipper.setInAnimation(null);
			this.flipper.setOutAnimation(null);
			addView();
			flipper.showNext();
			try 
			{
				FileOutputStream os = openFileOutput("op1x.xml", MODE_PRIVATE);
				String str = hand.xmltoStr();
				hand.Write(os, str);
			} 
			catch (FileNotFoundException e)
			{
				//ZZ DO SOMETING
			}
		}
	}

	private OPMLfeed getFeed() 
	{
		try 
		{
			InputStream in;
			try 
			{
				in = this.openFileInput("op1x.xml");
			} 
			catch (Exception e) 
			{
				in = getResources().openRawResource(R.raw.op);
			}
			hand = new OPMLHandler(in);
			return hand.getFeed();
		} 
		catch (Exception ee) 
		{
			return null;
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
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

	private void restoreDefault() 
	{
		this.deleteFile("op1x.xml");
		feed = getFeed();
		this.flipper.setInAnimation(null);
		this.flipper.setOutAnimation(null);
		addView();
		flipper.showNext();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		menu.add(0, 0, 0, "添加频道").setIcon(android.R.drawable.ic_menu_add);
		menu.add(0, 1, 0, "恢复默认数据").setIcon(android.R.drawable.ic_menu_revert);
		menu.add(0, 2, 0, "帮助").setIcon(android.R.drawable.ic_menu_help);
		menu.add(0, 3, 0, "关于旺财").setIcon(R.drawable.icon);
		return true;
	}

	private void showListView(View v) 
	{
		ListView itemlist = (ListView) v.findViewById(R.id.itemlist);
		registerForContextMenu(itemlist);
		if (feed == null) {
			this.htitle.setText("访问的OMPL无效");
			return;
		}
		@SuppressWarnings("unchecked")
		SimpleAdapter adapter = new SimpleAdapter(this,feed.getAllItemsForListView(), R.layout.listitem1,new String[] { "title" }, new int[] { R.id.ItemTitle });
		itemlist.setAdapter(adapter);
		itemlist.setOnItemClickListener(this);
		itemlist.setSelection(0);
		htitle.setText((CharSequence) feed.getTitle());
		itemlist.setOnTouchListener(new OnTouchListener() 
		{
			public boolean onTouch(View v, MotionEvent event) 
			{
				return detector.onTouchEvent(event);
			}
		});
		return;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		return detector.onTouchEvent(event);
	}

	private void go(int position) 
	{
		if (feed.getItem(position).getXmlUrl().equals("")) 
		{
			hand.setFeed(position, true);
			feed = hand.getFeed();
			addView();
			flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_in));
			flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_left_out));
			flipper.showNext();
		} 
		else 
		{
			Intent itemintent = new Intent(this, mainActive.class);
			Bundle b = new Bundle();
			b.putString("url", feed.getItem(position).getXmlUrl());
			itemintent.putExtra("android.intent.extra.rss", b);
			startActivityForResult(itemintent, 0);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) 
	{
		go(arg2);
	}

	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,ContextMenuInfo menuInfo) 
	{
		AdapterContextMenuInfo info = (AdapterContextMenuInfo) menuInfo;
		menu.setHeaderTitle(hand.getFeed().getItem(info.position).getTitle());
		menu.add(0, 0, 0, "阅读").setIcon(android.R.drawable.ic_menu_info_details);
		menu.add(0, 1, 0, "删除").setIcon(android.R.drawable.ic_menu_delete);
		menu.add(0, 2, 0, "返回").setIcon(android.R.drawable.ic_menu_close_clear_cancel);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) 
	{
		if (keyCode == KeyEvent.KEYCODE_BACK) 
		{
			if (hand.setFeed(-1, false)) 
			{
				feed = hand.getFeed();
				addView();
				this.flipper.setInAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_in));
				this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this,R.anim.push_right_out));
				this.flipper.showNext();
				return false;
			} 
			else 
			{
				showDialog(2);
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	
	@Override
	public boolean onTouch(View v, MotionEvent event) 
	{
		return false;
	}
	
	private class MySimpleGesture extends SimpleOnGestureListener 
	{
		// Touch了滑动一点距离后，up时触发
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) 
		{
			if (e2.getX() - e1.getX() > 50 && Math.abs(velocityX) > 50) 
			{
				if (hand.setFeed(-1, false)) {
					feed = hand.getFeed();
					addView();
					flipper.setInAnimation(AnimationUtils.loadAnimation(
							ActiveOPML.this, R.anim.push_right_in));
					flipper.setOutAnimation(AnimationUtils.loadAnimation(
							ActiveOPML.this, R.anim.push_right_out));
					unregisterForContextMenu((ListView) flipper
							.getCurrentView().findViewById(R.id.itemlist));
					flipper.showNext();
					return true;
				}
			}
			return false;
		}
	}

	private class task extends AsyncTask<String, String, String> 
	{
		// 后台执行的耗时任务，接收参数并返回结果
		// 当onPostExecute()执行完，在后台线程中被系统调用
		@Override
		protected String doInBackground(String... params) 
		{
			feed = getFeed();
			return "";
		}
		// 任务执行结束后，在UI线程中被系统调用
		// 一般用来显示任务已经执行结束
		@Override
		protected void onPostExecute(String result) 
		{
			addView();
			flipper.showNext();
			super.onPostExecute(result);
		}
	}



}
