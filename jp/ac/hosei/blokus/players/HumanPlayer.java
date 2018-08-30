package jp.ac.hosei.blokus.players;

import static jp.ac.hosei.blokus.viewer.BlocksViewer.*;

import jp.ac.hosei.blokus.Piece;
import jp.ac.hosei.blokus.Player;

public class HumanPlayer extends SimplePlayer {

    public HumanPlayer() {
        viewerFlag = true;
    }

    @Override
    public String getPlayerName() {
        return "HumanPlayer";
    }

    @Override
    public void play() {
        /**
         * my game player id
         */
        int myId = game.getMyId();

        Player player = game.getPlayer(myId);

        /**
         * available map
         *   0 .. unavailable
         *   1 .. vacant
         *   2 .. corner position necessary for putting block piece
         */
        int[][] available = game.getAvailability(myId);
        int[][] holdingPositions = viewer.getHoldingPositions();
        int pieceId = -1;
        int pose = 0;
        int i0 = -1;
        int j0 = -1;
        int id = game.getPlayer(game.getMyId()).getPlayerId();
        int[][] figure = null;
        int lastii = -1;
        int lastjj = -1;

        drawButtons();

        while (true) {
            viewer.waitForPoint();
            int x = viewer.getPointedX();
            int y = viewer.getPointedY();

            if (x >= boardLeft && x < boardRight && y >= boardTop && y < boardBottom) {
                // main board

                int ii = (y - boardTop) / cellSize;
                int jj = (x - boardLeft) / cellSize;

                if (lastii == ii && lastjj == jj) {
                    // perform it, when clicking the placed piece again
                    if (pieceId >= 0 && i0 >= 0 && j0 >= 0) {
                        lastii = -1;
                        lastjj = -1;

                        put(myId, pieceId, pose, j0, i0);
                        // sendLine(String.format("put,%d,%d,%d,%d,%d", myId, pieceId, pose, j0, i0));
                        return;
                    }
                }

                lastii = ii;
                lastjj = jj;

                if (pieceId >= 0 && isValid(available, figure, ii, jj)) {
                    i0 = ii;
                    j0 = jj;
                    viewer.setColor(255, 255, 255);
                    viewer.fillRect(boardLeft, boardTop, boardSize, boardSize);
                    viewer.drawBoard();
                    drawFigure(figure, id, boardLeft + jj * cellSize, boardTop + ii * cellSize, true);
                    drawFigure(figure, id, pieceLeft, pieceTop, false);
                    drawButtons();
                }

            } else if (x >= corners[0][0] && x < corners[0][0] + cellSize * 18
                    && y >= corners[0][1] && y < corners[0][1] + cellSize * 15) {
                // selection board
                if (lastii >= 0 || lastjj >= 0) {
                    viewer.setColor(255, 255, 255);
                    viewer.fillRect(boardLeft, boardTop, boardSize, boardSize);
                    viewer.drawBoard();
                    drawButtons();

                    lastii = -1;
                    lastjj = -1;
                    pieceId = -1;
                    i0 = -1;
                    j0 = -1;
                }

                int ii = (y - corners[0][1]) / cellSize;
                int jj = (x - corners[0][0]) / cellSize;

                int lastPieceId = pieceId;
                pieceId = -1;

                // select piece
                for (int i = 0; i < Piece.pieces.length; i++) {
                    if (!player.holds(i)) {
                        continue;
                    }
                    Piece piece = Piece.pieces[i];
                    figure = piece.getFigure(0);

                    if (ii >= holdingPositions[i][0]
                            && ii < holdingPositions[i][0] + figure.length
                            && jj >= holdingPositions[i][1]
                            && jj < holdingPositions[i][1] + figure[0].length) {

                        if (lastPieceId == i) {
                            // rotate it, when clicking the selected piece again
                            pose = (pose + 1) % 8;

                            pieceId = i;
                            figure = Piece.pieces[lastPieceId].getFigure(pose);
                            break;
                        }

                        pieceId = i;
                        pose = 0;
                        break;
                    }
                }
                if (pieceId == -1 && lastPieceId >= 0) {
                    pieceId = lastPieceId;
                    figure = Piece.pieces[pieceId].getFigure(pose);
                }
            } else if (x >= rotateLeft && x < rotateLeft + buttonWidth && y >= rotateTop && y < rotateTop + buttonHeight) {
                // rotation
                if (lastii >= 0 || lastjj >= 0) {
                    viewer.setColor(255, 255, 255);
                    viewer.fillRect(boardLeft, boardTop, boardSize, boardSize);
                    viewer.drawBoard();
                    drawButtons();

                    lastii = -1;
                    lastjj = -1;
                }

                pose = (pose + 1) % 8;

                figure = Piece.pieces[pieceId].getFigure(pose);
            } else if (x >= confirmLeft && x < confirmLeft + buttonWidth && y >= confirmTop && y < confirmTop + buttonHeight) {
                // finish
                lastii = -1;
                lastjj = -1;

                if (pieceId >= 0 && i0 >= 0 && j0 >= 0) {
                    put(myId, pieceId, pose, j0, i0);
                    // sendLine(String.format("put,%d,%d,%d,%d,%d", myId, pieceId, pose, j0, i0));
                    return;
                }
            } else if (x >= passLeft && x < passLeft + buttonWidth && y >= passTop && y < passTop + buttonHeight) {
                // pass
                lastii = -1;
                lastjj = -1;

                pass();
                // sendLine("pass");
                return;
            }

            if (pieceId >= 0) {
                viewer.setColor(150, 150, 150);
                viewer.fillRect(pieceLeft, pieceTop, pieceSize, pieceSize);

                drawFigure(figure, id, pieceLeft, pieceTop, false);
            }
        }
    }

    private void drawButtons() {
        viewer.setColor(220, 220, 220);
//        viewer.fillRect(flipLeft, flipTop, buttonWidth, buttonHeight);
        viewer.setColor(200, 200, 200);
        viewer.fillRect(rotateLeft, rotateTop, buttonWidth, buttonHeight);
        viewer.fillRect(confirmLeft, confirmTop, buttonWidth, buttonHeight);
        viewer.fillRect(passLeft, passTop, buttonWidth, buttonHeight);
        viewer.setColor(0, 0, 0);
//        viewer.drawStringCenter(flipLeft + buttonWidth / 2, flipTop + 30 * rate, "反転");
        viewer.drawStringCenter(rotateLeft + buttonWidth / 2, rotateTop + 30 * rate, "回転");
        viewer.drawStringCenter(confirmLeft + buttonWidth / 2, confirmTop + 30 * rate, "決定");
        viewer.drawStringCenter(passLeft + buttonWidth / 2, passTop + 30 * rate, "パス");
        viewer.forceRepaint();
    }

    private void drawFigure(int[][] figure, int id, int x, int y, boolean strong) {
        for (int i = 0; i < figure.length; i++) {
            for (int j = 0; j < figure[i].length; j++) {
                if (figure[i][j] == 1) {
                    if (strong) {
                        viewer.setLightColor(id);
                    } else {
                        viewer.setColor(id);
                    }
                    viewer.fillRect(x + j * cellSize, y + i * cellSize, cellSize, cellSize);
                    if (!strong) {
                        viewer.setLightColor(id);
                        viewer.fillRect((x + j * cellSize + 4 * rate), (y + i * cellSize + 4 * rate), cellSize - 7 * rate, cellSize - 7 * rate);
                    }
                    viewer.setColor(0, 0, 0);
                    viewer.drawRect(x + j * cellSize, y + i * cellSize, cellSize, cellSize);
                }
            }
        }
        viewer.forceRepaint();
    }
}