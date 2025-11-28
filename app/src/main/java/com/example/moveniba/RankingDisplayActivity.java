package com.example.moveniba;

import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RankingDisplayActivity extends AppCompatActivity {

    private RankingDataSource dataSource;
    private RecyclerView recyclerView;
    private RankingDisplayAdapter adapter;
    private Button buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranking_display);

        dataSource = new RankingDataSource(this);
        dataSource.open();

        recyclerView = findViewById(R.id.recycler_view_ranking_display);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        buttonBack = findViewById(R.id.button_back);

        buttonBack.setOnClickListener(v -> finish()); // Lógica para o botão Voltar

        List<Ranking> rankingList = dataSource.getAllRankings();
        adapter = new RankingDisplayAdapter(this, rankingList);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        dataSource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        dataSource.close();
        super.onPause();
    }
}
