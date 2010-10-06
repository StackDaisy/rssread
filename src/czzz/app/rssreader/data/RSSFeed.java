package czzz.app.rssreader.data;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import czzz.base.string.*;
public class RSSFeed 
{
	private String title = null;
	private String pubdate = null;
	private int itemcount = 0;
	private List<RSSItem> itemlist;
	private String category;
	private String description;
	private String link;
	private String imageTile;
	private String imageLink;
	private String imageURL;
	private String imageDescription;
	
	
	public RSSFeed()
	{
		itemlist = new Vector(0); 
	}
	public int getItemcount()
	{
		return itemcount;
	}
	public int addItem(RSSItem item)
	{
		itemlist.add(item);
		itemcount++;
		return itemcount;
	}
	public RSSItem getItem(int location)
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
			item.put(RSSItem.TITLE, itemlist.get(i).getTitle());
			item.put(RSSItem.PUBDATE, itemlist.get(i).getPubDate());
			data.add(item);
		}
		return data;
	}
	int getItemCount()
	{
		return itemcount;
	}
	public void setTitle(String title)
	{
		this.title = Common.rb(title) ;
	}
	public void setPubDate(String pubdate)
	{
		this.pubdate =Common.rb( pubdate);
	}
	public String getTitle()
	{
		return title;
	}
	public String getPubDate()
	{
		return pubdate;
	}
	public String getCategory() {
		// TODO Auto-generated method stub
		return category;
	}
	public void setCategory(String category) {
		this.category=Common.rb (category);
		// TODO Auto-generated method stub
		
	}
	public String  getDescription() {
		return description;
		// TODO Auto-generated method stub
	}
	public void setDescription(String description) {
		this.description=Common.rb(description);
		// TODO Auto-generated method stub
		
	}
	public String  getLink() {
		return link;
		// TODO Auto-generated method stub
	}
	public void setLink(String link) {
		this.link=Common.rb( link);
		// TODO Auto-generated method stub
		
	}
	public void setImageTile(String imageTile) {
		this.imageTile = Common.rb(imageTile);
	}
	public String getImageTile() {
		return imageTile;
	}
	public void setImageLink(String imageLink) {
		this.imageLink = Common.rb(imageLink);
	}
	public String getImageLink() {
		return imageLink;
	}
	public void setImageURL(String imageURL) {
		this.imageURL = Common.rb(imageURL);
	}
	public String getImageURL() {
		return imageURL;
	}
	public void setImageDescription(String imageDescription) {
		this.imageDescription = Common.rb(imageDescription);
	}
	public String getImageDescription() {
		return imageDescription;
	}
	
	
	
}
