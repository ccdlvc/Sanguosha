package core.client.game.operations;

import commands.game.server.EndStageInGameServerCommand;
import net.client.GamePanel;
import player.PlayerComplete;
import ui.game.Activatable;
import ui.game.CardGui;
import ui.game.EquipmentGui;
import ui.game.GamePanelUI;

public class DealOperation implements Operation {

	private GamePanel panel;
	
	@Override
	public void onEnded() {
		panel.getChannel().send(new EndStageInGameServerCommand());
	}
	
	@Override
	public void onCardClicked(CardGui card) {
		panel.pushOperation(card.getCard().generateOperation(), card);
	}
	
	@Override
	public void onEquipmentClicked(EquipmentGui equipment) {
		panel.pushOperation(equipment.getEquipment().generateOperation(), equipment);
	}

	@Override
	public void onActivated(GamePanel panel, Activatable source) {
		this.panel = panel;
		GamePanelUI panelUI = panel.getContent();
		panelUI.showCountdownBar();
		PlayerComplete player = panelUI.getSelf();
		System.out.println("Deal activated");
		for(CardGui cardUI : panelUI.getCardRackUI().getCardUIs()) {
			if (cardUI.getCard().isActivatableBy(player)) {
				System.out.println(cardUI.getCard().getName() + " activated");
				cardUI.setActivatable(true);
			}
		}
		// if (player.getWeapon()...) check weapon use
		// skills
	}

}