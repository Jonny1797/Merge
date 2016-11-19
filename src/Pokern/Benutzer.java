package Pokern;

import static java.lang.Thread.sleep;

/**
 * Created by DemonenHerr on 18.11.2016.
 */
public class Benutzer implements Runnable {

    Spieler sp;
    Tisch tisch;

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
            if(sp.istDrann){
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


            }
        }
    }
}