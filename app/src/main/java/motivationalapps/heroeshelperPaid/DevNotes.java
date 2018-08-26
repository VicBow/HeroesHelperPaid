package motivationalapps.heroeshelperPaid;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class DevNotes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dev_notes);

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean theme = sharedPref.getBoolean(SettingsActivity.KEY_PREF_THEME, false);
        ScrollView wholeView = findViewById(R.id.wholeView);
        LinearLayout mainView = findViewById(R.id.mainView);
        TextView title = findViewById(R.id.dev_title);
        TextView note1 = findViewById(R.id.note1);
        TextView date1 = findViewById(R.id.date1);
        TextView note2 = findViewById(R.id.note2);
        TextView date2 = findViewById(R.id.date2);
        if (!theme) {
            wholeView.setBackgroundColor(getResources().getColor(R.color.background_light));
            mainView.setBackgroundColor(getResources().getColor(R.color.background_light));
            title.setTextColor(getResources().getColor(R.color.text_color_light));
            note1.setTextColor(getResources().getColor(R.color.text_color_light));
            date1.setTextColor(getResources().getColor(R.color.text_color_light));
            note2.setTextColor(getResources().getColor(R.color.text_color_light));
            date2.setTextColor(getResources().getColor(R.color.text_color_light));
        }
        else {
            wholeView.setBackgroundColor(getResources().getColor(R.color.background_dark));
            mainView.setBackgroundColor(getResources().getColor(R.color.background_dark));
            title.setTextColor(getResources().getColor(R.color.text_color_dark));
            note1.setTextColor(getResources().getColor(R.color.text_color_dark));
            date1.setTextColor(getResources().getColor(R.color.text_color_dark));
            note2.setTextColor(getResources().getColor(R.color.text_color_dark));
            date2.setTextColor(getResources().getColor(R.color.text_color_dark));
        }
    }
}
