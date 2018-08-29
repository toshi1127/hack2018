package jp.ac.hosei.blokus;

/**
 * Player for BLOCKS
 *
 * @author fujita
 *
 */
public class Player {
	/**
	 * current condition of holding pieces
	 */
	private boolean[] holding = new boolean[Piece.pieces.length];

	/**
	 * native player id given before playing games.
	 * It is not a game player id in a game.
	 */
	private int playerId;
	private String name;

	/**
	 * Costructor
	 * @param playerId
	 */
	public Player(int playerId) {
		this.playerId = playerId;

		for(int i = 0; i < holding.length; i++) {
			holding[i] = true;
		}
	}

	/**
	 * check if i-th piece is kept in hand.
	 * @param i
	 * @return
	 */
	public boolean holds(int i) {
		return holding[i];
	}

	/**
	 * set a used mark
	 * @param i
	 */
	public void uses(int i) {
		holding[i] = false;
	}

	/**
	 * return native player id
	 * @return native player id
	 */
	public int getPlayerId() {
		return playerId;
	}

	/**
	 * return player name
	 * @return player name
	 */
	public String getName() {
		return name;
	}

	/**
	 * set native player name
	 * @param playerId
	 */
	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	/**
	 * set player name
	 * @param name
	 */
	public void setPlayerName(String name) {
		this.name = name;
	}
}
