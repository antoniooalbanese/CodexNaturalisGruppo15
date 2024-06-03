package application.model;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

/**
 * Questa classe rappresenta il mazzo delle carte obiettivo.
 */
public class MazzoObiettivo {
	/**
	 * Lista di carte obiettivo.
	 */
	private ArrayList<CartaObiettivo> mazzo;
	
	/**
	 * Metodo che carica il contenuto del file json contenente le carte
	 * obiettivo nel mazzo delle carte obiettivo.
	 * @throws JsonSyntaxException: quando non è rispettata la sintassi
	 * json
	 * @throws IOException: quando il file non viene trovato nel 
	 * percorso indicato
	 */
	public void load() throws JsonSyntaxException, IOException{
		this.mazzo = JsonHelper.loadJson("MazzoObiettivo.json",  new TypeToken<List<CartaObiettivo>>(){}.getType());
	}
	
	/**
	 * Metodo che ritorna il mazzo iniziale visto sul fronte.
	 * @return: mazzo iniziale visto sul fronte
	 */
	public ArrayList<CartaObiettivo> getMazzoFronte() {
		return this.mazzo;
	}
}
