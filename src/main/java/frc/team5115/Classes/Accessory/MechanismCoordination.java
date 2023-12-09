package frc.team5115.Classes.Accessory;

import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.InstantCommand;

public class MechanismCoordination {

    public enum State {
        FullyStowed, // both arms are stowed, with the berry on the outside and the bunny catcher locked in behind it
        BunnyStowedBerryDeployed, // the bunny catcher is stowed, but the berry catcher is out
        FullyDeployed, // both arms are deployed
        Moving // something is currently changing
    }

    public enum Action {
        DeployArm,
        StowArm,
        DeployCatcher,
        StowCatcher,
    }

    private State state;

    public MechanismCoordination(State mechState) {
        this.state = mechState;
    }

    public State getState() {
        return state;
    }

    /**
     * Checks if you could perform an action on the state of the machine and then gives you a command to run after the robot is physically moved.
     * Returns null if the action is not allowed on the current state of the robot.
     * @param action the action to attempt to perform
     * @return a command to schedule after you run your actual commands to move the subsystems; or returns null if the action is not allowed on the state
     */
    public Command tryPerformAction(Action action) {
        if (state == State.Moving) return null;

        State newState = attemptActionOnState(state, action);
        if (newState == state) {
            // create a command that will jsut run the setState function
            return new InstantCommand(new Runnable() {
                @Override
                public void run() {
                    System.out.println("MechanismCoordination sets a new state of: " + newState.toString());
                    state = newState;
                }
            });
        } else {
            return null;
        }
    }

    /**
     * Try to perform an action on a state, and then tells you what the resulting state would be
     * @param state the current state
     * @param action the action to perform
     * @return returns the new state, given the action
     */
    private static State attemptActionOnState(State state, Action action) {
        switch (action) {
            case DeployArm:
                if (state == State.FullyStowed) return State.BunnyStowedBerryDeployed;
                break;

            case StowArm:
                if (state == State.BunnyStowedBerryDeployed) return State.FullyStowed;
                break;

            case DeployCatcher:
                if (state == State.BunnyStowedBerryDeployed) return State.FullyDeployed;
                break;

            case StowCatcher:
                if (state == State.FullyDeployed) return State.BunnyStowedBerryDeployed;
                break;

            default:
                break;
        }
        // the action couldn't be run on the state, so the state does not change
        return state;
    }
}
