package Pokern;
import handChecker.PokerCard;

public class Karte implements PokerCard {
	private PokerCard.Color color;
	private PokerCard.Value value;

	public Karte(PokerCard.Color color, PokerCard.Value value){
		this.color = color;
		this.value = value;
	}

	@Override
	public Color getColor() {
		return color;
	}


	@Override
	public Value getValue() {
		return  value;
	}

}