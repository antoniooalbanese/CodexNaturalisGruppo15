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
	 * Costruttore della classe quando l'obiettivo da raggiungere riguarda il 
	 * conteggio di un determinato tipo di risorse.
	 * @param tipo
	 * @param risorsa
	 */
	public Obiettivo(TipoObiettivo tipo, Regno risorsa) {
		this.tipo = tipo;
		this.risorsa = risorsa;
		this.oggetto = null;
		this.disposizione = null;
	}
	
	/**
	 * Costruttore della classe quando l'obiettivo da raggiungere riguarda il 
	 * conteggio di determianti tipi di oggetti.
	 * @param tipo
	 * @param oggetto
	 */
	public Obiettivo(TipoObiettivo tipo, ArrayList<Oggetto> oggetto) {
		this.tipo = tipo;
		this.risorsa = null;
		this.oggetto = oggetto;
		this.disposizione = null;
	}
	
	/**
	 * Costruttore della classe quando l'obiettivo da raggiungere riguarda il 
	 * conteggio di una determinata disposizione delle carte che deve formare o
	 * una diagonale, oppure una "elle".
	 * @param tipo
	 * @param diag
	 */
	public Obiettivo(TipoObiettivo tipo, Regno[][] disposizione) {
		this.tipo = tipo;
		this.risorsa = null;
		this.oggetto = null;
		this.disposizione = disposizione;
	}

	public TipoObiettivo getTipo() {
		return tipo;
	}

	public void setTipo(TipoObiettivo tipo) {
		this.tipo = tipo;
	}

	public Regno getRisorsa() {
		return risorsa;
	}

	public void setRisorsa(Regno risorsa) {
		this.risorsa = risorsa;
	}

	public ArrayList<Oggetto> getOggetto() {
		return oggetto;
	}

	public void setOggetto(ArrayList<Oggetto> oggetto) {
		this.oggetto = oggetto;
	}

	public Regno[][] getDisposizione() {
		return disposizione;
	}

	public void setDisposizione(Regno[][] disposizione) {
		this.disposizione = disposizione;
	}
	
	public String showObiettivo() {
		
		String obi = " punti per ";
		String ogg = "";
		String mat = "";
		
		/** DA RICONTROLLARE SOPRATUTTO RIEMPIMENTO MATRICE E SPAZIO DEDICATO ALLE CELLE PER IL RIEMPIMENTO
		 * 
		 */
		
		switch (getTipo()) {
		
		case RISORSA:
			obi += "3 risorse del tipo " + getRisorsa();
			break;
		case OGGETTO:
			for (int i =0; i <oggetto.size(); i++) {
				ogg += oggetto.get(i);
				if (i==oggetto.size()-1) {
					break;
				}
				ogg+= ", ";
			}
			obi += "ogni gruppo di oggetti del tipo " + ogg;
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
