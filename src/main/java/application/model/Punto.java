package application.model;

/**
 * Questa classe rappresenta il punteggio che può assegnare una carta
 * una volta che viene posizionata sulla board.
 */
public class Punto {
	/**
	 * Attributo che indica il tipo di punteggio assegnato dalla carta.
	 */
	private TipoPunto tipo;
	
	/**
	 * Attributo che indica la quantità di punti assegnati.
	 */
	private int somma;
	
	/**
	 * Attributo che indica l'oggetto da contare per ottenere il 
	 * punteggio della carta.
	 */
	private Oggetto oggetto;
	
	/**
	 * Costruttore della classe quando il tipo di punteggio è del tipo 
	 * che viene assegnato immediatamente senza dover fare controlli
	 * oppure quando è del tipo che viene assegnato controllando il 
	 * numero di angoli coperti dalla carta.
	 * @param tipo: tipo di punto 
	 * @param somma: quantità di punti assegnati
	 */
	public Punto(TipoPunto tipo, int somma) {
		this.tipo = tipo;
		this.somma = somma;
		this.oggetto = null;
	}
	
	/**
	 * Costruttore della classe quando il tipo di punteggio è del tipo 
	 * che viene assegnato controllando la quantità di un determinato
	 * oggetto.
	 * @param tipo: tipo di punto
	 * @param somma: quantità di punti assegnati
	 * @param oggetto: oggetto di cui si deve controllare il numero
	 * presente sulla board per assegnare il punteggio al momento
	 * del posizionamento 
	 */
	public Punto(TipoPunto tipo, int somma, Oggetto oggetto) {
		this.tipo = tipo;
		this.somma = somma;
		this.oggetto = oggetto;
	}
	
	/**
	 * Metodo che ritorna il tipo di punto.
	 * @return tipo di punto
	 */
	public TipoPunto getTipo() {
		return tipo;
	}

	/**
	 * Metodo che modifica il tipo di punto.
	 * @param tipo: tipo di punto
	 */
	public void setTipo(TipoPunto tipo) {
		this.tipo = tipo;
	}
	
	/**
	 * Metodo che ritorna la quantità di punti assegnati.
	 * @return quantità di punti assegnati
	 */
	public int getSomma() {
		return somma;
	}
	
	/**
	 * Metodo che modifica la quantità di punti assegnati.
	 * @param somma: quantità di punti assegnati
	 */
	public void setSomma(int somma) {
		this.somma = somma;
	}

	/**
	 * Metodo che ritorna il tipo di oggetto da contare per 
	 * ottenere il punteggio della carta.
	 * @return tipo di oggetto da contare per ottenere il 
	 * punteggio della carta
	 */
	public Oggetto getOggetto() {
		return oggetto;
	}
	
	/**
	 * Metodo che modifica il tipo di oggetto da contare per ottenere
	 * il punteggio della carta.
	 * @param oggetto: tipo di oggetto da contare per ottenere il 
	 * punteggio della carta
	 */
	public void setOggetto(Oggetto oggetto) {
		this.oggetto = oggetto;
	}
	
	/**
	 * Metodo che permette di prendere tutte le informazioni di un
	 * punto sotto forma di stringa.
	 * @return stringa contenente tutte le informazioni relative ad un
	 * punto
	 */
	public String showPunto() {
		String riga = "";
		
		switch(getTipo()) {
		case IMMEDIATO: 
			riga = String.valueOf(getSomma());
			break;
		case ANGOLO:
			riga = String.valueOf(getSomma()) + " per ogni angolo coperto da questa carta";
			break;
		case OGGETTO:
			riga = String.valueOf(getSomma()) + " per ogni oggetto del tipo: " + getOggetto() + " presenti sulla tua board";
			break;
		}
		
		return riga;
	}
}
