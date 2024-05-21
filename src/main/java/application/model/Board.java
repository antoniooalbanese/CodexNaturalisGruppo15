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

	public CartaIniziale getCentro() {
		return centro;
	}

	public void setCentro(CartaIniziale centro) {
		this.centro = centro;
	}

	public ArrayList<CartaRisorsa> getRisorsa() {
		return risorsa;
	}

	public void setRisorsa(ArrayList<CartaRisorsa> risorsa) {
		this.risorsa = risorsa;
	}

	public ArrayList<CartaOro> getOro() {
		return oro;
	}

	public void setOro(ArrayList<CartaOro> oro) {
		this.oro = oro;
	}

	public CartaObiettivo getObiettivo() {
		return obiettivo;
	}

	public void setObiettivo(CartaObiettivo obiettivo) {
		this.obiettivo = obiettivo;
	}

	public int getTurno() {
		return turno;
	}

	public void setTurno(int turno) {
		this.turno = turno;
	}

	public int getPunteggio() {
		return punteggio;
	}

	public void setPunteggio(int punteggio) {
		this.punteggio = punteggio;
	}

	public ArrayList<Integer> getNumRis() {
		return numRis;
	}

	public void setNumRis(ArrayList<Integer> numRis) {
		this.numRis = numRis;
	}

	public ArrayList<Integer> getNumOgg() {
		return numOgg;
	}

	public void setNumOgg(ArrayList<Integer> numOgg) {
		this.numOgg = numOgg;
	}
	
	
	
}
