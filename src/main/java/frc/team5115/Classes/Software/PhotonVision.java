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

    private PhotonCamera photonCamera;
    private PhotonCamera camRight;
    private PhotonCamera camLeft;
    private PhotonPoseEstimator photonPoseEstimator;
    private PhotonPoseEstimator leftPoseEstimator;
    private PhotonPoseEstimator rightPoseEstimator;


     
 
     public PhotonVision() {
         // Change the name of your camera here to whatever it is in the PhotonVision UI.
         camRight = new PhotonCamera(frc.team5115.Constants.VisionConstants.rightCameraName);
         camLeft = new PhotonCamera(frc.team5115.Constants.VisionConstants.leftCameraName);
         photonCamera = new PhotonCamera(frc.team5115.Constants.VisionConstants.frontCameraName);

         try {
             // Attempt to load the AprilTagFieldLayout that will tell us where the tags are on the field.
             AprilTagFieldLayout fieldLayout = AprilTagFields.k2023ChargedUp.loadAprilTagLayoutField();
             // Create pose estimator
             photonPoseEstimator = new PhotonPoseEstimator(
            fieldLayout, PoseStrategy.MULTI_TAG_PNP, photonCamera,  frc.team5115.Constants.VisionConstants.robotToCamL);
            photonPoseEstimator.setMultiTagFallbackStrategy(PoseStrategy.LOWEST_AMBIGUITY);
         } catch (IOException e) {
             // The AprilTagFieldLayout failed to load. We won't be able to estimate poses if we don't know
             // where the tags are.
             DriverStation.reportError("Failed to load AprilTagFieldLayout", e.getStackTrace());
             photonPoseEstimator = null;
         }
         
         try {
             AprilTagFieldLayout fieldLayout = AprilTagFields.k2023ChargedUp.loadAprilTagLayoutField();
             leftPoseEstimator = new PhotonPoseEstimator(
            fieldLayout, PoseStrategy.MULTI_TAG_PNP, camLeft,  frc.team5115.Constants.VisionConstants.robotToCamL);
            leftPoseEstimator.setMultiTagFallbackStrategy(PoseStrategy.LOWEST_AMBIGUITY);
         } catch (IOException e) {
             DriverStation.reportError("Failed to load AprilTagFieldLayout", e.getStackTrace());
             leftPoseEstimator = null;
         }
         
         try {
             AprilTagFieldLayout fieldLayout = AprilTagFields.k2023ChargedUp.loadAprilTagLayoutField();
             rightPoseEstimator = new PhotonPoseEstimator(
            fieldLayout, PoseStrategy.MULTI_TAG_PNP, camRight,  frc.team5115.Constants.VisionConstants.robotToCamL);
            rightPoseEstimator.setMultiTagFallbackStrategy(PoseStrategy.LOWEST_AMBIGUITY);
         } catch (IOException e) {
             DriverStation.reportError("Failed to load AprilTagFieldLayout", e.getStackTrace());
             rightPoseEstimator = null;
         }
     }
 
     /**
      * @param estimatedRobotPose The current best guess at robot pose
      * @return an EstimatedRobotPose with an estimated pose, the timestamp, and targets used to create
      *     the estimate
      */
     public Optional<EstimatedRobotPose> getEstimatedGlobalPose(Pose2d prevEstimatedRobotPose) {
         if (photonPoseEstimator == null) {
             // The field layout failed to load, so we cannot estimate poses.
             return Optional.empty();
         }
         photonPoseEstimator.setReferencePose(prevEstimatedRobotPose);
         leftPoseEstimator.setReferencePose(prevEstimatedRobotPose);
         rightPoseEstimator.setReferencePose(prevEstimatedRobotPose);

         var pose1 = photonPoseEstimator.update();
         var pose2 = photonPoseEstimator.update();
         var pose3 = photonPoseEstimator.update();

         if (pose1.isPresent()) {
            return pose1;
         } else if( pose2.isPresent()){
            return pose2;
         } else if (pose3.isPresent()){
            return pose3;
         } else {
            return Optional.empty();
         }


     }

 }