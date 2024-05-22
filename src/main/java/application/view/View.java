package application.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import application.model.CampoDiGioco;
import application.model.CartaIniziale;
import application.model.CartaObiettivo;
import application.model.CartaOro;
import application.model.CartaRisorsa;
import application.model.Giocatore;
import application.model.Mano;
import application.model.Pedina;
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
	 * Metodo che avvisa i giocatori a chi appartiene il turno che sta per essere
	 * giocato.
	 * @param nick
	 */
	public void tellWhoseTurn(String nick) {
		System.out.println("TURNO DI: " + nick );
	}
	
	/**
	 * Metodo che mostra in output le board dei giocatori.
	 * @param n
	 */
	public void showAllBoards(Giocatore g) {
		System.out.println("Carte in campo degli altri giocatori:");
		this.showOtherBoards(g);
		System.out.println("BOARD DI: " + g.getNick());
		this.showPlayerStatus(g);
	}
	
	/**
	 * Metodo che stampa a schermo le board degli altri giocatori che non sono 
	 * di turno.
	 * @param g
	 */
	public void showOtherBoards(Giocatore g) {
		
	}
	
	/**
	 * Metodo che stampa a schermo lo stato del giocatore di turno, ossia:
	 * la board, le carte che ha in mano e l'obiettivo nascosto.  
	 * @param g
	 */
	public void showPlayerStatus(Giocatore g) {
		
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
			  System.out.println(ob.showCard());
		}
	}
	
	/**
	 * Metodo che mostra le carte in mano di un giocatore.
	 * @param nick
	 * @param mano
	 */
	public void showHand(String nick, Mano mano) {
		System.out.println("\nMANO DEL GIOCATORE " + nick + ":");
		
		for (CartaRisorsa r : mano.getRisorsa()) {
			  System.out.println(r.showCard());
		}
		
		for (CartaOro o : mano.getOro()) {
			  System.out.println(o.showCard());
		}	
	}
	
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
}
