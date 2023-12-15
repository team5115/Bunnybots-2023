package frc.team5115.Classes.Hardware;

import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import edu.wpi.first.wpilibj.*;
import edu.wpi.first.wpilibj.DoubleSolenoid;

public class HardwareBunnyCatcher {
    public enum PistonState {
        In, Out, Off
    }

    static final double positionConversionFactor = 360.0 / 5.0; // 360 degrees per 1 catcher rev * 1 catcher rev per 5 motor revs
    protected final CANSparkMax roller;
    protected final RelativeEncoder encoder;
    protected final DoubleSolenoid leftSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 0, 1);
    protected final DoubleSolenoid rightSolenoid = new DoubleSolenoid(PneumaticsModuleType.CTREPCM, 2, 3);

    public HardwareBunnyCatcher() {
        roller = new CANSparkMax(11, MotorType.kBrushless);
        encoder = roller.getEncoder();
        encoder.setPositionConversionFactor(positionConversionFactor);
    }

    public void setPistons(PistonState state) {
        DoubleSolenoid.Value value = convertPistonStateToDoubleSolenoidValue(state);
        leftSolenoid.set(value);
        rightSolenoid.set(value);
    }

    private DoubleSolenoid.Value convertPistonStateToDoubleSolenoidValue(PistonState state) {
        switch (state) {
            case In:
                return DoubleSolenoid.Value.kReverse;
            case Out:
                return DoubleSolenoid.Value.kForward;
            case Off:
            default:
                return DoubleSolenoid.Value.kOff;
        }
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
