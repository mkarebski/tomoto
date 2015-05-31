package pl.mikolajkarebski.tomoto.activities;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.mikolajkarebski.tomoto.R;
import pl.mikolajkarebski.tomoto.constants.URL;
import pl.mikolajkarebski.tomoto.model.ZalogowanyUzytkownik;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.image.SmartImageView;

public class OfertaDetailActivity extends Activity{
	
	private SmartImageView image = null;
	private TextView from = null;
	private TextView price = null;
	private TextView details = null;
	private TextView dateTo = null;
	private Button sendButton = null;
	private String email = null;
	private TextView przebieg;
	private long idOferty;
	private TextView opis;
	private SharedPreferences sesja;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//Log.d("GooglePlayServices", String.valueOf(serviceOK()));
		
		
		setContentView(R.layout.oferta_detail);
		
		image = (SmartImageView) findViewById(R.id.imageView1);
		from = (TextView) findViewById(R.id.from);
		price = (TextView) findViewById(R.id.price);
		details = (TextView) findViewById(R.id.car_detail);
		dateTo = (TextView) findViewById(R.id.dateTo);
		opis = (TextView) findViewById(R.id.opis);
		przebieg = (TextView) findViewById(R.id.przebieg);
		sendButton = (Button) findViewById(R.id.btnSendEmail);
		
		Intent mainIntent = getIntent();
		String miejsce = mainIntent.getStringExtra("miejsce");
		String wojewodztwo = mainIntent.getStringExtra("wojewodztwo");
		image.setImageUrl(mainIntent.getStringExtra("obrazek"));
		from.setText(miejsce+" ("+wojewodztwo+")");
		price.setText(mainIntent.getStringExtra("cena")+ "PLN");
		details.setText(mainIntent.getStringExtra("marka")+" "+mainIntent.getStringExtra("model")+" ("+mainIntent.getStringExtra("rocznik")+")");
		dateTo.setText("Data zakoñczenia: "+mainIntent.getStringExtra("dataZakonczenia"));
		przebieg.setText("Przegieg: "+mainIntent.getStringExtra("przebieg")+" km");
		email = mainIntent.getStringExtra("email");
		idOferty = Long.parseLong(mainIntent.getStringExtra("id"));
		opis.setText(mainIntent.getStringExtra("opis"));
		
//		map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
//		LatLng coords = getLatLongFromAddress(miejsce+",%20"+wojewodztwo);
//		Marker locationMarker = map.addMarker(new MarkerOptions().position(coords).title(miejsce));
//		Marker locationMarker = map.addMarker(new MarkerOptions().position(new LatLng(50.033300, 55.00303030)).title(miejsce));
//		map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(50.033300, 55.00303030), 15));
		
		sendButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_SEND);
				i.setType("message/rfc822");
				i.putExtra(Intent.EXTRA_EMAIL  , new String[]{email});
				i.putExtra(Intent.EXTRA_SUBJECT, "Wiadomoœæ z ToMoto!");
				i.putExtra(Intent.EXTRA_TEXT   , "U¿ytkownik "+(sesja.getString("imie", ""))+" "+(sesja.getString("nazwisko", ""))+" jest zainteresowany kupnem samochodu "+details.getText());
				try {
				    startActivity(Intent.createChooser(i, "Wysy³am maila..."));
				} catch (android.content.ActivityNotFoundException ex) {
				    Toast.makeText(getBaseContext(), "There are no email clients installed.", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
/*	private boolean serviceOK() {
		int isAvaliable = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
		if(isAvaliable == ConnectionResult.SUCCESS) {
			return true;
		} else
		if(GooglePlayServicesUtil.isUserRecoverableError(isAvaliable)){
			Dialog dialog = GooglePlayServicesUtil.getErrorDialog(isAvaliable, this, 9001);
			dialog.show();
		} else {
			Toast.makeText(this, "cant connect to gps", Toast.LENGTH_LONG).show();
		}
		return false;
	}*/

	@Override
	protected void onResume() {
	   sesja=getSharedPreferences(URL.MyPREFERENCES, Context.MODE_PRIVATE);
	   super.onResume();
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
		case R.id.action_search: 	
			Toast.makeText(getApplicationContext(), "Szukam", Toast.LENGTH_LONG).show();
			break;
		case R.id.action_previous: 	
			//Toast.makeText(getApplicationContext(), "Poprzedni ekran", Toast.LENGTH_LONG).show();
			finish();
			break; 
		}
		return super.onOptionsItemSelected(item);
	}

/*	
	private static LatLng getLatLongFromAddress(String address) {
	    String uri = "http://maps.google.com/maps/api/geocode/json?address="+address+"&sensor=false";
	    HttpGet httpGet = new HttpGet(uri);
	    HttpClient client = new DefaultHttpClient();
	    HttpResponse response;
	    StringBuilder stringBuilder = new StringBuilder();

	    try {
	        response = client.execute(httpGet);
	        HttpEntity entity = response.getEntity();
	        InputStream stream = entity.getContent();
	        int b;
	        while ((b = stream.read()) != -1) {
	            stringBuilder.append((char) b);
	        }
	    } catch (ClientProtocolException e) {
	        e.printStackTrace();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    JSONObject jsonObject = new JSONObject();
	    try {
	        jsonObject = new JSONObject(stringBuilder.toString());

	        double lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
	            .getJSONObject("geometry").getJSONObject("location")
	            .getDouble("lng");

	        double lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
	            .getJSONObject("geometry").getJSONObject("location")
	            .getDouble("lat");

	        Log.d("latitude", "" + lat);
	        Log.d("longitude", "" + lng);
	        return new LatLng(lat, lng);
	    } catch (JSONException e) {
	        e.printStackTrace();
	    }
	    return null;
	}	

*/}
