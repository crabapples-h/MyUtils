package com.MissX.utils.test;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Scanner;

public class TestMain3 {
	
	public static void main(String [] args) throws IOException, InterruptedException {
		final Socket socket = new Socket("192.168.0.110", 8889);
		System.out.println("客户端启动");
		Thread sendData = new Thread(new Runnable() {
			
			public void run() {
				sendData(socket);
			}
		});
		Thread getData = new Thread(new Runnable() {
			public void run() {
				getData(socket);
			}
		});
		sendData.start();
		getData.start();
		
	}

	private static void getData(Socket socket)   {
		try {
			while(true) {
				InputStreamReader isr = new InputStreamReader(socket.getInputStream());
				int i = 0;
				String str = "";
				while((i = isr.read())!=-1) {
					char c = (char)i;
					str += c;
					if(str.indexOf("-1")!=-1) {
						break;
					}
				}
				System.out.println("来自服务器的消息:"+str.substring(0,str.length()-2));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private static void sendData(Socket socket){
		try {
			Scanner scan = new Scanner(System.in);
			while(true) {
				OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
				String str1 = "";
				while(true) {
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
}