package frc.team5115.Commands.Intake;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team5115.Classes.Accessory.MechanismCoordination;
import frc.team5115.Classes.Accessory.MechanismCoordination.State;
import frc.team5115.Classes.Software.BunnyCatcher;

public class DeployCatcher extends SequentialCommandGroup {

    final MechanismCoordination coordination;

    public DeployCatcher(BunnyCatcher bunnyCatcher, MechanismCoordination coordination) {
        this.coordination = coordination;

        // if it's already fully stowed, then we don't want to collide with the arm, so we exit early
        if (coordination.getState() == State.FullyStowed) return;

        addCommands(
            new InstantCommand(bunnyCatcher::deployCatcher),
            new WaitCommand(0.5),
            new InstantCommand(this::updateCoordination)
        );
    }

    private void updateCoordination() {
        coordination.setState(State.FullyDeployed);
    }
}