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
	
	/**
	 * Metodo che ritorna la carta iniziale che si trova al centro della board.
	 * @return
	 */
	public CartaIniziale getCentro() {
		return centro;
	}
	
	/**
	 * Metodo che modifica la carta iniziale che si trova al centro della board.
	 * @param centro
	 */
	public void setCentro(CartaIniziale centro) {
		this.centro = centro;
	}
	
	/**
	 * Metodo che ritorna le carte risorsa presenti nella board.
	 * @return
	 */
	public ArrayList<CartaRisorsa> getRisorsa() {
		return risorsa;
	}
	
	/**
	 * Metodo che modifica le carte risorsa presenti nella board.
	 * @param risorsa
	 */
	public void setRisorsa(ArrayList<CartaRisorsa> risorsa) {
		this.risorsa = risorsa;
	}
	
	/**
	 * Metodo che ritorna le carte oro presenti nella board.
	 * @return
	 */
	public ArrayList<CartaOro> getOro() {
		return oro;
	}
	
	/**
	 * Metodo che modifica le carte oro presenti nella board.
	 * @param oro
	 */
	public void setOro(ArrayList<CartaOro> oro) {
		this.oro = oro;
	}
	
	/**
	 * Metodo che ritorna la carta obietttivo presente nella board.
	 * @return
	 */
	public CartaObiettivo getObiettivo() {
		return obiettivo;
	}
	
	/**
	 * Metodo che modifica la carta obiettivo presente nella board.
	 * @param obiettivo
	 */
	public void setObiettivo(CartaObiettivo obiettivo) {
		this.obiettivo = obiettivo;
	}
	
	/**
	 * Metodo che ritorna il numero dei turni giocati dal giocatore.
	 * @return
	 */
	public int getTurno() {
		return turno;
	}
	
	/**
	 * Metodo che modifica il numero dei turni giocati dal giocatore.
	 * @param turno
	 */
	public void setTurno(int turno) {
		this.turno = turno;
	}
	
	/**
	 * Metodo che ritorna il numero di punti in possesso del giocatore.
	 * @return
	 */
	public int getPunteggio() {
		return punteggio;
	}
	
	/**
	 * Metodo che modifica il numero di punti in possesso del giocatore.
	 * @param punteggio
	 */
	public void setPunteggio(int punteggio) {
		this.punteggio = punteggio;
	}
	
	/**
	 * Metodo che ritorna il numero dei vari tipi di risorse presenti nella board.
	 * @return
	 */
	public ArrayList<Integer> getNumRis() {
		return numRis;
	}
	
	/**
	 * Metodo che modifica il numero dei vari tipi di risorse presenti nella board.
	 * @param numRis
	 */
	public void setNumRis(ArrayList<Integer> numRis) {
		this.numRis = numRis;
	}
	
	/**
	 * Metodo che ritorna il numero dei vari tipi di oggetti presenti nella board.
	 * @return
	 */
	public ArrayList<Integer> getNumOgg() {
		return numOgg;
	}
	
	/**
	 * Metodo che modifica il numero dei vari tipi di oggetti presenti nella board.
	 * @param numOgg
	 */
	public void setNumOgg(ArrayList<Integer> numOgg) {
		this.numOgg = numOgg;
	}
	
	
	
}
