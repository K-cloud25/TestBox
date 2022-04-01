package org.textbox.testbox

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import kotlinx.coroutines.delay

class pdf : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdf)
        val branch:TextView=findViewById(R.id.textView)
        val year:TextView=findViewById(R.id.textView1)

        var yearpath = intent.getStringExtra("year1")
        var branches = intent.getStringExtra("title")

        year.text=yearpath
        branch.text=branches

    }
}