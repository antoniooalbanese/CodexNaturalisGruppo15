package application.model;

/** 
 * Questa è la classe che decrive un generico angolo appartenente ad una 
 * generica carta.
 */
public class Angolo {
	/**
	 * Attributo che descrive il tipo dell'angolo.
	 */
	private TipoAngolo tipo;
	/**
	 * Posizione dell'angolo all'interno della carta.
	 */
	private Posizione pos;
	/**
	 * Risorsa contenuta all'interno dell'angolo, nel caso in cui questo 
	 * contenga una risorsa.
	 */
	private Regno risorsa;
	/**
	 * Oggetto contenuto all'interno dell'angolo, nel caso in cui questo
	 * contenga un oggetto.
	 */
	private Oggetto oggetto;
	/**
	 * Codice della carta contenente l'angolo a cui l'angolo in questione è
	 * collegato.
	 */
	private String link;
	
	/**
	 * Questo è il costruttore della classe.
	 * @param id
	 * @param tipo
	 * @param pos
	 * @param risorsa
	 * @param oggetto
	 * @param link
	 */
	public Angolo(String id, TipoAngolo tipo, Posizione pos, Regno risorsa, 
				  Oggetto oggetto, String link) {
		this.tipo = tipo;
		this.pos = pos;
		this.risorsa = risorsa;
		this.oggetto = oggetto;
		this.link = link;
	}
	
	/**
	 * Metodo che ritorna il tipo dell'angolo.
	 * @return
	 */
	public TipoAngolo getTipo() {
		return tipo;
	}

	/**
	 * Metodo che modifica il tipo dell'angolo.
	 * @param tipo
	 */
	public void setTipo(TipoAngolo tipo) {
		this.tipo = tipo;
	}

	/**
	 * Metodo che ritorna la posizione dell'angolo.
	 * @return
	 */
	public Posizione getPos() {
		return pos;
	}

	/**
	 * Metodo che modifica la posizione dell'angolo.
	 * @param pos
	 */
	public void setPos(Posizione pos) {
		this.pos = pos;
	}

	/**
	 * Metodo che ritorna il tipo della risorsa presente nell'angolo.
	 * @return
	 */
	public Regno getRisorsa() {
		return risorsa;
	}

	/**
	 * Metodo che modifica il tipo della risorsa presente nell'angolo.
	 * @param risorsa
	 */
	public void setRisorsa(Regno risorsa) {
		this.risorsa = risorsa;
	}

	/**
	 * Metodo che ritorna il tipo di oggetto presente nell'angolo.
	 * @return
	 */
	public Oggetto getOggetto() {
		return oggetto;
	}

	/**
	 * Metodo che modifica il tipo di oggetto presente nell'angolo.
	 * @param oggetto
	 */
	public void setOggetto(Oggetto oggetto) {
		this.oggetto = oggetto;
	}

	/**
	 * Metodo che ritorna il codice della carta il cui angolo è collegato 
	 * all'angolo in questione.
	 * @return
	 */
	public String getLink() {
		return link;
	}

	/**
	 * Metodo che modifica il codice della carta il cui angolo è collegato 
	 * all'angolo in questione.
	 * @param link
	 */
	public void setLink(String link) {
		this.link = link;
	}

	/**
	 * Metodo che produce una stringa contenente tutte le informazioni relative ad 
	 * un angolo.
	 * @return
	 */
	public String showAngolo() {
		String riga = null;
		
		switch(getTipo()) {
		case VUOTO: 
			riga = "tipo: " + getTipo() + ", posizione: " + getPos();
			break;
		case NASCOSTO:
			riga = "tipo: " + getTipo() + ", posizione: " + getPos();
			break;
		case RISORSA:
			riga = "tipo: " + getTipo() + ", posizione: " + getPos() + ", risorsa: " + getRisorsa();
			break;
		case OGGETTO:
			riga = "tipo: " + getTipo() + ", posizione: " + getPos() + ", oggetto: " + getOggetto();
			break;
		}
		
		return riga;
	}
}
