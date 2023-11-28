package frc.team5115.Classes.Hardware;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class HardwareBunnyCatcher {
    static final double positionConversionFactor = 360; // UNKNOWN
    //final CANSparkMax roller;
    //final RelativeEncoder encoder;

    public HardwareBunnyCatcher() {
        //roller = new CANSparkMax(5, MotorType.kBrushless);
        //encoder = roller.getEncoder();
       // encoder.setPositionConversionFactor(positionConversionFactor);
    }

    public double getPosition() {
        //return encoder.getPosition();
        return 0.0;
    }

    /**
     * 
     * @param speed in degrees per second
     */
    public void setSpeed(double speed) {
       // roller.set(speed / 360.0);
    }
}
