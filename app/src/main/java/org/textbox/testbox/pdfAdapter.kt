package org.textbox.testbox

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.textbox.testbox.databinding.ActivityAssignmentBranchesBinding
import org.textbox.testbox.databinding.ActivityPdftempBinding

class  pdfAdapter( private var pdfarraylist:ArrayList<pdfclass>):RecyclerView.Adapter<pdfAdapter.MyViewHolder>() {

//    private var context:Context


    private lateinit var binding: ActivityPdftempBinding





    class MyViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val titlepdf1 :TextView=itemView.findViewById(R.id.titlepdf)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView=LayoutInflater.from(parent.context).inflate(R.layout.activity_pdftemp,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
       val model =pdfarraylist[position]
        holder.titlepdf1.text=model.title

    }

    override fun getItemCount(): Int {
        return pdfarraylist.size
    }
}