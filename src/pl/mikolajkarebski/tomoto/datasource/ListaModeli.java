package pl.mikolajkarebski.tomoto.datasource;

import pl.mikolajkarebski.tomoto.constants.URL;
import pl.mikolajkarebski.tomoto.handlers.SpinnerHandler;
import android.widget.ArrayAdapter;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.RequestParams;

public class ListaModeli {

	public static void getLista(final ArrayAdapter<String> adapter, RequestParams rp) {
		AsyncHttpClient client = new AsyncHttpClient();
		SpinnerHandler handler = new SpinnerHandler(adapter, "samochod");
        client.get(URL.socket+URL.samochodWS, rp, handler);
	}
	
	
}
