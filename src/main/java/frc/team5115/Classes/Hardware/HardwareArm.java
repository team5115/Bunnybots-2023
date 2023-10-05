package frc.team5115.Classes.Hardware;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxRelativeEncoder;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.util.Units;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team5115.Classes.Accessory.Angle;
import frc.team5115.Classes.Accessory.I2CHandler;

public class HardwareArm extends SubsystemBase{
    private final CANSparkMax intakeTurn;
    private final TalonSRX grabLeft;
    private final TalonSRX grabRight;
    
    private final NAVx navx;
    private final I2CHandler i2c;

    private final double Ks = 0.13;
    private final double Kv = 4.5;
    private final double Ka = 0.1113;
    private final double Kg = 0.39;
    private final ArmFeedforward arm = new ArmFeedforward(Ks, Kg, Kv, Ka); // Rad Calibrated

    public HardwareArm(NAVx navx, I2CHandler i2c){
        grabLeft = new TalonSRX(2);
        grabRight = new TalonSRX(4);

        this.navx = navx;
        this.i2c = i2c;

        intakeTurn = new CANSparkMax(5, MotorType.kBrushless);  
        intakeTurn.setIdleMode(IdleMode.kBrake);
        intakeTurn.setSmartCurrentLimit(80, 80);
    }

    public void spinGrabbers(double speedNormalized) {
        grabLeft.set(TalonSRXControlMode.PercentOutput, speedNormalized);
        grabRight.set(TalonSRXControlMode.PercentOutput, -speedNormalized);
    }

    public void setTurn(double speed){
        //TURN ONLY IF THE ARMS ARE WITHDREW

        if(speed != speed) {
            speed = 0;
        }
        intakeTurn.setVoltage(Math.max(arm.calculate((getArmRad()), 1.5*speed), -10));
    }

    public void stop(){
        setTurn(0);
    }
    
    public double getTurnCurrent(){
        return intakeTurn.getOutputCurrent();
    }
    
    public boolean getFault(CANSparkMax.FaultID f){
        return intakeTurn.getFault(f);
    }

    /** 
     * Returns the angle of the turning arm
     * @return the angle the arm turned
     */

    public double getArmDeg(){
        // return getTurnEncoder() * (360.0 / (48.0 * 49.0 / 10.0));
        return angleFromSensors();
    }

    /**
     * This uses the navx and the bno to get the arm degree instead of motor encoder
     * @return the angle the arm is at in degrees relative to the horizontal
     */
    private double angleFromSensors() {
        final double navxPitch = navx.getPitchDeg();
        final double bnoPitch = i2c.getPitch();
        return Angle.rollover(bnoPitch - navxPitch, -180);
    }

    public double getArmRad(){
        return Math.toRadians(getArmDeg());
    }

    public void disableBrake(){
        //intakeTurn.setIdleMode(IdleMode.kCoast);
    }

    public void enableBrake(){
        intakeTurn.setIdleMode(IdleMode.kBrake);
    }
}
