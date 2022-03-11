package org.textbox.testbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.textbox.testbox.databinding.ActivityMainBinding
private lateinit var binding: ActivityMainBinding


//Firebase Auth
private lateinit var fireAuth: FirebaseAuth
private lateinit var firebaseDatabase: FirebaseDatabase


private lateinit var textView: TextView
class DispActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getData()

    }




    private fun getData(){

//        val firebaseUser =  fireAuth.currentUser
//        val uid = firebaseUser?.uid
//        val ref= FirebaseDatabase.getInstance().getReference()
//        if (uid != null) {
//            ref.child(uid).get().addOnSuccessListener {
//                val firstname = it.child("firsName").value.toString()
//                textView.setText(firstname)
//
//
//            }
//        }
        val ref = FirebaseDatabase.getInstance().getReference("user")
        ref.child(fireAuth.uid!!)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val firstname = "${snapshot.child("firstName").value}"
                    textView.text=firstname
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            })
    }


}