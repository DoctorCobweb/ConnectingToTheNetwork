package au.com.spinninghalf.connectingtothenetwork;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

public class ViewGig extends Activity 
{
   private long rowID; // selected contact's name
   private TextView showTextView; // displays contact's name 
   private TextView dateTextView; // displays contact's phone
   private TextView descriptionTextView; // displays contact's email
   private TextView priceTextView; // displays contact's street
   private TextView tixUrlTextView; // displays contact's city/state/zip

   // called when the activity is first created
   @Override
   public void onCreate(Bundle savedInstanceState) 
   {
      super.onCreate(savedInstanceState);
      setContentView(R.layout.view_gig);

      // get the TextViews
      showTextView = (TextView) findViewById(R.id.showTextView);
      dateTextView = (TextView) findViewById(R.id.dateTextView);
      descriptionTextView = (TextView) findViewById(R.id.descriptionTextView);
      priceTextView = (TextView) findViewById(R.id.priceTextView);
      tixUrlTextView = (TextView) findViewById(R.id.tixUrlTextView);
      
      // get the selected contact's row ID
      Bundle extras = getIntent().getExtras();
      rowID = extras.getLong(GigListActivity.ROW_ID); 
   } // end method onCreate

   // called when the activity is first created
   @Override
   protected void onResume()
   {
      super.onResume();
      
      // create new LoadContactTask and execute it 
      new LoadContactTask().execute(rowID);
   } // end method onResume
   
   // performs database query outside GUI thread
   private class LoadContactTask extends AsyncTask<Long, Object, Cursor> 
   {
      DatabaseConnector databaseConnector = 
         new DatabaseConnector(ViewGig.this);

      // perform the database access
      @Override
      protected Cursor doInBackground(Long... params)
      {
         databaseConnector.open();
         
         // get a cursor containing all data on given entry
         return databaseConnector.getOneContact(params[0]);
      } // end method doInBackground

      // use the Cursor returned from the doInBackground method
      @Override
      protected void onPostExecute(Cursor result)
      {
         super.onPostExecute(result);
   
         result.moveToFirst(); // move to the first item 
   
         // get the column index for each data item
         int showIndex = result.getColumnIndex("show");
         int dateIndex = result.getColumnIndex("date");
         int descriptionIndex = result.getColumnIndex("description");
         int priceIndex = result.getColumnIndex("price");
         int tixUrlIndex = result.getColumnIndex("tixUrl");
   
         // fill TextViews with the retrieved data
         showTextView.setText(result.getString(showIndex));
         dateTextView.setText(result.getString(dateIndex));
         descriptionTextView.setText(result.getString(descriptionIndex));
         priceTextView.setText(result.getString(priceIndex));
         tixUrlTextView.setText(result.getString(tixUrlIndex));
   
         result.close(); // close the result cursor
         databaseConnector.close(); // close database connection
      } // end method onPostExecute
   } // end class LoadContactTask
 

} // end class ViewContact

