package pl.mikolajkarebski.tomoto.model;

public class ZalogowanyUzytkownik {
	public static String imie;
	public static String nazwisko;
	public static String email;
	public static String haslo;
	public static String miejscowosc;
	public static String getFullName() { return imie+" "+nazwisko; }
}
