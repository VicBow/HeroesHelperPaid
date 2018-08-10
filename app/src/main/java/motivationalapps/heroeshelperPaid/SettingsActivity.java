package motivationalapps.heroeshelperPaid;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

    public static final String KEY_PREF_THEME = "theme_switch";
    public static final String KEY_PREF_TRANSPARENT = "transparent_preference";
    public static final String KEY_PREF_HAND = "hand_switch";
    public static final String KEY_PREF_ICON_SIZE = "icon_size";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().replace(android.R.id.content, new SettingsFragment()).commit();
    }
}
