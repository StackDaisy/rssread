package czzz.app.rssreader.sax;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

import czzz.app.rssreader.data.*;

public class RSSHandler extends DefaultHandler 
{
	
	RSSFeed rssFeed;
	RSSItem rssItem;
	String lastElementName = "";
	final int RSS_TITLE = 1;
	final int RSS_LINK = 2;
	final int RSS_DESCRIPTION = 3;
	final int RSS_CATEGORY = 4;
	final int RSS_PUBDATE = 5;
	final int URL = 6;
	boolean isInItem=false;
	boolean isInImage=false;
	int currentstate = 0;
	int maxcount=50;
	int curcount=0;
	private StringBuffer buf;

	public RSSHandler()
	{
	}
	
	public RSSFeed getFeed()
	{
		return rssFeed;
	}
	
	
	public void startDocument() throws SAXException
	{
		rssFeed = new RSSFeed();
		rssItem = new RSSItem();
		buf=new StringBuffer();
	}
	public void endDocument() throws SAXException
	{
	}
	public void startElement(String namespaceURI, String localName,String qName, Attributes atts) throws SAXException
	{
		if (localName.equals("channel"))
		{
			currentstate = 0;
			return;
		}
		if (localName.equals("item"))
		{curcount++;
			isInItem=true;
			rssItem = new RSSItem();
			return;
		}
		if (localName.equals("image"))
		{
			isInImage=true;
			return;
		}
		if (localName.equals("title"))
		{
			currentstate = RSS_TITLE;
			return;
		}
		if (localName.equals("description"))
		{
			currentstate = RSS_DESCRIPTION;
			return;
		}
		if (localName.equals("link"))
		{
			currentstate = RSS_LINK;
			return;
		}
		if (localName.equals("category"))
		{
			currentstate = RSS_CATEGORY;
			return;
		}
		if (localName.equals("pubDate"))
		{
			currentstate = RSS_PUBDATE;
			return;
		}
		if (localName.equals("url"))
		{
			currentstate = URL;
			return;
		}
		currentstate = 0;
	}
	
	public void endElement(String namespaceURI, String localName, String qName) throws SAXException
	{
		buf.toString().trim();
		String theString =buf.toString();
		buf.setLength(0);
		if (localName.equals("item"))
		{
			rssFeed.addItem(rssItem);
			isInItem=false;
			if (curcount >maxcount)
			{	throw new SAXException("stop by zz");
			}
			return;
			
				
		}
		if (localName.equals("image"))
		{
			isInImage=false;
			return;
		}
		
		
		switch (currentstate)
		{
			case RSS_TITLE:
				if (isInItem)
				{
						rssItem.setTitle(theString);
						if (curcount>maxcount)
						{this.endDocument();
						return;
						}
				}
				else if (isInImage)
				{
					rssFeed.setImageTile(theString);
				}
				else
				{
						rssFeed.setTitle(theString);
				}
				break;
			case RSS_LINK:
				if (isInItem)
				{
						rssItem.setLink(theString);
				}
				else if (isInImage)
				{
					rssFeed.setImageLink(theString);
				}
				else
				{
						rssFeed.setLink(theString);
				}
				break;
			case RSS_DESCRIPTION:
				if (isInItem)
				{
						rssItem.setDescription( theString);
				}
				else if (isInImage)
				{
					rssFeed.setImageDescription(theString);
				}
				else
				{
						rssFeed.setDescription( theString);
				}
				break;
			case RSS_CATEGORY:
				if (isInItem)
				{
						rssItem.setCategory(theString);
				}
				else
				{
						rssFeed.setCategory(theString);
				}
				break;
			case RSS_PUBDATE:
				if (isInItem)
				{
					rssItem.setPubDate(theString);
				}
				else
				{
						rssFeed.setPubDate(theString);
				}
				break;
			case URL:
				rssFeed.setImageURL(theString);
			default:
				return;
		}
		
	//如果解析一个item节点结束，就将rssItem添加到rssFeed中。

	}
	 
	public void characters(char ch[], int start, int length)
	{
		 buf.append(ch,start,length);		
	}
}
