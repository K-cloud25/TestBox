package org.textbox.testbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.ArrayList


class assignmentAdapter(private var title: Array<String>)
    :RecyclerView.Adapter<assignmentAdapter.ViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener{
        fun onItemClick(position:Int)
    }

    fun setItemClickListener(listener: onItemClickListener){
        mListener=listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): assignmentAdapter.ViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.branchestemp,parent,false)

        return ViewHolder(view, mListener)
    }

    override fun onBindViewHolder(holder: assignmentAdapter.ViewHolder, position: Int) {
        holder.itemTitle.text= title[position].toString()
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