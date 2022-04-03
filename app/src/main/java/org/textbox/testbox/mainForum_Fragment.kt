package org.textbox.testbox

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.lang.Math.random
import java.util.*
import kotlin.collections.ArrayList
import kotlin.random.Random

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class mainForum_Fragment : Fragment() {

    private lateinit var queryBody : EditText

    private lateinit var queryRV : RecyclerView
    private lateinit var queryList : ArrayList<query_Class>

    private lateinit var todayDate : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cal = Calendar.getInstance()
        todayDate = cal[Calendar.DAY_OF_YEAR].toString()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view =  inflater.inflate(R.layout.fragment_main_forum_, container, false)

        queryBody = view.findViewById(R.id.queryTypeET)

        val sendBtn : ImageView = view.findViewById(R.id.sendBtn)
        sendBtn.setOnClickListener {
            sendQuery(view.context,queryBody.text.toString())
        }

        queryRV  = view.findViewById(R.id.queryRV)
        setupRV(view.context)

        return view
    }

    private fun sendQuery(context : Context, queryBodye : String){
        val Dbred = FirebaseDatabase.getInstance().getReference("Query")

        if(!queryBodye.isBlank()){
            val userID = FirebaseAuth.getInstance().currentUser?.uid.toString()
            val queryID = (0 .. 100).random().toString() + userID
            var userName = " "
            val Dbref = FirebaseDatabase.getInstance().getReference("user").child(userID)
            Dbref.child("firstName").get().addOnSuccessListener {
                if(it.exists()){
                    userName = it.value.toString()

                    val cal = Calendar.getInstance()
                    todayDate = cal[Calendar.DAY_OF_YEAR].toString()

                    val obj = query_Class(queryID,userName,userID,queryBodye,todayDate)
                    Dbred.child(queryID).setValue(obj).addOnSuccessListener {
                        queryBody.setText(" ")
                    }
                }
            }
        }
    }

    private fun setupRV(context : Context){
        val DBref = FirebaseDatabase.getInstance().getReference("Query")
        queryRV.layoutManager = LinearLayoutManager(view?.context)
        queryRV.setHasFixedSize(true)

        queryList = arrayListOf(query_Class())

        DBref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    queryList.clear()
                    for(child in snapshot.children){
                        val obj = child.getValue(query_Class::class.java)

                        val validDate = obj?.validDate?.toInt()

                        if(validDate!=null){
                            if(Math.abs((validDate - todayDate.toInt())) >=15){
                                FirebaseDatabase.getInstance().getReference("Query")
                                    .child(obj.queryID.toString()).removeValue()
                        }else{
                            queryList.add(obj!!)
                        }}
                    }
                    queryRV.adapter = Query_Adapter(queryList)
                }
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

    }

    companion object {
        fun newInstance(param1: String, param2: String) =
            mainForum_Fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

