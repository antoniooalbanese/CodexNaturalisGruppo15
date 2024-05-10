package application.view;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

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
		
		while(true) {
			try {
				System.out.println("Benvenuti!!! Volete giocare? (SI/NO)");
				String res = SCANNER.nextLine();
				if(res.equalsIgnoreCase("NO")) {
					return false;
				}else {
					return true;
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

}
