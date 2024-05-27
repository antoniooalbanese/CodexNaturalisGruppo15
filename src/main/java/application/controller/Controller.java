package application.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gson.JsonSyntaxException;

import application.model.Angolo;
import application.model.Board;
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
import application.model.Obiettivo;
import application.model.Oggetto;
import application.model.Pedina;
import application.model.Posizione;
import application.model.Regno;
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
			this.count(this.model.getCampo().getGiocatore().get(i).getBoard(),scelta, null, null);
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
				view.showBoard(this.model.getCampo().getGiocatore().get(i));
				this.pesca(this.model.getCampo().getGiocatore().get(i));
				while(!view.passaMano()) {
					
				}
				last = this.checkLastTurn(this.model.getCampo().getGiocatore().get(i));
			}
		}
		
		this.checkExtraPoint();
		view.isGameOverMessage(this.createRanking(this.model.getCampo().getGiocatore()));
	}
	
	public void checkExtraPoint() {
		for(int i = 0; i < num; i++) {
			 this.checkObjective(this.model.getCampo().getGiocatore().get(i).getBoard(), this.model.getCampo().getGiocatore().get(i).getBoard().getObiettivo());
		}
	}
	
	public ArrayList<String> createRanking(ArrayList<Giocatore> giocatori){
		ArrayList<String> ranking = new ArrayList<String>();
		String g;
		for (int k=0; k < giocatori.size(); k++) {
			ranking.add(giocatori.get(k).getNick());
		}
		for(int i = 0; i < giocatori.size() - 1; i++) {
			for(int j = 0; j < giocatori.size() - i - 1; j++) {
				if(giocatori.get(j).getBoard().getPunteggio() < giocatori.get(j + 1).getBoard().getPunteggio()) {
					g = ranking.get(j);
					ranking.set(j, ranking.get(j + 1));		
					ranking.set(j + 1, g);
				}
			}
		}
		
		return ranking;
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
		String sceltaFronte = "";
		Carta cartaScelta;
		ArrayList<CartaRisorsa> libereRisorsa = new ArrayList<CartaRisorsa>();
		ArrayList<CartaOro> libereOro = new ArrayList<CartaOro>();
		CartaIniziale liberaIniziale;
		boolean check = false;
		boolean checkAngoli = false;
		boolean req = false;
		String riga = "";
		CartaIniziale cardI = null;
		CartaRisorsa cardR = null;
		CartaOro cardO = null;

		liberaIniziale = getFreeInitialCard(g.getBoard().getCentro());
		if(this.getFreeResourceCards(g.getBoard().getRisorsa()) != null) {
			libereRisorsa.addAll(this.getFreeResourceCards(g.getBoard().getRisorsa()));
		} else {
			libereRisorsa = null;
		}
		if(this.getFreeGoldCards(g.getBoard().getOro()) != null) {
			libereOro.addAll(this.getFreeGoldCards(g.getBoard().getOro()));
		} else {
			libereOro = null;
		}
		
		view.showFreeCornersMessage();
		
		view.showFreeInitialCorners(liberaIniziale, this.getFreeInitialCorners(liberaIniziale));
		
		if (libereRisorsa != null) {
			for(int z = 0; z < libereRisorsa.size(); z++) {
				view.showFreeResourceCorners(libereRisorsa.get(z), this.getFreeResourceCorners(libereRisorsa.get(z)));
			}
		}
		
		if (libereOro != null) {
			for(int k = 0; k < libereOro.size(); k++) {
				view.showFreeGoldCorners(libereOro.get(k), this.getFreeGoldCorners(libereOro.get(k)));
			}
		}
		
		while(check != true && req != true) {
			scelta = view.chooseWhatToPlace().toUpperCase();
			try {
				for(int i = 0; i < g.getMano().getRisorsa().size(); i++) {
					if(scelta.equalsIgnoreCase(g.getMano().getRisorsa().get(i).getId())) {
						check = true;
						req = true;
						break;
					}
				}
				
				for(int j = 0; j < g.getMano().getOro().size(); j++) {
					if(scelta.equalsIgnoreCase(g.getMano().getOro().get(j).getId())) {
						check = true;
						req = true;
						break;
					}
				}
				int i = 0;
				while(!check) {
					if(scelta.equalsIgnoreCase(g.getMano().getOro().get(i).getId())) {
						if(this.checkRequirements(g, g.getMano().getOro().get(i))) {
							check = true;
							req = true;
							break;
						} else {
							check = false;
							req = false;
							break;
						}
					}
					i++;
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
		
		check = false;
		sceltaFronte = scelta;
		
		while(check != true && checkAngoli != true) {
			scelta = sceltaFronte;
			riga = view.chooseWhatToCover().toUpperCase();
		
			try {
				if(riga.equals(liberaIniziale.getId())) {
					cardI = liberaIniziale;	
					check = true;
					
				}
				if (libereRisorsa != null) {
					for(CartaRisorsa r: libereRisorsa) {
						if(riga.equals(r.getId())) {
							cardR = r;
							check = true;	
						}
					}
				} 
				if (libereOro != null) {
					for(CartaOro o: libereOro) {
						if(riga.equals(o.getId())) {
							cardO = o;
							check = true;

						}
					}
				}
				
				if(scelta.charAt(0)=='R') {
					if(view.chooseWhichSide(g.getNick(), g.getMano().getResourceById(scelta), this.model.getCampo().getMazzoR().getRetroCarta(g.getMano().getResourceById(scelta)))) {
						cartaScelta = g.getMano().getResourceById(scelta);
					} else {
						cartaScelta = this.model.getCampo().getMazzoR().getRetroCarta(g.getMano().getResourceById(scelta));
					}
				} else {
					if(view.chooseWhichSide(g.getNick(), g.getMano().getGoldById(scelta),this.model.getCampo().getMazzoO().getRetroCarta(g.getMano().getGoldById(scelta)))) {
						cartaScelta = g.getMano().getGoldById(scelta);
					} else {
						cartaScelta = this.model.getCampo().getMazzoO().getRetroCarta(g.getMano().getGoldById(scelta));
					}
				}
				
				view.showFreeCornersMessage();
				
				if(cardI != null) {
					view.showFreeInitialCorners(cardI, this.getFreeInitialCorners(cardI));
					if(this.checkPlaceInitial(g, scelta, cartaScelta, cardI, view.chooseWhichCorner())) {
						check = true;
					}
				}
				if(cardR != null) {
					view.showFreeResourceCorners(cardR, this.getFreeResourceCorners(cardR));
					if(this.checkPlaceResource(g, scelta, cartaScelta, cardR, view.chooseWhichCorner())) {
						check = true;
					}
				}else if(cardO != null) {
					view.showFreeGoldCorners(cardO, this.getFreeGoldCorners(cardO));
					if(this.checkPlaceGold(g, scelta, cartaScelta, cardO, view.chooseWhichCorner())) {
						check = true;
					}
				}
				
				if(check == false) {
					throw new IOException();
				}
				
			}catch(IOException e) {
				view.insertAValidCode();
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
				if(g.getBoard().getMatrix()[i][j] == null) {
					continue;
				} else {
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
		}
		return false;
	}
	
	public boolean checkPlaceCondition(Angolo ang, Angolo coperto) {
		if(ang.getTipo().equals(TipoAngolo.NASCOSTO)) {
			if(coperto.getTipo().equals(TipoAngolo.VUOTO)) {
				view.showPlaceErrorMessage();
				return false;
			}
		} 
		if(coperto.getTipo().equals(TipoAngolo.NASCOSTO)) {
			view.showImpossiblePlaceMessage();
			return false;
		}
		return true;
	}
	
	/**
	 * Metodo che controlla che la carta possa essere realmente posizionata su una carta iniziale
	 * come vuole il giocatore.
	 * @param g
	 * @param scelta
	 * @param cartaScelta
	 * @param coperta
	 * @param angolo
	 * @return
	 */
	public boolean checkPlaceInitial(Giocatore g, String scelta, Carta cartaScelta, CartaIniziale coperta, Posizione angolo) {
		if(!this.checkFreeMatrix(g, coperta, angolo)) {
			view.isFullMessage();
			return false;
		} 
		switch(angolo) {
		case ADX:
			if(cartaScelta.getId().charAt(0)=='R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.BSX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.BSX, angolo, scelta);
						break;
					}
				}
			} else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.BSX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.BSX, angolo, scelta);
						break;
					}
				}
			}
		case BDX:
			if(cartaScelta.getId().charAt(0)=='R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.ASX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.ASX, angolo, scelta);
						break;
					}
				}
			} else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.ASX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.ASX, angolo, scelta);
						break;
					}
				}
			}
		case BSX:
			if(cartaScelta.getId().charAt(0)=='R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.ADX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.ADX, angolo, scelta);
						break;
					}
				}
			} else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.ADX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.ADX, angolo, scelta);
						break;
					}
				}
			}
		case ASX:
			if(cartaScelta.getId().charAt(0)=='R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.BDX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.BDX, angolo, scelta);
						break;
					}
				}
			} else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.BDX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.BDX, angolo, scelta);
						break;
					}
				}
			}
			
		}
		
		return true;
	}
	/**
	 * Metodo che controlla che la carta possa essere realmente posizionata su una carta risorsa
	 * come vuole il giocatore.
	 * @param g
	 * @param scelta
	 * @param cartaScelta
	 * @param coperta
	 * @param angolo
	 * @return
	 */
	public boolean checkPlaceResource(Giocatore g, String scelta, Carta cartaScelta, CartaRisorsa coperta, Posizione angolo) {
		if(!this.checkFreeMatrix(g, coperta, angolo)) {
			view.isFullMessage();
			return false;
		} 
		switch(angolo) {
		case ADX:
			if(cartaScelta.getId().charAt(0)=='R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.BSX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.BSX, angolo, scelta);
						break;
					}
				}
			} else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.BSX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.BSX, angolo, scelta);
						break;
					}
				}
			}
		case BDX:
			if(cartaScelta.getId().charAt(0)=='R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.ASX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.ASX, angolo, scelta);
						break;
					}
				}
			} else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.ASX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.ASX, angolo, scelta);
						break;
					}
				}
			}
		case BSX:
			if(cartaScelta.getId().charAt(0)=='R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.ADX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.ADX, angolo, scelta);
						break;
					}
				}
			} else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.ADX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.ADX, angolo, scelta);
						break;
					}
				}
			}
		case ASX:
			if(cartaScelta.getId().charAt(0)=='R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.BDX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.BDX, angolo, scelta);
						break;
					}
				}
			} else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.BDX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.BDX, angolo, scelta);
						break;
					}
				}
			}
			
		}
		
		return true;
	}
	
	/**
	 * Metodo che controlla che la carta possa essere realmente posizionata su una carta oro
	 * come vuole il giocatore.
	 * @param g
	 * @param scelta
	 * @param cartaScelta
	 * @param coperta
	 * @param angolo
	 * @return
	 */
	public boolean checkPlaceGold(Giocatore g, String scelta, Carta cartaScelta, CartaOro coperta, Posizione angolo) {
		if(!this.checkFreeMatrix(g, coperta, angolo)) {
			view.isFullMessage();
			return false;
		} 
		switch(angolo) {
		case ADX:
			if(cartaScelta.getId().charAt(0)=='R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.BSX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.BSX, angolo, scelta);
						break;
					}
				}
			} else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.BSX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.BSX, angolo, scelta);
						break;
					}
				}
			}
		case BDX:
			if(cartaScelta.getId().charAt(0)=='R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.ASX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.ASX, angolo, scelta);
						break;
					}
				}
			} else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.ASX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.ASX, angolo, scelta);
						break;
					}
				}
			}
		case BSX:
			if(cartaScelta.getId().charAt(0)=='R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.ADX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.ADX, angolo, scelta);
						break;
					}
				}
			} else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.ADX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.ADX, angolo, scelta);
						break;
					}
				}
			}
		case ASX:
			if(cartaScelta.getId().charAt(0)=='R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.BDX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.BDX, angolo, scelta);
						break;
					}
				}
			} else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.BDX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.BDX, angolo, scelta);
						break;
					}
				}
			}
			
		}
		
		return true;
	}
	
	/**
	 * Metodo che verifica che tutti gli angoli che andrebbe a coprire la carta da posizionare 
	 * rispettino le regole di posizionamento.
	 * @param carta
	 * @param coperta
	 * @param g
	 * @param angolo
	 * @return
	 */
	public boolean checkPlace (Carta carta, Carta coperta, Giocatore g, Posizione angolo) {
		
		for(int i = 0; i < g.getBoard().getMatrix().length; i++) {
			for(int j = 0; j < g.getBoard().getMatrix()[i].length; j++) {
				if (g.getBoard().getMatrix()[i][j] == null) {
					continue;
				} else {
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
								
							} else if (coperta.getId().charAt(0) == 'O') {
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
								
							} else {
								switch (angolo) {
								case ADX:
									if(g.getBoard().getMatrix()[i-2][j] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ASX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j])).getAngoloByPosizione(Posizione.BDX))) {
										return false;
									}
									if(g.getBoard().getMatrix()[i-2][j+2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ADX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j+2])).getAngoloByPosizione(Posizione.BSX))) {
										return false;
									}
									if(g.getBoard().getMatrix()[i][j+2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BDX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i][j+2])).getAngoloByPosizione(Posizione.ASX))) {
										return false;
									}
									break;
								case BDX:
									if(g.getBoard().getMatrix()[i][j+2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ADX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i][j+2])).getAngoloByPosizione(Posizione.BSX))) {
										return false;
									}
									if(g.getBoard().getMatrix()[i+2][j+2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BDX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j+2])).getAngoloByPosizione(Posizione.ASX))) {
										return false;
									}
									if(g.getBoard().getMatrix()[i+2][j] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BSX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j])).getAngoloByPosizione(Posizione.ADX))) {
										return false;
									}
									break;
								case BSX:
									if(g.getBoard().getMatrix()[i+2][j] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BDX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j])).getAngoloByPosizione(Posizione.ASX))) {
										return false;
									}
									if(g.getBoard().getMatrix()[i+2][j-2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BSX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j-2])).getAngoloByPosizione(Posizione.ADX))) {
										return false;
									}
									if(g.getBoard().getMatrix()[i][j-2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ASX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i][j-2])).getAngoloByPosizione(Posizione.BDX))) {
										return false;
									}
									break;
								case ASX:
									if(g.getBoard().getMatrix()[i][j-2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BSX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i][j-2])).getAngoloByPosizione(Posizione.ADX))) {
										return false;
									}
									if(g.getBoard().getMatrix()[i-2][j-2] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ASX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j-2])).getAngoloByPosizione(Posizione.BDX))) {
										return false;
									}
									if(g.getBoard().getMatrix()[i-2][j] != null && !checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ADX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j])).getAngoloByPosizione(Posizione.BSX))) {
										return false;
									}
									break;
								
								}
							}
							
						} else if(coperta.getId().charAt(0) =='O') {
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
							
						} else if (coperta.getId().charAt(0) == 'R') {
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
						
						} else {
							switch (angolo) {
							case ADX:
								if(g.getBoard().getMatrix()[i-2][j] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ASX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j])).getAngoloByPosizione(Posizione.BDX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i-2][j+2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ADX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j+2])).getAngoloByPosizione(Posizione.BSX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i][j+2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BDX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i][j+2])).getAngoloByPosizione(Posizione.ASX))) {
									return false;
								}
								break;
							case BDX:
								if(g.getBoard().getMatrix()[i][j+2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ADX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i][j+2])).getAngoloByPosizione(Posizione.BSX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i+2][j+2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BDX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j+2])).getAngoloByPosizione(Posizione.ASX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i+2][j] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BSX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j])).getAngoloByPosizione(Posizione.ADX))) {
									return false;
								}
								break;
							case BSX:
								if(g.getBoard().getMatrix()[i+2][j] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BDX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j])).getAngoloByPosizione(Posizione.ASX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i+2][j-2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BSX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j-2])).getAngoloByPosizione(Posizione.ADX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i][j-2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ASX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i][j-2])).getAngoloByPosizione(Posizione.BDX))) {
									return false;
								}
								break;
							case ASX:
								if(g.getBoard().getMatrix()[i][j-2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BSX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i][j-2])).getAngoloByPosizione(Posizione.ADX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i-2][j-2] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ASX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j-2])).getAngoloByPosizione(Posizione.BDX))) {
									return false;
								}
								if(g.getBoard().getMatrix()[i-2][j] != null && !checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ADX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i-2][j])).getAngoloByPosizione(Posizione.BSX))) {
									return false;
								}
								break;
							}
						}
						
					}
					
				}
			}
		}
		return true;
	}
		
	
	/**
	 * Metodo che aggiunge alla lista di carte della board la carta piazzata e 
	 * aggiorna la relativa matrice e i contatori.
	 * @param card
	 * @param coperta
	 * @param angoloPos
	 * @param angoloCop
	 */
	public void placeCard(Giocatore g, Carta card, Carta coperta, Posizione angoloPos, Posizione angoloCop, String scelta) {
		if(card.getId().charAt(0)=='R') {
			if(coperta.getId().charAt(0)=='R') {
				((CartaRisorsa)card).getAngoloByPosizione(angoloPos).setLink(coperta.getId());
				((CartaRisorsa)coperta).getAngoloByPosizione(angoloCop).setLink(card.getId());
			} else if(coperta.getId().charAt(0)=='O') {
				((CartaRisorsa)card).getAngoloByPosizione(angoloPos).setLink(coperta.getId());
				((CartaOro)coperta).getAngoloByPosizione(angoloCop).setLink(card.getId());
			}else {
				((CartaRisorsa)card).getAngoloByPosizione(angoloPos).setLink(coperta.getId());
				((CartaIniziale)coperta).getAngoloByPosizione(angoloCop).setLink(card.getId());
			}
		} else if (coperta.getId().charAt(0)=='O') {
			((CartaOro)card).getAngoloByPosizione(angoloPos).setLink(coperta.getId());
			((CartaOro)coperta).getAngoloByPosizione(angoloCop).setLink(card.getId());
			} else if(coperta.getId().charAt(0)=='R') {
				((CartaOro)card).getAngoloByPosizione(angoloPos).setLink(coperta.getId());
				((CartaRisorsa)coperta).getAngoloByPosizione(angoloCop).setLink(card.getId());
			}else {
				((CartaOro)card).getAngoloByPosizione(angoloPos).setLink(coperta.getId());
				((CartaIniziale)coperta).getAngoloByPosizione(angoloCop).setLink(card.getId());
		}
		
		int riga = (g.getBoard().getMatrix().length/2)+1;
		int colonna = (g.getBoard().getMatrix().length/2)+1;
		for(int i = 0; i < g.getBoard().getMatrix().length; i++) {
			for(int j = 0; j < g.getBoard().getMatrix()[i].length; j++) {
				if(g.getBoard().getMatrix()[i][j] != null) {
					if(g.getBoard().getMatrix()[i][j].equals(coperta.getId())) {
						riga=i;
						colonna=j;
					}
				}
			}
		}
		boolean delete = false;
		switch(angoloCop) {
		case ADX:
			if(riga == 0 || colonna == 8) {
				delete = true;
			}
			break;
		case BDX:
			if(riga == 8 || colonna == 8) {
				delete = true;
			}
			break;
		case BSX:
			if(riga == 8 || colonna == 0) {
				delete = true;
			}
			break;
		case ASX:
			if(riga == 0 || colonna == 0) {
				delete = true;
			}
			break;
		}
		
		if(delete) {
			String [][] mat = new String [g.getBoard().getMatrix().length + 8][g.getBoard().getMatrix().length + 8];
			for(int i = 0; i < g.getBoard().getMatrix().length; i++) {
				for(int j = 0; j < g.getBoard().getMatrix()[i].length; j++) {
					mat [i + 4][j + 4] = g.getBoard().getMatrix()[i][j];
				}
			}
			g.getBoard().setMatrix(mat);
		}
		
		this.count(g.getBoard(), coperta, getCarteCoperte(g,coperta, angoloCop), angoloCop);
		this.countPoints(g.getBoard(), card);
		
		if(card.getId().charAt(0)=='R') {
			g.getMano().getRisorsa().remove(g.getMano().getResourceById(scelta));
			g.getBoard().getRisorsa().add((CartaRisorsa) card);
		} else {
			g.getMano().getOro().remove(g.getMano().getGoldById(scelta));
			g.getBoard().getOro().add((CartaOro) card);
		}
		
		
	}
	
	/**
	 * Metodo che ritorna gli id delle carte che stanno per essere coperte con il posizionamento.
	 * @param g
	 * @param coperta
	 * @param angolo
	 * @return
	 */
	public ArrayList<String> getCarteCoperte(Giocatore g, Carta coperta, Posizione angolo) {
		ArrayList<String> carteCoperte = new ArrayList<String>();
		for(int i = 0; i < g.getBoard().getMatrix().length; i++) {
			for(int j = 0; j < g.getBoard().getMatrix()[i].length; j++) {
				if(g.getBoard().getMatrix()[i][j] != null) {
					if(g.getBoard().getMatrix()[i][j].equals(coperta.getId())) {
						switch(angolo) {
						case ADX:
							carteCoperte.add(coperta.getId());
							if (g.getBoard().getMatrix()[i-2][j] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i-2][j]);
							} else {
								carteCoperte.add("0");
							}
							if (g.getBoard().getMatrix()[i][j+2] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i][j+2]);
							} else {
								carteCoperte.add("0");
							}
							if (g.getBoard().getMatrix()[i-2][j+2] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i-2][j+2]);
							} else {
								carteCoperte.add("0");
							}
							break;
						case BDX:
							carteCoperte.add(coperta.getId());
							if (g.getBoard().getMatrix()[i+2][j] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i+2][j]);
							} else {
								carteCoperte.add("0");
							}
							if (g.getBoard().getMatrix()[i][j+2] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i][j+2]);
							} else {
								carteCoperte.add("0");
							}
							if (g.getBoard().getMatrix()[i+2][j+2] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i+2][j+2]);
							} else {
								carteCoperte.add("0");
							}
							break;
						case BSX:
							carteCoperte.add(coperta.getId());
							if (g.getBoard().getMatrix()[i+2][j] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i+2][j]);
							} else {
								carteCoperte.add("0");
							}
							if (g.getBoard().getMatrix()[i][j-2] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i][j-2]);
							} else {
								carteCoperte.add("0");
							}
							if (g.getBoard().getMatrix()[i+2][j-2] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i+2][j-2]);
							} else {
								carteCoperte.add("0");
							}
							break;
						case ASX:
							carteCoperte.add(coperta.getId());
							if (g.getBoard().getMatrix()[i-2][j] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i-2][j]);
							} else {
								carteCoperte.add("0");
							}
							if (g.getBoard().getMatrix()[i][j-2] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i][j-2]);
							} else {
								carteCoperte.add("0");
							}
							if (g.getBoard().getMatrix()[i-2][j-2] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i-2][j-2]);
							} else {
								carteCoperte.add("0");
							}
							break;
						}
					}
				}
			}
		}	
		
		return carteCoperte;
	}
	
	/**
	 * Metodo che aggiorna i vari contatori visulizzati sulla Board.
	 * @param board
	 * @param carta
	 * @param carteCoperte
	 * @param angolo
	 */
	public void count(Board board, Carta carta, ArrayList<String> carteCoperte, Posizione angolo) {
		if(carteCoperte == null) {
			for (int i=0; i < 4; i++) {
				if (((CartaIniziale)carta).getAngoli().get(i).getTipo().equals(TipoAngolo.RISORSA)) {
					countRisorsa(board, ((CartaIniziale)carta).getAngoli().get(i), true);
				}
			}
		} else {
			if(carta.getId().charAt(0)=='R') {
				if (((CartaRisorsa) carta).getCentro() != null) {
					int sum = 0;
					switch(((CartaRisorsa) carta).getCentro()) {
					case VEGETALE:
						sum = board.getNumRis().get(0);
						sum ++;
						board.getNumRis().set(0, sum);
						break;
					case ANIMALE:
						sum = board.getNumRis().get(1);
						sum ++;
						board.getNumRis().set(1, sum);
						break;
					case FUNGHI:
						sum = board.getNumRis().get(2);
						sum ++;
						board.getNumRis().set(2, sum);
						break;
					case INSETTI:
						sum = board.getNumRis().get(3);
						sum ++;
						board.getNumRis().set(3, sum);
						break;
					}
				} else {
					for (int i=0; i < 4; i++) {
						if (((CartaRisorsa)carta).getAngoli().get(i).getTipo().equals(TipoAngolo.RISORSA)) {
							countRisorsa(board, ((CartaRisorsa)carta).getAngoli().get(i), true);
						
						} else if (((CartaRisorsa)carta).getAngoli().get(i).getTipo().equals(TipoAngolo.OGGETTO)) {
							countOggetto(board, ((CartaRisorsa)carta).getAngoli().get(i), true);
						}
					}
				}
			} else if (carta.getId().charAt(0)=='O') {
				if (((CartaOro) carta).getCentro() != null) {
					int sum = 0;
					switch(((CartaOro) carta).getCentro()) {
					case VEGETALE:
						sum = board.getNumRis().get(0);
						sum ++;
						board.getNumRis().set(0, sum);
						break;
					case ANIMALE:
						sum = board.getNumRis().get(1);
						sum ++;
						board.getNumRis().set(1, sum);
						break;
					case FUNGHI:
						sum = board.getNumRis().get(2);
						sum ++;
						board.getNumRis().set(2, sum);
						break;
					case INSETTI:
						sum = board.getNumRis().get(3);
						sum ++;
						board.getNumRis().set(3, sum);
						break;
					}
				} else {
					for (int i=0; i<4; i++) {
						if (((CartaOro)carta).getAngoli().get(i).getTipo().equals(TipoAngolo.RISORSA)) {
							countRisorsa(board, ((CartaOro)carta).getAngoli().get(i), true);
						
						} else if (((CartaOro)carta).getAngoli().get(i).getTipo().equals(TipoAngolo.OGGETTO)) {
							countOggetto(board, ((CartaOro)carta).getAngoli().get(i), true);
						}
					}
				}
			}
			switch(angolo) {
			case ADX:
				for(int i=0; i<carteCoperte.size(); i++) {
					if (i==0) {
						if(carteCoperte.get(i).charAt(0)=='I') {
							CartaIniziale cardI = (CartaIniziale) board.getByID(carteCoperte.get(i));
							if(cardI.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardI.getAngoloByPosizione(Posizione.ADX), false);
								
							} else if (cardI.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardI.getAngoloByPosizione(Posizione.ADX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='R') {
							CartaRisorsa cardR = (CartaRisorsa) board.getByID(carteCoperte.get(i));
							if(cardR.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardR.getAngoloByPosizione(Posizione.ADX), false);
								
							} else if (cardR.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardR.getAngoloByPosizione(Posizione.ADX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='O') {
							CartaOro cardO = (CartaOro) board.getByID(carteCoperte.get(i));
							if(cardO.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardO.getAngoloByPosizione(Posizione.ADX), false);
								
							} else if (cardO.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardO.getAngoloByPosizione(Posizione.ADX), false);
								
							}
						}
						
					}
					if (i==1) {
						if(carteCoperte.get(i).charAt(0)=='I') {
							CartaIniziale cardI = (CartaIniziale) board.getByID(carteCoperte.get(i));
							if(cardI.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardI.getAngoloByPosizione(Posizione.BDX), false);
								
							} else if (cardI.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardI.getAngoloByPosizione(Posizione.BDX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='R') {
							CartaRisorsa cardR = (CartaRisorsa) board.getByID(carteCoperte.get(i));
							if(cardR.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardR.getAngoloByPosizione(Posizione.BDX), false);
								
							} else if (cardR.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardR.getAngoloByPosizione(Posizione.BDX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='O') {
							CartaOro cardO = (CartaOro) board.getByID(carteCoperte.get(i));
							if(cardO.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardO.getAngoloByPosizione(Posizione.BDX), false);
								
							} else if (cardO.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardO.getAngoloByPosizione(Posizione.BDX), false);
								
							}
						}
						
					}
					if (i==2) {
						if(carteCoperte.get(i).charAt(0)=='I') {
							CartaIniziale cardI = (CartaIniziale) board.getByID(carteCoperte.get(i));
							if(cardI.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardI.getAngoloByPosizione(Posizione.ASX), false);
								
							} else if (cardI.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardI.getAngoloByPosizione(Posizione.ASX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='R') {
							CartaRisorsa cardR = (CartaRisorsa) board.getByID(carteCoperte.get(i));
							if(cardR.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardR.getAngoloByPosizione(Posizione.ASX), false);
								
							} else if (cardR.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardR.getAngoloByPosizione(Posizione.ASX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='O') {
							CartaOro cardO = (CartaOro) board.getByID(carteCoperte.get(i));
							if(cardO.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardO.getAngoloByPosizione(Posizione.ASX), false);
								
							} else if (cardO.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardO.getAngoloByPosizione(Posizione.ASX), false);
								
							}
						}
						
					}
					if (i==3) {
						if(carteCoperte.get(i).charAt(0)=='I') {
							CartaIniziale cardI = (CartaIniziale) board.getByID(carteCoperte.get(i));
							if(cardI.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardI.getAngoloByPosizione(Posizione.BSX), false);
								
							} else if (cardI.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardI.getAngoloByPosizione(Posizione.BSX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='R') {
							CartaRisorsa cardR = (CartaRisorsa) board.getByID(carteCoperte.get(i));
							if(cardR.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardR.getAngoloByPosizione(Posizione.BSX), false);
								
							} else if (cardR.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardR.getAngoloByPosizione(Posizione.BSX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='O') {
							CartaOro cardO = (CartaOro) board.getByID(carteCoperte.get(i));
							if(cardO.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardO.getAngoloByPosizione(Posizione.BSX), false);
								
							} else if (cardO.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardO.getAngoloByPosizione(Posizione.BSX), false);
								
							}
						}	
					}
				}
				break;
			case BDX:
				for(int i=0; i<carteCoperte.size(); i++) {
					if (i==0) {
						if(carteCoperte.get(i).charAt(0)=='I') {
							CartaIniziale cardI = (CartaIniziale) board.getByID(carteCoperte.get(i));
							if(cardI.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardI.getAngoloByPosizione(Posizione.BDX), false);
								
							} else if (cardI.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardI.getAngoloByPosizione(Posizione.BDX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='R') {
							CartaRisorsa cardR = (CartaRisorsa) board.getByID(carteCoperte.get(i));
							if(cardR.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardR.getAngoloByPosizione(Posizione.BDX), false);
								
							} else if (cardR.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardR.getAngoloByPosizione(Posizione.BDX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='O') {
							CartaOro cardO = (CartaOro) board.getByID(carteCoperte.get(i));
							if(cardO.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardO.getAngoloByPosizione(Posizione.BDX), false);
								
							} else if (cardO.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardO.getAngoloByPosizione(Posizione.BDX), false);
								
							}
						}
						
					}
					if (i==1) {
						if(carteCoperte.get(i).charAt(0)=='I') {
							CartaIniziale cardI = (CartaIniziale) board.getByID(carteCoperte.get(i));
							if(cardI.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardI.getAngoloByPosizione(Posizione.ADX), false);
								
							} else if (cardI.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardI.getAngoloByPosizione(Posizione.ADX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='R') {
							CartaRisorsa cardR = (CartaRisorsa) board.getByID(carteCoperte.get(i));
							if(cardR.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardR.getAngoloByPosizione(Posizione.ADX), false);
								
							} else if (cardR.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardR.getAngoloByPosizione(Posizione.ADX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='O') {
							CartaOro cardO = (CartaOro) board.getByID(carteCoperte.get(i));
							if(cardO.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardO.getAngoloByPosizione(Posizione.ADX), false);
								
							} else if (cardO.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardO.getAngoloByPosizione(Posizione.ADX), false);
								
							}
						}
						
					}
					if (i==2) {
						if(carteCoperte.get(i).charAt(0)=='I') {
							CartaIniziale cardI = (CartaIniziale) board.getByID(carteCoperte.get(i));
							if(cardI.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardI.getAngoloByPosizione(Posizione.BSX), false);
								
							} else if (cardI.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardI.getAngoloByPosizione(Posizione.BSX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='R') {
							CartaRisorsa cardR = (CartaRisorsa) board.getByID(carteCoperte.get(i));
							if(cardR.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardR.getAngoloByPosizione(Posizione.BSX), false);
								
							} else if (cardR.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardR.getAngoloByPosizione(Posizione.BSX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='O') {
							CartaOro cardO = (CartaOro) board.getByID(carteCoperte.get(i));
							if(cardO.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardO.getAngoloByPosizione(Posizione.BSX), false);
								
							} else if (cardO.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardO.getAngoloByPosizione(Posizione.BSX), false);
								
							}
						}
						
					}
					if (i==3) {
						if(carteCoperte.get(i).charAt(0)=='I') {
							CartaIniziale cardI = (CartaIniziale) board.getByID(carteCoperte.get(i));
							if(cardI.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardI.getAngoloByPosizione(Posizione.ASX), false);
								
							} else if (cardI.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardI.getAngoloByPosizione(Posizione.ASX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='R') {
							CartaRisorsa cardR = (CartaRisorsa) board.getByID(carteCoperte.get(i));
							if(cardR.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardR.getAngoloByPosizione(Posizione.ASX), false);
								
							} else if (cardR.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardR.getAngoloByPosizione(Posizione.ASX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='O') {
							CartaOro cardO = (CartaOro) board.getByID(carteCoperte.get(i));
							if(cardO.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardO.getAngoloByPosizione(Posizione.ASX), false);
								
							} else if (cardO.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardO.getAngoloByPosizione(Posizione.ASX), false);
								
							}
						}
						
					}
				}
				break;
			case BSX:
				for(int i=0; i<carteCoperte.size(); i++) {
					if (i==0) {
						if(carteCoperte.get(i).charAt(0)=='I') {
							CartaIniziale cardI = (CartaIniziale) board.getByID(carteCoperte.get(i));
							if(cardI.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardI.getAngoloByPosizione(Posizione.BSX), false);
								
							} else if (cardI.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardI.getAngoloByPosizione(Posizione.BSX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='R') {
							CartaRisorsa cardR = (CartaRisorsa) board.getByID(carteCoperte.get(i));
							if(cardR.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardR.getAngoloByPosizione(Posizione.BSX), false);
								
							} else if (cardR.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardR.getAngoloByPosizione(Posizione.BSX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='O') {
							CartaOro cardO = (CartaOro) board.getByID(carteCoperte.get(i));
							if(cardO.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardO.getAngoloByPosizione(Posizione.BSX), false);
								
							} else if (cardO.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardO.getAngoloByPosizione(Posizione.BSX), false);
								
							}
						}
						
					}
					if (i==1) {
						if(carteCoperte.get(i).charAt(0)=='I') {
							CartaIniziale cardI = (CartaIniziale) board.getByID(carteCoperte.get(i));
							if(cardI.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardI.getAngoloByPosizione(Posizione.ASX), false);
								
							} else if (cardI.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardI.getAngoloByPosizione(Posizione.ASX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='R') {
							CartaRisorsa cardR = (CartaRisorsa) board.getByID(carteCoperte.get(i));
							if(cardR.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardR.getAngoloByPosizione(Posizione.ASX), false);
								
							} else if (cardR.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardR.getAngoloByPosizione(Posizione.ASX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='O') {
							CartaOro cardO = (CartaOro) board.getByID(carteCoperte.get(i));
							if(cardO.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardO.getAngoloByPosizione(Posizione.ASX), false);
								
							} else if (cardO.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardO.getAngoloByPosizione(Posizione.ASX), false);
								
							}
						}
						
					}
					if (i==2) {
						if(carteCoperte.get(i).charAt(0)=='I') {
							CartaIniziale cardI = (CartaIniziale) board.getByID(carteCoperte.get(i));
							if(cardI.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardI.getAngoloByPosizione(Posizione.BDX), false);
								
							} else if (cardI.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardI.getAngoloByPosizione(Posizione.BDX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='R') {
							CartaRisorsa cardR = (CartaRisorsa) board.getByID(carteCoperte.get(i));
							if(cardR.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardR.getAngoloByPosizione(Posizione.BDX), false);
								
							} else if (cardR.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardR.getAngoloByPosizione(Posizione.BDX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='O') {
							CartaOro cardO = (CartaOro) board.getByID(carteCoperte.get(i));
							if(cardO.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardO.getAngoloByPosizione(Posizione.BDX), false);
								
							} else if (cardO.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardO.getAngoloByPosizione(Posizione.BDX), false);
								
							}
						}
						
					}
					if (i==3) {
						if(carteCoperte.get(i).charAt(0)=='I') {
							CartaIniziale cardI = (CartaIniziale) board.getByID(carteCoperte.get(i));
							if(cardI.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardI.getAngoloByPosizione(Posizione.ADX), false);
								
							} else if (cardI.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardI.getAngoloByPosizione(Posizione.ADX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='R') {
							CartaRisorsa cardR = (CartaRisorsa) board.getByID(carteCoperte.get(i));
							if(cardR.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardR.getAngoloByPosizione(Posizione.ADX), false);
								
							} else if (cardR.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardR.getAngoloByPosizione(Posizione.ADX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='O') {
							CartaOro cardO = (CartaOro) board.getByID(carteCoperte.get(i));
							if(cardO.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardO.getAngoloByPosizione(Posizione.ADX), false);
								
							} else if (cardO.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardO.getAngoloByPosizione(Posizione.ADX), false);
								
							}
						}
						
					}
				}
				break;
			case ASX:
				for(int i=0; i<carteCoperte.size(); i++) {
					if (i==0) {
						if(carteCoperte.get(i).charAt(0)=='I') {
							CartaIniziale cardI = (CartaIniziale) board.getByID(carteCoperte.get(i));
							if(cardI.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardI.getAngoloByPosizione(Posizione.ASX), false);
								
							} else if (cardI.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardI.getAngoloByPosizione(Posizione.ASX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='R') {
							CartaRisorsa cardR = (CartaRisorsa) board.getByID(carteCoperte.get(i));
							if(cardR.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardR.getAngoloByPosizione(Posizione.ASX), false);
								
							} else if (cardR.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardR.getAngoloByPosizione(Posizione.ASX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='O') {
							CartaOro cardO = (CartaOro) board.getByID(carteCoperte.get(i));
							if(cardO.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardO.getAngoloByPosizione(Posizione.ASX), false);
								
							} else if (cardO.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardO.getAngoloByPosizione(Posizione.ASX), false);
								
							}
						}
						
					}
					if (i==1) {
						if(carteCoperte.get(i).charAt(0)=='I') {
							CartaIniziale cardI = (CartaIniziale) board.getByID(carteCoperte.get(i));
							if(cardI.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardI.getAngoloByPosizione(Posizione.BSX), false);
								
							} else if (cardI.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardI.getAngoloByPosizione(Posizione.BSX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='R') {
							CartaRisorsa cardR = (CartaRisorsa) board.getByID(carteCoperte.get(i));
							if(cardR.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardR.getAngoloByPosizione(Posizione.BSX), false);
								
							} else if (cardR.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardR.getAngoloByPosizione(Posizione.BSX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='O') {
							CartaOro cardO = (CartaOro) board.getByID(carteCoperte.get(i));
							if(cardO.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardO.getAngoloByPosizione(Posizione.BSX), false);
								
							} else if (cardO.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardO.getAngoloByPosizione(Posizione.BSX), false);
								
							}
						}
						
					}
					if (i==2) {
						if(carteCoperte.get(i).charAt(0)=='I') {
							CartaIniziale cardI = (CartaIniziale) board.getByID(carteCoperte.get(i));
							if(cardI.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardI.getAngoloByPosizione(Posizione.ADX), false);
								
							} else if (cardI.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardI.getAngoloByPosizione(Posizione.ADX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='R') {
							CartaRisorsa cardR = (CartaRisorsa) board.getByID(carteCoperte.get(i));
							if(cardR.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardR.getAngoloByPosizione(Posizione.ADX), false);
								
							} else if (cardR.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardR.getAngoloByPosizione(Posizione.ADX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='O') {
							CartaOro cardO = (CartaOro) board.getByID(carteCoperte.get(i));
							if(cardO.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardO.getAngoloByPosizione(Posizione.ADX), false);
								
							} else if (cardO.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardO.getAngoloByPosizione(Posizione.ADX), false);
								
							}
						}
						
					}
					if (i==3) {
						if(carteCoperte.get(i).charAt(0)=='I') {
							CartaIniziale cardI = (CartaIniziale) board.getByID(carteCoperte.get(i));
							if(cardI.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardI.getAngoloByPosizione(Posizione.BDX), false);
								
							} else if (cardI.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardI.getAngoloByPosizione(Posizione.BDX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='R') {
							CartaRisorsa cardR = (CartaRisorsa) board.getByID(carteCoperte.get(i));
							if(cardR.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardR.getAngoloByPosizione(Posizione.BDX), false);
								
							} else if (cardR.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardR.getAngoloByPosizione(Posizione.BDX), false);
								
							}
							
						} else if(carteCoperte.get(i).charAt(0)=='O') {
							CartaOro cardO = (CartaOro) board.getByID(carteCoperte.get(i));
							if(cardO.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(board, cardO.getAngoloByPosizione(Posizione.BDX), false);
								
							} else if (cardO.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(board, cardO.getAngoloByPosizione(Posizione.BDX), false);
								
							}
						}
					}
				}
				break;
			}
		}
	}
	
	/**
	 * Metodo che aggiorna il contatore delle risorse.
	 * @param board
	 * @param angolo
	 * @param somma
	 */
	public void countRisorsa(Board board, Angolo angolo, boolean somma) {
		if (somma) {
			int sum = 0;
			switch (angolo.getRisorsa()) {
			case VEGETALE:
				sum = board.getNumRis().get(0);
				sum ++;
				board.getNumRis().set(0, sum);
				break;
			case ANIMALE:
				sum = board.getNumRis().get(1);
				sum ++;
				board.getNumRis().set(1, sum);
				break;
			case FUNGHI:
				sum = board.getNumRis().get(2);
				sum ++;
				board.getNumRis().set(2, sum);
				break;
			case INSETTI:
				sum = board.getNumRis().get(3);
				sum ++;
				board.getNumRis().set(3, sum);
				break;
			default:
				break;
			}
		} else {
			int dif = 0;
			switch (angolo.getRisorsa()) {
			case VEGETALE:
				dif = board.getNumRis().get(0);
				dif --;
				board.getNumRis().set(0, dif);
				break;
			case ANIMALE:
				dif = board.getNumRis().get(1);
				dif --;
				board.getNumRis().set(1, dif);
				break;
			case FUNGHI:
				dif = board.getNumRis().get(2);
				dif --;
				board.getNumRis().set(2, dif);
				break;
			case INSETTI:
				dif = board.getNumRis().get(3);
				dif --;
				board.getNumRis().set(3, dif);
				break;
			default:
				break;
			
			}
			
		}
		
	}
	
	/**
	 * Metodo che aggiorna il contatore degli oggetti.
	 * @param board
	 * @param angolo
	 * @param somma
	 */
	public void countOggetto(Board board, Angolo angolo, boolean somma) {
		if (somma) {
			int sum = 0;
			switch (angolo.getOggetto()) {
			case PIUMA:
				sum = board.getNumOgg().get(0);
				sum ++;
				board.getNumOgg().set(0, sum);
				break;
			case INCHIOSTRO:
				sum = board.getNumOgg().get(1);
				sum ++;
				board.getNumOgg().set(1, sum);
				break;
			case PERGAMENA:
				sum = board.getNumOgg().get(2);
				sum ++;
				board.getNumOgg().set(2, sum);
				break;
			default:
				break;
			}
		} else {
			int dif = 0;
			switch (angolo.getOggetto()) {
			case PIUMA:
				dif = board.getNumOgg().get(0);
				dif --;
				board.getNumOgg().set(0, dif);
				break;
			case INCHIOSTRO:
				dif = board.getNumOgg().get(1);
				dif --;
				board.getNumOgg().set(1, dif);
				break;
			case PERGAMENA:
				dif = board.getNumOgg().get(2);
				dif --;
				board.getNumOgg().set(2, dif);
				break;
			default:
				break;
			}
			
		}
		
	}
	
	/**
	 * Metodo che conta i punti asegnati da una carta al suo posizionamento.
	 * @param board
	 * @param carta
	 */
	public void countPoints(Board board, Carta carta) {
		int punto = 0;
		int numLink = 0;
		if(carta.getId().charAt(0)=='R') {
			if (((CartaRisorsa)carta).getPunto()!=null) {
				punto = ((CartaRisorsa)carta).getPunto().getSomma();
			}
		} else if (((CartaOro)carta).getPunto() != null) {
			switch(((CartaOro)carta).getPunto().getTipo()){
			case IMMEDIATO:
				punto = ((CartaOro)carta).getPunto().getSomma();
				break;
			case ANGOLO:
				for(Angolo a: ((CartaOro)carta).getAngoli()) {
					if(a.getLink() != null) {
						numLink ++;
					}
				}
				punto = ((CartaOro)carta).getPunto().getSomma() * numLink;
				break;
			case OGGETTO:
				switch(((CartaOro)carta).getPunto().getOggetto()) {
				case PIUMA:
					punto = board.getNumOgg().get(0) * ((CartaOro)carta).getPunto().getSomma();
				case INCHIOSTRO:
					punto = board.getNumOgg().get(1) * ((CartaOro)carta).getPunto().getSomma();
				case PERGAMENA:
					punto = board.getNumOgg().get(2) * ((CartaOro)carta).getPunto().getSomma();
				}
			}
		}
		board.setPunteggio(board.getPunteggio() + punto);
	}
	
	/**
	 * Metodo che permette al giocatore di turno di pescare una carta.
	 * @param n
	 */
	public void pesca(Giocatore g) {
		view.showField(this.model.getCampo());
		view.showHand(g.getNick(), g.getMano());
		String pescata = "";
		boolean trovata = false;
		
		while(!trovata) {
			pescata = view.chooseWhatToDraw().toUpperCase();
			try {
				for(int i = 0; i < this.model.getCampo().getRisorsa().size(); i++) {
					if(pescata.equals(this.model.getCampo().getRisorsa().get(i).getId())) {
						trovata = true;
						g.getMano().getRisorsa().add(this.model.getCampo().getRisorsa().get(i));
						this.model.getCampo().getRisorsa().remove(i);
						this.model.getCampo().getRisorsa().add((CartaRisorsa) this.estrai(this.model.getCampo().getMazzoR().getMazzoFronte()));
					}
				}
				for(int i = 0; i < this.model.getCampo().getOro().size(); i++) {
					if(pescata.equals(this.model.getCampo().getOro().get(i).getId())) {
						trovata = true;
						g.getMano().getOro().add(this.model.getCampo().getOro().get(i));
						this.model.getCampo().getOro().remove(i);
						this.model.getCampo().getOro().add((CartaOro) this.estrai(this.model.getCampo().getMazzoO().getMazzoFronte()));
					}
				}
				if(this.model.getCampo().getMazzoR().showRetro(0).getId().equals(pescata)) {
					trovata = true;
					g.getMano().getRisorsa().add(this.model.getCampo().getMazzoR().getMazzoFronte().get(0));
					this.model.getCampo().getMazzoR().getMazzoFronte().remove(0);
				}
				if(this.model.getCampo().getMazzoO().showRetro(0).getId().equals(pescata)) {
					trovata = true;
					g.getMano().getOro().add(this.model.getCampo().getMazzoO().getMazzoFronte().get(0));
					this.model.getCampo().getMazzoO().getMazzoFronte().remove(0);
				}
				if(!trovata) {
					throw new IOException();
				}
			} catch (IOException e) {
				view.insertAValidCode();
			}
				
		}
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
	
	public CartaIniziale getFreeInitialCard(CartaIniziale carta) {
		for(int i=0; i < 4; i++) {
			if(this.getFreeInitialCorners(carta)==null) {
				return null;
			} else {
				return carta;
			}
		}
		
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
		
		if (card==null) {
			return null;
		}
		
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
		
		if (card==null) {
			return null;
		}
		
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
	
	public ArrayList<Angolo> getFreeInitialCorners(CartaIniziale card){
	ArrayList<Angolo> corner = new ArrayList<Angolo>();
		
		for (Angolo c : card.getAngoli()) {
			if(c.getLink() == null && c.getTipo() != TipoAngolo.NASCOSTO) {
				corner.add(c);
			}
		}
		
		return corner;
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
	
	public void checkObjective(Board board, CartaObiettivo obiettivo) {
		
		switch (obiettivo.getObiettivo().getTipo()) {
		case RISORSA:	
			board.setPunteggio(board.getPunteggio() + countResource(obiettivo.getObiettivo().getRisorsa(), board) * obiettivo.getPunto());
			break;
		case OGGETTO:
			if(obiettivo.getObiettivo().getOggetto().get(0).equals(Oggetto.PIUMA)) {
				if(obiettivo.getObiettivo().getOggetto().get(1).equals(Oggetto.PIUMA)) {
					board.setPunteggio(board.getPunteggio() + countObject(Oggetto.PIUMA, null, board) * obiettivo.getPunto());
				}else {
					board.setPunteggio(board.getPunteggio() + countObject(null ,obiettivo.getObiettivo().getOggetto(), board) * obiettivo.getPunto());
				}	
			}else {
				board.setPunteggio(board.getPunteggio() + countObject(obiettivo.getObiettivo().getOggetto().get(0), null, board) * obiettivo.getPunto());
			}
		case DISPOSIZIONE:
			board.setPunteggio(board.getPunteggio() + countDisposition(board, obiettivo.getObiettivo()) * obiettivo.getPunto());	
			break;
		}
	}
	
	public int countResource(Regno risorsa, Board board) {
		switch(risorsa) {
		case VEGETALE:
			return board.getNumRis().get(0) / 3;
		case ANIMALE: 
			return board.getNumRis().get(1) / 3;	
		case FUNGHI:
			return board.getNumRis().get(2) / 3;	
		case INSETTI:
			return board.getNumRis().get(3) / 3;
		}
		return 0;
	}
	
	public int countObject(Oggetto oggetto, ArrayList<Oggetto> diversi, Board board) {
		int piuma = 0;
		int inchiostro = 0;
		int pergamena = 0;
		
		if(oggetto != null) {
			switch(oggetto) {
			case PIUMA:
				return board.getNumOgg().get(0) / 2;
			case INCHIOSTRO:
				return board.getNumOgg().get(1) / 2;
			case PERGAMENA:
				return board.getNumOgg().get(2) / 2;
			}
		}
		
		if(diversi != null) {
			piuma = board.getNumOgg().get(0);
			inchiostro = board.getNumOgg().get(1);
			pergamena = board.getNumOgg().get(2);
			return (piuma + inchiostro + pergamena) / 3;
		}
		
		return 0;
	}
	
	public int countDisposition(Board board, Obiettivo obiettivo) {
		int counter = 0;
		
		for(int i = 0; i < board.getMatrix().length - 2; i++) {
			for (int j = 0; j < board.getMatrix()[i].length - 2; j++) {
				counter += this.scanDisposition(board.getMatrix(), obiettivo.getDisposizione(), i, j);
			}
		}
		return counter;
	}
   
	public int scanDisposition (String [][] mat, Regno [][] obiettivo, int i, int j) {
		boolean scanner = true;
		
		while(scanner || i < 3) {
			while(scanner || j < 3) {
				switch(obiettivo [i][j]) {
				case VEGETALE:
					if(!mat [i][j].contains("VR")) {
						scanner = false;
						return 0;
					}
					break;
				case ANIMALE:
					if(!mat [i][j].contains("BL")) {
						scanner = false;
						return 0;
					}
					break;
				case FUNGHI:
					if(!mat [i][j].contains("RS")) {
						scanner = false;
						return 0;
					}
					break;
				case INSETTI:
					if(!mat [i][j].contains("VL")) {
						scanner = false;
						return 0;
					}
					break;
				}
				j++;
			}
			i++;
		}
		return 1;
	}
	
	public void endGame() {
		view.endMessage();
		System.exit(0);
	}
}
