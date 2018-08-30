package jp.ac.hosei.blokus;

import jp.ac.hosei.blokus.server.BlokusServer;

/**
 * Simple launcher
 *
 * @author fujita
 * @version 1.0
 * @date 2018/08/29
 */
public class Launcher {
	public static void main(String[] args) {
		/*
		String[] arguments = {
				"jp.ac.hosei.blokus.client.ClientController",
				"jp.ac.hosei.blokus.client.ClientController",
				"jp.ac.hosei.blokus.client.ClientController",
				"jp.ac.hosei.blokus.client.ClientController"
		};
		*/
		String[] arguments = {
				"jp.ac.hosei.blokus.players.PlayerTM",
				"jp.ac.hosei.blokus.players.SimplePlayer2",
				"jp.ac.hosei.blokus.players.SimplePlayer3",
//				"jp.ac.hosei.blokus.players.SimplePlayer3"
				"jp.ac.hosei.blokus.players.HumanPlayer"
		};

		BlokusServer.main(arguments);
	}
}
