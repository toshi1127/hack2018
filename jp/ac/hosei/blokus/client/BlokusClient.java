package jp.ac.hosei.blokus.client;

import java.io.IOException;
import java.net.Socket;

import jp.ac.hosei.blokus.players.SimplePlayer;

/**
 * Blokus Client
 *
 * @author fujita
 * @version 1.00
 * @data 2018/08/29
 */
public class BlokusClient {
	private static int port = 50000;
	private static String server = "127.0.0.1";

	public static void main(String[] args) {
		try {
			Socket socket = new Socket(server, port);

			ClientController client = new SimplePlayer();
			client.setSocket(socket);

			client.run();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
