package commands.game.server.ingame;

import cards.Card;
import core.player.PlayerInfo;
import core.server.game.Game;
import core.server.game.controllers.GameController;
import core.server.game.controllers.specials.instants.BarbarianInvasionGameController;

public class InitiateBarbarianInvasionInGameServerCommand extends AbstractInitiationInGameServerCommand {

	private static final long serialVersionUID = 1L;
	
	public InitiateBarbarianInvasionInGameServerCommand(Card card) {
		super(null, card);
	}

	@Override
	protected GameController getController(Game game, PlayerInfo target) {
		return new BarbarianInvasionGameController(game.getCurrentPlayer().getPlayerInfo(), game);
	}

}
