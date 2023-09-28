package frc.team5115.Commands.Intake;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team5115.Classes.Accessory.Angle;
import frc.team5115.Classes.Software.BunnyCatcher;

public class CatchBunny extends CommandBase{
    static final double distToStop = 1;
    private BunnyCatcher bunnyCatcher;
    private double absoluteSetpoint;
    
    public CatchBunny(BunnyCatcher bunnyCatcher, double absoluteSetpoint) {
        this.bunnyCatcher = bunnyCatcher;
        this.absoluteSetpoint = absoluteSetpoint;
    }

    @Override
    public void execute() {
        bunnyCatcher.turnTowardsAngle(absoluteSetpoint, 1);
    }

    @Override
    public void end(boolean interrupted) {
        bunnyCatcher.stop();
    }

    @Override
    public boolean isFinished() {
        return new Angle(absoluteSetpoint).getDelta(bunnyCatcher.getAngle()) < distToStop;
    }
}
