package application.model;

import java.util.ArrayList;

/**
 * Questa classe rappresenta il mazzo delle carte oro.
 */
public class MazzoOro {
	/**
	 * Lista di carte oro.
	 */
	ArrayList<CartaOro> mazzo = new ArrayList<CartaOro>(40);
	
	/**
	 * Costruttore della classe.
	 */
	public MazzoOro(ArrayList<CartaOro> mazzo) {
		this.mazzo = mazzo;
	}
	
	/**
	 * BISOGNA RICORDARE CHE IL MAZZO E' DA VISUALIZZARE MOSTRANDO IL RETRO
	 * DELLE CARTE.
	 */
}
