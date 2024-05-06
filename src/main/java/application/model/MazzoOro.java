package application.model;

import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Questa classe rappresenta il mazzo delle carte oro.
 */
public class MazzoOro {
	/**
	 * Lista di carte oro.
	 */
	ArrayList<CartaOro> mazzo = new ArrayList<CartaOro>(40);
	/**
	 * Numero di carte rimamenti nel mazzo oro.
	 */
	private int rimanenti;
	
	/**
	 * Costruttore della classe, inizializzando il mazzo vuoto.
	 */
	public MazzoOro(ArrayList<CartaOro> mazzo) {
		this.mazzo = null;
		this.rimanenti = 40;
	}
	
	/**
	 * Metodo per caricare il mazzo delle carte oro partendo dal file json MazzoOro.json.
	 * @return
	 */
	public ArrayList<CartaOro> loadMazzoO(){
		Gson gson = new Gson();
		String filename = "MazzoOro.json";
        ArrayList<CartaOro> oro = new ArrayList<CartaOro>();

        try (FileReader reader = new FileReader(filename)){
            Type listType = new TypeToken<ArrayList<CartaOro>>(){}.getType();
            oro = gson.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return oro;
	}
	
	/**
	 * BISOGNA RICORDARE CHE IL MAZZO E' DA VISUALIZZARE MOSTRANDO IL RETRO
	 * DELLE CARTE.
	 */
}
	
