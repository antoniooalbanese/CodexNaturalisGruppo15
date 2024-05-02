package application.model;

import java.util.ArrayList;

/**
 * Questa classe rappresenta il mazzo delle carte iniziali.
 */
public class MazzoIniziale {
	/**
	 * Lista di carte iniziali.
	 */
	ArrayList<CartaIniziale> mazzo = new ArrayList<CartaIniziale>(6);
	
	/**
	 * Costruttore della classe.
	 */
	public MazzoIniziale(ArrayList<CartaIniziale> mazzo) {
		this.mazzo = mazzo;
	}
	
	/**
	 * BISOGNA RICORDARE CHE IL MAZZO E' DA VISUALIZZARE MOSTRANDO IL RETRO
	 * DELLE CARTE.
	 */
}
