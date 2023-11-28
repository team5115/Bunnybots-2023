package frc.team5115.Commands.Auto;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team5115.Classes.Software.Drivetrain;
import frc.team5115.Commands.Auto.VisionAuto.DriveForwardWVision;
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
    
    private void ScoreBerryWithVision() {
        addCommands(
            new DriveForwardWVision(drivetrain, -0.26, 0.5), // 
            new DriveForwardWVision(drivetrain, +0.65, 1.2), //
            new DriveForwardWVision(drivetrain, -0.85, 0.8) // 
        );
    }

    private void exitDenWithVision() {
        addCommands(
            new DriveForwardWVision(drivetrain, +3, 1.0), // exit community
            new InstantCommand(drivetrain :: stop)
        );
    }
}
