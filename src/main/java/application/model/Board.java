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
	private ArrayList<CartaRisorsa> risorsa;
	/**
	 * Insieme di carte oro piazzate da un giocatore.
	 */
	private ArrayList<CartaOro> oro;
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
	private ArrayList<Integer> numRis;
	/**
	 * Lista contenente il numero di ogni oggetto presente sulla board. 
	 */
	private ArrayList<Integer> numOgg;
	
	/**
	 * Costruttore della classe.
	 * @param centro
	 */
	public Board(CartaIniziale centro) {
		this.centro = centro;
		this.turno = 0;
		this.punteggio = 0;
	}
	
}
