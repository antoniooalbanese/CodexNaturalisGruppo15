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
	 * Costruttore della classe Giocatore quando il giocatore inizia la partita.
	 * @param id
	 * @param nick
	 * @param pedina
	 */
	public Giocatore(String id, String nick, Pedina pedina) {
		this.id = id;
		this.nick = nick;
		this.pedina = pedina;
		this.inizio = null;
		this.mano = null;
		this.board = null;
	}
	
	/**
	 * Questo metodo ritorna l'id del giocatore.
	 * @return
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Metodo che ritorna la mano del giocatore.
	 * @return
	 */
	public Mano getMano() {
        return this.mano;
    }
	
	/**
	 * Metodo che inizializza la board del giocatore.
	 * @param card
	 */
	public void initBoard(CartaIniziale card) {
		this.board = new Board(card);
	}
	
	/**
	 * Metodo che ritorna la board del giocatore.
	 * @return
	 */
	public Board getBoard() {
		return this.board;
	}
	
}
