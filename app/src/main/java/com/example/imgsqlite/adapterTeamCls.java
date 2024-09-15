package com.example.imgsqlite;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class adapterTeamCls  extends BaseAdapter {

    private Context context;
    private int layout;
    private List<teamCls> teamClsList;

    public adapterTeamCls(Context context, int layout, List<teamCls> teamClsList) {
        this.context = context;
        this.layout = layout;
        this.teamClsList = teamClsList;
    }

    @Override
    public int getCount() {
        return teamClsList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = inflater.inflate(layout,null);

        TextView tvName = convertView.findViewById(R.id.tvName);
        TextView tvStadium = convertView.findViewById(R.id.tvStadium);
        TextView tvCapacity = convertView.findViewById(R.id.tvCapacity);
        ImageView imgLogo = convertView.findViewById(R.id.imgLogo);
        Button btnEdit = convertView.findViewById(R.id.btnEdit);

        teamCls team = teamClsList.get(position);
        tvName.setText(team.getNAME());
        tvStadium.setText(team.getSTADIUM());
        tvCapacity.setText(team.getCAPACITY());
        imgLogo.setImageBitmap(BitmapUtility.getImage(team.getLOGO()));

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UIDActivity.class);
                intent.putExtra("ID",team.getID());
                intent.putExtra("ACTION", "UPDATE");
                context.startActivity(intent);
            }
        });

        return convertView;
    }
}
