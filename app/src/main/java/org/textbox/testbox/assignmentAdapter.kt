package org.textbox.testbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
val title= arrayOf("CS","ECE","MECH","POLY","PETRO","ELE")

private lateinit var mListener: assignmentAdapter.onItemClickListener


class assignmentAdapter():RecyclerView.Adapter<assignmentAdapter.ViewHolder>() {
    interface onItemClickListener{
        fun onItemClick(position:Int)

    }
    fun setItemClickListener(listener: onItemClickListener){
        mListener=listener
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): assignmentAdapter.ViewHolder {
      val v=LayoutInflater.from(parent.context).inflate(R.layout.branchestemp,parent,false)
        return ViewHolder(v, mListener)

    }

    override fun onBindViewHolder(holder: assignmentAdapter.ViewHolder, position: Int) {

        holder.itemTitle.text=title[position]
    }

    override fun getItemCount(): Int {
        return title.size
    }
    inner class ViewHolder(itemView:View,listener: onItemClickListener):RecyclerView.ViewHolder(itemView){
        var itemTitle:TextView=itemView.findViewById(R.id.branchname)
        init{
            itemView.setOnClickListener{
                listener.onItemClick(adapterPosition)
            }

        }


    }
}