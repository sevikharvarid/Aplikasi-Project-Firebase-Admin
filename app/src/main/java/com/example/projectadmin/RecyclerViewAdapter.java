package com.example.projectadmin;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

//Class Adapter ini Digunakan Untuk Mengatur Bagaimana Data akan Ditampilkan
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {


    //Deklarasi Variable
    private ArrayList<Question> listQuestion;
    private ArrayList<String> listSpecificQuestion;
    private Context context;
    private FirebaseUser auth;
    private String key;

    //Membuat Interfece
    public interface dataListener {
        void onDeleteData(Question data, int position);
    }

    dataListener listener;

    public RecyclerViewAdapter(ArrayList<Question> listQuestion, Context context, dataListener listener) {
        this.listQuestion = listQuestion;
        this.context = context;
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView Email, Isi;
        private CardView ListItem;

        ViewHolder(View itemView) {
            super(itemView);
            Email = itemView.findViewById(R.id.question_author);
            Isi = itemView.findViewById(R.id.question_title);
            ListItem = itemView.findViewById(R.id.card_view);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View V = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_layout, parent, false);
        return new ViewHolder(V);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        final String Email = listQuestion.get(position).getAuthor();
        final String Isi = listQuestion.get(position).getDescription();

        holder.Email.setText(Email);
        holder.Isi.setText(Isi);
        holder.ListItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("dataCategory", listQuestion.get(position).getCategory());
                bundle.putString("dataAuthor", listQuestion.get(position).getAuthor());
                bundle.putString("dataDescription", listQuestion.get(position).getDescription());
                bundle.putString("question_id", listQuestion.get(position).getId());
                Intent intent = new Intent(view.getContext(), ViewQuestion.class);
                intent.putExtras(bundle);
                context.startActivity(intent);

            }
        });
        holder.ListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                final String[] action = {"Delete"};
                AlertDialog.Builder alert = new AlertDialog.Builder(view.getContext());
                alert.setItems(action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int i) {
                        switch (i) {
                            case 0:
                                listener.onDeleteData(listQuestion.get(position), position);
                                break;
                        }
                    }
                });
                alert.create();
                alert.show();
                return true;
            }
        });
    }

    @Override
    public int getItemCount() {
        return listQuestion.size();
    }


}