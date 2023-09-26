package frc.team5115.Commands.Auto;

import java.util.HashMap;

import com.pathplanner.lib.commands.FollowPathWithEvents;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import frc.team5115.Classes.Software.*;
import frc.team5115.Classes.Hardware.*;
import frc.team5115.Classes.Software.Paths;

public class AutoCommandGroup extends SequentialCommandGroup {
    final Drivetrain drivetrain;
	final Paths paths;

    public AutoCommandGroup(Drivetrain drivetrain, boolean inIdealPosition){
        this.drivetrain = drivetrain;
		this.paths = new Paths();
        }
    
	/**
	 * Places a cube in a low node. Expects that the robot has its back facing the grid and has a cube on its base.
	 */
    private void cubeDrop() {
        addCommands(    
            new DriveForward(drivetrain, -0.26, 0.5), // back up to node
            new DriveForward(drivetrain, +0.65, 1.2), // speed away to drop cube
            new DriveForward(drivetrain, -0.85, 0.8) // back up to push cube into place
        );
        // this should finish with the robot pushed up against the node
    }

	/**
	 * Exits the community.
	 */
    private void exitCommunity() {
        addCommands(
            new DriveForward(drivetrain, +3, 1.0), // exit community
            new InstantCommand(drivetrain :: stop)
        );
    }

	/**
	 * Exits the community. Uses PathPlanner instead of the `DriveForward`.
	 */
	private void exitCommunityPathPlanner() {
		addCommands(
			drivetrain.getRamseteCommand(paths.ExitCommunity)
		);
	}
}
