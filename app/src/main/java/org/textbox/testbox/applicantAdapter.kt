package org.textbox.testbox.adapters

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import org.textbox.testbox.R
import org.textbox.testbox.classes.applicantClass

class applicantAdapter(private var applicationList : ArrayList<applicantClass>)
    : RecyclerView.Adapter<applicantAdapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(
            R.layout.applicant_item_layout,
            parent,
            false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentItem = applicationList[position]

        holder.firstNameTV.text = currentItem.firstName
        holder.lastNameTV.text = currentItem.lastName

        holder.acceptBtn.setOnClickListener {
            Toast.makeText(it.context,currentItem.email.toString(),Toast.LENGTH_SHORT).show()
            holder.emailEV.setText(currentItem.email.toString())

            val intent = Intent(Intent.ACTION_SENDTO).apply{
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(currentItem.email.toString()))
                putExtra(Intent.EXTRA_SUBJECT,currentItem.projectID.toString())
                putExtra(Intent.EXTRA_TEXT,currentItem.email.toString())
            }
            holder.itemView.context.startActivity(intent)

        }
        holder.rejectBtn.setOnClickListener {
            val DBref = FirebaseDatabase.getInstance().getReference("teamUps").child(currentItem.projectID.toString())
            DBref.child("appliedID").child(currentItem.id.toString()).removeValue()
        }
    }

    override fun getItemCount(): Int {
        return applicationList.size
    }

    class MyViewHolder(itemview :View) :RecyclerView.ViewHolder(itemview){
        val rejectBtn : Button = itemview.findViewById(R.id.rejectBtn)
        val acceptBtn : Button = itemview.findViewById(R.id.acceptBtn)
        val firstNameTV : TextView = itemview.findViewById(R.id.applicantFirstName)
        val lastNameTV : TextView = itemview.findViewById(R.id.applicantLastName)
        val emailEV : EditText = itemview.findViewById(R.id.applicantEmail)
    }
}