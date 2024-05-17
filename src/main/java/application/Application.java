package application;

import java.io.IOException;
import java.util.ArrayList;

import com.google.gson.JsonSyntaxException;

import application.controller.Controller;
import application.model.Giocatore;
import application.model.Model;
import application.view.View;

/**
 * Questa classe rappresenta il launcher del programma.(DA RIGUARDARE IL COMMENTO).
 */
public class Application {
	
	public static void main(String[] args) throws JsonSyntaxException, IOException {
		View view = new View();
		Model model = new Model();
		Controller controller = new Controller(model,view);
		
		controller.startGame();
		controller.getPlayersNumber();
		controller.initializePlayers();
		controller.initializeField();
		controller.giveStartCards();
		
	}
}
