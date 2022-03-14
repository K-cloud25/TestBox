package org.textbox.testbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class MyAdapter(private var requestList : ArrayList<requestClass>): RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.request_item_layout,parent,false)

        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = requestList[position]

        holder.userNameText.text = currentItem.userName.toString()
        holder.workText.text = currentItem.work.toString()
        holder.requiText.text = currentItem.requirement.toString()
        holder.projectName.text = currentItem.projectName.toString()
    }

    override fun getItemCount(): Int {
        return requestList.size
    }

    class MyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
        val userNameText : TextView = itemView.findViewById(R.id.userName)
        val workText : TextView = itemView.findViewById(R.id.workRQU)
        val requiText : TextView = itemView.findViewById(R.id.reqRQU)
        val projectName : TextView = itemView.findViewById(R.id.projectName)

    }
}