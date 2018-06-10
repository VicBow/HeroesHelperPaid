package motivationalapps.heroeshelperPaid;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Feedback extends AppCompatActivity {

    private EditText feedbackBox;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedbackBox = findViewById(R.id.feedback);
        findViewById(R.id.feedbackButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFeedback();
            }
        });



    }

    private void sendFeedback() {
        /*Intent emailIntent;
        String feedback;
        emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/html");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {getResources().getString(R.string.mail_feedback_email)});
        feedback = feedbackBox.getText().toString();
        emailIntent.putExtra(Intent.EXTRA_TEXT, feedback);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from the Heroes Helper App");

        startActivity(Intent.createChooser(emailIntent, "Send email using...")); */

        final String feedback;
        feedback = feedbackBox.getText().toString();
        final String email = "motivationalapps2017@gmail.com";

        final ProgressDialog dialog = new ProgressDialog(this);
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender(email, "FEH2018!!");
                    sender.sendMail("Feedback from Heroes Helper",
                            feedback,
                            email,
                            email);
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
        feedbackBox.setText("");
        Toast.makeText(this, "Thanks! We appreciate your feedback.", Toast.LENGTH_LONG).show();
    }

    public void closeActivity(View view) {
        finish();
    }
}
