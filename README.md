# AndroidBasicApps
This repo contains several apps that introduce Android basic features

Repo is organised into two main directories
1. AppSource -- contains only complete source code of the application. Apps under this directory can be imported into Android Studio.
2. LookupFiles -- contains only the important java/xml/screenshots files that define the core of the application. Users can quickly look into the source.


# Projects : 

# Basic_Bluetooth : 
Contains code to perform basic Bluetooth operations in Android. The features include
1. Enable/Disable Bluetooth
2. Get list of paired devices
3. Start Bluetooth device discovery to search for nearby Bluetooth devices	4. Register broadcast receivers for Bluetooth state changes events, device found intents.


# ActivityTransitions: 
Introduces following activity topics
1. Activity life cycle, When new activity is started, current activity goes to one level down in the backstack,
			while new activity becomes the top of the statck. Similarly when back is press, 
			activity at the top of the stack is destroyed bringing back the old activity to the top of the stack.
2. Button click listenters
3. Sending data as extras as part of the intent in main activity.
4. Retreiving data from intent and displaying it to the user.
Note: LookupFiles for this project contains video and screeshot of logcat depicting above mentioned features.
