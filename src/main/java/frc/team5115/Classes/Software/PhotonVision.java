/*
* MIT License
*
* Copyright (c) PhotonVision
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in all
* copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
*/

package frc.team5115.Classes.Software;

import java.io.IOException;
import java.util.Optional;

import org.photonvision.EstimatedRobotPose;
import org.photonvision.PhotonCamera;
import org.photonvision.PhotonPoseEstimator;
import org.photonvision.PhotonPoseEstimator.PoseStrategy;

import edu.wpi.first.apriltag.AprilTagFieldLayout;
import edu.wpi.first.apriltag.AprilTagFields;
import edu.wpi.first.math.geometry.Pose2d;
import edu.wpi.first.wpilibj.DriverStation;

public class PhotonVision {
    private static final int NUM_OF_CAMS = 3;
    private PhotonCamera[] cameras;
    private PhotonPoseEstimator[] photonEstimators; 

    public PhotonVision() {
        cameras = new PhotonCamera[] {
            new PhotonCamera(frc.team5115.Constants.VisionConstants.rightCameraName),
            new PhotonCamera(frc.team5115.Constants.VisionConstants.leftCameraName),
            new PhotonCamera(frc.team5115.Constants.VisionConstants.frontCameraName)
        };

        // initialize the cameras
        photonEstimators = new PhotonPoseEstimator[] {null, null, null};

        try {
            // Attempt to load the AprilTagFieldLayout that will tell us where the tags are on the field.
            AprilTagFieldLayout fieldLayout = AprilTagFields.k2023ChargedUp.loadAprilTagLayoutField();
            // create pose estimators

            for (int i = 0; i < NUM_OF_CAMS; i++) {
                photonEstimators[i] = new PhotonPoseEstimator(
                    fieldLayout,
                    PoseStrategy.MULTI_TAG_PNP,
                    cameras[i],
                    frc.team5115.Constants.VisionConstants.robotToCamL);

                photonEstimators[i].setMultiTagFallbackStrategy(PoseStrategy.LOWEST_AMBIGUITY);
            }

        } catch (IOException e) {
            // The AprilTagFieldLayout failed to load. We won't be able to estimate poses if we don't know
            // where the tags are.
            DriverStation.reportError("Failed to load AprilTagFieldLayout", e.getStackTrace());
            photonEstimators = null;
        }
    }

    /**
    * @param estimatedRobotPose The current best guess at robot pose
    * @return an EstimatedRobotPose with an estimated pose, the timestamp, and targets used to create
    *     the estimate
    */
    public Optional<EstimatedRobotPose> getEstimatedGlobalPose(Pose2d prevEstimatedRobotPose) {
        if (photonEstimators == null) {
            // The field layout failed to load, so we cannot estimate poses.
            return Optional.empty();
        }

        // loop thru all the pose estimators and return the first estimation.
        // if none of them have any then return an empty
        for(int i = 0; i < NUM_OF_CAMS; i++) {
            photonEstimators[i].setReferencePose(prevEstimatedRobotPose);
            var pose = photonEstimators[i].update();
            if (pose.isPresent()) return pose;
        }

        return Optional.empty();
    }
}