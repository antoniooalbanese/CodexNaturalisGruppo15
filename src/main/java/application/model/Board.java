package application.model;

import java.util.ArrayList;

/**
 * Questa classe rappresenta la board di un giocatore, ossia l'insieme di tutte
 * le carte piazzate da un giocatore.
 */
public class Board {
	/**
	 * Identificativo del giocatore a cui è associata la board.
	 */
	private String id;
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
	 * Costruttore della classe.
	 * @param id
	 * @param centro
	 * @param risorsa
	 * @param oro
	 * @param obiettivo
	 */
	public Board(String id, CartaIniziale centro, 
				 ArrayList<CartaRisorsa> risorsa, ArrayList<CartaOro> oro,
				 CartaObiettivo obiettivo) {
		this.id = id;
		this.centro = centro;
		this.risorsa = risorsa;
		this.oro = oro;
		this.obiettivo = obiettivo;
	}
}
