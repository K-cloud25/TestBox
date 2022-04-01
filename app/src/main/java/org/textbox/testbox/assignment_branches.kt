package org.textbox.testbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.textbox.testbox.databinding.ActivityAssignmentBranchesBinding


class assignment_branches : AppCompatActivity(){

    private lateinit var binding : ActivityAssignmentBranchesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignmentBranchesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val yearpath=intent.getStringExtra("year").toString()
        Toast.makeText(this, yearpath, Toast.LENGTH_SHORT).show()
    }
}