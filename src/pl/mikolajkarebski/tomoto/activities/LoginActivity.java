package pl.mikolajkarebski.tomoto.activities;
 
import java.io.IOException;
import java.io.StringReader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.http.Header;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import pl.mikolajkarebski.tomoto.R;
import pl.mikolajkarebski.tomoto.constants.URL;
import pl.mikolajkarebski.tomoto.constants.Utility;
import pl.mikolajkarebski.tomoto.model.Oferta;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
/**
 * 
 * Login Activity Class 
 *
 */
public class LoginActivity extends Activity {
    ProgressDialog prgDialog;
    TextView errorMsg;
    EditText emailET;
    EditText pwdET;
    SharedPreferences sharedpreferences;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        errorMsg = (TextView)findViewById(R.id.login_error);
        emailET = (EditText)findViewById(R.id.loginEmail);
        pwdET = (EditText)findViewById(R.id.loginPassword);
        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Proszê czekaæ");
        prgDialog.setCancelable(false);
    }

    @Override
	protected void onResume() {
	   sharedpreferences=getSharedPreferences(URL.MyPREFERENCES, Context.MODE_PRIVATE);
	   if (sharedpreferences.contains("imie"))
	   {
		   navigatetoHomeActivity();
	   }
	   super.onResume();
	}
    /**
     * Method gets triggered when Login button is clicked
     * 
     * @param view
     */
    public void loginUser(View view){
        String email = emailET.getText().toString();
        String password = pwdET.getText().toString();
        RequestParams params = new RequestParams();
        if(Utility.isNotNull(email) && Utility.isNotNull(password)){
            if(Utility.validate(email)){
                params.put("email", email);
                params.put("haslo", password);
                invokeWS(params);
            } else {
                Toast.makeText(getApplicationContext(), "Please enter valid email", Toast.LENGTH_LONG).show();
            }
        } else{
            Toast.makeText(getApplicationContext(), "Please fill the form, don't leave any field blank", Toast.LENGTH_LONG).show();
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
                 Log.d("response",response);
         		 if(response.contains("imie")) {
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
         			String imie = null, nazwisko = null, miasto = null, email = null;
         		    if (doc == null) return;
         			NodeList nList = doc.getElementsByTagName("uzytkownicy");
         			for (int temp = 0; temp < nList.getLength(); temp++) {
         				Node node = nList.item(temp);
         				Element e = (Element) node;
         				imie = e.getAttribute("imie");
         				nazwisko = e.getAttribute("nazwisko");
         				miasto = e.getAttribute("miejscowosc");
         				email = e.getAttribute("email");
         			}
         			Editor editor = sharedpreferences.edit();
         		    editor.putString("imie", imie);
         		    editor.putString("nazwisko", nazwisko);
         		    editor.putString("miasto", miasto);
         		    editor.putString("email", email);
         		    editor.commit();
         			navigatetoHomeActivity();
         		 }
         		 else {
         		 	Toast.makeText(getApplicationContext(), "B³¹d logowania", Toast.LENGTH_LONG).show();
         		 }
             }

			@Override
			public void onFailure(int statusCode, Header[] arg1, byte[] arg2,
					Throwable arg3) {
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
 
    /**
     * Method gets triggered when Register button is clicked
     * 
     * @param view
     */
    public void navigatetoRegisterActivity(View view){
        Intent loginIntent = new Intent(getApplicationContext(),RegisterActivity.class);
        loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(loginIntent);
    }
}