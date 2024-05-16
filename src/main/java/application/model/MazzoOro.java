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
	 * Lista di carte oro mostrate sul fronte.
	 */
	private ArrayList<CartaOro> mazzoFronte;
	/**
	 * Lista di carte oro mostrate sul retro.
	 */
	private ArrayList<CartaOro> mazzoRetro;
	/**
	 * Numero di carte rimamenti nel mazzo oro.
	 */
	private int rimanenti;
	
	/**
	 * Costruttore della classe.
	 */
	public MazzoOro() {
		this.mazzoFronte = new ArrayList<>();
        this.mazzoRetro = new ArrayList<>();
		this.rimanenti = 40;
	}
	/**
	 * Metodo che carica il contenuto del file json contenente le carte oro
	 * nel mazzo delle carte oro.
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public void load() throws JsonSyntaxException, IOException{
		ArrayList<CartaOro> mazzo = JsonHelper.loadJson("MazzoOro.json",  new TypeToken<List<CartaOro>>(){}.getType());
	
		for(int i = 0; i < mazzo.size() - 4; i++) {
			this.mazzoFronte.add(mazzo.get(i));
		}
		
		for(int i = 40; i < mazzo.size(); i++) {
			this.mazzoRetro.add(mazzo.get(i));
		}
	}
	
	/**
	 * Metodo che ritorna il mazzo oro visto sul fronte.
	 * @return
	 */
	public ArrayList<CartaOro> getMazzoFronte() {
		return this.mazzoFronte;
	}
	
	/**
	 * Metodo che ritorna il mazzo oro visto sul retro.
	 * @return
	 */
	public ArrayList<CartaOro> getMazzoRetro() {
		return this.mazzoRetro;
	}
	
	/**
	 * BISOGNA RICORDARE CHE IL MAZZO E' DA VISUALIZZARE MOSTRANDO IL RETRO
	 * DELLE CARTE.
	 */
}
	
