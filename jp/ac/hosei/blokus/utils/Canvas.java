package jp.ac.hosei.blokus.utils;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 * キャンバス簡易表示のためのクラス
 * @author satoru
 */
public class Canvas {
	/**
	 * キャンバスに表示されるパネル
	 */
	private DrawingPanel panel;

	/**
	 * 基本フレーム
	 */
	private JFrame frame;

	/**
	 * キャンバスの幅
	 */
	private int width = 500;

	/**
	 * キャンバスの高さ
	 */
	private int height = 500;

	/**
	 * バックエンドに置かれる書き込みイメージバッファ
	 */
	private BufferedImage image;

	/**
	 * イメージバッファへ書くための Graphics インスタンス
	 */
	private Graphics g;

	/**
	 * ポイントされたX座標
	 */
	private int pointedX;

	/**
	 * ポイントされたY座標
	 */
	private int pointedY;

	/**
	 * 自動再描画を行うフラグ
	 */
	private boolean repaintFlag = true;

	/**
	 *
	 */
	private MouseListener listener = null;

	/**
	 * クラス初期化
	 */
	public Canvas() {
		frame = new JFrame("Canvas");
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		panel = new DrawingPanel(image);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().add(panel);
		frame.setSize(width, height);
		g = image.getGraphics();
		g.clearRect(0, 0, width, height);
		g.setColor(new Color(255, 255,255));
		g.fillRect(0, 0, width, height);
	}

	/**
	 * キャンバスの表示
	 * @param w 幅
	 * @param h 高さ
	 */
	public void show(int w, int h) {
		setWindowSize(w, h);
		show();
	}

	/**
	 * キャンバスの表示
	 */
	public void show() {
		frame.setVisible(true);
		int pw = panel.getWidth();
		int ph = panel.getHeight();
		int fwidth = frame.getWidth() + width - pw;
		int fheight = frame.getHeight() + height - ph;
		frame.setSize(fwidth, fheight);

		panel.addMouseListener(new MouseListener() {
			public void mouseClicked(MouseEvent e) {
				if(listener != null) {
					listener.mouseClicked(e);
				}
			}

			public void mouseEntered(MouseEvent e) {
				if(listener != null) {
					listener.mouseEntered(e);
				}
			}

			public void mouseExited(MouseEvent e) {
				if(listener != null) {
					listener.mouseExited(e);
				}
			}

			public void mousePressed(MouseEvent e) {
				if(listener != null) {
					listener.mousePressed(e);
				}
				pointedX = e.getX();
				pointedY = e.getY();

				synchronized(panel) {
					panel.notifyAll();
				}
			}

			public void mouseReleased(MouseEvent e) {
				if(listener != null) {
					listener.mouseReleased(e);
				}
			}
		});
	}

	public void addMouseListener(MouseListener mouseListener) {
		listener = mouseListener;
	}

	/**
	 * キャンバスの画面クリア
	 */
	public void clear() {
		g.clearRect(0, 0, width, height);
		g.setColor(new Color(255, 255,255));
		g.fillRect(0, 0, width, height);
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * テキスト描画
	 * @param x X座標
	 * @param y Y座標
	 * @param text テキスト
	 */
	public void drawString(double x, double y, Object text) {
		g.drawString(text.toString(), round(x), round(y));
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * テキストの中央位置への描画
	 * @param x X座標
	 * @param y Y座標
	 * @param text テキスト
	 */
	public void drawStringCenter(double x, double y, Object text) {
		FontMetrics metrics = g.getFontMetrics();
		int w = metrics.stringWidth(text.toString());
		//int h = metrics.getHeight();

		//g.drawString(text.toString(), round(x) - w / 2, round(y) + h / 2);
		g.drawString(text.toString(), round(x) - w / 2, round(y));
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * テキストの右揃え(高さは中央揃え)の描画
	 * @param x X座標
	 * @param y Y座標
	 * @param text テキスト
	 */
	public void drawStringRight(double x, double y, Object text) {
		FontMetrics metrics = g.getFontMetrics();
		int w = metrics.stringWidth(text.toString());
		//int h = metrics.getHeight();

		//g.drawString(text.toString(), round(x) - w, round(y) + h / 2);
		g.drawString(text.toString(), round(x) - w, round(y));
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * テキストの左揃え(高さは中央揃え)の描画
	 * @param x X座標
	 * @param y Y座標
	 * @param text テキスト
	 */
	public void drawStringLeft(double x, double y, Object text) {
		//FontMetrics metrics = g.getFontMetrics();
		//int h = metrics.getHeight();

		//g.drawString(text.toString(), round(x), round(y) + h / 2);
		g.drawString(text.toString(), round(x), round(y));
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * 線の描画
	 * @param x0 始点のX座標
	 * @param y0 始点のY座標
	 * @param x1 終点のX座標
	 * @param y1 終点のY座標
	 */
	public void drawLine(double x0, double y0, double x1, double y1) {
		g.drawLine(round(x0), round(y0), round(x1), round(y1));
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * 線の描画
	 * @param x X座標
	 * @param y Y座標
	 */
	public void drawPoint(double x, double y) {
		g.drawRect(round(x), round(y), 1, 1);
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * 長方形の描画
	 * @param x 左上のX座標
	 * @param y 左上のY座標
	 * @param w 幅
	 * @param h 高さ
	 */
	public void drawRect(double x, double y, double w, double h) {
		g.drawRect(round(x), round(y), round(w), round(h));
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * 長方形の塗りつぶし描画
	 * @param x 左上のX座標
	 * @param y 左上のY座標
	 * @param w 幅
	 * @param h 高さ
	 */
	public void fillRect(double x, double y, double w, double h) {
		g.fillRect(round(x), round(y), round(w), round(h));
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * 円の描画
	 * @param x 中心のX座標
	 * @param y 中心のY座標
	 * @param r 半径
	 */
	public void drawCircle(double x, double y, double r) {
		int r2 = round(r * 2);
		g.drawOval(round(x - r), round(y - r), r2, r2);
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * 円の塗りつぶし描画
	 * @param x 中心のX座標
	 * @param y 中心のY座標
	 * @param r 半径
	 */
	public void fillCircle(double x, double y, double r) {
		int r2 = round(r * 2);
		g.fillOval(round(x - r), round(y - r), r2, r2);
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * 楕円の描画
	 * @param x 左上のX座標
	 * @param y 左上のY座標
	 * @param w 幅
	 * @param h 高さ
	 */
	public void drawOval(double x, double y, double w, double h) {
		g.drawOval(round(x), round(y), round(w), round(h));
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * 楕円の塗りつぶし描画
	 * @param x 左上のX座標
	 * @param y 左上のY座標
	 * @param w 幅
	 * @param h 高さ
	 */
	public void fillOval(double x, double y, double w, double h) {
		g.fillOval(round(x), round(y), round(w), round(h));
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * 三角形の描画
	 * @param xPoints X座標の配列
	 * @param yPoints Y座標の配列
	 */
	public void drawTriangle(double x0, double y0, double x1, double y1, double x2, double y2) {
		int[] xPoints = new int[] {
				round(x0), round(x1), round(x2)
		};

		int[] yPoints = new int[] {
				round(y0), round(y1), round(y2)
		};

		g.drawPolygon(xPoints, yPoints, xPoints.length);
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * 三角形の描画
	 * @param xPoints X座標の配列
	 * @param yPoints Y座標の配列
	 */
	public void fillTriangle(double x0, double y0, double x1, double y1, double x2, double y2) {
		int[] xPoints = new int[] {
				round(x0), round(x1), round(x2)
		};

		int[] yPoints = new int[] {
				round(y0), round(y1), round(y2)
		};

		g.fillPolygon(xPoints, yPoints, xPoints.length);
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * 多角形の描画
	 * @param xPoints X座標の配列
	 * @param yPoints Y座標の配列
	 */
	public void drawPoligon(int[] xPoints, int[] yPoints) {
		g.drawPolygon(xPoints, yPoints, xPoints.length);
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * 多角形の塗りつぶし描画
	 * @param xPoints X座標の配列
	 * @param yPoints Y座標の配列
	 */
	public void fillPoligon(int[] xPoints, int[] yPoints) {
		g.fillPolygon(xPoints, yPoints, xPoints.length);
		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * ファイルで指定した画像の描画
	 * @param x 左上のX座標
	 * @param y 左上のY座標
	 * @param fileName 画像ファイル名
	 */
	public void drawImage(double x, double y, String fileName) {
		File file = new File(fileName);
		drawImage(round(x), round(y), file);
	}

	/**
	 * ファイルで指定した画像の描画
	 * @param x 左上のX座標
	 * @param y 左上のY座標
	 * @param fileName 画像ファイルオブジェクト
	 */
	public void drawImage(double x, double y, File file) {
		try {
			BufferedImage im = ImageIO.read(file);
			g.drawImage(im, round(x), round(y), null);
			if(repaintFlag) {
				panel.repaint();
			}
		} catch (Exception e) {
			System.err.println(file.getName() + "はオープンできません");
		}
	}

	/**
	 * ファイルで指定した画像の描画
	 * @param x 左上のX座標
	 * @param y 左上のY座標
	 * @param image Imageオブジェクト
	 */
	public void drawImage(double x, double y, Image image) {
		try {
			g.drawImage(image, round(x), round(y), null);
			if(repaintFlag) {
				panel.repaint();
			}
		} catch (Exception e) {
			System.err.println("image を表示できません");
		}
	}

	public void setWidth(int width) {
		BasicStroke wideStroke = new BasicStroke(width);
		Graphics2D g2 = (Graphics2D)g;
		g2.setStroke(wideStroke);
	}

	/**
	 * 色の設定
	 * @param red 赤の値
	 * @param green 緑の値
	 * @param blue 青の値
	 */
	public void setColor(int red, int green, int blue) {
		g.setColor(new Color(red, green, blue));
	}

	/**
	 * 色の設定
	 * @param red 赤の値
	 * @param green 緑の値
	 * @param blue 青の値
	 * @param alpha アルファ成分の値
	 */
	public void setColor(int red, int green, int blue, int alpha) {
		g.setColor(new Color(red, green, blue, alpha));
	}

	/**
	 * キャンバスのサイズ変更
	 * @param w キャンバスの幅
	 * @param h キャンバスの高さ
	 */
	public void setSize(int w, int h) {
		setWindowSize(w, h);
		// frame.setVisible(false);
		frame.setVisible(true);
		int pw = panel.getWidth();
		int ph = panel.getHeight();
		int fwidth = frame.getWidth() + width - pw;
		int fheight = frame.getHeight() + height - ph;
		frame.setSize(fwidth, fheight);

		if(repaintFlag) {
			panel.repaint();
		}
	}

	/**
	 * ウィンドウサイズの設定
	 * setSize() と show() から呼び出される
	 * @param w キャンバスの幅
	 * @param h　キャンバスの高さ
	 */
	private void setWindowSize(int w, int h) {
		// バックグランドにある BufferedImage もサイズ変更する
		BufferedImage newImage = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		// 古い内容のコピー
		panel.setSize(w, h);
		panel.setImage(newImage);
		width = w;
		height = h;
		frame.setSize(w, h);
		g = newImage.getGraphics();
		g.clearRect(0, 0, width, height);
		g.setColor(new Color(255, 255, 255));
		g.fillRect(0, 0, width, height);
		g.drawImage(image, 0, 0, null);
		image = newImage;
	}

	/**
	 * 文字の大きさの変更
	 * @param size 文字サイズ
	 */
	public void setFontSize(double size) {
		Font font = g.getFont();
		Font newFont = font.deriveFont((float)size);
		g.setFont(newFont);
	}

	/**
	 * マウス入力を待つメソッド
	 * @return
	 */
	public void waitForPoint() {
		synchronized(panel) {
			try {
				panel.wait();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * メッセージを出して、マウス入力を待つメソッド
	 * @return
	 */
	public void waitForPoint(String message) {
		JOptionPane.showMessageDialog(null, message);
		waitForPoint();
	}

	/**
	 * 指定した時間だけスリープする
	 * @param time スリープする時間(ミリ秒)
	 */
	public void waitForCountdown(long time) {
		try {
			Thread.sleep(time);
		} catch (Exception e) {
		}
	}

	/**
	 * 自動再描画を無効する
	 */
	public void disableAutoRepaint() {
		repaintFlag = false;
	}

	/**
	 * 自動再描画を有効にする
	 */
	public void enableAutoRepaint() {
		repaintFlag = true;
	}

	/**
	 * 自動再描画の有効/無効を設定する
	 * @param flag 再描画の有効/無効を指定
	 */
	public void setAutoRepaint(boolean flag) {
		repaintFlag = flag;
	}

	public void forceRepaint() {
		panel.repaint();
	}

	/**
	 * ポイントされた X座標を返却
	 * @return
	 */
	public int getPointedX(){
		return pointedX;
	}

	/**
	 * ポイントされた Y座標を返却
	 * @return
	 */
	public int getPointedY() {
		return pointedY;
	}

	/**
	 * 画像の保存
	 * @param file 保存先のファイルオブジェクト
	 */
	public void save(File file) {
		try {
			ImageIO.write(image, "png", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 画像の保存
	 * @param file 保存先のファイル名
	 */
	public void save(String fileName) {
		File file = new File(fileName);
		try {
			ImageIO.write(image, "png", file);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 画像の読み込み
	 * @param file 読み込みファイル名
	 */
	public void load(String fileName) {
		drawImage(0, 0, fileName);
	}

	/**
	 * 画像の読み込み
	 * @param file 読み込みファイルオブジェクト
	 */
	public void load(File file) {
		drawImage(0, 0, file);
	}

	private int round(double x) {
		return (int)(x + 0.5);
	}

	/**
	 * 描画用パネル
	 * @author satoru
	 *
	 */
	class DrawingPanel extends JPanel {
		/**
		 * バックで描画するイメージバッファ―
		 */
		private BufferedImage image;
		static final long serialVersionUID = 0L;

		/**
		 * コンストラクタ
		 * @param image イメージバッファ
		 */
		DrawingPanel(BufferedImage image) {
			this.image = image;
		}

		/**
		 * 表示ルーチン
		 * イメージバッファの内容を全面的にコピーする
		 */
		@Override
		public void paintComponent(Graphics g) {
	        g.drawImage(image, 0, 0, null);
		}

		/**
		 * イメージバッファの設定
		 * @param image イメージバッファ
		 */
		void setImage(BufferedImage image) {
			this.image = image;
		}
	}
}
