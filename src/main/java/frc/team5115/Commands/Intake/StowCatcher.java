package frc.team5115.Commands.Intake;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team5115.Classes.Accessory.MechanismCoordination;
import frc.team5115.Classes.Accessory.MechanismCoordination.State;
import frc.team5115.Classes.Software.BunnyCatcher;

public class StowCatcher extends SequentialCommandGroup {

    final MechanismCoordination coordination;

    public StowCatcher(BunnyCatcher bunnyCatcher, MechanismCoordination coordination) {
        this.coordination = coordination;

        // the catcher is always allowed to stow itself
        // if fully stowed, np; if fully deployed, np, and if half half, then np because bunny is already stowed :)
        // however, we do need to determine what the state will be after this

        addCommands(
            new InstantCommand(bunnyCatcher::stowCatcher),
            new WaitCommand(0.5),
            new InstantCommand(this::updateCoordination)
        );
    }
    
    private void updateCoordination() {
        switch (coordination.getState()) {
            case FullyStowed:
                // don't change anything
                break;
            case BunnyStowedBerryDeployed:
                coordination.setState(State.FullyStowed);
                break;
            case FullyDeployed:
                coordination.setState(State.BunnyStowedBerryDeployed);
                break;
            default:
                DriverStation.reportError("Somebody added another coordination state without updating the StowCatcher class!", true);
                break;
        }
    }
}