package com.example.qrhunter.ui.leaderboard;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.qrhunter.R;
import com.example.qrhunter.data.model.Player;

import java.util.ArrayList;
import java.util.Objects;

public class LeaderboardAdapter extends ArrayAdapter<Player> {
    private String playerId;

    public LeaderboardAdapter(Context context, ArrayList<Player> players, String playerId) {
        super(context, 0, players);
        this.playerId = playerId;
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

        Player otherPlayer = getItem(position);
        TextView player_name = view.findViewById(R.id.player_name);
        TextView player_score = view.findViewById(R.id.player_score);
        TextView player_rank = view.findViewById(R.id.player_rank);
        player_score.setText(Integer.toString((int) Math.round(otherPlayer.getTotalScore())));
        player_name.setText(otherPlayer.getUsername());

        // Navigate to profile on click
        view.setOnClickListener(v -> {
            NavController navController = Navigation.findNavController(v);

            // If the selected profile is the user, just navigate to ProfileFragment, otherwise OtherProfileFragment
            if (otherPlayer.getId().equals(this.playerId)) {
                navController.navigate(R.id.action_navigation_leaderboard_to_navigation_profile);
            } else {
                LeaderboardFragmentDirections.ActionNavigationLeaderboardToOtherProfileFragment action =
                        LeaderboardFragmentDirections.actionNavigationLeaderboardToOtherProfileFragment(otherPlayer.getId());
                navController.navigate(action);
            }
        });

        if (otherPlayer.getRank() == 1) {
            Drawable drawable = ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.placement_first, null);
            player_rank.setBackground(drawable);
            player_rank.setText("");
        } else if (otherPlayer.getRank() == 2) {
            Drawable drawable = ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.placement_second, null);
            player_rank.setBackground(drawable);
            player_rank.setText("");

        } else if (otherPlayer.getRank() == 3) {
            Drawable drawable = ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.placement_third, null);
            player_rank.setBackground(drawable);
            player_rank.setText("");

        } else {
            player_rank.setText(String.valueOf(otherPlayer.getRank()));
            player_rank.setBackground(null);
        }
        if (Objects.equals(otherPlayer.getId(), playerId)) {
            Drawable drawable = ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.user_display, null);
            ColorDrawable newDrawable = new ColorDrawable(Color.parseColor("#FFF26F"));
            view.setBackground(newDrawable);
        } else {
            // Reset background color for non-selected items
            Drawable drawable = ResourcesCompat.getDrawable(getContext().getResources(), R.drawable.user_display, null);
            view.setBackground(drawable);
        }

        return view;
    }
}
