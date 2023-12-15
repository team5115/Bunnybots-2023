package frc.team5115.Commands.Arms;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team5115.Classes.Software.Arm;
import frc.team5115.Classes.Software.BunnyCatcher;

public class ToggleCatcher extends CommandBase{
    private final Arm arm;
    private final BunnyCatcher bunnyCatcher;
    private final Timer timer;
    private final double timeToComplete = 1;
    private boolean endEarly = false;
    
    public ToggleCatcher(BunnyCatcher bunnyCatcher, Arm arm) {
        this.arm = arm;
        this.bunnyCatcher = bunnyCatcher;
        timer = new Timer();
    }

    @Override
    public void initialize() {
        if (bunnyCatcher.isDeployed()) {
            // stow it
            bunnyCatcher.stow();
        } else {
            // stow if arm not deployed
            if (arm.isDeployed()) {
                endEarly = true;
            } else {
                bunnyCatcher.deploy();
            }
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
        return timer.get() > timeToComplete;
    }
}
