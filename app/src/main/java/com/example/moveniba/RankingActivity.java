package com.example.moveniba;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RankingActivity extends AppCompatActivity implements RankingAdapter.OnItemClickListener {

    private RankingDataSource dataSource;
    private RecyclerView recyclerView;
    private RankingAdapter adapter;
    private List<Ranking> rankingList;
    private EditText editTextName;
    private Button buttonSave;
    private Button buttonPlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking);

        dataSource = new RankingDataSource(this);
        dataSource.open();

        recyclerView = findViewById(R.id.recycler_view_ranking);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        editTextName = findViewById(R.id.edit_text_name);
        buttonSave = findViewById(R.id.button_save);
        buttonPlay = findViewById(R.id.button_play);

        buttonSave.setOnClickListener(v -> {
            String name = editTextName.getText().toString().trim();
            if (!name.isEmpty()) {
                long id = dataSource.addRanking(name);
                if (id != -1) {
                    updateRankingList();
                    editTextName.setText("");
                } else {
                    Toast.makeText(RankingActivity.this, "Erro ao salvar usuário", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(RankingActivity.this, "Digite um nome", Toast.LENGTH_SHORT).show();
            }
        });

        buttonPlay.setOnClickListener(v -> showPlayerSelectionDialog());

        updateRankingList();
    }

    private void showPlayerSelectionDialog() {
        List<Ranking> players = dataSource.getAllRankings();
        String[] playerNames = new String[players.size()];
        for (int i = 0; i < players.size(); i++) {
            playerNames[i] = players.get(i).getName();
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Selecione um Jogador");
        builder.setItems(playerNames, (dialog, which) -> {
            Ranking selectedPlayer = players.get(which);
            Intent intent = new Intent(RankingActivity.this, MainActivity.class);
            intent.putExtra("PLAYER_ID", selectedPlayer.getId());
            startActivity(intent);
        });
        builder.show();
    }


    private void updateRankingList() {
        rankingList = dataSource.getAllRankings();
        adapter = new RankingAdapter(this, rankingList);
        adapter.setOnItemClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onEditClick(int position) {
        Ranking selectedPlayer = rankingList.get(position);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Editar Nome");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setText(selectedPlayer.getName());
        builder.setView(input);

        builder.setPositiveButton("Salvar", (dialog, which) -> {
            String newName = input.getText().toString().trim();
            if (!newName.isEmpty()) {
                dataSource.updateName(selectedPlayer.getId(), newName);
                updateRankingList();
            }
        });
        builder.setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    @Override
    public void onDeleteClick(int position) {
        dataSource.deleteRanking(rankingList.get(position).getId());
        rankingList.remove(position);
        adapter.notifyItemRemoved(position);
        adapter.notifyItemRangeChanged(position, rankingList.size());
    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
        updateRankingList();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }
}
