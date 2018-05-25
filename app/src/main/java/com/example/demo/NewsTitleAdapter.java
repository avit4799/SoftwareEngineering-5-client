package com.example.demo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;


public class NewsTitleAdapter extends RecyclerView.Adapter<NewsTitleAdapter.ViewHolder> {
    private List<NewsTitle> ntList;
    private final Context context;

    static class ViewHolder extends RecyclerView.ViewHolder{
        View newsView;
        TextView nt_title;
        TextView nt_source;
        TextView nt_date;

        public ViewHolder(View view){
            super(view);
            newsView = view;
            nt_title = (TextView) view.findViewById(R.id.news_title);
            nt_source = (TextView) view.findViewById(R.id.news_source);
            nt_date = (TextView) view.findViewById(R.id.news_date);
        }
    }

    public NewsTitleAdapter(List<NewsTitle> ntList_, Context context_){
        this.ntList = ntList_;
        this.context = context_;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_panel, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.newsView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int position = holder.getAdapterPosition();
                NewsTitle nt = ntList.get(position);
                Intent intent = new Intent(context, NewsActivity.class);
                context.startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position){
        NewsTitle nt = ntList.get(position);
        holder.nt_title.setText(nt.get_title());
        holder.nt_date.setText(nt.get_newsdate());
        holder.nt_source.setText(nt.get_source());
    }

    @Override
    public int getItemCount(){
        return ntList.size();
    }
}
