package frc.team5115.Commands.ArmStateControl;

import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team5115.Classes.Accessory.MechanismCoordination;
import frc.team5115.Classes.Software.Arm;
import frc.team5115.Classes.Software.BunnyCatcher;

public class DeployFully extends SequentialCommandGroup {
    public DeployFully(Arm arm, BunnyCatcher bunnyCatcher, MechanismCoordination coordination) {
        switch (coordination.getState()) {
            case FullyStowed:
                // we have to deploy the entire thing
                addCommands(
                    new DeployArm(arm, coordination),
                    new DeployCatcher(bunnyCatcher, coordination)
                );
                break;

            case BunnyStowedBerryDeployed:
                // we just have to deploy BunnyCatcher
                addCommands(new DeployCatcher(bunnyCatcher, coordination));
                break;

            default:
                break;
        }
    }
}
