package application;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Questa classe rappresenta il launcher del programma.(DA RIGUARDARE IL COMMENTO).
 */
public class Application {
	
	public static void main(String[] args) throws IOException {
		Scanner sc = new Scanner(System.in);
		String risposta;
		System.out.println("CODEX NATURALIS\n");
		System.out.println("Benvenuti!!! Volete giocare?");
		
		while(true) {
			risposta = sc.nextLine();
				if(risposta.equals("SI") || risposta.equals("NO")) {
					break;
				}
			System.out.println("Risposta errata, riprova con SI o NO");
		}
		
		if(risposta.equals("NO")) {
			System.out.println("\nArrivederci!!!");
			System.exit(0);
		}
		
		System.out.println("Inserire il numero di giocatori:");
		int giocatori;
		
		while(true) {

			try {
				giocatori = sc.nextInt();
				if(giocatori < 2 || giocatori > 4) {
					throw new IOException("Unacceptable response");
				}else {
					break;
				}
			} catch(IOException e) {
				System.out.println("Il numero minimo di giocatori è 2 e il numero massimo di giocatori è 4");
			} catch(InputMismatchException e) {
				System.out.println("La risposta non è accettabile, riprova con un numero");
			} finally {
				sc.nextLine();
			}
		}
	
	}
}
