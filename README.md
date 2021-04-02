# facial-recognition-via-passport-reader

This project enables facial recognition by using the biometric photo registered in the passport. After installing the mobile application on your phone, you can run it so that you can read your passport that has an RFID chip. You can use Android Studio for this. For the mobile application, the tananaev/passport-reader repository was used by making necessary changes to it. Changes made are usually inside ResultActivity.java and ImageUtil.java files.
 
As soon as the passport is read by the mobile application, an HTML file will be created in your directory. This HTML file allows you to send your Base64 encrypted photo to the local host via a form. For localhost, you can run the my_server.py file on your computer and create a simple server this way. After submitting the form, face recognition can be performed with the face_recognition.py script.You should install the face_recognition library in your environment to run this script.
