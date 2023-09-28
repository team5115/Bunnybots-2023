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

public class HardwareArm extends SubsystemBase{
    private final CANSparkMax intakeTurn;
    private final RelativeEncoder TurningEncoder;
    private final TalonSRX grabLeft;
    private final TalonSRX grabRight;
    
    private final double Ks = 0.13;
    private final double Kv = 4.5;
    private final double Ka = 0.1113;
    private final double Kg = 0.39;
    public boolean FF = true;
    private final ArmFeedforward arm = new ArmFeedforward(Ks, Kg, Kv, Ka); // Rad Calibrated
    //private double encoderConstant = 1/49;
    private double startingTurnValue = Units.degreesToRadians(-96); //Rads

    public HardwareArm(){
        grabLeft = new TalonSRX(120);
        grabRight = new TalonSRX(121);

        intakeTurn = new CANSparkMax(7, MotorType.kBrushless);  
        intakeTurn.setIdleMode(IdleMode.kBrake);
        intakeTurn.setSmartCurrentLimit(80, 80);

        TurningEncoder = intakeTurn.getEncoder(SparkMaxRelativeEncoder.Type.kHallSensor, 42);
        TurningEncoder.setPositionConversionFactor(1);
        TurningEncoder.setVelocityConversionFactor(1);
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
        if(FF){
            intakeTurn.setVoltage(Math.max(arm.calculate((getArmRad()), 1.5*speed), -10));
        }
        else{
            if(speed>.37){
                speed = 0.37;
            }
            else if(speed<-0.2){
                speed = -0.2;
            }
            intakeTurn.set(speed);
        }
    }

    public void stop(){
        setTurn(0);
    }
    
    public double getTurnCurrent(){
        return intakeTurn.getOutputCurrent();
    }
    
    
    public double getTurnEncoder(){
        return (TurningEncoder.getPosition());
    }

    public double getTurnVelocity(){
        return (TurningEncoder.getVelocity());
    }

    public boolean getFault(CANSparkMax.FaultID f){
        return intakeTurn.getFault(f);
    }

    public void zeroEncoders(){
        TurningEncoder.setPosition(0);
    }

    public void setEncoders(double Length, double angle){
        TurningEncoder.setPosition(angle/(360.0 / (48.0 * 49.0 / 10.0))); // Set the angle 
        // TurningEncoder.setPosition(Units.radiansToDegrees(startingTurnValue)/(360.0 / (48.0 * 49.0 / 10.0)));
    }

    /** 
     * Returns the angle of the turning arm
     * @return the angle the arm turned
     */

    public double getArmDeg(){
        return getTurnEncoder() * (360.0 / (48.0 * 49.0 / 10.0));
    }

    public double getArmRad(){
        //return Math.toRadians(getArmDeg());
        return Math.toRadians(getArmDeg() + startingTurnValue);
    }

    public void disableBrake(){
        //intakeTurn.setIdleMode(IdleMode.kCoast);
    }

    public void enableBrake(){
        intakeTurn.setIdleMode(IdleMode.kBrake);
    }
}
