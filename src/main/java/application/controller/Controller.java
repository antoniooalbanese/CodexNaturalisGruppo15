package application.controller;

import application.model.Model;
import application.view.View;

/**
 * Classe che gestisce l'andamento del gioco e l'interazione tra model e view.
 */
public class Controller {
	private Model model;
	private View view;
	
	/**
	 * Costruttore della classe.
	 * @param model
	 * @param view
	 */
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
	}
}
