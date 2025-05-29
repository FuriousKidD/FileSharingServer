/**
 * 
 */
package com.server;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * This is the class of the server which will be bined to some port, and waits for clients to connect to it
 * before to and from communication
 */
public class Server {

	private ServerSocket ss = null;
	private boolean run = false;// to be set to true if the server object succesfully binds to a port
	
	public Server(int port) {
		
		try {
			ss = new ServerSocket(port);
		} catch (Exception e) {
			System.err.println(e.getMessage());
		}
		run = true;
	}
	
	/*
	 * Method for running the server, on specified port number
	 */
	public void runServer() {
		
		/*
		 * The while loop is for persistency so that it keeps on running, accepting new and multiple client connections.
		 */
		while(run == true) {
			System.out.println("Server waiting to connect...");
			try {
			Thread threads = new Thread(new ClientHandler(ss.accept()));//the thread is to allow multiple clients to be able to connect
			threads.start();
			}
			catch (Exception e) {
				System.err.println(e.getMessage());
			}//end catch
		}//end while
	}//end runServer
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
	}
}
