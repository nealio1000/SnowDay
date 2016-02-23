package ventureindustries.snowday;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
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

//    public static MonthOfSnow newInstance(String mountain) {
//        MonthOfSnow newMonth = new MonthOfSnow();
//        Bundle args = new Bundle();
//        args.putString("mountain", mountain);
//        newMonth.setArguments(args);
//
//        return newMonth;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//
//        if (savedInstanceState != null) {
//            mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
//        }
//
//        return inflater.inflate(R.layout.fragment_month_of_snow, container, false);
//    }


    @Override
    public void onStart() {
        super.onStart();

        // During startup, check if there are arguments passed to the fragment.
        // onStart is a good place to do this because the layout has already been
        // applied to the fragment at this point so we can safely call the method
        // below that sets the article text.
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
            System.out.println("good connection");
            new PullSnowHistoryTask().execute(stringUrl);
        } else {
            System.out.println("bad connection");
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

                for (int i = 6; i < datedivs.size(); i++)
                    data[0][i - 6] = datedivs.get(i).ownText();

                for (int i = 6; i < dowdivs.size(); i++)
                    data[1][i - 6] = dowdivs.get(i).ownText();

                for (int i = 0, k = 0; i < valuedivs.size(); i++) {
                    if (!Objects.equals(valuedivs.get(i).ownText(), "")) {
                        data[2][k] = valuedivs.get(i).ownText();
                        k++;
                    }
                }

                String[] formattedData = new String[30];
                for (int i = 0; i < 30; i++)
                    formattedData[i] = data[0][i] + "\n" + data[1][i] + "\n" + data[2][i];

                return formattedData;


            } catch (IOException e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(final String[] result) {

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    updateMonthView(result);
                }
            });
        }
    }

}
