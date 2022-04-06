package org.textbox.testbox

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import org.textbox.testbox.databinding.ActivityChangeProfileBinding
import java.net.URI

class ChangeProfile : AppCompatActivity() {

    private lateinit var binding : ActivityChangeProfileBinding

    private lateinit var fireAuth : FirebaseAuth

    var branchtext = ""
    var branch = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChangeProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        fireAuth = FirebaseAuth.getInstance()

        setOriInfo()
        setUpbranchSpinner()

        binding.button.setOnClickListener {
            SaveInfo()
        }

        binding.selectimage.setOnClickListener {
            SelectImage()
        }

        ImageUri= Uri.parse("android.resource://$packageName/${R.drawable.profilepic}")
    }
    private fun UploadImage() {

        val progress= ProgressDialog(this)
        progress.setMessage("Uploading File ...")
        progress.setCancelable(false)
        progress.show()
        val filename=fireAuth.currentUser?.uid
        val Storageref = FirebaseStorage.getInstance().getReference("ProfilePic/$filename")
        if(ImageUri != Uri.parse("android.resource://$packageName/${R.drawable.profilepic}")){
            Storageref.putFile(ImageUri).addOnSuccessListener {
                binding.profilePic.setImageURI(null)
                Toast.makeText(this,"Successfully Uploaded", Toast.LENGTH_SHORT).show()
                progress.dismiss()
            }
        }else{
            progress.dismiss()
        }
    }

    private fun SelectImage() {
        val intent= Intent()
        intent.type="image/"
        intent.action= Intent.ACTION_GET_CONTENT
        startActivityForResult(intent,100)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if( requestCode==100 && resultCode== RESULT_OK){
            ImageUri=data?.data!!
            binding.profilePic.setImageURI(ImageUri)
        }
    }

    private fun setOriInfo(){
        val UID = fireAuth.currentUser?.uid.toString()

        FirebaseDatabase.getInstance().getReference("user").child(UID)
            .get().addOnSuccessListener {
                val userName : String = it.child("firstName").value.toString()
               binding.editTextTextPersonName3.setText(userName)

                val lastName : String = it.child("lastName").value.toString()
                binding.editTextTextPersonName.setText(lastName)

                val email : String = it.child("email").value.toString()
                binding.editTextTextPersonName2.setText(email)

                branch = it.child("branch").value.toString()
            }
    }

    private fun SaveInfo(){
        val firstName=binding.editTextTextPersonName3.text.toString().trim()
        val lastName=binding.editTextTextPersonName.text.toString().trim()
        val email=binding.editTextTextPersonName2.text.toString().trim()

        if(firstName.isEmpty()){
            binding.editTextTextPersonName3.error="Please enter the First Name"
            return
        }
        if(lastName.isEmpty()){
            binding.editTextTextPersonName.error="Please enter the Last Name"
            return
        }
        if(email.isEmpty()){
            binding.editTextTextPersonName2.error="Please enter the Email ID"
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
        UploadImage()
    }

    private fun setUpbranchSpinner() {

        val adapter = ArrayAdapter.createFromResource(this,
            R.array.branch,android.R.layout.simple_spinner_item)

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.branchSpinner.adapter = adapter
        binding.branchSpinner.prompt = "Branch"

        binding.branchSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selectedItem = p0!!.getItemAtPosition(p2)
                branchtext=selectedItem.toString()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                branchtext = branch
            }

        }
    }
}