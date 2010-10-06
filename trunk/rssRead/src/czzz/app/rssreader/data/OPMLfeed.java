package czzz.app.rssreader.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class OPMLfeed {
	
	private String title;
	private int itemcount = 0;
	private List<OutlineItem> itemlist;
	private String pos;
	
	public  OPMLfeed()
	{
		itemlist = new Vector(0); 
	}
	
	public int addItem(OutlineItem item)
	{
		itemlist.add(item);
		itemcount++;
		return itemcount;
	}
	public OutlineItem getItem(int location)
	{
		return itemlist.get(location);
	}
	public List getAllItems()
	{
		return itemlist;
	}
	public void delItems()
	{
		itemlist.clear();
	}
	public List getAllItemsForListView(){
		List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
		int size = itemlist.size();
		for(int i=0;i<size;i++){
			HashMap<String, Object>	item = new HashMap<String, Object>();
			item.put("title", itemlist.get(i).getTitle());
			item.put("xmlUrl", itemlist.get(i).getXmlUrl());
			data.add(item);
		}
		return data;
	}
	
	
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	
	public int getItemcount() {
		return itemcount;
	}

	public void setPos(String pos) {
		this.pos = pos;
	}

	public String getPos() {
		return pos;
	}
	
	

}
