package cards.specials.instant;

import commands.Command;
import commands.operations.Operation;
import commands.operations.special.BarbarianInvasionOperation;
import player.PlayerComplete;


public class BarbarianInvasion extends Instant
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 8054906715946205031L;

	public BarbarianInvasion(int num, Suit suit) 
	{
		super(num, suit);
	}

	@Override
	public String getName() 
	{
		return "Barbarian Invasion";
	}

	@Override
	protected Operation createOperation(PlayerComplete player, Command next) 
	{
		player.getGameListener().setConfirmEnabled(true);
		return new BarbarianInvasionOperation(player,this,next);
	}
}
