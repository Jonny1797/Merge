package Pokern;

import java.time.Clock;

public class Main {

//	long startGeld = 5000;
//	int[] sBlind = {100, 200, 400, 500, 1000, 2000, 4000, 5000};
//	int sBlindIndex;

	public static void main(String[] args) {

		Thread threadTisch = new Thread( new Tisch() );
		threadTisch.start();

//		//Erstelle Spieler
//		Spieler Eberhart = new Spieler("Eberhard");
//		Spieler Guenther = new Spieler("Guenther");
//		//Erstelle einen Tisch und ein Kartendeck
//		Tisch tisch = new Tisch();
//		KartenDeck deck = new KartenDeck();
//		//Füge die Spieler zum Tisch hinzu
//		tisch.fuegeSpielerHinzu(Eberhart);
//		tisch.fuegeSpielerHinzu(Guenther);
//		//Gebe den Mitspielern das Startgeld
//		tisch.setStartGeld(getStartGeld());
//		//Mach dir ein Kartendeck und mische es
//		deck.erstelleEinDeck();
//		deck.mischeDasDeck();
//		//Setze den BigBlind und Dealer
//		tisch.setSmallBlindValue(getSmallBlind());
//		tisch.setDealer();
//		//what ever
//		if(tisch.wieVielerSpieler() > 2){
//			//entweder definiert man einfach die Blinds über den Dealer
//			//oder wir machen dafür auch noch Variablen!?
//		}else if (tisch.wieVielerSpieler() == 2){
//			//tisch.nextDealer().geld = tisch.bBlindValue /2;
//			//Die Spieler bekommen Karten
//			tisch.givePlayerCards();
//		}else{
//			//Letzter Spieler hat gewonnen!!!
//
//		}
		System.out.println("Ende der Main");
	}
//
//	public long getStartGeld(){
//		return startGeld;
//	}
//
//	private int getSmallBlind(){
//		return sBlind[sBlindIndex];
//	}
//
//	private int nextBlind (){
//		if(sBlindIndex++ <= sBlind.length){
//			return sBlindIndex++;
//		}else{
//			return sBlindIndex;
//		}
//
//	}
}