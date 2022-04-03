package org.textbox.testbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.textbox.testbox.databinding.ActivityQueryRepliesBinding

class QueryReplies : AppCompatActivity() {

    private lateinit var binding : ActivityQueryRepliesBinding

    private lateinit var replyList : ArrayList<Reply_Class>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQueryRepliesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val queryID = intent.getStringExtra("QueryID").toString()

        FirebaseDatabase.getInstance().getReference("Query")
            .child(queryID).get().addOnSuccessListener {
                if(it.exists()){
                    binding.queryUserNameTV.text = it.child("queryUserName").value.toString()
                    binding.queryBodyTV.text = it.child("queryBody").value.toString()
                }
            }

        binding.sendRplBtn.setOnClickListener {
            sendReply(queryID)
        }

        setUpRV(queryID)
    }

    private fun sendReply(queryId : String){
        val repBody = binding.replyTypeET.text.toString()
        val CuserID = FirebaseAuth.getInstance().currentUser?.uid.toString()
        val id = (1 .. 100).random().toString() + CuserID

        val obj = Reply_Class(id,CuserID,repBody,queryId)

        FirebaseDatabase.getInstance().getReference("Query")
            .child(queryId).child("queryUserID").get().addOnSuccessListener {
                if(it.value.toString() != CuserID){
                    var DBref = FirebaseDatabase.getInstance().getReference("Query")
                    .child(queryId).child("Replies").child(id).setValue(obj)
                    .addOnSuccessListener {
                        binding.replyTypeET.setText(" ")
                    }
                }else{
                    Toast.makeText(this,"Cannot Reply To Self",Toast.LENGTH_SHORT).show()
                    binding.replyTypeET.setText(" ")
                }
            }
    }

    private fun setUpRV(queryId: String){

        binding.replyRV.layoutManager = LinearLayoutManager(this)
        binding.replyRV.setHasFixedSize(true)

        replyList = arrayListOf<Reply_Class>()

        val DBref = FirebaseDatabase.getInstance().getReference("Query")
            .child(queryId).child("Replies")
            .addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if(snapshot.exists()){
                        replyList.clear()
                        for (child in snapshot.children){
                            val obj = child.getValue(Reply_Class::class.java)
                            replyList.add(obj!!)
                        }
                        binding.replyRV.adapter = Reply_Adapter(replyList)
                    }
                }

                override fun onCancelled(error: DatabaseError) {}
            })

    }
}