package frc.team5115.Commands.Arms;

import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.team5115.Classes.Software.BunnyCatcher;

public class StowCatcher extends Command{
    private final BunnyCatcher bunnyCatcher;
    private final Timer timer;
    private final double timeToComplete = 1;
    private boolean endEarly = false;
    
    public StowCatcher(BunnyCatcher bunnyCatcher) {
        this.bunnyCatcher = bunnyCatcher;
        timer = new Timer();
    }

    @Override
    public void initialize() {
        // you can stow with abandon :)
        bunnyCatcher.stow();
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
