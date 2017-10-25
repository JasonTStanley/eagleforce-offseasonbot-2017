package org.usfirst.frc.team2073.robot.subsys;

import org.usfirst.frc.team2073.robot.RobotMap;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.command.Subsystem;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;

public class GearIntakeSubsystem extends Subsystem {
	private final Victor intakeMotor;
	private final DigitalInput lightSensor;

	public GearIntakeSubsystem() {
		intakeMotor = RobotMap.getGearIntakeMotor();
		lightSensor = RobotMap.getLightSensor();

		// TODO: Extract to constants
		LiveWindow.addActuator("Gear Intake", "Intake Motor", intakeMotor);
		LiveWindow.addSensor("Gear Intake", "Light Sensor", lightSensor);
	}

	@Override
	protected void initDefaultCommand() {
	}
	
	public void gearIn() {
		intakeMotor.set(-1);
	}

	public void gearOut() {
		intakeMotor.set(1);
	}
	
	public void gearHold() {
		intakeMotor.set(.1);
	}

	public void gearStop() {
		intakeMotor.set(0);
	}

	public boolean lightSensor() {
		return lightSensor.get();
	}
}
