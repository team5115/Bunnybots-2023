package frc.team5115.Commands.Auto;

import java.util.HashMap;

import com.pathplanner.lib.commands.FollowPathWithEvents;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team5115.Classes.Software.*;
import frc.team5115.Classes.Hardware.*;
import frc.team5115.Constants.Paths;

public class AutoCommandGroup extends SequentialCommandGroup {
    final Drivetrain drivetrain;

    public AutoCommandGroup(Drivetrain drivetrain, boolean doInsidePath){
        this.drivetrain = drivetrain;
        if(doInsidePath){
            denToYardInside();
        }
        else{
            denToYardOutside();
        }
        
    }

    private void denToYardOutside(){
        addCommands(drivetrain.getRamseteCommand(Paths.denToYardOutside));
    }

    private void denToYardInside(){
        addCommands(drivetrain.getRamseteCommand(Paths.denToYardInside));
    }
}
