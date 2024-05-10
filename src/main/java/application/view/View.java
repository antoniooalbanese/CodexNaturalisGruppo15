package application.view;

import application.controller.Controller;

/**
 * Classe che rappresenta l'interfaccia di output e input utilizzata dai giocatori.
 */
public class View {
	private Controller controller;
	
	/**
	 * Costruttore della classe.
	 * @param controller
	 */
	public View(Controller controller) {
		this.controller = controller;
	}
}
