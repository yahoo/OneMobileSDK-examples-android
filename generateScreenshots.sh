#!/bin/bash
rm -r generatedScreenshots

./gradlew clean assembleDebug assembleAndroidTest

cd VideoInFragment/
fastlane screengrab
cd ../

cd VideoInView/
fastlane screengrab
cd ../

cd ControlsModifiedContentControls/
fastlane screengrab
cd ../

cd ControlsCustomContentControls/
fastlane screengrab
cd ../

cd ControlsThemedControls/
fastlane screengrab
cd ../

cd ControlsSidebarVolume/
fastlane screengrab
cd ../

cd ControlsSidebarFullscreen/
fastlane screengrab
cd ../

cd ContentOverrideMetadata/
fastlane screengrab
cd ../

cd ContentLoadThumbnails/
fastlane screengrab
cd ../
