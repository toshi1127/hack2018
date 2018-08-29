package jp.ac.hosei.blokus.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import jp.ac.hosei.blokus.Game;
import jp.ac.hosei.blokus.viewer.BlocksViewer;

public class ClientController implements Runnable {
	protected BufferedReader reader;
	protected PrintWriter writer;

	protected int playerId;
	protected String[] playerNames = new String[4];
	protected int totalGames;
	protected int[] players = new int[4];
	protected Game game;
	protected BlocksViewer viewer;
	protected boolean viewerFlag = true;

	public ClientController() {
	}

	/**
	 * return player name
	 * @return player name
	 */
	public String getPlayerName() {
		return "Basic Player";
	}

	/**
	 * play one action.
	 */
	public void play() {
		if(viewerFlag) {
			viewer.waitForPoint();
		}
		put(game.getMyId(), 17, 2, 0, 17);
		// sendLine(String.format("put,%d,17,2,0,17", game.getMyId()));
	}

	/**
	 * mailLoop for client player
	 */
	public final void mainLoop() {
		while(true) {
			String line = readLine();
			String[] words = line.split(",");

			if(words[0].equals("end")) {
				break;
			} else if(words[0].equals("play")) {
				play();
			} else if(words[0].equals("put")) {
				int gamePlayerId = Integer.parseInt(words[1]);
				int piece = Integer.parseInt(words[2]);
				int pose = Integer.parseInt(words[3]);
				int x = Integer.parseInt(words[4]);
				int y = Integer.parseInt(words[5]);

				game.putPiece(gamePlayerId, piece, pose, x, y);
				if(viewerFlag) {
					viewer.drawBoard();
				}
			} else if(words[0].equals("error")) {
				System.err.println(line);
				break;
			}
		}
	}

	public final void setSocket(Socket socket) {
		try {
			InputStream is = socket.getInputStream();
			OutputStream os = socket.getOutputStream();
			InputStreamReader isr = new InputStreamReader(is);
			reader = new BufferedReader(isr);
			writer = new PrintWriter(os);
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public final void setInputOutput(InputStream is, OutputStream os) {
		InputStreamReader isr = new InputStreamReader(is);
		reader = new BufferedReader(isr);
		writer = new PrintWriter(os);
	}

	public final void run() {
		try {
			sendLine(String.format("name,%s", getPlayerName()));

			getPlayerId();
			getNames();
			getTotalGames();

			if(viewerFlag) {
				viewer = new BlocksViewer();
			}

			for(int i = 0; i < totalGames; i++) {
				getPlayers();

				game = new Game(players);

				int myId = 0;

				for(int j = 0; j < 4; j++) {
					// game.getPlayer(j).setPlayerName(playerNames[players[j]]);

					if(players[j] == playerId) {
						game.setMyId(j);
						myId = j;
					}
				}

				for(int j = 0; j < 4; j++) {
					int jj = (j + myId) % 4;
					game.getPlayer(j).setPlayerName(playerNames[players[jj]]);
				}

				if(viewerFlag) {
					viewer.setGame(game);
					viewer.drawBoard();
				}

				mainLoop();
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public final void getPlayerId() throws IOException {
		String line = readLine();
		String[] words = line.split(",");
		if(words[0].equals("playerId")) {
			playerId = Integer.parseInt(words[1]);
		} else {
			System.err.println("Protocol Error");
		}
	}

	public final void getNames() throws IOException {
		String line = readLine();
		String[] words = line.split(",");
		if(words[0].equals("names")) {
			playerNames = new String[4];
			for(int i = 0; i < 4; i++) {
				playerNames[i] = words[i + 1];
			}
		} else {
			System.err.println("Protocol Error");
		}
	}

	public final void getTotalGames() throws IOException {
		String line = readLine();
		String[] words = line.split(",");
		if(words[0].equals("games")) {
			totalGames = Integer.parseInt(words[1]);
		} else {
			System.err.println("Protocol Error");
		}
	}

	public final void getPlayers() throws IOException {
		String line = readLine();
		String[] words = line.split(",");
		if(words[0].equals("players")) {
			for(int i = 0; i < 4; i++) {
				players[i] = Integer.parseInt(words[i + 1]);
			}
		} else {
			System.err.println("Protocol Error");
		}
	}

	public final void sendLine(String str) {
		writer.println(str);
		writer.flush();
	}

	public final String readLine() {
		try {
			return reader.readLine();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}

	public final void setViewerFlag(boolean set) {
		viewerFlag = set;
	}

	public final void put(int myId, int pieceId, int pose, int x, int y) {
		sendLine(String.format("put,%d,%d,%d,%d,%d", myId, pieceId, pose, x, y));
	}

	public final void pass() {
		sendLine("pass");
	}
}
