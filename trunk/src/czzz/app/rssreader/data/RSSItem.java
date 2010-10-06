package czzz.app.rssreader.data;

import java.text.ParseException;

import czzz.base.string.*;

public class RSSItem 
{
	public static final String TITLE="title";
	public static final String PUBDATE="pubdate";	
	private String title = null;
	private String description = null;
	private String link = null;
	private String category = null;
	private String pubdate = null;

	
	public RSSItem()
	{
	}
	public void setTitle(String title)
	{
		this.title = Common.rb(title);
	}
	public	void setDescription(String description)
	{
		this.description = Common.rb(description);
	}
	public	void setLink(String link)
	{
		this.link = Common.rb(link);
	}
	public	void setCategory(String category)
	{
		this.category = Common.rb(category);
	}
	public	void setPubDate(String pubdate)
	{
		this.pubdate = pubdate;
	}
	public String getTitle()
	{
		return title;
	}
	public	String getDescription()
	{
		return description;
	}
	public	String getLink()
	{
		return link;
	}
	public String getCategory()
	{
		return category;
	}
	public String getPubDate()
	{
		return Common.ChangeDateString(pubdate);
		
	}
	public String toString()
	{
		if (title.length() > 20)
		{
			return title.substring(0, 42) + "...";
		}
		return title;
	}
}
