package application;

import java.io.IOException;
import com.google.gson.JsonSyntaxException;
import application.controller.Controller;
import application.model.Model;
import application.view.View;

/**
 * Questa classe rappresenta il launcher del programma.
 */
public class Application {
	
	/**
	 * Metodo che lancia il programma.
	 * @param args: argomenti della riga di comando passati 
	 * durante l'esecuzione del programma
	 * @throws JsonSyntaxException quando il file json non rispetta
	 * la sintassi json
	 * @throws IOException quando non viene trovato il file sul 
	 * percorso indicato
	 */
	public static void main(String[] args) throws JsonSyntaxException, IOException {
		View view = new View();
		Model model = new Model();
		Controller controller = new Controller(model,view);
		
		/**
		 * Metodi che inizializzano la partita.
		 */
		controller.startGame();
		controller.getPlayersNumber();
		controller.initializePlayers();
		controller.initializeField();
		controller.giveStartCards();
		controller.giveObjectiveCards();
		controller.chooseFirstPlayer();
		
		/**
		 * Metodi che gestiscono la partita.
		 */
		controller.playGame();
		
		/**
		 * Metodo che gestisce l'uscita dalla partita.
		 */
		controller.endGame();
		
	}
}
