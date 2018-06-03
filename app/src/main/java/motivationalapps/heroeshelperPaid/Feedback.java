package motivationalapps.heroeshelperPaid;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

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
        Intent emailIntent;
        String feedback;
        emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/html");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[] {getResources().getString(R.string.mail_feedback_email)});
        feedback = feedbackBox.getText().toString();
        emailIntent.putExtra(Intent.EXTRA_TEXT, feedback);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback from the Heroes Helper App");

        startActivity(Intent.createChooser(emailIntent, "Send email using..."));
    }

    public void closeActivity(View view) {
        finish();
    }
}
