package pl.mikolajkarebski.tomoto.handlers;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.mikolajkarebski.tomoto.adapters.OfertaAdapter;
import pl.mikolajkarebski.tomoto.constants.URL;
import pl.mikolajkarebski.tomoto.model.Oferta;
import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;

public class ListaOfertHandler extends AsyncHttpResponseHandler {

	List<Oferta> listatmp;
	OfertaAdapter adapter;	
	
	public ListaOfertHandler(List<Oferta> lista, OfertaAdapter adapter) {
		listatmp = lista;
		this.adapter = adapter;
	}
	
	@Override
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
		Oferta oferta = null;
		NodeList nList = doc.getElementsByTagName("oferta");
		for (int temp = 0; temp < nList.getLength(); temp++) {
			Node node = nList.item(temp);
			Element o = (Element) node;
		    oferta = new Oferta();
		    oferta.id = o.getElementsByTagName("id").item(0).getTextContent();
		    oferta.marka = o.getElementsByTagName("marka").item(0).getTextContent();
			oferta.model = o.getElementsByTagName("model").item(0).getTextContent();
			oferta.rocznik = o.getElementsByTagName("rok").item(0).getTextContent();
			oferta.opis = o.getElementsByTagName("rok").item(0).getTextContent();
			oferta.przebieg = o.getElementsByTagName("przebieg").item(0).getTextContent();
			oferta.cena = o.getElementsByTagName("cena").item(0).getTextContent();
			oferta.wojewodztwo = o.getElementsByTagName("wojewodztwo").item(0).getTextContent();
			oferta.miejsce = o.getElementsByTagName("miasto").item(0).getTextContent();
			oferta.email = o.getElementsByTagName("email").item(0).getTextContent();
			oferta.obrazek = "http://lorempixel.com/800/600/sports";//o.getChild("obrazek").getText();
			oferta.dataZakonczenia = o.getElementsByTagName("data_zakonczenia").item(0).getTextContent();
			Log.d("oferta", oferta.toString());
			listatmp.add(oferta);
			adapter.notifyDataSetChanged();
		}
    }

    @Override
    public void onFailure(int statusCode, Throwable error, String content) {
    	Log.d(URL.szukajWS, "Nie mozna sie polaczyc");
    }

    public List<Oferta> getLista() {
    	Log.d("listaOfert2",listatmp.toString());
    	return listatmp;
    }
    
}
