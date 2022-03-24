package org.textbox.testbox

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.textbox.testbox.databinding.ActivityClubSignUpBinding
import java.util.*

class ClubSignUp : AppCompatActivity() {

    private lateinit var binding : ActivityClubSignUpBinding

    //Date
    private var day : Int = 0
    private var month : Int = 0
    private var year : Int = 0


    private var email : String = " "
    private var password : String = " "

    private lateinit var fireAuth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClubSignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val calendar = Calendar.getInstance()
        day = calendar.get(Calendar.DAY_OF_MONTH)
        month = calendar.get(Calendar.MONTH)
        year = calendar.get(Calendar.YEAR)

        binding.FoundingDate.setOnClickListener {
            val dpd = DatePickerDialog(this,DatePickerDialog.OnDateSetListener {
                    view,
                    mYear,
                    mMonth,
                    mDay->
                val dateFormat = "$mDay/${mMonth+1}/$mYear"
                binding.FoundingDate.setText(dateFormat)
            },year,month,day).show()
        }

        fireAuth = FirebaseAuth.getInstance()

        binding.signUPBtn.setOnClickListener {
            validateAllData()
        }
    }

    private fun validateAllData(){
        val temail = binding.ClubEmail.text.toString()
        val name = binding.ClubName.text.toString()
        val tpasswrd = binding.ClubPassword.text.toString()
        val website = binding.ClubWebsite.text.toString()
        val foundindData = binding.FoundingDate.text.toString()
        val pname = binding.ClubPresidentName.text.toString()

        if(!Patterns.EMAIL_ADDRESS.matcher(temail).matches()){
            binding.ClubEmail.error = " Enter Valid Email Address"
        }
        else if(TextUtils.isEmpty(tpasswrd)){
            binding.ClubPassword.error = "Enter PassWord"
        }else if(TextUtils.isEmpty(name)){
            binding.ClubName.error = "Enter Clubname"
        }else if(TextUtils.isEmpty(website)){
            binding.ClubWebsite.error = "Enter WebSite Link"
        }else if(foundindData == "00/00/0000"){
            binding.FoundingDate.error = "Enter Club Founding Date"
        }else if(TextUtils.isEmpty(pname)){
            binding.ClubPresidentName.error = "Enter Presidents Name"
        }
        else{
            email = temail
            password = tpasswrd
            signUpFireBase()
        }
    }

    private fun signUpFireBase(){
        fireAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {

                val fireBaseUser = fireAuth.currentUser
                val email = fireBaseUser!!.email
                Toast.makeText(this,"Account created with $email",Toast.LENGTH_SHORT).show()
                storeClubData()
            }
            .addOnFailureListener { e->
                Toast.makeText(this,"SignUp Failed due to ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun storeClubData(){
        val cUID = fireAuth.currentUser?.uid
        val cName = binding.ClubName.text.toString()
        val cEmail = binding.ClubEmail.text.toString()
        val cFD = binding.FoundingDate.text.toString()
        val cPname = binding.ClubPresidentName.text.toString()
        val cWebSite = binding.ClubWebsite.text.toString()

        val clubObj = ClubClass(cUID,cName,cEmail,cFD,cPname,cWebSite)

        val DBref = FirebaseDatabase.getInstance().getReference("Clubs")

        if(cUID!=null){
            DBref.child(cUID).setValue(clubObj).addOnSuccessListener {
                Toast.makeText(this,"Data Added Succesfully ",Toast.LENGTH_SHORT).show()
            }
        }
    }
}