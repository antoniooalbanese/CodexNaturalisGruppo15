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
	 */
	public Model() throws JsonSyntaxException, IOException {
		this.campo = null;
		mazzoI.load();
	}
	
	/**
	 * Metodo che ritorna il campo di gioco.
	 * @return
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
	 * @return
	 */
	public MazzoIniziale getMazzoIniziale() {
		return this.mazzoI;
	}
	
}
