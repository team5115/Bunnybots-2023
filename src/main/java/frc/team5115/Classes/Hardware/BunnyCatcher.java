package frc.team5115.Classes.Hardware;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.PIDController;
import frc.team5115.Classes.Acessory.Angle;

public class BunnyCatcher {
    static final double positionConversionFactor = 360; // UNKNOWN
    static final double kP = 1.0/360.0;
    static final double MaxSpeed = 1; // degrees per second

    final PIDController pidController;
    final CANSparkMax roller;
    final RelativeEncoder encoder;
    final Angle currentAngle;

    public BunnyCatcher() {
        pidController = new PIDController(1.0/360.0, 0, 0);
        roller = new CANSparkMax(100, MotorType.kBrushless);
        encoder = roller.getEncoder();
        encoder.setPositionConversionFactor(positionConversionFactor);
        currentAngle = new Angle(encoder.getPosition());
    }

    public void turnTowardsAngle(double setpoint, int direction) {
        Angle goal = new Angle(setpoint);
        double delta = currentAngle.getDelta(goal, direction); // change depending on how we want to turn
        double pid = pidController.calculate(delta);
        double speed = Math.min(Math.abs(pid), MaxSpeed) * Math.signum(pid);
        roller.set(speed);
    }

    public void updateAngle() {
        currentAngle.angle = encoder.getPosition();
    }
}
