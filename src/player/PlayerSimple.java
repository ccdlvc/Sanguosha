package player;

import java.util.Collection;
import java.util.List;

import cards.Card;
import cards.equipments.Equipment;
import cards.equipments.Equipment.EquipmentType;
import listeners.game.CardDisposalListener;
import listeners.game.CardOnHandListener;
import listeners.game.EquipmentListener;
import listeners.game.HealthListener;

/**
 * client side simple player implementation, used to hold information of "other players"
 * @author Harry
 *
 */
public class PlayerSimple extends Player
{
	private HealthListener healthListener;
	private CardOnHandListener cardsOnHandListener;
	private EquipmentListener equipmentListener;
	private CardDisposalListener disposalListener;
	private int cardsCount;
	
	public PlayerSimple(String name) 
	{
		super(name);
		cardsCount = 0;
	}
	public PlayerSimple(String name, int position) 
	{
		super(name,position);
		cardsCount = 0;
	}
	/**
	 * register health listener to monitor the change in health
	 * @param listener
	 */
	public void registerHealthListener(HealthListener listener)
	{
		healthListener = listener;
		healthListener.onSetHealthLimit(getHero().getHealthLimit());
		healthListener.onSetHealthCurrent(getHealthCurrent());
	}
	/**
	 * register card-on-hand listener to monitor the change of card-on-hand
	 * @param listener
	 */
	public void registerCardOnHandListener(CardOnHandListener listener)
	{
		cardsOnHandListener = listener;
	}
	/**
	 * register equipment listener to monitor the change of equipments
	 * @param listener
	 */
	public void registerEquipmentListener(EquipmentListener listener)
	{
		equipmentListener = listener;
	}
	/**
	 * register card disposal listener to monitor the use and disposal of cards
	 * @param listener
	 */
	public void registerCardDisposalListener(CardDisposalListener listener)
	{
		disposalListener = listener;
	}
	/**
	 * <li>{@link HealthListener} notified
	 */
	@Override
	public void changeHealthLimitTo(int n)
	{
		super.changeHealthLimitTo(n);
		healthListener.onSetHealthLimit(getHero().getHealthLimit());
	}
	/**
	 * <li>{@link HealthListener} notified
	 */
	@Override
	public void changeHealthLimitBy(int n)
	{
		super.changeHealthLimitBy(n);
		healthListener.onSetHealthLimit(getHero().getHealthLimit());
	}
	/**
	 * <li>{@link HealthListener} notified
	 */
	@Override
	public void changeHealthCurrentTo(int n)
	{
		super.changeHealthCurrentTo(n);
		healthListener.onSetHealthCurrent(n);
	}
	/**
	 * <li>{@link HealthListener} notified
	 * @param n
	 */
	@Override
	public void changeHealthCurrentBy(int n)
	{
		super.changeHealthCurrentBy(n);
		healthListener.onHealthChangedBy(n);
	}
	/**
	 * <li>{@link CardOnHandListener} notified
	 * @param card
	 */
	@Override
	public void addCard(Card card)
	{
		cardsCount++;
		cardsOnHandListener.onCardAdded(card);
	}
	/**
	 * <li>{@link CardOnHandListener} notified
	 * <li>{@link CardDisposalListener} notified
	 * @param card
	 */
	@Override
	public void useCard(Card card)
	{
		cardsCount--;
		cardsOnHandListener.onCardRemoved(card);
		disposalListener.onCardUsed(card);
	}
	/**
	 * <li>{@link CardDisposalListener} notified
	 * @param card
	 */
	@Override
	public void discardCard(Card card)
	{
		cardsCount--;
		cardsOnHandListener.onCardRemoved(card);
		disposalListener.onCardDisposed(card);
	}
	@Override
	public int getHandCount() 
	{
		return cardsCount;
	}
	public CardDisposalListener getDisposalListener()
	{
		return disposalListener;
	}
	/**
	 * Show a card on the disposal area. The 
	 * card is neither used or discarded.
	 * <li>{@link CardDisposalListener} notified
	 * @param card
	 */
	public void showCard(Card card)
	{
		disposalListener.onCardDisposed(card);
	}
	/**
	 * Show a collection of cards
	 * @param cards
	 */
	public void showCards(Collection<? extends Card> cards)
	{
		for(Card card : cards)
			showCard(card);
	}
	@Override
	public void removeCardFromHand(Card card)
	{
		cardsCount--;
		cardsOnHandListener.onCardRemoved(card);
	}
	/**
	 * discard an equipment
	 * <li>{@link EquipmentListener} notified
	 * @param type
	 */
	@Override
	public void unequip(EquipmentType type)
	{
		equipmentListener.onUnequipped(type);
		super.unequip(type);
	}
	/**
	 * equip new equipment
	 * <li>{@link EquipmentListener} notified
	 * @param equipment : new equipment
	 */
	@Override
	public void equip(Equipment equipment)
	{
		super.equip(equipment);
		equipmentListener.onEquipped(equipment);
	}

	/**
	 * {@link HealthListener} notified
	 */
	@Override
	public void kill()
	{
		super.kill();
		healthListener.onDeath();
		equipmentListener.onUnequipped(EquipmentType.WEAPON);
		equipmentListener.onUnequipped(EquipmentType.SHIELD);
		equipmentListener.onUnequipped(EquipmentType.HORSEPLUS);
		equipmentListener.onUnequipped(EquipmentType.HORSEMINUS);
	}
	public void clearDisposalArea()
	{
		disposalListener.refresh();
	}

}