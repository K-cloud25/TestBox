package org.textbox.testbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import org.textbox.testbox.databinding.ActivityAssignmentBinding
import org.textbox.testbox.databinding.ActivityMainBinding


class assignment_branches : AppCompatActivity() {
    private lateinit var binding: ActivityAssignmentBinding

    private lateinit var rv:RecyclerView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)
        rv=findViewById(R.id.branchesrv)

        var yearpath=intent.getStringExtra("year" )
        Toast.makeText(this, yearpath, Toast.LENGTH_SHORT).show()

    }
}