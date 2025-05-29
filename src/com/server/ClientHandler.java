/**
 * 
 */
package com.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

import com.helper.HelperMethods;


/**
 * This class will handle all client's connected to the server
 * It will do all the checking of protocols as well as instantiating the IO streams of the server side
 * The server sends and receives data through this class
 * The client handler must always be multithreaded if it is to handle multiple clients
 */
public class ClientHandler implements Runnable {
	private Socket socket = null;
	
	/*
	 * I for reading client commands
	 * O for writing response to client
	 * This is for communication between client and server
	 */
	private BufferedReader buffRead = null;//this is for reading text input from a stream
	private PrintWriter printWrite = null;//writing text input to a stream
	
	//O for writing bytes of data to client, wrap it with buffered input for efficiency
	private DataOutputStream dataOut = null;//for writing bytes to a stream
	private DataInputStream dataIn = null;//for reading bytes from a stream
	
	//Files that we focus on. Text and image
	private File txtFileList = null;//will contain the path and file of the text file consiting of list
	private Scanner txtFileRead = null;//scanner object which will be used for reading input from a text file data
	private File imgFile = null;//will haave the image file and its directory
	
	public static int id = 0;
	
	/*
	 * accepts the client's socket object which will be accepted by the server
	 * Object is multithreaded so should be passed in the parameter of the thread constructor within the server's try block
	 */
	public ClientHandler(Socket socket) {
		this.socket = socket;
	}
	
	@Override
	public void run() {//this is the process which handles multiple processes simultaneously

		try {
			processClient();//processes everything to do with the client, and server communication
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}//end catch block
		
		//add finally block for resource management
		finally {
			try {
				HelperMethods.closeStreams(buffRead, printWrite, dataIn, dataOut);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}//end run block
	
	/*
	 * This method handles the exchange of data between client and server
	 * It checks all the protocols and executes the appropriate responses
	 */
	public void processClient() throws Exception{
		
		/** 
		 * Setting up the streams
		 * I/O streams
		 */
		
		buffRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		printWrite = new PrintWriter(socket.getOutputStream(),true);
		
		//binary I/O such as reading and writing files like images at a byte level
		dataIn = new DataInputStream(socket.getInputStream());
		dataOut = new DataOutputStream(socket.getOutputStream());
		
		//HelperMethods.openStreams(this.socket, this.buffRead, this.printWrite, this.dataIn, this.dataOut);
		txtFileList = new File("data/data/server/ImgList.txt");
		/*
		 * Check if file exists
		 * 
		 */
		if(txtFileList.exists()) {
			txtFileRead = new Scanner(txtFileList);
			
			/*
			 *dynamic array that contains the list of image files within the server
			 *The image ID corresponds with the index of array
			 */
			ArrayList<String> fileList = new ArrayList<String>();
			
			String line = "";
			while(txtFileRead.hasNextLine()) {
				line = txtFileRead.nextLine();
				fileList.add(line);
			}//end of while
			
			//
			id = fileList.size();//latest id
			System.out.println("size of array " + fileList.size());
			
			
			//problem fixed
			String clientMessage = buffRead.readLine();
			System.out.println(clientMessage);
			
			if(clientMessage.matches("LIST")) {
				ClientHandler.id = fileList.size();
				
				//send a response for successfully prototcol
				//printWrite.println("Server: Sending list of files");
				//sends the size of the array, for iteration purposes for client side
				printWrite.println(fileList.size());
				System.out.println("size of array " + fileList.size());
				
				//sends the list of images to the client side
				for (String list : fileList) {
					printWrite.println(list);
					System.out.println(list);
				}//end foreach
				
			}//end if
			
			//preproblem fixed
			clientMessage = buffRead.readLine();
			System.out.println(clientMessage);
			if(clientMessage.matches("DOWN\\s\\d+")) {//No problems
				StringTokenizer token = new StringTokenizer(clientMessage, " ");
				token.nextToken();
				System.err.println("clienthandler Line 121");
				
				String index = token.nextToken();
				System.out.println("Index number: " + index);
				/**
				 * This code first extracts the string with corresponding ID requested by client
				 * then, it removes the ID and extracts the name of the image and its extension
				 * for the purpose of reading/writing bytes
				 */
				String imageName = fileList.get(Integer.parseInt(index)-1);
				int indexOfFirstSpace = imageName.indexOf(" "); //gets the index of the first appearing space
				imageName = imageName.substring( indexOfFirstSpace + 1); // gets the text staring from the index after the first space
				System.err.println("Image Name: " + imageName);
				
				imgFile = new File("data/data/server/" + imageName);
				
				if(imgFile.exists()) {
					
					//sending file size to client
					printWrite.println(imgFile.length());
					
					System.err.println("clienthandler Line 145");
					
					/*
					 * sending bytes to the client
					 */
					HelperMethods.writeBytes(imgFile, dataOut);
					System.err.println("clienthandler Line 151");
					
				}//end if
				else {
					printWrite.println("Image File Does Not Exist");
				}//end else
				
				
			}//end if
			
			//preproblem
			clientMessage = buffRead.readLine();
			System.out.println(clientMessage);
			
			if(clientMessage.matches("UP\\s\\d+\\s[\\w.]+\\s\\d+")) {
				System.out.println("UP Command");//fixed
				StringTokenizer token = new StringTokenizer(clientMessage," ");
				token.nextToken();//command
				int id = Integer.parseInt(token.nextToken());
				String fileName = token.nextToken();
				int size = Integer.parseInt(token.nextToken());
				
				File imageUploaded = new File("data/data/server/" + fileName);
				HelperMethods.readBytes(imageUploaded, dataIn, size);
				
				String lineToAdd = id + fileName;
				fileList.add(lineToAdd);
				FileWriter txtFileAppend = new FileWriter(txtFileList, true);
				txtFileAppend.write(lineToAdd);
				
				
			}
		}//end of if
		else {
			printWrite.println("File Not Found!");
		}//end else
	}//end process client
	


}
