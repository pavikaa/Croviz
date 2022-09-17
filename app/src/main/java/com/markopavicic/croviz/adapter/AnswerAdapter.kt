package com.markopavicic.croviz.adapter

import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView
import com.markopavicic.croviz.R
import com.markopavicic.croviz.model.data.Answer
import com.markopavicic.croviz.model.data.Result

class AnswerAdapter(private val context: Context, private val dataset: List<Answer>) :
    RecyclerView.Adapter<AnswerAdapter.ItemViewHolder>() {
    private var numCorrect = 0
    private var numIncorrect = 0

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvAnswer: TextView = view.findViewById(R.id.tv_answer)
        val card: MaterialCardView = view.findViewById(R.id.answer_card)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_answer, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = dataset[position]
        holder.tvAnswer.text = item.answer

        holder.card.setOnClickListener {
            holder.card.toggle()
            if (holder.card.isChecked) {
                if (item.correct)
                    numCorrect += 1
                else
                    numIncorrect += 1
            } else {
                if (item.correct)
                    numCorrect -= 1
                else
                    numIncorrect -= 1
            }
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }

    fun getResults(): Result {
        return Result(numCorrect, numIncorrect)
    }
}