package com.eagleforce.robot.service;

import edu.wpi.first.wpilibj.Encoder;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

public class DrivetrainService {
	

	private Victor lMotor = new Victor(0);
	private Victor lMotor2 = new Victor(1);
	private Victor rMotor = new Victor(2);
	private Victor rMotor2 = new Victor(3);
	private Encoder lEnc = new Encoder(3, 4);
	private Encoder rEnc = new Encoder(1, 2);
	private Solenoid sol1 = new Solenoid(0);
	private Solenoid sol2 = new Solenoid(1);

	double preTurn;

	public DrivetrainService() {
		lEnc.setDistancePerPulse(.01227);
		rEnc.setDistancePerPulse(.01227);
		
//		MotionProfileGenerationService mps = new MotionProfileGenerationService();
//		MotionProfileConfiguration conf1;

	}

	public double turnSense(double Ptart) {
		double sTurn = SmartDashboard.getNumber("Sense", .7) * Ptart * Ptart * Ptart
				+ Ptart * (1 - SmartDashboard.getNumber("Sense", .7));
		return sTurn;
	}

	public double inverse(double Start) {
		double inv = (Start - preTurn) * SmartDashboard.getNumber("Inverse", .2) + Start;
		return inv;
	}

	public void pTurn(double turn) {
		rMotor.set(-turn);
		lMotor.set(-turn);
		rMotor2.set(-turn);
		lMotor2.set(-turn);
	}

	public void move(double speed, double turn) {
		rMotor.set(-(inverse(speed) - (inverse(speed) * turnSense(turn))));
		lMotor.set(inverse(speed) + (inverse(speed) * turnSense(turn)));
		rMotor2.set(-(inverse(speed) - (inverse(speed) * turnSense(turn))));
		lMotor2.set(inverse(speed) + (inverse(speed) * turnSense(turn)));
	}

	public void shiftHighGear() {
		sol1.set(true);
		sol2.set(true);
	}

	public void shiftLowGear() {
		sol1.set(false);
		sol2.set(true);
	}

}
