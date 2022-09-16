package com.example.travelspot.adapter

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

class AnswerAdapter(private val context: Context, private val dataset: MutableList<Answer>) :
    RecyclerView.Adapter<AnswerAdapter.ItemViewHolder>() {
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvAnswer: TextView = view.findViewById(R.id.tv_creation_answer)
        val btnRemoveAnswer: TextView = view.findViewById(R.id.btn_creation_remove_answer)
        val card: MaterialCardView = view.findViewById(R.id.creation_answer_card)
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
        when (item.isCorrect) {
            true -> holder.card.strokeColor = context.getColor(R.color.green)
            else -> holder.card.strokeColor = context.getColor(R.color.red)
        }
        holder.btnRemoveAnswer.setOnClickListener {
            dataset.remove(item)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return dataset.size
    }
}