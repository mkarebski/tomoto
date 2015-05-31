package pl.mikolajkarebski.tomoto.datasource;


import java.util.List;

import pl.mikolajkarebski.tomoto.adapters.OfertaAdapter;
import pl.mikolajkarebski.tomoto.constants.URL;
import pl.mikolajkarebski.tomoto.handlers.ListaOfertHandler;
import pl.mikolajkarebski.tomoto.model.Oferta;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class ListaOfert {

	public static List<Oferta> getListaOfert(List<Oferta> listaOfert, final OfertaAdapter adapter) {
		final List<Oferta> listatmp = listaOfert;
		AsyncHttpClient client = new AsyncHttpClient();
		ListaOfertHandler handler = new ListaOfertHandler(listatmp, adapter);
        client.get(URL.socket, handler);
        return handler.getLista();
	}

	public static List<Oferta> getListaOfert(List<Oferta> listaOfert, final OfertaAdapter adapter, RequestParams rp) {
		final List<Oferta> listatmp = listaOfert;
		AsyncHttpClient client = new AsyncHttpClient();
		ListaOfertHandler handler = new ListaOfertHandler(listatmp, adapter);
        client.get(URL.socket+URL.szukajWS, rp, handler);
        return handler.getLista();
	}
	
	
}

