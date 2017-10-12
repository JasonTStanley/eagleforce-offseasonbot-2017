package com.eagleforce.robot.service;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

import com.eagleforce.robot.model.CameraMessage;

public class CameraService {
	private CameraMessage lastMessage = new CameraMessage();
 	private CameraMessageParsingService parser = new CameraMessageParsingService();
	private CameraMessageReciever camReciever = new CameraMessageReciever();
	private static CameraService instance = null;
	
	public static CameraService getInstance(){
		if(instance == null){
			instance = new CameraService();
		}
		return instance;
	}
	
	private CameraService() {
//		only allow one thread to be created
		new Thread(camReciever).start();

	}
 	
	public CameraMessage lastCameraMessage(){
		return lastMessage;
	}
	
	private class CameraMessageReciever implements Runnable{

		@Override
		public void run() {
			while(!Thread.interrupted()){ 
				try{
					String message = "";
					DatagramSocket socket = new DatagramSocket(2073);
					byte[] receiveData = new byte[24];
					DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
					socket.receive(receivePacket);
					message = new String(receivePacket.getData());
					// get message from packet
					CameraMessage msg = parser.parseJson(message);
					socket.close();
					
					lastMessage = msg;
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		
		}
		
	}
	

}