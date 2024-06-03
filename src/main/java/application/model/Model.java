package application.model;

import java.io.IOException;
import com.google.gson.JsonSyntaxException;

/**
 * Classe che racchiude tutti i componenti del gioco.
 */
public class Model {
	/**
	 * Attributo che descrive il campo di gioco.
	 */
	private CampoDiGioco campo;
	
	/**
	 * Attributo che rappresenta il mazzo contenente le carte iniziali.
	 */
	private MazzoIniziale mazzoI = new MazzoIniziale();
	
	/**
	 * Costruttore della classe.
	 * @throws JsonSyntaxException quando il file json non rispetta
	 * la sintassi json
	 * @throws IOException quando non viene trovato il file sul 
	 * percorso indicato
	 */
	public Model() throws JsonSyntaxException, IOException {
		this.campo = null;
		mazzoI.load();
	}
	
	/**
	 * Metodo che ritorna il campo di gioco.
	 * @return campo di gioco in cui si sta giocando la partita
	 * contenente tutti gli oggetti che servono
	 */
	public CampoDiGioco getCampo() {
		return this.campo;
	}
	
	/**
	 * Metodo che inizializza il campo da gioco.
	 */
	public void initCampo() {
		this.campo = new CampoDiGioco();
	}
	
	/**
	 * Metodo che ritorna il il mazzo iniziale.
	 * @return mazzo della carte iniziali presente sul campo di gioco
	 */
	public MazzoIniziale getMazzoIniziale() {
		return this.mazzoI;
	}
}
