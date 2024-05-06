package application.model;

import java.util.ArrayList;

/**
 * Questa classe rappresenta la board di un giocatore, ossia l'insieme di tutte
 * le carte piazzate da un giocatore.
 */
public class Board {
	/**
	 * Carta iniziale posseduta e piazzata da un giocatore.
	 */
	private CartaIniziale centro;
	/**
	 * Insieme di carte risorsa piazzate da un giocatore.
	 */
	private ArrayList<CartaRisorsa> risorsa = new ArrayList<CartaRisorsa>(40);
	/**
	 * Insieme di carte oro piazzate da un giocatore.
	 */
	private ArrayList<CartaOro> oro = new ArrayList<CartaOro>(40);
	/**
	 * Carta obiettivo che rappresenta l'obiettivo personale che se 
	 * il giocatore soddisfa dona a quest'ultimo i punti indicata sulla carta. 
	 */
	private CartaObiettivo obiettivo;
	/**
	 * Numero di turni giocati dal giocatore.
	 */
	private int turno;
	/**
	 * Punteggio attribuito al giocatore.
	 */
	private int punteggio;
	/**
	 * Lista contenente il numero di ogni risorsa presente sulla board. 
	 */
	private ArrayList<Integer> num_ris = new ArrayList<Integer>();
	/**
	 * Lista contenente il numero di ogni oggetto presente sulla board. 
	 */
	private ArrayList<Integer> num_ogg = new ArrayList<Integer>();
	
	/**
	 * Costruttore della classe.
	 * @param centro
	 * @param obiettivo
	 */
	public Board(CartaIniziale centro, CartaObiettivo obiettivo) {
		this.centro = centro;
		this.risorsa = null;
		this.oro = null;
		this.obiettivo = obiettivo;
		this.turno = 0;
		this.punteggio = 0;
		this.num_ris = null;
		this.num_ogg = null;
	}
}
