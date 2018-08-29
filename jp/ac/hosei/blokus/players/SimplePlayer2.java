package jp.ac.hosei.blokus.players;

import jp.ac.hosei.blokus.Piece;

public class SimplePlayer2 extends SimplePlayer {
	public SimplePlayer2() {
		viewerFlag = false;
	}

	@Override
	public String getPlayerName() {
		return "SimplePlayer2";
	}

	@Override
	protected Solution findFirst(int myId, int[][] available) {
		for(int k = Piece.pieces.length - 1; k >= 0; k--) {
			if(game.getPlayer(myId).holds(k)) {
				Piece piece = Piece.pieces[k];
				for(int i = 0; i < 20; i++) {
					for(int j = 0; j < 20; j++) {
						// corner positions are very important to find a solutino
						if(available[i][j] == 2) {
							// decending order of the sizes of pieces

							Solution solve = findFirst(available, i, j, piece, k);
							if(solve != null) {
								return solve;
							}
						}
					}
				}
			}
		}
		return null;
	}
}
