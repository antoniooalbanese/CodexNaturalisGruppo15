package application.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * Questa classe rappresenta il mazzo delle carte risorsa.
 */
public class MazzoRisorsa {
	/**
	 * Lista di carte risorsa mostrate sul fronte.
	 */
	private ArrayList<CartaRisorsa> mazzoFronte;
	/**
	 * Lista di carte risorsa mostrate sul retro.
	 */
	private ArrayList<CartaRisorsa> mazzoRetro;
	/**
	 * Numero di carte rimamenti nel mazzo risorsa.
	 */
	private int rimanenti;
	
	/**
	 * Costruttore della classe.
	 */
	public MazzoRisorsa() {
		this.rimanenti = 40;
	}

	/**
	 * Metodo che carica il contenuto del file json contenente le carte risorsa
	 * nel mazzo delle carte risorsa.
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public void load() throws JsonSyntaxException, IOException{
		ArrayList<CartaRisorsa> mazzo = JsonHelper.loadJson("MazzoRisorsa.json",  new TypeToken<List<CartaRisorsa>>(){}.getType());
		
		for(int i = 0; i < mazzo.size() - 4; i++) {
			this.mazzoFronte.add(mazzo.get(i));
		}
		
		for(int i = 40; i < mazzo.size(); i++) {
			this.mazzoRetro.add(mazzo.get(i));
		}
	}
	
	/**
	 * BISOGNA RICORDARE CHE IL MAZZO E' DA VISUALIZZARE MOSTRANDO IL RETRO
	 * DELLE CARTE.
	 */
	
}
