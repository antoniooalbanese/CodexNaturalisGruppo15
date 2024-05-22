package application.model;

import java.util.ArrayList;

/**
 * Questa classe implementa gli obiettivi che i giocatori devono raggiungere.
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
	private Regno[][] disposizione = new Regno[3][3];

	/**
	 * Metodo che ritorna il tipo di obiettivo.
	 * @return
	 */
	public TipoObiettivo getTipo() {
		return tipo;
	}
	
	/**
	 * Metodo che modifica il tipo di obiettivo.
	 * @param tipo
	 */
	public void setTipo(TipoObiettivo tipo) {
		this.tipo = tipo;
	}
	
	/**
	 * Metodo che ritorna il tipo di risorsa richiesta per il raggiungimento 
	 * dell'obiettivo.
	 * @return
	 */
	public Regno getRisorsa() {
		return risorsa;
	}

	/**
	 * Metodo che modifica il tipo di risorsa richiesta per il raggiungimento 
	 * dell'obiettivo.
	 * @param risorsa
	 */
	public void setRisorsa(Regno risorsa) {
		this.risorsa = risorsa;
	}
	
	/**
	 * Metodo che ritorna i tipi di oggetti richiesti per il raggiungimento 
	 * dell'obiettivo. 
	 * @return
	 */
	public ArrayList<Oggetto> getOggetto() {
		return oggetto;
	}

	/**
	 * Metodo che modifica i tipi di oggetti richiesti per il raggiungimento 
	 * dell'obiettivo.
	 * @param oggetto
	 */
	public void setOggetto(ArrayList<Oggetto> oggetto) {
		this.oggetto = oggetto;
	}

	/**
	 * Metodo che ritorna la disposizione da soddisfare per il raggiungimento
	 * dell'obiettivo.
	 * @return
	 */
	public Regno[][] getDisposizione() {
		return disposizione;
	}
	
	/**
	 * Metodo che modifica la disposizione da soddisfare per il raggiungimento
	 * dell'obiettivo.
	 * @param disposizione
	 */
	public void setDisposizione(Regno[][] disposizione) {
		this.disposizione = disposizione;
	}
	
	/**
	 * Metodo che ritorna tutte le informazioni riguardanti un obiettivo sotto
	 * forma di stringa.
	 * @return
	 */
	public String showObiettivo() {
		
		String obi = " punti per ";
		String ogg = "";
		String mat = "";
		
		/** DA RICONTROLLARE SOPRATUTTO RIEMPIMENTO MATRICE E SPAZIO DEDICATO ALLE CELLE PER IL RIEMPIMENTO
		 * RIEMPIRE CON I COLORI AL POSTO DELLE RISORSE
		 * 
		 */
		
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
			for (int j=0; j<3; j++) {
				for (int k=0; k<3; k++) {
					if(disposizione [j][k]==null) {
						mat += "      ";
					}else {
						mat += disposizione [j][k];
					}
					
					if (k==2) {
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
