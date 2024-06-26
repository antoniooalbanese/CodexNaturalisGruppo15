package application.model;

import java.util.ArrayList;

/**
 * Questa classe rappresenta il campo di gioco contenente tutti gli 
 * elementi comuni che sono visibili da tutti i giocatori, compresi gli 
 * stessi giocatori.
 */
public class CampoDiGioco {
	/**
	 * Lista dei giocatori che partecipano alla partita.
	 */
	private ArrayList<Giocatore> giocatore = new ArrayList<Giocatore>();
	/**
	 * Mazzo contenente le carte risorsa non ancora pescate da nessun giocatore.
	 */
	private MazzoRisorsa mazzoR;
	/**
	 * Lista delle due carte risorsa visibili ai giocatori che questi possono
	 * scegliere di pescare.
	 */
	private ArrayList<CartaRisorsa> risorsa = new ArrayList<CartaRisorsa>();
	/**
	 * Mazzo contenente le carte oro non ancora pescate da nessun giocatore.
	 */
	private MazzoOro mazzoO;
	/**
	 * Lista delle due carte oro visibili ai giocatori che questi possono
	 * scegliere di pescare.
	 */
	private ArrayList<CartaOro> oro = new ArrayList<CartaOro>();
	/**
	 * Mazzo contenente le carte obiettivo non ancora pescate da nessun 
	 * giocatore.
	 */
	private MazzoObiettivo mazzoOb;
	/**
	 * Lista delle due carte obiettivo visibili ai giocatori che questi possono
	 * scegliere di pescare.
	 */
	private ArrayList<CartaObiettivo> obiettivo = new ArrayList<CartaObiettivo>();
	
	/**
	 * Metodo che ritorna la lista dei giocatori che giocano la partita.
	 * @return lista dei giocatori che giocano la partita
	 */
	public ArrayList<Giocatore> getGiocatore() {
		return this.giocatore;
	}
	
	/**
	 * Metodo che modifica la lista dei giocatori.
	 * @param giocatore: lista dei giocatori
	 */
	public void setGiocatore(ArrayList<Giocatore> giocatore) {
		this.giocatore = giocatore;
	}

	/**
	 * Metodo che permette di aggiungere un giocatore alla partita.
	 * @param g: giocatore che viene aggiunto alla partita
	 */
	public void addPlayer(Giocatore g) {
		this.giocatore.add(g);
	}
	
	/**
	 * Metodo che ritorna il mazzo risorsa.
	 * @return mazzo delle carte risorsa
	 */
	public MazzoRisorsa getMazzoR() {
		return this.mazzoR;
	}
	
	/**
	 * Metodo che modifica il mazzo risorsa.
	 * @param mazzoR: mazzo delle carte risorsa
	 */
	public void setMazzoR(MazzoRisorsa mazzoR) {
		this.mazzoR = mazzoR;
	}
	
	/**
	 * Metodo che ritorna le carte risorsa estratte sul campo di gioco.
	 * @return carte risorsa estratte sul campo di gioco
	 */
	public ArrayList<CartaRisorsa> getRisorsa() {
		return this.risorsa;
	}
	
	/**
	 * Metodo che modifica le carte risorsa estratte sul campo di gioco.
	 * @param risorsa: carte risorsa estratte sul campo di gioco
	 */
	public void setRisorsa(ArrayList<CartaRisorsa> risorsa) {
		this.risorsa = risorsa;
	}
	
	/**
	 * Metodo che ritorna il mazzo oro.
	 * @return mazzo delle carte oro
	 */
	public MazzoOro getMazzoO() {
		return this.mazzoO;
	}

	/**
	 * Metodo che modifica il mazzo oro.
	 * @param mazzoO: mazzo delle carte oro
	 */
	public void setMazzoO(MazzoOro mazzoO) {
		this.mazzoO = mazzoO;
	}

	/**
	 * Metodo che ritorna le carte oro estratte sul campo di gioco.
	 * @return carte oro estratte sul campo di gioco
	 */
	public ArrayList<CartaOro> getOro() {
		return oro;
	}
	
	/**
	 * Metodo che modifica le carte oro estratte sul campo di gioco.
	 * @param oro: carte oro estratte sul campo di gioco
	 */
	public void setOro(ArrayList<CartaOro> oro) {
		this.oro = oro;
	}
	
	/**
	 * Metodo che ritorna il mazzo delle carte obiettivo.
	 * @return mazzo delle carte obiettivo
	 */
	public MazzoObiettivo getMazzoOb() {
		return mazzoOb;
	}
	
	/**
	 * Metodo che modifica il mazzo delle carte obiettivo.
	 * @param mazzoOb: mazzo delle carte obiettivo
	 */
	public void setMazzoOb(MazzoObiettivo mazzoOb) {
		this.mazzoOb = mazzoOb;
	}
	
	/**
	 * Metodo che ritorna le carte obiettivo estratte sul campo di gioco.
	 * @return carte obiettivo estratte sul campo di gioco
	 */
	public ArrayList<CartaObiettivo> getObiettivo() {
		return obiettivo;
	}
	
	/**
	 * Metodo che modifica le carte obiettivo estratte sul campo di gioco.
	 * @param obiettivo: carte obiettivo estratte sul campo di gioco
	 */
	public void setObiettivo(ArrayList<CartaObiettivo> obiettivo) {
		this.obiettivo = obiettivo;
	}
}
