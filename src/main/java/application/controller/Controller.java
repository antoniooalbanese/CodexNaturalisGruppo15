package application.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.JsonSyntaxException;

import application.model.CampoDiGioco;
import application.model.Carta;
import application.model.CartaIniziale;
import application.model.CartaObiettivo;
import application.model.CartaOro;
import application.model.CartaRisorsa;
import application.model.Giocatore;
import application.model.MazzoIniziale;
import application.model.MazzoObiettivo;
import application.model.MazzoOro;
import application.model.MazzoRisorsa;
import application.model.Model;
import application.model.Pedina;
import application.view.View;

/**
 * Classe che gestisce l'andamento del gioco e l'interazione tra model e view.
 */
public class Controller  {
	/**
	 * Attributo che rappresenta un'istanza del modello del gioco.
	 */
	private Model model;
	/**
	 * Attributo che rappresenta un'istanza dell'interfaccia del gioco.
	 */
	private View view;
	/**
	 * Numero dei giocatori della partita.
	 */
	private int num;
	
	/**
	 * Costruttore della classe.
	 * @param model
	 * @param view
	 */
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
		this.num = 0;
	}
	
	/**
	 * Metodo che fa partire il gioco.
	 */
	public void startGame(){
		
		if(!view.welcomeMessage()) {
			view.endMessage();
			System.exit(0);
		}
	}
	
	/**
	 * Metodo che ottiene il numero dei giocatori.
	 */
	public void getPlayersNumber() {
		num = view.getPlayersNumberMessage();
	}
	
	/**
	 * Metodo che inizializza i giocatori ad inizio gioco.
	 * @return
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 */
	public void initializePlayers() throws JsonSyntaxException, IOException {
		this.model = new Model();
		this.model.initCampo();
		ArrayList<Pedina> rimanenti = new ArrayList<Pedina>();
		rimanenti.add(Pedina.ROSSO);
		rimanenti.add(Pedina.BLU);
		rimanenti.add(Pedina.VERDE);
		rimanenti.add(Pedina.GIALLO);
		
		for (int i = 0; i < num; i++) {
			String nick = view.getNick(i + 1);
			Pedina pedina;
			while(true) {
				pedina = view.getPedina(i + 1, rimanenti);
				if(!rimanenti.contains(pedina)) {
					view.retry();
				}else {
					break;
				}
			}
			rimanenti.remove(pedina);
			String id = String.valueOf(i + 1);
			Giocatore gio = new Giocatore(id, nick, pedina);
			this.model.getCampo().addPlayer(gio);
		}
		
	}
	
	/**
	 * Metodo per estarre le carte del mazzo.
	 * @param mazzo
	 * @return
	 */
	public Carta estrai(ArrayList<? extends Carta> mazzo){
		Carta carta = mazzo.get(0);
		mazzo.remove(0);
		return carta;
	}
	
	/**
	 * Metodo utilizzato per ottenere una carta in una determinata posizione. 
	 * @param i
	 * @param mazzo
	 * @return
	 */
	public Carta getCarta(int i, ArrayList<Carta> mazzo){
		return mazzo.get(i);
	}
	
	/**
	 * Metodo utilizzato per inizializzare il campo da gioco.
	 * @throws IOException 
	 * @throws JsonSyntaxException 
	 */
	public void initializeField() throws JsonSyntaxException, IOException {
		MazzoRisorsa mazzoR = new MazzoRisorsa();
		ArrayList<CartaRisorsa> downR = new ArrayList <CartaRisorsa>();
		MazzoOro mazzoO = new MazzoOro();
		ArrayList<CartaOro> downO = new ArrayList <CartaOro>();
		
		mazzoR.load();
		Collections.shuffle(mazzoR.getMazzoFronte());
		downR.add((CartaRisorsa) this.estrai(mazzoR.getMazzoFronte()));
		downR.add((CartaRisorsa) this.estrai(mazzoR.getMazzoFronte()));
		
		mazzoO.load();
		Collections.shuffle(mazzoO.getMazzoFronte());
		downO.add((CartaOro) this.estrai(mazzoO.getMazzoFronte()));
		downO.add((CartaOro) this.estrai(mazzoO.getMazzoFronte()));
		
		this.model.getCampo().setMazzoR(mazzoR);
		this.model.getCampo().setRisorsa(downR);
		this.model.getCampo().setMazzoO(mazzoO);
		this.model.getCampo().setOro(downO);
	}

	/**
	 * Metodo che distribuisce le carte iniziali ai giocatori.
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public void giveStartCards() throws JsonSyntaxException, IOException {
		Collections.shuffle(this.model.getMazzoIniziale().getMazzoFronte());
		for(int i = 0; i < this.num; i++) {
			CartaIniziale carta = (CartaIniziale) this.estrai(this.model.getMazzoIniziale().getMazzoFronte());
			if(view.chooseStartCard(this.model.getCampo().getGiocatore().get(i).getNick(),carta , this.model.getMazzoIniziale().getRetroCarta(carta))) {
				this.model.getCampo().getGiocatore().get(i).initBoard(carta);
			}else {
				this.model.getCampo().getGiocatore().get(i).initBoard(this.model.getMazzoIniziale().getRetroCarta(carta));
			}
			this.model.getCampo().getGiocatore().get(i).getBoard().getMatrix()[5][5] = this.model.getCampo().getGiocatore().get(i).getId();
			this.pescaMazzo(this.model.getCampo().getGiocatore().get(i), this.model.getCampo().getMazzoR().getMazzoFronte());
			this.pescaMazzo(this.model.getCampo().getGiocatore().get(i), this.model.getCampo().getMazzoR().getMazzoFronte());
			this.pescaMazzo(this.model.getCampo().getGiocatore().get(i), this.model.getCampo().getMazzoO().getMazzoFronte());
			view.showHand(this.model.getCampo().getGiocatore().get(i).getNick(), this.model.getCampo().getGiocatore().get(i).getMano());
			while(!view.passaMano()) {
				
			}
		}	
	}
	
	/**
	 * Metodo che estrae sul banco le due carte obiettivo generali e 
	 * distribuisce le carte obiettivo segrete ai vari giocatori.
	 * @throws JsonSyntaxException
	 * @throws IOException
	 */
	public void giveObjectiveCards() throws JsonSyntaxException, IOException {
		MazzoObiettivo mazzoOb = new MazzoObiettivo();
		ArrayList<CartaObiettivo> downOb = new ArrayList<CartaObiettivo>();
		mazzoOb.load();
		Collections.shuffle(mazzoOb.getMazzoFronte());
		downOb.add((CartaObiettivo) this.estrai(mazzoOb.getMazzoFronte()));
		downOb.add((CartaObiettivo) this.estrai(mazzoOb.getMazzoFronte()));
		this.model.getCampo().setMazzoOb(mazzoOb);
		this.model.getCampo().setObiettivo(downOb);
		
		for(int i=0; i<num; i++) {
			CartaObiettivo carta1 = (CartaObiettivo) this.estrai(this.model.getCampo().getMazzoOb().getMazzoFronte());
			CartaObiettivo carta2 = (CartaObiettivo) this.estrai(this.model.getCampo().getMazzoOb().getMazzoFronte());
			if(view.chooseObjectiveCard(this.model.getCampo().getGiocatore().get(i).getNick(),carta1,carta2)) {
				this.model.getCampo().getGiocatore().get(i).getBoard().setObiettivo(carta1);
				this.model.getCampo().getMazzoOb().getMazzoFronte().add(carta2);
			}else {
				this.model.getCampo().getGiocatore().get(i).getBoard().setObiettivo(carta2);
				this.model.getCampo().getMazzoOb().getMazzoFronte().add(carta1);	
			}
		}
	}
	
	/**
	 * Metodo che sceglie l'ordine dei giocatori.
	 */
	public void chooseFirstPlayer() {
		Collections.shuffle(this.model.getCampo().getGiocatore());
		this.model.getCampo().getGiocatore().get(0).setInizio();
		view.showNewOrder(this.model.getCampo().getGiocatore());
	}
	
	/**
	 * Metodo che gestisce l'andamento della partita.
	 */
	public void playGame() {
		view.startMessage();
		
		while(!this.isGameOver()) {
			for(int i = 0; i < num; i++) {
				view.tellWhoseTurn(this.model.getCampo().getGiocatore().get(i).getNick());
				view.showAllBoards(this.model.getCampo().getGiocatore().get(i), this.model.getCampo().getGiocatore());
				view.showField(this.model.getCampo());
				this.posiziona(this.model.getCampo().getGiocatore().get(i));
				this.pesca(this.model.getCampo().getGiocatore().get(i));
				while(!view.passaMano()) {
					
				}
			}
		}
	}
	
	/**
	 * Metodo che controlla se si sono verificate le condizioni per terminare la
	 * partita, ossia che almeno un giocatore abbia raggiunto o superato 20 punti 
	 * con il solo posizionamento delle carte.
	 * @return
	 */
	public boolean isGameOver() {
		return true;
	}
	
	/**
	 * Metodo che gestisce il posizionamento delle carte sulla board.
	 * @param g
	 */
	public void posiziona(Giocatore g) {
		try {
			switch(view.chooseWhatToPlace()) {
			default:
				throw new IOException();
			}
		}catch(IOException e) {
			view.insertAValidCode();
		}

		/**view.chooseWhereToPlace()*/
	}
	
	/**
	 * Metodo che permette al giocatore in posizione n di pescare una carta.
	 * @param n
	 */
	public void pesca(Giocatore g) {
		
	}
	
	/**
	 * Metodo che permette al giocatore di pescare una carta da un determinato 
	 * mazzo.
	 * @param giocatore
	 * @param mazzo
	 */
	public void pescaMazzo(Giocatore giocatore, ArrayList<? extends Carta> mazzo) {
		if(mazzo.get(0) instanceof CartaRisorsa) {
			giocatore.getMano().getRisorsa().add((CartaRisorsa) mazzo.get(0));
		}else if(mazzo.get(0) instanceof CartaOro) {
			giocatore.getMano().getOro().add((CartaOro) mazzo.get(0));
		}
		mazzo.remove(0);
	}
	
	/**
	 * Metodo che permette al giocatore di pescare una carta tra quelle scoperte
	 * sul campo di gioco.
	 * @param giocatore
	 * @param down
	 * @return
	 */
	public Carta pescaDown(Giocatore giocatore, ArrayList<? extends Carta> down) {
		return null;
	}
		
}
