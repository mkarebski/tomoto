package pl.mikolajkarebski.tomoto.model;

import java.io.Serializable;

public class Oferta implements Serializable{
	private static final long serialVersionUID = 1L;
	public String id; 
	public String marka;
	public String model; 
	public String rocznik; 
	public String opis;  
	public String przebieg;
	public String cena; 
	public String wojewodztwo;
	public String miejsce;
	public String obrazek;
	public String email;
	public String dataZakonczenia;
	@Override
	public String toString() {
		return "Oferta [id=" + id + ", marka=" + marka + ", model=" + model
				+ ", rocznik=" + rocznik + ", opis=" + opis + ", przebieg="
				+ przebieg + ", cena=" + cena + ", wojewodztwo=" + wojewodztwo
				+ ", miejsce=" + miejsce + ", obrazek=" + obrazek + ", email="
				+ email + ", dataZakonczenia=" + dataZakonczenia + "]";
	}
}
