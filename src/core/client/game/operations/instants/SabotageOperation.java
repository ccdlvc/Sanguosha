package core.client.game.operations.instants;

import commands.game.server.GameServerCommand;
import commands.game.server.ingame.InitiateSabotageInGameServerCommand;
import core.client.game.operations.AbstractSingleTargetCardOperation;
import core.heroes.Hero;
import ui.game.interfaces.CardUI;
import ui.game.interfaces.ClientGameUI;
import ui.game.interfaces.PlayerUI;

public class SabotageOperation extends AbstractSingleTargetCardOperation {

	@Override
	protected GameServerCommand getCommand() {
		return new InitiateSabotageInGameServerCommand(this.targetUI.getPlayer().getPlayerInfo(), ((CardUI) this.activator).getCard());
	}

	@Override
	protected void setupTargetSelection() {
		ClientGameUI<? extends Hero> panelUI = this.panel.getContent();
		for (PlayerUI other : panelUI.getOtherPlayersUI()) {
			// TODO: check Delayed zone
			if (other.getPlayer().getHandCount() > 0 || other.getPlayer().isEquipped()) {
				other.setActivatable(true);
			}
		}
		panelUI.setCancelEnabled(true);
	}

}