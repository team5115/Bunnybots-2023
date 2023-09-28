package frc.team5115.Classes.Software;

import edu.wpi.first.math.controller.PIDController;
import frc.team5115.Classes.Accessory.Angle;
import frc.team5115.Classes.Hardware.HardwareBunnyCatcher;

public class BunnyCatcher {
    static final double kP = 1;
    static final double MaxSpeed = 30; // degrees per second

    final HardwareBunnyCatcher hardwareBunnyCatcher;
    final PIDController pidController;
    final Angle currentAngle;

    public BunnyCatcher(HardwareBunnyCatcher hardwareBunnyCatcher) {
        this.hardwareBunnyCatcher = hardwareBunnyCatcher;
        pidController = new PIDController(kP, 0, 0);
        currentAngle = new Angle(hardwareBunnyCatcher.getPosition());
    }

    public void stop() {
        hardwareBunnyCatcher.setSpeed(0);
    }
    
    public void turnTowardsAngle(double setpoint, int direction) {
        final Angle goal = new Angle(setpoint);
        final double delta = currentAngle.getDelta(goal, direction);
        final double current = currentAngle.getDegrees(0);

        final double pid = pidController.calculate(current, current + delta);
        final double speed = Math.min(Math.abs(pid), MaxSpeed) * Math.signum(delta);

        hardwareBunnyCatcher.setSpeed(speed);
        // System.out.println("Current: " + current + " | Delta: " + delta + " | Current+Delta: " + (current + delta));
    }

    public void updateAngle() {
        currentAngle.angle = hardwareBunnyCatcher.getPosition();
    }

    public Angle getAngle() {
        return currentAngle;
    }
}
