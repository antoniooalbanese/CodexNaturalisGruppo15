package application;

import java.util.ArrayList;

import application.controller.Controller;
import application.model.Giocatore;
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
		int num;
		ArrayList<Giocatore> giocatori;
		
		controller.startGame();
		num = controller.getPlayersNumber();
		controller.initializeFielda();
		giocatori = controller.initializePlayers(num);
		
	}
}
