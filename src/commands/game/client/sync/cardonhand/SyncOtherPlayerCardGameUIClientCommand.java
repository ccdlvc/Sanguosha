package commands.game.client.sync.cardonhand;

import java.util.Collections;

import commands.game.client.AbstractGameUIClientCommand;
import core.client.GamePanel;
import core.heroes.Hero;
import core.player.PlayerSimple;
import exceptions.server.game.InvalidPlayerCommandException;

public class SyncOtherPlayerCardGameUIClientCommand extends AbstractGameUIClientCommand {

	private static final long serialVersionUID = 7418973417086495080L;
	
	private final String name;
	private final boolean add;
	private final int amount;
	
	public SyncOtherPlayerCardGameUIClientCommand(String name, boolean add, int amount) {
		this.name = name;
		this.add = add;
		this.amount = amount;
	}

	@Override
	public void execute(GamePanel<? extends Hero> panel) {
		PlayerSimple player = panel.getContent().getPlayer(name);
		if (add) {
			player.addCards(Collections.nCopies(amount, null));
		} else {
			for (int i = 0; i < amount; i++) {
				try {
					player.removeCardFromHand(null);
				} catch (InvalidPlayerCommandException e) {
					e.printStackTrace();
				}
			}
		}
	}

}
