package application.model;

import java.util.ArrayList;

import application.view.AnsiEscapeCodes;

/**
 * Questa classe implementa le carte iniziali.
 */
public class CartaIniziale extends Carta{
	/**
	 * Attributo che descrive la risorsa o le risorse che si trovano al centro 
	 * della carta nel caso in cui si mostri il retro di quest'ultima.
	 */
	private ArrayList<Regno> centro = new ArrayList<Regno>();
	/**
	 * Questa ArrayList contiene i 4 angoli presenti nella carta seguendo la 
	 * seguente logica degli indici:
	 * indice 0: angolo in alto a destra;
	 * indice 1: angolo in basso a destra;
	 * indice 2: angolo in basso a sinistra;
	 * indice 3: angolo in alto a sinistra.
	 */
	private ArrayList<Angolo> angoli = new ArrayList<Angolo>();
	
	/**
	 * Questo è il costruttore della classe nel caso in cui si mostri
	 * il fronte della carta.
	 * @param id
	 * @param centro
	 * @param angoli
	 */
	public CartaIniziale(String id, Regno regno, ArrayList<Regno> centro,
						 ArrayList<Angolo> angoli) {
		super(id);
		this.centro = centro;
		this.angoli = angoli;
	}
	
	/** 
	 * Questo è il costruttore della classe nel caso in cui si mostri
	 * il retro della carta.
	 * @param id
	 * @param fronte
	 * @param angoli
	 */
	public CartaIniziale(String id, Boolean fronte, Regno regno,
						ArrayList<Angolo> angoli) {
		super(id, false);
		this.centro = null;
		this.angoli = angoli;
	}
	
	
	
	public ArrayList<Regno> getCentro() {
		return centro;
	}

	public void setCentro(ArrayList<Regno> centro) {
		this.centro = centro;
	}

	public ArrayList<Angolo> getAngoli() {
		return angoli;
	}

	public void setAngoli(ArrayList<Angolo> angoli) {
		this.angoli = angoli;
	}

	/**
	 * Metodo che permette di mostrare tutte le informazioni di una carta iniziale.
	 * @return
	 */
	public String showCard() {
		
		String ang = "";
		
		for(int i =0;i<4;i++) { 
			ang += "\n       " + angoli.get(i).showAngolo();
		}
		
		if(this.getFronte()==true) {
			return "\nCarta Iniziale:\n   " + "ID: " + AnsiEscapeCodes.WHITE_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + getId() + AnsiEscapeCodes.ENDING_CODE.getCode() + "\n   " + "Centro: " + centro.toString() + "\n   " + "Angoli: " + ang;
		} else {
			return "\nCarta Iniziale:\n   " + "ID: " + AnsiEscapeCodes.WHITE_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + getId() + AnsiEscapeCodes.ENDING_CODE.getCode() + "\n   " + "Angoli: " + ang;
		}

	}
	
	public Angolo getAngoloByPosizione(Posizione pos) {
		for(Angolo a : this.angoli) {
			if(a.getPos().equals(pos)) {
				return a;
			}
		}
		return null;
	}

}
