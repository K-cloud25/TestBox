package org.textbox.testbox

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class Reply_Adapter(private var replyList : ArrayList<Reply_Class>)
    :RecyclerView.Adapter<Reply_Adapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.reply_query_layout,
            parent,
            false
        )
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = replyList[position]

        holder.replyBody.text = currentItem.replyBody.toString()
        holder.removeBtn.setOnClickListener {
            deleteRpl(
                currentItem.queryID.toString(),
                currentItem.replyId.toString(),
                currentItem.senderID.toString())
        }

        FirebaseDatabase.getInstance().getReference("user")
            .child(currentItem.senderID.toString()).child("firstName")
            .get().addOnSuccessListener {
                if(it.value == null){
                    FirebaseDatabase.getInstance().getReference("Clubs")
                        .child(currentItem.senderID.toString())
                        .child("clubName").get().addOnSuccessListener {
                            holder.replyUserName.text = it.value.toString()
                        }
                }else{
                    holder.replyUserName.text = it.value.toString()
                }
            }
    }

    override fun getItemCount(): Int {
        return replyList.size
    }

    class MyViewHolder(itemView : View):RecyclerView.ViewHolder(itemView){
        val replyUserName : TextView = itemView.findViewById(R.id.userNameReply)
        val replyBody : TextView = itemView.findViewById(R.id.replyBody)
                val removeBtn : ImageView = itemView.findViewById(R.id.removeReply)
    }

    private fun deleteRpl(id : String,rpID : String,senderID :String){
        FirebaseDatabase.getInstance().getReference("Query").child(id)
            .child("queryUserID").get().addOnSuccessListener {
                if (it.exists()){
                    if(it.value.toString() == FirebaseAuth.getInstance().currentUser?.uid.toString())
                    {
                        FirebaseDatabase.getInstance().getReference("Query").child(id)
                            .child("Replies").child(rpID).removeValue()
                    }
                }
            }
    }
}