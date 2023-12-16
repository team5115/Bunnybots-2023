package frc.team5115.Commands.Auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team5115.Constants.Paths;
import frc.team5115.Classes.Software.Arm;
import frc.team5115.Classes.Software.BunnyCatcher;
import frc.team5115.Classes.Software.Drivetrain;

public class AutoCommandGroup extends SequentialCommandGroup {
    final Drivetrain drivetrain;
    final Arm arm;
    final BunnyCatcher bunnyCatcher;

    public AutoCommandGroup(Drivetrain drivetrain, boolean doOutsidePath, Arm arm, BunnyCatcher bunnyCatcher){
        this.drivetrain = drivetrain;
        this.arm = arm;
        this.bunnyCatcher = bunnyCatcher;

        if(doOutsidePath){
            denToYardOutside();
        }
        else{
            denToYardInside();
        }

        // addCommands(new DriveTimed(drivetrain, 1, 0.5));
    }

    private void denToYardOutside(){
        addCommands(new FollowTrajectory(drivetrain, Paths.denToYardOutside));
    }

    private void denToYardInside(){
        addCommands(new FollowTrajectory(drivetrain, Paths.denToYardInside));
    }
}
