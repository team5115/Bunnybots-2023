package frc.team5115.Commands.Auto;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team5115.Classes.Accessory.Angle;
import frc.team5115.Classes.Accessory.MechanismCoordination;
import frc.team5115.Classes.Accessory.MechanismCoordination.State;
import frc.team5115.Classes.Software.Arm;
import frc.team5115.Classes.Software.BunnyCatcher;
import frc.team5115.Commands.Intake.MoveArm;

public class FullyDeploy extends SequentialCommandGroup {
    final MechanismCoordination coordination;

    public FullyDeploy(Arm arm, BunnyCatcher bunnyCatcher, MechanismCoordination coordination) {
        this.coordination = coordination;

        // TODO determine what angle is deployed
        addCommands(
            new MoveArm(new Angle(90), arm),
            new WaitCommand(0.5),
            new InstantCommand(bunnyCatcher::deployCatcher),
            new InstantCommand(this::updateCoordination)
        );
    }

    private void updateCoordination() {
        coordination.setState(State.FullyDeployed);
    }
}
