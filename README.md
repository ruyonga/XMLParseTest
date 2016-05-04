# XMLParseTest
an Android app that:
1. Gets and parses the XML downloads list from the 1st URL, and extracts the item IDs.
2. Then, In parallel, downloads and parses the XML for each of these item IDs using the second URL. 
3. Display the downloaded phrases in the UI, as they are successfully downloaded.


        minSdkVersion 14
        targetSdkVersion 23
        versionCode 1
        versionName "1.0"

Libraries:
Drawable Text: com.amulyakhare:com.amulyakhare.textdrawable:1.0.
Cardview: com.android.support:cardview-v7:23.3.0
EventBus: de.greenrobot:eventbus:3.0.0-beta1
JobQues: com.path:android-priority-jobqueue:1.1.2

