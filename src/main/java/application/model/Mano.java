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
	ArrayList<CartaRisorsa> risorsa = new ArrayList<CartaRisorsa>(4);
	/**
	 * Attributo che rappresenta le carte oro in mano al giocatore.
	 */
	ArrayList<CartaOro> oro = new ArrayList<CartaOro>(4);
	
	/**
	 * Costruttore della classe.
	 * @param risorsa
	 * @param oro
	 */
	public Mano(ArrayList<CartaRisorsa> risorsa, ArrayList<CartaOro> oro) {
		this.risorsa = risorsa;
		this.oro = oro;
	}
}
