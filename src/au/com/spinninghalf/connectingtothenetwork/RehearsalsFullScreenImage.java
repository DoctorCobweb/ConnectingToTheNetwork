package au.com.spinninghalf.connectingtothenetwork;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;


public class RehearsalsFullScreenImage extends Activity {
	public int thumbnailId;
	public int position;
	
	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rehearsals_full_screen_image);
        
        //get the reference to the ImageView
        ImageView mainImageView = (ImageView) findViewById(R.id.rehearsalsFullScreenImageView);
        
        
        //get the extra info from the intent
        Bundle extras = getIntent().getExtras();
        
        //get the id of the thumbnail selected
        thumbnailId = extras.getInt(RehearsalsFragmentOne.ARG_SELECTED_IMAGE);
        
        //get the position in the GridView of the thumbnail selected.
        //position is used in a string array containing the photo blurbs which
        //are displayed in toast messages.
        position = extras.getInt(RehearsalsFragmentOne.ARG_SELECTED_IMAGE_POSITION);
        
        //get the string array resource containing the photo descriptions
        //then convert it to an actual string array.
        final String[] photoLabels = getResources().getStringArray(R.array.rehearsalsPhotoLabels);
        
        //set the ImageView as the image corresponding to the thumbnail selected.
        mainImageView.setImageResource(thumbnailId);
        
        //make the toast message and display the photo blurn inside it.
        Toast.makeText(RehearsalsFullScreenImage.this, photoLabels[position], Toast.LENGTH_SHORT).show();
        
        
        /*
        //trying out status notifications...
        //get a reference to the NotificationManager
        String ns = Context.NOTIFICATION_SERVICE;
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
        
        //instantiate the notification
        int icon = R.drawable.ic_spinninghalf_notification;
        CharSequence tickerText = "Spinning Half Notification"; //this is what is displayed in header up top in status bar to alert user.
        long when = System.currentTimeMillis();
        
        Notification notification = new Notification(icon, tickerText, when);
        
        //define the notification's message and PendingIntent.
        Context context = getApplicationContext();
        CharSequence contentTitle = "Spinning Half";
        CharSequence contentText = "These are some of our selected photos of the rehearsal rooms.";
        Intent notificationIntent = new Intent();
        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        	
        notification.setLatestEventInfo(context, contentTitle, photoLabels[position], contentIntent);
        
        //pass the Notification to the NotificationManger. Voila.
        final int HELLO_ID = 1; //just a int for identification of the notification.
        mNotificationManager.notify(HELLO_ID, notification);
        */
    }
    
}