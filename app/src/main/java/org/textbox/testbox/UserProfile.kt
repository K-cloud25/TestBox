package org.textbox.testbox

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import org.textbox.testbox.databinding.ActivityUserProfileBinding

private lateinit var binding: ActivityUserProfileBinding


//Firebase Auth
private lateinit var firebaseStorage: FirebaseStorage
private lateinit var fireAuth: FirebaseAuth
private lateinit var firebaseDatabase: FirebaseDatabase


lateinit var ImageUri:Uri

class UserProfile : AppCompatActivity() {

    lateinit var editTextTextPersonName3: EditText
    lateinit var editTextTextPersonName2: EditText
    lateinit var editTextTextPersonName: EditText

    lateinit var branchSpinner : Spinner

    lateinit var fireAuth : FirebaseAuth

    private val button by lazy {
        findViewById(R.id.button) as Button
    }
    var branchtext=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        editTextTextPersonName3 = findViewById (R.id.editTextTextPersonName3)!!
        editTextTextPersonName2 = findViewById (R.id.editTextTextPersonName2)!!
        editTextTextPersonName = findViewById (R.id.editTextTextPersonName)!!



        binding.selectimage.setOnClickListener{
            SelectImage()
        }




        button.setOnClickListener(){
            UploadImage()
            saveHero()




        }

        branchSpinner = findViewById(R.id.branchSpinner)
        fireAuth = FirebaseAuth.getInstance()

        setUpbranchSpinner()



    }

    private fun UploadImage() {

        val progress=ProgressDialog(this)
        progress.setMessage("Uploading File ...")
        progress.setCancelable(false)
        progress.show()
        val filename=fireAuth.currentUser?.uid
        val Storageref =FirebaseStorage.getInstance().getReference("ProfilePic/$filename")
        if( ImageUri!=null){
        Storageref.putFile(ImageUri).addOnSuccessListener {
            binding.profilePic.setImageURI(null)
            Toast.makeText(this@UserProfile,"Successfully Uploaded",Toast.LENGTH_SHORT).show()

        }}


    }

    private fun SelectImage() {
        val intent=Intent()
        intent.type="image/"
        intent.action=Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,100)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if( requestCode==100 && resultCode== RESULT_OK){
            ImageUri=data?.data!!
            binding.profilePic.setImageURI(ImageUri)
        }
    }
    private fun setUpbranchSpinner() {

        val adapter = ArrayAdapter.createFromResource(this,
            R.array.branch,android.R.layout.simple_spinner_item)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        branchSpinner.adapter = adapter
        branchSpinner.prompt = "Branch"

        branchSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {


                 val selectedItem = p0!!.getItemAtPosition(p2)
                 branchtext=selectedItem.toString()

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
        val heroId = fireAuth.currentUser?.uid
        val hero= Userclass(heroId,firstName, lastName, email,branchtext)
        if (heroId != null) {
            ref.child(heroId).setValue(hero).addOnSuccessListener {
                Toast.makeText(applicationContext,"info saved successfully", Toast.LENGTH_LONG).show()
                val intent = Intent(this, Nav_Activity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }
}