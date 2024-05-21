package application.view;

import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;

import application.model.CartaIniziale;
import application.model.CartaObiettivo;
import application.model.Giocatore;
import application.model.Pedina;

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
	public boolean startMessage() {
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
					throw new Exception();
				}
			} catch(Exception e){
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
					throw new Exception();
				}
			}catch(Exception e) {
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
		
		System.out.println("\nQuesta è la carta iniziale di " + gio + "\n");
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
	
	public boolean chooseObjectiveCard(String gio, CartaObiettivo obi1, CartaObiettivo obi2) {
		
		System.out.println("\nQueste sono le due carte obiettivo del giocatore " + gio + ":" );
		System.out.println("\nCARTA 1:");
		System.out.println(obi1.showCard());
		System.out.println("\nCARTA 2:");
		System.out.println(obi2.showCard());
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
}
