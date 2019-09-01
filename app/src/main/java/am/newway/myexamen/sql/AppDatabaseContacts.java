package am.newway.myexamen.sql;

import am.newway.myexamen.data.Contacts;
import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Contacts.class }, version = 1, exportSchema = false)
public abstract class AppDatabaseContacts extends RoomDatabase {
    public abstract ContactsDao getContactsDao();


}

