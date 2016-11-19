package Pokern;
import handChecker.PokerCard;
import java.util.ArrayList;

public class KartenDeck {
    static ArrayList<Karte> Deck = new ArrayList<>();

    public void erstelleEinDeck() {
        for (PokerCard.Value value : PokerCard.Value.values()) {
            for (PokerCard.Color color : PokerCard.Color.values()) {
                Deck.add(new Karte(color, value));
            }
        }
    }

    public void mischeDasDeck() {
        for(int i = 1; i < (((int) Math.random() * 53) + 270); ++i) {
            int zufallsZahl = (int) (Math.random() * Deck.size());
            Karte tempKarte = Deck.get(zufallsZahl);
            Deck.remove(zufallsZahl);
            Deck.add(tempKarte);
        }
    }

    public Karte getKarte(){
        Karte tempKarte = Deck.get(0);
        Deck.remove(0);
        return tempKarte;
    }

}