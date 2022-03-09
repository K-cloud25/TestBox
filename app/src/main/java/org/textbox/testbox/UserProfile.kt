package org.textbox.testbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.FirebaseDatabase

class UserProfile : AppCompatActivity() {

    lateinit var editTextTextPersonName3: EditText
    lateinit var editTextTextPersonName2: EditText
    lateinit var editTextTextPersonName: EditText
    private val button by lazy {
        findViewById(R.id.button) as Button
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)
        editTextTextPersonName3 = findViewById (R.id.editTextTextPersonName3)!!
        editTextTextPersonName2 = findViewById (R.id.editTextTextPersonName2)!!
        editTextTextPersonName = findViewById (R.id.editTextTextPersonName)!!

        button.setOnClickListener(){
            saveHero()
        }

    }
    private fun saveHero(){
        val firstName=editTextTextPersonName3.text.toString().trim()
        val lastName=editTextTextPersonName.text.toString().trim()
        val email=editTextTextPersonName2.text.toString().trim()

        if(firstName.isEmpty()){
            editTextTextPersonName3.error="Please enter the First Name"
            return
        }
        if(lastName.isEmpty()){
            editTextTextPersonName.error="Please enter the Last Name"
            return
        }
        if(email.isEmpty()){
            editTextTextPersonName2.error="Please enter the Email ID"
            return
        }

        val ref=FirebaseDatabase.getInstance().getReference("user")
        val heroId=ref.push().key
        val hero=Userclass(heroId,firstName, lastName, email)
        if (heroId != null) {
            ref.child(heroId).setValue(hero).addOnSuccessListener {
                Toast.makeText(applicationContext,"info saved successfully", Toast.LENGTH_LONG).show()
            }
        }


    }
}