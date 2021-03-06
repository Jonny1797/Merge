package Pokern;
import java.util.ArrayList;
import java.util.Scanner;

public class Spieler {
	String name;
	private long geld;
    private long pod;
	private int currentPod;
    private boolean isAllIn = false;
	private boolean istDabei = true;
	private boolean control = false;
	private boolean istDran = false;
	private ArrayList<Karte> handKarten = new ArrayList<>();

	public Spieler(String Name/*, long Geld*/){
		this.name = Name;
		//this.geld = Geld;
	}

	//KARTEN############################################################################################################
	public void bekommeKarte(Karte karte){
		handKarten.add(karte);
	}
	//karten------------------------------------------------------------------------------------------------------------
	public ArrayList<Karte> getHandKarten(){
		return handKarten;
	}
	//ENDE_KARTEN#######################################################################################################

	//SPIELER###########################################################################################################
	public String getSpielerName(){
		return name;
	}
	//spieler-----------------------------------------------------------------------------------------------------------
	public int spielerWahlRundeEins(){
		while(true) {
			System.out.println("Wählen Sie zwischen folgenden Aktionen:");
			System.out.println("1: Call");
			System.out.println("2: Fold");
			System.out.println("3: Raise");
			Scanner s = new Scanner(System.in);
			int i = s.nextInt();
			switch (i) {
				case 1:
					return 0;
				case 2:
					return -1;
				case 3:
					return 1;
				default:
					System.out.println("Ungültige Eingabe.");
					break;
			}
		}
	}
	//spieler-----------------------------------------------------------------------------------------------------------
	public int spielerWahlRundeZDV(){
		while(true) {
			System.out.println("Wählen Sie zwischen folgenden Aktionen:");
			System.out.println("1: Check");
			System.out.println("2: Bet/ Raise");
			System.out.println("3: Fold");
			Scanner s = new Scanner(System.in);
			int i = s.nextInt();
			switch (i) {
				case 1:
					return 0;
				case 2:
					return 1;
				case 3:
					return -1;
				default:
					System.out.println("Ungültige Eingabe.");
					break;
			}
		}
	}
	//ENDE_SPIELER######################################################################################################


	public boolean getControl(){
		return this.control;
	}

	public void setControl(boolean control){
		this.control = control;
	}

	public boolean getIstDran(){
		return this.istDran;
	}

	public void setIstDran(boolean istDran){
		this.istDran = istDran;
	}

	public boolean getIstDabei(){
		return istDabei;
	}

	public void setIstDabei(boolean istDabei){
		this.istDabei = istDabei;
	}

	public int getCurrentPod(){
		return currentPod;
	}
	//GELD##############################################################################################################
	public void setGeld(long geld){
		this.geld = geld;
	}
	//geld--------------------------------------------------------------------------------------------------------------
	public void bekommeGeld(long Gewinn){
		geld = geld + Gewinn;
	}
	//geld--------------------------------------------------------------------------------------------------------------
	public void verliereGeld(long Verlust){
		geld = geld - Verlust;
	}
	//geld--------------------------------------------------------------------------------------------------------------
	public long wieVielGeld(){
		return geld;
	}
	//geld--------------------------------------------------------------------------------------------------------------
	public boolean kannSetzen(long geld){
		return this.geld > geld;
	}
	//ENDE_GELD#########################################################################################################

	//ALLIN#############################################################################################################
	public void setAllInZuIstAllIn(){
		isAllIn = true;
	}
	//allin-------------------------------------------------------------------------------------------------------------
	public  void setAllInZuIstNichtAllIn(){
		isAllIn = false;
	}
	//allin-------------------------------------------------------------------------------------------------------------
	public boolean getIsAllIn(){
		return isAllIn;
	}
	//ENDE_ALLIN########################################################################################################

    //IN_DER_RUNDE######################################################################################################
    public void setIstDabeiZuFalse(){
        istDabei = false;
    }
    //isinderrunde------------------------------------------------------------------------------------------------------
    public void setIstDabeiZuTrue(){
        istDabei = true;
    }
    //isinderrunde------------------------------------------------------------------------------------------------------
    public boolean getIsInDerRunde(){
        return istDabei;
    }
    //ENDE_IN_DER_RUNDE#################################################################################################

    //Pod###############################################################################################################
    public void addZumPod(long geld){
        pod = pod + geld;
    }
    //pod---------------------------------------------------------------------------------------------------------------
    public void resetPod(){
        pod = 0;
    }
    //pod---------------------------------------------------------------------------------------------------------------
    public long getPod(){
        return pod;
    }
    //pod---------------------------------------------------------------------------------------------------------------
    public void verminderePod(long geld){
        pod = pod - geld;
    }
    //End_Pod###########################################################################################################
}
