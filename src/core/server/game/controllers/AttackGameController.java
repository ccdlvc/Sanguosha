package core.server.game.controllers;

import java.util.stream.Collectors;

import cards.Card;
import cards.basics.Dodge;
import commands.game.client.RequestDodgeGameUIClientCommand;
import core.PlayerInfo;
import core.server.Game;
import exceptions.server.game.InvalidPlayerCommandException;
import net.server.GameRoom;
import player.PlayerComplete;

public class AttackGameController implements GameController {

	public enum AttackStage {
		TARGET_SELECTION, // client side
		BEFORE_TARGET_LOCKED, // client side
		TARGET_LOCKED,
		AFTER_TARGET_LOCKED_SKILLS,
		AFTER_TARGET_LOCKED_WEAPONS,
		DODGE_DECISION, // Taichi Formation
		USING_DODGE,
		AFTER_USING_DODGE,
		ATTACK_DODGED_SKILLS,
		ATTACK_DODGED_WEAPONS,
		ATTACK_NOT_DODGED_PREVENTION,
		ATTACK_NOT_DODGED_ADDITION,
		BEFORE_DAMAGE,
		DAMAGE,
		END;
		
		private static final AttackStage[] VALUES = values();
		
		public AttackStage nextStage() {
			return VALUES[(this.ordinal() + 1) % VALUES.length];
		}
	}
	
	private AttackStage stage;
	private PlayerComplete source;
	private PlayerComplete target;
	private final GameRoom room;
	private final Game game;
	
	public AttackGameController(PlayerInfo source, PlayerInfo target, GameRoom room) {
		this.stage = AttackStage.TARGET_LOCKED;
		this.room = room;
		this.game = room.getGame();
		this.source = game.findPlayer(source);
		this.target = game.findPlayer(target);
	}
	
	public void targetReacted(Card card) {
		if (card == null) {
			// target gives up reacting
			stage = AttackStage.ATTACK_NOT_DODGED_PREVENTION;
			proceed();
			return;
		}
		if (!(card instanceof Dodge) || !target.getCardsOnHand().contains(card)) {
			throw new RuntimeException("Card is not dodge or target does not have this card");
		}
		try {
			target.useCard(card);
		} catch (InvalidPlayerCommandException e) {
			try {
				target.setAttackUsed(target.getAttackUsed() - 1);
			} catch (InvalidPlayerCommandException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			return;
		}
		stage = AttackStage.AFTER_USING_DODGE;
		proceed();
	}

	@Override
	public void proceed() {
		switch (stage) {
			case TARGET_SELECTION:
				break;
			case BEFORE_TARGET_LOCKED:
				break;
			case TARGET_LOCKED:
				stage = stage.nextStage();
				proceed();
				break;
			case AFTER_TARGET_LOCKED_SKILLS:
				stage = stage.nextStage();
				proceed();
				break;
			case AFTER_TARGET_LOCKED_WEAPONS:
				stage = stage.nextStage();
				proceed();
				break;
			case DODGE_DECISION:
				stage = stage.nextStage();
				proceed();
				break;
			case USING_DODGE:
				final PlayerInfo targetInfo = this.target.getPlayerInfo();
				room.sendCommandToPlayers(
					game.getPlayersInfo().stream().collect(
						Collectors.toMap(
							info -> info.getName(),
							info -> new RequestDodgeGameUIClientCommand(targetInfo)
						)
					)
				);
				break;
			case AFTER_USING_DODGE:
				stage = stage.nextStage();
				proceed();
				break;
			case ATTACK_DODGED_SKILLS:
				stage = stage.nextStage();
				proceed();
				break;
			case ATTACK_DODGED_WEAPONS:
				stage = AttackStage.END;
				proceed();
				break;
			case ATTACK_NOT_DODGED_PREVENTION:
				stage = stage.nextStage();
				proceed();
				break;
			case ATTACK_NOT_DODGED_ADDITION:
				stage = stage.nextStage();
				proceed();
				break;
			case BEFORE_DAMAGE:
				stage = stage.nextStage();
				proceed();
				break;
			case DAMAGE:
				target.changeHealthCurrentBy(-1);
				stage = stage.nextStage();
				proceed();
				break;
			case END:
				source.clearDisposalArea();
				target.clearDisposalArea();
				game.popGameController();
				game.getGameController().proceed();
				break;
			default:
				break;
		}
	}

}
