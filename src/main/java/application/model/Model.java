package application.model;

import java.util.ArrayList;

import application.controller.Controller;

/**
 * Classe che racchiude tutti i componenti del gioco.
 */
public class Model {
	/**
	 * Attributo che descrive il campo di gioco.
	 */
	private CampoDiGioco campo;
	/**
	 * Costruttore della classe.
	 * @param campo
	 */
	public Model(CampoDiGioco campo) {
		this.campo = campo;
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
}
