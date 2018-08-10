package motivationalapps.heroeshelperPaid;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ActionReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getStringExtra("icon");
        if (action.equals("closeIcon")) {
            FloatingWidgetService.closeItem();
        }
        else if (action.equals("openIcon")) {
            FloatingWidgetService.openItem();
        }
    }
}
