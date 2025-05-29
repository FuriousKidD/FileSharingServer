/**
 * 
 */
package com.helper;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * 
 */
public class HelperMethods {
	
	/**
	 * This method is used for sending bytes to the client/server
	 * of which these bytes are to then be read and processed by the cleint/server 
	 * @param fileToSend
	 * @param dataOut
	 * @throws Exception
	 */
	public static void writeBytes(File fileToSend, DataOutputStream dataOut) throws Exception {
		
		int n = 0;
		byte [] buffer = new byte[1024];
		
		try(FileInputStream fileRead = new FileInputStream(fileToSend)){
		
			while((n = fileRead.read(buffer)) > 0) {
				dataOut.write(buffer, 0, buffer.length);
				dataOut.flush();
			}//end while
		
		}//end try block
	}//end writeBytes
	
	/**
	 * This method is used to read the bytes that were sent by client/server
	 * which are then to be processed
	 * @param FileToRead
	 * @param dataIn
	 * @param fileSize
	 * @throws Exception
	 */
	public static void readBytes(File FileToRead, DataInputStream dataIn, int fileSize)throws Exception {
		int n = 0;
		int totalBytes = 0;
		byte [] buffer = new byte[1024];
		
		try (FileOutputStream fileWrite = new FileOutputStream(FileToRead)) {
			while(totalBytes < fileSize) {
				n = dataIn.read(buffer, 0, buffer.length);
				fileWrite.write(buffer, 0, n);
				fileWrite.flush();
				if(n==-1) {//this was the issue, fixed
					break;
				}//end if
				totalBytes += n;
			}//end while
		}//end try block
		
		
	}//end readBytes
	
	/**
	 * This method is used for opening the main streams needed for communication between client and server
	 * as well as the streams for the sending and receiving of data
	 * @param socket
	 * @param buffRead
	 * @param printWrite
	 * @param dataIn
	 * @param dataOut
	 * @throws Exception
	 */
	public static void openStreams(Socket socket, BufferedReader buffRead, PrintWriter printWrite,
										DataInputStream dataIn, DataOutputStream dataOut) throws Exception{
			
			//the opening of streams for client/server communication 
			buffRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printWrite = new PrintWriter(socket.getOutputStream(),true);
			
			//opening the streams that will be used for the reading and writing of bytes to server from client and vice versa
			dataIn = new DataInputStream(socket.getInputStream());
			dataOut = new DataOutputStream(socket.getOutputStream());
			
		
		
	}//end openStreams
	
	/**
	 * Method is used to close the streams once they are no longer in use
	 * Ideally it is best to call this method in a finally block, after all streams are  no longer in use
	 * @param buffRead
	 * @param printWrite
	 * @param dataIn
	 * @param dataOut
	 * @throws Exception
	 */
	public static void closeStreams(BufferedReader buffRead, PrintWriter printWrite,
			DataInputStream dataIn, DataOutputStream dataOut) throws Exception{
		
		if (buffRead != null)
			buffRead.close();
		if(printWrite != null)
			printWrite.close();
		if(dataIn != null)
			dataIn.close();
		if (dataOut != null)
			dataOut.close();
		
	}
}
