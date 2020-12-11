package com.example.projects.report;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_report, container, false);
        BarChart chart = v.findViewById(R.id.chart);
        TextView initial = v.findViewById(R.id.initial);
        TextView month = v.findViewById(R.id.month);
        TextView year = v.findViewById(R.id.year);

        List<BarEntry> uploads = new ArrayList<>();
        ArrayList<String> s = new ArrayList<>();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child("data");
        databaseReference.addValueEventListener(new ValueEventListener() {
            int i=0;
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chart.clear();
                uploads.clear();
                s.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    String date = postSnapshot.child("date").getValue().toString();
                    String in = postSnapshot.child("investment_invest_value").getValue().toString();
                    String current = postSnapshot.child("investment_current_value").getValue().toString();
                    String m = postSnapshot.child("month").getValue().toString();
                    String y = postSnapshot.child("year").getValue().toString();
                    Float f = Float.parseFloat(current);
                    uploads.add(new BarEntry(f,i));
                    s.add(date);
                    initial.setText("Rs."+in);
                    month.setText(m);
                    year.setText(","+y);
                    i=i+1;
                }
                BarDataSet barDataSet = new BarDataSet(uploads,"Current Value of investmnet");
                barDataSet.setColors(Collections.singletonList(Color.BLUE));
                BarData barData = new BarData(s,barDataSet);
                chart.setData(barData);
                chart.animateX(2000);
                chart.animateY(2000);
                chart.invalidate();
                chart.setDescription("");
                chart.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


        return v;
    }
}