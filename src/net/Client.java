package net;


import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import update.ClientReadyNotification;
import update.Update;
import listener.ClientListener;
import listener.GameListener;

public class Client extends Thread
{
	private int masterPort;
	private String masterHost;
	
	private ClientListener listener;
	private ObjectOutputStream out;
	
	public Client(ClientListener listener)
	{
		this.listener = listener;
	}
	/**
	 * Set master's port to connect
	 * @param port
	 */
	public void setPort(int port)
	{
		masterPort = port;
	}
	/**
	 * Set master's host to connect
	 * @param host
	 */
	public void setHost(String host)
	{
		masterHost = host;
	}
	/**
	 * Send a notification/game update to master
	 * @param note
	 */
	public void sendToMaster(Update update)
	{
		try
		{
			out.writeObject(update);
		}
		catch(IOException e)//need to handle connection failure in the future
		{
			System.err.println("Client: I/O Exception when sending notification");
			e.printStackTrace();
		}
	}
	@Override
	public void run()
	{
		Socket socket = null;
		ObjectInputStream in;
		try
		{
			socket = new Socket(masterHost,masterPort);
			out = new ObjectOutputStream(socket.getOutputStream());
			in = new ObjectInputStream(socket.getInputStream());
			listener.onNotified(new ClientReadyNotification());
			System.out.println("Listening to master");
			while(true)
			{
				Update update = (Update)in.readObject();
				listener.onNotified(update);
			}
		}
		catch(IOException e)
		{
			System.err.println("Client: I/O Exception when connecting with master");
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) 
		{
			System.err.println("Client: Received invalid response from master");
			e.printStackTrace();
		}
	}
}
