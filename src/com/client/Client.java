/**
 * 
 */
package com.client;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

import com.helper.HelperMethods;

/**
 * 
 */
public class Client {
	
	/*
	 * The BufferedReader is to be used for reading server responses
	 * The PrintWriter is to be used for writing protocols to the server
	 */
	private BufferedReader buffRead = null;
	private PrintWriter printWrite = null;
	
	/*
	 * These are to be used for reading and writing bytes
	 */
	private DataInputStream dataIN = null;
	private DataOutputStream dataOUT = null;
	
	private File imgFile = null;


	private String list = "";
	
	private Socket socket = null;
	private int port = 0;
	private InetAddress host = null;
	
	/**
	 * 
	 * @param port
	 * @param host
	 */
	public Client(int port, InetAddress host) {
		this.port = port;
		this.host = host;
	}
	
	
	public void connectToServer() throws Exception{
		
		socket = new Socket(host, port);
		System.out.println("\n" + host + " connected to server on port: " + port);
		
		buffRead = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		printWrite = new PrintWriter(socket.getOutputStream(),true);
		
		dataIN = new DataInputStream(socket.getInputStream());
		dataOUT = new DataOutputStream(socket.getOutputStream());
		
		/*
		printWrite.println("LIST");
		//problem
		String strSize = buffRead.readLine();
		System.out.println("Size: " + strSize);
		String list = "";
		//int arrLength = Integer.parseInt(strSize);//read the size of array containing list
		//for(int i = 0; i < arrLength; i++) {
			//list = list + buffRead.readLine() + "\n";
			//System.out.println(list);
		//}
		*/
		
	}//
	
	public String listImages() throws Exception{
		//send LIST command to the server
		printWrite.println("LIST");
		String strSize = buffRead.readLine();
		int arrLength = Integer.parseInt(strSize);//read the size of array containing list
		for(int i = 0; i < arrLength; i++) {
			list = list + buffRead.readLine() + "\n";
			System.out.println(list);
		}
		return list;
	}
	
	public void downloadImage(int ID) throws Exception {
		
		printWrite.println("DOWN " + ID);
		
		//problem fixed
		int fileSize = Integer.parseInt(buffRead.readLine());
		
		String [] arrList = list.split("\n");
		String fileName = arrList[ID-1];//bug, fixed
		
		int indexOfFirstSpace = fileName.indexOf(" ");
		fileName = fileName.substring(indexOfFirstSpace + 1);
		//System.err.println("file name: " + fileName);
		imgFile = new File("data/data/client/" + fileName);//file to download
		
		setImgFile(imgFile);
	
		//problem fixed
		try {
			
			HelperMethods.readBytes(imgFile, dataIN, fileSize);
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			e.printStackTrace();
		}
		
	}
	
	public void uploadImage(int id, String fileName, int fileSize) throws Exception {
		printWrite.println("UP " + id + " " + fileName + " " + fileSize);
		File fileToUpload = new File("data/data/client/"+fileName);
		HelperMethods.writeBytes(fileToUpload, dataOUT);
		
	}
	
	//setters and getters
	
	public int getPort() {
		return port;
	}


	public void setPort(int port) {
		this.port = port;
	}


	public InetAddress getHost() {
		return host;
	}


	public void setHost(InetAddress host) {
		this.host = host;
	}

	
	
	public void setImgFile(File imgFile) {
		this.imgFile = imgFile;
	}
	
	public File getImgFile() {
		return imgFile;
	}
}

