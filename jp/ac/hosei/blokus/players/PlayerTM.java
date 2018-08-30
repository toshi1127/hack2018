package jp.ac.hosei.blokus.players;

import jp.ac.hosei.blokus.Piece;
import jp.ac.hosei.blokus.client.ClientController;
import jp.ac.hosei.blokus.players.SimplePlayer.Solution;
import java.util.ArrayList;

import java.lang.*;
import java.util.HashMap;
import java.util.Map;

public class PlayerTM extends ClientController {
	private Map<Solution, Double> SolutionList = new HashMap();
	 public int count = 0; 
	public PlayerTM() {
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
		int[][] board = game.getBoard();
		/**
		 * MAX356(89x4)マス埋まる．
		 * 盤面は20x20
		 */
		for (int i = 0;i < Piece.pieces.length;i++) {
			getNumberOfEdge(Piece.pieces[i].getFigure(0));
		}

		Solution first;
		if (countFilledBlocks(board) == 0) {
			count = 0;
		}
		if (countFilledBlocks(board)<70) {// 序盤
			System.out.println("序盤");
			System.out.println(count);
			first = findFirst(myId, available, board); 
//			first = findDistant(myId,available);
		}else if (countFilledBlocks(board)>=70 || count >= 4) {// 中盤/適当．あとで直す
			System.out.println("中盤");
			System.out.println(count);
			first = findDistant(myId,available); //遠くてかつ長いのを出す
			if(count > 8) {
				first = findEdgeSolution(myId,available, board);
			} else if(count > 13) {
				first = findDistant2(myId,available); //遠いいところに出す
			}
//			first = findDistant2(myId,available); //遠いいところに出す
		}else {// 終盤
			first = findDistant2(myId,available);
		}

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
		        } else {
		        	return findDistant(myId,available);
		        }
		         
		         
		       } else if (k == 11 && count == 2 && i + j == 19) { 
		        Piece piece = Piece.pieces[k]; 
		        Solution solve = findFirst(available, i, j, piece, k); 
		        count = count + 1; 
		        if (solve != null) { 
			         return solve; 
			        } else {
			        	return findDistant(myId,available);
			        }
		       } else if (k == 19 && count == 3 && i + j == 19) { 
		        Piece piece = Piece.pieces[k]; 
		        Solution solve = findFirst(available, i, j, piece, k); 
		        count = count + 1; 
		        if (solve != null) { 
			         return solve; 
			        } else {
			        	return findDistant(myId,available);
			        }

		       } else if(count >=4) {
		    	   return findDistant(myId,available);
		       }
		      } 

		     } 
		    } 
		   } 
		  } 
		   

		  return null; 

		 } 
	
	protected Solution findDistant(int myId, int[][] available) {
 		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				if(available[i][j] == 2) {
					// decending order of the sizes of pieces
					if(i < 8 && j > 10) {
						for(int k = Piece.pieces.length - 1; k >= 0; k--) {
							if(game.getPlayer(myId).holds(k)) {
								Piece piece = Piece.pieces[k];
								if( i > j) {
									// iが19から
									for(int I=0;I<19;I++) {
										for(int J=19;J>0;J--) {
											if(J > 10) {
												Solution Solve =findI(I, available, J, piece, k);
												if(Solve != null) {
//													return Solve;
												} 
											}else {
												continue;
											}
										}
									}
								} else if( j > i ) {
									// jが19から
									for(int J=19;J>0;J--) {
										for(int I=0;I<19;I++) {
											if(I < 10) {
												Solution Solve =findI(I, available, J, piece, k);
												if(Solve != null) {
//													return Solve;
												} 
											}else {
												continue;
											}
										}
									}
								}
							}
						}
					} else {
						for(int k = Piece.pieces.length - 1; k >= 0; k--) {
						  if(game.getPlayer(myId).holds(k)) {
							Piece piece = Piece.pieces[k];
							if( i > j) {
								// iが19から
								for(int I=0;I<19;I++) {
									for(int J=19;J>0;J--) {
										if(I < 10) {
											Solution Solve =findI(I, available, J, piece, k);
											if(Solve != null) {
//												return Solve;
											} 
										} else {
											continue;
										}
									}
								}
							} else if( j > i ) {
								// jが19から
								for(int J=19;J>0;J--) {
									for(int I=0;I<19;I++) {
										if(I < 10) {
											Solution Solve =findI(I, available, J, piece, k);
											if(Solve != null) {
//												return Solve;
											} 
										}else {
											continue;
										}
									}
								}
							}
						  }
						}
					}
				}
			}
		}
		Solution solve = findMostLength(SolutionList);
		SolutionList = new HashMap();
		count++;
		return solve;
	}
	
	protected Solution findDistant2(int myId, int[][] available) {
 		for(int i = 0; i < 20; i++) {
			for(int j = 0; j < 20; j++) {
				if(available[i][j] == 2) {
					// decending order of the sizes of pieces
					if(i < 8 && j > 10) {
						for(int k = Piece.pieces.length - 1; k >= 0; k--) {
							if(game.getPlayer(myId).holds(k)) {
								Piece piece = Piece.pieces[k];
								if( i > j) {
									// iが19から
									for(int I=0;I<19;I++) {
										for(int J=19;J>0;J--) {
												Solution Solve =findI(I, available, J, piece, k);
												if(Solve != null) {
//													return Solve;
												} 
											else {
												continue;
											}
										}
									}
								} else if( j > i ) {
									// jが19から
									for(int J=19;J>0;J--) {
										for(int I=0;I<19;I++) {
												Solution Solve =findI(I, available, J, piece, k);
												if(Solve != null) {
//													return Solve;
												} 
											else {
												continue;
											}
										}
									}
								}
							}
						}
					} else {
						for(int k = Piece.pieces.length - 1; k >= 0; k--) {
						  if(game.getPlayer(myId).holds(k)) {
							Piece piece = Piece.pieces[k];
							if( i > j) {
								// iが19から
								for(int I=0;I<19;I++) {
									for(int J=19;J>0;J--) {
											Solution Solve =findI(I, available, J, piece, k);
											if(Solve != null) {
//												return Solve;
											} 
										else {
											continue;
										}
									}
								}
							} else if( j > i ) {
								// jが19から
								for(int J=19;J>0;J--) {
									for(int I=0;I<19;I++) {
											Solution Solve =findI(I, available, J, piece, k);
											if(Solve != null) {
//												return Solve;
											} 
										else {
											continue;
										}
									}
								}
							}
						  }
						}
					}
				}
			}
		}
		Solution solve = findMostLength(SolutionList);
		SolutionList = new HashMap();
		count++;
		return solve;
	}
	
	protected Solution findI(int i, int[][] available, int j, Piece piece, int pieceId) {
		return findFirst(available, i++, j--, piece, pieceId);
	}
	
	protected Solution findJ(int j, int[][] available, int i, Piece piece, int pieceId) {
		return findFirst(available, i++, j--, piece, pieceId);
	}
	
	// 以降戦略
		protected Solution findMiddleSolution(int myId, int[][] available) {
			//防御戦略
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
		for (int i = 0;i < board.length;i++) {
			for (int j = 0;j < board[i].length;j++) {
				if (board[i][j] >= 0) count++;
			}
		}
		return count;
	}

	protected int getNumberOfEdge(int[][] figure) {
		// ピース1つを入力してそのエッジの数を返す
		int[][] pEdge = pieceEdge(figure);
		int count = 0;
		for (int i = 0;i < pEdge.length;i++) {
			for (int j = 0;j < pEdge[i].length;j++) {
				count += pEdge[i][j];
			}
		}
		return count;
	}

	protected int[][] pieceEdge(int[][] figure) {
		// ピース1つを入力して各Blockのエッジ数を返す
		int[][] figureEdge = new int[figure.length][figure[0].length];
		int count = 0;
		for (int i = 0;i < figure.length;i++) {
			for (int j = 0;j < figure[i].length;j++) {
				if (figure[i][j] != 0) {
					int[] neighbor = new int[4];
					count = 0;
					for(int m = -1; m <= 1; m += 2) {
						int ii = i + m;
						if(ii >= 0 && ii < figure.length) {
							if(figure[ii][j] != 0) {
								neighbor[m+1] = 1;
							}
						}

						int jj = j + m;
						if(jj >= 0 && jj < figure[i].length) {
							if(figure[i][jj] != 0) {
								neighbor[m+2] = 1;
							}
						}
					}
					for (int n = 0;n < 4;n++) {
						if ((1-neighbor[n])*(1-neighbor[(n+1)%4]) != 0){
							count++;
						}
					}
					figureEdge[i][j] = count;
				}
			}
		}
		for(int i = 0;i < figureEdge.length;i++) {
			for (int j = 0;j < figureEdge[i].length;j++) {
//				System.out.print(figureEdge[i][j]+" ");
			}
//			System.out.println();
		}
//		System.out.println();
		return figureEdge;
	}
	/**
	 * 打ち終わったときにエッジが最も多くなるSolutionを返す．
	 * エッジは，自ブロックが隣接しておらず，エッジの対角が開いている部分のみを抽出した．
	 * @param myId
	 * @param available
	 * @param board
	 * @return
	 */
	protected Solution findEdgeSolution(int myId, int[][] available, int[][]board) {
		ArrayList<Solution> sols = new ArrayList<Solution>();
		// 置けるピースを探す
		for (int i = 0;i < 20;i++) {
			for (int j = 0;j < 20;j++) {
				// corner positions are very important to find a solutino
				if(available[i][j] == 2) {
					// decending order of the sizes of pieces
					for(int k = Piece.pieces.length - 1; k >= 0; k--) {
						if(game.getPlayer(myId).holds(k)) {
							Piece piece = Piece.pieces[k];

							ArrayList<Solution> solve = findPoses(available, i, j, piece, k);
							if(solve != null) {
								sols.addAll(solve);
							}
						}
					}
				}
			}
		}
		// 置けるピースのエッジを数える
		// いらない？
//		for (int i = 0;i < sols.size();i++) {
//			Solution s = sols.get(i);
//			int sEdge = getNumberOfEdge(Piece.pieces[s.piece].getFigure(s.pose));
//		}
		// 置けるピースを場に置いた時の使えるエッジを数える
		// 最善を返す
		int bestEdge = -1;
		Solution bestSol = null;
		for (int i = 0;i < sols.size();i++) {
			int[][] boardClone = setBlock(board,sols.get(i),myId);//ボードに仮置きする操作
			int boardEdge = getBoardEdge(boardClone, myId);
			if (boardEdge>bestEdge) {
				bestEdge = boardEdge;
				bestSol = sols.get(i);
			}
		}
		if (bestSol != null) {
			return bestSol;
		}
		return null;
	}
	/**
	 * findFirst改変．
	 *置ける姿勢全部を返す．
	 * @param available
	 * @param row
	 * @param col
	 * @param piece
	 * @param pieceId
	 * @return
	 */
	protected ArrayList<Solution> findPoses(int[][] available, int row, int col, Piece piece, int pieceId) {
		double width = piece.figure.length;
		ArrayList<Solution> s = new ArrayList<Solution>();
		if(piece.figure[0].length > width) {
			width = piece.figure[0].length;
		}

		for(int pose = 0; pose < 8; pose++) {
			int[][] figure = piece.getFigure(pose);
			int h = figure.length;
			int w = figure[0].length;
			for(int i = row - h + 1; i <= row; i++) {
				if(i < 0 || i + h - 1 >= 20) continue;

				for(int j = col - w + 1; j <= col; j++) {
					if(j < 0 || j + w - 1 >= 20) continue;

					if(isValid(available, figure, i, j)) {
						Solution solution = new Solution(pieceId, pose, j, i);
						s.add(solution);
					}
				}
			}
		}
		if (s.size()>0) {
			return s;
		}
		return null;
	}

	protected int[][] getPieceEdge(int[][] figure) {
		// ピース1つを入力して各Blockのエッジ数を返す
		int[][] figureEdge = new int[figure.length][figure[0].length];
		int count = 0;
		for (int i = 0;i < figure.length;i++) {
			for (int j = 0;j < figure[i].length;j++) {
				if (figure[i][j] != 0) {
					int[] neighbor = new int[4];
					count = 0;
					for(int m = -1; m <= 1; m += 2) {
						int ii = i + m;
						if(ii >= 0 && ii < figure.length) {
							if(figure[ii][j] != 0) {
								neighbor[m+1] = 1;
							}
						}

						int jj = j + m;
						if(jj >= 0 && jj < figure[i].length) {
							if(figure[i][jj] != 0) {
								neighbor[m+2] = 1;
							}
						}
					}
					for (int n = 0;n < 4;n++) {
						if ((1-neighbor[n])*(1-neighbor[(n+1)%4]) != 0){
							count++;
						}
					}
					figureEdge[i][j] = count;
				}
			}
		}
//		for(int i = 0;i < figureEdge.length;i++) {
//			for (int j = 0;j < figureEdge[i].length;j++) {
//				System.out.print(figureEdge[i][j]+" ");
//			}
//			System.out.println();
//		}
//		System.out.println();
		return figureEdge;
	}
	/**
	 * 盤面に自分のエッジがいくつあるか返す<br>
	 * 自分のだけでエッジを探索する<br>
	 * 自分のエッジの対角方向に人のIDがなければ使えるエッジである
	 * @param board
	 * @param myId
	 * @return
	 */
	protected int getBoardEdge(int[][] board, int myId) {
		int edge = 0;
		int count = 0;
		int idx = 0;
		for (int i = 0;i < 20-1;i++) {//ブロックでなくgridpointを見る
			for (int j = 0;j < 20-1;j++) {
				count = 0;
				for (int k = 0;k < 2;k++) {//頂点に接する4ブロック
					for (int l = 0;l < 2;l++) {
						if (board[i+k][j+l] == myId) {
							count++;
							idx = 2*k+l;
						}
					}
				}
				if (count == 1 && board[i+(1-idx/2)][j+(1-idx%2)] == -1) {
					edge++;
				}
			}
		}
		return edge;
	}
	/**
	 * Game.javaのputPieceを利用<br>
	 * 盤面に1つ仮置きする
	 * @param board
	 * @param s
	 * @param myId
	 * @return
	 */
	protected int[][] setBlock(int[][] board, Solution s, int myId) {
		int r=board.length, c = board[0].length;
		int[][] boardClone = new int[r][c];
		for (int i = 0;i < r;i++) {
			for (int j = 0;j < c;j++) {
				boardClone[i][j] = board[i][j];
			}
		}
		int[][] fig = (Piece.pieces[s.piece].getFigure(s.pose));
		for(int i = 0; i < fig.length; i++) {
			for(int j = 0; j < fig[i].length; j++) {
				if(fig[i][j] == 1) {
					int ii = topLeft[0][1] + (s.y + i) * transform[0][3] + (s.x + j) * transform[0][1];
					int jj = topLeft[0][0] + (s.y + i) * transform[0][2] + (s.x + j) * transform[0][0];
//					System.out.println("("+ii+","+jj+")");
					if (boardClone[ii][jj] == -1) {
						boardClone[ii][jj] = myId;
					}else {
						System.out.println("Error! this place cannot set block");
					}
				}
			}
		}
		return boardClone;
	}
	/** transform matrix for main board
	 * Game.javaのコピー
	 * */
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