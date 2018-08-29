package jp.ac.hosei.blokus.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class PlayerController  {
	@SuppressWarnings("unused")
	private Socket socket;
	@SuppressWarnings("unused")
	private RoomController room;
	private String name;
	private BufferedReader reader;
	private PrintWriter writer;
	@SuppressWarnings("unused")
	private int playerId;
	private InputStream is;
	private OutputStream os;
	private int point;

	public PlayerController(Socket socket, RoomController room) throws IOException {
		this(room, socket.getInputStream(), socket.getOutputStream());
		this.socket = socket;
	}

	public PlayerController(RoomController room, InputStream is, OutputStream os) throws IOException {
		this.room = room;
		this.is = is;
		this.os = os;
	}

	public void init(int playerId) {
		this.playerId = playerId;

		try {
			InputStreamReader isr = new InputStreamReader(is);
			reader = new BufferedReader(isr);
			writer = new PrintWriter(os);

			String line = readLine();
			String[] words = line.split(",");
			if(words[0].equals("name")) {
				name = words[1];
				sendLine(String.format("playerId,%d", playerId));
			} else {
				System.err.println("Protocol Error");
			}
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
	}

	public String readLine() {
		try {
			return reader.readLine();
		} catch (IOException ioe) {
			ioe.printStackTrace();
			return null;
		}
	}

	public void sendLine(String text) {
		writer.println(text);
		writer.flush();
	}

	public String getName() {
		return name;
	}

	public int getPoint() {
		return point;
	}

	public void setPoint(int point) {
		this.point = point;
	}

	public void addPoint(int point) {
		this.point += point;
	}
}
