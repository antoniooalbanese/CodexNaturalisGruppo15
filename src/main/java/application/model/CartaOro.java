package application.model;

import java.util.ArrayList;

/**
 * Questa classe implementa le carte di tipo oro.
 */
public class CartaOro extends Carta{
	/**
	 * Questo attributo descrive a quale dei 4 regni appartiene la carta oro.
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
	 * Attributo che descrive il requisito da soddisfare per posizionare
	 * la carta.
	 */
	private Requisito requisito;
	
	/**
	 * Questo è il costruttore della classe nel caso in cui si mostri
	 * il fronte della carta.
	 * @param id
	 * @param fronte
	 * @param regno
	 * @param angoli
	 * @param punto
	 * @param requisito
	 */
	public CartaOro(String id, Regno regno, ArrayList<Angolo> angoli, Punto punto,
					Requisito requisito) {
		super(id);
		this.regno = regno;
		this.centro = null;
		this.angoli = angoli;
		this.punto = punto;
		this.requisito = requisito;
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
	public CartaOro(String id, Regno regno, Regno centro, 
					ArrayList<Angolo> angoli) {
		super(id, false);
		this.regno = regno;
		this.centro = centro;
		this.angoli = angoli;
		this.punto = null;
		this.requisito = null;
	}
	
	/**
	 * Metodo che ritorna il regno a cui appartiene la carta oro
	 * @return
	 */
	public Regno getRegno() {
		return regno;
	}
	
	/**
	 * Metodo che modifica il regno a cui appartiene la carta oro.
	 * @param regno
	 */
	public void setRegno(Regno regno) {
		this.regno = regno;
	}
	
	/**
	 * Metodo che ritorna il tipo della risorsa nel centro della carta oro.
	 * @return
	 */
	public Regno getCentro() {
		return centro;
	}
	
	/**
	 * Metodo che modifica il tipo della risorsa nel centro della carta oro.
	 * @param centro
	 */
	public void setCentro(Regno centro) {
		this.centro = centro;
	}
	
	/**
	 * Metodo che ritorna la lista di angoli della carta oro.
	 * @return
	 */
	public ArrayList<Angolo> getAngoli() {
		return angoli;
	}
	
	/**
	 * Metodo che modifica la lista di angoli della carta oro.
	 * @param angoli
	 */
	public void setAngoli(ArrayList<Angolo> angoli) {
		this.angoli = angoli;
	}

	/**
	 * Metodo che ritorna i punti assegnati dalla carta oro al suo posizionamento.
	 * @return
	 */
	public Punto getPunto() {
		return punto;
	}
	
	/**
	 * Metodo che modifica i punti asseganti dalla carta oro al suo posizionamento.
	 * @param punto
	 */
	public void setPunto(Punto punto) {
		this.punto = punto;
	}
	
	/**
	 * Metodo che ritorna il requisito di posizionamento della carta oro.
	 * @return
	 */
	public Requisito getRequisito() {
		return requisito;
	}
	
	/**
	 * Metodo che modifica il requisito di posizionamento della carta oro.
	 * @param requisito
	 */
	public void setRequisito(Requisito requisito) {
		this.requisito = requisito;
	}
	
	/**
	 * Metodo che permette di mostrare tutte le informazioni di una carta oro.
	 * @return
	 */
	public String showCard() {
	String ang = "";
		
		for(int i =0;i<4;i++) { 
			ang += "\n       " + angoli.get(i).showAngolo();
		}
		
		if(this.getFronte() == true) {
			return "\nCarta Oro:\n   " + "ID: " + getId() + "\n   " + "Angoli: " + ang + "\n   " + "Punti: " + getPunto().showPunto() + "\n   "
					+ "Requisito di piazzamento: " + getRequisito().showRequisito();
		} else {
			return "\nCarta Oro:\n   " + "ID: " + getId() + "\n   " + "Centro: " + centro.toString() + "Angoli: " + ang;
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
