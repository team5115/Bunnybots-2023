package frc.team5115.Commands.ArmStateControl;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team5115.Classes.Accessory.MechanismCoordination;
import frc.team5115.Classes.Software.Arm;
import frc.team5115.Classes.Software.BunnyCatcher;

public class StowFully extends SequentialCommandGroup {
    public StowFully(Arm arm, BunnyCatcher bunnyCatcher, MechanismCoordination coordination) {
        switch (coordination.getState()) {
            case FullyDeployed:
                // we have to stow the entire thing
                addCommands(
                    new StowCatcher(bunnyCatcher, coordination),
                    new StowArm(arm, coordination)
                );
                break;

            case BunnyStowedBerryDeployed:
                // we just have to stow the arm/berry catcher
                addCommands(new StowArm(arm, coordination));
                break;

            default:
            case FullyStowed:
                // do nothing here
                break;
        }
    }
}
