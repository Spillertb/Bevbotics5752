
package org.usfirst.frc.team5752.robot;

import edu.wpi.first.wpilibj.AnalogInput;

//import com.bevbotics.Bevbotics2015.RobotMap;

import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.Talon;
import edu.wpi.first.wpilibj.TalonSRX;
//import edu.wpi.first.wpilibj.TalonSRX;
import edu.wpi.first.wpilibj.buttons.Button;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */
public class Robot extends IterativeRobot {
	/**
	 * This function is run when the robot is first started up and should be
	 * used for any initialization code.
	 */

	final int MOTOR_LEFT_1 = 0;
	final int MOTOR_LEFT_2 = 1;

	final int MOTOR_RIGHT_1 = 2;
	final int MOTOR_RIGHT_2 = 3;

	final int WINCH_MOTOR = 5;

	final int XBOX_INPUT = 1;

	final int BUTTON_A = 1;
	final int BUTTON_B = 2;
	final int BUTTON_X = 3;
	final int BUTTON_Y = 4;
	final int BUTTON_LEFT = 5;
	final int BUTTON_RIGHT = 6;
	final int L_YAXIS = 1;

	final int LEFT_TRIGGER = 2;
	final int RIGHT_TRIGGER = 3;

	final int LIMIT_SWITCH_IN = 2;
	final int LIMIT_SWITCH_OUT = 1;

	boolean xPressed = false;
	boolean yPressed = false;
	boolean LEFT_PRESS = false;

	double LEFT_POWER;
	double RIGHT_POWER;

	// use to cap speed at a % speed -> Should be 0.0 to 1.0
	// not final
	double ACCEL_RATE = 1;
	// use to decelerate & accelerate robot so it wont flip

	// final double ACCEL_COEFFICIENT = 0.025;

	final double ACCEL_COEFFICIENT = 0.05;

	final double WINCH_SPEED = 1;

	boolean RELEASED = false;

	double lastLeftPower = 0.0;
	double lastRightPower = 0.0;

	AnalogInput upLimitIn = new AnalogInput(LIMIT_SWITCH_IN);
	AnalogInput upLimitOut = new AnalogInput(LIMIT_SWITCH_OUT);

	Talon leftMotor1 = new Talon(MOTOR_LEFT_1);
	Talon leftMotor2 = new Talon(MOTOR_LEFT_2);
	Talon rightMotor1 = new Talon(MOTOR_RIGHT_1);
	Talon rightMotor2 = new Talon(MOTOR_RIGHT_2);

	TalonSRX winchMotor = new TalonSRX(WINCH_MOTOR); // TalonSRX = newer model
														// of regular Talon,
														// defined differently
	// Talon winchMotor = new Talon(WINCH_MOTOR);

	// one joystick (js), several axis and buttons on one joystick

	Joystick js = new Joystick(XBOX_INPUT);

	Button buttonA = new JoystickButton(js, BUTTON_A);
	Button buttonB = new JoystickButton(js, BUTTON_B);
	Button buttonX = new JoystickButton(js, BUTTON_X);
	Button buttonY = new JoystickButton(js, BUTTON_Y);

	// double lastLeftPower = 0.0;
	public void setLeftPower(double power) {
		if (Math.abs(lastLeftPower - power) > ACCEL_COEFFICIENT) {
			if (Math.signum(power - lastLeftPower) == 1) {
				power = lastLeftPower + ACCEL_COEFFICIENT;
			} else if (Math.signum(power - lastLeftPower) == -1) { // it could
																	// also be 0
				power = lastLeftPower - ACCEL_COEFFICIENT;
			}

		}
		leftMotor1.set(-1 * power * ACCEL_RATE);
		leftMotor2.set(-1 * power * ACCEL_RATE);

		lastLeftPower = power;
	}

	// double lastRightPower = 0.0;
	public void setRightPower(double power) {
		if (Math.abs(lastRightPower - power) > ACCEL_COEFFICIENT) {
			if (Math.signum(power - lastRightPower) == 1) {
				power = lastRightPower + ACCEL_COEFFICIENT;
			} else if (Math.signum(power - lastRightPower) == -1) { // it could
																	// also be 0
				power = lastRightPower - ACCEL_COEFFICIENT;
			}
		}

		rightMotor1.set(power * ACCEL_RATE * .896);
		rightMotor2.set(power * ACCEL_RATE * .896);

		lastRightPower = power;
	}

	public double winchStop() {
		if (RELEASED) {
			// winchMotor.set(-.05);
			RELEASED = false;
			return -.05;
		} else {
			// winchMotor.set(0);
			return 0;

		}
	}

	public void robotInit() {
	}

	/**
	 * This function is called periodically during autonomous
	 */
	public void autonomousPeriodic() {

	}

	/**
	 * This function is called periodically during operator control
	 */
	public void teleopPeriodic() {

		// boolean A_value = js.getRawButton(BUTTON_A);

		/*
		 * UP if (js.getRawButton(BUTTON_X)){ winchMotor.set(WINCH_SPEED); }
		 * else { winchMotor.set(0); } DOWN if (js.getRawButton(BUTTON_Y)){
		 * winchMotor.set(-1* WINCH_SPEED); } else { winchMotor.set(0); }
		 */
		// up

		/*
		 * if (upLimitIn.getVoltage() < 3) { // if limit switch is NOT pressed
		 * winchMotor.set(js.getRawAxis(L_YAXIS)); } else if
		 * (upLimitIn.getVoltage() > 3 && js.getRawAxis(L_YAXIS) < 0) {
		 * winchMotor.set(js.getRawAxis(L_YAXIS)); } else { winchMotor.set(0); }
		 */

		// boolean x_release
		// boolean y_release

		// B = down
		// X = up

		if (upLimitIn.getVoltage() < 3 && js.getRawButton(BUTTON_X)) {
			winchMotor.set(1);
		} else if (js.getRawButton(BUTTON_B)) { // limit switch does not matter
												// when going down
			winchMotor.set(-1);
			RELEASED = false;
		} else if (upLimitIn.getVoltage() > 3 && !RELEASED) {
			// RELEASED = true;
			// winchMotor.set(winchStop());
			winchMotor.set(-.05);
			RELEASED = true;
		} else {
			// winchMotor.set(0);
			winchMotor.set(winchStop());
		}

		if (js.getRawButton(BUTTON_RIGHT)) {
			setRightPower(js.getRawAxis(3));
		} else {
			setRightPower(-1 * js.getRawAxis(3));
		}

		if (js.getRawButton(BUTTON_LEFT)) {
			setLeftPower(js.getRawAxis(2));
		} else {
			setLeftPower(-1 * js.getRawAxis(2));
		}

	}

	/**
	 * This function is called periodically during test mode
	 */
	public void testPeriodic() {

	}

}
