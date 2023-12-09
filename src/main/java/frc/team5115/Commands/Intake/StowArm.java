package frc.team5115.Commands.Intake;

import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;
import frc.team5115.Classes.Accessory.Angle;
import frc.team5115.Classes.Accessory.MechanismCoordination;
import frc.team5115.Classes.Accessory.MechanismCoordination.State;
import frc.team5115.Classes.Software.Arm;

public class StowArm extends SequentialCommandGroup {

    final MechanismCoordination coordination;

    public StowArm(Arm arm, MechanismCoordination coordination) {
        this.coordination = coordination;

        // if we are fully deployed, we are not allowed to stow the arm without first stowing the bunny catcher
        if (coordination.getState() == State.FullyDeployed) {
            System.out.println("StowArm fails because already fully deployed");
            return;
        }

        addCommands(
            // TODO determine what angle is stowed
            new MoveArm(new Angle(0), arm),
            new WaitCommand(0.5),
            new InstantCommand(this::updateCoordination)
        );
    }
    
    private void updateCoordination() {
        coordination.setState(State.FullyStowed);
    }
}
