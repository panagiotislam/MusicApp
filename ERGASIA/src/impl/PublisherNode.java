package impl;
import java.io.*;
import java.net.Socket;



public class PublisherNode {
	private int id;
	private String host;
	private String topic;
	private int port;
	private Value data;
	private PrintWriter outputStream;
	private Socket clientSocket;



	public PublisherNode(int id, String topic, String host, int port, Value data) {
		this.id = id;
		this.topic = topic;

		this.host = host;
		this.port = port;
		this.data = data;
	}


	public void connect() {
		System.out.println("Starting Publisher on Thread: " + Thread.currentThread().getName() + " " + Thread.currentThread().getId());
		try {
			this.clientSocket = new Socket(host, port);
			this.outputStream = new PrintWriter(clientSocket.getOutputStream(), true);
			registerToBroker(topic, id+"");
			push(topic, data);
			try {
				Thread.sleep(1500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} catch (IOException  e) {
			e.printStackTrace();
		}finally {
			disconnect();
		}
	}


	public void disconnect() {
		try {
			outputStream.close();
			clientSocket.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void registerToBroker(String topic, String publisherId) {
		outputStream.println("Connection Request");
		outputStream.println(topic + "," + id + "," + 0);
	}


	public void push(String artist_name, Value value) throws IOException {
		outputStream.println("[Publisher " + id + "] artistName: " + artist_name + ", Value: " + value);
	}



}








