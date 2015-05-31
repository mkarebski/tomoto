package pl.mikolajkarebski.tomoto.adapters;

import java.util.List;

import pl.mikolajkarebski.tomoto.R;
import pl.mikolajkarebski.tomoto.model.Oferta;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.loopj.android.image.SmartImageView;

@SuppressLint("InflateParams")
public class OfertaAdapter extends ArrayAdapter<Oferta> {

	private Context context;
	private List<Oferta> listaOfert;
	
	public OfertaAdapter(Context context, int textViewResourceId, List<Oferta> objects) {
		super(context, textViewResourceId, objects);
		this.context = context;
		this.listaOfert = objects;
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		Oferta oferta = listaOfert.get(position);
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		
		View view = inflater.inflate(R.layout.oferta_list_item, null);
		
		SmartImageView image = (SmartImageView) view.findViewById(R.id.imageView1);
		image.setImageUrl(oferta.obrazek);
		
		TextView carDetail = (TextView) view.findViewById(R.id.car_detail);
		carDetail.setText(oferta.marka+" "+oferta.model+" ("+oferta.rocznik+")");
		
		TextView przebieg = (TextView) view.findViewById(R.id.przebieg);
		przebieg.setText(oferta.przebieg+" km");
		
		TextView from = (TextView) view.findViewById(R.id.from);
		from.setText(oferta.miejsce);
		
		TextView price = (TextView) view.findViewById(R.id.price);
		price.setText(oferta.cena+" PLN");
		
		return view;
		
	}
	
	

}
