package application.model;

import java.util.ArrayList;

/**
 * Questa classe rappresenta la board di un giocatore, ossia l'insieme di tutte
 * le carte piazzate da un giocatore.
 */
public class Board {
	/**
	 * Matrice che rappresenta la disposizione delle carte sulla board.
	 */
	private String [][] matrix;
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
	 * Lista contenente il numero di ogni risorsa presente sulla board, più nello
	 * specifico la lista di contatori è composta in questo modo:
	 * 	elemento 0: numero risorse di tipo VEGETALE;
	 * 	elemento 1: numero risorse di tipo ANIMALE;
	 * 	elemento 2: numero risorse di tipo FUNGHI;
	 * 	elemento 3: numero risorse di tipo INSETTI. 
	 */
	private ArrayList<Integer> numRis = new ArrayList<Integer>();
	
	/**
	 * Lista contenente il numero di ogni oggetto presente sulla board, più nello
	 * specifico la lista di contatori è composta in questo modo:
	 * 	elemento 0: numero oggetti di tipo PIUMA;
	 * 	elemento 1: numero oggetti di tipo INCHIOSTRO;
	 * 	elemento 2: numero oggetti di tipo PERGAMENA;
	 */
	private ArrayList<Integer> numOgg = new ArrayList<Integer>();
	
	/**
	 * Costruttore della classe.
	 * @param centro
	 */
	public Board(CartaIniziale centro) {
		this.matrix = new String [9][9];
		this.centro = centro;
		this.turno = 0;
		this.punteggio = 0;
		for(int i=0; i<4; i++) {
			this.numRis.add(0);
		}
		for(int i=0; i<3; i++) {
			this.numOgg.add(0);
		}
	}
	
	/**
	 * Metodo che ritorna la matrice che rappresenta la disposizione delle carte 
	 * sulla board.
	 * @return
	 */
	public String[][] getMatrix(){
		return this.matrix;
	}
	
	/**
	 * Metodo che modifica la matrice che rappresenta la disposizione delle carte 
	 * sulla board. 
	 * @param matrix
	 */
	public void setMatrix(String [][] matrix){
		this.matrix = matrix;
	}
	
	/**
	 * Metodo che modifica l'elemento della matrice nella posizione data in input
	 * @param row
	 * @param column
	 * @param cell
	 */
	public void setMatrixElementByIndex(int row, int column, String cell) {
		this.matrix [row][column] = cell;
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
	
	public Carta getByID (String id) {
		if(id.charAt(0)=='R') {
			for(CartaRisorsa r: this.risorsa ) {
				if(r.equals(id)) {
					return r;
				}
			}
		}
		if(id.charAt(0)=='O') {
			for(CartaOro o: this.oro ) {
				if(o.equals(id)) {
					return o;
				}
			}
		}
		return null;
	}
	
	
	
}
