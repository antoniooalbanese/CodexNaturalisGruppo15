package application.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

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
import application.model.Mano;
import application.model.Pedina;
import application.model.Posizione;
import application.model.Regno;

/**
 * Classe che rappresenta l'interfaccia di output e input 
 * utilizzata dai giocatori.
 */
public class View {
	/**
	 * Attributo che gestisce gli input da console.
	 */
	private static final Scanner SCANNER = new Scanner(System.in);
	
	/**
	 * Metodo che stampa il messaggio di inizio del gioco.
	 * @return TRUE se i giocatori decidono di iniziare a giocare,
	 * FALSE se i giocatori decidono di non giocare
	 * @throws IOException quando la risposta non è tra quelle
	 * ammesse(SI o NO)
	 */
	public boolean welcomeMessage() throws IOException{
		System.out.println("\n				╦ ╦╔═╗╦  ╔═╗╔═╗╔╦╗╔═╗  ╔╦╗╔═╗\n"
				+ "				║║║║╣ ║  ║  ║ ║║║║║╣    ║ ║ ║\n"
				+ "				╚╩╝╚═╝╩═╝╚═╝╚═╝╩ ╩╚═╝   ╩ ╚═╝\n\n");
		
		System.out.println("  ██████╗ ██████╗ ██████╗ ███████╗██╗  ██╗    ███╗   ██╗ █████╗ ████████╗██╗   ██╗██████╗  █████╗ ██╗     ██╗███████╗\n"
				+ " ██╔════╝██╔═══██╗██╔══██╗██╔════╝╚██╗██╔╝    ████╗  ██║██╔══██╗╚══██╔══╝██║   ██║██╔══██╗██╔══██╗██║     ██║██╔════╝\n"
				+ " ██║     ██║   ██║██║  ██║█████╗   ╚███╔╝     ██╔██╗ ██║███████║   ██║   ██║   ██║██████╔╝███████║██║     ██║███████╗\n"
				+ " ██║     ██║   ██║██║  ██║██╔══╝   ██╔██╗     ██║╚██╗██║██╔══██║   ██║   ██║   ██║██╔══██╗██╔══██║██║     ██║╚════██║\n"
				+ " ╚██████╗╚██████╔╝██████╔╝███████╗██╔╝ ██╗    ██║ ╚████║██║  ██║   ██║   ╚██████╔╝██║  ██║██║  ██║███████╗██║███████║\n"
				+ "  ╚═════╝ ╚═════╝ ╚═════╝ ╚══════╝╚═╝  ╚═╝    ╚═╝  ╚═══╝╚═╝  ╚═╝   ╚═╝    ╚═════╝ ╚═╝  ╚═╝╚═╝  ╚═╝╚══════╝╚═╝╚══════╝\n"
				+ "");
		
		System.out.println("\n╔════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╗");
		
		System.out.println("\nVolete giocare? (SI/NO)");
		
		while(true) {
			try {
				String res = SCANNER.nextLine();
				if(res.equalsIgnoreCase("NO")) {
					return false;
				}else if(res.equalsIgnoreCase("SI")){
					System.out.println("\n╚════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╝");
					return true;
				}else {
					throw new IOException();
				}
			}catch(IOException e){
				System.out.println("Risposta non ammessa, riprova con SI o NO");
			}
		}
	}
	
	 /**
	  * Metodo che stampa il messaggio di chiusura del programma.
	  */
	public void endMessage() {
		System.out.println("\n\n ________   ______    ______     ________  __   __   ______   ______   ______   ______    ______    ________    \n"
				+ "/_______/\\ /_____/\\  /_____/\\   /_______/\\/_/\\ /_/\\ /_____/\\ /_____/\\ /_____/\\ /_____/\\  /_____/\\  /_______/\\   \n"
				+ "\\::: _  \\ \\\\:::_ \\ \\ \\:::_ \\ \\  \\__.::._\\/\\:\\ \\\\ \\ \\\\::::_\\/_\\:::_ \\ \\\\::::_\\/_\\:::_ \\ \\ \\:::__\\/  \\__.::._\\/   \n"
				+ " \\::(_)  \\ \\\\:(_) ) )_\\:(_) ) )_   \\::\\ \\  \\:\\ \\\\ \\ \\\\:\\/___/\\\\:\\ \\ \\ \\\\:\\/___/\\\\:(_) ) )_\\:\\ \\  __   \\::\\ \\    \n"
				+ "  \\:: __  \\ \\\\: __ `\\ \\\\: __ `\\ \\  _\\::\\ \\__\\:\\_/.:\\ \\\\::___\\/_\\:\\ \\ \\ \\\\::___\\/_\\: __ `\\ \\\\:\\ \\/_/\\  _\\::\\ \\__ \n"
				+ "   \\:.\\ \\  \\ \\\\ \\ `\\ \\ \\\\ \\ `\\ \\ \\/__\\::\\__/\\\\ ..::/ / \\:\\____/\\\\:\\/.:| |\\:\\____/\\\\ \\ `\\ \\ \\\\:\\_\\ \\ \\/__\\::\\__/\\\n"
				+ "    \\__\\/\\__\\/ \\_\\/ \\_\\/ \\_\\/ \\_\\/\\________\\/ \\___/_(   \\_____\\/ \\____/_/ \\_____\\/ \\_\\/ \\_\\/ \\_____\\/\\________\\/!!!");
	}
	
	/**
	 * Metodo che chiede il numero di giocatori.
	 * @return numero di giocatori
	 * @throws IOException quando la risposta non è tra quelle
	 * ammesse(un numero compreso tra 2 e 4 compresi)
	 * @exception InputMismatchException quando la risposta non è 
	 * un numero 
	 */
	public int getPlayersNumberMessage() throws IOException {
		System.out.println("\n╔════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╗");
		System.out.print("\nInserire il numero di giocatori:");
		int giocatori;
		
		while(true) {
			try {
				giocatori = SCANNER.nextInt();
				if(giocatori < 2 || giocatori > 4) {
					throw new IOException("Unacceptable response");
				}else {
					System.out.println("\n╚════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╝");
					return giocatori;
				}
			}catch(IOException e) {
				System.out.println("Il numero minimo di giocatori è 2 e il numero massimo di giocatori è 4");
			}catch(InputMismatchException e) {
				System.out.println("La risposta non è accettabile, riprova con un numero");
			}finally {
				SCANNER.nextLine();
			}
		}
	}
	
	/**
	 * Metodo che chiede il nickname del giocatore.
	 * @param n: numero del giocatore a cui si chiede il nickname da 
	 * utilizzare durante la partita
	 * @return nickname scelto dal giocatore 
	 * @throws IOException quando la risposta non è tra quelle
	 * ammesse(SI o NO)
	 */
	public String getNick(int n) throws IOException {
		String giocatore;
		System.out.println("\n╔════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╗");
		
		do {
			System.out.println("\nInserire il nickname del giocatore " + n +":");
			giocatore = SCANNER.nextLine();
		} while(giocatore.length()==0);
		
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
			}catch(IOException e){
				System.out.println("Risposta non ammessa, riprova con SI o NO");
			} 
		}
	}
	
	/**
	 * Metodo che chiede al giocatore la pedina scelta. 
	 * @param n: numero del giocatore a cui si chiede di selezionare la
	 * pedina da utilizzare durante la partita tra quelle disponibili
	 * @param ped: lista di pedine rimanenti che possono essere scelte
	 * dai giocatori
	 * @return pedina scelta dal giocatore
	 * @throws IOException quando la risposta non è tra quelle
	 * ammesse(ROSSO, BLU, VERDE o GIALLO)
	 */
	public Pedina getPedina(int n, ArrayList<Pedina> ped)throws IOException {
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
					System.out.println("\n╚════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╝");
					return colore;
				case "BLU":
					System.out.println("\n╚════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╝");
					colore = Pedina.BLU;
					return colore;
				case "VERDE":
					System.out.println("\n╚════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╝");
					colore = Pedina.VERDE;
					return colore;
				case "GIALLO":
					System.out.println("\n╚════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╝");
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
	 * Metodo che avvisa l'utente che la pedina da lui selezionata 
	 * è già stata scelta da un altro giocatore precedentemente.
	 */
	public void retry() {
		System.out.println("\n╔════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╗");
		System.out.println("\nLa pedina è già stata scelta");
		
	}
	
	/**
	 * Metodo che chiede all'utente se intende piazzare la carta
	 * iniziale mostrando il fronte oppure mostrando il retro.
	 * @param gio: nickname del giocatore che deve scegliere se
	 * posizionare la propria carta iniziale vista sul fronte oppure
	 * vista sul retro
	 * @param fronte: carta iniziale vista sul fronte
	 * @param retro: carta iniziale vista sul retro
	 * @return TRUE se il giocatore decide di posizionare la carta
	 * iniziale vista sul fronte, FALSE se il giocatore decide di 
	 * posizionare la carta iniziale vista sul retro
	 * @throws IOException quando la risposta non è tra quelle
	 * ammesse(FRONTE o RETRO)
	 */
	public boolean chooseStartCard(String gio, CartaIniziale fronte, CartaIniziale retro) throws IOException{
		System.out.println("\n╔════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╗");
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
					System.out.println("\n╚════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╝");
					return true;
				}else if (ris.equalsIgnoreCase("RETRO")) {
					System.out.println("\n╚════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╝");
					return false;
				}else {
					throw new IOException();
				}
			}catch(IOException e) {
				System.out.println("Risposta non valida, riprova con FRONTE o RETRO");
			}
		}
	}	
	
	/**
	 * Metodo che chiede all'utente quale delle due carte obiettivo 
	 * che ha ricevuto intende tenere e quale invece intende scartare.
	 * @param gio: nickname del giocatore che deve scegliere quale 
	 * delle carte obiettivo pescate intende tenere e quale intende
	 * scartare
	 * @param obi1: prima carta obiettivo pescata
	 * @param obi2: seconda carta obiettivo pescata
	 * @return TRUE se il giocatore decide di tenere la prima carta
	 * obiettivo, FALSE se il giocatore decide di tenere la seconda
	 * carta obiettivo
	 * @throws IOException quando la risposta non è tra quelle
	 * ammesse(1 o 2)
	 */
	public boolean chooseObjectiveCard(String gio, CartaObiettivo obi1, CartaObiettivo obi2) throws IOException {
		System.out.println("\n╔════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╗");
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
					System.out.println("\n╚════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╝");
					return true;
				}else if (ris.equalsIgnoreCase("2")) {
					System.out.println("\n╚════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╝");
					return false;
				}else {
					throw new IOException();
				}
			}catch(IOException e) {
				System.out.println("Risposta non valida, riprova con 1 o 2");
			}
		}
	}
	
	/**
	 * Metodo che gestisce la visualizzazione degli obiettivi
	 * di tipo disposizione.
	 * @param riga: stringa contenente tutte le informazioni relative
	 * alla carta obiettivo che contiene l'obiettivo in questione
	 * @param dispo: matrice che rappresenta la disposizione che deve
	 * essere rispettata per ottenere i punti assegnabili dalla
	 * carta obiettivo
	 */
	public void showObjective(String riga, Regno[][]dispo) {
		String regex = "[\n]";
		String [] righe = riga.split(regex);
		String line = "   ";
		
		if(righe[3].contains("disposizione")) {
			for(int i = 0; i < 4; i++) {
				for(int j = 0; j < 4; j++) {
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
					
					if (j == 3) {
						line += "\n   ";
					}
				}
			}
		}
		
		System.out.print(righe[0] + "\n" + righe[1] + "\n" + righe[2] + "\n" + righe[3] + "\n" + line);
		System.out.println("\n｡☆✼★━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━★✼☆｡");
	}
	
	/**
	 * Metodo che mostra qual è l'ordine di gioco che i giocatori 
	 * dovranno seguire.
	 * @param ordine: lista dei giocatori che giocheranno la partita
	 */
	public void showNewOrder(ArrayList<Giocatore> ordine) {
		String nuovoOrdine = "";
		
		for (int i=0; i < ordine.size(); i++) {
			nuovoOrdine += "    " + (i+1) + "." + ordine.get(i).getNick();
			nuovoOrdine += "\n";
		}
		
		System.out.println("\n╔════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╗");
		System.out.println("\nQuesto è l'ordine di gioco:\n\n" + nuovoOrdine);
		System.out.println("\n╚════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╝");
		
	}
	
	/**
	 * Metodo che mostra in output il messaggio di fine dei 
	 * preparativi ed inizio della partita vera e propria.
	 */
	public void startMessage() {
		System.out.println("\n╔════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╗\n");
		System.out.println("			  ╔═╗╦═╗╔═╗╔═╗╔═╗╦═╗╔═╗╔╦╗╦╦  ╦╦  ╔╦╗╔═╗╦═╗╔╦╗╦╔╗╔╔═╗╔╦╗╦\n"
				+ "			  ╠═╝╠╦╝║╣ ╠═╝╠═╣╠╦╝╠═╣ ║ ║╚╗╔╝║   ║ ║╣ ╠╦╝║║║║║║║╠═╣ ║ ║\n"
				+ "			  ╩  ╩╚═╚═╝╩  ╩ ╩╩╚═╩ ╩ ╩ ╩ ╚╝ ╩   ╩ ╚═╝╩╚═╩ ╩╩╝╚╝╩ ╩ ╩ ╩");
		System.out.println("	╔═╗╦ ╦╔═╗  ╦  ╔═╗  ╦  ╦╔═╗╔═╗╔╦╗╦═╗╔═╗  ╔═╗╔═╗╦═╗╔╦╗╦╔╦╗╔═╗  ╔═╗╔╗ ╔╗ ╦╔═╗  ╦╔╗╔╦╔═╗╦╔═╗\r\n"
				+ "	║  ╠═╣║╣   ║  ╠═╣  ╚╗╔╝║ ║╚═╗ ║ ╠╦╝╠═╣  ╠═╝╠═╣╠╦╝ ║ ║ ║ ╠═╣  ╠═╣╠╩╗╠╩╗║╠═╣  ║║║║║╔═╝║║ ║\r\n"
				+ "	╚═╝╩ ╩╚═╝  ╩═╝╩ ╩   ╚╝ ╚═╝╚═╝ ╩ ╩╚═╩ ╩  ╩  ╩ ╩╩╚═ ╩ ╩ ╩ ╩ ╩  ╩ ╩╚═╝╚═╝╩╩ ╩  ╩╝╚╝╩╚═╝╩╚═╝");
		System.out.println("\n╚════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╝");
	}
	
	/**
	 * Metodo che dice ai giocatori che i mazzi sono terminati.
	 * @param over: booleano che descrive se i mazzi sono entrambi 
	 * terminati(TRUE se sono terminati e FALSE se invece non sono
	 * ancora terminati)
	 * @return TRUE se i mazzi sono terminati, FALSE se i mazzi non 
	 * sono ancora terminati
	 */
	public boolean showDecksAreOverMessage(boolean over) {
		if(over) {
			System.out.println("I mazzi sono entrambi terminati");
			return true;
		}
		
		return false;
	}
	
	/**
	 * Metodo che avvisa i giocatori che stanno per giocare 
	 * il loro ultimo turno.
	 * @param last: booleano che indica se il giocatore di turno sta 
	 * per giocare il suo ultimo turno
	 */
	public void tellLastTurn(boolean last) {
		if(last) {
			System.out.println("\nÈ L'ULTIMO TURNO");
		}
	}
	
	/**
	 * Metodo che avvisa i giocatori a chi appartiene il turno 
	 * che sta per essere giocato.
	 * @param nick: nickname del giocatore di turno
	 */
	public void tellWhoseTurn(String nick) {
		System.out.println("\n╔════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╗\n");
		System.out.println("TURNO DI: " + nick + "\n" );
	}
	
	/**
	 * Metodo che mostra in output le board(carte in campo) dei 
	 * giocatori.
	 * @param g: giocatore di turno
	 * @param giocatori: lista dei giocatori che stanno giocando la
	 * partita
	 */
	public void showAllBoards(Giocatore g, ArrayList<Giocatore> giocatori) {
		System.out.println("Informazioni sul campo degli altri giocatori:");
		this.showOtherBoards(g, giocatori);
		this.showPlayerStatus(g);
	}
	
	/**
	 * Metodo che stampa a schermo le board degli altri giocatori che
	 * non sono di turno.
	 * @param g: giocatore di turno
	 * @param giocatori: lista dei giocatori che stanno giocando la
	 * partita
	 */
	public void showOtherBoards(Giocatore g, ArrayList<Giocatore> giocatori) {
		for(int i = 0; i < giocatori.size(); i++) {
			if(giocatori.get(i) != g) {
				this.showEnemyBoard(giocatori.get(i));
			}
		}
	}
	
	/**
	 * Metodo che stampa a schermo la board di un avversario.
	 * @param g: generico giocatore avversario
	 */
	public void showEnemyBoard(Giocatore g) {
		Board bor = g.getBoard();
		String [][] mat = bor.getMatrix();
		String intro =  "\n──────────────────────────────────── ❝ CAMPO DI " + g.getNick() + " ❞ ────────────────────────────────────" + "\n" +
						"   Turni giocati: " + bor.getTurno() + "\n" +
						"   Punteggio: " + bor.getPunteggio() + "\n" +
						"   Risorse presenti negli angoli delle carte posizionate sul campo:" + "\n" +
						"     VEGETALE: " + bor.getNumRis().get(0) + "\n" +
						"     ANIMALE: " + bor.getNumRis().get(1) + "\n" +
						"     FUNGHI: " + bor.getNumRis().get(2) + "\n" +
						"     INSETTI: " + bor.getNumRis().get(3) + "\n" +
						"   Oggetti presenti negli angoli delle carte posizionate sul campo:" + "\n" + 
						"     PIUMA: " + bor.getNumOgg().get(0) + "\n" + 
						"     INCHIOSTRO " + bor.getNumOgg().get(1) + "\n" +
						"     PERGAMENA: " + bor.getNumOgg().get(2);
		String board = "Carte in campo:" + "\n   ";
		
		for (int i = 0; i < mat.length; i++) {
			board += "     ";
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
		System.out.println("\n─────────────────────────────────────────────────────────────────────────────────────────────\n");
	}
	
	
	/**
	 * Metodo che stampa a schermo la board di un determinato giocatore.
	 * @param g: giocatore di cui si stampa la board
	 */
	public void showBoard(Giocatore g) {
		Board bor = g.getBoard();
		String [][] mat = bor.getMatrix();
		Regno[][] disp = bor.getObiettivo().getObiettivo().getDisposizione();
		String intro =  "\n──────────────────────────────────── ❝ CAMPO DI " + g.getNick() + " ❞ ────────────────────────────────────" + "\n" +
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
		String board = "Carte in campo:" + "\n   ";
		
		for (int i = 0; i < mat.length; i++) {
			board += "     ";
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
		showObjective(bor.getObiettivo().showCard(), disp);
		System.out.println("\n" + board);
		System.out.println("\n─────────────────────────────────────────────────────────────────────────────────────────────");
	}
	
	/**
	 * Metodo che stampa a schermo lo stato del giocatore di turno, 
	 * ossia: la board e le carte che ha in mano.  
	 * @param g: giocatore di turno di cui si stampano le informazioni
	 * a lui utili sulla propria condizione di gioco
	 */
	public void showPlayerStatus(Giocatore g) {
		System.out.println("Informazioni sullo stato del giocatore di turno:");
		this.showBoard(g);
		this.showHand(g.getNick(), g.getMano());
	}
	
	/**
	 * Metodo che mostra i mazzi e le carte scoperte presenti 
	 * sul campo di gioco. 
	 * @param campo: campo di gioco dove si svolge la partita
	 * contenente i vari mazzi e i giocatori
	 */
	public void showField(CampoDiGioco campo) {
		System.out.println("\nSUL CAMPO DA GIOCO CI SONO QUESTE CARTE:");
		System.out.println("\n\nCIMA DEI MAZZI RISORSA E ORO:");
		System.out.println(campo.getMazzoR().getRetroCarta(campo.getMazzoR().getMazzoFronte().get(0)).showCard());
		System.out.println("｡☆✼★━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━★✼☆｡");
		System.out.println(campo.getMazzoO().getRetroCarta(campo.getMazzoO().getMazzoFronte().get(0)).showCard());
		System.out.println("｡☆✼★━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━★✼☆｡");
		System.out.println("\n\nCARTE SCOPERTE:");
	
		for (CartaRisorsa r : campo.getRisorsa()) {
			System.out.println(r.showCard());
			System.out.println("｡☆✼★━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━★✼☆｡");
		}
		
		for (CartaOro o : campo.getOro()) {
			System.out.println(o.showCard());
			System.out.println("｡☆✼★━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━★✼☆｡");
		}
		
		System.out.println("\n\nOBIETTIVI COMUNI:");
		
		for (CartaObiettivo ob : campo.getObiettivo()) {
			showObjective(ob.showCard(), ob.getObiettivo().getDisposizione());
		}
		
	}
	
	/**
	 * Metodo che mostra le carte in mano di un giocatore.
	 * @param nick: nickname del giocatore di cui si mostra la mano
	 * @param mano: mano del giocatore che viene mostata
	 */
	public void showHand(String nick, Mano mano) {
		System.out.println("\n╔════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╗");
		System.out.println("\nMANO DI " + nick + ":");
		
		for (CartaRisorsa r : mano.getRisorsa()) {
			System.out.println(r.showCard());
			System.out.println("｡☆✼★━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━★✼☆｡");
		}
		
		for (CartaOro o : mano.getOro()) {
			System.out.println(o.showCard());
			System.out.println("｡☆✼★━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━★✼☆｡");
		}
	}
	
	/**
	 * Metodo che mostra al giocatore di turno il suo obiettivo segreto.
	 * @param g: giocatore di turno
	 * @param board: carte presenti sul campo del giocatore di turno
	 */
	public void showSecretObjective(Giocatore g, Board board) {
		System.out.println("OBIETTIVO SEGRETO DI " + g.getNick());
		System.out.println(board.getObiettivo().showCard());
	}
	
	/**
	 * Metodo che chiede al giocatore di turno se ha intenzione
	 * di passare la mano.
	 * @return TRUE se il giocatore di turno intende passare la mano,
	 * FALSE se il giocatore di turno non intende passare la mano
	 * @throws IOException quando la risposta non è tra quelle
	 * ammesse(SI o NO)
	 */
	public boolean passaMano() throws IOException {
		System.out.println("\nVuoi passare la mano al prossimo giocatore? (SI/NO)");
		
		while(true) {
			try {
				String check = SCANNER.nextLine();
				if(check.equalsIgnoreCase("NO")) {
					return false;
				}else if(check.equalsIgnoreCase("SI")){
					System.out.println("\n╚════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╝");
					return true;
				}else {
					throw new IOException();
				}
			}catch(IOException e){
				System.out.println("Risposta non ammessa, riprova con SI o NO");
			}
		}
	}
	
	/**
	 * Metodo che chiede al giocatore di turno quale carta posizionare.
	 * @return la stringa contenente l'id della carta che il 
	 * giocatore di turno ha scelto di posizionare
	 */
	public String chooseWhatToPlace() {
		System.out.println("Inserisci il codice della carta che vuoi posizionare:");
		String scelta = SCANNER.nextLine().toUpperCase();
		return scelta;
	}
	
	/**
	 * Metodo che stampa il messaggio di errore di codice non valido,
	 * ossia quando il codice non è tra quelli disponibili per
	 * la scelta.
	 */
	public void insertAValidCode() {
		System.out.println("Codice della carta non valido, riprova con uno dei codici disponibili");
	}
	
	/**
	 * Metodo che stampa il messaggio di errore dell'inserimento della
	 * carta in quanto non soddisfa i requisiti.
	 */
	public void showRequirementMessage() {
		System.out.println("La carta non soddisfa i requisiti, scegliere un'altra carta");
	}
	
	/**
	 * Metodo che chiede all'utente se intende piazzare la carta scelta 
	 * mostrando il fronte o il retro.
	 * @param gio: nickname del giocatore che deve scegliere come 
	 * piazzare la carta
	 * @param fronte: carta da posizionare vista sul fronte
	 * @param retro: carta da posizionare vista sul retro
	 * @return TRUE se il giocatore decide di posizionare la carta
	 * vista sul fronte, FALSE se il giocatore decide di posizionare 
	 * la carta vista sul retro
	 * @throws IOException quando la risposta non è tra quelle
	 * ammesse(FRONTE o RETRO)
	 * 
	 */
	public boolean chooseWhichSide(String gio, Carta fronte, Carta retro)throws IOException {
		String f = "";
		String r = "";
		
		if (fronte.getId().charAt(0)=='R') {
			f = ((CartaRisorsa)fronte).showCard();
			r = ((CartaRisorsa)retro).showCard();
		}else {
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
				}else if (ris.equalsIgnoreCase("RETRO")) {
					return false;
				}else {
					throw new IOException();
				}
			}catch(IOException e) {
				System.out.println("Risposta non valida, riprova con FRONTE o RETRO");
			}
		}
	}		
	
	/**
	 * Metodo che chiede al giocatore di turno quale carta coprire 
	 * con quella che vuole posizionare.
	 * @return stringa contenente l'id della carta che il giocatore 
	 * intende coprire con la carta che vuole posizionare 
	 */
	public String chooseWhatToCover() {
		System.out.println("Inserisci il codice della carta che vuoi coprire:");
		return SCANNER.nextLine();
	}
	
	/**
	 * Metodo che stampa il messaggio di apertura della lista
	 * degli angoli disponibili.
	 */
	public void showFreeCornersMessage() {
		System.out.println("\nGli angoli disponibili sono:\n");
	}
	
	/**
	 * Metodo che stampa tutti gli angoli della carta iniziale liberi.
	 * @param cardI: carta iniziale di cui si mostrano gli angoli
	 * @param angolo: lista degli angoli liberi della carta
	 */
	public void showFreeInitialCorners(CartaIniziale cardI, ArrayList<Angolo> angolo) {
		String code = "" + AnsiEscapeCodes.WHITE_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + cardI.getId() + AnsiEscapeCodes.ENDING_CODE.getCode();
		
		System.out.println(code + ":\n");
		
		for(Angolo a: angolo) {
			System.out.println("   " + a.showAngolo() + "\n");
		}
	}
	
	/**
	 * Metodo che stampa tutti gli angoli di una carta risorsa liberi.
	 * @param cardR: carta risorsa di cui si mostrano gli angoli
	 * @param angolo: lista degli angoli liberi della carta
	 */
	public void showFreeResourceCorners(CartaRisorsa cardR, ArrayList<Angolo> angolo) {
		String code = "";
		
		if (cardR.getId().contains("VR")) {
			 code = "" + AnsiEscapeCodes.GREEN_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + cardR.getId() + AnsiEscapeCodes.ENDING_CODE.getCode();  
		}else if (cardR.getId().contains("BL")){
			code = "" + AnsiEscapeCodes.CYAN_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + cardR.getId() + AnsiEscapeCodes.ENDING_CODE.getCode();
		}else if (cardR.getId().contains("RS")) {
			code = "" + AnsiEscapeCodes.RED_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + cardR.getId() + AnsiEscapeCodes.ENDING_CODE.getCode();
		}else if (cardR.getId().contains("VL")) {
			code = "" + AnsiEscapeCodes.VIOLET_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + cardR.getId() + AnsiEscapeCodes.ENDING_CODE.getCode();
		}
		
		System.out.println(code + ":\n");
		
		for(Angolo a: angolo) {
			System.out.println("   " + a.showAngolo() + "\n");
		}
	}
	
	/**
	 * Metodo che stampa tutti gli angoli di una carta oro liberi.
	 * @param cardO: carta oro di cui si mostrano gli angoli
	 * @param angolo: lista degli angoli liberi della carta
	 */
	public void showFreeGoldCorners(CartaOro cardO, ArrayList<Angolo> angolo) {
		String code = "";
		
		if (cardO.getId().contains("VR")) {
			 code = "" + AnsiEscapeCodes.GREEN_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + cardO.getId() + AnsiEscapeCodes.ENDING_CODE.getCode();  
		}else if (cardO.getId().contains("BL")){
			code = "" + AnsiEscapeCodes.CYAN_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + cardO.getId() + AnsiEscapeCodes.ENDING_CODE.getCode();
		}else if (cardO.getId().contains("RS")) {
			code = "" + AnsiEscapeCodes.RED_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + cardO.getId() + AnsiEscapeCodes.ENDING_CODE.getCode();
		}else if (cardO.getId().contains("VL")) {
			code = "" + AnsiEscapeCodes.VIOLET_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + cardO.getId() + AnsiEscapeCodes.ENDING_CODE.getCode();
		}
		
		System.out.println(code + ":\n");
		
		for(Angolo a: angolo) {
			System.out.println("   " + a.showAngolo() + "\n");
		}
	}
	
	/**
	 * Metodo che chiede al giocatore di turno quale angolo vuole
	 * coprire della carta che vuole coprire con la carta che 
	 * vuole posizionare.
	 * @return posizione dell'angolo che il giocatore intende coprire
	 * @throws IOException quando la risposta non è tra quelle
	 * ammesse(ADX, BDX, BSX o ASX) 
	 */
	public Posizione chooseWhichCorner()throws IOException {
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
			}catch(IOException e){
				System.out.println("Risposta non ammessa, riprova con ADX, BDX, BSX, ASX");
			}
		}
	}
	
	/**
	 * Metodo che avvisa il giocatore di turno che l'angolo che intende
	 * coprire è già occupato da un'altra carta.
	 * @param angoli: lista degli angoli liberi che il giocatore può
	 * scegliere di coprire al posto di quello già occupato
	 */
	public void isCornerAlreadyOccupied(ArrayList<Angolo> angoli) {
		String message = "L'angolo selezionato non è disponibile, è già occupato. Riprova con: (";
		
		for(int i = 0; i < angoli.size(); i++) {
			message += angoli.get(i).getPos();
			
			if(i != angoli.size() - 1) {
				message += ", ";
			}else {
				message += "): ";
			}
		}
		
		System.out.println(message);
	}
	
	/**
	 * Metodo che stampa al giocatore di turno un messaggio che dice 
	 * che la posizione da lui scelta è già occupata da un'altra carta.
	 */
	public void isFullMessage() {
		System.out.println("La posizione scelta è gia occupata");
	}
	
	/**
	 * Metodo che stampa al giocatore di turno un messaggio che dice
	 * che un angolo nascosto non può coprire un angolo vuoto.
	 */
	public void showPlaceErrorMessage() {
		System.out.println("Un angolo nascosto non può coprire un angolo vuoto. Controlla e riprova");
	}
	
	/**
	 * Metodo che stampa al giocatore di turno un messaggio che dice
	 * che un angolo nascosto non può essere coperto da nessun altro
	 * angolo.
	 */
	public void showImpossiblePlaceMessage() {
		System.out.println("Non è possibile coprire un angolo nascosto");
	}
	
	/**
	 * Metodo che chiede al giocatore di turno di inserire il codice
	 * della carta che intende pescare.
	 * @return stringa contenente il codice della carta che il
	 * giocatore di turno intende pescare
	 */
	public String chooseWhatToDraw() {		
		System.out.println("Inserisci il codice della carta che vuoi pescare");
		return SCANNER.nextLine();
	}
	
	/**
	 * Metodo che stampa ai giocatori il messaggio che li avvisa che 
	 * la partita è terminata.
	 */
	public void isGameOverMessage() {
		System.out.println("\n╔════════════════════════════════════════════*.·:·.☽✧    ✦    ✧☾.·:·.*════════════════════════════════════════════╗\n");
		System.out.println("    ____  ___    ____ ___________________        ________________  __  ________   _____  _________ \n"
				+ "   / __ \\/   |  / __ /_  __/  _/_  __/   |      /_  __/ ____/ __ \\/  |/  /  _/ | / /   |/_  __/   |\n"
				+ "  / /_/ / /| | / /_/ // /  / /  / / / /| |       / / / __/ / /_/ / /|_/ // //  |/ / /| | / / / /| |\n"
				+ " / ____/ ___ |/ _, _// / _/ /  / / / ___ |      / / / /___/ _, _/ /  / _/ // /|  / ___ |/ / / ___ |\n"
				+ "/_/   /_/  |_/_/ |_|/_/ /___/ /_/ /_/  |_|     /_/ /_____/_/ |_/_/  /_/___/_/ |_/_/  |_/_/ /_/  |_|");
		System.out.print("        ,\t\t\t\t\t\t" + "                              __\n"
				+ "        }`-.   ,          ,\t\t\t\t" + "                            .d$$b\n"
				+ "        \\ \\ '-' \\      .-'{\t\t\t\t" + "                          .' TO$;\\\n"
				+ "        _} .  | ,`\\   /  ' ;    .-;\\\t\t\t" + "                         /  : TP._;\n"
				+ "       {    \\ |    | / `/  '-.,/ ; |\t\t\t" + "                        / _.;  :Tb|\n"
				+ "       { -- -.  '  '`-, .--._.' ;  \\__\t\t\t" + "                       /   /   ;j$j\n"
				+ "        \\     \\ | '  /  |`.    ;    _,`\\\t\t" + "                   _.-\"       d$$$$\n"
				+ "         '. '-     ' `_- '.`;  ; ,-`_.-'\\t\t\t" + "                 .' ..       d$$$$;\n"
				+ "     ,--.  \\    `   /` '--'  `;.` (`  _\t\t\t" + "                /  /P'      d$$$$P. |\\\n"
				+ "  .--.\\  '._) '-. \\ \\ `-.    ;     `-';|\t\t" + "               /   \"      .d$$$P' |\\^\"l\n"
				+ "  '. -. '         __ '.  ;  ;     _,-' /\t\t" + "             .'           `T$P^\"\"\"\"\"  :\n"
				+ "   { __'.\\  ' '-,/; `-'   ';`.- `   .-'\t\t\t" + "         ._.'      _.'                ;\n"
				+ "    '-.  `-._'  | `;     ;`'   .-'`\t\t\t" + "      `-.-\".-'-' ._.       _.-\"    .-\"\n"
				+ "      <_ -'   ` .\\  `;  ;     (_.'`\\\t\t\t" + "    `.-\" _____  ._              .-\"\n"
				+ "      _.;-\"``\"'-._'. `:;  ___, _.-' |\t\t\t" + "   -(.g$$$$$$$b.              .'\n" 
				+ "  .-'\\'. '.` \\ \\_,_`\\ ;##`   `';  _.'\t\t\t" + "     \"\"^^T$$$P^)            .(:\n"
				+ " /_'._\\ \\  \\__;#####./###.      \\`\t\t\t" + "       _/  -\"  /.'         /:/;\n"
				+ " \\.' .'`/\"`/ (#######)###::.. _.'\t\t\t" + "    ._.'-'`-'  \")/         /;/;\n"
				+ "  '.' .'  ; , |:.  `|()##`\"\"\"` \t\t\t\t" + "    ._.'-'`-'  \")/         /;/;\n"
				+ "    `'-../__/_\\::   /O()()o\t\t\t\t" + ".-\" ..--\"\"        -'          :\n"
				+ "             ()'._.'`()()'\t\t\t\t" + "..--\"\"--.-\"         (\\      .-(\\\n");
		System.out.println("\n\n					CLASSIFICA FINALE: ");
		
	}
	
	/**
	 * Metodo che al termine della partita stampa per un giocatore 
	 * il proprio posto in classifica ed il punteggio che ha
	 * conseguito.
	 * @param nick: nickname del giocatore di cui si mostra il posto
	 * in classifica ed il punteggio che ha conseguito
	 * @param punti: punteggio conseguito dal giocatore di cui si
	 * mostra il posto in classifica
	 * @param posizione: posizione in classifica del giocatore di cui
	 * si mostra il punteggio conseguito
	 */
	public void showFinalPoints(String nick, int punti, int posizione) {
		System.out.println("					" + posizione + "° " + nick + ": " + punti + " punti");
	}
	
	/**
	 * Metodo che stampa un messaggio ai giocatori che dice chi è
	 * il vincitore della partita.
	 * @param winner: stringa contenente il nickname del giocatore che
	 * ha vinto la partita
	 */
	public void showWinner(String winner) {
		System.out.println("\n");
		
		if(winner.contains("&")) {
			System.out.println("					I VINCITORI SONO:\n");
		}else {
			System.out.println("					IL VINCITORE È:\n");
		}
		
		System.out.println("					" + AnsiEscapeCodes.RED_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + "!" + AnsiEscapeCodes.ENDING_CODE.getCode()
							+ AnsiEscapeCodes.GREEN_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + "!" + AnsiEscapeCodes.ENDING_CODE.getCode() 
							+ AnsiEscapeCodes.CYAN_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + "!" + AnsiEscapeCodes.ENDING_CODE.getCode()
							+ AnsiEscapeCodes.VIOLET_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + "!" + AnsiEscapeCodes.ENDING_CODE.getCode()
							+ AnsiEscapeCodes.WHITE_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + winner + AnsiEscapeCodes.ENDING_CODE.getCode()
							+ AnsiEscapeCodes.VIOLET_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + "!" + AnsiEscapeCodes.ENDING_CODE.getCode() 
							+ AnsiEscapeCodes.CYAN_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + "!" + AnsiEscapeCodes.ENDING_CODE.getCode()
							+ AnsiEscapeCodes.GREEN_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + "!" + AnsiEscapeCodes.ENDING_CODE.getCode() 
							+ AnsiEscapeCodes.RED_BACKGROUND.getCode() + AnsiEscapeCodes.DEFAULT_TEXT.getCode() + "!" + AnsiEscapeCodes.ENDING_CODE.getCode());
		
		System.out.println("\n\n");
		System.out.print("                `\t\t\t\t\t\t" + "        __.....__\n"
				+ ";,,,             `       '             ,,,;\t\t\t" + "     .'\" _  o    \"`.\n"
				+ "`YES8888bo.       :     :       .od8888YES'\t\t\t" + "   .' O (_)     () o`.\n"
				+ "  888IO8DO88b.     :   :     .d8888I8DO88\t\t\t" + "  .           O       .\n"
				+ "  8LOVEY'  `Y8b.   `   '   .d8Y'  `YLOVE8\t\t\t" + " . ()   o__...__    O  .\n"
				+ " jTHEE!  .db.  Yb. '   ' .dY  .db.  8THEE!\t\t\t" + ". _.--'''''      ''''''--._ .\n"
				+ "   `888  Y88Y    `b ( ) d'    Y88Y  888'\t\t\t" + ":'''                     ''';\n"
				+ "    8MYb  '\"        ,',        \"'  dMY8t\t\t\t" + " `-.__    :   :    __.-'\n"
				+ "   j8prECIOUSgf\"'   ':'   `\"?g8prECIOUSk\t\t\t" + "      '''-:   :-'''\n"
				+ "     'Y'   .8'     d' 'b     '8.   'Y'\t\t\t\t" + "         J     L\n"
				+ "      !   .8' db  d'; ;`b  db '8.   !\t\t\t\t" + "         :     :\n"
				+ "         d88  `'  8 ; ; 8  `'  88b\t\t\t\t" + "         :     :\n"
				+ "        d88Ib   .g8 ',' 8g.   dI88b\t\t\t\t" + "        J       L\n"
				+ "       :888LOVE88Y'     'Y88LOVE888:\t\t\t\t" + "        :       :\n"
				+ "       '! THEE888'       `888THEE !'\t\t\t\t" + "        :       :\n"
				+ "          '8Y  `Y         Y'  Y8'\t\t\t\t" + "        :       :\n"
				+ "           Y                   Y	\t\t\t" + "        `._____.'");
	}
}
