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
 
/*
   
   // create the Activity's menu from a menu resource XML file
   @Override
   public boolean onCreateOptionsMenu(Menu menu) 
   {
      super.onCreateOptionsMenu(menu);
      MenuInflater inflater = getMenuInflater();
      inflater.inflate(R.menu.view_contact_menu, menu);
      return true;
   } // end method onCreateOptionsMenu
   
   // handle choice from options menu
   @Override
   public boolean onOptionsItemSelected(MenuItem item) 
   {
      switch (item.getItemId()) // switch based on selected MenuItem's ID
      {
         case R.id.editItem:
            // create an Intent to launch the AddEditContact Activity
            Intent addEditContact =
               new Intent(this, AddEditContact.class);
            
            // pass the selected contact's data as extras with the Intent
            addEditContact.putExtra(AddressBook.ROW_ID, rowID);
            addEditContact.putExtra("name", nameTextView.getText());
            addEditContact.putExtra("phone", phoneTextView.getText());
            addEditContact.putExtra("email", emailTextView.getText());
            addEditContact.putExtra("street", streetTextView.getText());
            addEditContact.putExtra("city", cityTextView.getText());
            startActivity(addEditContact); // start the Activity
            return true;
         case R.id.deleteItem:
            deleteContact(); // delete the displayed contact
            return true;
         default:
            return super.onOptionsItemSelected(item);
      } // end switch
   } // end method onOptionsItemSelected
   
   // delete a contact
   private void deleteContact()
   {
      // create a new AlertDialog Builder
      AlertDialog.Builder builder = 
         new AlertDialog.Builder(ViewContact.this);

      builder.setTitle(R.string.confirmTitle); // title bar string
      builder.setMessage(R.string.confirmMessage); // message to display

      // provide an OK button that simply dismisses the dialog
      builder.setPositiveButton(R.string.button_delete,
         new DialogInterface.OnClickListener()
         {
            @Override
            public void onClick(DialogInterface dialog, int button)
            {
               final DatabaseConnector databaseConnector = 
                  new DatabaseConnector(ViewContact.this);

               // create an AsyncTask that deletes the contact in another 
               // thread, then calls finish after the deletion
               AsyncTask<Long, Object, Object> deleteTask =
                  new AsyncTask<Long, Object, Object>()
                  {
                     @Override
                     protected Object doInBackground(Long... params)
                     {
                        databaseConnector.deleteContact(params[0]); 
                        return null;
                     } // end method doInBackground

                     @Override
                     protected void onPostExecute(Object result)
                     {
                        finish(); // return to the AddressBook Activity
                     } // end method onPostExecute
                  }; // end new AsyncTask

               // execute the AsyncTask to delete contact at rowID
               deleteTask.execute(new Long[] { rowID });               
            } // end method onClick
         } // end anonymous inner class
      ); // end call to method setPositiveButton
      
      builder.setNegativeButton(R.string.button_cancel, null);
      builder.show(); // display the Dialog
   } // end method deleteContact
*/
} // end class ViewContact

