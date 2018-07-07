package motivationalapps.heroeshelperPaid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra("close");
        if (action.equals("closeIcon")) {
            //Toast.makeText(context, "Close the icon!", Toast.LENGTH_LONG).show();
            FloatingWidgetService.closeItem("icon");
        }
    }
}
