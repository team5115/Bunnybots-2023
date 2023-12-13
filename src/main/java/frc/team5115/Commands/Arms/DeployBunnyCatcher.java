package frc.team5115.Commands.Arms;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team5115.Classes.Software.Arm;
import frc.team5115.Classes.Software.BunnyCatcher;

public class DeployBunnyCatcher extends CommandBase{
    private final Arm arm;
    private final BunnyCatcher bunnyCatcher;
    private final Timer timer;
    private final double timeToComplete = 1;
    private boolean endEarly = false;
    
    public DeployBunnyCatcher(Arm arm, BunnyCatcher bunnyCatcher) {
        this.arm = arm;
        this.bunnyCatcher = bunnyCatcher;
        timer = new Timer();
    }

    @Override
    public void initialize() {
        // if the arm is deployed, then you are not allowed to deploy the bunny catcher
        if (arm.isDeployed()) {
            endEarly = true;
        } else {
            bunnyCatcher.deploy();
        }
    }

    @Override
    public void end(boolean interrupted) {
        bunnyCatcher.stop();
    }

    @Override
    public boolean isFinished() {
        if (endEarly) return true;
        // confirm that PID is done
        return 
    }
}
