package jp.ac.hosei.blokus.players;

import jp.ac.hosei.blokus.Piece;

public class SimplePlayer3 extends SimplePlayer {
	public SimplePlayer3() {
		viewerFlag = false;
	}

	@Override
	public String getPlayerName() {
		return "SimplePlayer3";
	}

	@Override
	protected Solution findFirst(int myId, int[][] available) {
		for(int k = Piece.pieces.length - 1; k >= 0; k--) {
			if(game.getPlayer(myId).holds(k)) {
				Piece piece = Piece.pieces[k];
				for(int ii = 0; ii < 20; ii++) {
					int i = (ii + 10) % 20;
					for(int jj = 0; jj < 20; jj++) {
						int j = (jj + 10) % 20;
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