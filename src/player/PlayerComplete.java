package player;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import cards.Card;
import commands.Stage;
import core.client.game.operations.Operation;
import core.server.game.controllers.GameController;
import exceptions.server.game.InvalidPlayerCommandException;
import listeners.game.GameListener;
import listeners.game.PlayerStatusListener;

/**
 * client side complete implementation of player, used as player himself
 * 
 * @author Harry
 *
 */
public class PlayerComplete extends PlayerSimple {
	// ******** in-game properties ***********
	private List<Card> cardsOnHand;
	private int attackLimit;// limit of attacks can be used in one TURN_DEAL, by
							// default 1
	private int attackUsed;// number of attacks already used this TURN_DEAL
	private int wineLimit;// limit of wines can be used in on TURN_DEAL, by
							// default 1
	private int wineUsed;// number of wines already used this TURN_DEAL
	private boolean isWineUsed;// whether wine is currently used

	// private settings

	// in-game interactive properties
	private List<Card> cardsUsedThisTurn;

	private PlayerStatusListener statusListener;

	public PlayerComplete(String name, int position) {
		super(name, position);
		init();
	}

	private void init() {
		cardsOnHand = new ArrayList<Card>();

		cardsUsedThisTurn = new ArrayList<Card>();
		// init in-game interactive properties
		attackLimit = 1;
		attackUsed = 0;
		wineLimit = 1;
		wineUsed = 0;
		isWineUsed = false;
	}
	
	public void registerPlayerStatusListener(PlayerStatusListener listener) {
		this.statusListener = listener;
	}

	public List<Card> getCardsOnHand() {
		return cardsOnHand;
	}

	@Override
	public void addCard(Card card) {
		cardsOnHand.add(card);
		super.addCard(card);
	}

	@Override
	public void useCard(Card card) throws InvalidPlayerCommandException {
		if (!cardsOnHand.contains(card)) {
			throw new InvalidPlayerCommandException("useCard: Card " + card + " is not on player's hand");
		}
		cardsOnHand.remove(card);
		super.useCard(card);
	}
	
	@Override
	public void useCards(Collection<Card> cards) throws InvalidPlayerCommandException {
		if (!cardsOnHand.containsAll(cards)) {
			throw new InvalidPlayerCommandException("useCards: Some cards are not on player's hand");
		}
		super.useCards(cards);
	}

	@Override
	public void discardCard(Card card) throws InvalidPlayerCommandException {
		if (!cardsOnHand.contains(card)) {
			throw new InvalidPlayerCommandException("dicardCard: Card " + card + " is not on player's hand");
		}
		cardsOnHand.remove(card);
		super.discardCard(card);
	}

	@Override
	public void removeCardFromHand(Card card) throws InvalidPlayerCommandException {
		if (!cardsOnHand.contains(card)) {
			throw new InvalidPlayerCommandException("removeCardFromHand: Card " + card + " is not on player's hand");
		}
		super.removeCardFromHand(card);
		cardsOnHand.remove(card);
	}

	// //**************** methods related to game flow ***************
	// /**
	// * <li>{@link GameListener} notified
	// */
	// public void startDealing()
	// {
	// gameListener.setEndEnabled(true);
	// for(Card card : cardsOnHand)
	// {
	// if(card.isActivatableBy(this))
	// gameListener.setCardSelectable(card, true);
	// else
	// gameListener.setCardSelectable(card, false);
	// }
	// }
	// /**
	// * <li>No card selected
	// * <li>No target selected
	// * <li>No player enabled(targetSelection off)
	// * <li>No update to send
	// * <li>confirm disabled
	// */
	// public void endDealing()
	// {
	// if(operation != null)
	// {
	// operation.onCancelledBy(this);
	// }
	// disableAll();
	// currentStage.nextStage(this);
	// client.sendToServer(currentStage);
	// }
	//
	// /**
	// * End the player's turn:
	// * <li> attack used reset to 0
	// * <li> wine used reset to 0
	// * <li> operation reset to null
	// * <li> card used this turn reset to empty
	// * <li> continue to next stage
	// */
	// public void endTurn()
	// {
	// attackUsed = 0;
	// wineUsed = 0;
	// isWineUsed = false;
	// operation = null;
	// cardsUsedThisTurn.clear();
	// currentStage.nextStage(this);
	// client.sendToServer(currentStage);
	// }
	// /**
	// * <li>no card activated
	// * <li>no operation
	// * <li>no card on hand selectable
	// * <li>no target selectable
	// * <li>no button can be pressed
	// */
	// public void disableAll()
	// {
	// cardActivated = null;
	// setAllCardsOnHandSelectable(false);
	// setAllTargetsSelectableExcludingSelf(false);
	// gameListener.setConfirmEnabled(false);
	// gameListener.setCancelEnabled(false);
	// gameListener.setEndEnabled(false);
	// }
	//
	// //**************** methods related to interactions ****************
	// public void setOperation(Operation op)
	// {
	// operation = op;
	// }
	// public void setAllTargetsSelectableExcludingSelf(boolean selectable)
	// {
	// for(PlayerOriginal p : otherPlayers)
	// if(p.isAlive())
	// gameListener.setTargetSelectable(p.getPlayerInfo(), selectable);
	// }
	// public void setAllTargetsSelectableIncludingSelf(boolean selectable)
	// {
	// setAllTargetsSelectableExcludingSelf(selectable);
	// gameListener.setTargetSelectable(getPlayerInfo(), selectable);
	// }
	// public void setAllCardsOnHandSelectable(boolean selectable)
	// {
	// for(Card card : cardsOnHand)
	// gameListener.setCardSelectable(card,selectable);
	// }
	// /**
	// * select a card on hand, done by Gui
	// * <li>{@link GameListener} notified
	// * @param card
	// */
	// public void chooseCardOnHand(Card card)
	// {
	// if(cardActivated == null && operation == null)//no card activated
	// {
	// operation = card.onActivatedBy(this,currentStage);
	// cardActivated = card;
	// }
	// else if(cardActivated != null)
	// {
	// operation.onCancelledBy(this);
	// operation = null;
	// if(cardActivated.equals(card))//cancel
	// {
	// cardActivated = null;
	// }
	// else//change
	// {
	// cardActivated = card;
	// operation = cardActivated.onActivatedBy(this,currentStage);
	// }
	// }
	// else//something activated
	// {
	// operation.onCardSelected(this, card);
	// }
	// }
	// public void choosePlayer(PlayerSimple player)
	// {
	// operation.onPlayerSelected(this, player);
	// }
	//
	// public void setCardSelectableByName(String cardName,boolean selectable)
	// {
	// for(Card card : cardsOnHand)
	// if(card.getName().equals(cardName))
	// gameListener.setCardSelectable(card,selectable);
	// }
	// public void setCardSelectableByType(CardType cardType,boolean selectable)
	// {
	// for(Card card : cardsOnHand)
	// if(card.getType() == cardType)
	// gameListener.setCardSelectable(card,selectable);
	// }
	//
	// /**
	// * called by user clicking "confirm"
	// */
	// public void confirm()
	// {
	// gameListener.clearMessage();
	// disableAll();
	// Operation temp = operation;
	// operation = null;
	// temp.onConfirmedBy(this);
	// }
	// /**
	// * called by user clicking "cancel"
	// * <li>operation.onCancelledBy(this)
	// * <li>operation = null;
	// * <li>cardActivated = null
	// */
	// public void cancel()
	// {
	// gameListener.clearMessage();
	// cardActivated = null;
	// Operation temp = operation;
	// operation = null;
	// temp.onCancelledBy(this);
	// }
	//
	// public PlayerInfo getNextPlayerAlive()
	// {
	// Player next = null;
	// for(Player p : otherPlayers)
	// {
	// if(!p.isAlive())
	// continue;
	// if(next == null && p.getPosition() > getPosition())
	// {
	// next = p;
	// continue;
	// }
	// if(p.getPosition() > getPosition() && p.getPosition() <
	// next.getPosition())
	// next = p;
	// }
	// if(next == null)
	// {
	// for(Player p : otherPlayers)
	// {
	// if(!p.isAlive())
	// continue;
	// if(next == null && p.getPosition() < getPosition())
	// {
	// next = p;
	// continue;
	// }
	// if(p.getPosition() < next.getPosition())
	// next = p;
	// }
	// }
	// if(next == null)
	// System.err.println("Master: Next player not found");
	// return next.getPlayerInfo();
	// }
	//
	// /**
	// * Find the match of player, can be this player or other players
	// * @param p
	// * @return the match
	// */
	// public PlayerSimple findMatch(PlayerInfo p)
	// {
	// for(PlayerSimple player : otherPlayers)
	// if(player.equals(p))
	// return player;
	// if(this.equals(p))
	// return this;
	// System.err.println("Player not found");
	// return null;
	// }
	// ************** methods related to in-game properties ****************
	
	@Override
	public void flip() {
		super.flip();
		statusListener.onFlip(isFlipped());
	}

	public void setAttackLimit(int limit) throws InvalidPlayerCommandException {
		if (limit != getAttackLimit()) {
			attackLimit = limit;
			statusListener.onSetAttackLimit(limit);
		}
	}

	public void setAttackUsed(int amount) throws InvalidPlayerCommandException {
		if (amount != getAttackUsed()) {
			attackUsed = amount;
			statusListener.onSetAttackUsed(amount);
		}
	}

	public void useAttack() throws InvalidPlayerCommandException {
		attackUsed++;
		statusListener.onAttackUsed();
	}

	public void setWineUsed(int amount) throws InvalidPlayerCommandException {
		if (amount != getWineUsed()) {
			wineUsed = amount;
			statusListener.onSetWineUsed(amount);
		}
	}
	
	public void useWine() throws InvalidPlayerCommandException {
		wineUsed++;
		isWineUsed = true;
		statusListener.onWineUsed();
	}

	public int getAttackUsed() {
		return attackUsed;
	}

	public int getAttackLimit() {
		return attackLimit;
	}

	public boolean isWineUsed() {
		return isWineUsed;
	}

	public int getWineUsed() {
		return wineUsed;
	}

	public int getWineLimit() {
		return wineLimit;
	}

}
