============================
Description:
ESM, an alternative shells for Windows. with custom graphic build on swing, using purely Java. 
This project started as a note taking system, for better organising and collecting and keeping track of notes.
currently version includes the following basic functionalities: different type of dynamic background, a simple file browser, infinity note board, dictionary, password manager. Calendar, file browser and instant note youtube-dl, audio player.
more functionality will be added in the future. ESM is intended to play the role as an alternative, or supplement for the current windows GUI.

============================
Requirement:
ESM used MySQL for storing all the information, so in order to use password manager, dictionary... , a local or online database account will be needed. 
SQL account are stored at ESM.setting index: 2010-2012. password manager key are stored at 2020.
It also used a popular youtube download python library. The youtube downloader with GUI is coming in the future update. So in order for it to function, python will need to be installed on the computer, with the youtube-dl lib installed as well.
if you like to use the audio player feature, you can fill the main folder containing all the music at index: 420 in the ESM.setting file.

============================
How to run:
To use ESM, have a folder containing the following files: ( name between <> )
Files <ESM.setting> < ESM_XML.setting> <ESM.jar> and a folder called <ESM>
All of which is given in the ESMexample folder as the demo code of the current version.
alternatively, you can build ESM and generate the ESM.jar by using the given code in src. the main method is in: src/ellus/ESM/Machine/mainDriver.java
To run. Open cmd, go to the folder containing the above file. Type: java- jar ESM.jar and hit enter.

============================
Use:
one of the design intention is that the user have multiple ways to access different functionality; can be purely using keyboard or purely using mouse.
the other design goal is that all the objective in ESM should be achieved by around the same amount of GUI input, same amount of user mouse and keyboard action. If task are leaves of a tree, and we are currently at home view, then the tree should be fairly balanced in depth.
The use of ESM should be fairly intuitive, even without proper direction, one will be able to figure out the way to navigate around different functionality.

When run ESM,  initial view is in desktop regular.
there is three desktop view: desktop link, desktop regular, desktop note.
arrow right, arrow down, mouse down = go to the next option.
arrow left, arrow up, mouse up= go to the previous option.

desktop regular currently is just a visualization.
desktop link contains shortcut
desktop note contains note that can be pinned on the desktop.
in all three desktop view, press space will open the functional tab.


in functional tab. you can choose the available app by left click, after choosing, you will be brought back to the desktop regular, switching out of desktop regular view will hide all the app window.

in desktop regular:
double press esc will minimize the ESM window.
press letter any key other than esc and space will open dictionary.

general keyboard mouse input.
Left mouse in general is focus and select, highlight.
keyboard ESC and Right mouse in general is for close, and go back.






