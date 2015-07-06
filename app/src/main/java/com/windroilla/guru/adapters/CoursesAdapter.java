package com.windroilla.guru.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.windroilla.guru.R;
import com.windroilla.guru.api.responseclasses.Course;

import java.util.List;

/**
 * Created by Surya Harsha Nunnaguppala on 4/7/15.
 */
public class CoursesAdapter extends RecyclerView.Adapter<CoursesAdapter.ViewHolder> {

    private List<Course> courseList;
    private Context context;
    private Resources resources;

    public CoursesAdapter(Context context, List<Course> courseList) {
        this.courseList = courseList;
        this.context = context;
        resources = context.getResources();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_item_course, parent, false);

        final ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.courseJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Join", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.courseStrategy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Strategy", Toast.LENGTH_SHORT).show();
            }
        });
        viewHolder.courseDiscuss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Discuss", Toast.LENGTH_SHORT).show();
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Course course = courseList.get(position);
        holder.courseName.setText(course.name);
        holder.courseCost.setText(resources.getString(R.string.courses_fee) + ": " + String.valueOf(course.amount));
        holder.courseType.setText(resources.getString(R.string.courses_type) + ": " + course.coursetype.description);
        holder.courseInstitute.setText(resources.getString(R.string.courses_institute) + ": " + course.institute.name);
        holder.courseStrength.setText(resources.getString(R.string.courses_registrations) + ": " + String.valueOf(course.strength));
        holder.courseInstructor.setText(resources.getString(R.string.courses_manager) + ": " + course.instructor.first_name + " " + course.instructor.last_name);
        holder.courseRating.setRating((float) course.rating);
        holder.courseValidity.setText(resources.getString(R.string.courses_validity) + ": " + String.valueOf(course.validity));
    }

    @Override
    public int getItemCount() {
        return courseList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public final CardView cardView;
        public final TextView courseName;
        public final TextView courseCost;
        public final RatingBar courseRating;
        public final TextView courseValidity;
        public final TextView courseStrength;
        public final TextView courseInstructor;
        public final TextView courseInstitute;
        public final TextView courseType;
        public final TextView courseDiscuss;
        public final TextView courseStrategy;
        public final TextView courseJoin;

        public ViewHolder(View itemView) {
            super(itemView);
            cardView = (CardView) itemView.findViewById(R.id.recycler_item_course_cardview);
            courseName = (TextView) itemView.findViewById(R.id.recycler_item_course_name);
            courseCost = (TextView) itemView.findViewById(R.id.recycler_item_course_amount);
            courseRating = (RatingBar) itemView.findViewById(R.id.recycler_item_course_rating);
            courseValidity = (TextView) itemView.findViewById(R.id.recycler_item_course_validity);
            courseStrength = (TextView) itemView.findViewById(R.id.recycler_item_course_strength);
            courseInstructor = (TextView) itemView.findViewById(R.id.recycler_item_course_instructor);
            courseInstitute = (TextView) itemView.findViewById(R.id.recycler_item_course_institute);
            courseType = (TextView) itemView.findViewById(R.id.recycler_item_course_type);
            courseDiscuss = (TextView) itemView.findViewById(R.id.recycler_item_course_discuss);
            courseStrategy = (TextView) itemView.findViewById(R.id.recycler_item_course_strategy);
            courseJoin = (TextView) itemView.findViewById(R.id.recycler_item_course_join);
            cardView.setUseCompatPadding(true);
            LayerDrawable layerDrawable = (LayerDrawable) courseRating.getProgressDrawable();
            for (int i = 0; i < 3; i++) {
                layerDrawable.getDrawable(i).setColorFilter(Color.rgb(255, 215, 0), PorterDuff.Mode.SRC_ATOP);
            }
        }
    }

}
