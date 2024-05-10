package application.model;

import application.controller.Controller;

/**
 * Classe che racchiude tutti i componenti del gioco.
 */
public class Model {
	private CampoDiGioco campo;
	private Controller controller;
	
	/**
	 * Costruttore della classe.
	 * @param campo
	 * @param controller
	 */
	public Model(CampoDiGioco campo, Controller controller) {
		this.campo = campo;
		this.controller = controller;
	}
	
}
