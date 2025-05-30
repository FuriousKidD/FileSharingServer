/**
 * 
 */
package com.client;

import java.io.File;
import java.io.FileInputStream;
import java.net.InetAddress;
import com.server.ClientHandler;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * 
 */
public class ClientGUI extends VBox {
	
	private Button btnConnect = null;
	private Button btnPull = null;
	private Button btnUpload = null;
	private Button btnDownload = null;//will go into the HBox
	private Button btnDisplay = null;
	
	private Image image = null;
	private ImageView displayImage = null;
	
	private Label lblFileList = null;
	private Label lblFileID = null;//will go into the HBox
	private Label lblResponse = null;
	
	private TextArea txtAreaFileList = null;
	private TextArea txtAreaServerResponses = null;
	
	private TextField txtFieldFileID = null;//will go into the HBox
	
	private HBox hDownload = null;
	private Client client = null;
	
	private int id = 0;
	
	public ClientGUI(double topPad, double rightPad, double bottomPad, double leftPad) {

		setPadding(new Insets(topPad, rightPad, bottomPad, leftPad));
		setSpacing(5);
		btnConnect = new Button("Connect");
		btnPull = new Button("PULL");
		btnUpload= new Button("UPLOAD");
		btnDownload = new Button("DOWNLOAD");
		btnDisplay = new Button("DISPLAY IMAGE");
		
		
		lblFileList = new Label("File List: ");
		lblFileID = new Label("Enter File ID TO Retrieve: ");
		lblResponse = new Label("Responses From Server: ");
		
		txtAreaFileList = new TextArea("Some Default string for the \n of testing");
		txtAreaServerResponses = new TextArea("Some Default string for the \n of testing");
		
		txtFieldFileID = new TextField("Enter ID");
		
		hDownload = new HBox();
		hDownload.setSpacing(5);
		hDownload.getChildren().addAll(lblFileID,txtFieldFileID,btnDownload);
		
		
		try {
			client = new Client(5432, InetAddress.getLocalHost());
			txtAreaServerResponses.appendText("\n Server waiting for connection on port: " + client.getPort());

		} 
		catch (Exception e) {
			e.printStackTrace();
		}//end catch
		
		
		/*
		 * Adding the connect, pull buttons
		 * and the File List label
		 * and the TextArea according to the vertical pane 
		 */
		getChildren().addAll(btnConnect,btnPull,lblFileList,txtAreaFileList,
							hDownload,btnUpload,lblResponse,txtAreaServerResponses,
							btnDisplay);
	}
	
	/**
	 * Connect the client to a server
	 */
	public void connect() {
		btnConnect.setOnAction(e->{

			try {
				client.connectToServer();
				txtAreaServerResponses.appendText("\n Successfuly connected!");
				txtAreaServerResponses.appendText("\n" + client.getHost() + " Connected to port: " + client.getPort());				
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} 
		});
	}
	/**
	 * @param line 
	 */
	public void showList() {
		btnPull.setOnAction(e->{
			try {
				
				txtAreaServerResponses.appendText("\n Server: sending list of files");
				
				txtAreaFileList.setText("List of files: \n");
				txtAreaFileList.appendText(client.listImages());
				
				
				
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});
	}//end showList
	
	/**
	 * 
	 * @param ID
	 */
	public void download() {
		btnDownload.setOnAction(e->{
			
			txtAreaServerResponses.appendText("\n Server: Uploading file to client... ");	
			txtAreaServerResponses.appendText("\n Client: downloading file from server...");
			
			try {
				int id = Integer.parseInt(txtFieldFileID.getText());
				
				client.downloadImage(id);
				
				image = new Image(new FileInputStream(client.getImgFile()));
				displayImage = new ImageView(image);
				displayImage.setFitHeight(150);
				displayImage.setFitWidth(150);
				displayImage.setVisible(false);//hide the image
				getChildren().add(displayImage);
				
			} catch (Exception e2) {
				System.err.println(e2.getMessage());
				e2.printStackTrace();
			}
			
		});
	}//end download
	
	public void upload(Stage stage) {
		btnUpload.setOnAction(e->{
			
			FileChooser chooseFile = new FileChooser();
			chooseFile.setTitle("Image to upload");
			
			File selectedFile = chooseFile.showOpenDialog(null);
			if(selectedFile.exists()) {
				
				ClientHandler.id+=1;
				int id = ClientHandler.id;
				
				int fileSize = (int) selectedFile.length();
				String fileName = selectedFile.getName();
				
				txtAreaServerResponses.appendText("Client: uploading file to server..");
				
				//
				try {
					client.uploadImage(id, fileName, fileSize);
					stage.setTitle("Choose File");
					stage.show();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					System.err.println(e1.getMessage());
					e1.printStackTrace();
				}
				
			}
			
		});
	}//end upload
	
	public void displayImage() {
		btnDisplay.setOnAction(e->{
			
			txtAreaServerResponses.appendText("client: displaying file");
			
			if(displayImage != null) {
				displayImage.setVisible(true);
			}
		});
	}
}
