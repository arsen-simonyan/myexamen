package am.newway.myexamen.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

import am.newway.myexamen.App;
import am.newway.myexamen.MainActivity;
import am.newway.myexamen.R;
import am.newway.myexamen.data.Contacts;
import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.MyViewHolder> {
    private List<Contacts> list;
    private Context context;

    public ContactsAdapter( List<Contacts> list, Context context )
    {
        this.list = list;
        this.context = context;
    }

    public Contacts getItem(int position)
    {
        return list.get(position);
    }

    public void addContact( Contacts contact)
    {
        list.add( contact );
        notifyItemInserted( list.size() - 1 );
    }

    private void removeTasks( final int position )
    {
        AsyncTask.execute( new Runnable() {
            @Override
            public void run() {
                if(list.size() > 0 && position < list.size())
                App.getInstance().getDatabase().getContactsDao().delete(list.get(position));
            }
        });
        list.remove( position );
        notifyItemRemoved( position );
    }

    @NonNull @Override public MyViewHolder onCreateViewHolder( @NonNull ViewGroup viewGroup , int i )
    {
        View view = LayoutInflater.from( viewGroup.getContext() ).inflate(
                R.layout.item_layout , viewGroup , false );
        MyViewHolder holder = new MyViewHolder( view );
        holder.textFirstName.setText( "" );
        holder.textPhoneNumber.setText( "" );
        return holder;
    }

    @Override public void onBindViewHolder( @NonNull MyViewHolder myViewHolder , int i )
    {
        myViewHolder.bind( list.get( i ) );
    }

    @Override public int getItemCount()
    {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView textFirstName;
        TextView textPhoneNumber;

        ImageView imageView;
        ImageView deleteImage;
        ImageView callImage;
        ConstraintLayout rl;
        Context context;

        MyViewHolder( View viewItem )
        {
            super( viewItem );
            textFirstName = viewItem.findViewById( R.id.textFirstName );
            textPhoneNumber= viewItem.findViewById( R.id.textPhoneNumber);

            imageView = viewItem.findViewById( R.id.image );
            deleteImage = viewItem.findViewById( R.id.deleteImage );
            callImage = viewItem.findViewById( R.id.callImage);
            rl = viewItem.findViewById( R.id.root );
            context = viewItem.getContext();

            deleteImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v) {
                    AlertDialog.Builder dlg = new AlertDialog.Builder(context)
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick( DialogInterface dialog, int which) {

                                }
                            })
                            .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick( DialogInterface dialog, int which) {
                                    removeTasks(getAdapterPosition());
                                }
                            })
                            .setTitle("Warning")
                            .setMessage( String.format("Do you want to delete  \"%s\"  item.\n Are you sure", textFirstName.getText().toString()));
                    dlg.show();
                }
            });

            callImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick( View v) {

                    if (context.getClass().equals(MainActivity.class)) {
                        ((MainActivity) context).callAction(textPhoneNumber.getText().toString());
                    }
                }
            });
        }

        void bind( Contacts contact )
        {
            textFirstName.setText( String.format( "%s %s", contact.getFirstName(), contact.getLastName() ) );
            textPhoneNumber.setText( contact.getPhoneNumber() );
            Uri uri = Uri.parse( contact.getImageUri() );

            //Glide.with( context ).load( uri ).into( imageView );

            Glide
                    .with(context)
                    .load(uri)
                    .centerCrop()
                    .placeholder(R.drawable.document)
                    .into(imageView);
        }
    }
}

