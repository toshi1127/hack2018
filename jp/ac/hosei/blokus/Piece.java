package jp.ac.hosei.blokus;

/**
 * Block piece
 *
 * @author fujita
 *
 */
public class Piece {
	/**
	 * all pieces
	 */
	public static Piece[] pieces = new Piece[21];

	static {
		/** piece patterns */
		int[][][] figures = {
				{{1}},
				{{1, 1}},
				{{1, 1},{0, 1}},
				{{1, 1, 1}},
				{{1, 1},{1, 1}},
				{{0, 1, 0},{1, 1, 1}},
				{{1, 1, 1, 1}},
				{{0, 0, 1}, {1, 1, 1}},
				{{0, 1, 1}, {1, 1, 0}},
				{{1, 0, 0, 0}, {1, 1, 1, 1}},
				{{0, 1, 0}, {0, 1, 0}, {1, 1, 1}},
				{{1, 0, 0}, {1, 0, 0}, {1, 1, 1}},
				{{0, 1, 1, 1}, {1, 1, 0, 0}},
				{{0, 0, 1}, {1, 1, 1}, {1, 0, 0}},
				{{1}, {1}, {1}, {1}, {1}},
				{{1, 0},{1, 1}, {1, 1}},
				{{0, 1, 1},{1, 1, 0}, {1, 0, 0}},
				{{1, 1},{1, 0}, {1, 1}},
				{{0, 1, 1},{1, 1, 0}, {0, 1, 0}},
				{{0, 1, 0},{1, 1, 1}, {0, 1, 0}},
				{{0, 1, 0, 0},{1, 1, 1, 1}}
		};

		for(int i = 0; i < pieces.length; i++) {
			pieces[i] = new Piece(figures[i]);
		}
	}

	/**
	 * figure of block piece
	 */
	public int[][] figure;

	/**
	 * rotation pose from 0 to 7
	 */
	// public int pose; // 0 - 7

	/**
	 * Piece constructor
	 *
	 * @param figure
	 */
	public Piece(int[][] figure) {
		this.figure = figure;
	}

	/**
	 * return figure after rotation specified with pose parameter
	 *
	 * @param pose
	 * @return figure array
	 */
	public int[][] getFigure(int pose) {
		int[][] transform;

		switch (pose) {
		case 0:
			transform = figure;
			break;
		case 1:
			transform = new int[figure[0].length][figure.length];
			for(int i = 0; i < figure.length; i++) {
				for(int j = 0; j < figure[i].length; j++) {
					transform[figure[i].length - j - 1][i] = figure[i][j];
				}
			}
			break;
		case 2:
			transform = new int[figure.length][figure[0].length];
			for(int i = 0; i < figure.length; i++) {
				for(int j = 0; j < figure[i].length; j++) {
					transform[figure.length - i - 1][figure[i].length - j - 1] = figure[i][j];
				}
			}
			break;
		case 3:
			transform = new int[figure[0].length][figure.length];
			for(int i = 0; i < figure.length; i++) {
				for(int j = 0; j < figure[i].length; j++) {
					transform[j][figure.length - i - 1] = figure[i][j];
				}
			}
			break;
		case 4:
			transform = new int[figure.length][figure[0].length];
			for(int i = 0; i < figure.length; i++) {
				for(int j = 0; j < figure[i].length; j++) {
					transform[i][figure[i].length - j - 1] = figure[i][j];
				}
			}
			break;
		case 5:
			transform = new int[figure[0].length][figure.length];
			for(int i = 0; i < figure.length; i++) {
				for(int j = 0; j < figure[i].length; j++) {
					transform[j][i] = figure[i][j];
				}
			}
			break;
		case 6:
			transform = new int[figure.length][figure[0].length];
			for(int i = 0; i < figure.length; i++) {
				for(int j = 0; j < figure[i].length; j++) {
					transform[figure.length - i - 1][j] = figure[i][j];
				}
			}
			break;
		case 7:
			transform = new int[figure[0].length][figure.length];
			for(int i = 0; i < figure.length; i++) {
				for(int j = 0; j < figure[i].length; j++) {
					transform[figure[i].length - j - 1][figure.length - i - 1] = figure[i][j];
				}
			}
			break;

		default:
			transform = figure;
		}

		return transform;
	}

	/**
	 * print out figure on console
	 *
	 * @param figure
	 */
	public static void printFigure(int[][] figure) {
		for(int i = 0; i < figure.length; i++) {
			for(int j = 0; j < figure[i].length; j++) {
				System.out.print(figure[i][j] == 0? "　": "■");
			}
			System.out.println();
		}
	}

	/**
	 * test main for cheking pose transformation
	 *
	 * @param args
	 */
	public static void main(String[] args) {
		for(int i = 0; i < pieces.length; i++) {
			for(int j = 0; j < 8; j++) {
				int[][] figure = pieces[i].getFigure(j);
				printFigure(figure);
				System.out.println();
			}
		}
	}
}
