package commands.game.server.ingame;

import cards.Card;
import core.server.game.Game;
import core.server.game.controllers.interfaces.DodgeUsableGameController;

public class DodgeReactionInGameServerCommand extends InGameServerCommand {

	private static final long serialVersionUID = 8547546299242633692L;

	private final Card dodge;
	
	public DodgeReactionInGameServerCommand(Card card) {
		this.dodge = card;
	}
	
	@Override
	public void execute(Game game) {
		if (dodge != null) {
			game.<DodgeUsableGameController>getGameController().onDodgeUsed(dodge);
		} else {
			game.<DodgeUsableGameController>getGameController().onDodgeNotUsed();
		}
	}

}
