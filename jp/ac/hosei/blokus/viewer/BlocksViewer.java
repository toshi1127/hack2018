package jp.ac.hosei.blokus.viewer;

import jp.ac.hosei.blokus.Game;
import jp.ac.hosei.blokus.Piece;
import jp.ac.hosei.blokus.utils.Canvas;

import java.awt.*;

public class BlocksViewer {

    public static double rate = 1.0;

    public static int cellSize = (int) (30 * rate);
    public static int boardSize = (int) (cellSize * 20);
    public static int boardLeft = (int) ((1920 * rate - (20 * cellSize * 2 + boardSize)) / 2 + (20 * cellSize));
    public static int boardRight = (int) (boardLeft + boardSize);
    public static int boardTop = (int) ((1080 * rate - boardSize) / 2);
    public static int boardBottom = (int) (boardTop + boardSize);
    public static int pieceLeft = (int) (boardLeft + cellSize * 3);
    public static int pieceTop = (int) (boardBottom + cellSize * 2);
    public static int pieceSize = (int) (155 * rate);
    public static int margin = (int) (100 * rate);

    public static int extraWidth = (cellSize * 3) / 2;

    public static int rotateLeft = (int) (boardLeft + boardSize/2 + cellSize);
    public static int rotateTop = (int) (boardBottom + cellSize * 2);
    public static int flipLeft = (int) (boardLeft + boardSize/2 - cellSize);
    public static int flipTop = (int) (boardBottom + cellSize * 6);
    public static int confirmLeft = (int) (boardLeft + boardSize/2 + cellSize * 6);
    public static int confirmTop = (int) (boardBottom + cellSize * 2);
    public static int passLeft = (int) (boardLeft + boardSize/2 + cellSize * 6);
    public static int passTop = (int) (boardBottom + cellSize * 6);
    public static int buttonWidth = (int) (cellSize * 3);
    public static int buttonHeight = (int) (cellSize * 2);

    public static void resize(double r) {
        rate = r;
        cellSize = (int) (30 * rate);
        boardSize = (int) (cellSize * 20);
        boardLeft = (int) ((1920 * rate - (20 * cellSize * 2 + boardSize)) / 2 + (20 * cellSize));
        boardRight = (int) (boardLeft + boardSize);
        boardTop = (int) ((1080 * rate - boardSize) / 2);
        boardBottom = (int) (boardTop + boardSize);
        pieceLeft = (int) (boardLeft + cellSize * 3);
        pieceTop = (int) (boardBottom + cellSize * 2);
        pieceSize = (int) (155 * rate);
        margin = (int) (100 * rate);

        extraWidth = (cellSize * 3) / 2;

        rotateLeft = (int) (boardLeft + boardSize/2 + cellSize);
        rotateTop = (int) (boardBottom + cellSize * 2);
        flipLeft = (int) (boardLeft + boardSize/2 + cellSize);
        flipTop = (int) (boardBottom + cellSize * 5);
        confirmLeft = (int) (boardLeft + boardSize/2 + cellSize * 6);
        confirmTop = (int) (boardBottom + cellSize * 2);
        passLeft = (int) (boardLeft + boardSize/2 + cellSize * 6);
        passTop = (int) (boardBottom + cellSize * 5);
        buttonWidth = (int) (cellSize * 3);
        buttonHeight = (int) (cellSize * 2);
        corners = new int[][]{
                {(int) (030 * rate), (int) (570 * rate)},
                {(int) (1350 * rate), (int) (570 * rate)},
                {(int) (1350 * rate), (int) (30 * rate)},
                {(int) (030 * rate), (int) (30 * rate)}
        };

        outerCorners = new int[][]{
                {boardLeft - cellSize / 2, boardTop + boardSize / 2},
                {boardRight - boardSize / 2, boardTop + boardSize / 2},
                {boardRight - boardSize / 2, boardTop - cellSize / 2},
                {boardLeft - cellSize / 2, boardTop - cellSize / 2}
        };
    }

    private Canvas canvas;

    private int[][] colors = {
            {194, 34, 54},
            {15, 85, 77},
            {51, 44, 113},
            {198, 173, 49},
    };

    private int[][] lightColors = {
            {255, 100, 100},
            {16, 185, 146},
            {100, 100, 255},
            {247, 232, 22},
    };

    private int[][] holdingPositions = {
            {0, 0},
            {0, 3},
            {0, 7},
            {0, 11},
            {0, 16},
            {2, 0},
            {3, 5},
            {2, 11},
            {6, 0},
            {6, 5},
            {5, 10},
            {5, 15},
            {10, 0},
            {9, 6},
            {9, 17},
            {9, 10},
            {13, 0},
            {13, 4},
            {13, 7},
            {9, 13},
            {14, 12}
    };

    public static int[][] corners = {
            {(int) (030 * rate), (int) (570 * rate)},
            {(int) (1350 * rate), (int) (570 * rate)},
            {(int) (1350 * rate), (int) (30 * rate)},
            {(int) (030 * rate), (int) (30 * rate)}
    };

    public static int[][] outerCorners = {
            {boardLeft - cellSize / 2, boardTop + boardSize / 2},
            {boardRight - boardSize / 2, boardTop + boardSize / 2},
            {boardRight - boardSize / 2, boardTop - cellSize / 2},
            {boardLeft - cellSize / 2, boardTop - cellSize / 2}
    };

    // transform matrix for main board
    private int[][] transform;

    private Game game;
    private Piece[] pieces;

    public BlocksViewer() {
        canvas = new Canvas();
        canvas.show((int) (1920 * rate), (int) (1080 * rate));
        canvas.disableAutoRepaint();
        pieces = Piece.pieces;
    }

    public void setGame(Game game) {
        this.game = game;
        transform = game.getTransform();
    }

    public void drawBoard() {
        try {

            Thread.sleep(1000); //3000ミリ秒Sleepする

        } catch (InterruptedException e) {
        }
        canvas.clear();
        canvas.setColor(150, 150, 150);
        canvas.fillRect(0, 0, 1920 * rate, 1080 * rate);
        for (int i = 0; i < 4; i++) {
            int position = game.getPosition(i);
            setLightColor(game.getPlayer(i).getPlayerId());
            canvas.fillRect(outerCorners[position][0], outerCorners[position][1], (boardSize + cellSize) / 2, (boardSize + cellSize) / 2);
        }
        int[][] board = game.getBoard();

        for (int i = 0; i < 20; i++) {
            for (int j = 0; j < 20; j++) {
                switch (board[i][j]) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                        setColor(game.getPlayer(board[i][j]).getPlayerId());
                        canvas.fillRect(boardLeft + j * cellSize, boardTop + i * cellSize, cellSize, cellSize);
                        setLightColor(game.getPlayer(board[i][j]).getPlayerId());
                        canvas.fillRect(boardLeft + 4 * rate + j * cellSize, boardTop + 4 * rate + i * cellSize, cellSize - 7 * rate, cellSize - 7 * rate);
                        break;
                    case -1:
                        canvas.setColor(255, 255, 255);
                        canvas.fillRect(boardLeft + j * cellSize, boardTop + i * cellSize, cellSize, cellSize);
                        break;
                }
//                canvas.fillRect(boardLeft + j * cellSize, boardTop + i * cellSize, cellSize, cellSize);
            }
        }

        for (int i = 0; i < 4; i++) {
            drawHolding(i);
        }

        canvas.setFontSize(20 * rate);
        canvas.setColor(255, 255, 255);
        for (int i = 0; i < 4; i++) {
            canvas.drawStringLeft(corners[i][0], corners[i][1] - (int) (5 * rate), game.getPlayer(i).getName());
        }
//        canvas.drawStringCenter((int) (600 * rate), (int) (880 * rate), game.getPlayer(0).getName());
//        canvas.drawStringCenter((int) (1020 * rate), (int) (350 * rate), game.getPlayer(1).getName());
//        canvas.drawStringCenter((int) (600 * rate), (int) (40 * rate), game.getPlayer(2).getName());
//        canvas.drawStringCenter((int) (180 * rate), (int) (350 * rate), game.getPlayer(3).getName());

        drawFrame();
        canvas.forceRepaint();
    }

    public void drawHolding(int gamePlayerId) {
        int position = game.getPosition(gamePlayerId);
        setColor(game.getPlayer(gamePlayerId).getPlayerId());
        canvas.fillRect(corners[position][0] - cellSize, corners[position][1] - cellSize, cellSize * 20, cellSize * 18);
        setLightColor(game.getPlayer(gamePlayerId).getPlayerId());

        for (int i = 0; i < pieces.length; i++) {
            if (game.getPlayer(gamePlayerId).holds(i)) {
                int[] pos = holdingPositions[i];
                Piece piece = pieces[i];
                int[][] figure = piece.getFigure(0);

//                canvas.setColor(0, 0, 0);
//                canvas.fillRect(corners[position][0], corners[position][1], cellSize, cellSize);
                setLightColor(game.getPlayer(gamePlayerId).getPlayerId());

                for (int j = 0; j < figure.length; j++) {
                    for (int k = 0; k < figure[j].length; k++) {
                        if (figure[j][k] == 1) {
                            int jj = pos[0] + j;
                            int kk = pos[1] + k;

                            int y = corners[position][1] + (jj * transform[0][3] + kk * transform[0][1]) * cellSize;
                            int x = corners[position][0] + (jj * transform[0][2] + kk * transform[0][0]) * cellSize;

                            setColor(game.getPlayer(gamePlayerId).getPlayerId());
                            canvas.fillRect(x, y, cellSize, cellSize);
                            setLightColor(game.getPlayer(gamePlayerId).getPlayerId());
                            canvas.fillRect(x + 4 * rate, y + 4 * rate, cellSize - 7 * rate, cellSize - 7 * rate);
                        }
                    }
                }
            }
        }
    }

    public void drawFrame() {
        canvas.drawImageCenterTop(boardLeft + boardSize / 2, 0, "jp/ac/hosei/blokus/viewer/blokus-alpha.png");
        canvas.setColor(0, 0, 0);
        // 盤面の描画
        for (int i = 0; i < 21; i++) {
            // 横線
            canvas.drawLine(boardLeft, boardTop + i * cellSize,
                    boardRight, boardTop + i * cellSize);
            // 縦線
            canvas.drawLine(boardLeft + i * cellSize, boardTop,
                    boardLeft + i * cellSize, boardBottom);
        }

        // 各持ち駒、横線の描画
        for (int i = 0; i < 17; i++) {
            // Player0
            canvas.drawLine(corners[3][0], corners[3][1] + i * cellSize,
                    corners[3][0] + 18 * cellSize, corners[3][1] + i * cellSize);
            // Player1
            canvas.drawLine(corners[0][0], corners[0][1] + i * cellSize,
                    corners[0][0] + 18 * cellSize, corners[0][1] + i * cellSize);

            // Player2
            canvas.drawLine(corners[1][0], corners[1][1] + i * cellSize,
                    corners[1][0] + 18 * cellSize, corners[1][1] + i * cellSize);
            // Player3
            canvas.drawLine(corners[2][0], corners[2][1] + i * cellSize,
                    corners[2][0] + 18 * cellSize, corners[2][1] + i * cellSize);
        }

        // 各持ち駒、縦線の描画
        for (int i = 0; i < 19; i++) {
            // Player0
            canvas.drawLine(corners[3][0] + i * cellSize, corners[3][1],
                    corners[3][0] + i * cellSize, corners[3][1] + cellSize * 16);
            // Player1
            canvas.drawLine(corners[0][0] + i * cellSize, corners[0][1],
                    corners[0][0] + i * cellSize, corners[0][1] + cellSize * 16);

            // Player2
            canvas.drawLine(corners[1][0] + i * cellSize, corners[1][1],
                    corners[1][0] + i * cellSize, corners[1][1] + cellSize * 16);
            // Player3
            canvas.drawLine(corners[2][0] + i * cellSize, corners[2][1],
                    corners[2][0] + i * cellSize, corners[2][1] + cellSize * 16);
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
