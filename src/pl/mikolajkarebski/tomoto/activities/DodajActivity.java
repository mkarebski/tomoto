package pl.mikolajkarebski.tomoto.activities;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.Calendar;

import org.apache.http.Header;

import pl.mikolajkarebski.tomoto.R;
import pl.mikolajkarebski.tomoto.constants.URL;
import pl.mikolajkarebski.tomoto.constants.Utility;
import pl.mikolajkarebski.tomoto.datasource.ListaMarek;
import pl.mikolajkarebski.tomoto.datasource.ListaMiast;
import pl.mikolajkarebski.tomoto.datasource.ListaModeli;
import pl.mikolajkarebski.tomoto.datasource.ListaWojewodztw;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.image.SmartImageView;

public class DodajActivity extends SearchActivity {
	private ProgressDialog prgDialog;
	private TextView errorMsg;
	private SharedPreferences sesja;
	private EditText rokET, opisET, przebiegET, cenaET;
	private Spinner wojewodztwo, miasto, model, marka;
	private String wojewodztwoTmp, miastoTmp, modelTmp, markaTmp;
	private TextView tvDisplayDate;
	private DatePicker dpResult;
	private Button btnChangeDate;
	private SmartImageView photo;
	private int year;
	private int month;
	private int day;
	private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
	
	static final int DATE_DIALOG_ID = 999;
    
    @Override
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dodaj);
        errorMsg = (TextView)findViewById(R.id.login_error);
        wojewodztwo = (Spinner) findViewById(R.id.wojewodztwa);
		miasto = (Spinner) findViewById(R.id.miasta);
		model = (Spinner) findViewById(R.id.model);
		marka = (Spinner) findViewById(R.id.marka);
		submit = (Button) findViewById(R.id.btnSearch);
		photo = (SmartImageView) findViewById(R.id.photo);
		photo.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Wybierz zdjêcie"), SELECT_PICTURE);
			}
		});
		
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
        
        sesja = getSharedPreferences(URL.MyPREFERENCES, Context.MODE_PRIVATE);
        rokET = (EditText)findViewById(R.id.rok);
        opisET = (EditText)findViewById(R.id.opis1);
        przebiegET = (EditText)findViewById(R.id.przebieg1);
        cenaET = (EditText)findViewById(R.id.cena);
        tvDisplayDate = (TextView) findViewById(R.id.tvDate);
        
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Proszê czekaæ...");
        prgDialog.setCancelable(false);
        
        setCurrentDateOnView();
		addListenerOnButton();
    }

    @Override
	protected void onResume() {
	   sesja=getSharedPreferences(URL.MyPREFERENCES, Context.MODE_PRIVATE);
	   super.onResume();
	}
    
    /**
     * Method gets triggered when Login button is clicked
     * 
     * @param view
     */
    public void dodajOgloszenie(View view){
        String rok = rokET.getText().toString();
        String opis = opisET.getText().toString();
        String przebieg = przebiegET.getText().toString();
        String cena = cenaET.getText().toString();
        String data = year+"."+month+"."+day;

        RequestParams params = new RequestParams();
        if(Utility.isNotNull(rok) && Utility.isNotNull(opis) && 
        	Utility.isNotNull(przebieg) && Utility.isNotNull(cena) && 
        	Utility.isNotNull(data) && Utility.isNotNull(selectedImagePath))
        {
        	SharedPreferences sharedpreferences=getSharedPreferences(URL.MyPREFERENCES, Context.MODE_PRIVATE);
        	
        	params.add("rok", rok);
        	params.add("opis", opis);
        	params.add("przebieg", przebieg);
        	params.add("cena", cena);
        	params.add("wojewodztwo", wojewodztwoTmp);
        	params.add("miasto", miastoTmp);
        	params.add("marka", markaTmp);
        	params.add("model", modelTmp);
        	params.add("data_zakonczenia", data);
        	params.add("link", encodeTobase64(((BitmapDrawable)photo.getDrawable()).getBitmap()));
        	params.add("email",(sharedpreferences.getString("email", "")));
            invokeWS(params);
        } else {
            Toast.makeText(getApplicationContext(), "Proszê uzupe³niæ wszystkie pola", Toast.LENGTH_LONG).show();
        }
    }
    
    /**
     * Method that performs RESTful webservice invocations
     * 
     * @param params
     */
    public void invokeWS(RequestParams params){
         prgDialog.show();
         AsyncHttpClient client = new AsyncHttpClient();
         client.get(URL.socket+URL.loginWS,params ,new AsyncHttpResponseHandler() {
        	 @Override
             public void onSuccess(String response) {
                 prgDialog.hide();
         		 if(response.contains("dodano")) {
         			
         		 }
         		 else {
         		 	Toast.makeText(getApplicationContext(), "B³¹d logowania", Toast.LENGTH_LONG).show();
         		 }
             }

			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] arg2, Throwable arg3) {
				 prgDialog.hide();
                 if(statusCode == 404){
                     Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                 } else if(statusCode == 500) {
                     Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                 } else {
                     Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                 }
			}
         });
    }
 
    /**
     * Method which navigates from Login Activity to Home Activity
     */
    public void navigatetoHomeActivity(){
        Intent homeIntent = new Intent(getBaseContext(),MainActivity.class);
        homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(homeIntent);
    }
    
    @Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id){ 
		case R.id.action_previous: finish();
		case R.id.action_search: 	
			Intent intent = new Intent(this,SearchActivity.class);
			startActivity(intent);
			break;
		}
		return super.onOptionsItemSelected(item);
	}
    
	// display current date
	public void setCurrentDateOnView() {
 
		tvDisplayDate = (TextView) findViewById(R.id.tvDate);
		dpResult = (DatePicker) findViewById(R.id.dpResult);
 
		final Calendar c = Calendar.getInstance();
		year = c.get(Calendar.YEAR);
		month = c.get(Calendar.MONTH);
		day = c.get(Calendar.DAY_OF_MONTH);
 
		// set current date into textview
		tvDisplayDate.setText(new StringBuilder()
			// Month is 0 based, just add 1
			.append(month + 1).append("-").append(day).append("-")
			.append(year).append(" "));
 
		// set current date into datepicker
		dpResult.init(year, month, day, null);
 
	}
 
	public void addListenerOnButton() {
 
		btnChangeDate = (Button) findViewById(R.id.btnChangeDate);
		btnChangeDate.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				showDialog(DATE_DIALOG_ID);
			}
 
		});
 
	}
	
	protected Dialog onCreateDialog(int id) {
		switch (id) {
		case DATE_DIALOG_ID:
		   // set date picker as current date
		   return new DatePickerDialog(this, datePickerListener, 
                         year, month,day);
		}
		return null;
	}
 
	private DatePickerDialog.OnDateSetListener datePickerListener = new DatePickerDialog.OnDateSetListener() {
		public void onDateSet(DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {
			year = selectedYear;
			month = selectedMonth;
			day = selectedDay;
 
			// set selected date into textview
			tvDisplayDate.setText(new StringBuilder().append(month + 1).append("-").append(day).append("-").append(year).append(" "));
 
			// set selected date into datepicker also
			dpResult.init(year, month, day, null);
 
		}
	};
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                Uri selectedImageUri = data.getData();
                selectedImagePath = getPath(selectedImageUri);
                File imgFile = new  File(selectedImagePath);
                if(imgFile.exists()){
                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    photo.setImageBitmap(myBitmap);
                }
            }
        }
    }

    /**
     * helper to retrieve the path of an image URI
     */
    public String getPath(Uri uri) {
            // just some safety built in 
            if( uri == null ) {
                // TODO perform some logging or show user feedback
                return null;
            }
            // try to retrieve the image from the media store first
            // this will only work for images selected from gallery
            String[] projection = { MediaStore.Images.Media.DATA };
            Cursor cursor = managedQuery(uri, projection, null, null, null);
            if( cursor != null ){
                int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                cursor.moveToFirst();
                return cursor.getString(column_index);
            }
            // this is our fallback here
            return uri.getPath();
    }
	
    public static String encodeTobase64(Bitmap image)
    {
        Bitmap immagex=image;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();  
        immagex.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b,Base64.DEFAULT);

        Log.e("LOOK", imageEncoded);
        return imageEncoded;
    }
    
}
