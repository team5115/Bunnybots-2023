package frc.team5115.Commands.Arms;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team5115.Classes.Software.Arm;

public class StowArm extends CommandBase{
    private final Arm arm;
    private boolean endEarly = false;
    
    public StowArm(Arm arm) {
        this.arm = arm;
    }

    @Override
    public void initialize() {
        // you can stow with abandon :)
        arm.stow();
    }

    @Override
    public void end(boolean interrupted) {
        arm.stop();
    }

    @Override
    public boolean isFinished() {
        if (endEarly) return true;
        // confirm that PID is done
        return arm.atSetpoint();
    }
}
