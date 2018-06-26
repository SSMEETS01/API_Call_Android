package com.multiscreen.siams.project6;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class GradeAdapter extends ArrayAdapter<Grade> {
    public GradeAdapter(@NonNull Context context, ArrayList<Grade> getallen) {
        super(context, 0, getallen);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.gradestandardlayout, parent, false
            );
        }

        Grade currentgrade = getItem(position);

        TextView tvCourse = (TextView)convertView.findViewById(R.id.tvCourse);
        tvCourse.setText(currentgrade.getCourseCode());

        TextView tvDate = (TextView)convertView.findViewById(R.id.tvDate);
        tvDate.setText(currentgrade.getResultDate());

        TextView tvScore = (TextView)convertView.findViewById(R.id.tvScore);
        tvScore.setText(currentgrade.getScore());
        return convertView;
    }
}
