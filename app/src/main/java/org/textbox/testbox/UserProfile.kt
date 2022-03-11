package org.textbox.testbox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.database.FirebaseDatabase

class UserProfile : AppCompatActivity() {

    lateinit var editTextTextPersonName3: EditText
    lateinit var editTextTextPersonName2: EditText
    lateinit var editTextTextPersonName: EditText

    lateinit var branchSpinner : Spinner

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

        branchSpinner = findViewById(R.id.branchSpinner)

        setUpbranchSpinner()

    }

    private fun setUpbranchSpinner() {
        val adapter = ArrayAdapter.createFromResource(this,
            R.array.branch,android.R.layout.simple_spinner_item)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        branchSpinner.adapter = adapter
        branchSpinner.prompt = "Branch"

        branchSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val selectedItem  = p0!!.getItemAtPosition(p2)
                Toast.makeText(this@UserProfile,"Selected $selectedItem",Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

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

        val ref = FirebaseDatabase.getInstance().getReference("user")
        val heroId = ref.push().key
        val hero=Userclass(heroId,firstName, lastName, email)
        if (heroId != null) {
            ref.child(heroId).setValue(hero).addOnSuccessListener {
                Toast.makeText(applicationContext,"info saved successfully", Toast.LENGTH_LONG).show()
                val intent = Intent(this,Nav_Activity::class.java)
                startActivity(intent)
                finish()
            }
        }

    }
}