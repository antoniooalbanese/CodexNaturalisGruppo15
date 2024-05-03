package application.model;

import java.util.ArrayList;

/**
 * Questa classe implementa le carte iniziali.
 */
public class CartaIniziale extends Carta{
	/**
	 * Attributo che descrive la risorsa o le risorse che si trovano al centro 
	 * della carta nel caso in cui si mostri il retro di quest'ultima.
	 */
	private ArrayList<Regno> centro = new ArrayList<Regno>(3);
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
	 * Questo è il costruttore della classe nel caso in cui si mostri
	 * il fronte della carta.
	 * @param id
	 * @param fronte
	 * @param regno
	 * @param centro
	 * @param angoli
	 */
	public CartaIniziale(String id, Boolean fronte, Regno regno, 
						 ArrayList<Regno> centro,ArrayList<Angolo> angoli) {
		super(id, fronte);
		this.regno = regno;
		this.centro = centro;
		this.angoli = angoli;
	}
	
	/** 
	 * Questo è il costruttore della classe nel caso in cui si mostri
	 * il retro della carta.
	 * @param id
	 * @param fronte
	 * @param regno
	 * @param angoli
	 */
	public CartaIniziale(String id, Boolean fronte, Regno regno,
						ArrayList<Angolo> angoli) {
		super(id, fronte);
		this.regno = regno;
		this.centro = null;
		this.angoli = angoli;
	}
	

}
