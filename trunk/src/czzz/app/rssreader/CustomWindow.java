package czzz.app.rssreader;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;
import android.widget.ImageView;

public class CustomWindow extends Activity {
	protected TextView htitle;
	protected ImageView hicon;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
        
        setContentView(R.layout.opml);
        
        getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.window_title);
        
        htitle = (TextView) findViewById(R.id.title0);
        hicon  = (ImageView) findViewById(R.id.header);
	}
}