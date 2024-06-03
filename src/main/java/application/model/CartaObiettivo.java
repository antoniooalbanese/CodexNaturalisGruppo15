package application.model;

/**
 * Questa classe implementa le carte di tipo obiettivo.
 */
public class CartaObiettivo extends Carta{
	/**
	 * Questo attributo descrive l'obiettivo che deve essere soddisfatto
	 * per attribuire al giocatore i punti della carta.
	 */
	private Obiettivo obiettivo;
	/**
	 * Attributo che descrive il valore dei punti asseganti al 
	 * raggiungimento dell'obiettivo.
	 */
	private int punto;
	
	/**
	 * Questo è il costruttore della classe.
	 * @param id: codice univoco della carta
	 * @param fronte: booleano che descrive se la carta è mostrata sul
	 * fronte(TRUE) o sul retro(FALSE)
	 * @param obiettivo: obiettivo che il giocatore deve raggiungere per
	 * ottenere i punti indicati sulla carta
	 * @param punto: punti assegnati al giocatore che raggiunge 
	 * l'obiettivo 
	 */
	public CartaObiettivo(String id, Boolean fronte, Obiettivo obiettivo,
						  int punto) {
		super(id);
		this.obiettivo = obiettivo;
		this.punto = punto;
	}

	/**
	 * Metodo che ritorna l'obiettivo contenuto nella carta.
	 * @return: obiettivo contenuto nella carta
	 */
	public Obiettivo getObiettivo() {
		return obiettivo;
	}

	/**
	 * Metodo che modifica l'obiettivo contenuto nella carta.
	 * @param obiettivo: obiettivo contenuto nella carta
	 */
	public void setObiettivo(Obiettivo obiettivo) {
		this.obiettivo = obiettivo;
	}

	/**
	 * Metodo che ritorna i punti dati dalla carta obiettivo in caso di
	 * raggiungimento dell'obiettivo. 
	 * @return: punti extra assegnati dalla carta
	 */
	public int getPunto() {
		return punto;
	}

	/**
	 * Metodo che ritorna i punti dati dalla carta obiettivo in caso di
	 * raggiungimento dell'obiettivo.
	 * @param punto: punti extra assegnati dalla carta
	 */
	public void setPunto(int punto) {
		this.punto = punto;
	}
	
	/**
	 * Metodo che permette di mostrare tutte le informazioni di una 
	 * carta obiettivo.
	 * @return: stringa contenente tutte le informazioni di una carta
	 * obiettivo
	 */
	public String showCard() {
		return "\nCarta Obiettivo:\n   " + "ID: " + getId() + "\n   " + "Punti: " + getPunto() + getObiettivo().showObiettivo();
	}
}
