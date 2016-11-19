package Pokern;

import static java.lang.Thread.sleep;
import java.util.Scanner;

/**
 * Created by DemonenHerr on 18.11.2016.
 */
public class Benutzer implements Runnable {

    private Spieler sp;
    private Tisch tisch;
    Scanner s = new Scanner(System.in);
    Scanner t = new Scanner(System.in);

    public Benutzer(Spieler sp, Tisch tisch){
        this.sp = sp;
        this.tisch = tisch;
    }

    @Override
    public void run() {
        while(sp.istDabei){
            try {
                wait(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(sp.istDran){
                System.out.println("/n/n/n/n/n/n/n/n/n/n");
                System.out.println("Spieler: " + sp.name);
                System.out.println("Geld: " + sp.wieVielGeld());
                System.out.println("Geld gesetzt: " + sp.pod);
                //System.out.println("Pod: " + sp.gesamtPod);
                System.out.print("Handkarten: ");
                if(sp.handKarten.size() == 2){
                    System.out.print("" + sp.handKarten.get(0).getColor() + sp.handKarten.get(0).getColor());
                    System.out.println("    " + sp.handKarten.get(1).getColor() + sp.handKarten.get(1).getValue());
                }else{
                    System.out.println("Es fehlen noch Karten");
                }
                System.out.print("Tischkarten: ");
                if(tisch.tischKarten.size() > 0){
                    for(int i = 0; i < tisch.tischKarten.size(); i++){
                        System.out.print("" + tisch.tischKarten.get(i).getColor() + tisch.tischKarten.get(i).getValue());
                    }
                }else{
                    System.out.println("...");
                }
                boolean ende;
                int wahl;
                do {
                    System.out.println("Wählen Sie zwischen folgenden Aktionen:");
                    if (tisch.raiseWert > 0) {
                        System.out.println("1: Call");
                    } else {
                        System.out.println("1: Check");
                    }
                    System.out.println("2: Fold");
                    if (tisch.raiseWert > 0) {
                        System.out.println("3: Raise");
                    } else {
                        System.out.println("3: Bet");
                    }
                    wahl = s.nextInt();
                    ende = true;
                    switch (wahl) {
                        //Der Spieler möchte Callen/ Checken
                        case 1:
                            if (tisch.raiseWert > 0) {
                                tisch.call();
                            } else {
                                tisch.getCurrentSpieler().control = true;
                            }
                            break;
                        //Der Spieler möchte aussteigen
                        case 2:
                            tisch.getCurrentSpieler().istDabei = false;
                            break;
                        //Der Spieler möchte erhöhen
                        case 3:
                            //um zu wissen, wann die Runde zu ende ist: control der Spieler setzen
                            tisch.setControl();
                            tisch.getCurrentSpieler().control = true;

                            System.out.println("Was möchtest du setzen?");
                            wahl = t.nextInt();
                            while (!tisch.legalRaise(wahl)) {
                                    System.out.println("Der Wert ist leider nicht zulässig. Wählen Sie ein Vielfaches von " + tisch.smallBlindList[tisch.smallBlindListIndex] * 2 + "!");
                                    System.out.println("Was möchtest du setzen?");
                                    wahl = t.nextInt();
                            }
                            tisch.gibRaiseOrCall(wahl);
                            tisch.raiseWert = wahl;
                            break;
                        default:
                            System.out.println("Die Wahl ist leider nicht zulässig.");
                            ende = false;
                            break;
                    }
                }
                while(!ende);
            }
        }
    }
}