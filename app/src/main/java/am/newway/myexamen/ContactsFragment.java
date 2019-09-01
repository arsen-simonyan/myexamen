package am.newway.myexamen;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import am.newway.myexamen.adapter.ContactsAdapter;
import am.newway.myexamen.data.Contacts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ContactsFragment extends Fragment
{
    private ContactsAdapter mAdapter;
    private OnFragmentInteractionListener mListener;

    public ContactsFragment()
    {
        // Required empty public constructor
    }

    public static ContactsFragment newInstance( String param1 , String param2 )
    {
        ContactsFragment fragment = new ContactsFragment();
        return fragment;
    }

    @Override
    public void onCreate( Bundle savedInstanceState )
    {
        super.onCreate( savedInstanceState );
        if (getArguments() != null)
        {
            //mParam1 = getArguments().getString( ARG_PARAM1 );
            //mParam2 = getArguments().getString( ARG_PARAM2 );
        }
    }

    @Override
    public View onCreateView( LayoutInflater inflater , ViewGroup container ,
                              Bundle savedInstanceState )
    {
        return inflater.inflate( R.layout.fragment_contacts , container , false );
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed( Uri uri )
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction( uri );
        }
    }

    @Override
    public void onAttach( Context context )
    {
        super.onAttach( context );
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException( context.toString()
                    + " must implement OnFragmentInteractionListener" );
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener
    {
        void onFragmentInteraction( Uri uri );
    }

    public void setContact( Contacts contact)
    {
        mAdapter.addContact( contact);
    }

    @Override public void onViewCreated( @NonNull View view , @Nullable Bundle savedInstanceState )
    {
        super.onViewCreated( view , savedInstanceState );

        RecyclerView recyclerView = view.findViewById( R.id.recyclerView );
        RecyclerView.LayoutManager mManager = new LinearLayoutManager( getActivity() , RecyclerView.VERTICAL, false);
        recyclerView.setLayoutManager( mManager );

        mAdapter = new ContactsAdapter( new ArrayList<Contacts>(), getContext());
        recyclerView.setAdapter( mAdapter );

        if( getActivity() != null)
        {
            AsyncTask.execute( new Runnable() {
                @Override
                public void run() {
                    List<Contacts> contacts = App.getInstance().getDatabase().getContactsDao().getAll();
                    for (Contacts contact : contacts)
                    {
                        mAdapter.addContact( contact );
                    }
                }
            });
        }


    }
}
