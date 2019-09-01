package am.newway.myexamen.data;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts")
public class Contacts
{
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String addressThis;
    private String imageUri;

    public Contacts( String firstName , String lastName , String phoneNumber , String addressThis , String imageUri )
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.addressThis = addressThis;
        this.imageUri = imageUri;
    }

    public int getId()
    {
        return id;
    }

    public void setId( int id )
    {
        this.id = id;
    }

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName( String firstName )
    {
        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName( String lastName )
    {
        this.lastName = lastName;
    }

    public String getPhoneNumber()
    {
        return phoneNumber;
    }

    public void setPhoneNumber( String phoneNumber )
    {
        this.phoneNumber = phoneNumber;
    }

    public String getAddressThis()
    {
        return addressThis;
    }

    public void setAddressThis( String addressThis )
    {
        this.addressThis = addressThis;
    }

    public String getImageUri()
    {
        return imageUri;
    }

    public void setImageUri( String imageUri )
    {
        this.imageUri = imageUri;
    }
}
