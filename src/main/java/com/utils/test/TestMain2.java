package com.utils.test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class TestMain2  {
	private static final Scanner scan = new Scanner(System.in);
	public static void main(String [] args) throws IOException {
		@SuppressWarnings("resource")
		ServerSocket server = new ServerSocket(8889);
		System.out.println("服务端启动完成");
		while(true) {
			Socket client = server.accept();
			createSocket(client);
		}
	}
	
	private static void createSocket(final Socket client) {
		Thread getData = new Thread(new Runnable() {
			public void run() {
				getData(client);
			}
		});
		getData.start();
		getData.setName("getDate()");
	}
	
	private static void sendData(Socket socket) {
		try {
			OutputStreamWriter osw = null;
			while(!socket.isClosed()&&socket.isConnected()) {
				osw = new OutputStreamWriter(socket.getOutputStream());
				String str1 = "";
				while(!socket.isClosed()&&socket.isConnected()) {
					str1 = scan.next();
					System.err.println(str1);
					osw.write(str1);
					osw.write("-1");
					osw.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	private static void getData(final Socket socket){
		Thread sendData = new Thread(new Runnable() {
			public void run() {
				sendData(socket);
			}
		});
		sendData.start();
		sendData.setName("sendData()");
		try {
			InputStreamReader isr = null;
			while(!socket.isClosed()&&socket.isConnected()) {
				isr = new InputStreamReader(socket.getInputStream());
				int i = 0;
				String str = "";
				while((i = isr.read())!=-1) {
					char c = (char)i;
					str += c;
					if(str.indexOf("-1")!=-1) {
						break;
					}
				}
				System.out.println("来自客户端的消息:"+str.substring(0,str.length()-2));
			}
			isr.close();
		} catch (RuntimeException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			sendData.destroy();
		}	
	}
}