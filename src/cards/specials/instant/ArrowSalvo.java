package cards.specials.instant;

import commands.Command;
import commands.operations.Operation;
import commands.operations.special_operations.ArrowSalvoOperation;
import player.PlayerComplete;


public class ArrowSalvo extends Instant
{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1395738598490175305L;

	public ArrowSalvo(int num, Suit suit) 
	{
		super(num, suit);
	}

	@Override
	public String getName()
	{
		return "Arrow Salvo";
	}

	@Override
	protected Operation createOperation(PlayerComplete player, Command next) 
	{
		player.getGameListener().setConfirmEnabled(true);
		return new ArrowSalvoOperation(player,this,next);
	}
}
