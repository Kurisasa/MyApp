package com.example.zapper.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.zapper.R
import com.example.zapper.model.Person

import kotlinx.android.synthetic.main.row_layout.view.*

class PersonAdapter (private val peopleList : List<Person>, private val listener : Listener) : RecyclerView.Adapter<PersonAdapter.ViewHolder>() {
    interface Listener {
        fun onItemClick(person: Person)
    }

    private val colors : Array<String> = arrayOf("#7E57C2", "#42A5F5", "#26C6DA", "#66BB6A", "#FFEE58", "#FF7043" , "#EC407A" , "#d32f2f")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(peopleList[position], listener, colors, position)
    }

    override fun getItemCount(): Int = peopleList.count()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.row_layout, parent, false)
        return ViewHolder(view)
    }

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {
        fun bind(person: Person, listener: Listener, colors : Array<String>, position: Int) {

            itemView.setOnClickListener{ listener.onItemClick(person) }
            itemView.setBackgroundColor(Color.parseColor(colors[position % 8]))
            itemView.text_name.text = person.name
        }
    }
}