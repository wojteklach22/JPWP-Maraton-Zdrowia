package jpwp.projekt.maraton_zdrowia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class Koniec_gry extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_koniec_gry);

        Wyniki();
    }

    public void Wyniki() {
        TextView scoreText = (TextView) findViewById(R.id.textView4);
        TextView highscoreText = (TextView) findViewById(R.id.textView5);

        int score = getIntent().getIntExtra("SCORE", 0);
        scoreText.setText(score + "");

        SharedPreferences settings = getSharedPreferences("HIGH_SCORE", Context.MODE_PRIVATE);
        int highScore = settings.getInt("HIGH_SCORE", 0);

        if (score > highScore) {
            highscoreText.setText("Najwyższy wynik (Hi-Score): " + score);

            // Update High Score
            SharedPreferences.Editor editor = settings.edit();
            editor.putInt("HIGH_SCORE", score);
            editor.commit();

        } else {
            highscoreText.setText("Najwyższy wynik (Hi-Score): " + highScore);

        }
    }
    public void jeszczeRaz_Click(View v) {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}