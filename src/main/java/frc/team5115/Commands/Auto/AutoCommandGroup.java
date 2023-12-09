package frc.team5115.Commands.Auto;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team5115.Constants.Paths;
import frc.team5115.Classes.Accessory.MechanismCoordination;
import frc.team5115.Classes.Software.Arm;
import frc.team5115.Classes.Software.BunnyCatcher;
import frc.team5115.Classes.Software.Drivetrain;
import frc.team5115.Commands.ArmStateControl.DeployFully;
import frc.team5115.Commands.ArmStateControl.StowFully;
import frc.team5115.Commands.Intake.SpinBunnyCatcher;

public class AutoCommandGroup extends SequentialCommandGroup {
    final Drivetrain drivetrain;
    final Arm arm;
    final BunnyCatcher bunnyCatcher;
    final MechanismCoordination coordination;

    public AutoCommandGroup(Drivetrain drivetrain, boolean doOutsidePath, Arm arm, BunnyCatcher bunnyCatcher, MechanismCoordination coordination){
        this.drivetrain = drivetrain;
        this.arm = arm;
        this.bunnyCatcher = bunnyCatcher;
        this.coordination = coordination;

        if(doOutsidePath){
            denToYardOutside();
        }
        else{
            denToYardInside();
        }
        catchBunny();
    }

    private void denToYardOutside(){
        addCommands(new FollowTrajectory(drivetrain, Paths.denToYardOutside));
    }

    private void denToYardInside(){
        addCommands(new FollowTrajectory(drivetrain, Paths.denToYardInside));
    }

    /**
    * Fully deploys everything, spins the bunny catcher 180 degrees, then re-stows everthing
    */
    private void catchBunny() {
        addCommands(
            new DeployFully(arm, bunnyCatcher, coordination),
            new WaitCommand(0.5),
            new SpinBunnyCatcher(bunnyCatcher, 180, 1),
            new WaitCommand(0.5),
            new StowFully(arm, bunnyCatcher, coordination)
        );
    } 
}
