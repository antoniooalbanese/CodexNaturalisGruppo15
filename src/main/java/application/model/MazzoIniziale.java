package application.model;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Questa classe rappresenta il mazzo delle carte iniziali.
 */
public class MazzoIniziale {
	/**
	 * Lista di carte iniziali.
	 */
	ArrayList<CartaIniziale> mazzo = new ArrayList<CartaIniziale>(6);
	
	/**
	 * Costruttore della classe, inizializzando il mazzo vuoto.
	 */
	public MazzoIniziale(ArrayList<CartaIniziale> mazzo) {
		this.mazzo = null;
	}
	
	/**
	 * Metodo per caricare il mazzo delle carte iniziali partendo dal file json MazzoIniziale.json.
	 * @return
	 */
	public ArrayList<CartaIniziale> loadMazzoI(){
		Gson gson = new Gson();
		String filename = "MazzoIniziale.json";
        ArrayList<CartaIniziale> iniziale = new ArrayList<CartaIniziale>();

        try (FileReader reader = new FileReader(filename)){
            Type listType = new TypeToken<ArrayList<CartaIniziale>>(){}.getType();
           iniziale = gson.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return iniziale;
	}
	
	/**
	 * BISOGNA RICORDARE CHE IL MAZZO E' DA VISUALIZZARE MOSTRANDO IL RETRO
	 * DELLE CARTE.
	 */
}
