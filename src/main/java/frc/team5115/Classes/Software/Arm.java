package frc.team5115.Classes.Software;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team5115.Classes.Hardware.HardwareArm;
import edu.wpi.first.math.controller.PIDController;

/**
 * The arm subsystem. Provides methods for controlling and getting information about the arm.
 */
public class Arm extends SubsystemBase{
    private static final double TURN_PID_TOLERANCE = 0;
    private static final double TURN_PID_KP = 0.04;
    private static final double TURN_PID_KI = 0.0;
    private static final double TURN_PID_KD = 0.0004;
    
    private HardwareArm hardwareArm;
    private double angle = -90;

    private PIDController turnController = new PIDController(TURN_PID_KP, TURN_PID_KI, TURN_PID_KD);

	/**
	 * `Arm` constructor.
	 * @param hardwareArm - The arm hardware subsystem to be used
	 * @param hardwareIntake - The intake hardware subsystem to be used
	 */
    public Arm(HardwareArm hardwareArm){
        this.hardwareArm = hardwareArm;
        turnController.setTolerance(TURN_PID_TOLERANCE);
    }

    public void turnSetAngle(double angle){
        this.angle = angle;
    }

    public void turnUp() {
        angle += 9*0.02;
    }

    public void turnDown() {
        angle -= 9*0.02;
    }

    /**
	 * Disable brake mode on the arm's motors.
	 */
    public void disableBrake(){
        hardwareArm.disableBrake();
    }

	/**
	 * Enable brake mode on the arm's motors.
	 */
    public void enableBrake(){
        hardwareArm.enableBrake();
    }

    public void updateController(){
        // final double delta = angle - intake.getArmDeg();
        final double pidOutput = turnController.calculate(hardwareArm.getArmDeg(), angle);
        
        if (!turnController.atSetpoint()) {
            hardwareArm.setTurn(pidOutput);
        }
    }

    public boolean getFault(CANSparkMax.FaultID f){
        return hardwareArm.getFault(f);
    }

    public void stop(){
        hardwareArm.stop();
    }

    public double getAngle() {
        return hardwareArm.getArmDeg();
    }

    public void spinIn() {
        hardwareArm.spinGrabbers(+0.7);
    }

    public void spinOut() {
        hardwareArm.spinGrabbers(-0.7);
    }

    public void spinStop() {
        hardwareArm.spinGrabbers(+0.09);
    }
}
