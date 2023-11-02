package frc.team5115.Classes.Hardware;

import com.ctre.phoenix.motorcontrol.TalonSRXControlMode;
import com.ctre.phoenix.motorcontrol.can.TalonSRX;
import com.revrobotics.CANSparkMax;
import com.revrobotics.CANSparkMax.IdleMode;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.wpilibj2.command.SubsystemBase;
import frc.team5115.Classes.Accessory.Angle;
import frc.team5115.Classes.Accessory.I2CHandler;

public class HardwareArm extends SubsystemBase{
    private final CANSparkMax armTurn;
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

        armTurn = new CANSparkMax(50, MotorType.kBrushless);  
        armTurn.setIdleMode(IdleMode.kBrake);
        armTurn.setSmartCurrentLimit(80, 80);
    }

    public void spinGrabbers(double speedNormalized) {
        grabLeft.set(TalonSRXControlMode.PercentOutput, speedNormalized);
        grabRight.set(TalonSRXControlMode.PercentOutput, -speedNormalized);
    }

    public void setTurn(double speed){
        if(speed != speed) {
            speed = 0;
        }
        armTurn.setVoltage(Math.max(arm.calculate(getArmAngle().getRadians(-Math.PI), 1.5*speed), -10));
    }

    public void stop(){
        setTurn(0);
    }
    
    public double getTurnCurrent(){
        return armTurn.getOutputCurrent();
    }
    
    public boolean getFault(CANSparkMax.FaultID f){
        return armTurn.getFault(f);
    }

    
    /**
     * This uses the navx and the bno to get the arm degree instead of motor encoder
     * @return the angle the arm is at relative to the horizontal
     */
    public Angle getArmAngle(){
        final double navxPitch = navx.getPitchDeg();
        final double bnoPitch = i2c.getPitch();
        return new Angle(bnoPitch - navxPitch);
    }

    public void disableBrake(){
        //intakeTurn.setIdleMode(IdleMode.kCoast);
    }

    public void enableBrake(){
        armTurn.setIdleMode(IdleMode.kBrake);
    }
}
