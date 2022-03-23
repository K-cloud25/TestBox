package org.textbox.testbox

import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import org.intellij.lang.annotations.JdkConstants
import org.textbox.testbox.databinding.ActivityClubSignUpBinding
import java.text.SimpleDateFormat
import java.util.*

class ClubSignUp : AppCompatActivity() {

    private lateinit var binding : ActivityClubSignUpBinding

    //Date
    private var day : Int = 0
    private var month : Int = 0
    private var year : Int = 0


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
                var dateFormat = "$mDay/${mMonth+1}/$mYear"
                binding.FoundingDate.setText(dateFormat)
            },year,month,day).show()
        }

    }
}