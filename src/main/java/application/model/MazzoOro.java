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
	 * Metodo che carica il contenuto del file json contenente le carte
	 * oro nel rispettivo mazzo.
	 * @throws JsonSyntaxException quando il file json non rispetta
	 * la sintassi json
	 * @throws IOException quando non viene trovato il file sul 
	 * percorso indicato
	 */
	public void load() throws JsonSyntaxException, IOException{
		ArrayList<CartaOro> mazzo = JsonHelper.loadJson("MazzoOro.json",  new TypeToken<List<CartaOro>>(){}.getType());
	
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
	 * Metodo che ritorna il mazzo delle carte oro visto sul fronte.
	 * @return lista delle carte presenti nel mazzo oro viste sul
	 * fronte
	 */
	public ArrayList<CartaOro> getMazzoFronte() {
		return this.mazzoFronte;
	}
	
	/**
	 * Metodo che ritorna il mazzo delle carte oro visto sul retro.
	 * @return lista delle carte presenti nel mazzo oro viste sul
	 * retro
	 */
	public ArrayList<CartaOro> getMazzoRetro() {
		return this.mazzoRetro;
	}
	
	/**
	 * Metodo che ritorna una determinata carta del mazzo retro delle 
	 * carte oro che appartiene al regno preso come parametro.
	 * @param risorsa: regno a cui appartiene la carta oro di cui si
	 * prende il retro
	 * @return retro della carta con il regno corrispondente
	 */
	public CartaOro getCartaRetroByRegno(Regno risorsa) {
		
		for(int i = 0; i < this.getMazzoRetro().size(); i++) {
			if(this.getMazzoRetro().get(i).getRegno().equals(risorsa)) {
				return this.getMazzoRetro().get(i);
			}
		}
		
		return null;
	}
	
	/**
	 * Metodo che ritorna il retro di una carta in posizione i del
	 * mazzo fronte delle carte oro.
	 * @param i: posizione della carta nel mazzo
	 * @return carta vista sul retro in posizione i nel mazzo 
	 */
	public CartaOro showRetro(int i) {
		return this.getCartaRetroByRegno(this.mazzoFronte.get(i).getRegno());	
	}
	
	/**
	 * Metodo che ritorna il retro di una carta.
	 * @param carta: carta di cui si vuole mostrare il retro
	 * @return retro della carta inserita come parametro
	 */
	public CartaOro getRetroCarta(CartaOro carta) {
		Regno risorsa = carta.getRegno();
		
		for(int i = 0; i < mazzoRetro.size(); i++) {
			if(this.mazzoRetro.get(i).getRegno().equals(risorsa)){
				return this.mazzoRetro.get(i);
			}
		}
		
		return null;
	}
}
	
