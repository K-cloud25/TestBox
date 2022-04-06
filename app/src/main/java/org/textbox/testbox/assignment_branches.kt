package org.textbox.testbox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import org.textbox.testbox.databinding.ActivityAssignmentBranchesBinding


class assignment_branches : AppCompatActivity(){

    private lateinit var binding : ActivityAssignmentBranchesBinding

    val title= arrayOf(
        "School Of Mechanical Engineering",
        "School Of Computer Engineering and Technology",
        "School Of Electronics Engineering and Communication Engineering",
        "School Of Civil Engineering",
        "School Of Electrical Engineering",
        "School Of Chemical Engineering",
        "School Of Polymer Engineering",
        "School Of Petroleum Engineering")

    val titleShort = arrayOf(
        "MeEng",
        "CseEng",
        "EleCommEng",
        "CivilEng",
        "ElecEng",
        "ChemEng",
        "PolyEng",
        "PetrEng"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignmentBranchesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val yearpath=intent.getStringExtra("year").toString()

        binding.branchesrv.layoutManager = LinearLayoutManager(this)
        binding.branchesrv.setHasFixedSize(true)

        val _adapter = assignmentAdapter(title)
        binding.branchesrv.adapter = _adapter
        _adapter.setItemClickListener(object : assignmentAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {
                val intent = Intent(binding.root.context,Main_Assignment::class.java)
                intent.putExtra("yearPath",yearpath)
                intent.putExtra("branchPath",titleShort[position])
                intent.putExtra("branch",title[position])
                startActivity(intent)
            }

        })
    }
}