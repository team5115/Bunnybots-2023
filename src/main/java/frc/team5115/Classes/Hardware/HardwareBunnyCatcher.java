package frc.team5115.Classes.Hardware;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DoubleSolenoid.Value;

public class HardwareBunnyCatcher {
    static final double positionConversionFactor = 360; // UNKNOWN
    final CANSparkMax roller;
    final RelativeEncoder encoder;

    private DoubleSolenoid armSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 0);

    public HardwareBunnyCatcher() {
        roller = new CANSparkMax(5, MotorType.kBrushless);
        encoder = roller.getEncoder();
        encoder.setPositionConversionFactor(positionConversionFactor);
    }

    public void armIn(){
        armSolenoid.set(Value.kReverse);
    }

    public void armOut(){
        armSolenoid.set(Value.kForward);
    }

    public double getPosition() {
        return encoder.getPosition();
    }

    /**
     * 
     * @param speed in degrees per second
     */
    public void setSpeed(double speed) {
       roller.set(speed / 360.0);
    }
}
