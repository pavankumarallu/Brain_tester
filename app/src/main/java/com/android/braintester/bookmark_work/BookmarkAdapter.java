package com.android.braintester.bookmark_work;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.braintester.Questions_work.QuestionModel;
import com.android.braintester.R;

import java.util.List;

public class BookmarkAdapter extends RecyclerView.Adapter<BookmarkAdapter.Viewholder> {
    private List<QuestionModel> list;

    public BookmarkAdapter(List<QuestionModel> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bookmark_item,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        holder.setData(list.get(position).getQuestion(),list.get(position).getCorrectAnswer(),position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{

        private TextView question,answer;
        private ImageButton delete;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question_b);
            answer = itemView.findViewById(R.id.Answer_b);
            delete = itemView.findViewById(R.id.delete);



        }

        private void setData(String question, String answer, final int position){
            this.question.setText(question);
            this.answer.setText(answer);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    list.remove(position);
                    notifyItemRemoved(position);
                }
            });
        }
    }
}
