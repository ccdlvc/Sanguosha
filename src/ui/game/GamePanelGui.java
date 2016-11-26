package ui.game;

import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JLabel;
import javax.swing.JPanel;

import cards.Card;
import core.client.GamePanel;
import core.heroes.original.Blank;
import core.heroes.original.HeroOriginal;
import core.player.PlayerCompleteClient;
import core.player.PlayerInfo;
import core.player.PlayerSimple;
import listeners.game.GameListener;
import listeners.game.client.ClientPlayerStatusListener;
import ui.game.interfaces.CardRackUI;
import ui.game.interfaces.ClientGameUI;
import ui.game.interfaces.HeroUI;
import ui.game.interfaces.PlayerUI;

public class GamePanelGui extends JPanel implements GameListener, ClientGameUI<HeroOriginal> {
	private static final long serialVersionUID = 2519723480954332278L;

	public static final int WIDTH = 1600;
	public static final int HEIGHT = 900;

	private CardRackGui cardRack;
	private EquipmentRackGui equipmentRack;
	private HeroGui heroGui;
	private LifebarGui healthGui;
	private CardDisposalGui disposalGui;
	private CardSelectionPane pane;
	private JPanel customizedPanel;

	private PlayerCompleteClient myself;
	private List<PlayerGui> otherPlayers;
	private ButtonGui confirm;
	private ButtonGui cancel;
	private ButtonGui end;

	private JLabel deckSize;
	private MessageBoxGui messageBox;
	
	private final GamePanel<HeroOriginal> panel;
	
	private int cancelSetCount;

	public GamePanelGui(PlayerInfo player, GamePanel<HeroOriginal> panel) {
		this.panel = panel;
		setLayout(null);
		myself = new PlayerCompleteClient(player.getName(), player.getPosition());

		cardRack = new CardRackGui(panel);
		equipmentRack = new EquipmentRackGui(panel);
		heroGui = new HeroGui(panel);
		healthGui = new LifebarGui();
		disposalGui = new CardDisposalGui(this);
		otherPlayers = new ArrayList<PlayerGui>();
		deckSize = new JLabel();
		deckSize.setFont(new Font(Font.MONOSPACED, Font.BOLD, 15));
		deckSize.setSize(100, 100);
		deckSize.setLocation(WIDTH - 100, PlayerGui.HEIGHT);
		messageBox = new MessageBoxGui();
		messageBox.setLocation(equipmentRack.getWidth(), HEIGHT - cardRack.getHeight() - MessageBoxGui.HEIGHT);
		pane = null;

		myself.registerGameListener(this);
		myself.registerCardOnHandListener(cardRack);
		myself.registerEquipmentListener(equipmentRack);
		myself.registerHealthListener(healthGui);
		myself.registerCardDisposalListener(disposalGui);
		myself.registerPlayerStatusListener(new ClientPlayerStatusListener(this));
		myself.setHero(new Blank());// change in the future
		healthGui.onSetHealthLimit(myself.getHero().getHealthLimit()); // change in the future
		healthGui.onSetHealthCurrent(myself.getHero().getHealthLimit()); // change in the future
		heroGui.setHero(myself.getHero());
		confirm = new ButtonGui("Confirm", e -> panel.getCurrentOperation().onConfirmed());
		confirm.setLocation(0, HEIGHT - CardRackGui.HEIGHT - ButtonGui.HEIGHT);
		cancel = new ButtonGui("Cancel", e -> panel.getCurrentOperation().onCanceled());
		cancel.setLocation(ButtonGui.WIDTH, HEIGHT - CardRackGui.HEIGHT - ButtonGui.HEIGHT);
		end = new ButtonGui("End", e -> panel.getCurrentOperation().onEnded());
		end.setLocation(ButtonGui.WIDTH * 2, HEIGHT - CardRackGui.HEIGHT - ButtonGui.HEIGHT);
		
		cancelSetCount = 0;

		setPreferredSize(new Dimension(WIDTH, HEIGHT));
		add(cardRack);
		add(equipmentRack);
		add(heroGui);
		add(healthGui);
		add(disposalGui);
		add(confirm);
		add(cancel);
		add(end);
		add(deckSize);
		add(messageBox);
	}

	public synchronized void addPlayer(PlayerInfo info) {
		PlayerSimple player = new PlayerSimple(info.getName(), info.getPosition());
		player.registerCardDisposalListener(disposalGui);
		PlayerGui p = new PlayerGui(player, panel);
		player.setHero(new Blank()); // remove later
		otherPlayers.add(p);
		p.setLocation(WIDTH - (otherPlayers.size()) * PlayerGui.WIDTH, 0);
		add(p);
		repaint();
	}

	@Override
	public CardRackUI getCardRackUI() {
		return cardRack;
	}
	
	@Override
	public HeroUI<HeroOriginal> getHeroUI() {
		return heroGui;
	}
	
	@Override
	public List<? extends PlayerUI> getOtherPlayersUI() {
		return otherPlayers;
	}
	
	@Override
	public List<PlayerSimple> getOtherPlayers() {
		return otherPlayers.stream().map(ui -> ui.getPlayer()).collect(Collectors.toList());
	}
	
	@Override
	public PlayerCompleteClient getSelf() {
		return myself;
	}
	
	@Override
	public int getNumberOfPlayers() {
		return otherPlayers.size() + 1;
	}
	
	@Override
	public synchronized PlayerUI getOtherPlayerUI(PlayerInfo other) {
		for (PlayerGui ui : otherPlayers) {
			if (ui.getPlayer().getPlayerInfo().equals(other)) {
				return ui;
			}
		}
		throw new RuntimeException("No Other player ui found");
	}
	
	@Override
	public synchronized PlayerUI getOtherPlayerUI(String name) {
		for (PlayerGui ui : otherPlayers) {
			if (ui.getPlayer().getPlayerInfo().getName().equals(name)) {
				return ui;
			}
		}
		throw new RuntimeException("No Other player ui found");
	}
	
	@Override
	public PlayerSimple getPlayer(String name) {
		for (PlayerGui ui : otherPlayers) {
			if (ui.getPlayer().getName().equals(name)) {
				return ui.getPlayer();
			}
		}
		return null; // throw later
	}

	@Override
	public void onPlayerAdded(PlayerSimple player) {
		player.registerCardDisposalListener(disposalGui);
		PlayerGui p = new PlayerGui(player, panel);
		otherPlayers.add(p);
		p.setLocation(WIDTH - (otherPlayers.size()) * PlayerGui.WIDTH, 0);
		add(p);
		repaint();
	}

	@Override
	public void setCardSelected(Card card, boolean selected) {
		cardRack.setCardSelected(card, selected);
	}

	@Override
	public void setTargetSelected(PlayerInfo player, boolean selected) {
		for (PlayerGui p : otherPlayers)
			if (p.getPlayer().equals(player)) {
				if (selected) {
					p.setActivated(true);
					heroGui.setActivated(true);
				} else {
					p.setActivated(false);
					heroGui.setActivated(false);
				}
				return;
			}

	}

	@Override
	public void setCardSelectable(Card card, boolean selectable) {
		cardRack.setCardSelectable(card, selectable);
	}

	@Override
	public void setTargetSelectable(PlayerInfo player, boolean selectable) {
		if (myself.equals(player))
			heroGui.setEnabled(selectable);
		for (PlayerGui p : otherPlayers)
			if (p.getPlayer().equals(player)) {
				p.setEnabled(selectable);
				return;
			}
	}

	@Override
	public void setConfirmEnabled(boolean isEnabled) {
		confirm.setEnabled(isEnabled);
	}

	@Override
	public void setCancelEnabled(boolean isEnabled) {
		if (isEnabled) {
			cancelSetCount++;
			cancel.setEnabled(true);
		} else {
			cancelSetCount--;
			if (cancelSetCount == 0) {
				cancel.setEnabled(false);
			}
			if (cancelSetCount < 0) {
				throw new RuntimeException("Cancel disabled more than enabled");
			}
		}
	}
	
	@Override
	public void setEndEnabled(boolean isEnabled) {
		end.setEnabled(isEnabled);
	}

	@Override
	public void setDeckSize(int size) {
		deckSize.setText(Integer.toString(size));
	}

	@Override
	public void setMessage(String message) {
		messageBox.setMessage(message);
	}

	@Override
	public void clearMessage() {
		messageBox.clearMessage();
	}

	@Override
	public void onDisplayCardSelectionPane(PlayerSimple player, boolean showHand, boolean showEquipments,
			boolean showDecisions) {
		// pane = new CardSelectionPane(player, showHand, showEquipments, showDecisions, this);
		pane.setLocation((WIDTH - pane.getWidth()) / 2, (HEIGHT - CardRackGui.HEIGHT - pane.getHeight()) / 2);
		add(pane);
		setComponentZOrder(pane, 0); // foremost
		pane.validate();
		pane.repaint();
	}

	@Override
	public void onDisplayCustomizedSelectionPaneAtCenter(JPanel panel) {
		this.onRemoveCustomizedSelectionPane();
		customizedPanel = panel;
		customizedPanel.setLocation((WIDTH - customizedPanel.getWidth()) / 2,
				(HEIGHT - CardRackGui.HEIGHT - customizedPanel.getHeight()) / 2);
		add(customizedPanel);
		setComponentZOrder(customizedPanel, 0); // foremost
		customizedPanel.validate();
		customizedPanel.repaint();
	}

	@Override
	public void onRemoveCustomizedSelectionPane() {
		if (customizedPanel != null) {
			remove(customizedPanel);
			customizedPanel = null;
			validate();
			repaint();
		}
	}
	
	@Override
	public void showCountdownBar() {
		// TODO implement action bar for self
	}
	
	@Override
	public JPanel getPanel() {
		return this;
	}
}