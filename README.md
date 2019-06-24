# AndroidBasicApps
This repo contains several apps that introduce Android basic features

Repo is organised into two main directories
1. AppSource -- contains only complete source code of the application. Apps under this directory can be imported into Android Studio.
2. LookupFiles -- contains only the important java/xml/screenshots files that define the core of the application. Users can quickly look into the source.

___

# Projects : 
___
# [Basic_Bluetooth](https://github.com/sncvikas/AndroidBasicApps/tree/master/AppSource/Basic_Bluetooth)  :
Contains code to perform basic Bluetooth operations in Android. The features include
1. Enable/Disable Bluetooth
2. Get list of paired devices
3. Start Bluetooth device discovery to search for nearby Bluetooth devices	4. Register broadcast receivers for Bluetooth state changes events, device found intents.

___
# [ActivityTransitions](https://github.com/sncvikas/AndroidBasicApps/tree/master/AppSource/ActivityTransitions):
Introduces following activity topics
1. Activity life cycle, When new activity is started, current activity goes to one level down in the backstack,
			while new activity becomes the top of the statck. Similarly when back is press, 
			activity at the top of the stack is destroyed bringing back the old activity to the top of the stack.
2. Button click listenters
3. Sending data as extras as part of the intent in main activity.
4. Retreiving data from intent and displaying it to the user.
		
`Note: LookupFiles for this project contains video and screeshot of logcat depicting above mentioned features.`
___

# [CustomList](https://github.com/sncvikas/AndroidBasicApps/tree/master/AppSource/CustomList)
Introduces the following topics
1. How to create a custom list adapter for your own UI where each row has ImageView, TextView and an ImageButton
2. Asynctask which runs long operations in the background, not on the main thread, in our app it loads installed
		applications' data such as icon, name, packagename, version
3. Progress bar that we start in asynctask and keep updating it with progress from doInBackground


`Note: LookupFiles for this project contains video and screeshot of logcat depicting above mentioned features.`
___

# [Notes using SQLite](https://github.com/sncvikas/AndroidBasicApps/tree/master/AppSource/NotesAppUsingSqlite)
Introduces the following topics
1. How to make use of Sqlite database to store data
2. Perform database operations such as insert, delete, update, query
3. Show custom dialog that accepts user input (EditText)
4. Listeners when items from the list are clicked

`Note: LookupFiles for this project contains screeshots and main source/resource files for the above mentioned features.`

___


# [FetchMovie - Web API Usage](https://github.com/sncvikas/AndroidBasicApps/tree/master/AppSource/WebApiUsage)
Introduces the following topics
1. How to create an AsyncTaskLoader to perform task in background thread
2. Create HttpUrConnection to connect to webservice to fetch details of a movie such as title year of release, language, genre, ImDb rating
3. Parse output which is in json format to extract desired movie attributes.

`Note: LookupFiles for this project contains screeshots and main source/resource files for the above mentioned features.`

___

# [Magnifier Widget](https://github.com/sncvikas/AndroidBasicApps/tree/master/AppSource/MagnifierOnImageView)
Introduces the following topics
1. How to create an Magnifier widget for an ImageView and TextView
2. Show the magnifier widget whenever the corresponding view is touched.

`Note: LookupFiles for this project contains screeshots and main source/resource files for the above mentioned features.`

___

# [NotificationsSample](https://github.com/sncvikas/AndroidBasicApps/tree/master/AppSource/NotificationsSample)
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

# [AppBarSample With Search Action](https://github.com/sncvikas/AndroidBasicApps/tree/master/AppSource/AppBarSearchSample)
Introduces the appbar/toolbar/actionbar with search action. This app has two activities main acitivty has a list of few quotes, and secondary acitivity shows the list of quotes that were searched from the main activity with a search string. Components used

1. ToolBar in layout xml, menu items in the menu.xml and providing menu click listeners, and searchable resource to indicate that we want to peform search from appbar menu.
2. Assigning a search action to one of the menu items by providing a actionViewClass as SearchView widget. And a creating an activity to listen for android.intent.action.SEARCH action.

`Note: LookupFiles for this project contains screeshots and main source/resource files for the above mentioned features.`
___

# [Dialogs Sample](https://github.com/sncvikas/AndroidBasicApps/tree/master/AppSource/DialogsSample)
DialogsSample project introduces the differnt types of popup dialogs that are supported in Android. The different types used in the app are
1. Show confirmation dialog, for yes or no kind of dialogs
2. Show single choice items dialog, to let user make a choice
3. Show single choice items with default selection
4. Show multi-choice items dialog, to let user select multiple options
5. Dialog with custom layout, Login page hosted in the current activity as a dialog
6. Embed dialog in activity layout, custom layout dialog shown in fullscreen

`Note: LookupFiles for this project contains screeshots and main source/resource files for the above mentioned features.`