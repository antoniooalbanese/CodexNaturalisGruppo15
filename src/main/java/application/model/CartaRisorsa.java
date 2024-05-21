package application.model;

import java.util.ArrayList;

/**
 * Questa classe implementa le carte di tipo risorsa.
 */
public class CartaRisorsa extends Carta {
	/**
	 * Questo attributo descrive a quale dei 4 regni appartiene la carta risorsa.
	 */
	private Regno regno;
	/**
	 * Attributo che descrive la risorsa che si trova al centro della carta
	 * nel caso in cui si mostri il retro di quest'ultima.
	 */
	private Regno centro;
	/**
	 * Questa ArrayList contiene i 4 angoli presenti nella carta seguendo la 
	 * seguente logica degli indici:
	 * indice 0: angolo in alto a destra;
	 * indice 1: angolo in basso a destra;
	 * indice 2: angolo in basso a sinistra;
	 * indice 3: angolo in alto a sinistra.
	 */
	private ArrayList<Angolo> angoli = new ArrayList<Angolo>(4);
	/**
	 * Questo attributo descrive gli eventuali punti che la carta attribuisce
	 * al giocatore che la piazza nel momento in cui questa viene piazzata.
	 */
	private Punto punto;
	
	/**
	 * Questo è il costruttore della classe nel caso in cui si mostri
	 * il fronte della carta.
	 * @param id
	 * @param fronte
	 * @param regno
	 * @param angoli
	 * @param punto
	 */
	public CartaRisorsa(String id, Regno regno, ArrayList<Angolo> angoli,
						Punto punto) {
		super(id);
		this.regno = regno;
		this.centro = null;
		this.angoli = angoli;
		this.punto = punto;
	}
	
	/**
	 * Questo è il costruttore della classe nel caso in cui si mostri
	 * il retro della carta.
	 * @param id
	 * @param fronte
	 * @param regno
	 * @param centro
	 * @param angoli
	 */
	public CartaRisorsa(String id, Regno regno, Regno centro,
						ArrayList<Angolo> angoli) {
		super(id, false);
		this.regno = regno;
		this.centro = centro;
		this.angoli = angoli;
		this.punto = null;
	}
	
	
	
	public Regno getRegno() {
		return regno;
	}

	public void setRegno(Regno regno) {
		this.regno = regno;
	}

	public Regno getCentro() {
		return centro;
	}

	public void setCentro(Regno centro) {
		this.centro = centro;
	}

	public ArrayList<Angolo> getAngoli() {
		return angoli;
	}

	public void setAngoli(ArrayList<Angolo> angoli) {
		this.angoli = angoli;
	}

	public Punto getPunto() {
		return punto;
	}

	public void setPunto(Punto punto) {
		this.punto = punto;
	}

	public String showCard() {
	String ang = "";
		
		for(int i =0;i<4;i++) { 
			ang += "\n       " + angoli.get(i).showAngolo();
		}
		
		if(this.getFronte()==true) {
			return "\nCarta Risorsa:\n   " + "ID: " + getId() + "\n   " + "Centro: " + centro.toString() + "\n   " + "Angoli: " + ang + "\n   " + "Punti: " + getPunto().showPunto();
		} else {
			return "\nCarta Risorsa:\n   " + "ID: " + getId() + "\n   " + "Angoli: " + ang;
		}

	}
}
