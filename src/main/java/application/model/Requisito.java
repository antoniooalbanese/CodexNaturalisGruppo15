package application.model;

import java.util.ArrayList;

/**
 * Questa classe descrive i requisiti di piazzamento delle carte oro.
 */
public class Requisito {
	/**
	 * Lista di risorse di cui è necessaria la presenza sulla propria board di
	 * gioco per piazzare una determinata carta.
	 */
	private ArrayList<Regno> risorsa = new ArrayList<Regno>();
	
	/**
	 * Costruttore della classe.
	 * @param risorsa
	 */
	public Requisito(ArrayList<Regno> risorsa) {
		this.risorsa = risorsa;
	}
}
