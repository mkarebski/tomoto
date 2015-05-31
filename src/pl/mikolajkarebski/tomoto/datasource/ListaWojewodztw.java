package pl.mikolajkarebski.tomoto.datasource;

import pl.mikolajkarebski.tomoto.constants.URL;
import pl.mikolajkarebski.tomoto.handlers.SpinnerHandler;
import android.widget.ArrayAdapter;

import com.loopj.android.http.AsyncHttpClient;

public class ListaWojewodztw {

	public static void getLista(final ArrayAdapter<String> adapter) {
		AsyncHttpClient client = new AsyncHttpClient();
		SpinnerHandler handler = new SpinnerHandler(adapter, "wojewodztwa");
        client.get(URL.socket+URL.wojewodztwoWS, handler);
	}
	
}
