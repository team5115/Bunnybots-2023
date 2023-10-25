package frc.team5115.Commands.Intake;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team5115.Classes.Accessory.Angle;
import frc.team5115.Classes.Software.Arm;

public class MoveArm extends CommandBase{
    private Arm arm;
    private Angle absoluteSetpoint;
    private double distToStop = 1;
    private boolean atSetpoint;
    
    public MoveArm(Angle absoluteSetpoint, Arm arm) {
        this.absoluteSetpoint = absoluteSetpoint;
        this.arm = arm;
        arm.setSetpoint(absoluteSetpoint);
    }

    @Override
    public void execute() {
        atSetpoint = arm.updateController();
    }

    @Override
    public void end(boolean interrupted) {
        arm.stop();
    }

    @Override
    public boolean isFinished() {
        // confirm that PID is done AND it matches this commands requirements for finishing
        return atSetpoint && absoluteSetpoint.getDelta(arm.getAngle()) < distToStop;
    }
}
