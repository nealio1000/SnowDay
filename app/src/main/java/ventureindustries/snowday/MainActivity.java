package ventureindustries.snowday;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends FragmentActivity implements HomeTurf.OnMountainSelectedListener
{
    private HomeTurf ht;
    private MonthOfSnow month;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check that the activity is using FrameLayout
        if (findViewById(R.id.list_fragment_container) != null
                && findViewById(R.id.month_fragment_container) != null) {


            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }

            // Create a the two fragments needed for MainActivity
            ht = new HomeTurf();
            month = new MonthOfSnow();

            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction transaction = fragmentManager.beginTransaction();

            transaction.add(R.id.list_fragment_container, ht);
            transaction.commit();
        }
    }

    public String getMountainUrl(String mountain) {
        String mountainUrl = "http://www.opensnow.com/location/";

        switch (mountain) {
            case "Copper Mountain":
                mountainUrl += "copper";
                return mountainUrl;
            case "Winter Park":
                mountainUrl += "winterpark";
                return mountainUrl;
            case "Steamboat":
                mountainUrl += "steamboat";
                return mountainUrl;
            case "Wolf Creek":
                mountainUrl += "wolfcreekcolorado";
                return mountainUrl;
            case "Monarch":
                mountainUrl += "monarch";
                return mountainUrl;
            case "Silverton":
                mountainUrl += "silverton";
                return mountainUrl;
            case "Purgatory":
                mountainUrl += "purgatory";
                return mountainUrl;
            case "Breckenridge":
                mountainUrl += "breckenridge";
                return mountainUrl;
            case "Telluride":
                mountainUrl += "telluride";
                return mountainUrl;
            case "Keystone":
                mountainUrl += "keystone";
                return mountainUrl;
            case "Arapahoe Basin":
                mountainUrl += "arapahoebasin";
                return mountainUrl;
            case "Vail":
                mountainUrl += "vail";
                return mountainUrl;
            case "Beaver Creek":
                mountainUrl += "beavercreek";
                return mountainUrl;
            case "Crested Butte":
                mountainUrl += "crestedbutte";
                return mountainUrl;
            case "Loveland":
                mountainUrl += "loveland";
                return mountainUrl;
        }
        return "Mountain not found";
    }

    @Override
    public void onMountainSelected(int position, String mountain) {
        String url = getMountainUrl(mountain);
        month = new MonthOfSnow();
        Bundle args = new Bundle();
        args.putString("mountain", url);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        month.setArguments(args);
        transaction.replace(R.id.month_fragment_container, month);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
