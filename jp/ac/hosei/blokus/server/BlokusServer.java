package jp.ac.hosei.blokus.server;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import jp.ac.hosei.blokus.client.ClientController;


/**
 * Blokus server
 *
 * @author Satoru Fujita
 * @version 1.0
 * @date  2018/08/29
 */
public class BlokusServer {
	private static int port = 50000;
	private static List<RoomController> rooms = new ArrayList<RoomController>();
	public static int totalGames = 20;

	public static void main(String[] args) {
		int players = 0;
		RoomController room = null;

		for(int i = 0; i < args.length; i++) {
			try {
				if(players == 0) {
					room = new RoomController();
					rooms.add(room);
				}

				ClientController cc = (ClientController)Class.forName(args[i]).newInstance();

				PipedInputStream pin0 = new PipedInputStream();
				PipedOutputStream pout0 = new PipedOutputStream(pin0);

				PipedInputStream pin1 = new PipedInputStream();
				PipedOutputStream pout1 = new PipedOutputStream(pin1);

				cc.setInputOutput(pin1, pout0);
				new Thread(cc).start();

				PlayerController player = new PlayerController(room, pin0, pout1);
				room.add(players, player);
				player.init(players);
				players++;

				if(players == 4) {
					new Thread(room).start();
					players = 0;
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}

		try {
			@SuppressWarnings("all")
			ServerSocket ss = new ServerSocket(port);

			while(true) {
				Socket s = ss.accept();
				System.out.println("accept");

				if(players == 0) {
					room = new RoomController();
					rooms.add(room);
				}

				PlayerController player = new PlayerController(s, room);
				room.add(players, player);
				player.init(players);

				players++;

				if(players == 4) {
					new Thread(room).start();
					players = 0;
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
}
