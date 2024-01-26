package frc.team5115.Commands.Intake;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team5115.Classes.Accessory.Angle;
import frc.team5115.Classes.Software.Arm;

public class MoveArm extends Command{
    private final Arm arm;
    
    public MoveArm(Angle absoluteSetpoint, Arm arm) {
        this.arm = arm;
        arm.setSetpoint(absoluteSetpoint);
    }

    @Override
    public void end(boolean interrupted) {
        arm.stop();
    }

    @Override
    public boolean isFinished() {
        // confirm that PID is done
        return arm.atSetpoint();
    }
}
