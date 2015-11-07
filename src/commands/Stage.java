package commands;

import commands.operations.TurnDiscardOperation;

import player.PlayerComplete;
import core.Game;
import core.PlayerInfo;

/**
 * stage update, controls basic game flow
 * @author Harry
 *
 */
public class Stage extends Command
{
	/**
	 * 
	 */
	private static final long serialVersionUID = -1512958754082503787L;
	public static final byte TURN_START_BEGINNING = 1;
	public static final byte TURN_START = 2;
	public static final byte TURN_DECISION_BEGINNING = 3;
	public static final byte TURN_DECISION = 4;
	public static final byte TURN_DRAW = 5;
	public static final byte TURN_DEAL_BEGINNING = 6;
	public static final byte TURN_DEAL = 7;
	public static final byte TURN_DISCARD_BEGINNING = 8;
	public static final byte TURN_DISCARD = 9;
	public static final byte TURN_DISCARD_END = 10;
	public static final byte TURN_END = 11;
	
	private PlayerInfo source;
	private int stage;
	
	public Stage(PlayerInfo source,int stage)
	{
		super(null);
		this.source = source;
		this.stage = stage;
	}
	public PlayerInfo getSource()
	{
		return source;
	}
	public int getStage()
	{
		return stage;
	}
	public void nextStage(PlayerComplete player)
	{
		if(stage != TURN_END)
			stage++;
		else
		{
			source = player.getNextPlayerAlive();
			stage = TURN_START_BEGINNING;
		}
	}
	public void nextPlayer(PlayerComplete player)
	{
		source = player.getNextPlayerAlive();
		stage = TURN_START_BEGINNING;
	}
	@Override
	public void ClientOperation(PlayerComplete player)
	{
		System.out.println(source.getName() +" Stage "+stage);
		player.setCurrentStage(this);
		player.clearDisposalArea();
		player.getGameListener().onRemoveCustomizedSelectionPane();
		if(player.equals(source))
		{
			if(!player.isAlive())
			{
				nextPlayer(player);
				player.sendToServer(this);
			}
			if(stage == TURN_DRAW)
			{
				turnDraw(player);
			}
			else if(stage == TURN_DEAL)
			{
				player.startDealing();
			}
			else if(stage == TURN_DISCARD)
			{
				turnDiscard(player);
			}
			else if(stage == TURN_END)
			{
				player.endTurn();
			}
				
			else
			{
				stage++;
				player.sendToServer(this);
			}
		}
	}
	private void turnDraw(PlayerComplete player)
	{
		nextStage(player);
		player.sendToServer(new DrawCardsFromDeck(player.getPlayerInfo(),2,this));
	}
	private void turnDiscard(PlayerComplete player)
	{
		if(player.getHandCount() <= player.getCardOnHandLimit())
		{
			nextStage(player);
			player.sendToServer(this);
		}
		else
		{
			nextStage(player);
			player.sendToServer(new TurnDiscardOperation(player.getPlayerInfo(),this));
		}
	}
	@Override
	public void ServerOperation(Game framework) 
	{
		framework.sendToAllClients(this);
	}
}