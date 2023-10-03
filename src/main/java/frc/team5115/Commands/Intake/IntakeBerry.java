package frc.team5115.Commands.Intake;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj2.command.CommandBase;
import frc.team5115.Classes.Software.Arm;

public class IntakeBerry extends CommandBase{

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
        arm.spinIn();
    }

    @Override
    public void execute() {
        // print stuff here if need be for debugging
    }

    @Override
    public void end(boolean interrupted) {
        arm.spinStop();
    }

    @Override
    public boolean isFinished() {
        return timer.get() > time;
    }
}