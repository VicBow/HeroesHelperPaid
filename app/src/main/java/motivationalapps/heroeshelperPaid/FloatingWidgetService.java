package motivationalapps.heroeshelperPaid;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class FloatingWidgetService extends Service implements View.OnClickListener {
    private WindowManager mWindowManager;
    private WindowManager.LayoutParams wrapParams;
    private WindowManager.LayoutParams matchParams;
    private View mFloatingWidgetView, collapsedView, expandedView, infoView, buildView;
    private String floatWidth;
    private Point szWindow = new Point();
    private LayoutInflater inflater;
    private ArrayAdapter ratingAdapter;
    private ArrayAdapter weaponAdapter;
    private ArrayAdapter colorAdapter;
    private ArrayAdapter heroAdapter;
    private Spinner ratingSpinner;
    private Spinner weaponSpinner;
    private Spinner colorSpinner;
    private int rating;
    private Boolean isEquipped;
    private String characterEquipped;
    private String color;
    private Spinner heroSpinner;
    private int levelSelected;
    private String characterLevel;
    private String character;
    private String characterRating;
    List<String[]> fiveHeroesList;
    List<String[]> fourHeroesList;
    List<String[]> threeHeroesList;
    List<String> fiveStarBlueList;
    List<String> fiveStarGreenList;
    List<String> fiveStarRedList;
    List<String> fiveStarColorlessList;
    List<String> fourStarBlueList;
    List<String> fourStarGreenList;
    List<String> fourStarRedList;
    List<String> fourStarColorlessList;
    List<String> threeStarBlueList;
    List<String> threeStarGreenList;
    List<String> threeStarRedList;
    List<String> threeStarColorlessList;
    private int index;
    private TextView characterNameInfo;
    private TextView characterRatingInfo;
    private TextView characterEquippedInfo;
    private TextView characterLevelInfo;
    private TextView hpLow;
    private TextView atkLow;
    private TextView spdLow;
    private TextView defLow;
    private TextView resLow;
    private TextView hpMid;
    private TextView atkMid;
    private TextView spdMid;
    private TextView defMid;
    private TextView resMid;
    private TextView hpHi;
    private TextView atkHi;
    private TextView spdHi;
    private TextView defHi;
    private TextView resHi;

    private int x_init_cord, y_init_cord, x_init_margin, y_init_margin, starting_x;

    Boolean theme, hand;
    int spinnerNum;
    SharedPreferences sharedPref;
    String transparentNum;

    //Variable to check if the Floating widget view is on left side or in right side
    // initially we are displaying Floating widget view to Left side so set it to true

    public FloatingWidgetService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return null;
    }

    @Override
    public void onCreate() {
        sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        theme = sharedPref.getBoolean(SettingsActivity.KEY_PREF_THEME, false);
        if (!theme) spinnerNum = R.layout.spinner_item;
        else spinnerNum = R.layout.spinner_item_dark;

        transparentNum = sharedPref.getString(SettingsActivity.KEY_PREF_TRANSPARENT, "1");
        hand = sharedPref.getBoolean(SettingsActivity.KEY_PREF_HAND, false);

        super.onCreate();



        //Set layout width to wrap so the layout can move around
        floatWidth = "wrap";
        String line; //the line to be read in from csv files

        //Create ArrayLists for 3, 4, and 5 star lists of each color
        fiveHeroesList = new ArrayList<>();
        fourHeroesList = new ArrayList<>();
        threeHeroesList = new ArrayList<>();
        fiveStarBlueList = new ArrayList<>();
        fiveStarColorlessList = new ArrayList<>();
        fiveStarRedList = new ArrayList<>();
        fiveStarGreenList = new ArrayList<>();
        fourStarBlueList = new ArrayList<>();
        fourStarColorlessList = new ArrayList<>();
        fourStarRedList = new ArrayList<>();
        fourStarGreenList = new ArrayList<>();
        threeStarBlueList = new ArrayList<>();
        threeStarColorlessList = new ArrayList<>();
        threeStarRedList = new ArrayList<>();
        threeStarGreenList = new ArrayList<>();

        //Create the input stream, and read the lines individually to sort them into their proper ArrayList for future use
        try {
            InputStreamReader in = new InputStreamReader(getAssets().open("FEH5.csv"));
            InputStreamReader in4 = new InputStreamReader(getAssets().open("FEH4.csv"));
            InputStreamReader in3 = new InputStreamReader(getAssets().open("FEH3.csv"));
            BufferedReader reader = new BufferedReader(in);
            BufferedReader reader4 = new BufferedReader(in4);
            BufferedReader reader3 = new BufferedReader(in3);
            //Add all five star characters from csv to string array list for data collection and
            //compare color to possible columns to check if character should be in list for spinner
            while ((line = reader.readLine()) != null) {
                String[] row = line.split(",");
                fiveHeroesList.add(row);
                switch (row[3]) {
                    case "Blue":
                        fiveStarBlueList.add(row[0]);
                        break;
                    case "Green":
                        fiveStarGreenList.add(row[0]);
                        break;
                    case "Red":
                        fiveStarRedList.add(row[0]);
                        break;
                    case "Colorless":
                        fiveStarColorlessList.add(row[0]);
                        break;
                }
            }
            in.close();
            reader.close();

            //Add all four star characters from csv to string array list for data collection and
            //compare color to possible columns to check if character should be in list for spinner
            while ((line = reader4.readLine()) != null) {
                String[] row = line.split(",");
                fourHeroesList.add(row);
                if (row[3].equals("Blue") && row[40].equals("1"))
                    fourStarBlueList.add(row[0]);
                else if (row[3].equals("Green") && row[40].equals("1"))
                    fourStarGreenList.add(row[0]);
                else if (row[3].equals("Red") && row[40].equals("1"))
                    fourStarRedList.add(row[0]);
                else if (row[3].equals("Colorless") && row[40].equals("1"))
                    fourStarColorlessList.add(row[0]);
            }
            in4.close();
            reader4.close();

            //Add all three star characters from csv to string array list for data collection and
            //compare color to possible columns to check if character should be in list for spinner
            while ((line = reader3.readLine()) != null) {
                String[] row = line.split(",");
                threeHeroesList.add(row);
                if (row[3].equals("Blue") && row[40].equals("1"))
                    threeStarBlueList.add(row[0]);
                else if (row[3].equals("Green") && row[40].equals("1"))
                    threeStarGreenList.add(row[0]);
                else if (row[3].equals("Red") && row[40].equals("1"))
                    threeStarRedList.add(row[0]);
                else if (row[3].equals("Colorless") && row[40].equals("1"))
                    threeStarColorlessList.add(row[0]);
            }
            in3.close();
            reader3.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        //init WindowManager and set layout type based on build version
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        int layoutType;
        getWindowManagerDefaultDisplay();
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            layoutType = WindowManager.LayoutParams.TYPE_PHONE;
        } else {
            layoutType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

        //Init LayoutInflater
        //wrap params for the small icon to be moved around
        //match_parent for width in order to have expanded layouts to match device width
        inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        wrapParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        matchParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutType,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        addFloatingWidgetView(inflater);

        changeTransparency();

        //method to implement the on click listeners
        implementClickListeners();
        implementTouchListenerToFloatingWidgetView();


    }


    /*  Add Floating Widget View to Window Manager  */
    @TargetApi(26)
    private void addFloatingWidgetView(LayoutInflater inflater) {
        //Inflate the floating view layout we created
        mFloatingWidgetView = inflater.inflate(R.layout.floating_widget_layout, null);

        //Specify the view position
        wrapParams.gravity = Gravity.TOP | Gravity.START;

        Point size = new Point();
        mWindowManager.getDefaultDisplay().getSize(size);
        if (hand) //They are right handed
            starting_x = 0;

        else starting_x = size.x;

        //Initially view will be added to top-left corner, you change x-y coordinates according to your need
        wrapParams.x = starting_x;
        wrapParams.y = 800;

        //Add the view to the window
        mWindowManager.addView(mFloatingWidgetView, wrapParams);

        //find id of collapsed view layout
        collapsedView = mFloatingWidgetView.findViewById(R.id.collapse_view);

        //find id of the expanded view layout
        expandedView = mFloatingWidgetView.findViewById(R.id.expanded_container);

        //find id of the info creation layout
        infoView = mFloatingWidgetView.findViewById(R.id.content_container);

        //find id of the recommended build layout
        buildView = mFloatingWidgetView.findViewById(R.id.build_container);
        addTextViews();

    }

    //Commented out code to check for anything less than Honeycomb
    private void getWindowManagerDefaultDisplay() {
        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2)
            mWindowManager.getDefaultDisplay().getSize(szWindow);
        /*else {
            int w = mWindowManager.getDefaultDisplay().getWidth();
            int h = mWindowManager.getDefaultDisplay().getHeight();
            szWindow.set(w, h);
        }*/
    }

    /*  Implement Touch Listener to Floating Widget Root View  */
    private void implementTouchListenerToFloatingWidgetView() {
        //Drag and move floating view using user's touch action.
        mFloatingWidgetView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {

            long time_start = 0, time_end = 0;

            boolean isLongClick = false;//variable to judge if user click long press

            Handler handler_longClick = new Handler();
            Runnable runnable_longClick = new Runnable() {
                @Override
                public void run() {
                    //Set isLongClick as true
                    isLongClick = true;
                }
            };

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                //Get Floating widget view params
                WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) mFloatingWidgetView.getLayoutParams();

                //get the touch location coordinates
                int x_cord = (int) event.getRawX();
                int y_cord = (int) event.getRawY();

                int x_cord_Destination, y_cord_Destination;

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        time_start = System.currentTimeMillis();

                        handler_longClick.postDelayed(runnable_longClick, 600);


                        x_init_cord = x_cord;
                        y_init_cord = y_cord;

                        //remember the initial position.
                        x_init_margin = layoutParams.x;
                        y_init_margin = layoutParams.y;

                        return true;
                    case MotionEvent.ACTION_UP:
                        isLongClick = false;

                        handler_longClick.removeCallbacks(runnable_longClick);

                        //Get the difference between initial coordinate and current coordinate
                        int x_diff = x_cord - x_init_cord;
                        int y_diff = y_cord - y_init_cord;

                        //The check for x_diff <5 && y_diff< 5 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Math.abs(x_diff) < 5 && Math.abs(y_diff) < 5) {
                            time_end = System.currentTimeMillis();

                            //Also check the difference between start time and end time should be less than 300ms
                            if ((time_end - time_start) < 300)
                                onFloatingWidgetClick();

                        }

                        y_cord_Destination = y_init_margin + y_diff;

                        int barHeight = getStatusBarHeight();
                        if (y_cord_Destination < 0) {
                            y_cord_Destination = 0;
                        } else if (y_cord_Destination + (mFloatingWidgetView.getHeight() + barHeight) > szWindow.y) {
                            y_cord_Destination = szWindow.y - (mFloatingWidgetView.getHeight() + barHeight);
                        }

                        layoutParams.y = y_cord_Destination;


                        return true;
                    case MotionEvent.ACTION_MOVE:
                        int x_diff_move = x_cord - x_init_cord;
                        int y_diff_move = y_cord - y_init_cord;

                        x_cord_Destination = x_init_margin + x_diff_move;
                        y_cord_Destination = y_init_margin + y_diff_move;


                        layoutParams.x = x_cord_Destination;
                        layoutParams.y = y_cord_Destination;

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingWidgetView, layoutParams);
                        return true;
                }
                return false;
            }
        });
    }

    private void implementClickListeners() {
        mFloatingWidgetView.findViewById(R.id.close_floating_view).setOnClickListener(this);
        mFloatingWidgetView.findViewById(R.id.close_expanded_view).setOnClickListener(this);
        //mFloatingWidgetView.findViewById(R.id.open_activity_button).setOnClickListener(this);
        mFloatingWidgetView.findViewById(R.id.info_button).setOnClickListener(this);
        mFloatingWidgetView.findViewById(R.id.close_content_view).setOnClickListener(this);
        //mFloatingWidgetView.findViewById(R.id.build_view).setOnClickListener(this);
        mFloatingWidgetView.findViewById(R.id.close_build_view).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.close_floating_view:
                //close the service and remove the from from the window
                stopSelf();
                break;
            case R.id.close_expanded_view:
                //Close the expanded view and make the collapsed view visible again. And change parameters for window view
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
                mWindowManager.updateViewLayout(mFloatingWidgetView, wrapParams);
                break;
            /*case R.id.open_activity_button:
                //open the activity and stop service
                //setCharacterInfo();
                Intent intent = new Intent(FloatingWidgetService.this, UnitDisplay.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                expandedView.setVisibility(View.GONE);
                collapsedView.setVisibility(View.VISIBLE);
                mWindowManager.updateViewLayout(mFloatingWidgetView, wrapParams);
                startActivity(intent);
                //close the service and remove view from the view hierarchy
                //stopSelf();
                break;
            */
            case R.id.info_button:
                setColorScheme();
                //Close the expanded view and make the infoView visible and then set the character info for display
                expandedView.setVisibility(View.GONE);
                infoView.setVisibility(View.VISIBLE);
                setCharacterInfo();
                break;

            case R.id.close_content_view:
                setColorScheme();
                //Close the info view and open up the expanded view
                infoView.setVisibility(View.GONE);
                expandedView.setVisibility(View.VISIBLE);
                break;
            /*case R.id.build_view:
                //Close info view and open up the build view
                infoView.setVisibility(View.GONE);
                buildView.setVisibility(View.VISIBLE);
                break; */
            case R.id.close_build_view:
                //Close build view and open info view
                buildView.setVisibility(View.GONE);
                infoView.setVisibility(View.VISIBLE);
                break;
        }
    }


    /*  Detect if the floating view is collapsed or expanded */
    private boolean isViewCollapsed() {
        return mFloatingWidgetView == null || mFloatingWidgetView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }


    /*  return status bar height on basis of device display metrics  */
    private int getStatusBarHeight() {
        return (int) Math.ceil(25 * getApplicationContext().getResources().getDisplayMetrics().density);
    }


    /*  Update Floating Widget view coordinates on Configuration change  */
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        getWindowManagerDefaultDisplay();

        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) mFloatingWidgetView.getLayoutParams();

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {


            if (layoutParams.y + (mFloatingWidgetView.getHeight() + getStatusBarHeight()) > szWindow.y) {
                layoutParams.y = szWindow.y - (mFloatingWidgetView.getHeight() + getStatusBarHeight());
                mWindowManager.updateViewLayout(mFloatingWidgetView, layoutParams);
            }

        }
    }

    /*  on Floating widget click show expanded view  */
    private void onFloatingWidgetClick() {
        if (isViewCollapsed()) {
            setColorScheme();
            //when clicked, the collapsed view image will go away and the expanded view opens up
            collapsedView.setVisibility(View.GONE);
            expandedView.setVisibility(View.VISIBLE);
            //change params so that it matches screen width
            mWindowManager.updateViewLayout(mFloatingWidgetView, matchParams);
            //method to fill all the spinners
            fillSpinners();
        }
    }

    private void setCharacterInfo() {
        RadioButton levelOne = mFloatingWidgetView.findViewById(R.id.radio_one);
        RadioButton levelForty = mFloatingWidgetView.findViewById(R.id.radio_forty);
        //Figure out what level the user selected. If they choose nothing than it's level one
        if (levelOne.isChecked()) {
            levelSelected = 1;
            characterLevel = "Level 1";
        } else if (levelForty.isChecked()) {
            levelSelected = 40;
            characterLevel = "Level 40";
        } else {
            levelSelected = 1;
            characterLevel = "Level 1";
        }

        //Grab the character selected, rating selected and find it in the databases to display stats
        character = heroSpinner.getSelectedItem().toString();
        characterRating = ratingSpinner.getSelectedItem().toString();
        //5 star character, grab from 5 star database
        if (rating == 5) {
            characterRatingInfo.setTextColor(ContextCompat.getColor(this, R.color.gold));
            int length = fiveHeroesList.size();
            String[] row;
            int i = 0;
            while (i < length) {
                row = fiveHeroesList.get(i);
                if (row[0].equals(character)) {
                    index = i;
                    i = length;
                } else {
                    i++;
                }
            }
        } else if (rating == 4) { //4 star character, grab from 5 star database
            int length = fourHeroesList.size();
            characterRatingInfo.setTextColor(ContextCompat.getColor(this, R.color.silver));
            String[] row;
            int i = 0;
            while (i < length) {
                row = fourHeroesList.get(i);
                if (row[0].equals(character)) {
                    index = i;
                    i = length;
                } else {
                    i++;
                }
            }
        } else { //3 star character, grab from 5 star database
            int length = threeHeroesList.size();
            characterRatingInfo.setTextColor(ContextCompat.getColor(this, R.color.bronze));
            String[] row;
            int i = 0;
            while (i < length) {
                row = threeHeroesList.get(i);
                if (row[0].equals(character)) {
                    index = i;
                    i = length;
                } else {
                    i++;
                }
            }
        }

        //Set text of textViews with info grabbed from database
        characterNameInfo.setText(character);
        characterRatingInfo.setText(characterRating);
        characterEquippedInfo.setText(characterEquipped);
        characterLevelInfo.setText(characterLevel);
        //method to set stats for everything else
        setCharacterStats();

    }

    private void fillSpinners() {
        //Setting default values
        rating = 5;
        color = "blue";
        isEquipped = true;

        if (ratingAdapter != null && weaponAdapter != null && colorAdapter != null) {
            ratingSpinner.setAdapter(ratingAdapter);
            weaponSpinner.setAdapter(weaponAdapter);
            colorSpinner.setAdapter(colorAdapter);
        } else {


            //Rating Star Spinner
            ratingSpinner = expandedView.findViewById(R.id.rating_spinner);
            ratingAdapter = ArrayAdapter.createFromResource(this, R.array.rating_stars, spinnerNum);
            ratingSpinner.setAdapter(ratingAdapter);

            //Weapon equipped Spinner
            weaponSpinner = expandedView.findViewById(R.id.weapon_spinner);
            weaponAdapter = ArrayAdapter.createFromResource(this, R.array.weapons, spinnerNum);
            weaponSpinner.setAdapter(weaponAdapter);

            //Color Spinner
            colorSpinner = expandedView.findViewById(R.id.color_spinner);
            colorAdapter = ArrayAdapter.createFromResource(this, R.array.colors, spinnerNum);
            colorSpinner.setAdapter(colorAdapter);

            //Hero Spinner
            heroSpinner = expandedView.findViewById(R.id.name_spinner);
            heroAdapter = new ArrayAdapter<>(this,spinnerNum, fiveStarBlueList);
            heroSpinner.setAdapter(heroAdapter);
        }

        //Change hero list based on rating selection
        ratingSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        rating = 5;
                        //changeHeroAdapter();
                        break;
                    case 1:
                        rating = 4;
                        //changeHeroAdapter();
                        break;
                    case 2:
                        rating = 3;
                        //changeHeroAdapter();
                        break;
                }
                changeHeroAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                rating = 5;
            }
        });

        //Collect equipment selection
        weaponSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        isEquipped = true;
                        characterEquipped = "Equipped";
                        break;
                    case 1:
                        isEquipped = false;
                        characterEquipped = "Not Equipped";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                isEquipped = true;
            }
        });

        //Change hero list based on color selection
        colorSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    //Case 0 is blue
                    case 0:
                        color = "blue";
                        //changeHeroAdapter();
                        break;
                    //Case 1 is Colorless
                    case 1:
                        color = "colorless";
                        //changeHeroAdapter();
                        break;
                    //Case 2 is Green
                    case 2:
                        color = "green";
                        //changeHeroAdapter();
                        break;
                    //Case 3 is Red
                    case 3:
                        color = "red";
                        //changeHeroAdapter();
                        break;
                }
                changeHeroAdapter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Color is defaulted on Blue
                color = "blue";
            }
        });


    }

    private void changeHeroAdapter() {
        if (rating == 5) {
            switch (color) {
                case "blue":
                    heroAdapter = new ArrayAdapter<>(this, spinnerNum, fiveStarBlueList);
                    break;
                case "colorless":
                    heroAdapter = new ArrayAdapter<>(this, spinnerNum, fiveStarColorlessList);
                    break;
                case "green":
                    heroAdapter = new ArrayAdapter<>(this, spinnerNum, fiveStarGreenList);
                    break;
                case "red":
                    heroAdapter = new ArrayAdapter<>(this, spinnerNum, fiveStarRedList);
            }
        } else if (rating == 4) {
            switch (color) {
                case "blue":
                    heroAdapter = new ArrayAdapter<>(this, spinnerNum, fourStarBlueList);
                    break;
                case "colorless":
                    heroAdapter = new ArrayAdapter<>(this, spinnerNum, fourStarColorlessList);
                    break;
                case "green":
                    heroAdapter = new ArrayAdapter<>(this, spinnerNum, fourStarGreenList);
                    break;
                case "red":
                    heroAdapter = new ArrayAdapter<>(this, spinnerNum, fourStarRedList);
            }
        } else {
            switch (color) {
                case "blue":
                    heroAdapter = new ArrayAdapter<>(this, spinnerNum, threeStarBlueList);
                    break;
                case "colorless":
                    heroAdapter = new ArrayAdapter<>(this, spinnerNum, threeStarColorlessList);
                    break;
                case "green":
                    heroAdapter = new ArrayAdapter<>(this, spinnerNum, threeStarGreenList);
                    break;
                case "red":
                    heroAdapter = new ArrayAdapter<>(this, spinnerNum, threeStarRedList);
            }
        }
        heroSpinner.setAdapter(heroAdapter);
    }

    private void setCharacterStats() {
        int positiveImage;
        if (!theme) positiveImage = R.drawable.ic_outline_add_circle_24px;
        else positiveImage = R.drawable.ic_outline_add_circle_24px_dark;
        int diff;
        String newValue;
        String[] character = fiveHeroesList.get(index); //Default to to five star heroes
        switch (rating) {
            case 5:
                character = fiveHeroesList.get(index); //get selected characters info from 5 star list
                break;
            case 4:
                character = fourHeroesList.get(index);
                break;
            case 3:
                character = threeHeroesList.get(index);
                break;
        }

        ImageView hpRec = infoView.findViewById(R.id.hp_rec);
        ImageView atkRec = infoView.findViewById(R.id.atk_rec);
        ImageView spdRec = infoView.findViewById(R.id.spd_rec);
        ImageView defRec = infoView.findViewById(R.id.def_rec);
        ImageView resRec = infoView.findViewById(R.id.res_rec);

        //Set HP bane/boon
        switch(character[41]) {
            case "boon":
                hpRec.setImageResource(positiveImage);
                break;
            case "bane":
                hpRec.setImageResource(R.drawable.ic_outline_remove_circle_24px);
                break;
            default:
                hpRec.setImageResource(R.drawable.ic_baseline_lens_24px);
        }
        //Set ATK bane/boon
        switch(character[42]) {
            case "boon":
                atkRec.setImageResource(positiveImage);
                break;
            case "bane":
                atkRec.setImageResource(R.drawable.ic_outline_remove_circle_24px);
                break;
            default:
                atkRec.setImageResource(R.drawable.ic_baseline_lens_24px);
        }

        //Set SPD bane/boon
        switch(character[43]) {
            case "boon":
                spdRec.setImageResource(positiveImage);
                break;
            case "bane":
                spdRec.setImageResource(R.drawable.ic_outline_remove_circle_24px);
                break;
            default:
                spdRec.setImageResource(R.drawable.ic_baseline_lens_24px);
        }

        //Set DEF bane/boon
        switch(character[44]) {
            case "boon":
                defRec.setImageResource(positiveImage);
                break;
            case "bane":
                defRec.setImageResource(R.drawable.ic_outline_remove_circle_24px);
                break;
            default:
                defRec.setImageResource(R.drawable.ic_baseline_lens_24px);
        }

        //Set RES bane/boon
        switch(character[45]) {
            case "boon":
                resRec.setImageResource(positiveImage);
                break;
            case "bane":
                resRec.setImageResource(R.drawable.ic_outline_remove_circle_24px);
                break;
            default:
                resRec.setImageResource(R.drawable.ic_baseline_lens_24px);
        }

        if (!isEquipped && levelSelected == 1) {
            hpLow.setText(character[5]);
            atkLow.setText(character[6]);
            spdLow.setText(character[7]);
            defLow.setText(character[8]);
            resLow.setText(character[9]);
            hpMid.setText(character[10]);
            atkMid.setText(character[11]);
            spdMid.setText(character[12]);
            defMid.setText(character[13]);
            resMid.setText(character[14]);
            hpHi.setText(character[15]);
            atkHi.setText(character[16]);
            spdHi.setText(character[17]);
            defHi.setText(character[18]);
            resHi.setText(character[19]);
        } else if (!isEquipped && levelSelected == 40) {
            hpLow.setText(character[20]);
            atkLow.setText(character[21]);
            spdLow.setText(character[22]);
            defLow.setText(character[23]);
            resLow.setText(character[24]);
            hpMid.setText(character[25]);
            atkMid.setText(character[26]);
            spdMid.setText(character[27]);
            defMid.setText(character[28]);
            resMid.setText(character[29]);
            hpHi.setText(character[30]);
            atkHi.setText(character[31]);
            spdHi.setText(character[32]);
            defHi.setText(character[33]);
            resHi.setText(character[34]);
        } else if (isEquipped && levelSelected == 1) {
            //First have to change the values based on weapon equipped 35-39
            //If stats are n/a they need to be skipped, otherwise do them all
            switch (character[5]) {
                case "n/a":
                    diff = Integer.parseInt(character[10]) + Integer.parseInt(character[35]);
                    newValue = String.valueOf(diff);
                    hpMid.setText(newValue);
                    hpLow.setText(character[5]);
                    hpHi.setText(character[15]);

                    diff = Integer.parseInt(character[11]) + Integer.parseInt(character[36]);
                    newValue = String.valueOf(diff);
                    atkMid.setText(newValue);
                    atkLow.setText(character[6]);
                    atkHi.setText(character[16]);

                    diff = Integer.parseInt(character[12]) + Integer.parseInt(character[37]);
                    newValue = String.valueOf(diff);
                    spdMid.setText(newValue);
                    spdLow.setText(character[7]);
                    spdHi.setText(character[17]);

                    diff = Integer.parseInt(character[13]) + Integer.parseInt(character[38]);
                    newValue = String.valueOf(diff);
                    defLow.setText(character[8]);
                    defMid.setText(newValue);
                    defHi.setText(character[18]);

                    diff = Integer.parseInt(character[14]) + Integer.parseInt(character[39]);
                    newValue = String.valueOf(diff);
                    resMid.setText(newValue);
                    resLow.setText(character[9]);
                    resHi.setText(character[19]);
                    break;
                case "TBD":
                    hpLow.setText(character[5]);
                    atkLow.setText(character[6]);
                    spdLow.setText(character[7]);
                    defLow.setText(character[8]);
                    resLow.setText(character[9]);
                    hpMid.setText(character[10]);
                    atkMid.setText(character[11]);
                    spdMid.setText(character[12]);
                    defMid.setText(character[13]);
                    resMid.setText(character[14]);
                    hpHi.setText(character[15]);
                    atkHi.setText(character[16]);
                    spdHi.setText(character[17]);
                    defHi.setText(character[18]);
                    resHi.setText(character[19]);
                    break;
                default:
                    int diffLow = Integer.parseInt(character[5]) + Integer.parseInt(character[35]);
                    diff = Integer.parseInt(character[10]) + Integer.parseInt(character[35]);
                    int diffHigh = Integer.parseInt(character[15]) + Integer.parseInt(character[35]);
                    newValue = String.valueOf(diff);
                    String newValueLow = String.valueOf(diffLow);
                    String newValueHigh = String.valueOf(diffHigh);


                    hpLow.setText(newValueLow);
                    hpMid.setText(newValue);
                    hpHi.setText(newValueHigh);

                    diffLow = Integer.parseInt(character[6]) + Integer.parseInt(character[36]);
                    diff = Integer.parseInt(character[11]) + Integer.parseInt(character[36]);
                    diffHigh = Integer.parseInt(character[16]) + Integer.parseInt(character[36]);
                    newValue = String.valueOf(diff);
                    newValueLow = String.valueOf(diffLow);
                    newValueHigh = String.valueOf(diffHigh);

                    atkLow.setText(newValueLow);
                    atkMid.setText(newValue);
                    atkHi.setText(newValueHigh);

                    diffLow = Integer.parseInt(character[7]) + Integer.parseInt(character[37]);
                    diff = Integer.parseInt(character[12]) + Integer.parseInt(character[37]);
                    diffHigh = Integer.parseInt(character[17]) + Integer.parseInt(character[37]);
                    newValue = String.valueOf(diff);
                    newValueLow = String.valueOf(diffLow);
                    newValueHigh = String.valueOf(diffHigh);

                    spdLow.setText(newValueLow);
                    spdMid.setText(newValue);
                    spdHi.setText(newValueHigh);

                    diffLow = Integer.parseInt(character[8]) + Integer.parseInt(character[38]);
                    diff = Integer.parseInt(character[13]) + Integer.parseInt(character[38]);
                    diffHigh = Integer.parseInt(character[18]) + Integer.parseInt(character[38]);
                    newValue = String.valueOf(diff);
                    newValueLow = String.valueOf(diffLow);
                    newValueHigh = String.valueOf(diffHigh);

                    defLow.setText(newValueLow);
                    defMid.setText(newValue);
                    defHi.setText(newValueHigh);

                    diffLow = Integer.parseInt(character[9]) + Integer.parseInt(character[39]);
                    diff = Integer.parseInt(character[14]) + Integer.parseInt(character[39]);
                    diffHigh = Integer.parseInt(character[19]) + Integer.parseInt(character[39]);
                    newValue = String.valueOf(diff);
                    newValueLow = String.valueOf(diffLow);
                    newValueHigh = String.valueOf(diffHigh);

                    resLow.setText(newValueLow);
                    resMid.setText(newValue);
                    resHi.setText(newValueHigh);
                    break;
            }
        } else { //is equipped and level 40
            //First have to change the values based on weapon equipped 35-39
            //If they have n/a values, then the unit only has mid stats, so don't try to get value
            //of low and hi stats, just set them to n/a
            switch (character[5]) {
                case "n/a":
                    diff = Integer.parseInt(character[25]) + Integer.parseInt(character[35]);
                    newValue = String.valueOf(diff);
                    hpMid.setText(newValue);
                    hpLow.setText(character[20]);
                    hpHi.setText(character[30]);

                    diff = Integer.parseInt(character[26]) + Integer.parseInt(character[36]);
                    newValue = String.valueOf(diff);
                    atkMid.setText(newValue);
                    atkLow.setText(character[21]);
                    atkHi.setText(character[31]);

                    diff = Integer.parseInt(character[27]) + Integer.parseInt(character[37]);
                    newValue = String.valueOf(diff);
                    spdMid.setText(newValue);
                    spdLow.setText(character[22]);
                    spdHi.setText(character[32]);

                    diff = Integer.parseInt(character[28]) + Integer.parseInt(character[38]);
                    newValue = String.valueOf(diff);
                    defMid.setText(newValue);
                    defLow.setText(character[23]);
                    defHi.setText(character[33]);

                    diff = Integer.parseInt(character[29]) + Integer.parseInt(character[39]);
                    newValue = String.valueOf(diff);
                    resMid.setText(newValue);
                    resLow.setText(character[24]);
                    resHi.setText(character[34]);
                    break;
                case "TBD":
                    hpLow.setText(character[20]);
                    atkLow.setText(character[21]);
                    spdLow.setText(character[22]);
                    defLow.setText(character[23]);
                    resLow.setText(character[24]);
                    hpMid.setText(character[25]);
                    atkMid.setText(character[26]);
                    spdMid.setText(character[27]);
                    defMid.setText(character[28]);
                    resMid.setText(character[29]);
                    hpHi.setText(character[30]);
                    atkHi.setText(character[31]);
                    spdHi.setText(character[32]);
                    defHi.setText(character[33]);
                    resHi.setText(character[34]);
                    break;
                default:
                    int diffLow = Integer.parseInt(character[20]) + Integer.parseInt(character[35]);
                    diff = Integer.parseInt(character[25]) + Integer.parseInt(character[35]);
                    int diffHigh = Integer.parseInt(character[30]) + Integer.parseInt(character[35]);
                    newValue = String.valueOf(diff);
                    String newValueLow = String.valueOf(diffLow);
                    String newValueHigh = String.valueOf(diffHigh);

                    hpLow.setText(newValueLow);
                    hpMid.setText(newValue);
                    hpHi.setText(newValueHigh);

                    diffLow = Integer.parseInt(character[21]) + Integer.parseInt(character[36]);
                    diff = Integer.parseInt(character[26]) + Integer.parseInt(character[36]);
                    diffHigh = Integer.parseInt(character[31]) + Integer.parseInt(character[36]);
                    newValue = String.valueOf(diff);
                    newValueLow = String.valueOf(diffLow);
                    newValueHigh = String.valueOf(diffHigh);

                    atkLow.setText(newValueLow);
                    atkMid.setText(newValue);
                    atkHi.setText(newValueHigh);

                    diffLow = Integer.parseInt(character[22]) + Integer.parseInt(character[37]);
                    diff = Integer.parseInt(character[27]) + Integer.parseInt(character[37]);
                    diffHigh = Integer.parseInt(character[32]) + Integer.parseInt(character[37]);
                    newValue = String.valueOf(diff);
                    newValueLow = String.valueOf(diffLow);
                    newValueHigh = String.valueOf(diffHigh);

                    spdLow.setText(newValueLow);
                    spdMid.setText(newValue);
                    spdHi.setText(newValueHigh);

                    diffLow = Integer.parseInt(character[23]) + Integer.parseInt(character[38]);
                    diff = Integer.parseInt(character[28]) + Integer.parseInt(character[38]);
                    diffHigh = Integer.parseInt(character[33]) + Integer.parseInt(character[38]);
                    newValue = String.valueOf(diff);
                    newValueLow = String.valueOf(diffLow);
                    newValueHigh = String.valueOf(diffHigh);

                    defLow.setText(newValueLow);
                    defMid.setText(newValue);
                    defHi.setText(newValueHigh);

                    diffLow = Integer.parseInt(character[24]) + Integer.parseInt(character[39]);
                    diff = Integer.parseInt(character[29]) + Integer.parseInt(character[39]);
                    diffHigh = Integer.parseInt(character[34]) + Integer.parseInt(character[39]);
                    newValue = String.valueOf(diff);
                    newValueLow = String.valueOf(diffLow);
                    newValueHigh = String.valueOf(diffHigh);

                    resLow.setText(newValueLow);
                    resMid.setText(newValue);
                    resHi.setText(newValueHigh);
                    break;
            }
        }
    }

    private void addTextViews() {
        characterNameInfo = infoView.findViewById(R.id.character_name);
        characterRatingInfo = infoView.findViewById(R.id.character_rating);
        characterEquippedInfo = infoView.findViewById(R.id.character_equipped);
        characterLevelInfo = infoView.findViewById(R.id.character_level);
        hpLow = infoView.findViewById(R.id.hp_low);
        atkLow = infoView.findViewById(R.id.atk_low);
        spdLow = infoView.findViewById(R.id.spd_low);
        defLow = infoView.findViewById(R.id.def_low);
        resLow = infoView.findViewById(R.id.res_low);
        hpMid = infoView.findViewById(R.id.hp_mid);
        atkMid = infoView.findViewById(R.id.atk_mid);
        spdMid = infoView.findViewById(R.id.spd_mid);
        defMid = infoView.findViewById(R.id.def_mid);
        resMid = infoView.findViewById(R.id.res_mid);
        hpHi = infoView.findViewById(R.id.hp_high);
        atkHi = infoView.findViewById(R.id.atk_high);
        spdHi = infoView.findViewById(R.id.spd_high);
        defHi = infoView.findViewById(R.id.def_high);
        resHi = infoView.findViewById(R.id.res_high);
    }

    private void setColorScheme() {
        theme = sharedPref.getBoolean(SettingsActivity.KEY_PREF_THEME, false);
        ImageView brandImage = mFloatingWidgetView.findViewById(R.id.brand_image);
        ImageView minimizeImage = mFloatingWidgetView.findViewById(R.id.close_expanded_view);
        TextView nameTextView = mFloatingWidgetView.findViewById(R.id.name);
        TextView ratingTextView = mFloatingWidgetView.findViewById(R.id.rating);
        TextView weaponTextView = mFloatingWidgetView.findViewById(R.id.weapon);
        TextView colorTextView = mFloatingWidgetView.findViewById(R.id.color);
        RadioButton levelOne = mFloatingWidgetView.findViewById(R.id.radio_one);
        RadioButton levelForty = mFloatingWidgetView.findViewById(R.id.radio_forty);
        TextView hpTitle = mFloatingWidgetView.findViewById(R.id.hp_title);
        TextView spdTitle = mFloatingWidgetView.findViewById(R.id.spd_title);
        TextView atkTitle = mFloatingWidgetView.findViewById(R.id.atk_title);
        TextView defTitle = mFloatingWidgetView.findViewById(R.id.def_title);
        TextView resTitle = mFloatingWidgetView.findViewById(R.id.res_title);
        TextView lowTitle = mFloatingWidgetView.findViewById(R.id.low_title);
        TextView medTitle = mFloatingWidgetView.findViewById(R.id.mid_title);
        TextView highTitle = mFloatingWidgetView.findViewById(R.id.high_title);
        TextView recTitle = mFloatingWidgetView.findViewById(R.id.recommended_title);
        ImageView brandImage2 = mFloatingWidgetView.findViewById(R.id.brand_image_info);
        ImageView backArrowImage = mFloatingWidgetView.findViewById(R.id.close_content_view);

        if(!theme) {
            //Set all the colors for a light theme
            expandedView.setBackground(getResources().getDrawable(R.drawable.background));
            brandImage.setImageDrawable(getResources().getDrawable(R.drawable.black_brand));
            minimizeImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_minimize));
            backArrowImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_black_left_arrow));
            nameTextView.setTextColor(getResources().getColor(R.color.text_color_light));
            ratingTextView.setTextColor(getResources().getColor(R.color.text_color_light));
            weaponTextView.setTextColor(getResources().getColor(R.color.text_color_light));
            colorTextView.setTextColor(getResources().getColor(R.color.text_color_light));
            levelOne.setTextColor(getResources().getColor(R.color.text_color_light));
            levelForty.setTextColor(getResources().getColor(R.color.text_color_light));

            infoView.setBackground(getResources().getDrawable(R.drawable.background));
            characterNameInfo.setTextColor(getResources().getColor(R.color.text_color_light));
            characterEquippedInfo.setTextColor(getResources().getColor(R.color.text_color_light));
            characterLevelInfo.setTextColor(getResources().getColor(R.color.text_color_light));
            //characterRatingInfo.setTextColor(getResources().getColor(R.color.text_color_light));
            hpTitle.setTextColor(getResources().getColor(R.color.text_color_light));
            spdTitle.setTextColor(getResources().getColor(R.color.text_color_light));
            atkTitle.setTextColor(getResources().getColor(R.color.text_color_light));
            resTitle.setTextColor(getResources().getColor(R.color.text_color_light));
            defTitle.setTextColor(getResources().getColor(R.color.text_color_light));
            lowTitle.setTextColor(getResources().getColor(R.color.text_color_light));
            medTitle.setTextColor(getResources().getColor(R.color.text_color_light));
            highTitle.setTextColor(getResources().getColor(R.color.text_color_light));
            hpMid.setTextColor(getResources().getColor(R.color.text_color_light));
            spdMid.setTextColor(getResources().getColor(R.color.text_color_light));
            atkMid.setTextColor(getResources().getColor(R.color.text_color_light));
            resMid.setTextColor(getResources().getColor(R.color.text_color_light));
            defMid.setTextColor(getResources().getColor(R.color.text_color_light));
            recTitle.setTextColor(getResources().getColor(R.color.text_color_light));
            brandImage2.setImageDrawable(getResources().getDrawable(R.drawable.black_brand_2));
            hpHi.setTextColor(getResources().getColor(R.color.hi_stat));
            spdHi.setTextColor(getResources().getColor(R.color.hi_stat));
            atkHi.setTextColor(getResources().getColor(R.color.hi_stat));
            resHi.setTextColor(getResources().getColor(R.color.hi_stat));
            defHi.setTextColor(getResources().getColor(R.color.hi_stat));
        }
        else {
            //Set all the colors for a dark theme
            expandedView.setBackground(getResources().getDrawable(R.drawable.background_dark));
            brandImage.setImageDrawable(getResources().getDrawable(R.drawable.yellow_brand));
            brandImage2.setImageDrawable(getResources().getDrawable(R.drawable.yellow_brand_2));
            backArrowImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_white_left_arrow));
            minimizeImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_minimize_white));
            nameTextView.setTextColor(getResources().getColor(R.color.text_color_dark));
            ratingTextView.setTextColor(getResources().getColor(R.color.text_color_dark));
            weaponTextView.setTextColor(getResources().getColor(R.color.text_color_dark));
            colorTextView.setTextColor(getResources().getColor(R.color.text_color_dark));
            levelOne.setTextColor(getResources().getColor(R.color.text_color_dark));
            levelForty.setTextColor(getResources().getColor(R.color.text_color_dark));

            infoView.setBackground(getResources().getDrawable(R.drawable.background_dark));
            characterNameInfo.setTextColor(getResources().getColor(R.color.text_color_dark));
            characterEquippedInfo.setTextColor(getResources().getColor(R.color.text_color_dark));
            characterLevelInfo.setTextColor(getResources().getColor(R.color.text_color_dark));
            //characterRatingInfo.setTextColor(getResources().getColor(R.color.text_color_dark));
            hpTitle.setTextColor(getResources().getColor(R.color.text_color_dark));
            spdTitle.setTextColor(getResources().getColor(R.color.text_color_dark));
            atkTitle.setTextColor(getResources().getColor(R.color.text_color_dark));
            resTitle.setTextColor(getResources().getColor(R.color.text_color_dark));
            defTitle.setTextColor(getResources().getColor(R.color.text_color_dark));
            lowTitle.setTextColor(getResources().getColor(R.color.text_color_dark));
            medTitle.setTextColor(getResources().getColor(R.color.text_color_dark));
            highTitle.setTextColor(getResources().getColor(R.color.text_color_dark));
            hpMid.setTextColor(getResources().getColor(R.color.mid_stat_yellow));
            spdMid.setTextColor(getResources().getColor(R.color.mid_stat_yellow));
            atkMid.setTextColor(getResources().getColor(R.color.mid_stat_yellow));
            resMid.setTextColor(getResources().getColor(R.color.mid_stat_yellow));
            defMid.setTextColor(getResources().getColor(R.color.mid_stat_yellow));
            recTitle.setTextColor(getResources().getColor(R.color.text_color_dark));
            hpHi.setTextColor(getResources().getColor(R.color.hi_stat_dark));
            spdHi.setTextColor(getResources().getColor(R.color.hi_stat_dark));
            atkHi.setTextColor(getResources().getColor(R.color.hi_stat_dark));
            resHi.setTextColor(getResources().getColor(R.color.hi_stat_dark));
            defHi.setTextColor(getResources().getColor(R.color.hi_stat_dark));

        }
    }

    private void changeTransparency() {
        //Toast.makeText(this, "The value the user chose is: " + transparentNum, Toast.LENGTH_LONG).show();
        ImageView collapsedImage = mFloatingWidgetView.findViewById(R.id.collapsed_iv);
        collapsedImage.setAlpha(Float.valueOf(transparentNum));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        /*  on destroy remove both view from window manager */

        if (mFloatingWidgetView != null)
            mWindowManager.removeView(mFloatingWidgetView);
    }
}