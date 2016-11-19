package Pokern;

import handChecker.HandChecker;
import handChecker.HandValue;
import handChecker.PokerCard;
import sun.security.provider.ConfigFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Tisch implements Runnable{
    private ArrayList<Spieler> mitSpieler = new ArrayList<>();
    ArrayList<Karte> tischKarten = new ArrayList<>();

    private long startGeld = 5000;

    public int[] smallBlindList = {100, 200, 400, 500, 1000, 2000, 4000, 5000};
    public int smallBlindListIndex = 0;
    private int currentSmallBlindValue;

    private int dealerSpielerIndex;
    private int smallBlindSpielerIndex;
    private int bigBlindSpielerIndex;
    private int currentSpielerIndex;

    public long raiseWert = 0;

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
            //Wer ist Small und Big Blind und wer ist an der Reihe?
            setSmallBlindSpielerIndex();
            setBigBlindSpielerIndex();
            nextSpieler(bigBlindSpielerIndex);

            //SmallBlindValue wird gelegt
            gibSmallBlind();

            //BigBlindValue wird gelegt
            gibBigBlind();

            //Die Spieler bekommen Karten
            gibSpielerKarten(deck);

            //Wahlmöglichkeiten erste Runde
            while(!getCurrentSpieler().control || currentSpielerIndex != bigBlindSpielerIndex +1){
                wahl();
                nextSpieler();
            }

            for (int i=0; i<3; i++){
                gibTischKarte(deck);
                System.out.println("Karte " + i + ":\t " + tischKarten.get(i).getColor() + ", " + tischKarten.get(i).getValue());
            }
            while (true){
                rundeZDV();
                nextSpieler();
            }
        }
        System.out.println("Spieler " + mitSpieler.get(0).getSpielerName() + " gewinnt");
        System.out.println("Das Spiel an diesem Tisch ist zu Ende.");
    }


    //DEALER############################################################################################################
    private void setDealer(){
        dealerSpielerIndex = (int) (Math.random() * (mitSpieler.size()-1));
        System.out.println("Der Dealer ist nun " + getDealer().name);
    }
    //dealer------------------------------------------------------------------------------------------------------------
    private void setNextDealer(){
       dealerSpielerIndex++;
        if(dealerSpielerIndex >= mitSpieler.size()){
            dealerSpielerIndex = 0;
        }
        System.out.println("Der Dealer ist nun " + getDealer().name);
        setSmallBlindSpielerIndex();
        setBigBlindSpielerIndex();
    }
    //dealer------------------------------------------------------------------------------------------------------------
    private Spieler getDealer(){
        return mitSpieler.get(dealerSpielerIndex);
    }
    //ENDE_DEALER#######################################################################################################



    //Spieler###########################################################################################################
    private void nextSpieler(){
        mitSpieler.get(currentSpielerIndex).istDran = false;
        currentSpielerIndex++;
        if(currentSpielerIndex >= mitSpieler.size()){
            currentSpielerIndex = 0;
        }
        System.out.println("Spieler " + mitSpieler.get(currentSpielerIndex).name + "ist nun an der Reihe.");
        mitSpieler.get(currentSpielerIndex).istDran = true;
    }
    //spieler-----------------------------------------------------------------------------------------------------------
    private void nextSpieler(int spieler){
        mitSpieler.get(spieler).istDran = false;
        currentSpielerIndex = spieler + 1;
        if(currentSpielerIndex >= mitSpieler.size()){
            currentSpielerIndex = 0;
        }
        System.out.println("Spieler " + mitSpieler.get(currentSpielerIndex).name + "ist nun an der Reihe.");
        mitSpieler.get(currentSpielerIndex).istDran = true;
    }
    //spieler-----------------------------------------------------------------------------------------------------------
    private void fuegeSpielerHinzu(Spieler s){
        mitSpieler.add(s);
        System.out.println("Spieler " + s.name + " sitzt nun mit am Tisch.");
    }
    //spieler-----------------------------------------------------------------------------------------------------------
    private void entferneSpieler(Spieler s){
        mitSpieler.remove(s);
    }
    //spieler-----------------------------------------------------------------------------------------------------------
    private int wieVielerSpieler(){
        return mitSpieler.size();
    }
    //spieler-----------------------------------------------------------------------------------------------------------
    private void setSmallBlindSpielerIndex(){
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
    private void setBigBlindSpielerIndex(){
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
    private void wahl(){
        /*boolean ende = false;
        System.out.println("Wählen Sie zwischen folgenden Aktionen:");
        if(raised) {
            System.out.println("1: Call");
        }
        else{
            System.out.println("1: Check");
        }
        System.out.println("2: Fold");
        if(raised){
            System.out.println("3: Raise");
        }
        else {
            System.out.println("3: Bet");
        }
        while(!ende) {
            switch (getCurrentSpieler().spielerWahlRundeEins()) { //thread?
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
                    raised = true;
                    ende = true;
                    break;

            }
        }
        for (Spieler i: mitSpieler){
            i.control =false;
        }*/
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
                    //gibRaiseOrCall(raiseWieViel());
                    ende = true;
                    break;
            }
        }
        for (Spieler i: mitSpieler){
            i.control =false;
        }
    }
    //spieler-----------------------------------------------------------------------------------------------------------
    public void call() {
        //Schau selbst, wie viel gesetzt werden muss
        gibRaiseOrCall(raiseWert - getCurrentSpieler().currentpod);
    }
    //spieler-----------------------------------------------------------------------------------------------------------
    public void setControl(){
        for (Spieler i : mitSpieler){
            i.control =false;
        }
    }
    //END_SPIELER#######################################################################################################

    //GELD##############################################################################################################
    private void gibSpielerStartGeld(long geld){
        for (Spieler i : mitSpieler){
            i.setGeld(geld);
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    private long getStartGeld(){
        return startGeld;
    }
    //------------------------------------------------------------------------------------------------------------------
    private void inDenPodEinzahlen(Spieler s, long geld){
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
    private void podAuszahlen(Spieler gewinner){

        ArrayList<Spieler> mitSpielerHandAmPod = new ArrayList<>();

        for(Spieler spieler:mitSpieler){
            if(spieler.istDabei){
                mitSpielerHandAmPod.add(spieler);
            }
        }

        List<PokerCard> Karten1 = null;
        List<PokerCard> Karten2 = null;
        Karten1.add(mitSpielerHandAmPod.get(0).handKarten.get(0));
        Karten1.add(mitSpielerHandAmPod.get(0).handKarten.get(1));
        Karten2.add(mitSpielerHandAmPod.get(1).handKarten.get(0));
        Karten2.add(mitSpielerHandAmPod.get(1).handKarten.get(1));
        for(int i = 0; i < tischKarten.size(); i++){
            Karten1.add(tischKarten.get(i));
            Karten2.add(tischKarten.get(i));
        }

        int gewinnerIndex; //Aus mitSpielerHandAmPod

        HandChecker hc = new HandChecker();

        for(int spielerIndex = 0; spielerIndex < mitSpielerHandAmPod.size(); spielerIndex++){
            if (hc.check(Karten1).compareTo(hc.check(Karten2)) > 0){

            }
        }

        //dsadfsasfasfasfasffas





       // HandValue handValue1 = hc.check(testKarten1);
       // HandValue handValue2 = hc.check(testKarten2);
      //  double i = handValue1.compareTo(handValue2);
        //So In Etwa



        /*long hauptPodValue = gewinner.getPod();
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
        }*/

    }
    //------------------------------------------------------------------------------------------------------------------
    private void gibSmallBlind(){

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
    private void gibBigBlind(){
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
    /*private long raiseWieViel(){
        long wert;
        System.out.println("Um wie viel möchtest du raisen?");
        Scanner s = new Scanner(System.in);
        while(!legalRaise(wert=s.nextLong())&&getCurrentSpieler().kannSetzen(wert)){
            System.out.println("Sie haben nicht genügend Geld, oder der Wert ist leider nicht zulässig. Wählen Sie ein Vielfaches von " + smallBlindList[smallBlindListIndex]*2);
        }
        return wert;
    }*/
    //------------------------------------------------------------------------------------------------------------------
    public void gibRaiseOrCall(long i){
        //this.pod += i; //?????????????????????????????????????????????????????????????????????????????????????????????
    }
    //ENDE_GELD#########################################################################################################

    //BLINDS############################################################################################################
    private int getSmallBlindValue(){
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
    private void gibSpielerKarten(KartenDeck deck){
        for(Spieler i:mitSpieler) {
            for(int j=0; j < 2; j++)
                i.bekommeKarte(deck.getKarte());
        }
    }
    //------------------------------------------------------------------------------------------------------------------
    private void gibTischKarte(KartenDeck deck){
        tischKarten.add(deck.getKarte());
    }
    //ENDE_KARTEN#######################################################################################################

    //PRÜFE#############################################################################################################
    public boolean legalRaise(long raise){
        return raise > smallBlindList[smallBlindListIndex]*2 || raiseWert > 0;
    }
    //PRÜFE_ENDE########################################################################################################
}
