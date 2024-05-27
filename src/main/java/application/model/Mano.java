package application.model;

import java.util.ArrayList;

/**
 * Questa classe descrive la mano di un giocatore, ossia le carte che un
 * giocatore ha nella sua mano.
 */
public class Mano {
	/**
	 * Attributo che rappresenta le carte risorsa in mano al giocatore.
	 */
	private ArrayList<CartaRisorsa> risorsa = new ArrayList<CartaRisorsa>();
	/**
	 * Attributo che rappresenta le carte oro in mano al giocatore.
	 */
	private ArrayList<CartaOro> oro = new ArrayList<CartaOro>();
	
	/**
	 * Metodo che ritorna le carte risorsa in mano al giocatore.
	 * @return
	 */
	public ArrayList<CartaRisorsa> getRisorsa() {
		return this.risorsa;
	}
	
	/**
	 * Metodo che ritorna le carte oro in mano al giocatore.
	 * @return
	 */
	public ArrayList<CartaOro> getOro() {
		return this.oro;
	}
	
	/**
	 * Metodo che restituisce la carta risorsa partendo dall'id.
	 * @param id
	 * @return
	 */
	public CartaOro getGoldById(String id) {
		for(CartaOro s: this.oro) {
			if(id.equals(s.getId())) {
				return s;
			}
		}
		
		return null;
	}
	
	/**
	 * Metodo che restituisce la carta oro partendo dall'id.
	 * @param id
	 * @return
	 */
	public CartaRisorsa getResourceById(String id) {
		for(CartaRisorsa s: this.risorsa) {
			if(id.equals(s.getId())) {
				return s;
			}
		}
		
		return null;
	}
	
	/**
	 * Metodo che sostuisce il fronte di una carta risorsa con il suo retro in mano.
	 * @param fronte
	 * @param retro
	 */
	public void setResourceElement(CartaRisorsa fronte, CartaRisorsa retro) {
		
		for(CartaRisorsa r : risorsa) {
			if(r.getId().equals(fronte.getId())) {
				r = retro;
			}
		}
	}
	
	/**
	 * Metodo che sostuisce il fronte di una carta oro con il suo retro in mano.
	 * @param fronte
	 * @param retro
	 */
	public void setGoldElement(CartaOro fronte, CartaOro retro) {
		
		for(CartaOro o : oro) {
			if(o.getId().equals(fronte.getId())) {
				o = retro;
			}
		}
	}
}
