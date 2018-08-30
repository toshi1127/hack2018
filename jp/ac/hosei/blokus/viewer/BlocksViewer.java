package jp.ac.hosei.blokus.viewer;

import jp.ac.hosei.blokus.Game;
import jp.ac.hosei.blokus.Piece;
import jp.ac.hosei.blokus.utils.Canvas;

public class BlocksViewer {
	public static double rate = 0.5;

	public static int cellSize = (int)(20 * rate);
	public static int boardSize = (int)(cellSize * 20);
	public static int boardLeft = (int)(400 * rate);
	public static int boardRight = (int)(boardLeft + boardSize);
	public static int boardTop = (int)(400 * rate);
	public static int boardBottom = (int)(boardTop + boardSize);
	public static int pieceLeft = (int)(900 * rate);
	public static int pieceTop = (int)(900 * rate);
	public static int pieceSize = (int)(100 * rate);
	public static int margin = (int)(100 * rate);

	public static int extraWidth = (cellSize * 3) / 2;

	public static int rotateLeft = (int)(850 * rate);
	public static int rotateTop = (int)(1100 * rate);
	public static int confirmLeft = (int)(970 * rate);
	public static int confirmTop = (int)(1100 * rate);
	public static int passLeft = (int)(1090 * rate);
	public static int passTop = (int)(1100 * rate);
	public static int buttonWidth = (int)(100 * rate);
	public static int buttonHeight = (int)(50 * rate);

	private Canvas canvas;

	private int[][] colors = {
			{ 255, 0, 0 },
			{ 0, 255, 0 },
			{ 0, 0, 255 },
			{ 255, 255, 0 },
	};

	private int[][] lightColors = {
			{ 255, 100, 100 },
			{ 100, 255, 100 },
			{ 100, 100, 255 },
			{ 255, 255, 100 },
	};

	private int[][] holdingPositions = {
		{ 0, 3 },
		{ 0, 7 },
		{ 0, 12 },
		{ 0, 17 },
		{ 2, 0 },
		{ 2, 4 },
		{ 3, 9 },
		{ 2, 15 },
		{ 2, 20 },
		{ 6, 0 },
		{ 5, 5 },
		{ 5, 9 },
		{ 6, 13 },
		{ 5, 18 },
		{ 5, 22 },
		{ 9, 0 },
		{ 9, 3 },
		{ 9, 7 },
		{ 9, 11 },
		{ 9, 15 },
		{ 10, 19 }
	};

	int[][] corners = {
			{ (int)(370 * rate), (int)(900 * rate) },
			{ (int)(900 * rate), (int)(810 * rate) },
			{ (int)(810 * rate), (int)(280 * rate) },
			{ (int)(280 * rate), (int)(370 * rate) }
	};

	// transform matrix for main board
	private int[][] transform;

	private Game game;
	private Piece[] pieces;

	public BlocksViewer() {
		canvas = new Canvas();
		canvas.show((int)(1200 * rate), (int)(1200 * rate));
		canvas.disableAutoRepaint();
		pieces = Piece.pieces;
	}

	public void setGame(Game game) {
		this.game = game;
		transform = game.getTransform();
	}

	public void drawBoard() {
		canvas.clear();
		int[][] board = game.getBoard();

		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				switch (board[i][j]) {
				case 0:
				case 1:
				case 2:
				case 3:
					setLightColor(game.getPlayer(board[i][j]).getPlayerId());
					break;
				case -1:
					canvas.setColor(255, 255, 255);
					break;
				}
				canvas.fillRect(boardLeft + j * cellSize, boardTop + i * cellSize, cellSize, cellSize);
			}
		}

		for(int i = 0; i < 4; i++) {
			drawHolding(i);
		}

		canvas.setFontSize(20 * rate);
		canvas.setColor(0, 0, 0);
		canvas.drawStringCenter((int)(600 * rate), (int)(880 * rate), game.getPlayer(0).getName());
		canvas.drawStringCenter((int)(1020 * rate), (int)(350 * rate), game.getPlayer(1).getName());
		canvas.drawStringCenter((int)(600 * rate), (int)(40 * rate), game.getPlayer(2).getName());
		canvas.drawStringCenter((int)(180 * rate), (int)(350 * rate), game.getPlayer(3).getName());

		drawFrame();
		canvas.forceRepaint();
	}

	public void drawHolding(int gamePlayerId) {
		setLightColor(game.getPlayer(gamePlayerId).getPlayerId());
		int position = game.getPosition(gamePlayerId);

		for(int i = 0; i < pieces.length; i++) {
			if(game.getPlayer(gamePlayerId).holds(i)) {
				int[] pos = holdingPositions[i];
				Piece piece = pieces[i];
				int[][] figure = piece.getFigure(0);

				for(int j = 0; j < figure.length; j++) {
					for(int k = 0; k < figure[j].length; k++) {
						if(figure[j][k] == 1) {
							int jj = pos[0] + j;
							int kk = pos[1] + k;

							int y = corners[position][1] + (jj * transform[position][3] + kk * transform[position][1]) * cellSize;
							int x = corners[position][0] + (jj * transform[position][2] + kk * transform[position][0]) * cellSize;

							canvas.fillRect(x, y, cellSize, cellSize);
						}
					}
				}
			}
		}
	}

	public void drawFrame() {
		canvas.setColor(0, 0, 0);
		for(int i = 0; i < 21; i++) {
			canvas.drawLine(boardLeft, boardTop + i * cellSize,
					boardRight, boardTop + i * cellSize);
			canvas.drawLine(boardLeft + i * cellSize, boardTop,
					boardLeft + i * cellSize, boardBottom);
		}

		for(int i = 0; i < 13; i++) {
			canvas.drawLine(boardLeft - extraWidth, boardBottom + margin + i * cellSize,
					boardRight + extraWidth, boardBottom + margin + i * cellSize);
			canvas.drawLine(boardLeft - extraWidth, boardTop - margin - i * cellSize,
					boardRight + extraWidth, boardTop - margin - i * cellSize);

			canvas.drawLine(boardRight + margin + i * cellSize, boardTop - extraWidth,
					boardRight + margin + i * cellSize, boardBottom + extraWidth);
			canvas.drawLine(boardLeft - margin - i * cellSize, boardTop - extraWidth,
					boardLeft - margin - i * cellSize, boardBottom + extraWidth);
		}

		for(int i = 0; i < 24; i++) {
			canvas.drawLine(boardLeft - extraWidth + i * cellSize, boardRight + margin,
					boardLeft - extraWidth + i * cellSize, boardRight + margin + cellSize * 12);
			canvas.drawLine(boardLeft - extraWidth + i * cellSize, boardLeft - margin,
					boardLeft - extraWidth + i * cellSize, boardLeft - margin - cellSize * 12);

			canvas.drawLine(boardRight + margin, boardTop - extraWidth + i * cellSize,
					boardRight + margin + cellSize * 12, boardTop - extraWidth + i * cellSize);
			canvas.drawLine(boardLeft - margin, boardTop - extraWidth + i * cellSize,
					boardLeft - margin - cellSize * 12, boardTop - extraWidth + i * cellSize);
		}
	}

	public void setLightColor(int id) {
		canvas.setColor(lightColors[id][0], lightColors[id][1], lightColors[id][2]);
	}

	public void setColor(int id) {
		canvas.setColor(colors[id][0], colors[id][1], colors[id][2]);
	}

	public void waitForPoint() {
		canvas.waitForPoint();
	}

	public int getPointedX() {
		return canvas.getPointedX();
	}

	public int getPointedY() {
		return canvas.getPointedY();
	}

	public int[][] getHoldingPositions() {
		return holdingPositions;
	}

	public void setColor(int r, int g, int b) {
		canvas.setColor(r, g, b);
	}

	public void fillRect(double x, double y, double width, double height) {
		canvas.fillRect(x, y, width, height);
	}

	public void drawRect(double x, double y, double width, double height) {
		canvas.drawRect(x, y, width, height);
	}

	public void drawStringCenter(double x, double y, String text) {
		canvas.drawStringCenter(x, y, text);
	}

	public void forceRepaint() {
		canvas.forceRepaint();
	}

	public Canvas getCanvas() {
		return canvas;
	}
}
