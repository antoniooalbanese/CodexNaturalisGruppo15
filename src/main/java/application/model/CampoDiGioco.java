package application.model;

import java.util.ArrayList;

/**
 * Questa classe rappresenta il campo di gioco contenente tutti gli elementi
 * comuni che sono visibili da tutti i giocatori, compresi gli stessi 
 * giocatori.
 */
public class CampoDiGioco {
	/**
	 * Lista dei giocatori che partecipano alla partita.
	 */
	private ArrayList<Giocatore> giocatore = new ArrayList<Giocatore>(4);
	/**
	 * Mazzo contenente le carte risorsa non ancora pescate da nessun giocatore.
	 */
	private MazzoRisorsa mazzoR;
	/**
	 * Lista delle due carte risorsa visibili ai giocatori che questi possono
	 * scegliere di pescare.
	 */
	private ArrayList<CartaRisorsa> risorsa = new ArrayList<CartaRisorsa>(2);
	/**
	 * Mazzo contenente le carte oro non ancora pescate da nessun giocatore.
	 */
	private MazzoOro mazzoO;
	/**
	 * Lista delle due carte oro visibili ai giocatori che questi possono
	 * scegliere di pescare.
	 */
	private ArrayList<CartaOro> oro = new ArrayList<CartaOro>(2);
	/**
	 * Mazzo contenente le carte obiettivo non ancora pescate da nessun 
	 * giocatore.
	 */
	private MazzoObiettivo mazzoOb;
	/**
	 * Lista delle due carte obiettivo visibili ai giocatori che questi possono
	 * scegliere di pescare.
	 */
	private ArrayList<CartaObiettivo> obiettivo = new ArrayList<CartaObiettivo>(2);
	
	public CampoDiGioco(ArrayList<Giocatore> giocatore, MazzoRisorsa mazzoR,
						ArrayList<CartaRisorsa> risorsa, MazzoOro mazzoO,
						ArrayList<CartaOro> oro, MazzoObiettivo mazzoOb,
						ArrayList<CartaObiettivo> obiettivo) {
		this.giocatore = giocatore;
		this.mazzoR = mazzoR;
		this.risorsa = risorsa;
		this.mazzoO = mazzoO;
		this.oro = oro;
		this.mazzoOb = mazzoOb;
		this.obiettivo = obiettivo;
	}
}
