package pl.mikolajkarebski.tomoto.activities;

import pl.mikolajkarebski.tomoto.R;
import pl.mikolajkarebski.tomoto.constants.Utility;
import pl.mikolajkarebski.tomoto.datasource.ListaMarek;
import pl.mikolajkarebski.tomoto.datasource.ListaMiast;
import pl.mikolajkarebski.tomoto.datasource.ListaModeli;
import pl.mikolajkarebski.tomoto.datasource.ListaWojewodztw;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.loopj.android.http.RequestParams;

public class SearchActivity extends Activity {
	
	Button submit;
	Spinner wojewodztwo, miasto, model, marka;
	String wojewodztwoTmp, miastoTmp, modelTmp, markaTmp;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);

		wojewodztwo = (Spinner) findViewById(R.id.wojewodztwa);
		miasto = (Spinner) findViewById(R.id.miasta);
		model = (Spinner) findViewById(R.id.model);
		marka = (Spinner) findViewById(R.id.marka);
		submit = (Button) findViewById(R.id.btnSearch);
	
		ArrayAdapter<String> wojewAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
		wojewAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		wojewodztwo.setAdapter(wojewAdapter);
		ListaWojewodztw.getLista(wojewAdapter);
		wojewodztwo.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
				wojewodztwoTmp = parent.getItemAtPosition(pos).toString();
				RequestParams rp = new RequestParams();
				rp.add("wojewodztwo", wojewodztwoTmp);
				
				ArrayAdapter<String> miastoAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_item);
				miastoAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				miasto.setAdapter(miastoAdapter);
				ListaMiast.getLista(miastoAdapter, rp);
				miasto.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override	
					public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
						miastoTmp = parent.getItemAtPosition(pos).toString();
					}
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {}
				});
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
		
		ArrayAdapter<String> markaAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item);
		markaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		marka.setAdapter(markaAdapter);
		ListaMarek.getLista(markaAdapter);
		marka.setOnItemSelectedListener(new OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
				markaTmp = parent.getItemAtPosition(pos).toString();
				RequestParams rp = new RequestParams();
				rp.add("marka", markaTmp);
				ArrayAdapter<String> modelAdapter = new ArrayAdapter<String>(getBaseContext(), R.layout.spinner_item);
				modelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
				model.setAdapter(modelAdapter);
				ListaModeli.getLista(modelAdapter, rp);
				model.setOnItemSelectedListener(new OnItemSelectedListener() {
					@Override
					public void onItemSelected(AdapterView<?> parent, View view, int pos,long id) {
						modelTmp = parent.getItemAtPosition(pos).toString();
					}
					@Override
					public void onNothingSelected(AdapterView<?> arg0) {}
				});
			}
			@Override
			public void onNothingSelected(AdapterView<?> arg0) {}
		});
	
	}
	
	public void szukaj(View view) {
		Log.d("wojew", String.valueOf(Utility.isNotNull(wojewodztwoTmp)));
		Log.d("miasto",String.valueOf(Utility.isNotNull(miastoTmp)));
		Log.d("marka", String.valueOf(Utility.isNotNull(markaTmp)));
		Log.d("model", String.valueOf(Utility.isNotNull(modelTmp)));
		
		if(Utility.isNotNull(wojewodztwoTmp) == false && Utility.isNotNull(miastoTmp) == false &&
				Utility.isNotNull(markaTmp) == false &&	Utility.isNotNull(modelTmp) == false) {
			Toast.makeText(getBaseContext(), "Nie wybrano ¿adnych kryteriów", Toast.LENGTH_LONG).show();
			return;
		}
		
		Intent intent = new Intent(SearchActivity.this, MainActivity.class);
		intent.putExtra("wojewodztwo", wojewodztwoTmp);
		intent.putExtra("miasto", miastoTmp);
		intent.putExtra("marka", markaTmp);
		intent.putExtra("model", modelTmp);
		startActivity(intent);
	}
}