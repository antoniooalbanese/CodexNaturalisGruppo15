package application.controller;

import application.model.Model;
import application.view.View;

/**
 * Classe che gestisce l'andamento del gioco e l'interazione tra model e view.
 */
public class Controller {
	/**
	 * Attributo che rappresenta un'istanza del modello del gioco.
	 */
	private Model model;
	/**
	 * Attributo che rappresenta un'istanza dell'interfaccia del gioco.
	 */
	private View view;
	/**
	 * Numero dei giocatori della partita.
	 */
	private int num;
	
	/**
	 * Costruttore della classe.
	 * @param model
	 * @param view
	 */
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
		this.num = 0;
	}
	
	/**
	 * Metodo che fa partire il gioco.
	 */
	public void startGame(){
		
		if(!view.startMessage()) {
			view.endMessage();
			System.exit(0);
		}
	}
	
	public void getPlayersNumber() {
		num = view.getPlayersNumberMessage();
	}
}
