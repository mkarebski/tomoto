package pl.mikolajkarebski.tomoto.activities;

import java.util.ArrayList;

import pl.mikolajkarebski.tomoto.R;
import pl.mikolajkarebski.tomoto.adapters.OfertaAdapter;
import pl.mikolajkarebski.tomoto.constants.Utility;
import pl.mikolajkarebski.tomoto.datasource.ListaOfert;
import pl.mikolajkarebski.tomoto.model.Oferta;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.loopj.android.http.RequestParams;

public class MainActivity extends ListActivity {

	ArrayList<Oferta> listaOfert = new ArrayList<Oferta>();
	OfertaAdapter adapter = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		adapter = new OfertaAdapter(this, R.layout.oferta_list_item, listaOfert);
		if(getIntent().getExtras() != null) {
			Log.d("intencjaExtras","nie jest nullem");
			RequestParams rp = getParams();
			ListaOfert.getListaOfert(listaOfert, adapter, rp);
		} else {
			Log.d("intencjaExtras","jest nullem");
			ListaOfert.getListaOfert(listaOfert, adapter);
		}
		adapter.notifyDataSetChanged();
		setListAdapter(adapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id){ 
		case R.id.action_previous: finish();
		case R.id.action_refresh: 	
			adapter.clear();
			if(getIntent().getExtras() != null) {
				RequestParams rp = getParams();
				ListaOfert.getListaOfert(listaOfert, adapter, rp);
			} else {
				ListaOfert.getListaOfert(listaOfert, adapter);
			}
			break;
		case R.id.action_search: 	
			Intent intent = new Intent(this,SearchActivity.class);
			startActivity(intent);
			break;
		case R.id.action_add:
			Intent intent1 = new Intent(this,DodajActivity.class);
			startActivity(intent1);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private RequestParams getParams() {
		Intent i = getIntent();
		String wojew = i.getStringExtra("wojewodztwo");
		String miasto = i.getStringExtra("miasto");
		String marka = i.getStringExtra("marka");
		String model = i.getStringExtra("model");
		
		RequestParams rp = new RequestParams();
		if(Utility.isNotNull(wojew))  rp.add("wojewodztwo", wojew);
		if(Utility.isNotNull(wojew) && Utility.isNotNull(miasto)) rp.add("miasto", miasto);
		if(Utility.isNotNull(marka)) rp.add("marka", marka);
		if(Utility.isNotNull(marka) && Utility.isNotNull(model)) rp.add("model", model);	
		return rp;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		String msg = null;
        if(listaOfert == null) msg="lista jest nullem"; else msg="lista nie jest nullem";
        Log.d("opis",msg);
		
		Oferta oferta = listaOfert.get(position);
		Intent intent = new Intent(this, OfertaDetailActivity.class );
		intent.putExtra("id", oferta.id);         
		intent.putExtra("marka", oferta.marka);         
		intent.putExtra("model", oferta.model);         
		intent.putExtra("rocznik", oferta.rocznik);     
		intent.putExtra("cena", oferta.cena);           
		intent.putExtra("miejsce", oferta.miejsce);     
		intent.putExtra("obrazek", oferta.obrazek);
		intent.putExtra("opis", oferta.opis); 
		intent.putExtra("przebieg", oferta.przebieg); 
		intent.putExtra("wojewodztwo", oferta.wojewodztwo); 
		intent.putExtra("email", oferta.email); 
		intent.putExtra("dataZakonczenia", oferta.dataZakonczenia); 		
		startActivity(intent);                          	                                                
	}                                                   
	                                                    
	                                                    
}                                                       
