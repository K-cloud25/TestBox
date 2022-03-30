package org.textbox.testbox

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import org.textbox.testbox.databinding.ActivityAssignmentBinding

class assignment : AppCompatActivity() {
    private lateinit var binding : ActivityAssignmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAssignmentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.card1.setOnClickListener{
            startbranch("FirstYear")

        }
        binding.card2.setOnClickListener{
            startbranch("SecondYear")

        }
        binding.card3.setOnClickListener{
            startbranch("ThirdYear")

        }
        binding.card4.setOnClickListener{
            startbranch("FourthYear")

        }




    }
    private fun startbranch(Year:String){
        val intent= Intent(this,assignment_branches::class.java)
        intent.putExtra("year",Year)
        startActivity(intent)

    }



    
    }

