package Pokern;

import sun.security.provider.ConfigFile;

import java.util.ArrayList;
import java.util.Scanner;

public class Tisch implements Runnable{
    ArrayList<Spieler> mitSpieler = new ArrayList<>();
    ArrayList<Karte> tischKarten = new ArrayList<>();

    long startGeld = 5000;

    int[] smallBlindList = {100, 200, 400, 500, 1000, 2000, 4000, 5000};
    int smallBlindListIndex = 0;
    int currentSmallBlindValue;

    int dealerSpielerIndex;
    int smallBlindSpielerIndex;
    int bigBlindSpielerIndex;
    int currentSpielerIndex;


    @Override public void run(){
        //Erstelle Spieler
        Spieler Eberhart = new Spieler("Eberhard");
        Spieler Guenther = new Spieler("Guenther");
        Spieler Max = new Spieler("Max");

        //Erstelle  ein Kartendeck
        KartenDeck deck = new KartenDeck();
        System.out.println("Das Deck wurde hinzugefügt");

        //Füge die Spieler zum Tisch hinzu
        fuegeSpielerHinzu(Eberhart);
        fuegeSpielerHinzu(Guenther);
        fuegeSpielerHinzu(Max);

        //Gebe den Mitspielern das Startgeld
        gibSpielerStartGeld(getStartGeld());

        //Mach dir ein Kartendeck und mische es
        deck.erstelleEinDeck();
        deck.mischeDasDeck();

        //Setze den SmallBlind
        currentSmallBlindValue = getSmallBlindValue();

        //Setzte den Dealer
        setDealer();

        while(mitSpieler.size() > 1){
            //Wer ist Small und BigBlind und wer ist an der Reihe?
            setSmallBlindSpielerIndex();
            setBigBlindSpielerIndex();
            nextSpieler();

            //SmallBlindValue wird gelegt
            gibSmallBlind();

            //BigBlindValue wird gelegt
            gibBigBlind();

            //Die Spieler bekommen Karten
            gibSpielerKarten();

            //Wahlmöglichkeiten erste Runde
            while(!getCurrentSpieler().control || currentSpielerIndex != bigBlindSpielerIndex +1){
                rundeEins();
                nextSpieler();
            }
            for (int i=0; i<3; i++){
                gibTischKarte();
                System.out.println("Karte " + i + ":\t " + tischKarten.get(i).getColor() + ", " + tischKarten.get(i).getValue());
            }
            while (true){
                rundeZDV();
                nextSpieler();
            }

        }
        System.out.println("Spieler " + mitSpieler.get(0).getSpielerName() + " gewinnt");
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
        System.out.println("Ende vom Tisch");
    }


    //DEALER############################################################################################################
    public void setDealer(){
        dealerSpielerIndex = (int) (Math.random() * (mitSpieler.size()-1));
        System.out.println("Der Dealer ist nun " + getDealer().name);
    }
    //dealer------------------------------------------------------------------------------------------------------------
    public void setNextDealer(){
       dealerSpielerIndex++;
        if(dealerSpielerIndex >= mitSpieler.size()){
            dealerSpielerIndex = 0;
        }
        System.out.println("Der Dealer ist nun " + getDealer().name);
        setSmallBlindSpielerIndex();
        setBigBlindSpielerIndex();
    }
    //dealer------------------------------------------------------------------------------------------------------------
    public Spieler getDealer(){
        return mitSpieler.get(dealerSpielerIndex);
    }
    //ENDE_DEALER#######################################################################################################



    //Spieler###########################################################################################################
    //unnötig. Es gibt schon die Methode getCurrentSpieler()
    public Spieler currentSpieler(){
        return mitSpieler.get(currentSpielerIndex);
    }
    //spieler-----------------------------------------------------------------------------------------------------------
    public Spieler nextSpieler(){
        currentSpielerIndex++;
        if(currentSpielerIndex >= mitSpieler.size()){
            currentSpielerIndex = 0;
        }
        System.out.println("Spieler " + mitSpieler.get(currentSpielerIndex).name + "ist nun an der Reihe.");
        return mitSpieler.get(currentSpielerIndex);
    }
    //spieler-----------------------------------------------------------------------------------------------------------
    public void fuegeSpielerHinzu(Spieler s){
        mitSpieler.add(s);
        System.out.println("Spieler " + s.name + " sitzt nun mit am Tisch.");
    }
    //spieler-----------------------------------------------------------------------------------------------------------
    public void entferneSpieler(Spieler s){
        mitSpieler.remove(s);
    }
    //spieler-----------------------------------------------------------------------------------------------------------
    public int wieVielerSpieler(){
        return mitSpieler.size();
    }
    //spieler-----------------------------------------------------------------------------------------------------------
    public void setSmallBlindSpielerIndex(){
        if(mitSpieler.size() > 2){
            smallBlindSpielerIndex = dealerSpielerIndex + 1;
            if(smallBlindSpielerIndex >= mitSpieler.size()){
                smallBlindSpielerIndex = smallBlindSpielerIndex - mitSpieler.size();
            }
        }else{
            smallBlindSpielerIndex = dealerSpielerIndex;
        }
        System.out.println("Der Small Blind ist nun " + mitSpieler.get(smallBlindSpielerIndex).name);
    }
    //spieler-----------------------------------------------------------------------------------------------------------
    public void setBigBlindSpielerIndex(){
        if(mitSpieler.size() > 2){
            bigBlindSpielerIndex = dealerSpielerIndex + 2;
            if(bigBlindSpielerIndex >= mitSpieler.size()){
                bigBlindSpielerIndex = bigBlindSpielerIndex - mitSpieler.size();
            }
        }else{
            if(dealerSpielerIndex == 0){
                bigBlindSpielerIndex = 1;
            }else{
                bigBlindSpielerIndex = 0;
            }
        }
        System.out.println("Der Big Blind ist nun " + mitSpieler.get(bigBlindSpielerIndex).name);
    }
    //spieler-----------------------------------------------------------------------------------------------------------
    public Spieler getCurrentSpieler(){
        return mitSpieler.get(currentSpielerIndex);
    }
    //spieler-----------------------------------------------------------------------------------------------------------
    public void rundeEins(){
        boolean ende = false;
        while(!ende) {
            switch (getCurrentSpieler().spielerWahlRundeEins()) {
                case -1:
                    getCurrentSpieler().istDabei = false;
                    ende = true;
                    break;
                case 0:
                    if(mitSpieler.get(currentSpielerIndex-1).control){
                        System.out.println("Das geht nicht.");
                    }
                    else {
                        call();
                        ende = true;
                        getCurrentSpieler().control=true;
                    }
                    break;
                case 1:
                    for (Spieler i: mitSpieler){
                        i.control =false;
                    }
                    getCurrentSpieler().control=true;
                    gibRaiseOrCall(raiseWieViel());
                    ende = true;
                    break;

            }
        }
        for (Spieler i: mitSpieler){
            i.control =false;
        }
    }
    //spieler-----------------------------------------------------------------------------------------------------------
    private void rundeZDV() {
        boolean ende = false;
        while(!ende) {
            switch (getCurrentSpieler().spielerWahlRundeZDV()) {
                case -1:
                    getCurrentSpieler().istDabei = false;
                    ende = true;
                    break;
                case 0:
                    if(mitSpieler.get(currentSpielerIndex-1).control){
                        System.out.println("Das geht nicht.");
                    }
                    else {
                        call();
                        ende = true;
                        getCurrentSpieler().control=true;
                    }
                    break;
                case 1:
                    for (Spieler i: mitSpieler){
                        i.control =false;
                    }
                    getCurrentSpieler().control=true;
                    gibRaiseOrCall(raiseWieViel());
                    ende = true;
                    break;
            }
        }
        for (Spieler i: mitSpieler){
            i.control =false;
        }
    }
    //spieler-----------------------------------------------------------------------------------------------------------
    private void call() {
        //Schau selbst, wie viel gesetzt werden muss
        gibRaiseOrCall(1);
    }
    //END_SPIELER#######################################################################################################

    //GELD##############################################################################################################
    public void gibSpielerStartGeld(long geld){
        for (Spieler i : mitSpieler){
            i.setGeld(geld);
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    public long getStartGeld(){
        return startGeld;
    }
    //------------------------------------------------------------------------------------------------------------------
    public void inDenPodEinzahlen(Spieler s, long geld){
        if(s.wieVielGeld() < geld){
            s.addZumPod(s.wieVielGeld());
            s.verliereGeld(s.wieVielGeld());
            System.out.println(s.getSpielerName() + " geht All-In mit. " + s.wieVielGeld());
        }else{
            s.addZumPod(geld);
            s.verliereGeld(geld);
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    public void podAuszahlen(Spieler gewinner){
        long hauptPodValue = gewinner.getPod();
        for(Spieler s:mitSpieler){
            if(hauptPodValue > s.getPod()){
                gewinner.bekommeGeld(s.getPod());
                s.resetPod();
            }else{
                gewinner.bekommeGeld(hauptPodValue);
                s.verminderePod(hauptPodValue);
            }
        }
        boolean nochNichtFertig = true;
        long minPod = -1;

        while(nochNichtFertig){
            nochNichtFertig = false;
            for(Spieler s:mitSpieler){
                if(s.getPod() != 0) {
                    nochNichtFertig = true;
                    if(minPod == -1 && s.getPod() != 0){
                        minPod = s.getPod();
                    }else if(minPod > s.getPod() && s.getPod() != 0){
                        minPod = s.getPod();
                    }
                }
            }
            for(Spieler s:mitSpieler){
                if(minPod <= s.getPod() && s.getPod() != 0){
                    s.bekommeGeld(minPod);
                    s.verminderePod(minPod);
                }
            }
        }

    }
    //------------------------------------------------------------------------------------------------------------------
    public void gibSmallBlind(){



        //if(mitSpieler.get(smallBlindSpielerIndex).wieVielGeld() < getSmallBlindValue()){
        //    long allInValue = mitSpieler.get(smallBlindSpielerIndex).wieVielGeld();
        //    mitSpieler.get(smallBlindSpielerIndex).setAllInZuIstAllIn();                                                   //Spieler wird All-in gesetzt
        //    mitSpieler.get(smallBlindSpielerIndex).verliereGeld(allInValue);                                               //All-in Betrag wird dem Spieler abgezogen
        //    pod = pod + allInValue;                                                                                 //All-in Betrag wird dem Pod hinzugefügt
        //    System.out.println(mitSpieler.get(smallBlindSpielerIndex).getSpielerName() + " ist All-in mit: " + allInValue);
        //}else{
        //    mitSpieler.get(smallBlindSpielerIndex).verliereGeld(getSmallBlindValue());
        //    pod = pod + getSmallBlindValue();
        //}
    }
    //------------------------------------------------------------------------------------------------------------------
    public void gibBigBlind(){
        //if(mitSpieler.get(bigBlindSpielerIndex).wieVielGeld() < getSmallBlindValue()){
        //    long allInValue = 2 * mitSpieler.get(bigBlindSpielerIndex).wieVielGeld();
        //    mitSpieler.get(bigBlindSpielerIndex).setAllInZuIstAllIn();                                                     //Spieler wird All-in gesetzt
        //    mitSpieler.get(bigBlindSpielerIndex).verliereGeld(allInValue);                                                 //All-in Betrag wird dem Spieler abgezogen
        //    pod = pod + allInValue;                                                                                 //All-in Betrag wird dem Pod hinzugefügt
        //    System.out.println(mitSpieler.get(bigBlindSpielerIndex).getSpielerName() + " ist All-in mit: " + allInValue);
        //}else{
        //    mitSpieler.get(bigBlindSpielerIndex).verliereGeld(2 * getSmallBlindValue());
        //    pod = pod + (2 * getSmallBlindValue());
        //}
    }
    //------------------------------------------------------------------------------------------------------------------
    public long raiseWieViel(){
        long wert;
        System.out.println("Um wie viel möchtest du raisen?");
        Scanner s = new Scanner(System.in);
        while(!legalRaise(wert=s.nextLong())&&getCurrentSpieler().kannSetzen(wert)){
            System.out.println("Sie haben nicht genügend Geld, oder der Wert ist leider nicht zulässig. Wählen Sie ein Vielfaches von " + smallBlindList[smallBlindListIndex]*2);
        }
        return wert;
    }
    //------------------------------------------------------------------------------------------------------------------
    public void gibRaiseOrCall(long i){
        //this.pod += i; //?????????????????????????????????????????????????????????????????????????????????????????????
    }
    //ENDE_GELD#########################################################################################################

    //BLINDS############################################################################################################
    public int getSmallBlindValue(){
        return smallBlindList[smallBlindListIndex];
    }
    //blind-------------------------------------------------------------------------------------------------------------
    private long setNextSmallBlind(){
        if(smallBlindListIndex++ <= smallBlindList.length){
            return smallBlindListIndex++;
        }else{
            return smallBlindListIndex;
        }
    }
    //ENDE_BLINDS#######################################################################################################

    //KARTEN############################################################################################################
    public void gibSpielerKarten (){
        for(Spieler i:mitSpieler) {
            for(int j=0; j < 2; j++)
                i.bekommeKarte(KartenDeck.getKarte());
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    private void gibTischKarte(){
        tischKarten.add(KartenDeck.getKarte());
    }
    //ENDE_KARTEN#######################################################################################################

    //PRÜFE#############################################################################################################
    private boolean legalRaise(long raise){
        return raise > smallBlindList[smallBlindListIndex] && smallBlindList[smallBlindListIndex] % raise == 0;
    }
    //PRÜFE_ENDE########################################################################################################
}