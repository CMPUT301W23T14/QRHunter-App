package com.example.qrhunter.ui.leaderboard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.qrhunter.R;
import com.example.qrhunter.data.model.Player;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class LeaderboardAdapter extends ArrayAdapter<Player> {
    public LeaderboardAdapter(Context context, ArrayList<Player> players) {
        super(context, 0, players);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup
            parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_leaderboard_players,
                    parent, false);
        } else {
            view = convertView;
        }
        // sets the contents of each entry in list view
        Player player = getItem(position);
        TextView player_name = view.findViewById(R.id.player_name);
        TextView player_score = view.findViewById(R.id.player_score);
        TextView player_rank = view.findViewById(R.id.player_rank);
        player_score.setText(Integer.toString(player.getTotalScore()));
        player_name.setText(player.getId());
        player_rank.setText(Integer.toString(player.getRank()));

        return view;
    }
}
