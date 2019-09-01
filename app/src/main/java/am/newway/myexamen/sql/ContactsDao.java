package am.newway.myexamen.sql;

import java.util.List;

import am.newway.myexamen.data.Contacts;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ContactsDao
{
    @Query("SELECT * FROM contacts")
    List<Contacts> getAll();

    @Insert
    long insert( Contacts contact );

    @Delete
    void delete( Contacts contact );
}
