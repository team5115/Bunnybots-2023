package frc.team5115.Classes.Software;

import com.revrobotics.CANSparkMax;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team5115.Classes.Accessory.Angle;
import frc.team5115.Classes.Hardware.HardwareArm;
import edu.wpi.first.math.controller.PIDController;

/**
 * The arm subsystem. Provides methods for controlling and getting information about the arm.
 */
public class Arm extends SubsystemBase{
    private static final double MIN_DEGREES = -180.0;
    private static final double TURN_PID_TOLERANCE = 0.0;
    private static final double TURN_PID_KP = 0.002;
    private static final double TURN_PID_KI = 0.0;
    private static final double TURN_PID_KD = 0.0;
    
    private final HardwareArm hardwareArm;
    private Angle setpoint;

    private PIDController turnController = new PIDController(TURN_PID_KP, TURN_PID_KI, TURN_PID_KD);
    private boolean isDeployed;

    public Arm(HardwareArm hardwareArm){
        this.hardwareArm = hardwareArm;
        turnController.setTolerance(TURN_PID_TOLERANCE);
        setpoint = new Angle(100); // ! The starting setpoint for when the robot turns on
    }

    public Angle getSetpoint() {
        return setpoint;
    }

    /**
     * Changes the setpoint, but doesn't actually change the reference so that it doesn't become linked to the parameter passed in
     * @param newSetpoint the new setpoint as an angle, but might as well be a double
     */
    public void setSetpoint(Angle newSetpoint) {
        setpoint.angle = newSetpoint.angle;
    }

    public void turnUp() {
        setpoint.angle += 9*0.02;
    }

    public void turnDown() {
        setpoint.angle -= 9*0.02;
    }

    public void disableBrake(){
        hardwareArm.disableBrake();
    }

    public void enableBrake(){
        hardwareArm.enableBrake();
    }

    /**
     * Update the pid controller to try to approach the setpoint angle by changing hardware arm's speed
     * @return true if the arm is at the setpoint
     */
    public boolean updateController(){
        final double pidOutput = turnController.calculate(getAngle().getDegrees(MIN_DEGREES), setpoint.getDegrees(MIN_DEGREES));
        System.out.println("Setpoint: " + setpoint.getDegrees(MIN_DEGREES) + " current angle: "+ getAngle().getDegrees(MIN_DEGREES) + " pid: " + pidOutput);
        
        boolean atSetpoint = turnController.atSetpoint();
        if (!atSetpoint) hardwareArm.setTurn(pidOutput);
        return atSetpoint;
    }

    public void turnRaw(double speed) {
        hardwareArm.turnRaw(speed);
    }

    public boolean atSetpoint() {
        return turnController.atSetpoint();
    }

    public boolean getFault(CANSparkMax.FaultID f){
        return hardwareArm.getFault(f);
    }

    public void stop(){
        hardwareArm.stop();
    }

    public Angle getAngle() {
        return hardwareArm.getArmAngle();
    }

    public void spin(double speed) {
        hardwareArm.spinGrabbers(speed);
    }

    public void deploy() {
        isDeployed = true;
        setpoint.angle = 5;
    }

    public void stow() {
        isDeployed = false;
        setpoint.angle = 90;
    }

    public boolean isDeployed() {
        return isDeployed;
    }
}
