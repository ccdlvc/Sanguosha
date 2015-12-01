package commands.game.client.sync.disposal;

import commands.game.client.GeneralGameUIClientCommand;
import core.client.GamePanel;
import core.heroes.Hero;

public class SyncDisposalAreaRefreshGameUIClientCommand extends GeneralGameUIClientCommand {
	
	private static final long serialVersionUID = 279006683296367016L;

	@Override
	protected void execute(GamePanel<? extends Hero> panel) {
		panel.getContent().getSelf().clearDisposalArea();
	}

}
