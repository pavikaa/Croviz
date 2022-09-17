package com.markopavicic.croviz.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.markopavicic.croviz.R
import com.markopavicic.croviz.model.data.Quiz
import com.markopavicic.croviz.ui.activity.QuizActivity
import com.markopavicic.croviz.utils.Constants

class QuizAdapter(
    private val context: Context,
    private val dataset: MutableList<Quiz>
) :
    RecyclerView.Adapter<QuizAdapter.ItemViewHolder>() {
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvQuizName: TextView = view.findViewById(R.id.rv_quiz_name)
        val ivQuizImage: ImageView = view.findViewById(R.id.rv_quiz_image)
        val cardQuizImage: MaterialCardView = view.findViewById(R.id.rv_quiz_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_quiz, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.tvQuizName.text = item.quizName
        when (item.quizCategory) {
            "general_knowledge" -> {
                holder.ivQuizImage.setImageDrawable(context.getDrawable(R.drawable.general))
            }
            "music" -> {
                holder.ivQuizImage.setImageDrawable(context.getDrawable(R.drawable.music))
            }
            "tv" -> {
                holder.ivQuizImage.setImageDrawable(context.getDrawable(R.drawable.tv))
            }
            "science" -> {
                holder.ivQuizImage.setImageDrawable(context.getDrawable(R.drawable.science))
            }
            "history" -> {
                holder.ivQuizImage.setImageDrawable(context.getDrawable(R.drawable.history))
            }
            "geography" -> {
                holder.ivQuizImage.setImageDrawable(context.getDrawable(R.drawable.geography))
            }
            else -> {
                holder.ivQuizImage.setImageDrawable(context.getDrawable(R.drawable.sports))
            }
        }
        holder.cardQuizImage.setOnClickListener {
            val intent = Intent(context, QuizActivity::class.java)
            intent.putExtra(Constants.QUIZ_ID_KEY, item.quizId)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}