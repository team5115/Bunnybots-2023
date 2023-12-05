package frc.team5115.Commands.Auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team5115.Classes.Software.Drivetrain;
import frc.team5115.Constants.Paths;

public class AutoCommandGroup extends SequentialCommandGroup {
    final Drivetrain drivetrain;

    public AutoCommandGroup(Drivetrain drivetrain, boolean doOutsidePath){
        this.drivetrain = drivetrain;
        if(doOutsidePath){
            denToYardOutside();
        }
        else{
            denToYardInside();
        }
    }

    private void denToYardOutside(){
        addCommands(new FollowTrajectory(drivetrain, Paths.denToYardOutside));
    }

    private void denToYardInside(){
        addCommands(new FollowTrajectory(drivetrain, Paths.denToYardInside));
    }
}
