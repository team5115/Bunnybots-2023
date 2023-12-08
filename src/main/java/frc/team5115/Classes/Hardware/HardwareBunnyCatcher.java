package frc.team5115.Classes.Hardware;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class HardwareBunnyCatcher {
    static final double positionConversionFactor = 360.0 / 5.0; // 360 degrees per 1 catcher rev * 1 catcher rev per 5 motor revs
    protected final CANSparkMax roller;
    protected final RelativeEncoder encoder;

    public HardwareBunnyCatcher() {
        roller = new CANSparkMax(5, MotorType.kBrushless);
        encoder = roller.getEncoder();
        encoder.setPositionConversionFactor(positionConversionFactor);
    }

    public double getPosition() {
        return encoder.getPosition();
    }

    /**
     * sets the speed of the roller motor based on -1 to 1 range
     * @param speed is a "speed"between -1 and 1
     */
    public void setSpeed(double speed) {
       roller.set(speed);
    }
}
