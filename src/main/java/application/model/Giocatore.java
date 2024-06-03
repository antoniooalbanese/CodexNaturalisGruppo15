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
	 * Attributo che rappresenta il nickname che il giocatore sceglie 
	 * di mostrare agli altri giocatori.
	 */
	private String nick;
	
	/**
	 * Pedina del colore scelto dal giocatore.
	 * 
	 */
	private Pedina pedina;
	
	/**
	 * Attributo che indica se si è in possesso della pedina nera 
	 * data al giocatore che inizia la partita.
	 */
	private boolean inizio;
	
	/**
	 * Attributo che rappresenta la mano del giocatore.
	 */
	private Mano mano = new Mano();
	
	/**
	 * Attributo che rappresenta l'insieme delle carte piazzate dal 
	 * giocatore sul campo di gioco.
	 */
	private Board board;
		
	/**
	 * Costruttore della classe Giocatore quando il giocatore 
	 * inizia la partita.
	 * @param id: codice univoco identificativo del giocatore
	 * @param nick: nickname mostrato agli altri giocatori
	 * @param pedina: pedina del colore scelto dal giocatore
	 */
	public Giocatore(String id, String nick, Pedina pedina) {
		this.id = id;
		this.nick = nick;
		this.pedina = pedina;
		this.inizio = false;
	}
	
	/**
	 * Questo metodo ritorna l'id del giocatore.
	 * @return codice identificativo del giocatore
	 */
	public String getId() {
		return this.id;
	}
	
	/**
	 * Questo metodo ritorna il nickname del giocatore.
	 * @return nickname del giocatore
	 */
	public String getNick() {
		return this.nick;
	}
	
	/**
	 * Metodo che ritorna la mano del giocatore.
	 * @return carte in mano del giocatore
	 */
	public Mano getMano() {
        return this.mano;
    }
	
	/**
	 * Metodo che inizializza la board del giocatore.
	 * @param card: carta iniziale posizionata al centro della board
	 * del giocatore
	 */
	public void initBoard(CartaIniziale card) {
		this.board = new Board(card);
	}
	
	/**
	 * Metodo che ritorna la board del giocatore.
	 * @return carte in campo del giocatore
	 */
	public Board getBoard() {
		return this.board;
	}
	
	/**
	 * Metodo che modifica il possesso della pedina nera(il primo 
	 * giocatore ad iniziare un nuovo turno).
	 */
	public void setInizio() {
		this.inizio = true;
	}
}
