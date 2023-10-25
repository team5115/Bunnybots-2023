package frc.team5115.Commands.Auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team5115.Classes.Software.*;
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
        addCommands(drivetrain.getRamseteCommand(Paths.denToYardOutside));
    }

    private void denToYardInside(){
        addCommands(drivetrain.getRamseteCommand(Paths.denToYardInside));
    }
}
