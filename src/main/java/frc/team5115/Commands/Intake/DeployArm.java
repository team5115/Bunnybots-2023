package frc.team5115.Commands.Intake;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team5115.Classes.Accessory.Angle;
import frc.team5115.Classes.Accessory.MechanismCoordination;
import frc.team5115.Classes.Accessory.MechanismCoordination.State;
import frc.team5115.Classes.Software.Arm;

public class DeployArm extends SequentialCommandGroup {

    final MechanismCoordination coordination;

    public DeployArm(Arm arm, MechanismCoordination coordination) {
        this.coordination = coordination;

        // if it's already fully deployed, then we don't wanna lie about the position of the bunny catcher
        if (coordination.getState() == State.FullyDeployed) return;

        addCommands(
            // TODO determine what angle is deployed
            new MoveArm(new Angle(90), arm),
            new WaitCommand(0.5),
            new InstantCommand(this::updateCoordination)
        );
    }

    private void updateCoordination() {
        coordination.setState(State.BunnyStowedBerryDeployed);
    }
}
