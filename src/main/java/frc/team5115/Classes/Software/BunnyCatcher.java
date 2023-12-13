package frc.team5115.Classes.Software;

import edu.wpi.first.math.controller.PIDController;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team5115.Classes.Accessory.Angle;
import frc.team5115.Classes.Hardware.HardwareBunnyCatcher;
import frc.team5115.Classes.Hardware.HardwareBunnyCatcher.PistonState;

public class BunnyCatcher extends SubsystemBase{
    // TODO tune bunny catcher pid values
    static final double kP = 0.1;
    static final double kI = 0.0;
    static final double kD = 0.0;
    static final double MaxSpeed = 1; // degrees per second

    private final HardwareBunnyCatcher hardwareBunnyCatcher;
    final PIDController pidController;
    final Angle currentAngle;

    private boolean isDeployed;

    public BunnyCatcher(HardwareBunnyCatcher hardwareBunnyCatcher) {
        this.hardwareBunnyCatcher = hardwareBunnyCatcher;
        pidController = new PIDController(kP, kI, kD);
        currentAngle = new Angle(hardwareBunnyCatcher.getPosition());
    }

    public void stop() {
        hardwareBunnyCatcher.setSpeed(0);
    }
    
    public boolean turnTowardsAngle(double setpoint, int direction) {
        return turnTowardsAngle(new Angle(setpoint), direction);
    }

    /**
    * Get the speed for a motor to reach a desired angle
    * 
    * @param setpoint the angle that needs to be reached by the motor
    * @param direction the direction to move: -1 always moves backwards, 0 finds the closest path, and 1 always moves forwards
    * @return if the pid loop reports being at the setpoint
    */
    public boolean turnTowardsAngle(Angle setpoint, int direction) {
        final double delta = currentAngle.getDelta(setpoint, direction);
        final double current = currentAngle.getDegrees(0);

        final double pid = pidController.calculate(current, current + delta);
        final double speed = Math.min(Math.abs(pid), MaxSpeed) * Math.signum(delta);

        hardwareBunnyCatcher.setSpeed(speed);
        return pidController.atSetpoint();
    }

    /**
     * just spin the motor at speed -1 to 1
     * @param speed
     */
    public void spin(double speed) {
        hardwareBunnyCatcher.setSpeed(speed);
    }

    public void updateAngle() {
        currentAngle.angle = hardwareBunnyCatcher.getPosition();
    }

    public Angle getAngle() {
        updateAngle();
        return currentAngle;
    }

    public void deploy() {
        isDeployed = true;
        hardwareBunnyCatcher.setPistons(PistonState.In);
    }

    public void stow() {
        isDeployed = false;
        hardwareBunnyCatcher.setPistons(PistonState.Out);
    }

    public boolean isDeployed() {
        return isDeployed;
    }
}
