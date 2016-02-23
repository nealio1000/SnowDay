package ventureindustries.snowday;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the
 * {@link OnMountainSelectedListener}
 * interface.
 */
public class HomeTurf extends ListFragment {

    // TODO: Customize parameter argument names
    private static final String ARG_COLUMN_COUNT = "column-count";
    private OnMountainSelectedListener mMountainSelectedListener;
    private String[] mountains = {
            "Copper Mountain", "Winter Park", "Steamboat",
            "Wolf Creek", "Monarch", "Silverton", "Purgatory",
            "Breckenridge", "Telluride", "Keystone", "Arapahoe Basin",
            "Vail", "Beaver Creek", "Crested Butte", "Loveland"
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HomeTurf() {
    }

    // TODO: Customize parameter initialization
    @SuppressWarnings("unused")
    public static HomeTurf newInstance() {
        HomeTurf fragment = new HomeTurf();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setListAdapter(new ArrayAdapter<>(getActivity(), R.layout.row_layout, mountains));
    }

    @Override
    public void onStart() {
        super.onStart();

        // When in two-pane layout, set the listview to highlight the selected list item
        // (We do this during onStart because at the point the listview is available.)
        if (getFragmentManager().findFragmentById(R.id.list) != null) {
            getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnMountainSelectedListener) {
            mMountainSelectedListener = (OnMountainSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mMountainSelectedListener = null;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {

        String selectedValue = (String) getListAdapter().getItem(position);

        // Notify the parent activity of selected item
        mMountainSelectedListener.onMountainSelected(position, selectedValue);

        // Set the item as checked to be highlighted when in two-pane layout
        getListView().setItemChecked(position, true);
    }

    public interface OnMountainSelectedListener {
        /**
         * Called by HomeTurf fragment when a list item is selected
         */
        void onMountainSelected(int position, String mountain);
    }
}
