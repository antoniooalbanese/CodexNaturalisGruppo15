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
	 * Lista di carte iniziali mostrate sul fronte.
	 */
	private ArrayList<CartaIniziale> mazzoFronte;
	/**
	 * Lista di carte iniziali mostrate sul retro.
	 */
	private ArrayList<CartaIniziale> mazzoRetro;
	
	/**
	 * Metodo che carica il contenuto del file json contenente le carte iniziali
	 * nel mazzo delle carte iniziali.
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public void load() throws JsonSyntaxException, IOException{
		ArrayList <CartaIniziale> mazzo = JsonHelper.loadJson("MazzoIniziale.json",  new TypeToken<List<CartaIniziale>>(){}.getType());
		
		for(int i = 0; i < mazzo.size() - 6; i++) {
			this.mazzoFronte.add(mazzo.get(i));
		}
		
		for(int i = 6; i < mazzo.size(); i++) {
			this.mazzoRetro.add(mazzo.get(i));
		}
	}
	
	/**
	 * Metodo che ritorna il mazzo iniziale visto sul fronte.
	 * @return
	 */
	public ArrayList<CartaIniziale> getMazzoFronte() {
		return this.mazzoFronte;
	}
	
	/**
	 * Metodo che ritorna il mazzo iniziale visto sul retro.
	 * @return
	 */
	public ArrayList<CartaIniziale> getMazzoRetro() {
		return this.mazzoRetro;
	}
	
	/**
	 * Metodo che ritorna la il retro di una carta.
	 * @param carta
	 * @return
	 */
	public CartaIniziale getRetroCarta(CartaIniziale carta) {
		String id = carta.getId();
		String last = id.substring(2);
		
		for(int i = 0; i < mazzoRetro.size(); i++) {
			if(this.mazzoRetro.get(i).getId().contains(last)){
				return this.mazzoRetro.get(i);
			}
		}
		return null;
	}
	/**
	 * BISOGNA RICORDARE CHE IL MAZZO E' DA VISUALIZZARE MOSTRANDO IL RETRO
	 * DELLE CARTE.
	 */
}
