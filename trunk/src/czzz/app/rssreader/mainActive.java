package czzz.app.rssreader;

import info.monitorenter.cpdetector.io.ASCIIDetector;
import info.monitorenter.cpdetector.io.CodepageDetectorProxy;
import info.monitorenter.cpdetector.io.JChardetFacade;
import info.monitorenter.cpdetector.io.ParsingDetector;
import info.monitorenter.cpdetector.io.UnicodeDetector;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnKeyListener;
import android.gesture.Gesture;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector.OnGestureListener;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.AdapterView.OnItemClickListener;
import czzz.app.rssreader.data.RSSFeed;
import czzz.app.rssreader.sax.RSSHandler;


public class mainActive extends CustomWindow implements OnItemClickListener

{
	//public final File RSS_URL = "http://news.sohu.com/rss/pfocus.xml";
	
//public final String RSS_URL = "http://news.sohu.com/rss/pfocus.xml";
	
	public final String tag = "RSSReader";
	public static RSSFeed feed = null;

	     


    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.main);
        
    	Intent startingIntent = getIntent();
		if (startingIntent != null) {
			Bundle bundle = startingIntent
					.getBundleExtra("android.intent.extra.rss");
			
			if (bundle != null) {
				final ProgressDialog myProgressDialog = ProgressDialog.show(mainActive.this,     
                         "请稍后", "正在获取网络数据", true);
               new AsyncTask<String, Void, RSSFeed>(){
                   @Override
                   protected void onPostExecute(RSSFeed result) {
                	   feed = result;
   					htitle.setText(feed.getTitle());
   					  showListView();
   					myProgressDialog.dismiss();
                   }
				@Override
				protected RSSFeed doInBackground(String... params) {
					 return getFeed(params[0]);
				}
               }.execute(bundle.getString("url"));
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
				}
			  
			}
		}
    
	
    
    private RSSFeed getFeed(String urlString) 
    {

    	   URL url=null;
		try {
			url = new URL(urlString);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	   CodepageDetectorProxy detector =   CodepageDetectorProxy.getInstance(); 
    	 //  detector.add(new ParsingDetector(false));
       detector.add(JChardetFacade.getInstance());   
    	 //ASCIIDetector用于ASCII编码测定   
    	 detector.add(ASCIIDetector.getInstance());   
    	 //UnicodeDetector用于Unicode家族编码的测定   
    	 detector.add(UnicodeDetector.getInstance());   
    	 Charset code = null;
		try {
			code = detector.detectCodepage(url);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}   
    	 InputSource is = null;    	 
    	 if("UTF-8".equalsIgnoreCase(code.name())){
 			try {
				is = new InputSource(url.openStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    	 }else{
    		    			InputStream stream = null;
							try {
								stream = url.openStream();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
    		    			InputStreamReader streamReader = null;
							try {
								streamReader = new InputStreamReader(stream,"GBK");
							} catch (UnsupportedEncodingException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}	
    		    			is = new InputSource(streamReader);
    		}
    	   SAXParserFactory factory = SAXParserFactory.newInstance();
           SAXParser parser = null;
		try {
			parser = factory.newSAXParser();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
           XMLReader xmlreader = null;
		try {
			xmlreader = parser.getXMLReader();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
           
           RSSHandler rssHandler = new RSSHandler();
           xmlreader.setContentHandler(rssHandler);
        
           try {
			xmlreader.parse(is);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			if (!e.getMessage().equals("stop by zz"))
			{return null;
			
			}
			else
				feed=rssHandler.getFeed();
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
  
           return rssHandler.getFeed();
    	
    }

    
    private void showListView() 
    {
        ListView itemlist = (ListView) findViewById(R.id.itemlist);     
        if (feed == null)
        {
        	setTitle("访问的RSS无效");
        	return;
        }
        
        SimpleAdapter adapter = new SimpleAdapter(this,   feed.getAllItemsForListView(),
       		 R.layout.listitem, new String[] { "title","pubdate" },
       		 new int[] { R.id.ItemTitle , R.id.ItemText});
        itemlist.setAdapter(adapter);
        itemlist.setOnItemClickListener(this);  
        itemlist.setSelection(0);
        
       // TextView tv=(TextView)findViewById(R.id.TextView01);
       // tv.setText((CharSequence)feed.getTitle());
        htitle.setText((CharSequence)feed.getTitle());
        if (feed.getImageURL()!=null)
        {
        	ImageView imageView =(ImageView)findViewById(R.id.ImageView01);
        	TextView  t=(TextView)findViewById(R.id.widget30);
            URL picUrl = null;
      		try {
      			picUrl = new URL(feed.getImageURL());
      		} catch (MalformedURLException e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		}
      		if (picUrl!=null)
      		{
            Bitmap pngBM=null;
      		try {
      			pngBM = BitmapFactory.decodeStream(picUrl.openStream());
      		} catch (IOException e) {
      			// TODO Auto-generated catch block
      			e.printStackTrace();
      		}
      		imageView.setVisibility(View.VISIBLE);
  			t.setVisibility(View.VISIBLE);
            imageView.setImageBitmap(pngBM);        
      		}
      		else
      		{
      			imageView.setVisibility(View.GONE);
      			t.setVisibility(View.GONE);
      		}
      	}
        
    }
    
    
     public void onItemClick(AdapterView parent, View v, int position, long id)
     {
    	 Intent itemintent = new Intent(this,ActivityShowDescription.class);
         
    	 Bundle b = new Bundle();
    	 /*b.putString("title", feed.getItem(position).getTitle());
    	 b.putString("description", feed.getItem(position).getDescription());
    	 b.putString("link", feed.getItem(position).getLink());
    	 b.putString("pubdate", feed.getItem(position).getPubDate());
    	 */
    	 b.putInt("pos",position);
    	 itemintent.putExtra("android.intent.extra.rssItem", b);
         startActivityForResult(itemintent, 0);
     }


	


    
}