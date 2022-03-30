package org.textbox.testbox

import android.content.Intent
import android.graphics.BitmapFactory
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class Notice_Adapter(private var noticeList : ArrayList<Notice_Class>)
    :RecyclerView.Adapter<Notice_Adapter.MyViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.notice_item_layout,parent,false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        var currentItem = noticeList[position]

        val id = currentItem.noticeId.toString()

        val Storageref = FirebaseStorage.getInstance().getReference("Notice/$id")
        val localfile= File.createTempFile("uid","jpg")
        Storageref.getFile(localfile).addOnSuccessListener {
            val bitmap= BitmapFactory.decodeFile(localfile.absolutePath)
            holder.imageView.setImageBitmap(bitmap)
        }

        holder.imageView.setOnClickListener {
            Toast.makeText(holder.itemView.context,id,Toast.LENGTH_SHORT).show()

            val intent = Intent(holder.itemView.context,NoticeFullScreen::class.java)
            intent.putExtra("PosterId",currentItem.noticeId)
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    class MyViewHolder(itemview : View) :RecyclerView.ViewHolder(itemview){
        val imageView : ImageView = itemview.findViewById(R.id.mainImageNotice)
    }
}