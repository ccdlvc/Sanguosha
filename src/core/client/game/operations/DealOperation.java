package core.client.game.operations;

import commands.game.server.ingame.EndStageInGameServerCommand;
import core.client.GamePanel;
import core.heroes.Hero;
import ui.game.interfaces.Activatable;
import ui.game.interfaces.CardUI;
import ui.game.interfaces.ClientGameUI;
import ui.game.interfaces.EquipmentUI;

public class DealOperation implements Operation {

	private GamePanel<? extends Hero> panel;
	
	@Override
	public void onEnded() {
		onConfirmed();
		// resetting weapon/skills, etc.
		panel.getChannel().send(new EndStageInGameServerCommand());
	}
	
	/**
	 * <p>{@inheritDoc}</p>
	 * 
	 * <p>
	 * Deal itself will not enable confirm button,
	 * but other operations may, so this is a way to
	 * clean up the UI changes made by Deal operation
	 * </p>
	 */
	@Override
	public void onConfirmed() {
		ClientGameUI<? extends Hero> panelUI = panel.getContent();
		panelUI.setEndEnabled(false);
		for(CardUI cardUI : panelUI.getCardRackUI().getCardUIs()) {
			cardUI.setActivatable(false);
		}
	}
	
	@Override
	public void onCardClicked(CardUI card) {
		panel.pushOperation(card.getCard().generateOperation(), card);
	}
	
	@Override
	public void onEquipmentClicked(EquipmentUI equipment) {
		panel.pushOperation(equipment.getEquipment().generateOperation(), equipment);
	}

	@Override
	public void onActivated(GamePanel<? extends Hero> panel, Activatable source) {
		this.panel = panel;
		ClientGameUI<? extends Hero> panelUI = panel.getContent();
		panelUI.showCountdownBar();
		for(CardUI cardUI : panelUI.getCardRackUI().getCardUIs()) {
			if (cardUI.getCard().isActivatable(panelUI)) {
				cardUI.setActivatable(true);
			}
		}
		panelUI.setEndEnabled(true);
		// if (player.getWeapon()...) check weapon use
		// skills
	}

}
