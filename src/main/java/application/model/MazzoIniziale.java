package application.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * Questa classe rappresenta il mazzo delle carte iniziali.
 */
public class MazzoIniziale {
	/**
	 * Lista di carte iniziali.
	 */
	private ArrayList<CartaIniziale> mazzo;
	
	/**
	 * Metodo che carica il contenuto del file json contenente le carte iniziali
	 * nel mazzo delle carte iniziali.
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public void load() throws JsonSyntaxException, IOException{
		this.mazzo = JsonHelper.loadJson("MazzoIniziale.json",  new TypeToken<List<CartaIniziale>>(){}.getType());
	}
	
	/**
	 * BISOGNA RICORDARE CHE IL MAZZO E' DA VISUALIZZARE MOSTRANDO IL RETRO
	 * DELLE CARTE.
	 */
}
