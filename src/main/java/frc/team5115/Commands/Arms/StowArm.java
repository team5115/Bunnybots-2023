package frc.team5115.Commands.Arms;

import edu.wpi.first.wpilibj2.command.Command;
import frc.team5115.Classes.Software.Arm;

public class StowArm extends Command{
    private final Arm arm;
    
    public StowArm(Arm arm) {
        this.arm = arm;
    }

    @Override
    public void initialize() {
        arm.stow();
    }

    @Override
    public void end(boolean interrupted) {
        arm.stop();
    }

    @Override
    public boolean isFinished() {
        return arm.atSetpoint();
    }
}
