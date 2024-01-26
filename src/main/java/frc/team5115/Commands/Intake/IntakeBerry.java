package frc.team5115.Commands.Intake;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.Command;
import frc.team5115.Classes.Software.Arm;

public class IntakeBerry extends Command{

    private Arm arm;
    private double time;
    private final Timer timer;

    public IntakeBerry(Arm arm, double time) {
        this.arm = arm;
        this.time = time;
        timer = new Timer();
    }

    @Override
    public void initialize(){
        timer.start();
        timer.reset();
        arm.spin(+0.5);
    }

    @Override
    public void execute() {
        // print stuff here if need be for debugging
    }

    @Override
    public void end(boolean interrupted) {
        arm.spin(+0);
    }

    @Override
    public boolean isFinished() {
        return timer.get() > time;
    }
}