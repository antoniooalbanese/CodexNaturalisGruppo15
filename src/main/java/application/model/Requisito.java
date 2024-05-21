package application.model;

import java.util.ArrayList;

/**
 * Questa classe descrive i requisiti di piazzamento delle carte oro.
 */
public class Requisito {
	/**
	 * Lista di risorse di cui è necessaria la presenza sulla propria board di
	 * gioco per piazzare una determinata carta.
	 */
	private ArrayList<Regno> risorsa = new ArrayList<Regno>();
	
	/**
	 * Costruttore della classe.
	 * @param risorsa
	 */
	public Requisito(ArrayList<Regno> risorsa) {
		this.risorsa = risorsa;
	}

	/**
	 * Metodo che ritorna il tipo di risorse richieste per il posizionamento.
	 * @return
	 */
	public ArrayList<Regno> getRisorsa() {
		return risorsa;
	}

	/**
	 * Metodo che modifica il tipo di risorse richieste per il posizionamento.
	 * @param risorsa
	 */
	public void setRisorsa(ArrayList<Regno> risorsa) {
		this.risorsa = risorsa;
	}
	
	/**
	 * Metodo che prende tutte le informazioni di un requisito e le ritorna sotto
	 * forma di stringa.
	 * @return
	 */
	public String showRequisito() {
		String req = "Risorse presenti sulla board necessarie ";
		
		for (int i = 0; i<risorsa.size(); i++) {
			req += risorsa.get(i);
		}
		
		return req;
	}
}
