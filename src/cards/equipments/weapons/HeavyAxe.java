package cards.equipments.weapons;

public class HeavyAxe extends Weapon {

	private static final long serialVersionUID = -5886230292135571127L;

	public HeavyAxe(int num, Suit suit, int id) {
		super(3, num, suit, id);
	}

	@Override
	public String getName() {
		return "Heave Axe";
	}

}
