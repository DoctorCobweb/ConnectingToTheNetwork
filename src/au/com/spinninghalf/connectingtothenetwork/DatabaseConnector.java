package au.com.spinninghalf.connectingtothenetwork;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;


public class DatabaseConnector 
{
   private static final String TAG = "DatabaseConnector";	
   private static final String DATABASE_NAME = "GigList";
   private static final String GIGS_TABLE = "gigs";
   private SQLiteDatabase database; // database object
   private DatabaseOpenHelper databaseOpenHelper; // database helper
   public boolean databaseOpen = false;

   // public constructor for DatabaseConnector
   public DatabaseConnector(Context context) 
   {
      databaseOpenHelper = 
         new DatabaseOpenHelper(context, DATABASE_NAME, null, 1);
   } 

   // open the database connection
   public void open() throws SQLException 
   {
      // create or open a database for reading/writing
      database = databaseOpenHelper.getWritableDatabase();
   } 

   // close the database connection
   public void close() 
   {
      if (database != null) {
         database.close(); // close the database connection
      }
      
      if (database != null && database.isOpen()) {
    	  Log.d(TAG, "in close() + ******DATABASE IS NOT CLOSED****");
    	  this.databaseOpen = true;
      }
      Log.d(TAG, "in close().");
   } 

   // inserts a new gig in the database
   public void insertGig(String _gig_id, 
		   				 String _author, 
		   				 String _show, 
		   				 String _date, 
		   				 String _description, 
		   				 String _price, 
		   				 String _tixUrl) 
   {
      ContentValues newGig = new ContentValues();
      open(); // open the database
      
      newGig.put("gig_id", _gig_id);
      newGig.put("author", _author);
      newGig.put("show", _show);
      newGig.put("date", _date);
      newGig.put("description", _description);
      newGig.put("price", _price);
      newGig.put("tixUrl", _tixUrl);	  
    	  
      database.insert(GIGS_TABLE, null, newGig);
      //close(); // close the database
   }

   // inserts a new gig in the database
   public void updateGig(long id,
		   		         String _gig_id, //returns a Cursor containing  {_id, shows} columns
				         String _author, 
				         String _show, 
				         String _date, 
				         String _description, 
				         String _price, 
				         String _tixUrl) 
   {
      ContentValues editGig = new ContentValues();
      editGig.put("gig_id", _gig_id);
      editGig.put("author", _author);
      editGig.put("show", _show);
      editGig.put("date", _date);
      editGig.put("description", _description);
      editGig.put("price", _price);
      editGig.put("tixUrl", _tixUrl);

      open(); // open the database
      database.update(GIGS_TABLE, editGig, "_id=" + id, null);
      //close(); // close the database
   } 

   //return a Cursor with all contact information in the database
   //only having the _id and show columns
   public Cursor getAllGigs() 
   {
	   open();
      return database.query(GIGS_TABLE, new String[] {"_id", "show"}, 
         null, null, null, null, "show");
   } 

   // get a Cursor containing all information about the gig specified
   // by the given id
   public Cursor getOneContact(long id) 
   {
      return database.query(
    		  GIGS_TABLE, null, "_id=" + id, null, null, null, null);
   }
   
   public Cursor getGigIdColumnVals() {
	   open();
	   return database.query(GIGS_TABLE, new String[] {"gig_id"}, null, null, null, null, null);
	   		
   }

   // delete the gig specified by the given String name
   public void deleteGig(long id) 
   {
      open(); // open the database
      database.delete(GIGS_TABLE, "_id=" + id, null);
      //close(); // close the database
   } 
   
   public void deleteAll() {
	   open();
	   database.delete(GIGS_TABLE, null, null);
	   //close();
   }
   
   public Cursor getErrorMsgInCursorForm(String error) {
	   Log.d(TAG, "in getErrorMsgInCursorForm.");
	   insertGig(error, error, error, error, error, error, error);
	   
	   return database.query(GIGS_TABLE, null, "gig_id=" + error, null, null, null, null);
   }
   
   private class DatabaseOpenHelper extends SQLiteOpenHelper 
   {
      // public constructor
      public DatabaseOpenHelper(Context context, String name,
         CursorFactory factory, int version) 
      {
         super(context, name, factory, version);
      } 

      // creates the 'gigs' table when the database is created
      @Override
      public void onCreate(SQLiteDatabase db) 
      {
         // query to create a new table named contacts
         String createQuery = "CREATE TABLE " + GIGS_TABLE +
            "(_id integer primary key autoincrement," +
            "gig_id TEXT, author TEXT, show TEXT, date TEXT, description TEXT," +
            "price TEXT, tixUrl TEXT);";
                  
         db.execSQL(createQuery); // execute the query
      } 

      @Override
      public void onUpgrade(SQLiteDatabase db, int oldVersion, 
          int newVersion) 
      {
      } 
   }
} 