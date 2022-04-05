package org.textbox.testbox

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import org.textbox.testbox.databinding.ActivityMainAssignmentBinding
import org.w3c.dom.Text
import java.net.URI

class Main_Assignment : AppCompatActivity() {

    private lateinit var binding : ActivityMainAssignmentBinding
    private lateinit var fireAuth: FirebaseAuth
    private lateinit var firebaseDatabase: FirebaseDatabase
    private lateinit var firebaseStorage: FirebaseStorage
    lateinit var uri: URI





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val yearPath = intent.getStringExtra("yearPath").toString()
        val branchPath = intent.getStringExtra("branchPath").toString()
        val branchName = intent.getStringExtra("branch").toString()




        val database= FirebaseDatabase.getInstance ().getReference(  "Uploads pdf")
        val storage = FirebaseStorage.getInstance().getReference("uploads pdf")



        binding.addBtn.setOnClickListener {
            AddPDF(binding.root.context,yearPath,branchPath,branchName)


        }



    }


    private fun SelectPDF() {
         val intent = Intent()
        intent.setType ("pdf/*")
        intent.setAction (Intent.ACTION_GET_CONTENT)
        val PDF : Int=0
        startActivityForResult (Intent.createChooser(intent,  "Select PDF"), PDF)
    }

    private fun AddPDF(context : Context,yearPath : String,branchPath : String,branchName : String){

        val window = AlertDialog.Builder(context)
        val view = layoutInflater.inflate(R.layout.pop_add_assignment_layout,null)

        window.setView(view)
        val root = window.create()
        root.show()

        val YearTV : TextView? = root.findViewById(R.id.yearPathTV)
        YearTV?.text = "Year : $yearPath"

        val branchTV : TextView? = root.findViewById(R.id.branchPathTV)
        branchTV?.text = "Branch : $branchName"

        val uploadBtn : Button? = root.findViewById(R.id.uploadPdfBtn)
        if (uploadBtn != null) {
            uploadBtn.setOnClickListener{
                SelectPDF()
            }
        }
    }


}