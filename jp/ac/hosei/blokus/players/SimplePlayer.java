package jp.ac.hosei.blokus.players;

import jp.ac.hosei.blokus.Piece;
import jp.ac.hosei.blokus.client.ClientController;
import java.lang.*;
import java.util.HashMap;
import java.util.Map;

public class SimplePlayer extends ClientController { 
	 private Map<Solution, Double> SolutionList = new HashMap(); 
	 public int count = 0; 

	 public SimplePlayer() { 
	  viewerFlag = false; 
	 } 

	 @Override 
	 public String getPlayerName() { 
	  return "PlayerTM"; 
	 } 

	 @Override 
	 public void play() { 
	  /** 
	   * my game player id 
	   */ 
	  int myId = game.getMyId(); 

	  /** 
	   * available map 0 .. unavailable 1 .. vacant 2 .. corner position 
	   * necessary for putting block piece 
	   */ 
	  int[][] available = game.getAvailability(myId); 
	  int[][] board = game.getBoard(); 
	  /** 
	   * MAX356(89x4)マス埋まる． 盤面は20x20 
	   */ 
	  for (int i = 0; i < Piece.pieces.length; i++) { 
	   getNumberOfEdge(Piece.pieces[i].getFigure(0)); 
	  } 

	  Solution first; 
	  if (countFilledBlocks(board) <= 100) {// 序盤 
	   first = findFirst(myId, available, board); 
	  } else if (countFilledBlocks(board) <= 80) {// 中盤/適当．あとで直す 
	   first = findMiddleSolution(myId, available); 
	  } else {// 終盤 
	   first = findFirst(myId, available, board); 
	  } 

	  // viewer.waitForPoint(); 
	  if (first != null) { 
	   put(myId, first.piece, first.pose, first.x, first.y); 
	   // sendLine(String.format("put,%d,%d,%d,%d,%d", myId, first.piece, 
	   // first.pose, first.x, first.y)); 
	   // System.out.println(String.format("put,%d,%d,%d,%d,%d", myId, 
	   // first.piece, first.pose, first.x, first.y)); 
	  } else { 
	   pass(); 
	   // sendLine("pass"); 
	   // System.out.println(String.format("pass,%d", myId)); 
	  } 
	 } 

	 protected Solution findFirst(int myId, int[][] available, int[][] board) { 
	  for (int i = 0; i < 20; i++) { 
	   for (int j = 0; j < 20; j++) { 
	    // corner positions are very important to find a solutino 
	    if (available[i][j] == 2) { 
	     // decending order of the sizes of pieces 
	     for (int k = Piece.pieces.length - 1; k >= 0; k--) { 
	      if (game.getPlayer(myId).holds(k)) { 
	       if (k == 16 && count == 0) { 
	        Piece piece = Piece.pieces[k]; 
	        Solution solve = findFirst(available, i, j, piece, k); 
	        count = count + 1; 
	        return solve; 
	       } else if (k == 13 && count == 1 && i + j == 19) { 
	        System.out.println("2手目"); 
	        Piece piece = Piece.pieces[k]; 
	        Solution solve = findFirst(available, i, j, piece, k); 
	        count = count + 1; 
	        if (solve != null) { 
	         return solve; 
	        } 
	         
	         
	       } else if (k == 11 && count == 2 && i + j == 19) { 
	        Piece piece = Piece.pieces[k]; 
	        Solution solve = findFirst(available, i, j, piece, k); 
	        count = count + 1; 
	        return solve; 
	       } else if (k == 19 && count == 3 && i + j == 19) { 
	        Piece piece = Piece.pieces[k]; 
	        Solution solve = findFirst(available, i, j, piece, k); 
	        count = count + 1; 
	        if (solve != null) { 
	         return solve; 
	        } 

	       } 
	      } 

	     } 
	    } 
	   } 
	  } 
	   

	  return null; 

	 } 

	 protected Solution findMiddleSolution(int myId, int[][] available) { 

	  for (int i = 0; i < 20; i++) { 
	   for (int j = 0; j < 20; j++) { 
	    // corner positions are very important to find a solutino 
	    if (available[i][j] == 2) { 
	     // decending order of the sizes of pieces 
	     for (int k = Piece.pieces.length - 1; k >= 0; k--) { 
	      if (game.getPlayer(myId).holds(k)) { 
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
	   } else if ((count != 0) && (SolutionList.get(answer) < SolutionList.get(key))) { 
	    answer = key; 
	   } 
	  } 
	  count = 0; 
	  return answer; 
	 } 

	 protected Solution findFirst(int[][] available, int row, int col, Piece piece, int pieceId) { 
	  double width = piece.figure.length; 

	  if (piece.figure[0].length > width) { 
	   width = piece.figure[0].length; 
	  } 

	  for (int pose = 0; pose < 8; pose++) { 
	   int[][] figure = piece.getFigure(pose); 
	   int h = figure.length; 
	   int w = figure[0].length; 
	   // double s = Math.sqrt(Math.pow(h, 2)+Math.pow(w, 2)); 対角線が長いもの 
	   if (count == 2) { 
	    if (pose == 1) { 
	     int[][] Figure = piece.getFigure(pose); 

	     for (int i = row - h + 1; i <= row; i++) { 
	      if (i < 0 || i + h - 1 >= 20) 
	       continue; 

	      for (int j = col - w + 1; j <= col; j++) { 
	       if (j < 0 || j + w - 1 >= 20) 
	        continue; 

	       if (isValid(available, Figure, i, j)) { 
	        Solution solution = new Solution(pieceId, pose, j, i); 
	        SolutionList.put(solution, width); 
	        return solution; 
	       } 
	      } 
	     } 
	    } 

	   } else { 
	    for (int i = row - h + 1; i <= row; i++) { 
	     if (i < 0 || i + h - 1 >= 20) 
	      continue; 

	     for (int j = col - w + 1; j <= col; j++) { 
	      if (j < 0 || j + w - 1 >= 20) 
	       continue; 

	      if (isValid(available, figure, i, j)) { 
	       Solution solution = new Solution(pieceId, pose, j, i); 
	       SolutionList.put(solution, width); 
	       return solution; 
	      } 
	       
	       
	     } 
	    } 
	   } 
	  } 

	  return null; 
	 } 

	 protected int countFilledBlocks(int[][] board) { 
	  int count = 0; 
	  for (int i = 0; i < board.length; i++) { 
	   for (int j = 0; j < board[i].length; j++) { 
	    if (board[i][j] >= 0) 
	     count++; 
	   } 
	  } 
	  return count; 
	 } 

	 protected int getNumberOfEdge(int[][] figure) { 
	  // ピース1つを入力してそのエッジの数を返す 
	  int[][] pEdge = pieceEdge(figure); 
	  int count = 0; 
	  for (int i = 0; i < pEdge.length; i++) { 
	   for (int j = 0; j < pEdge[i].length; j++) { 
	    count += pEdge[i][j]; 
	   } 
	  } 
	  return count; 
	 } 

	 protected int[][] pieceEdge(int[][] figure) { 
	  // ピース1つを入力して各Blockのエッジ数を返す 
	  int[][] figureEdge = new int[figure.length][figure[0].length]; 
	  int count = 0; 
	  for (int i = 0; i < figure.length; i++) { 
	   for (int j = 0; j < figure[i].length; j++) { 
	    if (figure[i][j] != 0) { 
	     int[] neighbor = new int[4]; 
	     count = 0; 
	     for (int m = -1; m <= 1; m += 2) { 
	      int ii = i + m; 
	      if (ii >= 0 && ii < figure.length) { 
	       if (figure[ii][j] != 0) { 
	        neighbor[m + 1] = 1; 
	       } 
	      } 

	      int jj = j + m; 
	      if (jj >= 0 && jj < figure[i].length) { 
	       if (figure[i][jj] != 0) { 
	        neighbor[m + 2] = 1; 
	       } 
	      } 
	     } 
	     for (int n = 0; n < 4; n++) { 
	      if ((1 - neighbor[n]) * (1 - neighbor[(n + 1) % 4]) != 0) { 
	       count++; 
	      } 
	     } 
	     figureEdge[i][j] = count; 
	    } 
	   } 
	  } 
	  // for(int i = 0;i < figureEdge.length;i++) { 
	  // for (int j = 0;j < figureEdge[i].length;j++) { 
	  // System.out.print(figureEdge[i][j]+" "); 
	  // } 
	  // System.out.println(); 
	  // } 
	  // System.out.println(); 
	  return figureEdge; 
	 } 

	 public boolean isValid(int[][] available, int[][] figure, int row, int col) { 
	  boolean edge = false; 

	  for (int i = 0; i < figure.length; i++) { 
	   for (int j = 0; j < figure[i].length; j++) { 
	    if (figure[i][j] == 1) { 
	     if (row + i >= 20 || col + j >= 20) { 
	      return false; 
	     } else if (available[row + i][col + j] == 0) { 
	      return false; 
	     } else if (available[row + i][col + j] == 2) { 
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