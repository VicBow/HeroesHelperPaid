package motivationalapps.heroeshelperPaid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class ActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra("close");
        if (action.equals("closeIcon")) {
            Toast.makeText(context, "Close the icon!", Toast.LENGTH_LONG).show();
            FloatingWidgetService.closeItem("icon");
        }
        else if (action.equals("closeApp")) {
            Toast.makeText(context, "Close the apps!", Toast.LENGTH_LONG).show();
        }
    }
}
