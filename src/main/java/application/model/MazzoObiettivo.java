package application.model;

import java.util.ArrayList;

/**
 * Questa classe rappresenta il mazzo delle carte obiettivo.
 */
public class MazzoObiettivo {
	/**
	 * Lista di carte obiettivo.
	 */
	ArrayList<CartaObiettivo> mazzo = new ArrayList<CartaObiettivo>(16);
	
	/**
	 * Costruttore della classe.
	 */
	public MazzoObiettivo(ArrayList<CartaObiettivo> mazzo) {
		this.mazzo = mazzo;
	}
	
	/**
	 * BISOGNA RICORDARE CHE IL MAZZO E' DA VISUALIZZARE MOSTRANDO IL RETRO
	 * DELLE CARTE.
	 */
}
