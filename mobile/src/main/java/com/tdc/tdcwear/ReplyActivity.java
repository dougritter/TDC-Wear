package com.tdc.tdcwear;

import android.os.Bundle;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.app.RemoteInput;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
/*
Esta classe é de autoria de Nelson Glauber
e seu uso foi previamente autorizado.
O código original está disponível em:
https://github.com/nglauber/NextLevelApps
*/
public class ReplyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        TextView textView = (TextView)findViewById(R.id.txtReply);

        Bundle remoteInput = RemoteInput.getResultsFromIntent(getIntent());
        if (remoteInput != null) {
            CharSequence resposta = remoteInput.getCharSequence(NotificationUtils.EXTRA_VOICE_REPLY);
            textView.setText(resposta);

            NotificationManagerCompat.from(this).cancel(NotificationUtils.NOTIFICATION_ID);
        }
    }
}
