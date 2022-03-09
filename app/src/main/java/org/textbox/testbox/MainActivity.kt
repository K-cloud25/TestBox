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
import org.textbox.testbox.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    //Action Bar
    private lateinit var actionBar : ActionBar

    //Progress Window
    private lateinit var progressDialog: ProgressDialog

    //Firebase Auth
    private lateinit var fireAuth : FirebaseAuth
    private var email = ""
    private var password = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //Configure Action Bar
        actionBar = supportActionBar!!
        actionBar.hide()

        //Configure Progress Bar
        progressDialog = ProgressDialog(this)
        progressDialog.setTitle("Please Wait")
        progressDialog.setMessage("Logging in....")
        progressDialog.setCanceledOnTouchOutside(false)

        //FireBase Auth
        fireAuth = FirebaseAuth.getInstance()
        checkUser()

        //SignUpBtn
        binding.signBtn.setOnClickListener {
            startActivity(Intent(this,SignUp::class.java))
            finish()
        }

        //LoginBtn
        binding.loginBtn.setOnClickListener {
            validateData()
        }
    }

    private fun validateData(){
        email = binding.logEmail.text.toString().trim()
        password = binding.logPassword.text.toString().trim()

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            //Put a red border
            binding.logEmail.setText("Incorrect Email Format")
        }
        else if(TextUtils.isEmpty(password)){
            //Put a red border
            binding.logPassword.setText("Please Enter Password")
        }
        else{
            fireBaseLogin()
        }
    }

    private fun fireBaseLogin(){
        progressDialog.show()

        fireAuth.signInWithEmailAndPassword(email,password)
            .addOnSuccessListener {
                progressDialog.dismiss()

                val firebaseUser = fireAuth.currentUser
                val email = firebaseUser!!.email
                Toast.makeText(this,"The Login successful as $email", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this,Nav_Activity::class.java))
                finish()
            }
            .addOnFailureListener { e->
                progressDialog.dismiss()
                Toast.makeText(this,"The Login failed as ${e.message}",Toast.LENGTH_SHORT).show()
            }
    }

    private fun checkUser(){
        //For Logged in user get the user info and key
        val firebaseUser =  fireAuth.currentUser
        if(firebaseUser!=null){
            startActivity(Intent(this,Nav_Activity::class.java))
            finish()
        }
    }

}