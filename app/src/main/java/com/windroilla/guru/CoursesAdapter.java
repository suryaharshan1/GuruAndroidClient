package com.windroilla.guru;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.windroilla.guru.api.Course;

import java.util.List;

/**
 * Created by Surya Harsha Nunnaguppala on 4/7/15.
 */
public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {

    private List<Course> courseList;

    public CoursesAdapter(List<Course> courseList) {
        this.courseList = courseList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_course, parent, false);

        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.courseName.setText(courseList.get(position).name);
        holder.courseCost.setText(String.valueOf(courseList.get(position).amount));
        holder.cardView.setUseCompatPadding(true);
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final CardView cardView;
        public final TextView courseName;
        public final TextView courseCost;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.recycler_item_course_cardview);
            courseName = (TextView) itemView.findViewById(R.id.recycler_item_course_name);
            courseCost = (TextView) itemView.findViewById(R.id.recycler_item_course_amount);
        }
    }

}
