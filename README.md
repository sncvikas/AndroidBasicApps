# AndroidBasicApps
This repo contains several apps that introduce Android basic features

Repo is organised as
1. Root directory -- contains only complete source code of the application. Apps under this directory can be imported into Android Studio.
2. LookupFiles -- contains only the important java/xml/screenshots files that define the core of the application. Users can quickly look into the source.

___

# Projects : 
___
# [Basic_Bluetooth](https://github.com/sncvikas/AndroidBasicApps/tree/master/Basic_Bluetooth)  :
Contains code to perform basic Bluetooth operations in Android. The features include
1. Enable/Disable Bluetooth
2. Get list of paired devices
3. Start Bluetooth device discovery to search for nearby Bluetooth devices	4. Register broadcast receivers for Bluetooth state changes events, device found intents.


___
# [ActivityTransitions](https://github.com/sncvikas/AndroidBasicApps/tree/master/ActivityTransitions):
Introduces following activity topics
1. Activity life cycle, When new activity is started, current activity goes to one level down in the backstack,
			while new activity becomes the top of the statck. Similarly when back is press, 
			activity at the top of the stack is destroyed bringing back the old activity to the top of the stack.
2. Button click listenters
3. Sending data as extras as part of the intent in main activity.
4. Retreiving data from intent and displaying it to the user.
		
`Note: LookupFiles for this project contains video and screeshot of logcat depicting above mentioned features.`
___

# [CustomList](https://github.com/sncvikas/AndroidBasicApps/tree/master/CustomList)
Introduces the following topics
1. How to create a custom list adapter for your own UI where each row has ImageView, TextView and an ImageButton
2. Asynctask which runs long operations in the background, not on the main thread, in our app it loads installed
		applications' data such as icon, name, packagename, version
3. Progress bar that we start in asynctask and keep updating it with progress from doInBackground


`Note: LookupFiles for this project contains video and screeshot of logcat depicting above mentioned features.`
___

# [Notes using SQLite](https://github.com/sncvikas/AndroidBasicApps/tree/master/NotesAppUsingSqlite)
Introduces the following topics
1. How to make use of Sqlite database to store data
2. Perform database operations such as insert, delete, update, query
3. Show custom dialog that accepts user input (EditText)
4. Listeners when items from the list are clicked

`Note: LookupFiles for this project contains screeshots and main source/resource files for the above mentioned features.`

___


# [FetchMovie - Web API Usage](https://github.com/sncvikas/AndroidBasicApps/tree/master/WebApiUsage)
Introduces the following topics
1. How to create an AsyncTaskLoader to perform task in background thread
2. Create HttpUrConnection to connect to webservice to fetch details of a movie such as title year of release, language, genre, ImDb rating
3. Parse output which is in json format to extract desired movie attributes.

`Note: LookupFiles for this project contains screeshots and main source/resource files for the above mentioned features.`

___

# [Magnifier Widget](https://github.com/sncvikas/AndroidBasicApps/tree/master/MagnifierOnImageView)
Introduces the following topics
1. How to create an Magnifier widget for an ImageView and TextView
2. Show the magnifier widget whenever the corresponding view is touched.

`Note: LookupFiles for this project contains screeshots and main source/resource files for the above mentioned features.`

___

# [NotificationsSample](https://github.com/sncvikas/AndroidBasicApps/tree/master/NotificationsSample)
Introduces the notifications feature of Android. Project demostrates below listed types of notifcations.
1. Basic notification
2. Notification with multiline line content
3. Notification with large icon
4. Notification to open an activity
5. Notification with an action button
6. Notification with an inline input
7. Notification with progress bar
8. Expandable notification with big image
9. Update a notification
10. Message style notifications
11. Notification to cancel after 5 seconds
12. Create a heads up notification
13. Cancel all notifications
14. Create custom notification layout
15. Open Notification Channel Settings

`Note: LookupFiles for this project contains screeshots and main source/resource files for the above mentioned features.`
___

# [AppBarSample With Search Action](https://github.com/sncvikas/AndroidBasicApps/tree/master/AppBarSearchSample)
Introduces the appbar/toolbar/actionbar with search action. This app has two activities main acitivty has a list of few quotes, and secondary acitivity shows the list of quotes that were searched from the main activity with a search string. Components used

1. ToolBar in layout xml, menu items in the menu.xml and providing menu click listeners, and searchable resource to indicate that we want to peform search from appbar menu.
2. Assigning a search action to one of the menu items by providing a actionViewClass as SearchView widget. And a creating an activity to listen for android.intent.action.SEARCH action.

`Note: LookupFiles for this project contains screeshots and main source/resource files for the above mentioned features.`
___

# [Dialogs Sample](https://github.com/sncvikas/AndroidBasicApps/tree/master/DialogsSample)
DialogsSample project introduces the differnt types of popup dialogs that are supported in Android. The different types used in the app are
1. Show confirmation dialog, for yes or no kind of dialogs
2. Show single choice items dialog, to let user make a choice
3. Show single choice items with default selection
4. Show multi-choice items dialog, to let user select multiple options
5. Dialog with custom layout, Login page hosted in the current activity as a dialog
6. Embed dialog in activity layout, custom layout dialog shown in fullscreen

`Note: LookupFiles for this project contains screeshots and main source/resource files for the above mentioned features.`

____
# [Menus Sample](https://github.com/sncvikas/AndroidBasicApps/tree/master/MenusSample)
MenusSample project introduces the different types of menus that are supported in Android. The app uses following menu types
1. Options menu seen in the action bar and in overflow
2. Context menu that appears on long clicking a list item
3. Action Mode menu, that appears on top of the action bar with icons when an view is long clicked.
4. Popup menu that appears on just clicking any view (here FloatingActionButton)

<br>Listeners are implemented when every menu item from each of the above menu types is clicked.

`Note: LookupFiles for this project contains screeshots and main source/resource files for the above mentioned features.`

____
# Services Sample [Service](https://github.com/sncvikas/AndroidBasicApps/tree/master/SampleServices) and [Client](https://github.com/sncvikas/AndroidBasicApps/tree/master/SampleServicesClient)
These two projects work together to introduce Android Services. <br/>
As part of [SampleServices](https://github.com/sncvikas/AndroidBasicApps/tree/master/SampleServices) source, we have following features
1. Creating a started service, starting it, stopping it from the same application. This runs on main thread. This can taken multiple requests at the same time. These must be explicitily stopped by calling stopSelf()/stopService()
2. Creating an intent service, starting it, stopping it from the same application. This runs on the background worker therad. Intent services can process only one intent at time, pending requests on queued in worker thread. You dont have stop them explicitly.
3. Creating a Bound service by extending the Binder class, binding to it, calling it's public methods, unbiding from it. Clients that are in the same process as the service can bind to such services.
4. Creating a foreground service in Android version O and after. These services are started with startForegroundService(), these services must show a notification to the user indicating the service is running. After a call to onStartCommand()/onHandleIntent(), they must call startForeground() by passing the notification that's started for the service. Once service is stopped, its corresponding notification is also killed. To create foreground service, we must have `android.permission.FOREGROUND_SERVICE`  permission in manifest.
5. Creating a bound service using a Messenger, binding to it from another application using expliciit intents. Mutliple clients can bind to such services, all requests are put in a message queue, processing the requests one by one. `android:exported` is set to `true` in manifest for these services to let outsiders bind.
6. Creating a bound service using AIDL interface by creating `.aidl` file, creating an instance of `Binder.Stub`. Clients must also have the identical `.aidl` file in their source to bind to such services. `android:exported` is set to `true` in manifest for these services to let outsiders bind  AIDL services differ from Messanger type bound services by allowing multiple clients to bind to it at the same time. Concurrency should be handled at the service itself.

As part of [SampleServicesClient](https://github.com/sncvikas/AndroidBasicApps/tree/master/SampleServicesClient) source, we have following features

1. Bind, interact and unbind with the service exported via Messanger of [SampleServices](https://github.com/sncvikas/AndroidBasicApps/tree/master/SampleServices)
2. Bind, interact and unbind with the service exported via AIDL of [SampleServices](https://github.com/sncvikas/AndroidBasicApps/tree/master/SampleServices)

`Note: LookupFiles for this project contains screeshots and main source/resource files for the above mentioned features. Ensure to look into AndroidManifest.xml file to see what kind of services need to exported `

____
# [Broadcast Receivers](https://github.com/sncvikas/AndroidBasicApps/tree/master/BroadcastsSample)
This project demonstrates different types of Broadcast receivers in Android. Following topics are covered
1. Register a broadcast receiver in AndroidManifest.xml
2. Disable a manifest registered broadcast receiver
3. Enable a manifest registered broadcast receiver
4. Register a broadcast receiver using context dynamically
5. Unregister a context registered broadcast receiver
6. Send a broadcast to other applications using its packageName or componentName.
7. Send and receive broadcast using LocalBroadcastManager
8. Send an ordered broadcast. `android:priority` attribute in manifest which receiver is delivered the broadcast first.

`Note: LookupFiles for this project contains screeshots and main source/resource files for the above mentioned features.`

___
# [BatteryStatusSample](https://github.com/sncvikas/AndroidBasicApps/tree/master/BatteryStatusSample)
This project introduces different broadcast intents that help you monitor battery state changes, and intent extras that provide useful battery related information
1. `Intent.ACTION_BATTERY_CHANGED` : This is sticky broadcast that does not need a receiver object when registered. BatteryManager returns the existing battery information as an Intent. These must always be context registered.
2. `Intent.ACTION_POWER_DISCONNECTED`, `Intent.ACTION_POWER_CONNECTED`: These are sent when device is connected for charging, these intents can be registered in manifest.
3. `Intent.ACTION_BATTERY_LOW`: This is sent when battery enters system defined low state. This can be registered in manifest.
4. `Intent.ACTION_BATTERY_OKAY`: This is sent when battery comes out low state. This can be registered in manifest.

`Note: Lookupfiles for this project contain screenshot, and main source file.`<br>
`Use commands listed here` [ADB_Commands](https://github.com/sncvikas/ADB_Commands#simulate-unplug-battery-event) ` to simulate battery events`

___
# [PreferencesSample](https://github.com/sncvikas/AndroidBasicApps/tree/master/PreferencesSample)
This projects introduces different preferences widgets that are supported in Android as listed below

`1. EditTextPreference`  - To take text input such as user name

`2. CheckBoxPreference` - To enable/disable a feature

`3. SwitchPreference` - To enable/disable, such as to show notifications to the user or not

`4. ListPreference` - Show a single choice list of values, such as how many notifications to show daily

`5. MultiListPreference` - Show a multi choice list of values, such as select multiple interests to show news for in news app

`6. Preference` - Plain text preference which user cannot change

`7. PreferenceScreen` - Single preference screen with intent infomation, such as starting an activity after clicking the preference

`8. PreferenceCategory` - Groups multiple related preferences into a single category.

`Note: Lookupfiles for this project contain screenshot, and main source file.`<br>
