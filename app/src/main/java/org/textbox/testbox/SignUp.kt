package org.textbox.testbox

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import org.textbox.testbox.databinding.ActivitySignUpBinding


class SignUp : AppCompatActivity() {

    //Action Bar
    private lateinit var actionBar : ActionBar

    //Progress Dialogue
    private lateinit var progressDialogue : ProgressDialog

    //FireBase Auth
    private lateinit var fireAuth : FirebaseAuth
    private var email = ""
    private var password = ""

    private lateinit var binding : ActivitySignUpBinding

    private var clickedTimes : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        actionBar = supportActionBar!!
        actionBar.hide()

        //Configure Progress Bar
        progressDialogue = ProgressDialog(this)
        progressDialogue.setTitle("Please Wait")
        progressDialogue.setMessage("Creating Account in....")
        progressDialogue.setCanceledOnTouchOutside(false)

        //FireAuth
        fireAuth = FirebaseAuth.getInstance()

        //SignUp Btn
        binding.signInBtn.setOnClickListener {
            validateData()
        }

        //Club Sign up Button
        clickedTimes = 0
        binding.clubSignUp.setOnClickListener {
            clickedTimes++
            if(clickedTimes==5){
                Toast.makeText(this,"clicked $clickedTimes",Toast.LENGTH_SHORT).show()
            }
            if(clickedTimes==10){
                val intent = Intent(this, ClubSignUp::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun validateData(){
        email = binding.tvEmailID.text.toString().trim()
        password = binding.tvPassWord.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //Turn Button Empty and Turn it Red
            binding.tvEmailID.setText("Incorrect Email Format")
        }
        else if(TextUtils.isEmpty(password)){
            //Turn Button Red
            binding.tvPassWord.setText("Please Enter")
        }
        else{
            signUpFireBase()
        }
    }

    private fun signUpFireBase(){
        progressDialogue.show()
        fireAuth.createUserWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                progressDialogue.dismiss()

                val fireBaseUser = fireAuth.currentUser
                val email = fireBaseUser!!.email
                Toast.makeText(this,"Account created with $email",Toast.LENGTH_SHORT).show()

                startActivity(Intent(this, UserProfile::class.java))
                finish()
            }
            .addOnFailureListener { e->
                progressDialogue.dismiss()
                Toast.makeText(this,"SignUp Failed due to ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    //This Function Moves the page back to Last Intent
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // Going Back to Previous Activity
        return super.onSupportNavigateUp()
    }
}