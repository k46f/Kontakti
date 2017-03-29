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
    }
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
                    mode.finish(); // Action picked, so close the contextual menu
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
}
