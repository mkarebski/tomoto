package pl.mikolajkarebski.tomoto.handlers;

import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import android.util.Log;
import android.widget.ArrayAdapter;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class SpinnerHandler extends AsyncHttpResponseHandler {
    
	ArrayAdapter<String> adapter;
	String xmltag;
	
	public SpinnerHandler(ArrayAdapter<String> adapter, String xmltag) {
		this.adapter = adapter;
		this.xmltag = xmltag;
	}

    public void onSuccess(String response) {       
	    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
	   	DocumentBuilder dBuilder = null;
	   	try {
	   		dBuilder = dbFactory.newDocumentBuilder();
	   	} catch (ParserConfigurationException e1) {
	   		e1.printStackTrace();
		}
	    InputSource is = new InputSource(new StringReader(response));
	    Document doc = null;
		try {
			doc = (Document) dBuilder.parse(is);
		} catch (SAXException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	    if (doc == null) return;
		NodeList nList = doc.getElementsByTagName(xmltag);
		adapter.add("");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node node = nList.item(temp);
			Element e = (Element) node;
		    adapter.add(e.getAttribute("nazwa"));
		    adapter.notifyDataSetChanged();
		}
		
	}

    public void onFailure(int statusCode, Throwable error, String content) {
    	Log.d(xmltag, String.valueOf(statusCode));
    	Log.d(xmltag, error.getMessage());
    }
}
