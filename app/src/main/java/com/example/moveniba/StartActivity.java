package com.example.moveniba;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List; // Importação que faltava

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        Button buttonRegisterUser = findViewById(R.id.button_register_user);
        Button buttonPlay = findViewById(R.id.button_play);
        Button buttonRanking = findViewById(R.id.button_ranking);

        buttonRegisterUser.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, UserManagementActivity.class);
            startActivity(intent);
        });

        buttonPlay.setOnClickListener(v -> {
            showPlayerSelectionDialog();
        });

        buttonRanking.setOnClickListener(v -> {
            Intent intent = new Intent(StartActivity.this, RankingDisplayActivity.class);
            startActivity(intent);
        });
    }

    private void showPlayerSelectionDialog() {
        RankingDataSource dataSource = new RankingDataSource(this);
        dataSource.open();
        List<Ranking> players = dataSource.getAllRankings();
        dataSource.close();

        String[] playerNames = new String[players.size()];
        for (int i = 0; i < players.size(); i++) {
            playerNames[i] = players.get(i).getName();
        }

        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Selecione um Jogador");
        builder.setItems(playerNames, (dialog, which) -> {
            Ranking selectedPlayer = players.get(which);
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            intent.putExtra("PLAYER_ID", selectedPlayer.getId());
            startActivity(intent);
        });
        builder.show();
    }
}
