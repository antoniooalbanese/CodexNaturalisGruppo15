package application.model;

import java.util.ArrayList;

/**
 * Questa classe rappresenta il mazzo delle carte risorsa.
 */
public class MazzoRisorsa {
	/**
	 * Lista di carte risorsa.
	 */
	ArrayList<CartaRisorsa> mazzo = new ArrayList<CartaRisorsa>(40);
	
	/**
	 * Costruttore della classe.
	 */
	public MazzoRisorsa(ArrayList<CartaRisorsa> mazzo) {
		this.mazzo = mazzo;
	}
	
	
	/**
	 * BISOGNA RICORDARE CHE IL MAZZO E' DA VISUALIZZARE MOSTRANDO IL RETRO
	 * DELLE CARTE.
	 */
	
}
