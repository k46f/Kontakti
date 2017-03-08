package io.github.k46f.kontakti;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;


/**
 * Vista para los leads del CRM
 */
public class ContactsFragment extends Fragment {

    private ListView contactList;
    private ArrayAdapter<String> contactAdapter;

    public ContactsFragment() {
        // Required empty public constructor
    }

    public static ContactsFragment newInstance(/*parámetros*/) {
        ContactsFragment fragment = new ContactsFragment();
        // Setup parámetros
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Gets parámetros
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_contacts, container, false);

        contactList = (ListView) root.findViewById(R.id.contacts_list);

        String[] leadsNames = {
                "Alexander Pierrot",
                "Carlos Lopez",
                "Sara Bonz",
                "Liliana Clarence",
                "Benito Peralta",
                "Juan Jaramillo",
                "Christian Steps",
                "Alexa Giraldo",
                "Linda Murillo",
                "Alexander Pierrot",
                "Carlos Lopez",
                "Sara Bonz",
                "Liliana Clarence",
                "Benito Peralta",
                "Juan Jaramillo",
                "Christian Steps",
                "Alexa Giraldo",
                "Linda Murillo",
                "Lizeth Astrada"
        };

        DatabaseManager dbm = new DatabaseManager(getContext());
        dbm.openDb();



        contactAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, leadsNames);

        contactList.setAdapter(contactAdapter);

        return root;
    }
}