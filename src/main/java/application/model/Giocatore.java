package application.model;

/**
 * Questa classe rappresenta un giocatore della partita. 
 */
public class Giocatore {
	/**
	 * Attributo che rappresenta il codice identificativo del giocatore.
	 */
	private String id;
	/**
	 * Attributo che rappresenta il nickname che il giocatore sceglie di
	 * mostrare agli altri giocatori.
	 */
	private String nick;
	/**
	 * Punteggio attribuito al giocatore.
	 */
	private int punteggio;
	/**
	 * Pedina del colore scelto dal giocatore.
	 */
	private Pedina pedina;
	/**
	 * Pedina nera data al giocatore che inizia la partita.
	 */
	private Pedina inizio;
	/**
	 * Attributo che rappresenta la mano del giocatore.
	 */
	private Mano mano;
	/**
	 * Attributo che rappresenta l'insieme delle carte piazzate dal giocatore
	 * sul campo di gioco.
	 */
	private Board board;
	
	/**
	 * Costruttore della classe.
	 * @param id
	 * @param nick
	 * @param punteggio
	 * @param pedina
	 * @param inizio
	 * @param mano
	 * @param board
	 */
	public Giocatore(String id, String nick, int punteggio, Pedina pedina,
					 Pedina inizio, Mano mano, Board board) {
		this.id = id;
		this.nick = nick;
		this.punteggio = punteggio;
		this.pedina = pedina;
		this.inizio = inizio;
		this.mano = mano;
		this.board = board;
	}
	
}
