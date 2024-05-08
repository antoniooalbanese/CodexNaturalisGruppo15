package application.model;

/**
 * Questa è la classe astratta che descrive una generica carta da gioco.
 * Si tratta di una classe astratta in quanto ogni tipo di carta possiede
 * caratteristiche diverse dagli altri tipi di carte. Quindi ogni tipo di carta
 * sarà un'estensionedi questa classe.
 */
public abstract class Carta {
	/**
	 * Codice univoco che identifica una carta specifica.
	 */
	private String id;
	/**
	 * Varibile booleana che, settata a True, indica che la carta mostra 
	 * il proprio fronte, mentre, settata a False, indica che la carta 
	 * mostra il proprio retro.
	 */
	private boolean fronte;
	
	/**
	 * Questo è il costruttore della classe.
	 * @param id
	 * @param fronte
	 */
	public Carta (String id, boolean fronte) {
		this.id = id;
		this.fronte = fronte;
	}
}
