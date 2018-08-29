package jp.ac.hosei.blokus.server;

import jp.ac.hosei.blokus.Game;
import jp.ac.hosei.blokus.Piece;
import jp.ac.hosei.blokus.utils.Canvas;

public class RoomController implements Runnable {
	private PlayerController[] clients;
	private int[] players;
	private Game game;

	public RoomController() {
		this.clients = new PlayerController[4];
	}

	public void add(int playerId, PlayerController player) {
		clients[playerId] = player;
	}

	public int getPlayerId(PlayerController player) {
		for(int i = 0; i < 4; i++) {
			if(clients[i] == player) {
				return i;
			}
		}
		return -1;
	}

	public void sendPlayerNames() {
		String str = String.format("names,%s,%s,%s,%s",
				clients[0].getName(),
				clients[1].getName(),
				clients[2].getName(),
				clients[3].getName());
		broadcast(str);
	}

	public void sendTotalGames() {
		String str = String.format("games,%d", BlokusServer.totalGames);
		broadcast(str);
	}

	public int[] assignPlayers() {
		players = new int[4];
		for(int i = 0; i < 4; i++) {
			players[i] = i;
		}

		// shuffle
		for(int i = 0; i < 1000; i++) {
			int x = (int)(Math.random() * 4);
			int y = (int)(Math.random() * 4);

			int num = players[x];
			players[x] = players[y];
			players[y] = num;
		}

		String str = String.format("players,%d,%d,%d,%d", players[0], players[1], players[2], players[3]);

		for(int i = 0; i < 4; i++) {
			clients[i].sendLine(str);
		}

		return players;
	}

	public void broadcast(String str) {
		for(int i = 0; i < 4; i++) {
			clients[i].sendLine(str);
		}
	}

	Canvas canvas = null;

	private void canvasInit() {
		canvas = new Canvas();
		canvas.show(500, 200);
		canvas.setColor(0, 0, 0);
		canvas.setFontSize(20);
		for(int i = 0; i < 4; i++) {
			canvas.drawStringLeft(20, i * 40 + 70, clients[i].getName());
		}
		canvas.drawStringRight(300, 30, "Last Game");
		canvas.drawStringRight(450, 30, "Total Point");
	}

	@Override
	public void run() {
		sendPlayerNames();
		sendTotalGames();

		canvasInit();

		for(int i = 0; i < BlokusServer.totalGames; i++) {
			int[] lastPiece = new int[4];
			int[] players = assignPlayers();

			game = new Game(players);
			game.setMyId(0);
			int passCount = 0;

			while(true) {
				for(int j = 0; j < 4; j++) {
					clients[players[j]].sendLine("play");
					// put piece
					String line = clients[players[j]].readLine();

					if(line == null) {
						// read error
						passCount++;
						continue;
					}

					String[] words = line.split(",");
					if(words.length == 0) {
						// something wrong
						passCount++;
					} else if(words[0].equals("pass")) {
						passCount++;
					} else if(words[0].equals("put")){
						if(words.length < 6) {
							// something wrong
							passCount++;
							continue;
						}
						int playerId = Integer.parseInt(words[1]);
						int pieceId = Integer.parseInt(words[2]);
						int pose = Integer.parseInt(words[3]);
						int x = Integer.parseInt(words[4]);
						int y = Integer.parseInt(words[5]);

						if(playerId != j) {
							System.err.println("Strange");
							passCount++;
							continue;
						}

						lastPiece[players[playerId]] = pieceId;

						if(game.putPiece(j, pieceId, pose, x, y)) {
							passCount = 0;
							String broadcastString = String.format("put,%d,%d,%d,%d,%d", j, pieceId, pose, x, y);
							broadcast(broadcastString);
						} else {
							clients[players[j]].sendLine("error," + line);
							passCount++;
						}
					}
				}

				if(passCount >= 4) {
					for(int j = 0; j < 4; j++) {
						clients[players[j]].sendLine("end");
					}

					canvas.setColor(255, 255, 255);
					canvas.fillRect(150, 40, 350, 160);

					canvas.setColor(0, 0, 0);

					// calculating points
					for(int j = 0; j < 4; j++) {
						for(int k = 0; k < 4; k++) {
							if(players[k] == j) {
								int count = 0;
								for(int m = 0; m < 21; m++) {
									if(game.getPlayer(k).holds(m)) {
										Piece piece = Piece.pieces[m];
										int[][] figure = piece.getFigure(0);
										for(int ii = 0; ii < figure.length; ii++) {
											for(int jj = 0; jj < figure[ii].length; jj++) {
												if(figure[ii][jj] == 1) {
													count--;
												}
											}
										}

									}
								}

								// complete
								if(count == 0) {
									count += 15;
									// last block is a single square
									if(lastPiece[j] == 0) {
										count += 5;
									}
								}

								clients[j].addPoint(count);
								System.out.println(clients[j].getName() + ": " + clients[j].getPoint());

								canvas.drawStringRight(300, j * 40 + 70, String.valueOf(count));
								canvas.drawStringRight(450, j * 40 + 70, String.valueOf(clients[j].getPoint()));

								canvas.forceRepaint();
								break;
							}
						}
					}

					break;
				}
			}
		}
	}
}
