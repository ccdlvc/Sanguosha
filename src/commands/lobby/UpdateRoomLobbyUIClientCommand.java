package commands.lobby;

import gui.net.server.LobbyGui;
import net.Connection;
import net.server.RoomInfo;

public class UpdateRoomLobbyUIClientCommand extends LobbyUIClientCommand {

	private static final long serialVersionUID = 3640415584254168416L;

	private final RoomInfo room;
	
	public UpdateRoomLobbyUIClientCommand(RoomInfo room) {
		this.room = room;
	}
	
	@Override
	public void execute(LobbyGui lobby, Connection connection) {
		lobby.updateRoom(room);
	}

}
