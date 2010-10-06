package czzz.app.rssreader;



import czzz.app.rssreader.data.RSSItem;
import android.os.Bundle;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.content.Intent;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.*;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.view.animation.AnimationUtils;

public class ActivityShowDescription extends CustomWindow implements OnGestureListener  
{
	TextView textView;
	TextView tvtitle;
	int pos;
	ViewFlipper flipper;
	ScrollView sc;
	private GestureDetector detector;//触摸监听实例 

	public void onCreate(Bundle icicle) 
	{
		super.onCreate(icicle);
		setContentView(R.layout.showdescription);
		detector = new GestureDetector(this);
		flipper = (ViewFlipper) this.findViewById(R.id.ViewFlipper01);
		Intent startingIntent = getIntent();
		if (startingIntent != null) 
		{
			Bundle bundle = startingIntent.getBundleExtra("android.intent.extra.rssItem");
			if (bundle == null) 
			{
				//ZZ DO SOMETING showdilog
				htitle.setText("不好意思程序出错啦");
			} 
			else 
			{
				htitle.setText( mainActive.feed.getTitle());
				pos=bundle.getInt("pos");
				addView();
				flipper.setOnTouchListener(new OnTouchListener() 
											{              
									     	   public boolean onTouch(View v, MotionEvent event) 
									     	   {   
									     		   return detector.onTouchEvent(event);   
									 		   }   
									     	});
			}
		} 
		else 
		{
			//ZZ DO SOMETING showdilog
			htitle.setText("不好意思程序出错啦");
		}
	}	
	
	private void addView()
	{
		LayoutInflater inflater = getLayoutInflater();
		try
		{
			flipper.removeAllViews();
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
	}

	private void showActive(int position,View v) 
	{
		RSSItem item=mainActive.feed.getItem(position);
		String title=item.getTitle(); 
		String content =  "　　<p><p>"
				+ item.getPubDate() + "<p><p>　　"
				+ item.getDescription().replace("\n", "<p>");
		textView = (TextView) v.findViewById(R.id.content);
		tvtitle=(TextView) v.findViewById(R.id.Rtitle);
		textView.setText(Html.fromHtml(content) );
		tvtitle.setText(Html.fromHtml("<a href="+item.getLink()+">"+title+"</a>"));
		View sc=v.findViewById(R.id.sc);
		sc.setOnTouchListener(new OnTouchListener() 
		{              
			public boolean onTouch(View v, MotionEvent event) 
			{   
				return detector.onTouchEvent(event);   
			}
     	});
		tvtitle.setMovementMethod(LinkMovementMethod.getInstance()); 
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) 
	{
		 return this.detector.onTouchEvent(event);    
	}

	@Override
	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,float velocityY) 
	{
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
				this.flipper.showPrevious();
				addView();
			}
        }
		return 	false;
	}

	
	@Override
	public boolean onDown(MotionEvent e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLongPress(MotionEvent e) {
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
