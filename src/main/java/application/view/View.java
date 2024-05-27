package application.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import application.model.Angolo;
import application.model.Board;
import application.model.CampoDiGioco;
import application.model.Carta;
import application.model.CartaIniziale;
import application.model.CartaObiettivo;
import application.model.CartaOro;
import application.model.CartaRisorsa;
import application.model.Giocatore;
import application.model.Mano;
import application.model.Pedina;
import application.model.Posizione;
import application.model.Regno;

/**
 * Classe che rappresenta l'interfaccia di output e input utilizzata dai giocatori.
 */
public class View {
	/**
	 * Attributo che gestisce gli input da console.
	 */
	private static final Scanner SCANNER = new Scanner(System.in);
	
	/**
	 * Metodo che stampa il messaggio di inizio del gioco.
	 * @return
	 */
	public boolean welcomeMessage() {
		System.out.println("CODEX NATURALIS\n");
		System.out.println("Benvenuti!!! Volete giocare? (SI/NO)");
		
		while(true) {
			try {
				String res = SCANNER.nextLine();
				if(res.equalsIgnoreCase("NO")) {
					return false;
				}else if(res.equalsIgnoreCase("SI")){
					return true;
				}else {
					throw new IOException();
				}
			} catch(IOException e){
				System.out.println("Risposta non ammessa, riprova con SI o NO");
			}
		}
	}
	 /**
	  * Metodo che stampa il messaggio di chiusura del programma.
	  */
	public void endMessage() {
		System.out.println("ARRIVEDERCI!!!");
	}
	
	/**
	 * Metodo che chiede il numero di giocatori.
	 */
	public int getPlayersNumberMessage() {
	
		System.out.print("Inserire il numero di giocatori:");
		int giocatori;
		
		while(true) {
			try {
				giocatori = SCANNER.nextInt();
				if(giocatori < 2 || giocatori > 4) {
					throw new IOException("Unacceptable response");
				}else {
					return giocatori;
				}
			} catch(IOException e) {
				System.out.println("Il numero minimo di giocatori è 2 e il numero massimo di giocatori è 4");
			} catch(InputMismatchException e) {
				System.out.println("La risposta non è accettabile, riprova con un numero");
			} finally {
				SCANNER.nextLine();
			}
		}
	
	}
	
	/**
	 * Metodo che chiede il nickname del giocatore.
	 * @param n
	 * @return
	 * @throws Exception 
	 */
	public String getNick(int n) {
		System.out.println("Inserire il nickname del giocatore " + n +":");
		String giocatore = SCANNER.nextLine();
		
		while(true) {
			System.out.println("Quindi il nickname del giocatore " + n +" è " + giocatore + "?");
			try {
				String ris = SCANNER.nextLine();
				if(ris.equalsIgnoreCase("NO")) {
					System.out.println("Allora qual è il tuo nickname?");
					giocatore = SCANNER.nextLine();
				}else if(ris.equalsIgnoreCase("SI")){
					return giocatore;
				}else {
					throw new IOException();
				}
			} catch(IOException e){
				System.out.println("Risposta non ammessa, riprova con SI o NO");
			} 
		}
	}
	
	/**
	 * Metodo che chiede al giocatore la pedina scelta. 
	 * @param n
	 * @return
	 */
	public Pedina getPedina(int n, ArrayList<Pedina> ped) {
		System.out.print("Quale pedina scegli giocatore " + n + " ");
		for(int i = 0; i < ped.size() - 1; i++) {
			System.out.print(ped.get(i) + ", ");
		}
		System.out.println(ped.get(ped.size() - 1));
		String pedina;
		Pedina colore = null;
		
		while(true) {
			try {
				pedina = SCANNER.nextLine().toUpperCase();
				switch (pedina) {
				case "ROSSO":
					colore = Pedina.ROSSO;
					return colore;
				case "BLU":
					colore = Pedina.BLU;
					return colore;
				case "VERDE":
					colore = Pedina.VERDE;
					return colore;
				case "GIALLO":
					colore = Pedina.GIALLO;
					return colore;
				default:
					throw new IOException();
				}
			}catch(IOException e) {
				System.out.println("Risposta non ammessa, riprova con ROSSO, BLU, VERDE o GIALLO");
			}
		}

	}
	
	/**
	 * Metodo che avvisa l'utente che la pedina da lui selezionata è già stata
	 * scelta.
	 */
	public void retry() {
		System.out.println("La pedina è già stata scelta");
	}
	
	/**
	 * Metodo che chiede all'utente se intende piazzare la carta iniziale mostrando
	 * il fronte oppure mostrando il retro.
	 * @param fronte
	 * @param retro
	 * @return
	 */
	public boolean chooseStartCard(String gio, CartaIniziale fronte, CartaIniziale retro) {
		
		System.out.println("\nQuesta è la carta iniziale di " + gio + ":\n");
		System.out.println("FRONTE:");
		System.out.println(fronte.showCard());
		System.out.println("\nRETRO:");
		System.out.println(retro.showCard());
		System.out.println("\n" + "Come vuoi posizionare la carta? (FRONTE/RETRO)");
		
		while(true) {
			try {
				String ris = SCANNER.nextLine();
				if(ris.equalsIgnoreCase("FRONTE")) {
					return true;
				} else if (ris.equalsIgnoreCase("RETRO")) {
					return false;
				} else {
					throw new IOException();
				}
			}catch(IOException e) {
				System.out.println("Risposta non valida, riprova con FRONTE o RETRO");
			}
		}
		
	}	
	
	/**
	 * Metodo che chiede all'utente quale delle due carte obiettivo che ha ricevuto
	 * intende tenere e quale invece intende scartare.
	 * @param gio
	 * @param obi1
	 * @param obi2
	 * @return
	 */
	public boolean chooseObjectiveCard(String gio, CartaObiettivo obi1, CartaObiettivo obi2) {
		System.out.println("\nQueste sono le due carte obiettivo del giocatore " + gio + ":" );
		System.out.println("\nCARTA 1:");
		this.showObjective(obi1.showCard(), obi1.getObiettivo().getDisposizione());
		System.out.println("\nCARTA 2:");
		this.showObjective(obi2.showCard(), obi2.getObiettivo().getDisposizione());
		System.out.println("\n" + "Quale carta vuoi scegliere? (1/2)");
		
		while(true) {
			try {
				String ris = SCANNER.nextLine();
				if(ris.equalsIgnoreCase("1")) {
					return true;
				} else if (ris.equalsIgnoreCase("2")) {
					return false;
				} else {
					throw new IOException();
				}
			}catch(IOException e) {
				System.out.println("Risposta non valida, riprova con 1 o 2");
			}
		}
	}
	
	/**
	 * Metodo che gestisce la visualizzazione degli obiettivi di tipo disposizione.
	 * @param riga
	 * @param dispo
	 */
	public void showObjective(String riga, Regno[][]dispo) {
		String regex = "[\n]";
		String [] righe = riga.split(regex);
		String line = "   ";
		
		if(righe[3].contains("disposizione")) {
			for(int i = 0; i < 3; i++) {
				for(int j = 0; j < 3; j++) {
					int s = i + 1;
					if(dispo[i][j] == null) {
						line += "     ";
					}else {
						switch(dispo[i][j]) {
						case VEGETALE:
							line += AnsiEscapeCodes.GREEN_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + "CARD" + s + AnsiEscapeCodes.ENDING_CODE.getCode();
							break;
						case ANIMALE:
							line += AnsiEscapeCodes.CYAN_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode( ) +"CARD" + s + AnsiEscapeCodes.ENDING_CODE.getCode();
							break;
						case FUNGHI:
							line += AnsiEscapeCodes.RED_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + "CARD" + s + AnsiEscapeCodes.ENDING_CODE.getCode();
							break;
						case INSETTI:
							line += AnsiEscapeCodes.VIOLET_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + "CARD" + s + AnsiEscapeCodes.ENDING_CODE.getCode();
							break;
						default:
							line += "     ";
							break;
						}
					}
					
					if (j == 2) {
						line += "\n   ";
					}
				}
			}
		}
		
		System.out.print(righe[0] + "\n" + righe[1] + "\n" + righe[2] + "\n" + righe[3] + "\n" + line);
	}
	
	/**
	 * Metodo che mostra qual è l'ordine di gioco che i giocatori dovranno seguire.
	 * @param ordine
	 */
	public void showNewOrder(ArrayList<Giocatore> ordine) {
		String nuovoOrdine = "";
		
		for (int i=0; i < ordine.size(); i++) {
			nuovoOrdine += "    " + (i+1) + "." + ordine.get(i).getNick();
			nuovoOrdine += "\n";
		}
		
		System.out.println("\nQuesto è l'ordine di gioco:\n\n" + nuovoOrdine);
		
	}
	
	/**
	 * Metodo che mostra in output il messaggio di fine dei preparativi ed inizio
	 * della partita vera e propria.
	 */
	public void startMessage() {
		System.out.println("I PREPARATIVI SONO TERMINATI");
		System.out.println("CHE LA PARTITA ABBIA INIZIO");
	}
	
	/**
	 * Metodo che dice ai giocatori che i mazzi sono terminati.
	 * @param over
	 * @return
	 */
	public boolean showDecksAreOverMessage(boolean over) {
		if(over) {
			System.out.println("I mazzi sono entrambi terminati");
			return true;
		}
		
		return false;
	}
	/**
	 * Metodo che avvisa i giocatori che stanno per giocare il loro ultimo turno.
	 * @param last
	 */
	public void tellLastTurn(boolean last) {
		if(last) {
			System.out.println("È L'ULTIMO TURNO");
		}
	}
	
	/**
	 * Metodo che avvisa i giocatori a chi appartiene il turno che sta per essere
	 * giocato.
	 * @param nick
	 */
	public void tellWhoseTurn(String nick) {
		System.out.println("TURNO DI: " + nick );
	}
	
	/**
	 * Metodo che mostra in output le board dei giocatori.
	 * @param g
	 * @param giocatori
	 */
	public void showAllBoards(Giocatore g, ArrayList<Giocatore> giocatori) {
		System.out.println("Informazioni sul campo degli altri giocatori:");
		this.showOtherBoards(g, giocatori);
		this.showPlayerStatus(g);
	}
	
	/**
	 * Metodo che stampa a schermo le board degli altri giocatori che non sono 
	 * di turno.
	 * @param g
	 * @param giocatori
	 */
	public void showOtherBoards(Giocatore g, ArrayList<Giocatore> giocatori) {
		for(int i = 0; i < giocatori.size(); i++) {
			if(giocatori.get(i) != g) {
				this.showEnemyBoard(giocatori.get(i));
			}
		}
	}
	
	/**
	 * Metodo che stampa a schermo la board di un avversarios.
	 * @param g
	 */
	public void showEnemyBoard(Giocatore g) {
		Board bor = g.getBoard();
		String [][] mat = bor.getMatrix();
		String intro =  "CAMPO DI: " + g.getNick() + "\n" +
						"   Turni giocati: " + bor.getTurno() + "\n" +
						"   Punteggio: " + bor.getPunteggio() + "\n" +
						"   Risorse presenti negli angoli delle carte posizionate sul campo:" + "\n" +
						"    VEGETALE: " + bor.getNumRis().get(0) + "\n" +
						"    ANIMALE: " + bor.getNumRis().get(1) + "\n" +
						"    FUNGHI: " + bor.getNumRis().get(2) + "\n" +
						"    INSETTI: " + bor.getNumRis().get(3) + "\n" +
						"   Oggetti presenti negli angoli delle carte posizionate sul campo:" + "\n" + 
						"    PIUMA: " + bor.getNumOgg().get(0) + "\n" + 
						"    INCHIOSTRO " + bor.getNumOgg().get(1) + "\n" +
						"    PERGAMENA: " + bor.getNumOgg().get(2) + "\n";
		String board = "Carte in campo:" + "\n";
		
		for (int i = 0; i < mat.length; i++) {
			board += "    ";
			for (int j = 0; j < mat[i].length; j++) {
				if(mat[i][j] == null) {
					board += "     ";
				}else {
					if(mat[i][j].charAt(0) == 'I') {
						board += AnsiEscapeCodes.WHITE_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + mat[i][j] + AnsiEscapeCodes.ENDING_CODE.getCode();
					} else {
						switch(mat[i][j].charAt(1)) {
						case 'R':
							board += AnsiEscapeCodes.RED_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + mat[i][j] + AnsiEscapeCodes.ENDING_CODE.getCode();
							break;
						case 'B':
							board += AnsiEscapeCodes.CYAN_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode( ) + mat[i][j] + AnsiEscapeCodes.ENDING_CODE.getCode();
							break;
						case 'V':
							if(mat[i][j].charAt(2) == 'R') {
								board += AnsiEscapeCodes.GREEN_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + mat[i][j] + AnsiEscapeCodes.ENDING_CODE.getCode();
							} else if(mat[i][j].charAt(2) == 'L') {
								board += AnsiEscapeCodes.VIOLET_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + mat[i][j] + AnsiEscapeCodes.ENDING_CODE.getCode();
							}
							break;
						default:
							board += "     ";
							break;
						}
					}
				}
				
				if (j == mat.length - 1) {
					board += "\n   ";
				}
			}
		}
		
		System.out.println(intro);
		System.out.println("\n" + board);
	}
	
	
	/**
	 * Metodo che stampa a schermo la board di un determinato giocatore.
	 * @param g
	 */
	public void showBoard(Giocatore g) {
		Board bor = g.getBoard();
		String [][] mat = bor.getMatrix();
		Regno[][] disp = bor.getObiettivo().getObiettivo().getDisposizione();
		String intro =  "CAMPO DI: " + g.getNick() + "\n" +
						"   Turni giocati: " + bor.getTurno() + "\n" +
						"   Punteggio: " + bor.getPunteggio() + "\n" +
						"   Risorse presenti negli angoli delle carte posizionate sul campo:" + "\n" +
						"    VEGETALE: " + bor.getNumRis().get(0) + "\n" +
						"    ANIMALE: " + bor.getNumRis().get(1) + "\n" +
						"    FUNGHI: " + bor.getNumRis().get(2) + "\n" +
						"    INSETTI: " + bor.getNumRis().get(3) + "\n" +
						"   Oggetti presenti negli angoli delle carte posizionate sul campo:" + "\n" + 
						"    PIUMA: " + bor.getNumOgg().get(0) + "\n" + 
						"    INCHIOSTRO " + bor.getNumOgg().get(1) + "\n" +
						"    PERGAMENA: " + bor.getNumOgg().get(2) + "\n" +
						"   Carta obiettivo segreta: " + "\n   ";
		String board = "Carte in campo:" + "\n";
		
		for (int i = 0; i < mat.length; i++) {
			board += "    ";
			for (int j = 0; j < mat[i].length; j++) {
				if(mat[i][j] == null) {
					board += "     ";
				}else {
					if(mat[i][j].charAt(0) == 'I') {
						board += AnsiEscapeCodes.WHITE_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + mat[i][j] + AnsiEscapeCodes.ENDING_CODE.getCode();
					} else {
						switch(mat[i][j].charAt(1)) {
						case 'R':
							board += AnsiEscapeCodes.RED_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + mat[i][j] + AnsiEscapeCodes.ENDING_CODE.getCode();
							break;
						case 'B':
							board += AnsiEscapeCodes.CYAN_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode( ) + mat[i][j] + AnsiEscapeCodes.ENDING_CODE.getCode();
							break;
						case 'V':
							if(mat[i][j].charAt(2) == 'R') {
								board += AnsiEscapeCodes.GREEN_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + mat[i][j] + AnsiEscapeCodes.ENDING_CODE.getCode();
							} else if(mat[i][j].charAt(2) == 'L') {
								board += AnsiEscapeCodes.VIOLET_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + mat[i][j] + AnsiEscapeCodes.ENDING_CODE.getCode();
							}
							break;
						default:
							board += "     ";
							break;
						}
					}
				}
				
				if (j == mat.length - 1) {
					board += "\n   ";
				}
			}
		}
		
		System.out.println(intro);
		showObjective(bor.getObiettivo().showCard(), bor.getObiettivo().getObiettivo().getDisposizione());
		System.out.println("\n" + board);
	}
	
	/**
	 * Metodo che stampa a schermo lo stato del giocatore di turno, ossia:
	 * la board, le carte che ha in mano.  
	 * @param g
	 */
	public void showPlayerStatus(Giocatore g) {
		System.out.println("Informazioni sullo stato del giocatore di turno:");
		this.showBoard(g);
		this.showHand(g.getNick(), g.getMano());
	}
	
	/**
	 * Metodo che mostra i mazzi e le carte scoperte presenti sul campo di gioco. 
	 */
	public void showField(CampoDiGioco campo) {
		System.out.println("SUL CAMPO DA GIOCO CI SONO QUESTE CARTE:");
		System.out.println("CIMA DEI MAZZI RISORSA E ORO:");
		campo.getMazzoR().showRetro(0).showCard();
		campo.getMazzoO().showRetro(0).showCard();
		System.out.println("CARTE SCOPERTE:");
	
		for (CartaRisorsa r : campo.getRisorsa()) {
			  System.out.println(r.showCard());
		}
		
		for (CartaOro o : campo.getOro()) {
			  System.out.println(o.showCard());
		}
		
		System.out.println("OBIETTIVI COMUNI:");
		
		for (CartaObiettivo ob : campo.getObiettivo()) {
			  showObjective(ob.showCard(), ob.getObiettivo().getDisposizione());
		}
	}
	
	/**
	 * Metodo che mostra le carte in mano di un giocatore.
	 * @param nick
	 * @param mano
	 */
	public void showHand(String nick, Mano mano) {
		System.out.println("\nMANO DI " + nick + ":");
		
		for (CartaRisorsa r : mano.getRisorsa()) {
			  System.out.println(r.showCard());
		}
		
		for (CartaOro o : mano.getOro()) {
			  System.out.println(o.showCard());
		}	
	}
	
	/**
	 * Metodo che mostra al giocatore di turno il suo obiettivo segreto.
	 * @param g
	 * @param board
	 */
	public void showSecretObjective(Giocatore g, Board board) {
		System.out.println("OBIETTIVO SEGRETO DI " + g.getNick());
		System.out.println(board.getObiettivo().showCard());
	}
	/**
	 * Metodo che chiede al giocatore di turno se ha intenzione di passare la mano.
	 * @return
	 */
	public boolean passaMano() {
		System.out.println("\nVuoi passare la mano al prossimo giocatore? (SI/NO)");
		
		while(true) {
			try {
				String check = SCANNER.nextLine();
				if(check.equalsIgnoreCase("NO")) {
					return false;
				}else if(check.equalsIgnoreCase("SI")){
					return true;
				}else {
					throw new IOException();
				}
			} catch(IOException e){
				System.out.println("Risposta non ammessa, riprova con SI o NO");
			}
		}
	}
	
	/**
	 * Metodo che chiede al giocatore di turno quale carta posizionare.
	 * @return
	 */
	public String chooseWhatToPlace() {
		System.out.println("Inserisci il codice della carta che vuoi posizionare:");
		return SCANNER.nextLine();
	}
	
	/**
	 * Metodo che stampa il messaggio di errore di codice non valido.
	 */
	public void insertAValidCode() {
		System.out.println("Codice della carta non valido, riprova con uno dei codici disponibili");
	}
	
	/**
	 * Metodo che stampa il messaggio di errore dell'inserimento della carta in
	 * quanto non soddisfa i requisiti.
	 */
	public void showRequirementMessage() {
		System.out.println("La carta non soddisfa i requisiti, scegliere un'altra carta");
	}
	
	/**
	 * Metodo che chiede all'utente se intende piazzare la carta scelta 
	 * mostrando il fronte o il retro.
	 * @param gio
	 * @param fronte
	 * @param retro
	 * @return
	 */
	public boolean chooseWhichSide(String gio, Carta fronte, Carta retro) {
		
		String f = "";
		String r = "";
		
		if (fronte.getId().charAt(0)=='R') {
			f = ((CartaRisorsa)fronte).showCard();
			r = ((CartaRisorsa)retro).showCard();
		} else {
			f = ((CartaOro)fronte).showCard();
			r = ((CartaOro)retro).showCard();
		}
		
		System.out.println("\nQuesta è la carta scelta da " + gio + ":\n");
		System.out.println("FRONTE:");
		System.out.println(f);
		System.out.println("\nRETRO:");
		System.out.println(r);
		System.out.println("\n" + "Come vuoi posizionare la carta? (FRONTE/RETRO)");
		
		while(true) {
			try {
				String ris = SCANNER.nextLine();
				if(ris.equalsIgnoreCase("FRONTE")) {
					return true;
				} else if (ris.equalsIgnoreCase("RETRO")) {
					return false;
				} else {
					throw new IOException();
				}
			}catch(IOException e) {
				System.out.println("Risposta non valida, riprova con FRONTE o RETRO");
			}
		}
	}		
	
	/**
	 * Metodo che chiede al giocatore di turno quale carta coprire con quella 
	 * che viene posizionata.
	 * @return
	 */
	public String chooseWhatToCover() {
		System.out.println("Inserisci il codice della carta che vuoi coprire:");
		return SCANNER.nextLine();
	}
	
	/**
	 * Metodo che stampa il messaggio di apertura della lista degli angoli 
	 * disponibili.
	 */
	public void showFreeCornersMessage() {
		System.out.println("Gli angoli disponibili sono:\n");
	}
	
	/**
	 * Metodo che stampa tutti gli angoli iniziali liberi.
	 * @param cardR
	 * @param angolo
	 */
	public void showFreeInitialCorners(CartaIniziale cardI, ArrayList<Angolo> angolo) {
	System.out.println(cardI.getId() + ":\n");
		
		for(Angolo a: angolo) {
			System.out.println("   " + a.showAngolo() + "\n");
		}
	}
	
	/**
	 * Metodo che stampa tutti gli angoli risorsa liberi.
	 * @param cardR
	 * @param angolo
	 */
	public void showFreeResourceCorners(CartaRisorsa cardR, ArrayList<Angolo> angolo) {
		System.out.println(cardR.getId() + ":\n");
		
		for(Angolo a: angolo) {
			System.out.println("   " + a.showAngolo() + "\n");
		}
	}
	
	/**
	 * Metodo che stampa tutti gli angoli oro liberi.
	 * @param cardO
	 * @param angolo
	 */
	public void showFreeGoldCorners(CartaOro cardO, ArrayList<Angolo> angolo) {
		System.out.println(cardO.getId() + ":\n");
		
		for(Angolo a: angolo) {
			System.out.println("   " + a.showAngolo() + "\n");
		}
	}
	
	
	public Posizione chooseWhichCorner() {
		System.out.println("Scegli l'angolo che vuoi coprire: (ADX/BDX/BSX/ASX)");
		while(true) {
			try {
				String res = SCANNER.nextLine();
				
				
				switch(res.toUpperCase()) {
				case "ADX":
					return Posizione.ADX;
				case "BDX":
					return Posizione.BDX;
				case "BSX":
					return Posizione.BSX;
				case "ASX":
					return Posizione.ASX;
				default:
					throw new IOException();
				}
			} catch(IOException e){
				System.out.println("Risposta non ammessa, riprova con ADX, BDX, BSX, ASX");
			}
		}
	}
	
	public void isFullMessage() {
		System.out.println("La posizione scelta è gia occupata");
	}
	public void showPlaceErrorMessage() {
		System.out.println("Un angolo nascosto non può coprire un angolo vuoto. Controlla e riprova");
	}
	
	public void showImpossiblePlaceMessage() {
		System.out.println("Non è possibile coprire un angolo nascosto");
	}
}
