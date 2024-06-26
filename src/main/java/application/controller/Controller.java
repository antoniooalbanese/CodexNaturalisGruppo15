package application.controller;

import java.io.IOException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import com.google.gson.JsonSyntaxException;
import application.model.Angolo;
import application.model.Board;
import application.model.Carta;
import application.model.CartaIniziale;
import application.model.CartaObiettivo;
import application.model.CartaOro;
import application.model.CartaRisorsa;
import application.model.Giocatore;
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
 * Classe che gestisce l'andamento del gioco e l'interazione tra model
 * e view.
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
	 * @param model: modello del gioco
	 * @param view: view che stampa a schermo e prende in entrata le
	 * varie informazioni interagendo con gli utenti
	 */
	public Controller(Model model, View view) {
		this.model = model;
		this.view = view;
		this.num = 0;
	}
	
	/**
	 * Metodo che fa partire il gioco.
	 * @throws IOException quando la risposta non è tra quelle
	 * ammesse(SI o NO)
	 */
	public void startGame() throws IOException{
		
		if(!view.welcomeMessage()) {
			view.endMessage();
			System.exit(0);
		}
	}
	
	/**
	 * Metodo che ottiene il numero dei giocatori.
	 * @throws IOException quando la risposta non è tra quelle
	 * ammesse(un numero compreso tra 2 e 4 compresi)
	 */
	public void getPlayersNumber() throws IOException {
		num = view.getPlayersNumberMessage();
	}
	
	/**
	 * Metodo che inizializza i giocatori ad inizio gioco.
	 * @throws JsonSyntaxException quando non è rispettata la sintassi
	 * json
	 * @throws IOException quando il file non viene trovato nel 
	 * percorso indicato
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
	 * Metodo che estrae una carta dalla cima del mazzo e la posiziona
	 * sul campo di gioco mostrandola sul fronte.
	 * @param mazzo: mazzo da cui si estrae la carta
	 * @return carta estratta dalla cima del mazzo
	 */
	public Carta estrai(ArrayList<? extends Carta> mazzo){
		Carta carta = mazzo.get(0);
		mazzo.remove(0);
		return carta;
	}
	
	/**
	 * Metodo utilizzato per ottenere una carta in una determinata
	 * posizione. 
	 * @param i: posizione nel mazzo della carta che si vuole prendere
	 * @param mazzo: mazzo da cui si vuole prendere la carta 
	 * @return carta che si trova nella posizione data come parametro
	 */
	public Carta getCarta(int i, ArrayList<Carta> mazzo){
		return mazzo.get(i);
	}
	
	/**
	 * Metodo utilizzato per inizializzare il campo da gioco.
	 * @throws JsonSyntaxException quando non è rispettata la sintassi
	 * json
	 * @throws IOException quando il file non viene trovato nel 
	 * percorso indicato
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
	 * @throws JsonSyntaxException quando non è rispettata la sintassi
	 * json
	 * @throws IOException quando il file non viene trovato nel 
	 * percorso indicato
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
			
			this.model.getCampo().getGiocatore().get(i).getBoard().getMatrix()[4][4] = scelta.getId();
			this.count(this.model.getCampo().getGiocatore().get(i),scelta, null, null);
			this.pescaMazzo(this.model.getCampo().getGiocatore().get(i), this.model.getCampo().getMazzoR().getMazzoFronte());
			this.pescaMazzo(this.model.getCampo().getGiocatore().get(i), this.model.getCampo().getMazzoR().getMazzoFronte());
			this.pescaMazzo(this.model.getCampo().getGiocatore().get(i), this.model.getCampo().getMazzoO().getMazzoFronte());
			view.showHand(this.model.getCampo().getGiocatore().get(i).getNick(), this.model.getCampo().getGiocatore().get(i).getMano());
			
			while(!view.passaMano()) {
				
			}
		}	
	}
	
	/**
	 * Metodo che estrae sul campo di gioco le due carte obiettivo 
	 * generali e distribuisce le carte obiettivo segrete ai vari
	 * giocatori.
	 * @throws JsonSyntaxException quando il file json non rispetta
	 * la sintassi json
	 * @throws IOException quando non viene trovato il file sul 
	 * percorso indicato
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
	 * Metodo che sceglie l'ordine di gioco dei giocatori.
	 */
	public void chooseFirstPlayer() {
		Collections.shuffle(this.model.getCampo().getGiocatore());
		this.model.getCampo().getGiocatore().get(0).setInizio();
		view.showNewOrder(this.model.getCampo().getGiocatore());
	}
	
	/**
	 * Metodo che gestisce l'andamento della partita.
	 * @throws IOException quando la risposta non è tra quelle
	 * ammesse(SI o NO)
	 */
	public void playGame() throws IOException {
		view.startMessage();
		boolean last = false;
		boolean finish = false;
		
		while(!this.isGameOver(finish)) {
			
			for(int i = 0; i < this.num; i++) {
				
				if(!view.showDecksAreOverMessage(areDecksFinished())) {
				
					view.tellLastTurn(last); 
					view.tellWhoseTurn(this.model.getCampo().getGiocatore().get(i).getNick());
					view.showAllBoards(this.model.getCampo().getGiocatore().get(i), this.model.getCampo().getGiocatore());
					view.showField(this.model.getCampo());
					this.posiziona(this.model.getCampo().getGiocatore().get(i));
					view.showBoard(this.model.getCampo().getGiocatore().get(i));
					this.pesca(this.model.getCampo().getGiocatore().get(i));
					
					while(!view.passaMano()) {
						
					}
					
					this.model.getCampo().getGiocatore().get(i).getBoard().setTurno(this.model.getCampo().getGiocatore().get(i).getBoard().getTurno()+1);
					last = this.checkLastTurn(this.model.getCampo().getGiocatore().get(i));
				
				} else {
					finish = true;
				}
				
				if (last) {
					finish = last;
				}
			}
		}
		
		for(int i = 0; i < this.num; i++ ) {
			this.checkExtraPoint(this.model.getCampo().getGiocatore().get(i));
		}
		
		this.createRanking(this.model.getCampo().getGiocatore());
		view.isGameOverMessage();
		
		for(int i = 0; i < this.model.getCampo().getGiocatore().size(); i++ ) {
			view.showFinalPoints(this.model.getCampo().getGiocatore().get(i).getNick(), this.model.getCampo().getGiocatore().get(i).getBoard().getPunteggio(), i+1);
		}
		
		view.showWinner(this.getUltimateWinner(this.model.getCampo().getGiocatore()));
	}
	
	/**
	 * Metodo che controlla gli eventuali punti in più dati al
	 * giocatore al termine della partita se durante quest'ultima è
	 * riuscito a conseguire gli obiettivi descritti nelle carte 
	 * obiettivo, sia quelle comuni che quelle segrete.
	 * @param g: giocatore di cui si controllano gli eventuali punti
	 * in più
	 */
	public void checkExtraPoint(Giocatore g) {
		this.checkObjective(g);
	}
	
	/**
	 * Metodo che crea la classifica finale mettendo in ordine i
	 * giocatori in base al punteggio conseguito.
	 * @param giocatori: lista dei giocatori che hanno giocato la 
	 * partita
	 * @return lista di stringhe che rappresentano i nickname 
	 * dei giocatori in ordine di punteggio
	 */
	 public ArrayList<String> createRanking(ArrayList<Giocatore> giocatori) {
		Collections.sort(giocatori, new Comparator<Giocatore>() {
	           
	          public int compare(Giocatore g1, Giocatore g2) {
	              return Integer.compare(g2.getBoard().getPunteggio(), g1.getBoard().getPunteggio());
	          }
	    });

	      ArrayList<String> ranking = new ArrayList<>();
	      
	      for (Giocatore giocatore : giocatori) {
	          ranking.add(giocatore.getNick());
	      }
	      
	      return ranking;
	}
	
	 /**
	  * Metodo che ritorna il vincitore sotto forma di stringa che 
	  * rappresenta il suo nickname.
	  * @param g: lista dei giocatori ordinati per punteggio
	  * @return nickname del vincitore
	  */
	public String getUltimateWinner(ArrayList<Giocatore> g) {
		String winner = "";
		int dim = g.size();
		
		switch(dim) {
		case 2:
			if(g.get(0).getBoard().getPunteggio() > g.get(1).getBoard().getPunteggio()) {
				winner = g.get(0).getNick();
			} else if(g.get(0).getBoard().getNumObj() > g.get(1).getBoard().getNumObj()) {
				winner = g.get(0).getNick();
			} else if(g.get(0).getBoard().getNumObj() < g.get(1).getBoard().getNumObj()){
				winner = g.get(1).getNick();
			} else {
				winner = g.get(0).getNick() + " & " + g.get(1).getNick();
			}
			break;
		case 3:
			if(g.get(0).getBoard().getPunteggio() > g.get(1).getBoard().getPunteggio()) {
				winner = g.get(0).getNick();
			} 
			else if (g.get(1).getBoard().getPunteggio() > g.get(2).getBoard().getPunteggio()) {
				if (g.get(0).getBoard().getNumObj() > g.get(1).getBoard().getNumObj()) {
					winner = g.get(0).getNick();
				}else if(g.get(0).getBoard().getNumObj() < g.get(1).getBoard().getNumObj()){
					winner = g.get(1).getNick();
				}else {
					winner = g.get(0).getNick() + " & " + g.get(1).getNick();
				}
			}else if(g.get(0).getBoard().getNumObj() > g.get(1).getBoard().getNumObj()) {
				if(g.get(0).getBoard().getNumObj() > g.get(2).getBoard().getNumObj()) {
					winner = g.get(0).getNick();
				}else if(g.get(0).getBoard().getNumObj() < g.get(2).getBoard().getNumObj()) {
					winner = g.get(2).getNick();
				}else {
					winner = g.get(0).getNick() + " & " + g.get(2).getNick();
				}
			}else if(g.get(0).getBoard().getNumObj() < g.get(1).getBoard().getNumObj()) {
				if(g.get(1).getBoard().getNumObj() > g.get(2).getBoard().getNumObj()) {
					winner = g.get(1).getNick();
				}else if(g.get(1).getBoard().getNumObj() < g.get(2).getBoard().getNumObj()) {
					winner = g.get(2).getNick();
				}else {
					winner = g.get(1).getNick() + " & " + g.get(2).getNick();
				}
			}else if(g.get(0).getBoard().getNumObj() > g.get(2).getBoard().getNumObj()){
				winner = g.get(0).getNick() + " & " + g.get(1).getNick();
			}else if(g.get(0).getBoard().getNumObj() < g.get(2).getBoard().getNumObj()) {
				winner = g.get(2).getNick();
			}else {
				winner = g.get(0).getNick() + " & " + g.get(1).getNick() + " & " + g.get(2).getNick();
				
			}
			break;
		case 4:
			if(g.get(0).getBoard().getPunteggio() > g.get(1).getBoard().getPunteggio()) {
				winner = g.get(0).getNick();
			}else if(g.get(1).getBoard().getPunteggio() > g.get(2).getBoard().getPunteggio()) {
				if (g.get(0).getBoard().getNumObj() > g.get(1).getBoard().getNumObj()) {
					winner = g.get(0).getNick();
				}else if(g.get(0).getBoard().getNumObj() < g.get(1).getBoard().getNumObj()){
					winner = g.get(1).getNick();
				}else {
					winner = g.get(0).getNick() + " & " + g.get(1).getNick();
				}
			}else if(g.get(2).getBoard().getPunteggio() > g.get(3).getBoard().getPunteggio()) {
				if(g.get(0).getBoard().getNumObj() > g.get(1).getBoard().getNumObj()) {
					if(g.get(0).getBoard().getNumObj() > g.get(2).getBoard().getNumObj()) {
						winner = g.get(0).getNick();
					}else if(g.get(0).getBoard().getNumObj() < g.get(2).getBoard().getNumObj()) {
						winner = g.get(2).getNick();
					}else {
						winner = g.get(0).getNick() + " & " + g.get(2).getNick();
					}
				}else if(g.get(0).getBoard().getNumObj() < g.get(1).getBoard().getNumObj()) {
					if(g.get(1).getBoard().getNumObj() > g.get(2).getBoard().getNumObj()) {
						winner = g.get(1).getNick();
					}else if(g.get(1).getBoard().getNumObj() < g.get(2).getBoard().getNumObj()) {
						winner = g.get(2).getNick();
					}else {
						winner = g.get(1).getNick() + " & " + g.get(2).getNick();
					}
				}else if(g.get(0).getBoard().getNumObj() > g.get(2).getBoard().getNumObj()){
					winner = g.get(0).getNick() + " & " + g.get(1).getNick();
				}else if(g.get(0).getBoard().getNumObj() < g.get(2).getBoard().getNumObj()) {
					winner = g.get(2).getNick();
				}else {
					winner = g.get(0).getNick() + " & " + g.get(1).getNick() + " & " + g.get(2).getNick(); 
				}
			}else if(g.get(0).getBoard().getNumObj() > g.get(1).getBoard().getNumObj()) {
				if(g.get(0).getBoard().getNumObj() > g.get(2).getBoard().getNumObj()) {
					if(g.get(0).getBoard().getNumObj() > g.get(3).getBoard().getNumObj()) {
						winner = g.get(0).getNick();
					}else if(g.get(0).getBoard().getNumObj() < g.get(3).getBoard().getNumObj()) {
						winner = g.get(3).getNick();
					}else {
						winner = g.get(0).getNick() + " & " + g.get(3).getNick();
					}
				}else if(g.get(0).getBoard().getNumObj() < g.get(2).getBoard().getNumObj()) {
					if(g.get(2).getBoard().getNumObj() > g.get(3).getBoard().getNumObj()) {
						winner = g.get(2).getNick();
					}else if(g.get(2).getBoard().getNumObj() < g.get(3).getBoard().getNumObj()) {
						winner = g.get(3).getId();
					}else {
						winner = g.get(2).getNick() + " & " + g.get(3).getNick();
					}
				}else {
					if(g.get(0).getBoard().getNumObj() > g.get(3).getBoard().getNumObj()) {
						winner = g.get(0).getNick() + " & " + g.get(2).getNick();
					}else if(g.get(0).getBoard().getNumObj() < g.get(3).getBoard().getNumObj()) {
						winner = g.get(3).getNick();
					}else {
						winner = g.get(0).getNick() + " & " + g.get(2).getNick() + " & " + g.get(3).getNick();
					}
				}
			}else if(g.get(0).getBoard().getNumObj() < g.get(1).getBoard().getNumObj()) {
				if(g.get(1).getBoard().getNumObj() > g.get(2).getBoard().getNumObj()) {
					if(g.get(1).getBoard().getNumObj() > g.get(3).getBoard().getNumObj()) {
						winner = g.get(1).getNick();
					}else if(g.get(1).getBoard().getNumObj() < g.get(3).getBoard().getNumObj()) {
						winner = g.get(3).getNick();
					}else {
						winner = g.get(1).getNick() + " & " + g.get(3).getNick();
					}
				}else if(g.get(1).getBoard().getNumObj() < g.get(2).getBoard().getNumObj()) {
					if(g.get(2).getBoard().getNumObj() > g.get(3).getBoard().getNumObj()) {
						winner = g.get(2).getNick();
					}else if(g.get(2).getBoard().getNumObj() < g.get(3).getBoard().getNumObj()) {
						winner = g.get(3).getId();
					}else {
						winner = g.get(2).getNick() + " & " + g.get(3).getNick();
					}
				}else {
					if(g.get(1).getBoard().getNumObj() > g.get(3).getBoard().getNumObj()) {
						winner = g.get(1).getNick() + " & " + g.get(2).getNick();
					}else if(g.get(1).getBoard().getNumObj() < g.get(3).getBoard().getNumObj()) {
						winner = g.get(3).getNick();
					}else {
						winner = g.get(1).getNick() + " & " + g.get(2).getNick() + " & " + g.get(3).getNick();
					}
				}
			}else {
				if(g.get(0).getBoard().getNumObj() > g.get(2).getBoard().getNumObj()) {
					if(g.get(0).getBoard().getNumObj() > g.get(3).getBoard().getNumObj()) {
						winner = g.get(0).getNick() + " & " + g.get(1).getNick();
					}else if(g.get(0).getBoard().getNumObj() < g.get(3).getBoard().getNumObj()) {
						winner = g.get(3).getNick();
					}else {
						winner = g.get(0).getNick() + " & " + g.get(1).getNick() + " & " + g.get(3).getNick();
					}
				}else if(g.get(0).getBoard().getNumObj() < g.get(2).getBoard().getNumObj()) {
					if(g.get(2).getBoard().getNumObj() > g.get(3).getBoard().getNumObj()) {
						winner = g.get(2).getNick();
					}else if(g.get(2).getBoard().getNumObj() < g.get(3).getBoard().getNumObj()) {
						winner = g.get(3).getId();
					}else {
						winner = g.get(2).getNick() + " & " + g.get(3).getNick();
					}
				}else {
					if(g.get(0).getBoard().getNumObj() > g.get(3).getBoard().getNumObj()) {
						winner = g.get(0).getNick() + " & " + g.get(1).getNick() + " & " + g.get(2).getNick();
					}else if(g.get(0).getBoard().getNumObj() < g.get(3).getBoard().getNumObj()) {
						winner = g.get(3).getId();
					}else {
						winner = g.get(0).getNick() + " & " + g.get(1).getNick() + " & " + g.get(2).getNick() + " & " + g.get(3).getNick();
					}
				}
			}
			break;
		}
		
		return winner;
	}
	
	/**
	 * Metodo che controlla se il giocatore a fine del proprio turno ha raggiunto
	 * almeno i 20 punti per far terminare la partita.
	 * @param g: giocatore di cui si controlla il punteggio
	 * @return TRUE se il giocatore ha conseguito almeno 20 punti,
	 * FALSE se il giocatore ha conseguito memo di 20 punti
	 */
	public boolean checkLastTurn(Giocatore g) {
		
		if(g.getBoard().getPunteggio() >= 20) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Metodo che controlla se si sono verificate le condizioni per
	 * terminare la partita, ossia che almeno un giocatore abbia 
	 * raggiunto o superato 20 punti con il solo posizionamento delle
	 * carte.
	 * @param last: booleano che, se TRUE indica che almeno un 
	 * giocatore ha raggiunto o superato i 20 punti, mentre se FALSE,
	 * indica che nessun giocatore ha ancora raggiunto i 20 punti
	 * @return TRUE se sussistono le condizioni per terminare la 
	 * partita, FALSE se invece non sussistono
	 */
	public boolean isGameOver(boolean last) {
		
		if(!last && !this.areDecksFinished()) {
			return false;
		}
		
		return true;
	}
	
	/**
	 * Metodo che controlla se i due mazzi(oro e risorsa)
	 * sono terminati.
	 * @return TRUE se i mazzi sono terminati, FALSE se almeno uno 
	 * dei mazzi non è ancora terminato 
	 */
	public boolean areDecksFinished() {
		
		if(this.model.getCampo().getMazzoR().getMazzoFronte().isEmpty() && this.model.getCampo().getMazzoO().getMazzoFronte().isEmpty()) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Metodo che gestisce il posizionamento delle carte sulla board.
	 * @param g: giocatore di turno che sta posizionando le carte
	 * sulla board
	 * @throws IOException quando il codice inserito dal giocatore
	 * che indica la carta che lui vuole coprire non è tra quelli
	 * disponibili e quindi il codice non è valido in quanto la 
	 * carta non esiste oppure non può essere coperta da nessun'altra
	 * carta
	 */
	public void posiziona(Giocatore g) throws IOException {
		String scelta = null;
		Carta cartaScelta = null;
		ArrayList<CartaRisorsa> libereRisorsa = new ArrayList<CartaRisorsa>();
		ArrayList<CartaOro> libereOro = new ArrayList<CartaOro>();
		CartaIniziale liberaIniziale;
		boolean check = false;
		boolean verso;
		boolean checkReq = false;
		boolean sceltaGiusta = false;
		boolean checkCover = false;
		String riga = "";
		CartaIniziale cardI = null;
		CartaRisorsa cardR = null;
		CartaOro cardO = null;

		liberaIniziale = getFreeInitialCard(g.getBoard().getCentro());
		
		if(this.getFreeResourceCards(g.getBoard().getRisorsa()) != null) {
			libereRisorsa.addAll(this.getFreeResourceCards(g.getBoard().getRisorsa()));
		}else {
			libereRisorsa = null;
		}
		
		if(this.getFreeGoldCards(g.getBoard().getOro()) != null) {
			libereOro.addAll(this.getFreeGoldCards(g.getBoard().getOro()));
		}else {
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
		
		while(!check) {
			while(!checkReq) {
				try {
					while(!sceltaGiusta) {
						do {
							scelta = view.chooseWhatToPlace();
						}while (scelta.length() == 0);
						if(scelta.charAt(0)=='R') {
							for(int i = 0; i < g.getMano().getRisorsa().size(); i++) {
								if(scelta.equalsIgnoreCase(g.getMano().getRisorsa().get(i).getId())) {
									scelta=g.getMano().getRisorsa().get(i).getId();
									sceltaGiusta = true;
								}else if(i==g.getMano().getRisorsa().size() && !sceltaGiusta) {
									view.insertAValidCode();
								}
							}
						}else if(scelta.charAt(0)=='O') {
							for(int j=0; j < g.getMano().getOro().size(); j++) {
								if(scelta.equalsIgnoreCase(g.getMano().getOro().get(j).getId())) {
										scelta = g.getMano().getOro().get(j).getId();
										sceltaGiusta = true;
								}else if(j==g.getMano().getOro().size() && !sceltaGiusta) {
									view.insertAValidCode();
								}
							}
						}else {
							sceltaGiusta = false;
							view.insertAValidCode();
						}
					}
				
					while (!checkCover) {
					riga = view.chooseWhatToCover().toUpperCase();
					
						if(riga.equalsIgnoreCase(liberaIniziale.getId())) {
							cardI = liberaIniziale;	
							checkCover = true;	
						}
						
						if (libereRisorsa != null) {
							for(CartaRisorsa r: libereRisorsa) {
								if(riga.equalsIgnoreCase(r.getId())) {
									cardR = r;
									checkCover = true;	
								}
							}
						} 
						
						if (libereOro != null) {
							for(CartaOro o: libereOro) {
								if(riga.equalsIgnoreCase(o.getId())) {
									cardO = o;
									checkCover = true;
		
								}
							}
						}
						
						if(!checkCover) {
							view.insertAValidCode();
						}
					} 
						if(scelta.charAt(0)=='R') {
							if(view.chooseWhichSide(g.getNick(), g.getMano().getResourceById(scelta), this.model.getCampo().getMazzoR().getRetroCarta(g.getMano().getResourceById(scelta)))) {
								cartaScelta = g.getMano().getResourceById(scelta);
								checkReq = true;
							}else {
								verso = false;
								cartaScelta = setAngoliRetro(g.getMano().getResourceById(scelta), g.getMano().getResourceById(scelta).getAngoli());
								((CartaRisorsa) cartaScelta).setCentro(this.model.getCampo().getMazzoR().getCartaRetroByRegno(g.getMano().getResourceById(scelta).getRegno()).getCentro());
								((CartaRisorsa) cartaScelta).setPunto(null);
								cartaScelta.setFronte(verso);
								checkReq = true;
							}
						}else {
								if(view.chooseWhichSide(g.getNick(), g.getMano().getGoldById(scelta),this.model.getCampo().getMazzoO().getRetroCarta(g.getMano().getGoldById(scelta)))) {
									cartaScelta = g.getMano().getGoldById(scelta);
									if(this.checkRequirements(g, (CartaOro) cartaScelta)) {
										checkReq = true;
									} else {
										checkReq = false;
										view.showRequirementMessage();
										sceltaGiusta = false;
										
									}
								}else {
									verso = false;
									cartaScelta = setAngoliRetro(g.getMano().getGoldById(scelta), g.getMano().getGoldById(scelta).getAngoli());
									((CartaOro) cartaScelta).setCentro(this.model.getCampo().getMazzoO().getCartaRetroByRegno(g.getMano().getGoldById(scelta).getRegno()).getCentro());
									((CartaOro) cartaScelta).setPunto(null);							
									cartaScelta.setFronte(verso);
									checkReq= true;
								}
							}
						if(checkCover == false) {
							throw new IOException();
						}
						
					}catch(IOException e) {
						view.insertAValidCode();
					}
				if(!checkReq) {
					checkCover = false;
				}
			}
				
			if(checkReq) {
				view.showFreeCornersMessage();
				
				if(cardI != null) {
					view.showFreeInitialCorners(cardI, this.getFreeInitialCorners(cardI));
					if(this.checkPlaceInitial(g, scelta, cartaScelta, cardI, this.checkCorners(this.getFreeInitialCorners(cardI)))) {
						check = true;
					} else {
						check = false;
						checkReq = false;
						checkCover = false;
						sceltaGiusta = false;
					}
				}
				if(cardR != null) {
					view.showFreeResourceCorners(cardR, this.getFreeResourceCorners(cardR));
					if(this.checkPlaceResource(g, scelta, cartaScelta, cardR, this.checkCorners(this.getFreeResourceCorners(cardR)))) {
						check = true;
					} else {
						check = false;
						checkReq = false;
						checkCover = false;
						sceltaGiusta = false;
					}
				}else if(cardO != null) {
					view.showFreeGoldCorners(cardO, this.getFreeGoldCorners(cardO));
					if(this.checkPlaceGold(g, scelta, cartaScelta, cardO, this.checkCorners(this.getFreeGoldCorners(cardO)))) {
						check = true;
					}else {
						check = false;
						checkReq = false;
						checkCover = false;
						sceltaGiusta = false;
					}
				}
			}else {
				check = false;	
			}	
		}	
	}
	
	/**
	 * Metodo che cambia gli angoli della carta fronte con gli angoli 
	 * della carta retro.
	 * @param fronte: carta vista sul fronte
	 * @param angoliFronte: lista degli angoli della carta vista sul
	 * fronte
	 * @return carta fronte con gli angoli settati come se fosse 
	 * vista sul retro
	 */
	public Carta setAngoliRetro (Carta fronte, ArrayList<Angolo> angoliFronte){
		Angolo angolo;
		ArrayList<Angolo> nuoviAngoli = new ArrayList<Angolo>();
			
			for(int i=0; i<4;i++) {
				angolo =angoliFronte.get(i);
				if(angolo.getPos()==Posizione.ADX) {
					angolo.setTipo(TipoAngolo.VUOTO);
					nuoviAngoli.add(angolo);
				}else if(angolo.getPos()==Posizione.BDX) {
					angolo.setTipo(TipoAngolo.VUOTO);
					nuoviAngoli.add(angolo);
				}else if (angolo.getPos()==Posizione.BSX) {
					angolo.setTipo(TipoAngolo.VUOTO);
					nuoviAngoli.add(angolo);
				}else {
					angolo.setTipo(TipoAngolo.VUOTO);
					nuoviAngoli.add(angolo);
				}
			}
			
		if (fronte.getId().charAt(0)=='R') {
			((CartaRisorsa) fronte).setAngoli(nuoviAngoli);
		}else {
			((CartaOro) fronte).setAngoli(nuoviAngoli);
		}
		
		return fronte;
	}
	
	/**
	 * Metodo che controlla se la carta oro che il giocatore vuole
	 * posizionare rispetta i requisiti di posizionamento.
	 * @param g: giocatore di turno che vuole posizionare la carta
	 * @param oro: carta oro che il giocatore vuole posizionare
	 * @return TRUE se i requisiti di posizionamento della carta oro
	 * sono soddisfatti, FALSE se i requisiti di posizionamento non
	 * sono soddisfatti
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
				continue;
			case ANIMALE:
				ani++;
				continue;
			case FUNGHI:
				fun++;
				continue;
			case INSETTI:
				ins++;
				continue;
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
	
	/**
	 * Metodo che ritorna la posizione dell'angolo di una carta data
	 * in input dall'utente
	 * @param angoli: lista di angoli di una carta
	 * @return posizione dell'angolo di una carta scelta dal giocatore
	 * @throws IOException quando la posizione scelta dal giocatore
	 * è già occupata da un'altra carta
	 */
	public Posizione checkCorners(ArrayList<Angolo> angoli) throws IOException {
		
		while(true) {
			try{
				 Posizione pos = view.chooseWhichCorner();
				
				for(Angolo a: angoli) {
					if(a.getPos().toString().equalsIgnoreCase(pos.toString())) {
						return pos;
					}
				}
				
				throw new IOException();
				
			}catch(IOException e){
				view.isCornerAlreadyOccupied(angoli);
			}
		}
	}
	
	/**
	 * Metodo che controlla se è possibile piazzare un angolo sopra
	 * un altro in base alle regole del gioco(un angolo nascosto non 
	 * può coprire un angolo vuoto)
	 * @param ang: angolo della carta da posizionare
	 * @param coperto: angolo della carta da coprire
	 * @return TRUE se l'angolo della carta che si vuole posizionare
	 * può coprire l'angolo scelto dal giocatore della carta che si
	 * vuole coprire, FALSE se l'angolo della carta che si 
	 * vuole posizionare non può coprire l'angolo scelto dal giocatore
	 * della carta che si vuole coprire l
	 */
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
	 * Metodo che controlla che la carta possa essere realmente
	 * posizionata su una carta iniziale come vuole il giocatore.
	 * @param g: giocatore di turno che sta posizionando la carta
	 * @param scelta: codice identificativo della carta che il
	 * giocatore vuole posizionare
	 * @param cartaScelta: carta che si vuole posizionare
	 * @param coperta: carta che si vuole coprire
	 * @param angolo: posizione dell'angolo che si vuole coprire
	 * @return TRUE se la carta può essere posizionata come vuole
	 * il giocatore; FALSE, se la carta non può essere posizionata
	 * come vuole il giocatore
	 */
	public boolean checkPlaceInitial(Giocatore g, String scelta, Carta cartaScelta, CartaIniziale coperta, Posizione angolo) {
		
		switch(angolo) {
		case ADX:
			if(cartaScelta.getId().charAt(0) == 'R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.BSX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.BSX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.BSX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.BSX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}
		case BDX:
			if(cartaScelta.getId().charAt(0) == 'R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.ASX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.ASX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.ASX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.ASX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}
		case BSX:
			if(cartaScelta.getId().charAt(0) == 'R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.ADX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.ADX, angolo, scelta);
						break;
					} else {
						return false;
					}
				}
			}else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.ADX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.ADX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}
		case ASX:
			if(cartaScelta.getId().charAt(0) == 'R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.BDX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.BDX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.BDX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.BDX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	/**
	 * Metodo che controlla che la carta possa essere realmente 
	 * posizionata su una carta risorsa come vuole il giocatore.
	 * @param g: giocatore di turno che sta posizionando la carta
	 * @param scelta: codice identificativo della carta che il
	 * giocatore vuole posizionare
	 * @param cartaScelta: carta che si vuole posizionare
	 * @param coperta: carta che si vuole coprire
	 * @param angolo: posizione dell'angolo che si vuole coprire
	 * @return TRUE se la carta può essere posizionata come vuole
	 * il giocatore; FALSE, se la carta non può essere posizionata
	 * come vuole il giocatore
	 */
	public boolean checkPlaceResource(Giocatore g, String scelta, Carta cartaScelta, CartaRisorsa coperta, Posizione angolo) {
		
		switch(angolo) {
		case ADX:
			if(cartaScelta.getId().charAt(0) == 'R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.BSX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.BSX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.BSX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.BSX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}
		case BDX:
			if(cartaScelta.getId().charAt(0) == 'R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.ASX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.ASX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.ASX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.ASX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}
		case BSX:
			if(cartaScelta.getId().charAt(0) == 'R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.ADX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				} else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.ADX, angolo, scelta);
						break;
					} else {
						return false;
					}
				}
			} else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.ADX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.ADX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}
		case ASX:
			if(cartaScelta.getId().charAt(0) == 'R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.BDX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.BDX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.BDX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.BDX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Metodo che controlla che la carta possa essere realmente 
	 * posizionata su una carta oro come vuole il giocatore.
	 * @param g: giocatore di turno che sta posizionando la carta
	 * @param scelta: codice identificativo della carta che il
	 * giocatore vuole posizionare
	 * @param cartaScelta: carta che si vuole posizionare
	 * @param coperta: carta che si vuole coprire
	 * @param angolo: posizione dell'angolo che si vuole coprire
	 * @return TRUE se la carta può essere posizionata come vuole
	 * il giocatore; FALSE, se la carta non può essere posizionata
	 * come vuole il giocatore
	 */
	public boolean checkPlaceGold(Giocatore g, String scelta, Carta cartaScelta, CartaOro coperta, Posizione angolo) {
		
		switch(angolo) {
		case ADX:
			if(cartaScelta.getId().charAt(0) == 'R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.BSX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.BSX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.BSX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.BSX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}
		case BDX:
			if(cartaScelta.getId().charAt(0) == 'R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.ASX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.ASX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.ASX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.ASX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}
		case BSX:
			if(cartaScelta.getId().charAt(0) == 'R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.ADX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.ADX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.ADX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.ADX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}
		case ASX:
			if(cartaScelta.getId().charAt(0) == 'R') {
				if(!checkPlaceCondition(((CartaRisorsa) cartaScelta).getAngoloByPosizione(Posizione.BDX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaRisorsa) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaRisorsa) cartaScelta), coperta, Posizione.BDX, angolo, scelta);
						break;
					}else {
						return false;
					}
				}
			}else {
				if(!checkPlaceCondition(((CartaOro) cartaScelta).getAngoloByPosizione(Posizione.BDX),coperta.getAngoloByPosizione(angolo))) {
					return false;
				}else {
					if(checkPlace(((CartaOro) cartaScelta), coperta, g, angolo)) {
						this.placeCard(g, ((CartaOro) cartaScelta), coperta, Posizione.BDX, angolo, scelta);
						break;
					} else {
						return false;
					}
				}
			}
		}
		
		return true;
	}
	
	/**
	 * Metodo che verifica che tutti gli angoli che andrebbe a coprire
	 * la carta da posizionare rispettino le regole di posizionamento
	 * e una volta fatto ciò setta i link reciproci(il link dell'angolo
	 * che deve coprire della carta da posizionare diventa
	 * l'identificativo della carta da coprire e il link dell'angolo 
	 * da coprire della carta da coprire diventa l'identificativo della
	 * carta da cui verrà coperto).
	 * @param carta: carta che si vuole posizionare
	 * @param coperta: carta che si vuole coprire
	 * @param g: giocatore di turno che sta posizionando la carta
	 * @param angolo:  posizione dell'angolo che si vuole coprire
	 * @return TRUE se l'angolo della carta che si vuole posizionare
	 * può coprire l'angolo scelto dal giocatore della carta che si
	 * vuole coprire, FALSE se l'angolo della carta che si 
	 * vuole posizionare non può coprire l'angolo scelto dal giocatore
	 * della carta che si vuole coprire
	 */
	public boolean checkPlace (Carta carta, Carta coperta, Giocatore g, Posizione angolo) {
		Carta cartaDaCoprire;
		boolean checkCon = false;
		boolean check1 = false;
		boolean check2 = false;
		boolean check3 = false;
		
		for(int i = 0; i < g.getBoard().getMatrix().length; i++) {
			for(int j = 0; j < g.getBoard().getMatrix()[i].length; j++) {
				if(g.getBoard().getMatrix()[i][j] == null) {
					continue;
				}else {
					if(g.getBoard().getMatrix()[i][j].equals(coperta.getId())) {
						switch (angolo) {
						case ADX:
							if(g.getBoard().getMatrix()[i-2][j] == null) {
								check1 = true;
								checkCon = false;
							}else if(carta.getId().charAt(0) == 'R') {
								if((g.getBoard().getMatrix()[i - 2][j]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ASX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j])).getAngoloByPosizione(Posizione.BDX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								}else if((g.getBoard().getMatrix()[i - 2][j]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ASX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j])).getAngoloByPosizione(Posizione.BDX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ASX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j])).getAngoloByPosizione(Posizione.BDX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								}
							}else if(carta.getId().charAt(0) == 'O') {
								if((g.getBoard().getMatrix()[i - 2][j]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ASX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j])).getAngoloByPosizione(Posizione.BDX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								}else if((g.getBoard().getMatrix()[i - 2][j]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ASX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j])).getAngoloByPosizione(Posizione.BDX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ASX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j])).getAngoloByPosizione(Posizione.BDX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								} 
							}
							
							if(checkCon) {
								cartaDaCoprire = g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j].toString());
								if(carta.getId().charAt(0) == 'R') {
									if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.ASX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.BDX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.ASX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.BDX).setLink(carta.getId());
									}else {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.ASX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.BDX).setLink(carta.getId());
									}	
								}else if(carta.getId().charAt(0) == 'O') {
									if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.ASX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.BDX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.ASX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.BDX).setLink(carta.getId());
									}else {
										((CartaOro)carta).getAngoloByPosizione(Posizione.ASX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.BDX).setLink(carta.getId());
									}
								}
							}
							
							if(g.getBoard().getMatrix()[i - 2][j + 2] == null) {
								check2 = true;
								checkCon = false;
							}else if(carta.getId().charAt(0) == 'R') {
								if ((g.getBoard().getMatrix()[i - 2][j + 2]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ADX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j + 2])).getAngoloByPosizione(Posizione.BSX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}else if((g.getBoard().getMatrix()[i - 2][j + 2]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ADX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j + 2])).getAngoloByPosizione(Posizione.BSX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ADX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j + 2])).getAngoloByPosizione(Posizione.BSX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}
							}else if(carta.getId().charAt(0) == 'O') {
								if((g.getBoard().getMatrix()[i - 2][j + 2]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ADX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j + 2])).getAngoloByPosizione(Posizione.BSX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}else if((g.getBoard().getMatrix()[i - 2][j + 2]).charAt(0) == 'O') {
									if (checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ADX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j + 2])).getAngoloByPosizione(Posizione.BSX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ADX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j + 2])).getAngoloByPosizione(Posizione.BSX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								} 
							}
							
							if(checkCon) {
								cartaDaCoprire = g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j + 2].toString());
								if(carta.getId().charAt(0) == 'R') {
									if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.ADX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.BSX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.ADX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.BSX).setLink(carta.getId());
									}else {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.ADX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.BSX).setLink(carta.getId());
									}
								}else if(carta.getId().charAt(0) == 'O') {
									if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.ADX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.BSX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.ADX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.BSX).setLink(carta.getId());
									}else {
										((CartaOro)carta).getAngoloByPosizione(Posizione.ADX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.BSX).setLink(carta.getId());
									}
								}
							}
							
							if(g.getBoard().getMatrix()[i][j + 2] == null) {
								check3 = true;
								checkCon = false;
							}else if(carta.getId().charAt(0) == 'R') {
								if((g.getBoard().getMatrix()[i][j + 2]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BDX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i][j + 2])).getAngoloByPosizione(Posizione.ASX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								}else if((g.getBoard().getMatrix()[i][j + 2]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BDX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i][j + 2])).getAngoloByPosizione(Posizione.ASX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BDX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i][j + 2])).getAngoloByPosizione(Posizione.ASX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								}
							}else if(carta.getId().charAt(0) == 'O') {
								if((g.getBoard().getMatrix()[i][j + 2]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BDX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i][j + 2])).getAngoloByPosizione(Posizione.ASX))) {
										checkCon = true;
										check3 = true;
									} else {
										check3 = false;
									}
								}else if((g.getBoard().getMatrix()[i][j + 2]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BDX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i][j + 2])).getAngoloByPosizione(Posizione.ASX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BDX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i][j + 2])).getAngoloByPosizione(Posizione.ASX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								} 
							}
							
							if(checkCon) {
								cartaDaCoprire = g.getBoard().getByID(g.getBoard().getMatrix()[i][j + 2].toString());
								if(carta.getId().charAt(0) == 'R') {
									if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.BDX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.ASX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.BDX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.ASX).setLink(carta.getId());
									}else {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.BDX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.ASX).setLink(carta.getId());
									}
								}else if(carta.getId().charAt(0) == 'O') {
									if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.BDX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.ASX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.BDX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.ASX).setLink(carta.getId());
									}else {
										((CartaOro)carta).getAngoloByPosizione(Posizione.BDX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.ASX).setLink(carta.getId());
									}
								}
							}
							
							if(check1 && check2 && check3) {
								return true;
							} else {
								return false;
							}
							
						case BDX:
							if(g.getBoard().getMatrix()[i][j + 2] == null) {
								 check1 = true;
								 checkCon = false;
							}else if(carta.getId().charAt(0) == 'R') {
								if((g.getBoard().getMatrix()[i][j + 2]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ADX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i][j + 2])).getAngoloByPosizione(Posizione.BSX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								}else if((g.getBoard().getMatrix()[i][j + 2]).charAt(0) == 'O') {
									if (checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ADX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i][j + 2])).getAngoloByPosizione(Posizione.BSX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ADX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i][j + 2])).getAngoloByPosizione(Posizione.BSX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								}
							}else if(carta.getId().charAt(0) == 'O') {
								if((g.getBoard().getMatrix()[i][j + 2]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ADX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i][j + 2])).getAngoloByPosizione(Posizione.BSX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								}else if((g.getBoard().getMatrix()[i][j + 2]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ADX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i][j + 2])).getAngoloByPosizione(Posizione.BSX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ADX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i][j + 2])).getAngoloByPosizione(Posizione.BSX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								} 
							}
							
							if(checkCon) {
								cartaDaCoprire = g.getBoard().getByID(g.getBoard().getMatrix()[i][j + 2].toString());
								if(carta.getId().charAt(0) == 'R') {
									if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.ADX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.BSX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.ADX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.BSX).setLink(carta.getId());
									}else {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.ADX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.BSX).setLink(carta.getId());
									}
								}else if(carta.getId().charAt(0) == 'O') {
									if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.ADX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.BSX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.ADX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.BSX).setLink(carta.getId());
									}else {
										((CartaOro)carta).getAngoloByPosizione(Posizione.ADX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.BSX).setLink(carta.getId());
									}
								}
							}
							
							if(g.getBoard().getMatrix()[i + 2][j + 2] == null) {
								check2 = true;
								checkCon = false;
							}else if(carta.getId().charAt(0) == 'R') {
								if((g.getBoard().getMatrix()[i + 2][j + 2]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BDX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j + 2])).getAngoloByPosizione(Posizione.ASX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}else if((g.getBoard().getMatrix()[i + 2][j + 2]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BDX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j + 2])).getAngoloByPosizione(Posizione.ASX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BDX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i+2][j+2])).getAngoloByPosizione(Posizione.ASX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}
							}else if(carta.getId().charAt(0) == 'O') {
								if((g.getBoard().getMatrix()[i + 2][j + 2]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BDX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j + 2])).getAngoloByPosizione(Posizione.ASX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}else if((g.getBoard().getMatrix()[i + 2][j + 2]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BDX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j + 2])).getAngoloByPosizione(Posizione.ASX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BDX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j + 2])).getAngoloByPosizione(Posizione.ASX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								} 
							}
							
							if(checkCon) {
								cartaDaCoprire = g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j + 2].toString());
								if(carta.getId().charAt(0) == 'R') {
									if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.BDX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.ASX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.BDX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.ASX).setLink(carta.getId());
									}else {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.BDX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.ASX).setLink(carta.getId());
									}
								}else if(carta.getId().charAt(0) == 'O') {
									if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.BDX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.ASX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.BDX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.ASX).setLink(carta.getId());
									}else {
										((CartaOro)carta).getAngoloByPosizione(Posizione.BDX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.ASX).setLink(carta.getId());
									}
								}
							}
							
							if(g.getBoard().getMatrix()[i + 2][j] == null) {
								check3 = true;
								checkCon = false;
							}else if(carta.getId().charAt(0) == 'R') {
								if ((g.getBoard().getMatrix()[i + 2][j]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BSX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j])).getAngoloByPosizione(Posizione.ADX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								}else if((g.getBoard().getMatrix()[i + 2][j]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BSX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j])).getAngoloByPosizione(Posizione.ADX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BSX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j])).getAngoloByPosizione(Posizione.ADX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								}
							}else if(carta.getId().charAt(0) == 'O') {
								if((g.getBoard().getMatrix()[i + 2][j]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BSX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j])).getAngoloByPosizione(Posizione.ADX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								}else if((g.getBoard().getMatrix()[i + 2][j]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BSX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j])).getAngoloByPosizione(Posizione.ADX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BSX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j])).getAngoloByPosizione(Posizione.ADX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								} 
							}
							
							if(checkCon) {
								cartaDaCoprire = g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j].toString());
								if(carta.getId().charAt(0) == 'R') {
									if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.BSX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.ADX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.BSX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.ADX).setLink(carta.getId());
									}else {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.BSX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.ADX).setLink(carta.getId());
									}
								}else if(carta.getId().charAt(0) == 'O') {
									if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.BSX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.ADX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0)=='R') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.BSX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.ADX).setLink(carta.getId());
									}else {
										((CartaOro)carta).getAngoloByPosizione(Posizione.BSX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.ADX).setLink(carta.getId());
									}
								}
							}
							
							if(check1 && check2 && check3) {
								return true;
							} else {
								return false;
							}
							
						case BSX:
							if(g.getBoard().getMatrix()[i + 2][j] == null) {
								check1 = true;
								checkCon = false;
							}else if(carta.getId().charAt(0) == 'R') {
								if((g.getBoard().getMatrix()[i + 2][j]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BDX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j])).getAngoloByPosizione(Posizione.ASX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								}else if((g.getBoard().getMatrix()[i + 2][j]).charAt(0)=='O') {
									if (checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BDX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j])).getAngoloByPosizione(Posizione.ASX))) {
										checkCon = true;
										check1 = true;
									} else {
										check1 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BDX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j])).getAngoloByPosizione(Posizione.ASX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								}
							}else if(carta.getId().charAt(0) == 'O') {
								if((g.getBoard().getMatrix()[i + 2][j]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BDX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j])).getAngoloByPosizione(Posizione.ASX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								}else if((g.getBoard().getMatrix()[i + 2][j]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BDX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j])).getAngoloByPosizione(Posizione.ASX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BDX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j])).getAngoloByPosizione(Posizione.ASX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								} 
							}
							
							if(checkCon) {
								cartaDaCoprire = g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j].toString());
								if(carta.getId().charAt(0) == 'R') {
									if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.BDX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.ASX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.BDX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.ASX).setLink(carta.getId());
									}else {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.BDX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.ASX).setLink(carta.getId());
									}
								}else if(carta.getId().charAt(0) == 'O') {
									if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.BDX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.ASX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.BDX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.ASX).setLink(carta.getId());
									}else {
										((CartaOro)carta).getAngoloByPosizione(Posizione.BDX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.ASX).setLink(carta.getId());
									}
								}
							}
							
							if(g.getBoard().getMatrix()[i + 2][j - 2] == null) {
								check2 = true;
								checkCon = false;
							}else if(carta.getId().charAt(0) == 'R') {
								if((g.getBoard().getMatrix()[i + 2][j - 2]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BSX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j - 2])).getAngoloByPosizione(Posizione.ADX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}else if((g.getBoard().getMatrix()[i + 2][j - 2]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BSX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j - 2])).getAngoloByPosizione(Posizione.ADX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BSX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j - 2])).getAngoloByPosizione(Posizione.ADX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}
							}else if(carta.getId().charAt(0) == 'O') {
								if((g.getBoard().getMatrix()[i + 2][j - 2]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BSX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j - 2])).getAngoloByPosizione(Posizione.ADX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}else if((g.getBoard().getMatrix()[i + 2][j - 2]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BSX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j - 2])).getAngoloByPosizione(Posizione.ADX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BSX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j - 2])).getAngoloByPosizione(Posizione.ADX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								} 
							}
							
							if(checkCon) {
								cartaDaCoprire = g.getBoard().getByID(g.getBoard().getMatrix()[i + 2][j - 2].toString());
								if(carta.getId().charAt(0) == 'R') {
									if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.BSX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.ADX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.BSX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.ADX).setLink(carta.getId());
									}else {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.BSX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.ADX).setLink(carta.getId());
									}
								}else if (carta.getId().charAt(0) == 'O') {
									if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.BSX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.ADX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.BSX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.ADX).setLink(carta.getId());
									}else {
										((CartaOro)carta).getAngoloByPosizione(Posizione.BSX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.ADX).setLink(carta.getId());
									}
								}
							}
							
							if(g.getBoard().getMatrix()[i][j - 2] == null) {
								check3 = true;
								checkCon = false;
							}else if(carta.getId().charAt(0) == 'R') {
								if((g.getBoard().getMatrix()[i][j - 2]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ASX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i][j - 2])).getAngoloByPosizione(Posizione.BDX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								}else if((g.getBoard().getMatrix()[i][j - 2]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ASX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i][j - 2])).getAngoloByPosizione(Posizione.BDX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ASX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i][j - 2])).getAngoloByPosizione(Posizione.BDX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								}
							}else if(carta.getId().charAt(0) == 'O') {
								if((g.getBoard().getMatrix()[i][j - 2]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ASX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i][j - 2])).getAngoloByPosizione(Posizione.BDX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								}else if((g.getBoard().getMatrix()[i][j - 2]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ASX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i][j - 2])).getAngoloByPosizione(Posizione.BDX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ASX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i][j - 2])).getAngoloByPosizione(Posizione.BDX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								} 
							}
							
							if(checkCon) {
								cartaDaCoprire = g.getBoard().getByID(g.getBoard().getMatrix()[i][j - 2].toString());
								if(carta.getId().charAt(0) == 'R') {
									if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.ASX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.BDX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.ASX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.BDX).setLink(carta.getId());
									}else {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.ASX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.BDX).setLink(carta.getId());
									}
								}else if (carta.getId().charAt(0) == 'O') {
									if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.ASX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.BDX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.ASX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.BDX).setLink(carta.getId());
									}else {
										((CartaOro)carta).getAngoloByPosizione(Posizione.ASX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.BDX).setLink(carta.getId());
									}
								}
							}
							
							if(check1 && check2 && check3) {
								return true;
							}else {
								return false;
							}
							
						case ASX:
							if(g.getBoard().getMatrix()[i][j - 2] == null) {
								check1 = true;
								checkCon = false;
							}else if(carta.getId().charAt(0) == 'R') {
								if ((g.getBoard().getMatrix()[i][j - 2]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BSX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i][j - 2])).getAngoloByPosizione(Posizione.ADX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								}else if((g.getBoard().getMatrix()[i][j - 2]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BSX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i][j - 2])).getAngoloByPosizione(Posizione.ADX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.BSX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i][j - 2])).getAngoloByPosizione(Posizione.ADX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								}
							}else if(carta.getId().charAt(0) == 'O') {
								if((g.getBoard().getMatrix()[i][j - 2]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BSX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i][j - 2])).getAngoloByPosizione(Posizione.ADX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								}else if((g.getBoard().getMatrix()[i][j - 2]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BSX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i][j - 2])).getAngoloByPosizione(Posizione.ADX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.BSX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i][j - 2])).getAngoloByPosizione(Posizione.ADX))) {
										checkCon = true;
										check1 = true;
									}else {
										check1 = false;
									}
								} 
							}
							
							if(checkCon) {
								cartaDaCoprire = g.getBoard().getByID(g.getBoard().getMatrix()[i][j - 2].toString());
								if(carta.getId().charAt(0) == 'R') {
									if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.BSX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.ADX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.BSX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.ADX).setLink(carta.getId());
									}else {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.BSX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.ADX).setLink(carta.getId());
									}
								}else if (carta.getId().charAt(0) == 'O') {
									if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.BSX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.ADX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.BSX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.ADX).setLink(carta.getId());
									}else {
										((CartaOro)carta).getAngoloByPosizione(Posizione.BSX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.ADX).setLink(carta.getId());
									}
								}
							}
							
							if(g.getBoard().getMatrix()[i - 2][j - 2] == null) {
								check2 = true;
								checkCon = false;
							}else if(carta.getId().charAt(0) == 'R') {
								if((g.getBoard().getMatrix()[i - 2][j - 2]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ASX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j - 2])).getAngoloByPosizione(Posizione.BDX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}else if((g.getBoard().getMatrix()[i - 2][j - 2]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ASX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j - 2])).getAngoloByPosizione(Posizione.BDX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ASX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j - 2])).getAngoloByPosizione(Posizione.BDX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}
							}else if(carta.getId().charAt(0) == 'O') {
								if((g.getBoard().getMatrix()[i - 2][j - 2]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ASX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j - 2])).getAngoloByPosizione(Posizione.BDX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}else if((g.getBoard().getMatrix()[i - 2][j - 2]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ASX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j - 2])).getAngoloByPosizione(Posizione.BDX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ASX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j - 2])).getAngoloByPosizione(Posizione.BDX))) {
										checkCon = true;
										check2 = true;
									}else {
										check2 = false;
									}
								} 
							}
							
							if(checkCon) {
								cartaDaCoprire = g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j - 2].toString());
								if(carta.getId().charAt(0) == 'R') {
									if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.ASX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.BDX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.ASX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.BDX).setLink(carta.getId());
									}else {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.ASX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.BDX).setLink(carta.getId());
									}
								}else if (carta.getId().charAt(0) == 'O') {
									if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.ASX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.BDX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.ASX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.BDX).setLink(carta.getId());
									}else {
										((CartaOro)carta).getAngoloByPosizione(Posizione.ASX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.BDX).setLink(carta.getId());
									}
								}
							}
							
							if(g.getBoard().getMatrix()[i - 2][j] == null) {
								check3 = true;
								checkCon = false;
							}else if(carta.getId().charAt(0) == 'R') {
								if((g.getBoard().getMatrix()[i - 2][j]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ADX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j])).getAngoloByPosizione(Posizione.BSX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								}else if((g.getBoard().getMatrix()[i - 2][j]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ADX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j])).getAngoloByPosizione(Posizione.BSX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaRisorsa) carta).getAngoloByPosizione(Posizione.ADX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j])).getAngoloByPosizione(Posizione.BSX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								}
							}else if(carta.getId().charAt(0) == 'O') {
								if((g.getBoard().getMatrix()[i - 2][j]).charAt(0) == 'R') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ADX), ((CartaRisorsa) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j])).getAngoloByPosizione(Posizione.BSX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								}else if((g.getBoard().getMatrix()[i - 2][j]).charAt(0) == 'O') {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ADX), ((CartaOro) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j])).getAngoloByPosizione(Posizione.BSX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								}else {
									if(checkPlaceCondition(((CartaOro) carta).getAngoloByPosizione(Posizione.ADX), ((CartaIniziale) g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j])).getAngoloByPosizione(Posizione.BSX))) {
										checkCon = true;
										check3 = true;
									}else {
										check3 = false;
									}
								} 
							}
							
							if(checkCon) {
								cartaDaCoprire = g.getBoard().getByID(g.getBoard().getMatrix()[i - 2][j].toString());
								if(carta.getId().charAt(0) == 'R') {
									if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.ADX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.BSX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.ADX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.BSX).setLink(carta.getId());
									}else {
										((CartaRisorsa)carta).getAngoloByPosizione(Posizione.ADX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.BSX).setLink(carta.getId());
									}
								}else if(carta.getId().charAt(0) == 'O') {
									if(cartaDaCoprire.getId().charAt(0) == 'O') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.ADX).setLink(cartaDaCoprire.getId());
										((CartaOro)cartaDaCoprire).getAngoloByPosizione(Posizione.BSX).setLink(carta.getId());
									}else if(cartaDaCoprire.getId().charAt(0) == 'R') {
										((CartaOro)carta).getAngoloByPosizione(Posizione.ADX).setLink(cartaDaCoprire.getId());
										((CartaRisorsa)cartaDaCoprire).getAngoloByPosizione(Posizione.BSX).setLink(carta.getId());
									}else {
										((CartaOro)carta).getAngoloByPosizione(Posizione.ADX).setLink(cartaDaCoprire.getId());
										((CartaIniziale)cartaDaCoprire).getAngoloByPosizione(Posizione.BSX).setLink(carta.getId());
									}
								}
							}
							
							if(check1 && check2 && check3) {
								return true;
							}else {
								return false;
							}
						}
					}
				}
			}
		}
		
		return false;
	}
		
	
	/**
	 * Metodo che aggiunge alla lista di carte della board la carta
	 * piazzata e aggiorna la relativa matrice e i contatori.
	 * @param g: giocatore di turno che piazza la carta
	 * @param card: carta posizionata
	 * @param coperta: carta che viene coperta
	 * @param angoloPos: angolo della carta posizionata che copre
	 * @param angoloCop: angolo che viene coperto dalla carta
	 * posizionata
	 * @param scelta: identificativo della carta posizionata
	 */
	public void placeCard(Giocatore g, Carta card, Carta coperta, Posizione angoloPos, Posizione angoloCop, String scelta) {
		
		if(card.getId().charAt(0) == 'R') {
			if(coperta.getId().charAt(0) == 'R') {
				((CartaRisorsa)card).getAngoloByPosizione(angoloPos).setLink(coperta.getId());
				((CartaRisorsa)coperta).getAngoloByPosizione(angoloCop).setLink(card.getId());
			}else if(coperta.getId().charAt(0) == 'O') {
				((CartaRisorsa)card).getAngoloByPosizione(angoloPos).setLink(coperta.getId());
				((CartaOro)coperta).getAngoloByPosizione(angoloCop).setLink(card.getId());
			}else {
				((CartaRisorsa)card).getAngoloByPosizione(angoloPos).setLink(coperta.getId());
				((CartaIniziale)coperta).getAngoloByPosizione(angoloCop).setLink(card.getId());
			}
		}else if (coperta.getId().charAt(0) == 'O') {
			((CartaOro)card).getAngoloByPosizione(angoloPos).setLink(coperta.getId());
			((CartaOro)coperta).getAngoloByPosizione(angoloCop).setLink(card.getId());
			}else if(coperta.getId().charAt(0) == 'R') {
				((CartaOro)card).getAngoloByPosizione(angoloPos).setLink(coperta.getId());
				((CartaRisorsa)coperta).getAngoloByPosizione(angoloCop).setLink(card.getId());
			}else {
				((CartaOro)card).getAngoloByPosizione(angoloPos).setLink(coperta.getId());
				((CartaIniziale)coperta).getAngoloByPosizione(angoloCop).setLink(card.getId());
		}
		
		int r = 0;
		int c = 0;
		
		for(int i = 0; i < g.getBoard().getMatrix().length; i++) {
			for(int j = 0; j < g.getBoard().getMatrix()[i].length; j++) {
				if(g.getBoard().getMatrix()[i][j] != null) {
					if(g.getBoard().getMatrix()[i][j].equals(coperta.getId())){
						r = i;
						c = j;
					}
				}
			}
		}
		
		switch(angoloCop) {
		case ADX:
			g.getBoard().setMatrixElementByIndex(r - 1, c + 1, card.getId());
			break;
		case BDX:
			g.getBoard().setMatrixElementByIndex(r + 1, c + 1, card.getId());
			break;
		case BSX:
			g.getBoard().setMatrixElementByIndex(r + 1, c - 1, card.getId());
			break;
		case ASX:
			g.getBoard().setMatrixElementByIndex(r - 1, c - 1, card.getId());
			break;
		}
		
		int riga = (g.getBoard().getMatrix().length / 2) + 1;
		int colonna = (g.getBoard().getMatrix().length / 2) + 1;
		
		for(int i = 0; i < g.getBoard().getMatrix().length; i++) {
			for(int j = 0; j < g.getBoard().getMatrix()[i].length; j++) {
				if(g.getBoard().getMatrix()[i][j] != null) {
					if(g.getBoard().getMatrix()[i][j].equals(card.getId())) {
						riga = i;
						colonna = j;
					}
				}
			}
		}
		
		boolean delete = false;
		
		switch(angoloCop) {
		case ADX:
			if(riga == 1 || colonna == g.getBoard().getMatrix()[0].length - 2) {
				delete = true;
			}
			break;
		case BDX:
			if(riga == g.getBoard().getMatrix().length - 2 || colonna == g.getBoard().getMatrix()[0].length - 2) {
				delete = true;
			}
			break;
		case BSX:
			if(riga == g.getBoard().getMatrix().length - 2 || colonna == 1) {
				delete = true;
			}
			break;
		case ASX:
			if(riga == 1 || colonna == 1) {
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
		
		this.count(g, card, getCarteCoperte(g,coperta, angoloCop), angoloCop);
		this.countPoints(g.getBoard(), card);
		
		if(card.getId().charAt(0) == 'R') {
			g.getMano().getRisorsa().remove(g.getMano().getResourceById(scelta));
			g.getBoard().getRisorsa().add((CartaRisorsa) card);
		}else {
			g.getMano().getOro().remove(g.getMano().getGoldById(scelta));
			g.getBoard().getOro().add((CartaOro) card);
		}
	}
	
	/**
	 * Metodo che ritorna gli id delle carte che stanno per essere 
	 * coperte con il posizionamento.
	 * @param g: giocatore di turno che sta posizionando la carta
	 * @param coperta: carta scelta dal giocatore per essere coperta
	 * @param angolo: angolo che deve essere coperto della carta
	 * che deve essere coperta
	 * @return lista della carte che stanno per essere coperte dalla carta 
	 * che sta per essere posizionata 
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
							
							if(g.getBoard().getMatrix()[i - 2][j] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i - 2][j]);
							}else {
								carteCoperte.add("0");
							}
							
							if(g.getBoard().getMatrix()[i][j + 2] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i][j + 2]);
							}else {
								carteCoperte.add("0");
							}
							
							if(g.getBoard().getMatrix()[i - 2][j + 2] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i - 2][j + 2]);
							}else {
								carteCoperte.add("0");
							}
							
							break;
						case BDX:
							carteCoperte.add(coperta.getId());
							
							if(g.getBoard().getMatrix()[i + 2][j] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i + 2][j]);
							}else {
								carteCoperte.add("0");
							}
							
							if (g.getBoard().getMatrix()[i][j + 2] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i][j + 2]);
							} else {
								carteCoperte.add("0");
							}
							
							if (g.getBoard().getMatrix()[i + 2][j + 2] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i + 2][j + 2]);
							} else {
								carteCoperte.add("0");
							}
							
							break;
						case BSX:
							carteCoperte.add(coperta.getId());
							
							if(g.getBoard().getMatrix()[i + 2][j] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i + 2][j]);
							}else {
								carteCoperte.add("0");
							}
							
							if(g.getBoard().getMatrix()[i][j - 2] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i][j - 2]);
							}else {
								carteCoperte.add("0");
							}
							
							if(g.getBoard().getMatrix()[i + 2][j - 2] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i + 2][j - 2]);
							}else {
								carteCoperte.add("0");
							}
							
							break;
						case ASX:
							carteCoperte.add(coperta.getId());
							
							if(g.getBoard().getMatrix()[i - 2][j] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i - 2][j]);
							}else {
								carteCoperte.add("0");
							}
							
							if(g.getBoard().getMatrix()[i][j - 2] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i][j - 2]);
							}else {
								carteCoperte.add("0");
							}
							
							if(g.getBoard().getMatrix()[i - 2][j - 2] != null) {
								carteCoperte.add(g.getBoard().getMatrix()[i - 2][j - 2]);
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
	 * Metodo che aggiorna i vari contatori visualizzati sulla Board.
	 * @param g: giocatore di turno
	 * @param carta: carta posizionata
	 * @param carteCoperte: lista della carte coperte dalla carta 
	 * posizionata
	 * @param angolo: angolo coperto dalla carta posizionata
	 */
	public void count(Giocatore g, Carta carta, ArrayList<String> carteCoperte, Posizione angolo) {
		
		if(carteCoperte == null) {
			
			for(int i = 0; i < 4; i++) {
				
				if(((CartaIniziale)carta).getAngoli().get(i).getTipo().equals(TipoAngolo.RISORSA)) {
					countRisorsa(g, ((CartaIniziale)carta).getAngoli().get(i), true);
				}
			}
			
		}else {
			
			if(carta.getId().charAt(0) == 'R') {
				
				if(((CartaRisorsa) carta).getCentro() != null) {
					
					int sum = 0;
					
					switch(((CartaRisorsa) carta).getCentro()) {
					case VEGETALE:
						sum = g.getBoard().getNumRis().get(0);
						sum ++;
						g.getBoard().getNumRis().set(0, sum);
						break;
					case ANIMALE:
						sum = g.getBoard().getNumRis().get(1);
						sum ++;
						g.getBoard().getNumRis().set(1, sum);
						break;
					case FUNGHI:
						sum = g.getBoard().getNumRis().get(2);
						sum ++;
						g.getBoard().getNumRis().set(2, sum);
						break;
					case INSETTI:
						sum = g.getBoard().getNumRis().get(3);
						sum ++;
						g.getBoard().getNumRis().set(3, sum);
						break;
					}
					
				}else {
					
					for(int i = 0; i < 4; i++) {
						
						if(((CartaRisorsa)carta).getAngoli().get(i).getTipo().equals(TipoAngolo.RISORSA)) {
							countRisorsa(g, ((CartaRisorsa)carta).getAngoli().get(i), true);
						}else if (((CartaRisorsa)carta).getAngoli().get(i).getTipo().equals(TipoAngolo.OGGETTO)) {
							countOggetto(g, ((CartaRisorsa)carta).getAngoli().get(i), true);
						}
					}
				}
				
			}else if(carta.getId().charAt(0) == 'O') {
				
				if(((CartaOro) carta).getCentro() != null) {
					
					int sum = 0;
					
					switch(((CartaOro) carta).getCentro()) {
					case VEGETALE:
						sum = g.getBoard().getNumRis().get(0);
						sum ++;
						g.getBoard().getNumRis().set(0, sum);
						break;
					case ANIMALE:
						sum = g.getBoard().getNumRis().get(1);
						sum ++;
						g.getBoard().getNumRis().set(1, sum);
						break;
					case FUNGHI:
						sum = g.getBoard().getNumRis().get(2);
						sum ++;
						g.getBoard().getNumRis().set(2, sum);
						break;
					case INSETTI:
						sum = g.getBoard().getNumRis().get(3);
						sum ++;
						g.getBoard().getNumRis().set(3, sum);
						break;
					}
				}else {
					
					for(int i = 0; i<4; i++) {
						
						if(((CartaOro)carta).getAngoli().get(i).getTipo().equals(TipoAngolo.RISORSA)) {
							countRisorsa(g, ((CartaOro)carta).getAngoli().get(i), true);
						}else if(((CartaOro)carta).getAngoli().get(i).getTipo().equals(TipoAngolo.OGGETTO)) {
							countOggetto(g, ((CartaOro)carta).getAngoli().get(i), true);
						}
					}
				}
			}
			
			switch(angolo) {
			case ADX:
				
				for(int i = 0; i < carteCoperte.size(); i++) {
					
					if(i == 0) {
						
						if(carteCoperte.get(i).charAt(0) == 'I') {
							
							CartaIniziale cardI = (CartaIniziale) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardI.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardI.getAngoloByPosizione(Posizione.ADX), false);
							}else if(cardI.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardI.getAngoloByPosizione(Posizione.ADX), false);
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'R') {
							CartaRisorsa cardR = (CartaRisorsa) g.getBoard().getByID(carteCoperte.get(i));
							if(cardR.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardR.getAngoloByPosizione(Posizione.ADX), false);
								
							} else if(cardR.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardR.getAngoloByPosizione(Posizione.ADX), false);
								
							}
							
						}else if(carteCoperte.get(i).charAt(0)=='O') {
							
							CartaOro cardO = (CartaOro) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardO.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardO.getAngoloByPosizione(Posizione.ADX), false);
								
							}else if(cardO.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardO.getAngoloByPosizione(Posizione.ADX), false);
								
							}
						}
					}
					
					if(i == 1) {
						
						if(carteCoperte.get(i).charAt(0) == 'I') {
							
							CartaIniziale cardI = (CartaIniziale) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardI.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardI.getAngoloByPosizione(Posizione.BDX), false);
							}else if(cardI.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardI.getAngoloByPosizione(Posizione.BDX), false);	
							}
						}else if(carteCoperte.get(i).charAt(0) == 'R') {
							
							CartaRisorsa cardR = (CartaRisorsa) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardR.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardR.getAngoloByPosizione(Posizione.BDX), false);
							}else if(cardR.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardR.getAngoloByPosizione(Posizione.BDX), false);
							}	
						}else if(carteCoperte.get(i).charAt(0) == 'O') {
							
							CartaOro cardO = (CartaOro) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardO.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardO.getAngoloByPosizione(Posizione.BDX), false);
							}else if(cardO.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardO.getAngoloByPosizione(Posizione.BDX), false);	
							}
						}
					}
					
					if(i == 2) {
						
						if(carteCoperte.get(i).charAt(0) == 'I') {
							
							CartaIniziale cardI = (CartaIniziale) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardI.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardI.getAngoloByPosizione(Posizione.ASX), false);	
							}else if(cardI.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardI.getAngoloByPosizione(Posizione.ASX), false);	
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'R') {
							
							CartaRisorsa cardR = (CartaRisorsa) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardR.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardR.getAngoloByPosizione(Posizione.ASX), false);
							}else if(cardR.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardR.getAngoloByPosizione(Posizione.ASX), false);	
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'O') {
							
							CartaOro cardO = (CartaOro) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardO.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardO.getAngoloByPosizione(Posizione.ASX), false);	
							}else if(cardO.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardO.getAngoloByPosizione(Posizione.ASX), false);
							}
						}
					}
					
					if(i == 3) {
						
						if(carteCoperte.get(i).charAt(0) == 'I') {
							
							CartaIniziale cardI = (CartaIniziale) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardI.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardI.getAngoloByPosizione(Posizione.BSX), false);	
							}else if(cardI.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardI.getAngoloByPosizione(Posizione.BSX), false);	
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'R') {
							
							CartaRisorsa cardR = (CartaRisorsa) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardR.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardR.getAngoloByPosizione(Posizione.BSX), false);		
							}else if(cardR.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardR.getAngoloByPosizione(Posizione.BSX), false);		
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'O') {
							
							CartaOro cardO = (CartaOro) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardO.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardO.getAngoloByPosizione(Posizione.BSX), false);	
							}else if(cardO.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardO.getAngoloByPosizione(Posizione.BSX), false);
							}
						}	
					}
				}
				
				break;
			case BDX:
				
				for(int i = 0; i < carteCoperte.size(); i++) {
					
					if(i == 0) {
						
						if(carteCoperte.get(i).charAt(0) == 'I') {
							
							CartaIniziale cardI = (CartaIniziale) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardI.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardI.getAngoloByPosizione(Posizione.BDX), false);
							}else if(cardI.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardI.getAngoloByPosizione(Posizione.BDX), false);
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'R') {
							
							CartaRisorsa cardR = (CartaRisorsa) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardR.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardR.getAngoloByPosizione(Posizione.BDX), false);	
							}else if(cardR.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardR.getAngoloByPosizione(Posizione.BDX), false);
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'O') {
							
							CartaOro cardO = (CartaOro) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardO.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardO.getAngoloByPosizione(Posizione.BDX), false);				
							}else if(cardO.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardO.getAngoloByPosizione(Posizione.BDX), false);					
							}
						}	
					}
					
					if(i == 1) {
						
						if(carteCoperte.get(i).charAt(0) == 'I') {
							
							CartaIniziale cardI = (CartaIniziale) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardI.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardI.getAngoloByPosizione(Posizione.ADX), false);		
							} else if (cardI.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardI.getAngoloByPosizione(Posizione.ADX), false);
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'R') {
							
							CartaRisorsa cardR = (CartaRisorsa) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardR.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardR.getAngoloByPosizione(Posizione.ADX), false);
							} else if (cardR.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardR.getAngoloByPosizione(Posizione.ADX), false);	
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'O') {
							
							CartaOro cardO = (CartaOro) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardO.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardO.getAngoloByPosizione(Posizione.ADX), false);	
							} else if (cardO.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardO.getAngoloByPosizione(Posizione.ADX), false);	
							}
						}
					}
					
					if(i == 2) {
						
						if(carteCoperte.get(i).charAt(0) == 'I') {
							
							CartaIniziale cardI = (CartaIniziale) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardI.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardI.getAngoloByPosizione(Posizione.BSX), false);	
							} else if (cardI.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardI.getAngoloByPosizione(Posizione.BSX), false);	
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'R') {
							
							CartaRisorsa cardR = (CartaRisorsa) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardR.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardR.getAngoloByPosizione(Posizione.BSX), false);	
							}else if (cardR.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardR.getAngoloByPosizione(Posizione.BSX), false);	
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'O') {
							
							CartaOro cardO = (CartaOro) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardO.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardO.getAngoloByPosizione(Posizione.BSX), false);
							}else if (cardO.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardO.getAngoloByPosizione(Posizione.BSX), false);	
							}
						}
					}
					
					if(i == 3) {
						
						if(carteCoperte.get(i).charAt(0) == 'I') {
							
							CartaIniziale cardI = (CartaIniziale) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardI.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardI.getAngoloByPosizione(Posizione.ASX), false);	
							}else if (cardI.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardI.getAngoloByPosizione(Posizione.ASX), false);	
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'R') {
							
							CartaRisorsa cardR = (CartaRisorsa) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardR.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardR.getAngoloByPosizione(Posizione.ASX), false);	
							}else if(cardR.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardR.getAngoloByPosizione(Posizione.ASX), false);	
							}
							
						}else if(carteCoperte.get(i).charAt(0)=='O') {
							CartaOro cardO = (CartaOro) g.getBoard().getByID(carteCoperte.get(i));
							if(cardO.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardO.getAngoloByPosizione(Posizione.ASX), false);	
							}else if(cardO.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardO.getAngoloByPosizione(Posizione.ASX), false);	
							}
						}
					}
				}
				
				break;
			case BSX:
				
				for(int i = 0; i < carteCoperte.size(); i++) {
					
					if(i == 0) {
						
						if(carteCoperte.get(i).charAt(0) == 'I') {
							
							CartaIniziale cardI = (CartaIniziale) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardI.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardI.getAngoloByPosizione(Posizione.BSX), false);	
							}else if(cardI.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardI.getAngoloByPosizione(Posizione.BSX), false);
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'R') {
							
							CartaRisorsa cardR = (CartaRisorsa) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardR.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardR.getAngoloByPosizione(Posizione.BSX), false);	
							} else if (cardR.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardR.getAngoloByPosizione(Posizione.BSX), false);
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'O') {
							
							CartaOro cardO = (CartaOro) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardO.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardO.getAngoloByPosizione(Posizione.BSX), false);			
							}else if(cardO.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardO.getAngoloByPosizione(Posizione.BSX), false);	
							}
						}
					}
					
					if (i == 1) {
						
						if(carteCoperte.get(i).charAt(0) == 'I') {
							
							CartaIniziale cardI = (CartaIniziale) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardI.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardI.getAngoloByPosizione(Posizione.ASX), false);	
							}else if(cardI.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardI.getAngoloByPosizione(Posizione.ASX), false);	
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'R') {
							
							CartaRisorsa cardR = (CartaRisorsa) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardR.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardR.getAngoloByPosizione(Posizione.ASX), false);
							}else if(cardR.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardR.getAngoloByPosizione(Posizione.ASX), false);	
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'O') {
							
							CartaOro cardO = (CartaOro) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardO.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardO.getAngoloByPosizione(Posizione.ASX), false);	
							}else if(cardO.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardO.getAngoloByPosizione(Posizione.ASX), false);	
							}
						}
					}
					
					if(i == 2) {
						
						if(carteCoperte.get(i).charAt(0) == 'I') {
							
							CartaIniziale cardI = (CartaIniziale) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardI.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardI.getAngoloByPosizione(Posizione.BDX), false);		
							}else if(cardI.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardI.getAngoloByPosizione(Posizione.BDX), false);
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'R') {
							
							CartaRisorsa cardR = (CartaRisorsa) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardR.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardR.getAngoloByPosizione(Posizione.BDX), false);	
							}else if(cardR.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardR.getAngoloByPosizione(Posizione.BDX), false);
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'O') {
							
							CartaOro cardO = (CartaOro) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardO.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardO.getAngoloByPosizione(Posizione.BDX), false);
							}else if(cardO.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardO.getAngoloByPosizione(Posizione.BDX), false);
							}
						}
					}
					
					if(i == 3) {
						
						if(carteCoperte.get(i).charAt(0) == 'I') {
							
							CartaIniziale cardI = (CartaIniziale) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardI.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardI.getAngoloByPosizione(Posizione.ADX), false);
							}else if(cardI.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardI.getAngoloByPosizione(Posizione.ADX), false);	
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'R') {
							
							CartaRisorsa cardR = (CartaRisorsa) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardR.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardR.getAngoloByPosizione(Posizione.ADX), false);	
							}else if(cardR.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardR.getAngoloByPosizione(Posizione.ADX), false);	
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'O') {
							
							CartaOro cardO = (CartaOro) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardO.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardO.getAngoloByPosizione(Posizione.ADX), false);		
							}else if(cardO.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardO.getAngoloByPosizione(Posizione.ADX), false);	
							}
						}
					}
				}
				
				break;
			case ASX:
				
				for(int i = 0; i < carteCoperte.size(); i++) {
					
					if(i == 0) {
						
						if(carteCoperte.get(i).charAt(0) == 'I') {
							
							CartaIniziale cardI = (CartaIniziale) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardI.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardI.getAngoloByPosizione(Posizione.ASX), false);
							}else if(cardI.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardI.getAngoloByPosizione(Posizione.ASX), false);
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'R') {
							
							CartaRisorsa cardR = (CartaRisorsa) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardR.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardR.getAngoloByPosizione(Posizione.ASX), false);	
							}else if(cardR.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardR.getAngoloByPosizione(Posizione.ASX), false);
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'O') {
							
							CartaOro cardO = (CartaOro) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardO.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardO.getAngoloByPosizione(Posizione.ASX), false);
							} else if (cardO.getAngoloByPosizione(Posizione.ASX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardO.getAngoloByPosizione(Posizione.ASX), false);	
							}
						}
						
					}
					
					if(i == 1) {
						
						if(carteCoperte.get(i).charAt(0) == 'I') {
							
							CartaIniziale cardI = (CartaIniziale) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardI.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardI.getAngoloByPosizione(Posizione.BSX), false);
							}else if(cardI.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardI.getAngoloByPosizione(Posizione.BSX), false);	
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'R') {
							
							CartaRisorsa cardR = (CartaRisorsa) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardR.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardR.getAngoloByPosizione(Posizione.BSX), false);	
							}else if(cardR.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardR.getAngoloByPosizione(Posizione.BSX), false);	
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'O') {
							
							CartaOro cardO = (CartaOro) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardO.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardO.getAngoloByPosizione(Posizione.BSX), false);	
							}else if(cardO.getAngoloByPosizione(Posizione.BSX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardO.getAngoloByPosizione(Posizione.BSX), false);
							}
						}
					}
					
					if(i == 2) {
						
						if(carteCoperte.get(i).charAt(0) == 'I') {
							
							CartaIniziale cardI = (CartaIniziale) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardI.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardI.getAngoloByPosizione(Posizione.ADX), false);	
							}else if(cardI.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardI.getAngoloByPosizione(Posizione.ADX), false);	
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'R') {
							
							CartaRisorsa cardR = (CartaRisorsa) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardR.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardR.getAngoloByPosizione(Posizione.ADX), false);
							} else if (cardR.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardR.getAngoloByPosizione(Posizione.ADX), false);
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'O') {
							
							CartaOro cardO = (CartaOro) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardO.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardO.getAngoloByPosizione(Posizione.ADX), false);
							}else if(cardO.getAngoloByPosizione(Posizione.ADX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardO.getAngoloByPosizione(Posizione.ADX), false);	
							}
						}
					}
					
					if(i == 3) {
						
						if(carteCoperte.get(i).charAt(0) == 'I') {
							
							CartaIniziale cardI = (CartaIniziale) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardI.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardI.getAngoloByPosizione(Posizione.BDX), false);	
							}else if(cardI.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardI.getAngoloByPosizione(Posizione.BDX), false);	
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'R') {
							
							CartaRisorsa cardR = (CartaRisorsa) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardR.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardR.getAngoloByPosizione(Posizione.BDX), false);	
							}else if(cardR.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardR.getAngoloByPosizione(Posizione.BDX), false);
							}
							
						}else if(carteCoperte.get(i).charAt(0) == 'O') {
							
							CartaOro cardO = (CartaOro) g.getBoard().getByID(carteCoperte.get(i));
							
							if(cardO.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.RISORSA)) {
								countRisorsa(g, cardO.getAngoloByPosizione(Posizione.BDX), false);	
							}else if(cardO.getAngoloByPosizione(Posizione.BDX).getTipo().equals(TipoAngolo.OGGETTO)) {
								countOggetto(g, cardO.getAngoloByPosizione(Posizione.BDX), false);	
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
	 * @param g: giocatore di turno
	 * @param angolo: angolo di cui si controlla il regno a cui 
	 * appartiene la risorsa che contiene
	 * @param somma: booleano che indica se il contatore deve essere
	 * aggiornato in positivo(TRUE) o in negativo(FALSE)
	 */
	public void countRisorsa(Giocatore g, Angolo angolo, boolean somma) {
		
		if (somma) {
			
			int sum = 0;
			
			switch (angolo.getRisorsa()) {
			case VEGETALE:
				sum = g.getBoard().getNumRis().get(0);
				sum ++;
				g.getBoard().getNumRis().set(0, sum);
				break;
			case ANIMALE:
				sum = g.getBoard().getNumRis().get(1);
				sum ++;
				g.getBoard().getNumRis().set(1, sum);
				break;
			case FUNGHI:
				sum = g.getBoard().getNumRis().get(2);
				sum ++;
				g.getBoard().getNumRis().set(2, sum);
				break;
			case INSETTI:
				sum = g.getBoard().getNumRis().get(3);
				sum ++;
				g.getBoard().getNumRis().set(3, sum);
				break;
			default:
				break;
			}
		}else {
			
			int dif = 0;
			
			switch (angolo.getRisorsa()) {
			case VEGETALE:
				dif = g.getBoard().getNumRis().get(0);
				dif --;
				g.getBoard().getNumRis().set(0, dif);
				break;
			case ANIMALE:
				dif = g.getBoard().getNumRis().get(1);
				dif --;
				g.getBoard().getNumRis().set(1, dif);
				break;
			case FUNGHI:
				dif = g.getBoard().getNumRis().get(2);
				dif --;
				g.getBoard().getNumRis().set(2, dif);
				break;
			case INSETTI:
				dif = g.getBoard().getNumRis().get(3);
				dif --;
				g.getBoard().getNumRis().set(3, dif);
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * Metodo che aggiorna il contatore degli oggetti.
	 * @param g: giocatore di turno
	 * @param angolo: angolo di cui si controlla il regno a cui 
	 * appartiene la risorsa che contiene
	 * @param somma: booleano che indica se il contatore deve essere
	 * aggiornato in positivo(TRUE) o in negativo(FALSE)
	 */
	public void countOggetto(Giocatore g, Angolo angolo, boolean somma) {
		
		if (somma) {
			
			int sum = 0;
			
			switch (angolo.getOggetto()) {
			case PIUMA:
				sum = g.getBoard().getNumOgg().get(0);
				sum ++;
				g.getBoard().getNumOgg().set(0, sum);
				break;
			case INCHIOSTRO:
				sum = g.getBoard().getNumOgg().get(1);
				sum ++;
				g.getBoard().getNumOgg().set(1, sum);
				break;
			case PERGAMENA:
				sum = g.getBoard().getNumOgg().get(2);
				sum ++;
				g.getBoard().getNumOgg().set(2, sum);
				break;
			default:
				break;
			}
		}else {
			
			int dif = 0;
			
			switch (angolo.getOggetto()) {
			case PIUMA:
				dif = g.getBoard().getNumOgg().get(0);
				dif --;
				g.getBoard().getNumOgg().set(0, dif);
				break;
			case INCHIOSTRO:
				dif = g.getBoard().getNumOgg().get(1);
				dif --;
				g.getBoard().getNumOgg().set(1, dif);
				break;
			case PERGAMENA:
				dif = g.getBoard().getNumOgg().get(2);
				dif --;
				g.getBoard().getNumOgg().set(2, dif);
				break;
			default:
				break;
			}
		}
	}
	
	/**
	 * Metodo che conta i punti asegnati da una carta al suo
	 * posizionamento.
	 * @param board: carte in campo del giocatore di turno
	 * @param carta: carta posizionata di cui si controllano i punti
	 * asseganti
	 */
	public void countPoints(Board board, Carta carta) {
		int punto = 0;
		int numLink = 0;
		
		if(carta.getId().charAt(0) == 'R') {
			
			if(((CartaRisorsa)carta).getPunto() != null) {
				punto = ((CartaRisorsa)carta).getPunto().getSomma();
			}
			
		}else if(((CartaOro)carta).getPunto() != null) {
			
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
					break;
				case INCHIOSTRO:
					punto = board.getNumOgg().get(1) * ((CartaOro)carta).getPunto().getSomma();
					break;
				case PERGAMENA:
					punto = board.getNumOgg().get(2) * ((CartaOro)carta).getPunto().getSomma();
					break;
				}
			}
		}
		board.setPunteggio(board.getPunteggio() + punto);
	}
	
	/**
	 * Metodo che permette al giocatore di turno di pescare una carta
	 * da uno dei due mazzi oppure una delle 4 carte scoperte sul campo
	 * di gioco.
	 * @param g: giocatore di turno che deve pescare
	 * @throws IOException quando l'identificativo inserito dal
	 * giocatore non corrisponde a nessuno dei codici disponibili e
	 * quindi non è valido
	 */
	public void pesca(Giocatore g) throws IOException {
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
				
			}catch(IOException e) {
				view.insertAValidCode();
			}
		}
	}
	
	/**
	 * Metodo che permette al giocatore di pescare una carta da
	 * un determinato mazzo.
	 * @param giocatore: giocatore di turno che pesca la carta dal
	 * mazzo
	 * @param mazzo: mazzo da cui il giocatore di turno pesca la carta
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
	 * Metodo che ritorna la carta iniziale della board nel caso in cui
	 * quest'ultima abbia almeno un angolo che può essere coperto.
	 * @param carta: carta iniziale posizionata sulla board 
	 * @return carta iniziale nel caso in cui abbia almeno un angolo
	 * che può essere coperto, altrimenti null
	 */
	public CartaIniziale getFreeInitialCard(CartaIniziale carta) {
		
		if(this.getFreeInitialCorners(carta)==null) {
			return null;
		}else {
			return carta;
		}
	}
	
	/**
	 * Metodo che ritorna le carte risorsa posizionate che hanno angoli 
	 * liberi che possono essere coperti da angoli di altre carte.
	 * @param card: lista delle carte risorsa presenti sulla board
	 * @return lista di carte risorsa che hanno almeno un angolo
	 * che può essere coperto
	 */
	public ArrayList<CartaRisorsa> getFreeResourceCards(ArrayList<CartaRisorsa> card){
		ArrayList<CartaRisorsa> free = new ArrayList<CartaRisorsa>();
		boolean empty = false;
		
		if(card == null) {
			return null;
		}
		
		for(int i = 0; i < card.size(); i++) {
			
			for(int j = 0; j < 4; j++) {
				
				if(this.getFreeResourceCorners(card.get(i)) != null) {
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
	 * Metodo che ritorna le carte oro posizionate che hanno angoli 
	 * liberi che possono essere coperti da angoli di altre carte.
	 * @param card: lista delle carte oro presenti sulla board
	 * @return lista di carte oro che hanno almeno un angolo
	 * che può essere coperto
	 */
	public ArrayList<CartaOro> getFreeGoldCards(ArrayList<CartaOro> card){
		ArrayList<CartaOro> free = new ArrayList<CartaOro>();
		boolean empty = false;
		
		if (card == null) {
			return null;
		}
		
		for(int i = 0; i < card.size(); i++) {
			
			for(int j = 0; j < 4; j++) {
			
				if(this.getFreeGoldCorners(card.get(i)) != null) {
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
	 * Metodo che ritorna la lista di angoli di una carta iniziale 
	 * che possono essere coperti possono essere coperti da angoli
	 * di altre carte.
	 * @param card: carta iniziale di cui si controllano gli angoli
	 * @return lista di angoli di una carta iniziale che possono
	 * essere coperti
	 */
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
	 * Metodo che ritorna tutti gli angoli di una carta risorsa che 
	 * possono essere coperti.
	 * @param card: carta risorsa di cui si controllano gli angoli
	 * @return lista di angoli di una carta risorsa che possono
	 * essere coperti
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
	 * Metodo che ritorna tutti gli angoli di una carta oro che 
	 * possono essere coperti.
	 * @param card: carta oro di cui si controllano gli angoli
	 * @return lista di angoli di una carta oro che possono
	 * essere coperti
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
	
	/**
	 * Metodo che controlla se un giocatore ha conseguito degli
	 * obiettivi durante la partita.
	 * @param g: giocatore di cui si controlla il conseguimento degli
	 * obiettivi
	 */
	public void checkObjective(Giocatore g) {
		
		for(int i = 0; i <= 2; i++) {
			
			if (i == 0) {
				Board board = g.getBoard();
				CartaObiettivo cartaObiettivo = board.getObiettivo();
			    Obiettivo obiettivo = cartaObiettivo.getObiettivo();
			    int punto = cartaObiettivo.getPunto();
			    
			    switch (obiettivo.getTipo()) {
		        case RISORSA:    
		            int puntiRisorsa = (countResource(obiettivo.getRisorsa(), g) * punto);
		            board.setPunteggio(board.getPunteggio() + puntiRisorsa);
		            break;
		        case OGGETTO:
		            List<Oggetto> oggetti = obiettivo.getOggetto();
		            
		            if (oggetti.size() >= 2) {
		            	
		                if (oggetti.get(0).equals(Oggetto.PIUMA) && oggetti.get(1).equals(Oggetto.PIUMA)) {
		                	int puntiOggetto = (countObject(Oggetto.PIUMA, null, g) * punto);
		                    board.setPunteggio(board.getPunteggio() + puntiOggetto);
		                }else if (oggetti.get(0).equals(Oggetto.INCHIOSTRO) && oggetti.get(1).equals(Oggetto.INCHIOSTRO)) {
		                	int puntiOggetto = (countObject(Oggetto.INCHIOSTRO, null, g) * punto);
		                    board.setPunteggio(board.getPunteggio() + puntiOggetto);
		                }else if (oggetti.get(0).equals(Oggetto.PERGAMENA) && oggetti.get(1).equals(Oggetto.PERGAMENA)) {
		                	int puntiOggetto = (countObject(Oggetto.PERGAMENA, null, g) * punto);
		                    board.setPunteggio(board.getPunteggio() + puntiOggetto);
		                }else if (oggetti.size() >= 3 && oggetti.get(0).equals(Oggetto.PIUMA) && oggetti.get(1).equals(Oggetto.INCHIOSTRO) && oggetti.get(2).equals(Oggetto.PERGAMENA)) {
		                	int puntiOggetto = (countObject(null, oggetti, g) * punto);
		                    board.setPunteggio(board.getPunteggio() + puntiOggetto);
		                }
		            }
		            
		            break;
		        case DISPOSIZIONE:
		        	int puntiDisposizione = conteggioDisp(g, obiettivo, cartaObiettivo) * punto;
		            board.setPunteggio(board.getPunteggio() + puntiDisposizione);    
		            break;
			    }
			    
			}else if (i == 1) {
				Board board = g.getBoard();
				CartaObiettivo cartaObiettivo = this.model.getCampo().getObiettivo().get(0);
			    Obiettivo obiettivo = cartaObiettivo.getObiettivo();
			    int punto = cartaObiettivo.getPunto();
			    
			    switch (obiettivo.getTipo()) {
		        case RISORSA:    
		            int puntiRisorsa = (countResource(obiettivo.getRisorsa(), g) * punto);
		            board.setPunteggio(board.getPunteggio() + puntiRisorsa);
		            break;
		        case OGGETTO:
		            List<Oggetto> oggetti = obiettivo.getOggetto();
		            
		            if (oggetti.size() >= 2) {
		            	
		                if (oggetti.get(0).equals(Oggetto.PIUMA) && oggetti.get(1).equals(Oggetto.PIUMA)) {
		                	int puntiOggetto = (countObject(Oggetto.PIUMA, null, g) * punto);
		                    board.setPunteggio(board.getPunteggio() + puntiOggetto);
		                }else if (oggetti.get(0).equals(Oggetto.INCHIOSTRO) && oggetti.get(1).equals(Oggetto.INCHIOSTRO)) {
		                	int puntiOggetto = (countObject(Oggetto.INCHIOSTRO, null, g) * punto);
		                    board.setPunteggio(board.getPunteggio() + puntiOggetto);
		                }else if (oggetti.get(0).equals(Oggetto.PERGAMENA) && oggetti.get(1).equals(Oggetto.PERGAMENA)) {
		                	int puntiOggetto = (countObject(Oggetto.PERGAMENA, null, g) * punto);
		                    board.setPunteggio(board.getPunteggio() + puntiOggetto);
		                }else if (oggetti.size() >= 3 && oggetti.get(0).equals(Oggetto.PIUMA) && oggetti.get(1).equals(Oggetto.INCHIOSTRO) && oggetti.get(2).equals(Oggetto.PERGAMENA)) {
		                	int puntiOggetto = (countObject(null, oggetti, g) * punto);
		                    board.setPunteggio(board.getPunteggio() + puntiOggetto);
		                }
		            }
		            
		            break;
		        case DISPOSIZIONE:
		        	int puntiDisposizione = conteggioDisp(g, obiettivo, cartaObiettivo) * punto;
		            board.setPunteggio(board.getPunteggio() + puntiDisposizione);    
		            break;
			    }
			}else {
				Board board = g.getBoard();
				CartaObiettivo cartaObiettivo = this.model.getCampo().getObiettivo().get(1);
			    Obiettivo obiettivo = cartaObiettivo.getObiettivo();
			    int punto = cartaObiettivo.getPunto();
			    
			    switch (obiettivo.getTipo()) {
		        case RISORSA:    
		            int puntiRisorsa = (countResource(obiettivo.getRisorsa(), g) * punto);
		            board.setPunteggio(board.getPunteggio() + puntiRisorsa);
		            break;
		        case OGGETTO:
		            List<Oggetto> oggetti = obiettivo.getOggetto();
		           
		            if (oggetti.size() >= 2) {
		            	
		                if (oggetti.get(0).equals(Oggetto.PIUMA) && oggetti.get(1).equals(Oggetto.PIUMA)) {
		                	int puntiOggetto = (countObject(Oggetto.PIUMA, null, g) * punto);
		                    board.setPunteggio(board.getPunteggio() + puntiOggetto);
		                }else if (oggetti.get(0).equals(Oggetto.INCHIOSTRO) && oggetti.get(1).equals(Oggetto.INCHIOSTRO)) {
		                	int puntiOggetto = (countObject(Oggetto.INCHIOSTRO, null, g) * punto);
		                    board.setPunteggio(board.getPunteggio() + puntiOggetto);
		                }else if (oggetti.get(0).equals(Oggetto.PERGAMENA) && oggetti.get(1).equals(Oggetto.PERGAMENA)) {
		                	int puntiOggetto = (countObject(Oggetto.PERGAMENA, null, g) * punto);
		                    board.setPunteggio(board.getPunteggio() + puntiOggetto);
		                }else if (oggetti.size() >= 3 && oggetti.get(0).equals(Oggetto.PIUMA) && oggetti.get(1).equals(Oggetto.INCHIOSTRO) && oggetti.get(2).equals(Oggetto.PERGAMENA)) {
		                	int puntiOggetto = (countObject(null, oggetti, g) * punto);
		                    board.setPunteggio(board.getPunteggio() + puntiOggetto);
		                }
		            }
		            
		            break;
		        case DISPOSIZIONE:
		        	int puntiDisposizione = conteggioDisp(g, obiettivo, cartaObiettivo) * punto;
		            board.setPunteggio(board.getPunteggio() + puntiDisposizione);    
		            break;
			    }
			}
		}
	}

	/**
	 * Metodo che permette di contare il numero di risorse utili 
	 * per la determinazione dell'obiettivo di tipo "risorsa".
	 * @param risorsa: regno a cui appartiene la risorsa il quale 
	 * contatore deve essere controllato per conoscere il numero di
	 * tali risorse presenti sulla board
	 * @param g: giocatore di cui si controllano il numero di risorse 
	 * del regno indicato sulla carta obiettivo presenti sulla board 
	 * @return numero di gruppi di tre risorse appartenenti
	 * al regno indicato sulla carta obiettivo
	 */
	public int countResource(Regno risorsa, Giocatore g) {
		  Board board = g.getBoard();
		  int ris = 0;
		  int risorsaIndex = -1;

		    switch (risorsa) {
		        case VEGETALE: 
		        	risorsaIndex = 0; 
		        	break;
		        case ANIMALE: 
		        	risorsaIndex = 1; 
		        	break;
		        case FUNGHI: 
		        	risorsaIndex = 2; 
		        	break;
		        case INSETTI: 
		        	risorsaIndex = 3; 
		        	break;
		    }

		    if (risorsaIndex != -1 && board.getNumRis().get(risorsaIndex) != 0) {
		        ris = board.getNumRis().get(risorsaIndex) / 3;
		        g.getBoard().addObj(ris);
		    }else {
		    	return ris;
	        }
		    
			return ris;
		}
	/**
	 * Metodo che permette di contare il numero di oggetti utili 
	 * per la determinazione dell'obiettivo di tipo "oggetto".
	 * @param oggetto: tipo di oggetto il quale contatore deve essere
	 * controllato per conoscere il numero di tali oggetti
	 * @param diversi: booleano che è TRUE se si tratta dell'obiettivo
	 * specifico in cui è necessario contare i gruppi formati da tre
	 * oggetti diversi presenti sulla board, mentre è FALSE se si
	 * tratta di qualsiasi altro obiettivo di tipo "oggetto"
	 * @param g: giocatore di cui si controllano gli oggetti del tipo 
	 * indicato sulla carta obiettivo presenti sulla board
	 * @return numero di gruppi del tipo indicato sulla carta 
	 * obiettivo presenti sulla board
	 */
	public int countObject(Oggetto oggetto, List<Oggetto> diversi, Giocatore g) {
	    Board board = g.getBoard();
	    int ris = 0;

	    if (oggetto != null) {
	    	
	        int oggettoIndex = -1;

	        switch (oggetto) {
	            case PIUMA: 
	            	oggettoIndex = 0; 
	            	break;
	            case INCHIOSTRO: 
	            	oggettoIndex = 1; 
	            	break;
	            case PERGAMENA: 
	            	oggettoIndex = 2; 
	            	break;
	        }

	        if (oggettoIndex != -1 && board.getNumOgg().get(oggettoIndex) != 0) {
	            ris = board.getNumOgg().get(oggettoIndex) / 2;
	            g.getBoard().addObj(ris);
	        } else {
	        	return ris;
	        }
	    }

	    if (diversi != null && diversi.size() >= 3) {
	    	
	        int piuma = board.getNumOgg().get(0);
	        int inchiostro = board.getNumOgg().get(1);
	        int pergamena = board.getNumOgg().get(2);
	        int groups = 0;

	        while (piuma > 0 && inchiostro > 0 && pergamena > 0) {
	            groups++;
	            piuma--;
	            inchiostro--;
	            pergamena--;
	        }
	        
	        g.getBoard().addObj(groups);
	        
	        return groups;
	    }

	    return ris;
	}
	
	/**
	 * Metodo che ritorna il numero di volte in cui la disposizione
	 * richiesta dall'obiettivo viene conseguita.
	 * @param g: giocatore di cui si controlla la disposizione delle
	 * carte sulla board
	 * @param obiettivo: obiettivo da conseguire per ottenere i punti
	 * @param cartaObiettivo: carta dalla quale viene preso l' id
	 * @return numero delle volte in cui la disposizione richiesta 
	 * dalla cartaObiettivo viene conseguita
	 */
	public int conteggioDisp(Giocatore g, Obiettivo obiettivo, Carta cartaObiettivo) {
		
		String mat[][] = g.getBoard().getMatrix();
		Regno matriceColore [][] = new Regno [mat.length][mat[0].length];
		String regno = null;
		int count = 0;
		int tipoObiettivo = 0;
		
		
		String tipo = cartaObiettivo.getId().substring(2,5).toUpperCase();
		
		switch(tipo) {
		case "FUN":
			tipoObiettivo = 0;
			for(int i = 0; i <mat.length; i++) {
				for(int j=0; j <mat.length; j++) {
					if (mat[i][j]!=null) {
						regno = g.getBoard().getMatrix()[i][j].substring(1,3).toUpperCase();
						switch(regno) {
						case "RS":
							matriceColore [i][j] = Regno.FUNGHI;
							break;
						default:
	                        matriceColore[i][j] = null;
	                        break;
						}
					} else 
						matriceColore[i][j] = null;
					}
			}
			break;
		case "ANI":
			tipoObiettivo = 1;
			for(int i = 0; i <mat.length; i++) {
				for(int j=0; j <mat.length; j++) {
					if (mat[i][j]!=null) {
						regno = g.getBoard().getMatrix()[i][j].substring(1,3).toUpperCase();
						switch(regno) {
						case "BL":
							matriceColore [i][j] = Regno.ANIMALE;
							break;
						default:
	                        matriceColore[i][j] = null;
	                        break;
						}
					} else 
						matriceColore[i][j] = null;
					}
			}
			break;
		case "VEG":
			tipoObiettivo = 2;
			for(int i = 0; i <mat.length; i++) {
				for(int j=0; j <mat.length; j++) {
					if (mat[i][j]!=null) {
						regno = g.getBoard().getMatrix()[i][j].substring(1,3).toUpperCase();
						switch(regno) {
						case "VR":
							matriceColore [i][j] = Regno.VEGETALE;
							break;
						default:
	                        matriceColore[i][j] = null;
	                        break;
						}
					} else 
						matriceColore[i][j] = null;
					}
			}
			break;
		case "INS":
			tipoObiettivo = 3;
			for(int i = 0; i <mat.length; i++) {
				for(int j=0; j <mat.length; j++) {
					if (mat[i][j]!=null) {
						regno = g.getBoard().getMatrix()[i][j].substring(1,3).toUpperCase();
						switch(regno) {
						case "VL":
							matriceColore [i][j] = Regno.INSETTI;
							break;
						default:
	                        matriceColore[i][j] = null;
	                        break;
						}
					} else 
						matriceColore[i][j] = null;
					}
			}
			break;
		case "FV1":
			tipoObiettivo = 4;
			for(int i = 0; i <mat.length; i++) {
				for(int j=0; j <mat.length; j++) {
					if (mat[i][j]!=null) {
						regno = g.getBoard().getMatrix()[i][j].substring(1,3).toUpperCase();
						switch(regno) {
						case "RS":
							matriceColore [i][j] = Regno.FUNGHI;
							break;
						case "VR":
							matriceColore [i][j] = Regno.VEGETALE;
							break;
						default:
	                        matriceColore[i][j] = null;
	                        break;
						}
					} else 
						matriceColore[i][j] = null;
					}
			}
			break;
		case "VI1":
			tipoObiettivo = 5;
			for(int i = 0; i <mat.length; i++) {
				for(int j=0; j <mat.length; j++) {
					if (mat[i][j]!=null) {
						regno = g.getBoard().getMatrix()[i][j].substring(1,3).toUpperCase();
						switch(regno) {
						case "VL":
							matriceColore [i][j] = Regno.INSETTI;
							break;
						case "VR":
							matriceColore [i][j] = Regno.VEGETALE;
							break;
						default:
	                        matriceColore[i][j] = null;
	                        break;
						}
					} else 
						matriceColore[i][j] = null;
					}
			}
			break;
		case "AF1":
			tipoObiettivo = 6;
			for(int i = 0; i <mat.length; i++) {
				for(int j=0; j <mat.length; j++) {
					if (mat[i][j]!=null) {
						regno = g.getBoard().getMatrix()[i][j].substring(1,3).toUpperCase();
						switch(regno) {
						case "RS":
							matriceColore [i][j] = Regno.FUNGHI;
							break;
						case "BL":
							matriceColore [i][j] = Regno.ANIMALE;
							break;
						default:
	                        matriceColore[i][j] = null;
	                        break;
						}
					} else 
						matriceColore[i][j] = null;
					}
			}
			break;
		case "IA1":
			tipoObiettivo = 7;
			for(int i = 0; i <mat.length; i++) {
				for(int j=0; j <mat.length; j++) {
					if (mat[i][j]!=null) {
						regno = g.getBoard().getMatrix()[i][j].substring(1,3).toUpperCase();
						switch(regno) {
						case "VL":
							matriceColore [i][j] = Regno.INSETTI;
							break;
						case "BL":
							matriceColore [i][j] = Regno.ANIMALE;
							break;
						default:
	                        matriceColore[i][j] = null;
	                        break;
						}
					} else 
						matriceColore[i][j] = null;
					}
			}
			break;
		}
			    		
			if(tipoObiettivo == 4 || tipoObiettivo == 5 || tipoObiettivo == 6 || tipoObiettivo == 7) {
				Regno [][] matriceL = new Regno[4][2];
				
				matriceL[0][0] = obiettivo.getDisposizione()[0][0];
				matriceL[0][1] = obiettivo.getDisposizione()[0][1];
				matriceL[1][0] = obiettivo.getDisposizione()[1][0];
				matriceL[1][1] = obiettivo.getDisposizione()[1][1];
				matriceL[2][0] = obiettivo.getDisposizione()[2][0];
				matriceL[2][1] = obiettivo.getDisposizione()[2][1];
				matriceL[3][0] = obiettivo.getDisposizione()[3][0];
				matriceL[3][1] = obiettivo.getDisposizione()[3][1];
				
				count = contaMatrice(matriceColore, matriceL, tipoObiettivo);
				
			}else {
				Regno [][] matriceL = new Regno[3][3];
				
				matriceL[0][0] = obiettivo.getDisposizione()[0][0];
				matriceL[0][1] = obiettivo.getDisposizione()[0][1];
				matriceL[0][2] = obiettivo.getDisposizione()[0][2];
				matriceL[1][0] = obiettivo.getDisposizione()[1][0];
				matriceL[1][1] = obiettivo.getDisposizione()[1][1];
				matriceL[1][2] = obiettivo.getDisposizione()[1][2];
				matriceL[2][0] = obiettivo.getDisposizione()[2][0];
				matriceL[2][1] = obiettivo.getDisposizione()[2][1];
				matriceL[2][2] = obiettivo.getDisposizione()[2][2];
				
				count = contaMatrice(matriceColore, matriceL, tipoObiettivo);
			}
		
		return count;
		
	}
	
	/**
	 * Metodo che scorre la matrice e conta quante volte la 
	 * disposizione è rispettata.
	 * @param matriceColore: matrice copia della board contente
	 * solamente i regni richiesti dalla carta obiettivo
	 * @param matriceL: matrice copia dell disposizione indicata 
	 * sulla carta obiettivo
	 * @param tipoObiettivo: numero intero che indica la tipologia 
	 * di disposizione da controllare
	 * @return numero di volte in cui la disposizione è rispettata
	 */
	public int contaMatrice (Regno matriceColore[][], Regno[][] matriceL, int tipoObiettivo) {
		
		int count = 0;
		
		for(int i = 0; i < matriceColore.length - matriceL.length ; i++) {
			for(int j = 0; j < matriceColore[i].length - matriceL[0].length; j++) {
				if(scanDisposition(matriceColore, matriceL, i, j, tipoObiettivo)) {
					count++;
					
                        }
					}
				}
		
		return count;
	}
	
	/**
	 * Metodo che controlla che una sottomatrice contenente 
	 * la disposizione obiettivo sia contenuta nella matrice
	 * copia della board
	 * @param mat: matrice copia della board
	 * @param disp: sottomatrice della disposizione
	 * @param i: riga della matrice mat
	 * @param j: colonna della matrice mat
	 * @param tipoObiettivo: numero intero che indica la tipologia 
	 * di disposizione da controllare
	 * @return TRUE se la corrispondenza è verifcata, 
	 * altrimenti FALSE
	 */
	public boolean scanDisposition (Regno [][] mat, Regno [][] disp, int i, int j, int tipoObiettivo) {
		
		
		boolean check1 = false;
		boolean check2 = false;
		boolean check3 = false;
		boolean check4 = false;
		
		if(tipoObiettivo==0 || tipoObiettivo==1 || tipoObiettivo==2 || tipoObiettivo==3) {
			   if (mat != null) {
		        	  if(tipoObiettivo==0 || tipoObiettivo==1) {
		        		  if(mat[i][j] == disp[0][0] || mat[i][j] != disp[0][0]) {
		        			  if(mat[i][j + 1] == disp[0][1] || mat[i][j + 1] != disp[0][1]) {
		        				  if(mat[i][j + 2] == disp[0][2]) {
		  		          			check1 = true; 
		  		          			if(mat[i + 1][j] == disp[0][1] || mat[i + 1][j] != disp[0][1]) {
		  		          				if(mat[i + 1][j + 1] == disp[1][1]) {
		  		          					if(mat[i + 1][j + 2] == disp [1][2] || mat[i + 1][j + 2] != disp [1][2]) {
		  		          						check2= true;
			  		          					if(mat[i + 2][j] == disp[2][0]) {
			  		          						if (mat[i + 2][j + 1] == disp[2][1] || mat[i + 2][j + 1] != disp[2][1]) {
			  		          							if (mat[i + 2][j + 2] == disp[2][2] || mat[i + 2][j + 2] != disp[2][2]) {
			  		          							check3 =true;
			  		        							}
			  		          						}
			  		          					}
			  		          				}
		  		          				}
		  		          			}
		  		          		}
		        			}
		        		}
		          	} else if (tipoObiettivo==2 || tipoObiettivo==3) {
		          	  if(mat[i][j] == disp[0][0]) {
	        			  if(mat[i][j + 1] == disp[0][1] || mat[i][j + 1] != disp[0][1]) {
	        				  if(mat[i][j + 2] == disp[0][2] || mat[i][j + 2] != disp[0][2]) {
	  		          			check1 = true; 
	  		          			if(mat[i + 1][j] == disp[0][1] || mat[i + 1][j] != disp[0][1]) {
	  		          				if(mat[i + 1][j + 1] == disp[1][1]) {
	  		          					if(mat[i + 1][j + 2] == disp [1][2] || mat[i + 1][j + 2] != disp [1][2]) {
	  		          						check2= true;
		  		          					if(mat[i + 2][j] == disp[2][0] || mat[i + 2][j] != disp[2][0]) {
		  		          						if (mat[i + 2][j + 1] == disp[2][1] || mat[i + 2][j + 1] != disp[2][1]) {
		  		          							if (mat[i + 2][j + 2] == disp[2][2]) {
		  		          							check3 =true;
		  		          							}
		  		          						}
		  		          					}
		  		          				}
	  		          				}
	  		          			}
	  		          		}
	        			}
	        		}
	        	}
		        if(check1 && check2 && check3) {
		        	if(tipoObiettivo==0 || tipoObiettivo==1) {
		        		mat[i][j + 2] = null;
		        		mat[i + 1][j + 1] = null;
		        		mat[i + 2][j] = null;
		        	} else if (tipoObiettivo==2 || tipoObiettivo==3) {
		        		mat[i][j]= null;
		        		mat[i + 1][j + 1] = null;
		        		mat[i + 2][j + 2] = null;
		        	}
		    		return true;
		    	} else {
		    		return false;
		    	}
			}
		}
		        
		if(tipoObiettivo==4 || tipoObiettivo==5 || tipoObiettivo==6 || tipoObiettivo==7) {
			   if (mat != null) {
		        	  if(tipoObiettivo==4) {
		        		  if(mat[i][j] == disp[0][0]) {
		        			  if(mat[i][j + 1] == disp[0][1] || mat[i][j + 1] != disp[0][1]) {
		        				  check1 = true;
		        				  if(mat[i + 1][j] == disp[1][0] || mat[i + 1][j] != disp[1][0]) {
				        			  if(mat[i + 1][j + 1] == disp[1][1] || mat[i + 1][j + 1] != disp[1][1]) {
				        				  check2 = true;
				        				  if(mat[i + 2][j] == disp[2][0]) {
						        			  if(mat[i + 2][j + 1] == disp[2][1] || mat[i + 2][j + 1] != disp[2][1]) {
						        				  check3 = true;
						        				  if(mat[i + 3][j] == disp[3][0] || mat[i + 3][j] != disp[3][0]) {
								        			  if(mat[i + 3][j + 1] == disp[3][1]) {
								        				  check4 = true; 
								        			  }
						        				  }
						        			  }
				        				  }
				        			  }
		        				  }
		        			  }
		        		  }
		        	  } else if (tipoObiettivo==5) {
		        		  if(mat[i][j] == disp[0][0] || mat[i][j] != disp[0][0]) {
		        			  if(mat[i][j + 1] == disp[0][1]) {
		        				  check1 = true;
		        				  if(mat[i + 1][j] == disp[1][0] || mat[i + 1][j] != disp[1][0]) {
				        			  if(mat[i + 1][j + 1] == disp[1][1] || mat[i + 1][j + 1] != disp[1][1]) {
				        				  check2 = true;
				        				  if(mat[i + 2][j] == disp[2][0] || mat[i + 2][j] != disp[2][0]) {
						        			  if(mat[i + 2][j + 1] == disp[2][1]) {
						        				  check3 = true;
						        				  if(mat[i + 3][j] == disp[3][0]) {
								        			  if(mat[i + 3][j + 1] == disp[3][1] || mat[i + 3][j + 1] != disp[3][1]) {
								        				  check4 = true; 
								        			  }
						        				  }
						        			  }
				        				  }
				        			  }
		        				  }
		        			  }
		        		  }
		        	  } else if (tipoObiettivo==6) {
		        		  if(mat[i][j] == disp[0][0] || mat[i][j] != disp[0][0]) {
		        			  if(mat[i][j + 1] == disp[0][1]) {
		        				  check1 = true;
		        				  if(mat[i + 1][j] == disp[1][0]) {
				        			  if(mat[i + 1][j + 1] == disp[1][1] || mat[i + 1][j + 1] != disp[1][1]) {
				        				  check2 = true;
				        				  if(mat[i + 2][j] == disp[2][0] || mat[i + 2][j] != disp[2][0]) {
						        			  if(mat[i + 2][j + 1] == disp[2][1] || mat[i + 2][j + 1] != disp[2][1]) {
						        				  check3 = true;
						        				  if(mat[i + 3][j] == disp[3][0]) {
								        			  if(mat[i + 3][j + 1] == disp[3][1] || mat[i + 3][j + 1] != disp[3][1]) {
								        				  check4 = true; 
								        			  }
						        				  }
						        			  }
				        				  }
				        			  }
		        				  }
		        			  }
		        		  }
		        	  } else if (tipoObiettivo==7) {
		        		  if(mat[i][j] == disp[0][0]) {
		        			  if(mat[i][j + 1] == disp[0][1] || mat[i][j + 1] != disp[0][1]) {
		        				  check1 = true;
		        				  if(mat[i + 1][j] == disp[1][0] || mat[i + 1][j] != disp[1][0]) {
				        			  if(mat[i + 1][j + 1] == disp[1][1]) {
				        				  check2 = true;
				        				  if(mat[i + 2][j] == disp[2][0] || mat[i + 2][j] != disp[2][0]) {
						        			  if(mat[i + 2][j + 1] == disp[2][1] || mat[i + 2][j + 1] != disp[2][1]) {
						        				  check3 = true;
						        				  if(mat[i + 3][j] == disp[3][0] || mat[i + 3][j] != disp[3][0]) {
								        			  if(mat[i + 3][j + 1] == disp[3][1]) {
								        				  check4 = true; 
								        			  }
						        				  }
						        			  }
				        				  }
				        			  }
		        				  }
		        			  }
		        		  }
		        	  } 
		        	  
		        if(check1 && check2 && check3 && check4) {
		        	if(tipoObiettivo==4) {
		        		mat[i][j] = null;
		        		mat[i + 2][j] = null;
		        		mat[i + 3][j + 1] = null;
		        	} else if (tipoObiettivo==5) {
		        		mat[i + 3][j] = null;
		        		mat[i + 2][j + 1] = null;
		        		mat[i + 0][j + 1] = null;
		        	} else if (tipoObiettivo==6) {
		        		mat[i][j + 1] = null;
		        		mat[i + 1][j] = null;
		        		mat[i + 3][j] = null;
		        	} else if (tipoObiettivo==7) {
		        		mat[i][j] = null;
		        		mat[i + 1][j + 1] = null;
		        		mat[i + 3][j + 1] = null;
		        	}
		    		return true;
		    	} else {
		    		return false;
		    	}
			}
		}
		
     return false;
		 
	}
	
	/**
	 * Metodo che gestisce la fine della partita e chiude il 
	 * launcher.
	 */
	public void endGame() {
		view.endMessage();
		System.exit(0);
	}
}
