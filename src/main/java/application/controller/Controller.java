package application.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.JsonSyntaxException;

import application.model.Angolo;
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
import application.model.Posizione;
import application.model.TipoAngolo;
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
			CartaIniziale scelta;
			if(view.chooseStartCard(this.model.getCampo().getGiocatore().get(i).getNick(),carta , this.model.getMazzoIniziale().getRetroCarta(carta))) {
				this.model.getCampo().getGiocatore().get(i).initBoard(carta);
				scelta = carta;
			}else {
				this.model.getCampo().getGiocatore().get(i).initBoard(this.model.getMazzoIniziale().getRetroCarta(carta));
				scelta = this.model.getMazzoIniziale().getRetroCarta(carta);
			}
			this.model.getCampo().getGiocatore().get(i).getBoard().getMatrix()[5][5] = scelta.getId();
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
		boolean last = false;
		
		while(!this.isGameOver(last)) {
			
			for(int i = 0; i < num; i++) {
				if(view.showDecksAreOverMessage(areDecksFinished())) {
					break;
				}
				view.tellLastTurn(last); 
				view.tellWhoseTurn(this.model.getCampo().getGiocatore().get(i).getNick());
				view.showAllBoards(this.model.getCampo().getGiocatore().get(i), this.model.getCampo().getGiocatore());
				view.showField(this.model.getCampo());
				this.posiziona(this.model.getCampo().getGiocatore().get(i));
				this.pesca(this.model.getCampo().getGiocatore().get(i));
				while(!view.passaMano()) {
					
				}
				last = this.checkLastTurn(this.model.getCampo().getGiocatore().get(i));
			}
		}
	}
	
	/**
	 * Metodo che controlla se il giocatore a fine del proprio turno ha raggiunto
	 * almeno i 20 punti per far terminare la partita.
	 * @param g
	 * @return
	 */
	public boolean checkLastTurn(Giocatore g) {
		if(g.getBoard().getPunteggio() >= 20) {
			return true;
		}
		return false;
	}
	
	/**
	 * Metodo che controlla se si sono verificate le condizioni per terminare la
	 * partita, ossia che almeno un giocatore abbia raggiunto o superato 20 punti 
	 * con il solo posizionamento delle carte.
	 * @param last
	 * @return
	 */
	public boolean isGameOver(boolean last) {
		if(!last && !this.areDecksFinished()) {
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo che controlla se i due mazzi(oro e risorsa) sono terminati.
	 * @return
	 */
	public boolean areDecksFinished() {
		if(this.model.getCampo().getMazzoR().getMazzoFronte().isEmpty() && this.model.getCampo().getMazzoO().getMazzoFronte().isEmpty()) {
			return true;
		}
		return false;
	}
	/**
	 * Metodo che gestisce il posizionamento delle carte sulla board.
	 * @param g
	 */
	public void posiziona(Giocatore g) {
		String scelta = "";
		ArrayList<CartaRisorsa> libereRisorsa = new ArrayList<CartaRisorsa>();
		ArrayList<CartaOro> libereOro = new ArrayList<CartaOro>();
		boolean check = false; 
		boolean req = false;
		String riga = "";
		CartaRisorsa cardR = null;
		CartaOro cardO = null;
		
		libereRisorsa.addAll(this.getFreeResourceCards(g.getBoard().getRisorsa()));
		libereOro.addAll(this.getFreeGoldCards(g.getBoard().getOro()));
		
		view.showFreeCornersMessage();
		
		for(int z = 0; z < libereRisorsa.size(); z++) {
			view.showFreeResourceCorners(libereRisorsa.get(z), this.getFreeResourceCorners(libereRisorsa.get(z)));
		}
		
		for(int k = 0; k < libereRisorsa.size(); k++) {
			view.showFreeGoldCorners(libereOro.get(k), this.getFreeGoldCorners(libereOro.get(k)));
		}
		
		while(check != true) {
			scelta = view.chooseWhatToPlace();
			try {
				for(int i = 0; i < g.getMano().getRisorsa().size(); i++) {
					if(scelta == g.getMano().getRisorsa().get(i).getId()) {
						check = true;
						break;
					}
				}
				
				for(int j = 0; j < g.getMano().getOro().size() - 1; j++) {
					if(scelta == g.getMano().getOro().get(j).getId()) {
						check = true;
						break;
					}
				}
				if(check == false) {
					throw new IOException();
				}
			}catch(IOException e) {
				view.insertAValidCode();
			}
		}
		
		check = false;
		
		while(check != true && req != true) {
			riga = view.chooseWhatToCover();
		
			try {
				for(CartaRisorsa r: libereRisorsa) {
					if(riga.equals(r.getId())) {
						cardR = r;
						check = true;
					}
				}
				
				for(CartaOro o: libereOro) {
					if(riga.equals(o.getId())) {
						if(this.checkRequirements(g, o)) {
							cardO = o;
							check = true;
							req = true;
						} else {
							check = true;
							req = false;
						}
					}
				}
				
				if(check == false) {
					throw new IOException();
				}
				
				if(req == false) {
					view.showRequirementMessage();
				}
			}catch(IOException e) {
				view.insertAValidCode();
			}
		}
		
		view.showFreeCornersMessage();
		
		check = false;
		
		while(!check) {
			if(cardR != null) {
				view.showFreeResourceCorners(cardR, cardR.getAngoli());
				if(this.checkPlaceResource(g, scelta, cardR, view.chooseWhichCorner())) {
					check = true;
				}
			}else if(cardO != null) {
				view.showFreeGoldCorners(cardO, cardO.getAngoli());
				if(this.checkPlaceGold(g, scelta, cardO, view.chooseWhichCorner())) {
					check = true;
				}
			}
		}
		
		
		
		
	}
	
	/**
	 * Metodo che controlla se la carta oro che il giocatore vuole posizionare 
	 * rispetta i requisiti di posizionamento.
	 * @param g
	 * @param oro
	 * @return
	 */
	public boolean checkRequirements(Giocatore g, CartaOro oro) {
		int veg = 0;
		int ani = 0;
		int fun = 0;
		int ins = 0;
		
		for(int i = 0; i < oro.getRequisito().getRisorsa().size(); i++) {
			switch(oro.getRequisito().getRisorsa().get(i)) {
			case VEGETALE:
				veg++;
			case ANIMALE:
				ani++;
			case FUNGHI:
				fun++;
			case INSETTI:
				ins++;
			}
		}
		
		if(veg <= g.getBoard().getNumRis().get(0)) {
			if(ani <= g.getBoard().getNumRis().get(1)) {
				if(fun <= g.getBoard().getNumRis().get(2)) {
					if(ins <= g.getBoard().getNumRis().get(3)) {
						return true;
					}
				}
			}
		}
		
		return false;
	}
	
	public boolean checkFreeMatrix(Giocatore g, Carta carta, Posizione angolo) {
		for(int i = 0; i < g.getBoard().getMatrix().length; i++) {
			for(int j = 0; j < g.getBoard().getMatrix()[i].length; j++) {
				if(g.getBoard().getMatrix()[i][j].equals(carta.getId())) {
					switch (angolo) {
					case ADX:
						if(g.getBoard().getMatrix()[i-1][j+1] == null) {
							return true;
						}
						break;
					case BDX:
						if(g.getBoard().getMatrix()[i+1][j+1] == null) {
							return true;
						}
						break;
					case BSX:
						if(g.getBoard().getMatrix()[i+1][j-1] == null) {
							return true;
						}
						break;
					case ASX:
						if(g.getBoard().getMatrix()[i-1][j-1] == null) {
							return true;
						}
						break;
					
					}
				}
			}
		}
		return false;
	}
	/**
	 * Metodo che controlla che la carta possa essere realmente posizionata su una carta risorsa
	 * come vuole il giocatore.
	 * @param scelta
	 * @param coperta
	 * @param angolo
	 * @return
	 */
	public boolean checkPlaceResource(Giocatore g, String scelta, CartaRisorsa coperta, Posizione angolo) {
		Carta card = g.getMano().getResourceById(scelta);
		if(this.checkFreeMatrix(g, coperta, angolo)) {
			view.isFullMessage();
			return false;
		}
		switch(angolo) {
		case ADX:
			if(!checkPlaceCondition(((CartaRisorsa) card).getAngoloByPosizione(Posizione.BSX),coperta.getAngoloByPosizione(angolo))) {
				view.showPlaceErrorMessage();
				return false;
			}
			if(checkPlace(card, coperta, g, angolo)) {
				view.showPlaceErrorMessage();
			}
			this.placeCard(g, card, coperta, Posizione.BSX, angolo);
		case BDX:
			if(!checkPlaceCondition(((CartaRisorsa) card).getAngoloByPosizione(Posizione.ASX),coperta.getAngoloByPosizione(angolo))) {
				view.showPlaceErrorMessage();
				return false;
			}
			if(checkPlace(card, coperta, g, angolo)) {
				view.showPlaceErrorMessage();
			}
			this.placeCard(g, card, coperta, Posizione.ASX, angolo);
		case BSX:
			if(!checkPlaceCondition(((CartaRisorsa) card).getAngoloByPosizione(Posizione.ADX),coperta.getAngoloByPosizione(angolo))) {
				view.showPlaceErrorMessage();
				return false;
			}
			if(checkPlace(card, coperta, g, angolo)) {
				view.showPlaceErrorMessage();
			}
			this.placeCard(g, card, coperta, Posizione.ADX, angolo);
		case ASX:
			if(!checkPlaceCondition(((CartaRisorsa) card).getAngoloByPosizione(Posizione.BDX),coperta.getAngoloByPosizione(angolo))) {
				view.showPlaceErrorMessage();
				return false;
			}
			if(checkPlace(card, coperta, g, angolo)) {
				view.showPlaceErrorMessage();
			}
			this.placeCard(g, card, coperta, Posizione.BDX, angolo);
		}
		
		return true;
	}
	
	public boolean checkPlaceCondition(Angolo ang, Angolo coperto) {
		if(ang.getTipo().equals(TipoAngolo.NASCOSTO)) {
			if(coperto.getTipo().equals(TipoAngolo.VUOTO)) {
				return false;
			}
		}
		return true;
	}
	/**
	 * Metodo che controlla che la carta possa essere realmente posizionata su una carta oro
	 * come vuole il giocatore.
	 * @param scelta
	 * @param coperta
	 * @param angolo
	 * @return
	 */
	public boolean checkPlaceGold(Giocatore g, String scelta, CartaOro coperta, Posizione angolo) {
		Carta card = g.getMano().getGoldById(scelta);
		if(this.checkFreeMatrix(g, coperta, angolo)) {
			view.isFullMessage();
			return false;
		}
		switch(angolo) {
		case ADX:
			if(!checkPlaceCondition(((CartaOro) card).getAngoloByPosizione(Posizione.BSX),coperta.getAngoloByPosizione(angolo))) {
				view.showPlaceErrorMessage();
				return false;
			}
			if(checkPlace(card, coperta, g, angolo)) {
				view.showPlaceErrorMessage();
			}
			this.placeCard(g, card, coperta, Posizione.BSX, angolo);
		case BDX:
			if(!checkPlaceCondition(((CartaOro) card).getAngoloByPosizione(Posizione.ASX),coperta.getAngoloByPosizione(angolo))) {
				view.showPlaceErrorMessage();
				return false;
			}
			if(checkPlace(card, coperta, g, angolo)) {
				view.showPlaceErrorMessage();
			}
			this.placeCard(g, card, coperta, Posizione.ASX, angolo);
		case BSX:
			if(!checkPlaceCondition(((CartaOro) card).getAngoloByPosizione(Posizione.ADX),coperta.getAngoloByPosizione(angolo))) {
				view.showPlaceErrorMessage();
				return false;
			}
			if(checkPlace(card, coperta, g, angolo)) {
				view.showPlaceErrorMessage();
			}
			this.placeCard(g, card, coperta, Posizione.ADX, angolo);
		case ASX:
			if(!checkPlaceCondition(((CartaOro) card).getAngoloByPosizione(Posizione.BDX),coperta.getAngoloByPosizione(angolo))) {
				view.showPlaceErrorMessage();
				return false;
			}
			if(checkPlace(card, coperta, g, angolo)) {
				view.showPlaceErrorMessage();
			}
			this.placeCard(g, card, coperta, Posizione.BDX, angolo);
		}
		
		return true;
	}
	
	
	public boolean checkPlace (Carta carta, Carta coperta, Giocatore g, Posizione angolo) {
		
		for(int i = 0; i < g.getBoard().getMatrix().length; i++) {
			for(int j = 0; j < g.getBoard().getMatrix()[i].length; j++) {
				if(g.getBoard().getMatrix()[i][j].equals(coperta.getId())) {
					if(carta.getId().charAt(0)=='R') {
						if(coperta.getId().charAt(0)=='R') {
							switch (angolo) {
							case ADX:
								if(g.getBoard().getMatrix()[i-2][j] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ASX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j])).getAngoloByPosizione(Posizione.BDX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i-2][j+2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ADX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j+2])).getAngoloByPosizione(Posizione.BSX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i][j+2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BDX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i][j+2])).getAngoloByPosizione(Posizione.ASX))) {
									return false;
								}
								break;
							case BDX:
								if(g.getBoard().getMatrix()[i][j+2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ADX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i][j+2])).getAngoloByPosizione(Posizione.BSX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i+2][j+2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BDX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j+2])).getAngoloByPosizione(Posizione.ASX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i+2][j] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BSX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j])).getAngoloByPosizione(Posizione.ADX))) {
									return false;
								}
								break;
							case BSX:
								if(g.getBoard().getMatrix()[i+2][j] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BDX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j])).getAngoloByPosizione(Posizione.ASX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i+2][j-2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BSX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j-2])).getAngoloByPosizione(Posizione.ADX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i][j-2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ASX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i][j-2])).getAngoloByPosizione(Posizione.BDX))) {
									return false;
								}
								break;
							case ASX:
								if(g.getBoard().getMatrix()[i][j-2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BSX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i][j-2])).getAngoloByPosizione(Posizione.ADX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i-2][j-2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ASX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j-2])).getAngoloByPosizione(Posizione.BDX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i-2][j] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ADX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j])).getAngoloByPosizione(Posizione.BSX))) {
									return false;
								}
								break;
							
							}
							
						} else {
							switch (angolo) {
							case ADX:
								if(g.getBoard().getMatrix()[i-2][j] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ASX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j])).getAngoloByPosizione(Posizione.BDX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i-2][j+2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ADX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j+2])).getAngoloByPosizione(Posizione.BSX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i][j+2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BDX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i][j+2])).getAngoloByPosizione(Posizione.ASX))) {
									return false;
								}
								break;
							case BDX:
								if(g.getBoard().getMatrix()[i][j+2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ADX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i][j+2])).getAngoloByPosizione(Posizione.BSX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i+2][j+2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BDX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j+2])).getAngoloByPosizione(Posizione.ASX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i+2][j] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BSX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j])).getAngoloByPosizione(Posizione.ADX))) {
									return false;
								}
								break;
							case BSX:
								if(g.getBoard().getMatrix()[i+2][j] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BDX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j])).getAngoloByPosizione(Posizione.ASX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i+2][j-2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BSX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j-2])).getAngoloByPosizione(Posizione.ADX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i][j-2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ASX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i][j-2])).getAngoloByPosizione(Posizione.BDX))) {
									return false;
								}
								break;
							case ASX:
								if(g.getBoard().getMatrix()[i][j-2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BSX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i][j-2])).getAngoloByPosizione(Posizione.ADX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i-2][j-2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ASX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j-2])).getAngoloByPosizione(Posizione.BDX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i-2][j] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ADX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j])).getAngoloByPosizione(Posizione.BSX))) {
									return false;
								}
								break;
							
							}
							
						} 
						
					} else if(coperta.getId().charAt(0)=='O') {
						switch (angolo) {
						case ADX:
							if(g.getBoard().getMatrix()[i-2][j] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ASX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j])).getAngoloByPosizione(Posizione.BDX))) {
								return false;
							}
							if(g.getBoard().getMatrix()[i-2][j+2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ADX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j+2])).getAngoloByPosizione(Posizione.BSX))) {
								return false;
							}
							if(g.getBoard().getMatrix()[i][j+2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BDX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i][j+2])).getAngoloByPosizione(Posizione.ASX))) {
								return false;
							}
							break;
						case BDX:
							if(g.getBoard().getMatrix()[i][j+2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ADX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i][j+2])).getAngoloByPosizione(Posizione.BSX))) {
								return false;
							}
							if(g.getBoard().getMatrix()[i+2][j+2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BDX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j+2])).getAngoloByPosizione(Posizione.ASX))) {
								return false;
							}
							if(g.getBoard().getMatrix()[i+2][j] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BSX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j])).getAngoloByPosizione(Posizione.ADX))) {
								return false;
							}
							break;
						case BSX:
							if(g.getBoard().getMatrix()[i+2][j] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BDX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j])).getAngoloByPosizione(Posizione.ASX))) {
								return false;
							}
							if(g.getBoard().getMatrix()[i+2][j-2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BSX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j-2])).getAngoloByPosizione(Posizione.ADX))) {
								return false;
							}
							if(g.getBoard().getMatrix()[i][j-2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ASX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i][j-2])).getAngoloByPosizione(Posizione.BDX))) {
								return false;
							}
							break;
						case ASX:
							if(g.getBoard().getMatrix()[i][j-2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BSX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i][j-2])).getAngoloByPosizione(Posizione.ADX))) {
								return false;
							}
							if(g.getBoard().getMatrix()[i-2][j-2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ASX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j-2])).getAngoloByPosizione(Posizione.BDX))) {
								return false;
							}
							if(g.getBoard().getMatrix()[i-2][j] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ADX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j])).getAngoloByPosizione(Posizione.BSX))) {
								return false;
							}
							break;
						
						}
						
					} else {
						switch (angolo) {
						case ADX:
							if(g.getBoard().getMatrix()[i-2][j] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ASX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j])).getAngoloByPosizione(Posizione.BDX))) {
								return false;
							}
							if(g.getBoard().getMatrix()[i-2][j+2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ADX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j+2])).getAngoloByPosizione(Posizione.BSX))) {
								return false;
							}
							if(g.getBoard().getMatrix()[i][j+2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BDX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i][j+2])).getAngoloByPosizione(Posizione.ASX))) {
								return false;
							}
							break;
						case BDX:
							if(g.getBoard().getMatrix()[i][j+2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ADX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i][j+2])).getAngoloByPosizione(Posizione.BSX))) {
								return false;
							}
							if(g.getBoard().getMatrix()[i+2][j+2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BDX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j+2])).getAngoloByPosizione(Posizione.ASX))) {
								return false;
							}
							if(g.getBoard().getMatrix()[i+2][j] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BSX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j])).getAngoloByPosizione(Posizione.ADX))) {
								return false;
							}
							break;
						case BSX:
							if(g.getBoard().getMatrix()[i+2][j] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BDX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j])).getAngoloByPosizione(Posizione.ASX))) {
								return false;
							}
							if(g.getBoard().getMatrix()[i+2][j-2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BSX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j-2])).getAngoloByPosizione(Posizione.ADX))) {
								return false;
							}
							if(g.getBoard().getMatrix()[i][j-2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ASX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i][j-2])).getAngoloByPosizione(Posizione.BDX))) {
								return false;
							}
							break;
						case ASX:
							if(g.getBoard().getMatrix()[i][j-2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BSX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i][j-2])).getAngoloByPosizione(Posizione.ADX))) {
								return false;
							}
							if(g.getBoard().getMatrix()[i-2][j-2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ASX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j-2])).getAngoloByPosizione(Posizione.BDX))) {
								return false;
							}
							if(g.getBoard().getMatrix()[i-2][j] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ADX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j])).getAngoloByPosizione(Posizione.BSX))) {
								return false;
							}
							break;
						
						}
						
					}
					
				}
			}
		}
		return false;
	}
		
	
	/**
	 * Metodo che aggiunge alla lista di carte della board la carta piazzata e 
	 * aggiorna la relativa matrice e i contatori.
	 * @param card
	 * @param coperta
	 * @param angoloPos
	 * @param angoloCop
	 */
	public void placeCard(Giocatore g, Carta card, Carta coperta, Posizione angoloPos, Posizione angoloCop) {
		if(card.getId().charAt(0)=='R') {
			if(coperta.getId().charAt(0)=='R') {
				((CartaRisorsa)card).getAngoloByPosizione(angoloPos).setLink(coperta.getId());
				((CartaRisorsa)coperta).getAngoloByPosizione(angoloCop).setLink(card.getId());
			} else {
				((CartaRisorsa)card).getAngoloByPosizione(angoloPos).setLink(coperta.getId());
				((CartaOro)coperta).getAngoloByPosizione(angoloCop).setLink(card.getId());
			}
		} else if (coperta.getId().charAt(0)=='O') {
			((CartaOro)card).getAngoloByPosizione(angoloPos).setLink(coperta.getId());
			((CartaOro)coperta).getAngoloByPosizione(angoloCop).setLink(card.getId());
		} else {
			((CartaOro)card).getAngoloByPosizione(angoloPos).setLink(coperta.getId());
			((CartaRisorsa)coperta).getAngoloByPosizione(angoloCop).setLink(card.getId());
		}
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
	
	/**
	 * Metodo che ritorna le carte risorsa posizionate che hanno angoli liberi che 
	 * possono essere coperti da angoli di altre carte.
	 * @param card
	 * @return
	 */
	public ArrayList<CartaRisorsa> getFreeResourceCards(ArrayList<CartaRisorsa> card){
		ArrayList<CartaRisorsa> free = new ArrayList<CartaRisorsa>();
		boolean empty = false;
		
		for(int i = 0; i < card.size(); i++) {
			for(int j = 0; j < 4; j++) {
				if(this.getFreeResourceCorners(free.get(i)) == null) {
					empty = true;
				}
			}
			if(empty == true) {
				free.add(card.get(i));
				empty = false;
			}
		}
		
		return free;
	}
	
	/**
	 * Metodo che ritorna le carte oro posizionate che hanno angoli liberi che 
	 * possono essere coperti da angoli di altre carte.
	 * @param card
	 * @return
	 */
	public ArrayList<CartaOro> getFreeGoldCards(ArrayList<CartaOro> card){
		ArrayList<CartaOro> free = new ArrayList<CartaOro>();
		boolean empty = false;
		
		for(int i = 0; i < card.size(); i++) {
			for(int j = 0; j < 4; j++) {
				if(this.getFreeGoldCorners(free.get(i)) == null) {
					empty = true;
				}
			}
			if(empty == true) {
				free.add(card.get(i));
				empty = false;
			}
		}
		
		return free;
	}
	
	/**
	 * Metodo che ritorna tutti gli angoli liberi di una carta risorsa.
	 * @param card
	 * @return
	 */
	public ArrayList<Angolo> getFreeResourceCorners(CartaRisorsa card){
		ArrayList<Angolo> corner = new ArrayList<Angolo>();
		
		for (Angolo c : card.getAngoli()) {
			if(c.getLink() == null && c.getTipo() != TipoAngolo.NASCOSTO) {
				corner.add(c);
			}
		}
		
		return corner;
	}
	
	/**
	 * Metodo che ritorna tutti gli angoli liberi di una carta oro.
	 * @param card
	 * @return
	 */
	public ArrayList<Angolo> getFreeGoldCorners(CartaOro card){
		ArrayList<Angolo> corner = new ArrayList<Angolo>();
		
		for (Angolo c : card.getAngoli()) {
			if(c.getLink() == null && c.getTipo() != TipoAngolo.NASCOSTO) {
				corner.add(c);
			}
		}
		
		return corner;
	}
}
