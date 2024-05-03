package application.model;

/** 
 * Questa è la classe che decrive un generico angolo appartenente ad una 
 * generica carta.
 */
public class Angolo {
	/**
	 * Attributo che descrive il tipo dell'angolo.
	 */
	private TipoAngolo tipo;
	/**
	 * Posizione dell'angolo all'interno della carta.
	 */
	private Posizione pos;
	/**
	 * Risorsa contenuta all'interno dell'angolo, nel caso in cui questo 
	 * contenga una risorsa.
	 */
	private Regno risorsa;
	/**
	 * Oggetto contenuto all'interno dell'angolo, nel caso in cui questo
	 * contenga un oggetto.
	 */
	private Oggetto oggetto;
	/**
	 * Codice della carta contenente l'angolo a cui l'angolo in questione è
	 * collegato.
	 */
	private String link;
	
	/**
	 * Questo è il costruttore della classe.
	 * @param id
	 * @param tipo
	 * @param pos
	 * @param risorsa
	 * @param oggetto
	 * @param link
	 */
	public Angolo(String id, TipoAngolo tipo, Posizione pos, Regno risorsa, 
				  Oggetto oggetto, String link) {
		this.tipo = tipo;
		this.pos = pos;
		this.risorsa = risorsa;
		this.oggetto = oggetto;
		this.link = link;
	}
}