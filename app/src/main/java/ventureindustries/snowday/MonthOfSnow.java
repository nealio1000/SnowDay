package ventureindustries.snowday;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.widget.ArrayAdapter;

import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 */
public class MonthOfSnow extends ListFragment {

    final static String ARG_POSITION = "position";
    int mCurrentPosition = -1;

    public MonthOfSnow() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        Bundle args = getArguments();

        if (args != null) {
            connect(args.getString("mountain"));
        }
    }

    public void updateMonthView(String[] data) {
        setListAdapter(new ArrayAdapter<>(getActivity(), R.layout.row_layout, data));
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // Save the current article selection in case we need to recreate the fragment
        outState.putInt(ARG_POSITION, mCurrentPosition);
    }


    /**
     * Checks if phone has good connection then calls the PullSnowHistoryTask
     * Async Task to connect to the website
     *
     * @param stringUrl The URL of the location
     */
    public void connect(String stringUrl) {
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            Log.d("CONNECTION INFO: ", "good connection");
            new PullSnowHistoryTask().execute(stringUrl);
        } else {
            Log.d("CONNECTION INFO: ", "bad connection");
        }
    }

    private class PullSnowHistoryTask extends AsyncTask<String, Integer, String[]> {
        @Override
        protected String[] doInBackground(String... urls) {
            String[][] data = new String[3][30];
            // params comes from the execute() call: params[0] is the url.
            try {

                Elements datedivs = Jsoup.connect(urls[0]).get().select("div[class=date]");
                Elements dowdivs = Jsoup.connect(urls[0]).get().select("div[class=dow]");
                Elements valuedivs = Jsoup.connect(urls[0]).get().select("div[class=value], span[class=zero], span[class=noreport ");

                for (int i = 6; i < dowdivs.size(); i++)
                    data[0][i - 6] = dowdivs.get(i).ownText();

                for (int i = 6; i < datedivs.size(); i++)
                    data[1][i - 6] = datedivs.get(i).ownText();

                for (int i = 0, k = 0; i < valuedivs.size(); i++) {
                    if (!Objects.equals(valuedivs.get(i).ownText(), "")) {
                        data[2][k] = valuedivs.get(i).ownText();
                        k++;
                    }
                }

                String[] formattedData = new String[30];
                for (int i = 0; i < 30; i++)
                    formattedData[i] = data[0][i] + ", " + data[1][i] + ": " + data[2][i];

                return formattedData;


            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final String[] result) {
            updateMonthView(result);
        }
    }

}
