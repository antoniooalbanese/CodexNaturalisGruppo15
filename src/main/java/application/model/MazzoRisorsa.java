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
	 * Lista di carte risorsa.
	 */
	private ArrayList<CartaRisorsa> mazzo;
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
		this.mazzo = JsonHelper.loadJson("MazzoRisorsa.json",  new TypeToken<List<CartaRisorsa>>(){}.getType());
	}
	
	/**
	 * BISOGNA RICORDARE CHE IL MAZZO E' DA VISUALIZZARE MOSTRANDO IL RETRO
	 * DELLE CARTE.
	 */
	
}
