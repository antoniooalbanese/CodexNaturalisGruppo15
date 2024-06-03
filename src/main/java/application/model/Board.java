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
	private ArrayList<CartaRisorsa> risorsa = new ArrayList<CartaRisorsa>();
	/**
	 * Insieme di carte oro piazzate da un giocatore.
	 */
	private ArrayList<CartaOro> oro = new ArrayList<CartaOro>();
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
	 * Attributo che descrive il numero di obiettivi conseguiti dal
	 * giocatore
	 */
	private int numObj;
	
	/**
	 * Costruttore della classe.
	 * @param centro: carta iniziale posizionata al centro della board
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
		this.numObj = 0;
	}
	
	/**
	 * Metodo che ritorna la matrice che rappresenta la disposizione 
	 * delle carte sulla board.
	 * @return: matrice che rappresenta la board
	 */
	public String[][] getMatrix(){
		return this.matrix;
	}
	
	/**
	 * Metodo che modifica la matrice che rappresenta la 
	 * disposizione delle carte sulla board. 
	 * @param matrix: matrice che rappresenta la board
	 */
	public void setMatrix(String [][] matrix){
		this.matrix = matrix;
	}
	
	/**
	 * Metodo che modifica l'elemento della matrice nella posizione data in input
	 * @param row: riga a cui appartiene l'elemento da modificare
	 * @param column: colonna a cui appartiene l'elemento da modificare
	 * @param cell: id che viene assegnato nella cella nella posizione
	 * data 
	 */
	public void setMatrixElementByIndex(int row, int column, String cell) {
		this.matrix [row][column] = cell;
	}
	
	/**
	 * Metodo che ritorna la carta iniziale che si trova al centro
	 * della board.
	 * @return: carta iniziale al centro della board
	 */
	public CartaIniziale getCentro() {
		return centro;
	}
	
	/**
	 * Metodo che modifica la carta iniziale che si trova al centro 
	 * della board.
	 * @param centro: carta iniziale al centro della board
	 */
	public void setCentro(CartaIniziale centro) {
		this.centro = centro;
	}
	
	/**
	 * Metodo che ritorna le carte risorsa presenti nella board.
	 * @return: carte risorsa presenti nella board
	 */
	public ArrayList<CartaRisorsa> getRisorsa() {
		return risorsa;
	}
	
	/**
	 * Metodo che modifica le carte risorsa presenti nella board.
	 * @param risorsa: carte risorsa presenti nella board 
	 */
	public void setRisorsa(ArrayList<CartaRisorsa> risorsa) {
		this.risorsa = risorsa;
	}
	
	/**
	 * Metodo che ritorna le carte oro presenti nella board.
	 * @return: carte oro presenti nella board
	 */
	public ArrayList<CartaOro> getOro() {
		return oro;
	}
	
	/**
	 * Metodo che modifica le carte oro presenti nella board.
	 * @param oro: carte oro presenti nella board
	 */
	public void setOro(ArrayList<CartaOro> oro) {
		this.oro = oro;
	}
	
	/**
	 * Metodo che ritorna la carta obietttivo presente nella board.
	 * @return: carta obiettivo segreta presente nella board
	 */
	public CartaObiettivo getObiettivo() {
		return obiettivo;
	}
	
	/**
	 * Metodo che modifica la carta obiettivo presente nella board.
	 * @param obiettivo: carta obiettivo segreta presente nella board
	 */
	public void setObiettivo(CartaObiettivo obiettivo) {
		this.obiettivo = obiettivo;
	}
	
	/**
	 * Metodo che ritorna il numero dei turni giocati dal giocatore.
	 * @return: numero di turni giocati
	 */
	public int getTurno() {
		return turno;
	}
	
	/**
	 * Metodo che modifica il numero dei turni giocati dal giocatore.
	 * @param turno: numero di turni giocati
	 */
	public void setTurno(int turno) {
		this.turno = turno;
	}
	
	/**
	 * Metodo che ritorna il numero di punti in possesso del giocatore.
	 * @return: punteggio del giocatore
	 */
	public int getPunteggio() {
		return punteggio;
	}
	
	/**
	 * Metodo che modifica il numero di punti in possesso del giocatore.
	 * @param punteggio: punteggio del giocatore
	 */
	public void setPunteggio(int punteggio) {
		this.punteggio = punteggio;
	}
	
	/**
	 * Metodo che ritorna il numero dei vari tipi di risorse 
	 * presenti nella board.
	 * @return: numero dei vari tipi di risorsa
	 */
	public ArrayList<Integer> getNumRis() {
		return numRis;
	}
	
	/**
	 * Metodo che modifica il numero dei vari tipi di risorse presenti 
	 * nella board.
	 * @param numRis: numero dei vari tipi di risorsa
	 */
	public void setNumRis(ArrayList<Integer> numRis) {
		this.numRis = numRis;
	}
	
	/**
	 * Metodo che ritorna il numero dei vari tipi di oggetti presenti 
	 * nella board.
	 * @return: numero dei vari tipi di oggetti
	 */
	public ArrayList<Integer> getNumOgg() {
		return numOgg;
	}
	
	/**
	 * Metodo che modifica il numero dei vari tipi di oggetti presenti 
	 * nella board.
	 * @param numOgg: numero dei vari tipi di oggetti
	 */
	public void setNumOgg(ArrayList<Integer> numOgg) {
		this.numOgg = numOgg;
	}
	
	/**
	 * Metodo che ritorna il numero di obiettivi conseguiti dal 
	 * giocatore.
	 * @return: numero di obiettivi conseguiti dal giocatore
	 */
	public int getNumObj() {
		return numObj;
	}
	
	/**
	 * Metodo che modifica il numero di obiettivi conseguiti dal
	 * giocatore.
	 * @param numObj: numero di obiettivi conseguiti dal giocatore
	 */
	public void setNumObj(int numObj) {
		this.numObj = numObj;
	}
	
	/**
	 * Metodo che aggiorna il numero di obiettivi conseguiti dal
	 * giocatore.
	 * @param num: numero di obiettivi da aggiungere a quelli conseguiti
	 * dal giocatore
	 */
	public void addObj(int num) {
		this.numObj += num;
	}
	
	/**
	 * Metodo che ritorna la carta presente nella board che possiede
	 * l'id passato come parametro
	 * @param id: id della carta da prendere dalla board
	 * @return: la carta che ha come id quello inserito come parametro
	 */
	public Carta getByID (String id) {
		if(id.charAt(0)=='R') {
			for(CartaRisorsa r: this.risorsa ) {
				if(r.getId().equals(id)) {
					return r;
				}
			}
		}
		
		if(id.charAt(0)=='O') {
			for(CartaOro o: this.oro ) {
				if(o.getId().equals(id)) {
					return o;
				}
			}
		}
		
		if(id.charAt(0)=='I') {
			return this.centro;
		}
		
		return null;
	}	
}
