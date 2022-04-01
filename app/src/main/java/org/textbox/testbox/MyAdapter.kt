package org.textbox.testbox

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import org.textbox.testbox.classes.requestClass
import java.util.*
import kotlin.collections.ArrayList

class MyAdapter(private var requestList : ArrayList<requestClass>):
    RecyclerView.Adapter<MyAdapter.MyViewHolder>(){

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

        holder.requestBtn.setOnClickListener {
            val requestID  = currentItem.requestID.toString()
            sendRequestID(it.context,position,requestID)
        }
    }

    override fun getItemCount(): Int {
        return requestList.size
    }

    class MyViewHolder(itemView : View): RecyclerView.ViewHolder(itemView) {
        val userNameText : TextView = itemView.findViewById(R.id.userName)
        val workText : TextView = itemView.findViewById(R.id.workRQU)
        val requiText : TextView = itemView.findViewById(R.id.reqRQU)
        val projectName : TextView = itemView.findViewById(R.id.projectName)
        val requestBtn : Button = itemView.findViewById(R.id.requestBtn)
    }

    private fun sendRequestID(view:Context,position:Int,requestId:String){
        val fireAuth = FirebaseAuth.getInstance()

        val userCheck = requestId.split("_")[1]

        if(userCheck == FirebaseAuth.getInstance().currentUser?.uid){
            Toast.makeText(view,"Bruh",Toast.LENGTH_SHORT).show()
        }else{

            val fireDatabase : DatabaseReference = FirebaseDatabase.getInstance().getReference("teamUps")
            fireDatabase.child(requestId).child("appliedID").child("${fireAuth.currentUser?.uid}")
                .setValue(fireAuth.currentUser?.uid)
                .addOnSuccessListener {
                    Toast.makeText(view,"Request Added",Toast.LENGTH_SHORT).show()
                }

            val cal = Calendar.getInstance()
            val todayDate = cal[Calendar.DAY_OF_YEAR].toString()

            FirebaseDatabase.getInstance().getReference("teamUps")
                .child(requestId).child("validDate").setValue(todayDate)
        }
    }
}