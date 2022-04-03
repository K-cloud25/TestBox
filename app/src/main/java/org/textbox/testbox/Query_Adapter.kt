package org.textbox.testbox

import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class Query_Adapter(private var queryList : ArrayList<query_Class>)
    :RecyclerView.Adapter<Query_Adapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.query_request_layout,
            parent,
            false
        )

        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = queryList[position]

        holder.queryUserName.text = currentItem.queryUserName
        holder.queryBody.text = currentItem.queryBody

        holder.replyBtn.setOnClickListener {
            val intent = Intent(holder.itemView.context,QueryReplies::class.java)
            intent.putExtra("QueryID",currentItem.queryID)
            holder.itemView.context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return queryList.size
    }

    class MyViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val queryUserName : TextView = itemView.findViewById(R.id.queryUsername)
        val queryBody : TextView = itemView.findViewById(R.id.queryBody)
        val replyBtn : Button = itemView.findViewById(R.id.replyBtn)
    }
    }