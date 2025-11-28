package com.example.moveniba;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RankingDisplayAdapter extends RecyclerView.Adapter<RankingDisplayAdapter.RankingViewHolder> {

    private final List<Ranking> rankingList;
    private final Context context;

    public RankingDisplayAdapter(Context context, List<Ranking> rankingList) {
        this.context = context;
        this.rankingList = rankingList;
    }

    @NonNull
    @Override
    public RankingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.ranking_display_item, parent, false);
        return new RankingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RankingViewHolder holder, int position) {
        Ranking ranking = rankingList.get(position);
        
        String rankString = (position + 1) + "º Lugar : ";
        holder.rankAndName.setText(rankString + ranking.getName());
        holder.score.setText(String.valueOf(ranking.getScore()));
    }

    @Override
    public int getItemCount() {
        return rankingList.size();
    }

    static class RankingViewHolder extends RecyclerView.ViewHolder {
        TextView rankAndName;
        TextView score;

        public RankingViewHolder(@NonNull View itemView) {
            super(itemView);
            rankAndName = itemView.findViewById(R.id.text_view_rank_and_name);
            score = itemView.findViewById(R.id.text_view_score);
        }
    }
}
