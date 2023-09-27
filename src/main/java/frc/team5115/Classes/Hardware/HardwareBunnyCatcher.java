package frc.team5115.Classes.Hardware;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class HardwareBunnyCatcher {
    static final double positionConversionFactor = 360; // UNKNOWN
    final CANSparkMax roller;
    final RelativeEncoder encoder;

    public HardwareBunnyCatcher() {
        roller = new CANSparkMax(100, MotorType.kBrushless);
        encoder = roller.getEncoder();
        encoder.setPositionConversionFactor(positionConversionFactor);
    }

    public double getPosition() {
        return encoder.getPosition();
    }

    public void setSpeed(double speed) {
        roller.set(speed);
    }
}
