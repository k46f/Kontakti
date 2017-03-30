package io.github.k46f.kontakti;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivityDeleteBar {

    private String contact_id;
    private View view;

    MainActivityDeleteBar(String contactId, View v){
        contact_id = contactId;
        view = v;
        // set null or default listener or accept as argument to constructor
        this.listener = null;
    }

    // Step 1 - This interface defines the type of messages I want to communicate to my owner
    public interface DeleteButtonListener {
        // These methods are the different events and
        // need to pass relevant arguments related to the event triggered
        public void onButtonPressed(String id);
    }

    // Step 2 - This variable represents the listener passed in by the owning object
    // The listener must implement the events interface and passes messages up to the parent.
    private DeleteButtonListener listener;

    // Tracks current contextual action mode
    private ActionMode currentActionMode;

    private final static String TITLE = "Delete";

    // Called when the user long-clicks on someView
    public boolean onLongClick() {
        if (currentActionMode != null) {
            return false;
        }
        view.startActionMode(modeCallBack);
        view.setSelected(true);
        return true;
    }

    // Define the callback when ActionMode is activated
    private ActionMode.Callback modeCallBack = new ActionMode.Callback() {
        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            mode.setTitle(TITLE);
            mode.getMenuInflater().inflate(R.menu.menu_main_delete, menu);
            return true;
        }

        // Called each time the action mode is shown.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false; // Return false if nothing is done
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.deleteMainButton:
                    // Trigger the deletion here
                    listener.onButtonPressed(contact_id); // <---- fire listener here
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            currentActionMode = null; // Clear current action mode
        }
    };

    // Assign the listener implementing events interface that will receive the events
    public void setCustomObjectListener(DeleteButtonListener listener) {
        this.listener = listener;
    }
}
