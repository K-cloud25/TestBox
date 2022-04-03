package org.textbox.testbox

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import org.textbox.testbox.databinding.ActivityMainAssignmentBinding
import org.w3c.dom.Text

class Main_Assignment : AppCompatActivity() {

    private lateinit var binding : ActivityMainAssignmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        val yearPath = intent.getStringExtra("yearPath").toString()
        val branchPath = intent.getStringExtra("branchPath").toString()
        val branchName = intent.getStringExtra("branch").toString()

        binding.addBtn.setOnClickListener {
            AddPDF(binding.root.context,yearPath,branchPath,branchName)
        }
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
    }
}