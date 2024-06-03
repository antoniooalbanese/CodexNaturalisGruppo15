package application.model;

import java.util.ArrayList;

/**
 * Questa classe implementa gli obiettivi che i giocatori 
 * devono raggiungere.
 */
public class Obiettivo {
	/**
	 * Attributo che indica il tipo di obiettivo.
	 */
	private TipoObiettivo tipo;
	
	/**
	 * Risorse richieste per raggiungere l'obiettivo. 
	 */
	private Regno risorsa;
	
	/**
	 * Oggetti richiesti per raggiungere l'obiettivo.
	 */
	private ArrayList<Oggetto> oggetto = new ArrayList<Oggetto>();
	
	/**
	 * Matrice contenente la disposizione che deve essere soddisfatta 
	 * per raggiungere l'obiettivo.
	 */
	private Regno[][] disposizione = new Regno[4][4];

	/**
	 * Metodo che ritorna il tipo di obiettivo.
	 * @return tipo dell'obiettivo
	 */
	public TipoObiettivo getTipo() {
		return tipo;
	}
	
	/**
	 * Metodo che modifica il tipo di obiettivo.
	 * @param tipo: tipo dell'obiettivo
	 */
	public void setTipo(TipoObiettivo tipo) {
		this.tipo = tipo;
	}
	
	/**
	 * Metodo che ritorna il regno a cui appartiene la risorsa 
	 * richiesta per il raggiungimento dell'obiettivo.
	 * @return regno a cui appartiene la risorsa richiesta 
	 * per raggiungere l'obiettivo
	 */
	public Regno getRisorsa() {
		return risorsa;
	}

	/**
	 * Metodo che modifica il regno a cui appartiene la risorsa 
	 * richiesta per il raggiungimento dell'obiettivo.
	 * @param risorsa: il regno a cui appartiene la risorsa 
	 * richiesta per il raggiungimento dell'obiettivo 
	 */
	public void setRisorsa(Regno risorsa) {
		this.risorsa = risorsa;
	}
	
	/**
	 * Metodo che ritorna i tipi di oggetti richiesti per il 
	 * raggiungimento dell'obiettivo. 
	 * @return tipi di oggetti richiesti per il raggiungimento 
	 * dell'obiettivo
	 */
	public ArrayList<Oggetto> getOggetto() {
		return oggetto;
	}

	/**
	 * Metodo che modifica i tipi di oggetti richiesti per il 
	 * raggiungimento dell'obiettivo.
	 * @param oggetto: tipi di oggetti richiesti per il raggiungimento 
	 * dell'obiettivo
	 */
	public void setOggetto(ArrayList<Oggetto> oggetto) {
		this.oggetto = oggetto;
	}

	/**
	 * Metodo che ritorna la disposizione da soddisfare per il 
	 * raggiungimento dell'obiettivo.
	 * @return disposizione da soddisfare per il raggiungimento 
	 * dell'obiettivo
	 */
	public Regno[][] getDisposizione() {
		return disposizione;
	}
	
	/**
	 * Metodo che modifica la disposizione da soddisfare per il 
	 * raggiungimento dell'obiettivo.
	 * @param disposizione: disposizione da soddisfare per il 
	 * raggiungimento dell'obiettivo
	 */
	public void setDisposizione(Regno[][] disposizione) {
		this.disposizione = disposizione;
	}
	
	/**
	 * Metodo che ritorna tutte le informazioni riguardanti un 
	 * obiettivo sotto forma di stringa.
	 * @return stringa contenente tutti i dettagli riguardanti 
	 * l'obiettivo
	 */
	public String showObiettivo() {
		String obi = " punti per ";
		String ogg = "";
		String mat = "";
		
		switch (getTipo()) {
		case RISORSA:
			obi += "3 risorse del tipo: " + getRisorsa();
			break;
		case OGGETTO:
			
			for (int i =0; i <oggetto.size(); i++) {
				ogg += oggetto.get(i);
				if (i==oggetto.size()-1) {
					break;
				}
				ogg+= ", ";
			}
			
			obi += "ogni gruppo di oggetti del tipo: " + ogg;
			break;
		case DISPOSIZIONE:
			
			for (int j=0; j<4; j++) {
				for (int k=0; k<4; k++) {
					if(disposizione [j][k]==null) {
						mat += "      ";
					}else {
						mat += disposizione [j][k];
					}
					
					if (k==3) {
						mat += "\n";
					}
				}
			}
			
			obi += "ogni disposizione del tipo:\n   " + mat;
			break;
		}
		
		return obi;
	}
}
