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
		this.mazzoFronte = new ArrayList<>();
        this.mazzoRetro = new ArrayList<>();
		this.rimanenti = 40;
	}

	/**
	 * Metodo che carica il contenuto del file json contenente le carte
	 * risorsa nel rispettivo mazzo.
	 * @throws JsonSyntaxException quando il file json non rispetta
	 * la sintassi json
	 * @throws IOException quando non viene trovato il file sul 
	 * percorso indicato
	 */
	public void load() throws JsonSyntaxException, IOException{
		ArrayList<CartaRisorsa> mazzo = JsonHelper.loadJson("MazzoRisorsa.json",  new TypeToken<List<CartaRisorsa>>(){}.getType());
		
		for(int i = 0; i < mazzo.size() - 4; i++) {
			mazzo.get(i).setFronte(true);
			this.mazzoFronte.add(mazzo.get(i));
		}
		
		for(int i = 40; i < mazzo.size(); i++) {
			mazzo.get(i).setFronte(false);
			this.mazzoRetro.add(mazzo.get(i));
		}
	}
	
	/**
	 * Metodo che ritorna il mazzo delle carte risorsa visto sul fronte.
	 * @return lista delle carte presenti nel mazzo risorsa viste sul
	 * fronte
	 */
	public ArrayList<CartaRisorsa> getMazzoFronte() {
		return this.mazzoFronte;
	}
	
	/**
	 * Metodo che ritorna il mazzo delle carte risorsa visto sul retro.
	 * @return lista delle carte presenti nel mazzo risorsa viste sul
	 * retro
	 */
	public ArrayList<CartaRisorsa> getMazzoRetro() {
		return this.mazzoRetro;
	}
	

	/**
	 * Metodo che ritorna una determinata carta del mazzo retro
	 * delle carte risorsa che appartiene al regno preso come parametro.
	 * @param risorsa: regno a cui appartiene la carta risorsa di cui si
	 * prende il retro
	 * @return retro della carta con il regno corrispondente
	 */
	public CartaRisorsa getCartaRetroByRegno(Regno risorsa) {
		
		for(int i = 0; i < this.getMazzoRetro().size(); i++) {
			if(this.getMazzoRetro().get(i).getRegno().equals(risorsa)) {
				return this.getMazzoRetro().get(i);
			}
		}
		
		return null;
	}
	
	/**
	 * Metodo che ritorna il retro di una carta in posizione i del
	 * mazzo fronte delle carte risorsa.
	 * @param i: posizione della carta nel mazzo
	 * @return carta vista sul retro in posizione i nel mazzo 
	 */
	public CartaRisorsa showRetro(int i) {
		return this.getCartaRetroByRegno(this.mazzoFronte.get(i).getRegno());	
	}
	
	/**
	 * Metodo che ritorna il retro di una carta.
	 * @param carta: carta di cui si vuole mostrare il retro
	 * @return retro della carta inserita come parametro
	 */
	public CartaRisorsa getRetroCarta(CartaRisorsa carta) {
		Regno risorsa = carta.getRegno();
		
		for(int i = 0; i < mazzoRetro.size(); i++) {
			if(this.mazzoRetro.get(i).getRegno().equals(risorsa)){
				return this.mazzoRetro.get(i);
			}
		}
		
		return null;
	}
}
