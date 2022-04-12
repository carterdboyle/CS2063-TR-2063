# CS2063-TR-2063

TR-2063 Drum Machine

This application provides a basic sequencer, sampler and live drum pad.

To use the application in sequencer mode hold the phone in the landscape orientation. This will allow you to program a pattern by clicking any of the cells.
When the cell switches from yellow to red, after clicking it, that means that the sound labelled to the left of the screen will trigger when the playhead
reaches that location. The names of the samples can be changed by simply clicking on them and editing, ensuring to click the DONE button when finished.
The buttons on the right side of the screen show the different patterns available. Creating a pattern will automatically store it to the pattern bank
associated with whatever button is highlighted on the right. The user can switch between patterns by selecting the different patterns on the right.

Using the application in portrait orientation will switch to the live drum pad, so be careful how you are holding the phone. In the live drum pad you can
configure sounds to be played when you hit the pads. The name of the currently selected sound bank is displayed at the top of the screen.
These settings will be saved as long as the application is installed on your phone. The same is true with the sound banks, which you can edit by clicking
the library button at the top of the screen. This will allow you to search through the files included with this application to build a sound bank.

The record functionality is very basic and allows you to record a sample which you can access from the file browser in soundbank creation. It will be
labelled 'sample.3gp'. You can load in this sound for any sound bank. You can record a sample by selecting the record button in portrait orientation.

# Release Notes

Ver 1.0 - tested on Android 8.0 and 12.0 on a Google Pixel 2 and 4a, with API 26 and 32, respectively. Neither group member had access to a physical android device so the application was testing with the emulated versions of the phone previously mentioned. 

Features available:

-Sequencer
-Live drum pad
-Ability to change tempo
-Ability to swap patterns in the sequencer
-Intro screen w/ help page
-Swaps between sequencer and live drum pad with orientation change
-Create and load from a library of sound banks
-Record audio from the microphone and load into sound bank

# Known Issues

-Noticed a bug when swapping between patterns quickly in the sequencer view where sometimes sounds would not play until the next iteration of the loop
-Hitting the back button after a screen orientation change can cause the application to display a blank screen if the previous activity was in a different orientation
-The text in the portrait version of the sound bank creation activity which corresponds to the name of the resource file can be potentially cut short...

# Supported API Levels

The application was developed for Oreo (Android API level 26) or Android 8.0. It was tested on Android 12.0 and it works so it should work for 8.0 and up. 

# Test cases / Scenarios

Some test cases could include:
-Switching orientations to switch between activities - try while the app is running to check if the sound players stop when they are supposed to.
-Add in some sound banks to the library and check the browse capability and ensure when re-opening the application the sound banks persist.
-Try the above point with the patterns in the sequencer as well
-Try to switch the orientation of the application while looking for a sound bank. It should not auto-adjust and when clicking 'OK' it should go back to the proper activity, based on the orientation the phone is currently in

