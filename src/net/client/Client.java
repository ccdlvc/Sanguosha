package net.client;

import gui.net.client.ClientFrameGui;
import gui.net.client.test.ClientFrameTestGui;

import java.io.IOException;
import java.util.Arrays;

import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import net.Connection;
import net.ConnectionListener;
import utils.Log;

/**
 * A representation of Client
 * 
 * @author Harry
 * 
 */
public class Client {
	private static final String TAG = "Client";
	private final Connector connector;
	private ConnectionListener listener;

	public Client() {
		this.connector = new Connector();
	}

	/**
	 * Try to connect to game server
	 * 
	 * @return the connection object if successful, null if failed
	 */
	public Connection connect() {
		try {
		    Connection connection = connector.connect();
			connection.setConnectionListener(this.getClientListener());
			connection.activate();
			Log.log(TAG, "Listening to Server...");
			return connection;
		} catch (IOException e) {
			listener.onConnectionLost(null, "Connection Failed");
			return null;
		}
	}

	public void registerClientListener(ConnectionListener listener) {
		this.listener = listener;
	}

	public ConnectionListener getClientListener() {
		return listener;
	}

	public static void main(String[] args) {
		Client client = new Client();
		try {
		    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
		        if ("Nimbus".equals(info.getName())) {
		            UIManager.setLookAndFeel(info.getClassName());
		            break;
		        }
		    }
		} catch (Exception e) {
		    // If Nimbus is not available, you can set the GUI to another look and feel.
		}
		if (Arrays.asList(args).contains("test")) {
			ClientFrameTestGui gui = new ClientFrameTestGui();
			client.registerClientListener(gui);
			gui.toRoom(client);
		} else {
			ClientFrameGui gui = new ClientFrameGui(client);
			client.registerClientListener(gui);
		}
	}

}
