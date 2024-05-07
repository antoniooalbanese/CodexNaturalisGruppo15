package application.model;

import java.util.ArrayList;

/**
 * Questa classe implementa gli obiettivi che i giocatori devono raggiungere.
 */
public class Obiettivo {
	/**
	 * Attributo che indica il tipo di obiettivo.
	 */
	private TipoObiettivo tipo;
	/**
	 * Risorse richieste per raggiungere l'obiettivo. 
	 */
	private Regno risorsa;
	/**
	 * Oggetti richiesti per raggiungere l'obiettivo.
	 */
	private ArrayList<Oggetto> oggetto = new ArrayList<Oggetto>();
	/**
	 * Matrice contenente la disposizione che deve essere soddisfatta 
	 * per raggiungere l'obiettivo.
	 */
	private Regno[][] disposizione = new Regno[3][3];

	
	/**
	 * Costruttore della classe quando l'obiettivo da raggiungere riguarda il 
	 * conteggio di un determinato tipo di risorse.
	 * @param tipo
	 * @param risorsa
	 */
	public Obiettivo(TipoObiettivo tipo, Regno risorsa) {
		this.tipo = tipo;
		this.risorsa = risorsa;
		this.oggetto = null;
		this.disposizione = null;
	}
	
	/**
	 * Costruttore della classe quando l'obiettivo da raggiungere riguarda il 
	 * conteggio di determianti tipi di oggetti.
	 * @param tipo
	 * @param oggetto
	 */
	public Obiettivo(TipoObiettivo tipo, ArrayList<Oggetto> oggetto) {
		this.tipo = tipo;
		this.risorsa = null;
		this.oggetto = oggetto;
		this.disposizione = null;
	}
	
	/**
	 * Costruttore della classe quando l'obiettivo da raggiungere riguarda il 
	 * conteggio di una determinata disposizione delle carte che deve formare o
	 * una diagonale, oppure una "elle".
	 * @param tipo
	 * @param diag
	 */
	public Obiettivo(TipoObiettivo tipo, Regno[][] disposizione) {
		this.tipo = tipo;
		this.risorsa = null;
		this.oggetto = null;
		this.disposizione = disposizione;
	}
	
	public Regno[][] getDisposizione() {
		return this.disposizione;
	}

}
