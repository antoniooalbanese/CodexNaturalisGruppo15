package application;

import application.controller.Controller;
import application.model.Model;
import application.view.View;

/**
 * Questa classe rappresenta il launcher del programma.(DA RIGUARDARE IL COMMENTO).
 */
public class Application {
	
	public static void main(String[] args) {
		View view = new View();
		Model model = new Model(null);
		Controller controller = new Controller(model,view);
		
		controller.startGame();
		controller.getPlayersNumber();
	}
}
