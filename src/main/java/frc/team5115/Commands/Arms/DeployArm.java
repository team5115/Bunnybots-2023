package frc.team5115.Commands.Arms;

import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team5115.Classes.Software.Arm;
import frc.team5115.Classes.Software.BunnyCatcher;

public class DeployArm extends CommandBase{
    private final Arm arm;
    private final BunnyCatcher bunnyCatcher;
    private boolean endEarly = false;
    
    public DeployArm(Arm arm, BunnyCatcher bunnyCatcher) {
        this.arm = arm;
        this.bunnyCatcher = bunnyCatcher;
    }

    @Override
    public void initialize() {
        // if the bunny catcher is deployed, then you are not allowed to deploy the arm
        if (bunnyCatcher.isDeployed()) {
            endEarly = true;
        } else {
            arm.deploy();
        }
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
