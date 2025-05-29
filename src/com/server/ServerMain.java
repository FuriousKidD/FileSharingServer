/**
 * 
 */
package com.server;

/**
 * 
 */
public class ServerMain {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Server server = new Server(5432);
		server.runServer();
	}

}
