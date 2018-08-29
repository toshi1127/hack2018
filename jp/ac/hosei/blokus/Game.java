package jp.ac.hosei.blokus;

/**
 * BLOCKS game manager
 *
 * @author Satoru Fujita
 *
 */
public class Game {
	private Player[] players = new Player[4];

	/** Board is designed from the bottom side player */
	private int[][] board = new int[20][20];

	private int[] reverse = new int[4];

	/** GamePlayerId of the bottom side player */
	private int myId;

	/** Positions on display
	   Down side is 0.
	   Right side is 1.
	   Top side is 2.
	   Left side is 3.*/
	public int positions[] = new int[4];

	/** transform matrix for main board */
	private int[][] transform = {
			{ 1, 0, 0, 1 },
			{ 0, -1, 1, 0 },
			{ -1, 0, 0, -1 },
			{ 0, 1, -1, 0 }
	};

	/** top left position for each player */
	private int[][] topLeft = {
			{ 0, 0 },
			{ 0, 19 },
			{ 19, 19 },
			{ 19, 0 }
	};

	public Game () {
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				board[i][j] = -1;
			}
		}

		for(int id = 0; id < 4; id++) {
			players[id] = new Player(id);
			this.reverse[id] = id;
		}
	}

	public Game (int[] players) {
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				board[i][j] = -1;
			}
		}

		for(int id = 0; id < 4; id++) {
			this.players[id] = new Player(players[id]);
			this.reverse[players[id]] = id;
		}
	}

	public void setPlayerId(int[] players) {
		for(int id = 0; id < 4; id++) {
			this.players[id].setPlayerId(players[id]);
		}
	}

	public Player getPlayer(int id) {
		return players[id];
	}

	public int[][] getBoard() {
		return board;
	}

	public boolean putPiece(int gamePlayerId, int pieceId, int pose, int x, int y) {
		if(isValid(gamePlayerId, pieceId, pose, x, y)) {
			int position = positions[gamePlayerId];
			Piece piece = Piece.pieces[pieceId];
			int[][] figure = piece.getFigure(pose);

			for(int i = 0; i < figure.length; i++) {
				for(int j = 0; j < figure[i].length; j++) {
					if(figure[i][j] == 1) {
						int ii = topLeft[position][1] + (y + i) * transform[position][3] + (x + j) * transform[position][1];
						int jj = topLeft[position][0] + (y + i) * transform[position][2] + (x + j) * transform[position][0];

						board[ii][jj] = gamePlayerId;
					}
				}
			}

			players[gamePlayerId].uses(pieceId);

			return true;
		} else {
			return false;
		}
	}

	public boolean isValid(int gamePlayerId, int pieceId, int pose, int x, int y) {
		int[][] available = getAvailability(gamePlayerId);
		int position = positions[gamePlayerId];
		boolean edge = false;

		Piece piece = Piece.pieces[pieceId];
		int[][] figure = piece.getFigure(pose);

		for(int i = 0; i < figure.length; i++) {
			for(int j = 0; j < figure[i].length; j++) {
				if(figure[i][j] == 1) {
					int ii = topLeft[position][1] + (y + i) * transform[position][3] + (x + j) * transform[position][1];
					int jj = topLeft[position][0] + (y + i) * transform[position][2] + (x + j) * transform[position][0];

					if(available[ii][jj] == 0) {
						return false;
					} if(available[ii][jj] == 2) {
						edge = true;
					}
				}
			}
		}

		return edge;
	}

	public int[][] getAvailability(int gamePlayerId) {
		int[][] available = new int[20][20];

		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				if(board[i][j] == -1) { // empty
					boolean canPut = true;
					for(int m = -1; m <= 1; m += 2) {
						int ii = i + m;
						if(ii >= 0 && ii < 20) {
							if(board[ii][j] == gamePlayerId) {
								// next to my piece
								canPut = false;
								break;
							}
						}

						int jj = j + m;
						if(jj >= 0 && jj < 20) {
							if(board[i][jj] == gamePlayerId) {
								// next to my piece
								canPut = false;
								break;
							}
						}
					}
					if(canPut) {
						boolean found = false;

						for(int m = -1; m <= 1; m += 2) {
							for(int n = -1; n <= 1; n += 2) {
								int ii = i + m;
								int jj = j + n;
								if(ii >= 0 && ii < 20 && jj >= 0 && jj < 20) {
									if(board[ii][jj] == gamePlayerId) {
										found = true;
										break;
									}
								}
							}
							if(found) {
								break;
							}
						}

						if(found) {
							available[i][j] = 2; // vertex
						} else{
							available[i][j] = 1; // free place
						}
					}
				} else {

				}
			}
		}

		// initial place
		int position = positions[gamePlayerId];

		if(position == 0 && available[19][0] == 1) {
			available[19][0] = 2;
		} else if(position == 1 && available[19][19] == 1) {
			available[19][19] = 2;
		} else if(position == 2 && available[0][19] == 1) {
			available[0][19] = 2;
		} else if(position == 3 && available[0][0] == 1) {
			available[0][0] = 2;
		}

		/* print out availability
		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				System.out.print(available[i][j]);
			}
			System.out.println();
		}
		System.out.println();
		*/

		return available;
	}

	public int[][] getTransform() {
		return transform;
	}

	public int getMyId() {
		return myId;
	}

	public void setMyId(int id) {
		myId = id;

		for(int i = 0; i < 4; i++) {
			positions[i] = (i + 4 - myId) % 4;
		}
	}

	public int getPosition(int id) {
		return positions[id];
	}
}
