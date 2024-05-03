package application.model;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Questa classe rappresenta il mazzo delle carte risorsa.
 */
public class MazzoRisorsa {
	/**
	 * Lista di carte risorsa.
	 */
	ArrayList<CartaRisorsa> mazzo = new ArrayList<CartaRisorsa>(40);
	
	/**
	 * Costruttore della classe, inizializzando il mazzo vuoto.
	 */
	public MazzoRisorsa(ArrayList<CartaRisorsa> mazzo) {
		this.mazzo = null;
	}

	/**
	 * Metodo per caricare il mazzo delle carte risorsa partendo dal file json MazzoRisorsa.json.
	 * @return
	 */
	public ArrayList<CartaRisorsa> loadMazzoR(){
		Gson gson = new Gson();
		String filename = "MazzoRisorsa.json";
        ArrayList<CartaRisorsa> risorsa = new ArrayList<CartaRisorsa>();

        try (FileReader reader = new FileReader(filename)){
            Type listType = new TypeToken<ArrayList<CartaRisorsa>>(){}.getType();
           risorsa = gson.fromJson(reader, listType);
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        return risorsa;
	}
	
	/**
	 * BISOGNA RICORDARE CHE IL MAZZO E' DA VISUALIZZARE MOSTRANDO IL RETRO
	 * DELLE CARTE.
	 */
	
}
