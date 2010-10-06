package czzz.app.rssreader;



import czzz.app.rssreader.data.RSSFeed;
import czzz.app.rssreader.data.RSSItem;
import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.*;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;

public class ActivityShowDescription extends CustomWindow implements OnGestureListener  {
	TextView textView;
	TextView tvtitle;
	int pos;
	ViewFlipper flipper;
	ScrollView sc;

	private GestureDetector detector;//触摸监听实例 
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.showdescription);
		
		detector = new GestureDetector(this);
		flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper01);
		
		//	flipper.addView(abc);
		
		//this.onTouch(this);
		
	
		Intent startingIntent = getIntent();
		if (startingIntent != null) {
			Bundle bundle = startingIntent
					.getBundleExtra("android.intent.extra.rssItem");
			if (bundle == null) {
				htitle.setText("不好意思程序出错啦");
			} else {
				htitle.setText( mainActive.feed.getTitle());
				pos=bundle.getInt("pos");
				addView();
				flipper.setOnTouchListener(new OnTouchListener() {              
		     	   public boolean onTouch(View v, MotionEvent event) {   
			     	        return detector.onTouchEvent(event);   
			     	    }   
			     	});
				//showActive();
		/*		if (pos!=0)
			//	{	
				//	flipper.addView(addView(pos-1));
				}
				if (pos+1<mainActive.feed.getItemcount())
				{
					flipper.addView(addView(pos+1));
				}
			*/
				
				//		showActive(pos,findViewById(R.layout.showdescription));
	
			}
		} else {
			htitle.setText("不好意思程序出错啦");
		}

	//	Button backbutton = (Button) findViewById(R.id.back);
	//	backbutton.setVisibility(View.GONE);
		//		backbutton.setOnClickListener(new Button.OnClickListener() {
//			public void onClick(View v) {
//				finish();
//			}
//		});
		
		//flipper.setOnTouchListener(l)
		}	
	
	private void addView()
	{
		LayoutInflater inflater = getLayoutInflater();
		try
		{
			flipper.removeAllViews();
			//flipper.removeViewAt(2);
		
		}
		catch(Exception e){}
		int i=0;
		if (pos!=0)
				{	
					View layout = inflater.inflate(R.layout.desclone,null);
					showActive(pos-1,layout);
					flipper.addView(layout);
					i++;

				}
			
			View layoutx = inflater.inflate(R.layout.desclone,null);
			showActive(pos,layoutx);
			flipper.addView(layoutx);
			
			flipper.setDisplayedChild(i);
			i++;

		
				if (pos+1<mainActive.feed.getItemcount())
				{
					View layout1 = inflater.inflate(R.layout.desclone,null);
					showActive(pos+1,layout1);
					flipper.addView(layout1);
					
				}		

		//	TextView text　=　(TextView)　layout.findViewById(R.id.text);　
	}

//	private void showActive() {
//		RSSItem item=mainActive.feed.getItem(pos);
//		String title=item.getTitle(); 
//		String content =  "　　<p><p>"
//				+ item.getPubDate() + "<p><p>　　"
//				+ item.getDescription().replace("\n", "<p>")
//				+ "<p><p><a href="+item.getLink()+">点击获取详细信息</a>" ;
//		textView = (TextView) findViewById(R.id.content);
//		tvtitle=(TextView) findViewById(R.id.Rtitle);
//		
//		textView.setText(Html.fromHtml(content) );
//		try
//		{
//		sc.scrollTo(10, 10);
//		}
//		catch(Exception e){}
//		tvtitle.setText(title);
////	//	Button prebutton=(Button)findViewById(R.id.PreButton);
////	//	Button nextbutton=(Button)findViewById(R.id.NextButton);
////		if (pos!=0)
////		{
////			prebutton.setVisibility(View.GONE);
////			prebutton.setOnClickListener(new Button.OnClickListener() {
////				public void onClick(View v) {
////					pos--;
////					showActive();
////				}
////			});
////		}
////		else
////		{
////			prebutton.setVisibility(View.GONE);	
////		}
////		if (pos+1<mainActive.feed.getItemcount())
////		{
////			nextbutton.setVisibility(View.GONE);
////			nextbutton.setOnClickListener(new Button.OnClickListener() {
////				public void onClick(View v) {
////					pos++;
////					showActive();
////				}
////			});
////		}
////		else
////		{
////			nextbutton.setVisibility(View.GONE);
////		}
//		
//		// TODO Auto-generated method stub
//		
//	}

	private int getLines()
	{
		   WindowManager windowManager = getWindowManager(); 
		   Display display = windowManager.getDefaultDisplay(); 
		   //int w = display.getWidth(); 
		   int h = display.getHeight();
		   return (int)((h-132)/24.86)+1;
		
	}

	private void showActive(int position,View v) {
		RSSItem item=mainActive.feed.getItem(position);
		String title=item.getTitle(); 
		String content =  "　　<p><p>"
				+ item.getPubDate() + "<p><p>　　"
				+ item.getDescription().replace("\n", "<p>");
		textView = (TextView) v.findViewById(R.id.content);
		tvtitle=(TextView) v.findViewById(R.id.Rtitle);
		//textView.setMaxLines(getLines());
		//textView.setMovementMethod(ScrollingMovementMethod.getInstance()); 
		textView.setText(Html.fromHtml(content) );
		tvtitle.setText(Html.fromHtml("<a href="+item.getLink()+">"+title+"</a>"));
		
		View sc=v.findViewById(R.id.sc);

	sc.setOnTouchListener(new OnTouchListener() {              
  	   public boolean onTouch(View v, MotionEvent event) {   
  		   return detector.onTouchEvent(event);   
  	   }
     	       
     	});
	tvtitle.setMovementMethod(LinkMovementMethod.getInstance()); 
	 
		
		
		// TODO Auto-generated method stub
		
	}

	
	
	 @Override
		public boolean onTouchEvent(MotionEvent event) {
			 return this.detector.onTouchEvent(event);    
		}

	
	@Override
	public boolean onDown(MotionEvent arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
			float velocityY) {
		if (e1.getX() - e2.getX() > 50 && Math.abs(velocityX) > 50) 
        {
			if (pos+1<mainActive.feed.getItemcount())
			{
						pos++;
						this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_in));
						this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_left_out));
						//showActive();
						this.flipper.showNext();
						addView();
			}
        }
		if (e2.getX() - e1.getX() > 50 && Math.abs(velocityX) > 50) 
        {
			if (pos!=0)
			{
						pos--;
						this.flipper.setInAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_in));
						this.flipper.setOutAnimation(AnimationUtils.loadAnimation(this, R.anim.push_right_out));
						//showActive();
						
						this.flipper.showPrevious();
						addView();
			}
        }
		// TODO Auto-generated method stub
		return 	false;
	}

	@Override
	public void onLongPress(MotionEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
			float distanceY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onShowPress(MotionEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

}
