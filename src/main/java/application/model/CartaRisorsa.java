package application.model;

import java.util.ArrayList;

import application.view.AnsiEscapeCodes;

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
	}
	
	/**
	 * Metodo che ritorna il regno a cui appartiene la carta risorsa.
	 * @return
	 */
	public Regno getRegno() {
		return regno;
	}
	
	/**
	 * Metodo che modifica il regno a cui appartiene la carta risorsa.
	 * @param regno
	 */
	public void setRegno(Regno regno) {
		this.regno = regno;
	}
	
	/**
	 * Metodo che ritorna il tipo di risorsa a cui appartiene il centro della 
	 * carta risorsa.
	 * @return
	 */
	public Regno getCentro() {
		return centro;
	}
	
	/**
	 * Metodo che modifica il tipo di risorsa a cui appartiene il centro della 
	 * carta risorsa.
	 * @param centro
	 */
	public void setCentro(Regno centro) {
		this.centro = centro;
	}
	
	/**
	 * Metodo che ritorna la lista di angoli della carta risorsa.
	 * @return
	 */
	public ArrayList<Angolo> getAngoli() {
		return angoli;
	}
	
	/**
	 * Metodo che modifica la lista di angoli della carta risorsa.
	 * @param angoli
	 */
	public void setAngoli(ArrayList<Angolo> angoli) {
		this.angoli = angoli;
	}
	
	/**
	 * Metodo che ritorna i punti assegnati al posizionamento della carta risorsa.
	 * @return
	 */
	public Punto getPunto() {
		return punto;
	}
	
	/**
	 * Metodo che modifica i punti assegnati al posizionamento della carta risorsa.
	 * @param punto
	 */
	public void setPunto(Punto punto) {
		this.punto = punto;
	}
	
	/**
	 * Metodo che permette di mostrare tutte le informazioni di una carta risorsa.
	 * @return
	 */
	public String showCard() {
	String ang = "";
	String code = "";
		
		for(int i =0;i<4;i++) { 
			ang += "\n       " + angoli.get(i).showAngolo();
		}
		
		if (this.getId().contains("VR")) {
			 code = "" + AnsiEscapeCodes.GREEN_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + getId() + AnsiEscapeCodes.ENDING_CODE.getCode();  
		} else if (this.getId().contains("BL")){
			code = "" + AnsiEscapeCodes.CYAN_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + getId() + AnsiEscapeCodes.ENDING_CODE.getCode();
		} else if (this.getId().contains("RS")) {
			code = "" + AnsiEscapeCodes.RED_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + getId() + AnsiEscapeCodes.ENDING_CODE.getCode();
		} else if (this.getId().contains("VL")) {
			code = "" + AnsiEscapeCodes.VIOLET_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + getId() + AnsiEscapeCodes.ENDING_CODE.getCode();
		}
		
		if(this.getFronte() == true) {
			if(this.getPunto() == null) {
				return "\nCarta Risorsa:\n   " + "ID: " + code + "\n   " + "Angoli: " + ang + "\n   ";
			} else {
				return "\nCarta Risorsa:\n   " + "ID: " + code + "\n   " + "Angoli: " + ang + "\n   " + "Punti: " + getPunto().showPunto();
			}
		} else {
			return "\nCarta Risorsa:\n   " + "ID: " + code + "\n   " + "Centro: " + centro.toString() + "\n   " + "Angoli: " + ang;
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
