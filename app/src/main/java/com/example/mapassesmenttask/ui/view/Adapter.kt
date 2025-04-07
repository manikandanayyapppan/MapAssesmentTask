package com.example.mapassesmenttask.ui.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mapassesmenttask.R
import com.example.mapassesmenttask.core.data.model.MapResponse

class Adapter(private var objects: List<MapResponse?>) :
    RecyclerView.Adapter<Adapter.ObjectViewHolder>() {

    fun updateData(newList: List<MapResponse?>) {
        objects = newList
        notifyDataSetChanged()
    }

    inner class ObjectViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val nameText: TextView = view.findViewById(R.id.uiTvName)
        val capacityText: TextView = view.findViewById(R.id.uiTvCapacity)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ObjectViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item, parent, false)
        return ObjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ObjectViewHolder, position: Int) {
        val item = objects[position]
        holder.nameText.text = item?.name
        holder.capacityText.text = "Capacity: ${item?.data?.capacity ?: "N/A"}"
    }

    override fun getItemCount(): Int = objects.size
}