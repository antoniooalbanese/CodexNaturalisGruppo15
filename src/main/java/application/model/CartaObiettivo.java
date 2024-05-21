package application.model;

/**
 * Questa classe implementa le carte di tipo obiettivo.
 */
public class CartaObiettivo extends Carta{
	/**
	 * Questo attributo descrive l'obiettivo che deve essere soddisfatto
	 * per attribuire al giocatore i punti della carta.
	 */
	private Obiettivo obiettivo;
	/**
	 * Attributo che descrive il valore dei punti asseganti al raggiungimento
	 * dell'obiettivo.
	 */
	private int punto;
	
	/**
	 * Questo è il costruttore della classe.
	 * @param id
	 * @param fronte
	 * @param obiettivo
	 * @param punto
	 */
	public CartaObiettivo(String id, Boolean fronte, Obiettivo obiettivo,
						  int punto) {
		super(id);
		this.obiettivo = obiettivo;
		this.punto = punto;
	}

	/**QUESTA CLASSE E' DA RIGUARDARE.
	 * TUTTO DIPENDE DA COME SI STRUTTURA LA CLASSE OBIETTIVO.
	 * HO IL DUBBIO CHE SI DEBBANO FARE 3 CLASSI
	 * DIVERSE PER I TRE TIPI DI OBIETTIVO DIVERSI. INOLTRE BISOGNA CAPIRE COME
	 * IDENTIFICARE LE STRUTTURE DA RISPETTARE PER L'OBIETTIVO DI TIPO 
	 * DISPOSIZIONE.
	 * INOLTRE BISOGNA FARE SIA UN COSTRUTTORE PER IL FRONTE CHE PER IL RETRO.
	 * IL RETRO AVRA' IL SOLO SCOPO DI MOSTRARE LA CARTA COPERTA.
	 */
	
	public String showCard() {
		return "CartaOro{" + "id = " + getId() + ", fronte = " + getFronte() + ", obiettivo = " +obiettivo+ ", punti = " + punto + "}";
	}
}
