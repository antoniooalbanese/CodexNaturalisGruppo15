package application.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import java.io.IOException;

/**
 * Questa classe rappresenta il mazzo delle carte oro.
 */
public class MazzoOro {
	/**
	 * Lista di carte oro.
	 */
	private ArrayList<CartaOro> mazzo;
	/**
	 * Numero di carte rimamenti nel mazzo oro.
	 */
	private int rimanenti;
	
	/**
	 * Costruttore della classe.
	 */
	public MazzoOro() {
		this.rimanenti = 40;
	}
	/**
	 * Metodo che carica il contenuto del file json contenente le carte oro
	 * nel mazzo delle carte oro.
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public void load() throws JsonSyntaxException, IOException{
		this.mazzo = JsonHelper.loadJson("MazzoOro.json",  new TypeToken<List<CartaOro>>(){}.getType());
	}
	
	/**
	 * BISOGNA RICORDARE CHE IL MAZZO E' DA VISUALIZZARE MOSTRANDO IL RETRO
	 * DELLE CARTE.
	 */
}
	
