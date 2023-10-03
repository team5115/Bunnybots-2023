package frc.team5115.Commands.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team5115.Classes.Accessory.Angle;
import frc.team5115.Classes.Software.Arm;

public class MoveArm extends CommandBase{
    private Arm arm;
    private double absoluteSetpoint;
    private double distToStop = 1;
    
    public MoveArm(double absoluteSetpoint, Arm arm) {
        this.absoluteSetpoint = absoluteSetpoint;
        this.arm = arm;
    }

    @Override
    public void execute() {
        arm.setAngle(absoluteSetpoint);
        arm.updateController();
    }

    @Override
    public void end(boolean interrupted) {
        arm.stop();
    }

    @Override
    public boolean isFinished() {
        return new Angle(absoluteSetpoint).getDelta(arm.getAngle()) < distToStop;
    }
}
