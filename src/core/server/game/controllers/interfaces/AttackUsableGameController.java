package core.server.game.controllers.interfaces;

import cards.Card;
import core.server.game.controllers.GameController;

public interface AttackUsableGameController extends GameController {

	public void onAttackUsed(Card card);
	
	public void onAttackNotUsed();
}
