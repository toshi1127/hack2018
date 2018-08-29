package jp.ac.hosei.blokus.players;

import jp.ac.hosei.blokus.Piece;
import jp.ac.hosei.blokus.client.ClientController;
import java.lang.*;
import java.util.HashMap;
import java.util.Map;

public class SimplePlayer extends ClientController {
	private Map<Solution, Double> SolutionList = new HashMap();
	public SimplePlayer() {
		viewerFlag = false;
	}

	@Override
	public String getPlayerName() {
		return "SimplePlayer";
	}

	@Override
	public void play() {
		/**
		 * my game player id
		 */
		int myId = game.getMyId();

		/**
		 * available map
		 *   0 .. unavailable
		 *   1 .. vacant
		 *   2 .. corner position necessary for putting block piece
		 */
		int[][] available = game.getAvailability(myId);

		Solution first = findFirst(myId, available);

		// viewer.waitForPoint();
		if(first != null) {
			put(myId, first.piece, first.pose, first.x, first.y);
			// sendLine(String.format("put,%d,%d,%d,%d,%d", myId, first.piece, first.pose, first.x, first.y));
			// System.out.println(String.format("put,%d,%d,%d,%d,%d", myId, first.piece, first.pose, first.x, first.y));
		} else {
			pass();
			// sendLine("pass");
			// System.out.println(String.format("pass,%d", myId));
		}
	}

	protected Solution findFirst(int myId, int[][] available) {

		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				// corner positions are very important to find a solutino
				if(available[i][j] == 2) {
					// decending order of the sizes of pieces
					for(int k = Piece.pieces.length - 1; k >= 0; k--) {
						if(game.getPlayer(myId).holds(k)) {
							Piece piece = Piece.pieces[k];
							Solution solve = findFirst(available, i, j, piece, k);
						}
					}
				}
			}
		}
		Solution solve = findMostLength(SolutionList);
		SolutionList = new HashMap();
		return solve;
	}
	
	protected Solution findMostLength(Map<Solution, Double> SolutionList) {
		Solution answer = null;
		int count = 0;
		for (Solution key : SolutionList.keySet()) {
			if (count == 0) {
				answer = key;
				count++;
			} else if((count != 0) && (SolutionList.get(answer) < SolutionList.get(key))) {
				answer = key;
			}
		}
		count = 0;
		return answer;
	}

	protected Solution findFirst(int[][] available, int row, int col, Piece piece, int pieceId) {
		double width = piece.figure.length;

		if(piece.figure[0].length > width) {
			width = piece.figure[0].length;
		}

		for(int pose = 0; pose < 8; pose++) {
			int[][] figure = piece.getFigure(pose);
			int h = figure.length;
			int w = figure[0].length;
//			double s = Math.sqrt(Math.pow(h, 2)+Math.pow(w, 2)); 対角線が長いもの
			for(int i = row - h + 1; i <= row; i++) {
				if(i < 0 || i + h - 1 >= 20) continue;

				for(int j = col - w + 1; j <= col; j++) {
					if(j < 0 || j + w - 1 >= 20) continue;

					if(isValid(available, figure, i, j)) {
						Solution solution = new Solution(pieceId, pose, j, i);
						SolutionList.put(solution, width);
						return solution;
					}
				}
			}
		}

		return null;
	}

	public boolean isValid(int[][] available, int[][] figure, int row, int col) {
		boolean edge = false;

		for(int i = 0; i < figure.length; i++) {
			for(int j = 0; j < figure[i].length; j++) {
				if(figure[i][j] == 1) {
					if(row + i >= 20 || col + j >= 20) {
						return false;
					} else if(available[row + i][col + j] == 0) {
						return false;
					} else if(available[row + i][col + j] == 2) {
						edge = true;
					}
				}
			}
		}

		return edge;
	}

	protected class Solution {
		int piece;
		int pose;
		int x;
		int y;

		Solution(int piece, int pose, int x, int y) {
			this.piece = piece;
			this.pose = pose;
			this.x = x;
			this.y = y;
		}
	}
}