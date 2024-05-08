package application.model;

/**
 * Questa classe rappresenta il punteggio che può assegnare una carta una volta
 * che viene posizionata sulla board.
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
	 * Attributo che indica l'oggetto da contare per ottenere il punteggio della 
	 * carta.
	 */
	private Oggetto oggetto;
	
	/**
	 * Costruttore della classe quando il tipo di punteggio è del tipo che viene 
	 * assegnato immediatamente senza dover fare controlli oppure quando è del tipo
	 * che viene assegnato controllando il numero di angoli coperti dalla carta.
	 * @param tipo
	 * @param somma
	 */
	public Punto(TipoPunto tipo, int somma) {
		this.tipo = tipo;
		this.somma = somma;
		this.oggetto = null;
	}
	
	/**
	 * Costruttore della classe quando il tipo di punteggio è del tipo che viene 
	 * assegnato controllando la quantità di un determinato oggetto.
	 * @param tipo
	 * @param somma
	 * @param oggetto
	 */
	public Punto(TipoPunto tipo, int somma, Oggetto oggetto) {
		this.tipo = tipo;
		this.somma = somma;
		this.oggetto = oggetto;
	}
}
