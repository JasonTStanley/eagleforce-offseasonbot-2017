package com.eagleforce.robot.controller;

import java.util.List;

import com.ctre.CANTalon.TrajectoryPoint;
import com.eagleforce.robot.service.DriverStationService;
import com.eagleforce.robot.service.MotionProfileGearIntakeService;

import edu.wpi.first.wpilibj.RobotState;
import edu.wpi.first.wpilibj.Timer;

public class GearIntakeController {

	private DriverStationService dvrSvc = new DriverStationService();
	private MotionProfileGearIntakeService gearSvc = new MotionProfileGearIntakeService();
	private boolean filledPoints = false;

	private Thread gearIntakeControl = new Thread() {
		@Override
		public void run() {
			while (true) {
				if (RobotState.isAutonomous()) {
					// put auto here
				} else if (RobotState.isOperatorControl()) {
				
					if (dvrSvc.gearIntakeButton()) {
						gearSvc.gearIn();
					} else if (dvrSvc.gearPlaceButton()) {
						gearSvc.gearOut();
					} else if (gearSvc.lightSensor()) {
						gearSvc.gearHold();
					} else {
						gearSvc.gearStop();
					}
					if (gearSvc.isZero()) {
						gearSvc.resetEnc();
					}
//					if (dvrSvc.ballIntakeButton()) {
//							gearSvc.stopMotionProfile();
//							gearSvc.pushPoints(gearSvc.upToDownTpList);
//							
//							}
//						}
					
					// Acts same as an if/else (first method to return true wins)
//					gearSvc.printTalonInfo();
					System.out.println(dvrSvc.gearPositionDPad());
					if (tryMotionProfiling(90, 0, gearSvc.upToDownTpList)
							|| tryMotionProfiling(90, 45, gearSvc.placeToDownTpList)
							|| tryMotionProfiling(45, 0, gearSvc.upToPlaceTpList)
							|| tryMotionProfiling(45, 90, gearSvc.downToPlaceTpList)
							|| tryMotionProfiling(0, 45, gearSvc.placeToUpTpList)
							|| tryMotionProfiling(0, 90, gearSvc.downToUpTpList)) {
						// Motion profiling works
						
					}
					else if (gearSvc.getApproxAngle() != 0 && gearSvc.getApproxAngle() != 45
							&& gearSvc.getApproxAngle() != 90) {
						System.out.println("ruh roh shaggy, we've been hit");
						gearSvc.resetGearIntake();
					} else {
//						System.out.println("GearIntake is broke af dude");
					}
					// TODO: Finalize motion profiles and configure selector
					// here
					
				}
				Timer.delay(.005);
			}

		}
		
		
	};

	private boolean tryMotionProfiling(int dvrAngle, int gearAngle, List<TrajectoryPoint> list) {
//		System.out.println("trying");
		if (dvrSvc.gearPositionDPad() == dvrAngle && gearSvc.getApproxAngle() == gearAngle) {
			if (!filledPoints) {
				System.out.println("filling");
				gearSvc.pushPoints(list);
				filledPoints = true;
			} else{
				System.out.println("proccessing");
				gearSvc.processPoints();
			}
			return true;
		}else {
			gearSvc.stopMotionProfile();
			filledPoints = false;
		}
		return false;
	}

	public void init() {
		gearSvc.resetEnc();
		gearSvc.motionProfileInit();
		gearIntakeControl.start();
	}

}
