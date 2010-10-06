package czzz.app.rssreader.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OutlineItem {
	
	private String text;
	private String title;
	private String type;
	private String xmlUrl;
	private String htmlUrl;
	private int itemcount = 0;
	private List<OutlineItem> itemlist;
	public OutlineItem()
	{}
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
	
	
	public void setText(String text) {
		this.text = text;
	}
	public String getText() {
		return text;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getTitle() {
		return title;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setXmlUrl(String xmlUrl) {
		this.xmlUrl = xmlUrl;
	}
	public String getXmlUrl() {
		return xmlUrl;
	}
	public void setHtmlUrl(String htmlUrl) {
		this.htmlUrl = htmlUrl;
	}
	public String getHtmlUrl() {
		return htmlUrl;
	}
	
}
