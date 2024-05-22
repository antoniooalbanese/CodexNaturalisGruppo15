package application.model;

import java.util.ArrayList;

/**
 * Questa classe descrive la mano di un giocatore, ossia le carte che un
 * giocatore ha nella sua mano.
 */
public class Mano {
	/**
	 * Attributo che rappresenta le carte risorsa in mano al giocatore.
	 */
	private ArrayList<CartaRisorsa> risorsa = new ArrayList<CartaRisorsa>();
	/**
	 * Attributo che rappresenta le carte oro in mano al giocatore.
	 */
	private ArrayList<CartaOro> oro = new ArrayList<CartaOro>();
	
	/**
	 * Metodo che ritorna le carte risorsa in mano al giocatore.
	 * @return
	 */
	public ArrayList<CartaRisorsa> getRisorsa() {
		return this.risorsa;
	}
	
	/**
	 * Metodo che ritorna le carte oro in mano al giocatore.
	 * @return
	 */
	public ArrayList<CartaOro> getOro() {
		return this.oro;
	}
}
