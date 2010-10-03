package czzz.app.rssreader.sax;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;
import org.xmlpull.v1.XmlSerializer;

import android.provider.ContactsContract.CommonDataKinds.Note;
import android.util.Xml;

import czzz.app.rssreader.data.OPMLfeed;
import czzz.app.rssreader.data.OutlineItem;

//import org.

//import czzz.app.rssreader.data.*;

public class OPMLHandler {
	OPMLfeed opmlFeed;
	OutlineItem outlineItem;
	OutlineItem baseItem;
	String lastElementName = "";
	String otitle = "";
	Boolean isroot = true;
	final int OPML_TITLE = 1;
	final int OUTLINE = 2;
	final int OUTLINE_TTTLE = 3;
	final int OUTLINE_XMLURL = 4;
	final int OUTLINE_HTMLUR = 5;
	final int OUTLINE_TYPE = 6;
	int currentstate = 0;
	Document doc;
	Node rnode;
	Node curnode;

	public OPMLHandler(InputStream in) {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		try {
			builder = factory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		try {
			doc = builder.parse(in);
			setFeed();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public OPMLfeed getFeed() {
		return opmlFeed;
	}

	public void setFeed() {
		opmlFeed = new OPMLfeed();
		otitle = doc.getElementsByTagName("title").item(0).getFirstChild()
				.getNodeValue();
		opmlFeed.setTitle(otitle);
		rnode = doc.getElementsByTagName("body").item(0);
		curnode = rnode;
		parseElement((Element) curnode);

	}

	public boolean setFeed(int pos, boolean isnext) {
		boolean rval = true;
		if (isnext) {
			getCurnode(pos);
		} else {
			if (curnode == rnode)
				return false;
			curnode = curnode.getParentNode();
		}
		if (curnode == rnode) {
			opmlFeed.setTitle(otitle);
		} else {
			opmlFeed.setTitle(curnode.getAttributes().getNamedItem("title")
					.getNodeValue());

		}
		parseElement((Element) curnode);
		return rval;
	}

	private void getCurnode(int pos) {
		NodeList list = curnode.getChildNodes();
		int j = 0;
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().equals("outline")) {
				if (j == pos) {
					curnode = list.item(i);
					break;
				}
				j++;
			}
		}
	}

	public boolean addElement(String type, String url, String title) {

		if (type == "rss") {
			Element newel = doc.createElement("outline");
			newel.setAttribute("title", title);
			newel.setAttribute("xmlUrl", url);
			try {

				curnode.insertBefore(newel, curnode.getChildNodes().item(0));
			} catch (Exception e) {
				curnode.appendChild(newel);
			}
			parseElement((Element) curnode);
		} else {
			Document doc1 = null;
			URL url1 = null;
			try {
				url1 = new URL(url);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return false;
			}
			DocumentBuilderFactory factory = DocumentBuilderFactory
					.newInstance();
			DocumentBuilder builder = null;
			try {
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

			try {
				doc1 = builder.parse(url1.openStream());
				Element newel = doc.createElement("outline");
				newel.setAttribute("title", title);
				Node tnode = doc1.getElementsByTagName("body").item(0);
				Element teml = (Element) tnode;
				// newel.appendChild(tnode);
				try {
					dupliate(doc, newel, teml);
				} catch (Exception e1) {
					// TODO Auto-generatcd catch block
					e1.printStackTrace();
					return false;
				}
				try {
					curnode
							.insertBefore(newel, curnode.getChildNodes()
									.item(0));
				} catch (Exception e) {
					curnode.appendChild(newel);
				}
				parseElement((Element) curnode);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}

		}

		return true;

	}

	// /
	// /
	private boolean dupliate(Document doc_dup, Element father, Element son)
			throws Exception {
		Boolean is_done = false;
		String son_name = son.getNodeName();
		Element sub_ITEM = doc_dup.createElement(son_name);
		// 复制节点的属性
		if (son.hasAttributes()) {
			NamedNodeMap attributes = son.getAttributes();
			for (int i = 0; i < son.getAttributes().getLength(); i++) {
				String attribute_name = attributes.item(i).getNodeName();
				String attribute_value = attributes.item(i).getNodeValue();
				sub_ITEM.setAttribute(attribute_name, attribute_value);
			}
		}
		if (son_name.equals("outline"))
			father.appendChild(sub_ITEM);
		if (son_name.equals("body"))
			sub_ITEM = father;
		// 复制节点的值
		// Text value_son = (Text) son.getFirstChild ();
		// String nodevalue_root = "";
		// if (value_son!= null && value_son.getLength () > 0) nodevalue_root =
		// (String) value_son.getNodeValue ();
		// Text valuenode_root = null;
		// if ((nodevalue_root!= null)&&(nodevalue_root.length () > 0))
		// valuenode_root = doc_dup.createTextNode (nodevalue_root);
		// if (valuenode_root!= null && valuenode_root.getLength () > 0)
		// sub_ITEM.appendChild (valuenode_root);
		// 复制子结点
		NodeList sub_messageItems = son.getChildNodes();
		int sub_item_number = sub_messageItems.getLength();
		if (sub_item_number < 2) {
			// 如果没有子节点,则返回
			is_done = true;
		} else {
			for (int j = 0; j < sub_item_number; j++) {
				// 如果有子节点,则递归调用本方法
				if (sub_messageItems.item(j).getNodeType() == Element.ELEMENT_NODE) {
					Element sub_messageItem = (Element) sub_messageItems
							.item(j);
					is_done = dupliate(doc_dup, sub_ITEM, sub_messageItem);
				}
			}
		}
		return is_done;
	}

	public String xmltoStr() {
		XmlSerializer serializer = Xml.newSerializer();
		StringWriter writer = new StringWriter();
		try {
			serializer.setOutput(writer);
			serializer.startDocument("UTF-8", true);
			Element root = doc.getDocumentElement();
			dealNode(root, serializer);
			serializer.endDocument();

			return writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	private void dealNode(Node node, XmlSerializer serializer) {
		try {
			if (node.getNodeType() == 3) {
				serializer.text(node.getNodeValue());
				return;
			}

			serializer.startTag("", node.getNodeName());
			// 把本node的text写入seriali
			String text = node.getNodeValue();
			if (text != null)
				serializer.text(text);
			// 先把node属性写入
			// =====================================
			NamedNodeMap map = node.getAttributes();
			int attrSize = map.getLength();
			for (int i = 0; i < attrSize; i++) {
				// 得到的是node的属性节点
				Node Attrnode = map.item(i);
				// 写出名字和
				String name = Attrnode.getNodeName();
				String value = Attrnode.getNodeValue();
				serializer.attribute("", name, value);

			}
			// =====================================

			// 再写node的各个子childnode
			// =====================================
			NodeList childs = node.getChildNodes();
			int nodeSize = childs.getLength();
			for (int i = 0; i < nodeSize; i++) {
				// 得到的是node的属性节点
				Node Childnode = childs.item(i);
				dealNode(Childnode, serializer);
			}
			serializer.endTag("", node.getNodeName());

		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public boolean Write(FileOutputStream os, String xmlstr) {
		try {
			// FileOutputStream fos = openFileOutput(FILE_NAME, 0);//
			// OutputStream os = OpenFileOutput (path, false);
			OutputStreamWriter osw = new OutputStreamWriter(os);
			osw.write(xmlstr);
			osw.close();
			os.close();
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
		return true;
	}

	// /
	// /
	public void parseElement(Element root) {
		opmlFeed.delItems();
		outlineItem = new OutlineItem();
		NodeList list = root.getChildNodes();
		for (int i = 0; i < list.getLength(); i++) {
			Node node = list.item(i);
			if (!node.getNodeName().equals("outline"))
				continue;
			Element e = (Element) node;
			outlineItem.setTitle(e.getAttribute("title"));
			outlineItem.setXmlUrl(e.getAttribute("xmlUrl"));
			opmlFeed.addItem(outlineItem);
			outlineItem = new OutlineItem();
		}
	}
	public boolean delElement(int position)
	{
		NodeList list = curnode.getChildNodes();
		Node tnode=null;
		int j = 0;
		for (int i = 0; i < list.getLength(); i++) {
			if (list.item(i).getNodeName().equals("outline")) {
				if (j == position) {
					tnode = list.item(i);
					break;
				}
				j++;
			}
		}
		if(tnode!=null)
		{
			curnode.removeChild(tnode);
			parseElement((Element) curnode);
			return true;
		}
		return false;
	}

}

