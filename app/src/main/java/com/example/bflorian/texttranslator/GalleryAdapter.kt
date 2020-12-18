package com.example.bflorian.texttranslator
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.gallery_item.view.*
import java.io.File


public class GalleryAdapter(private val imageFiles: ArrayList<File>) :
    RecyclerView.Adapter<GalleryAdapter.ViewHolder>() {


    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {

        var image: ImageView = view.findViewById(R.id.imageView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.gallery_item, parent, false) as View
        // LW set the view's size, margins, paddings and layout parameters
        val lp = view.getLayoutParams()

        lp.height = GalleryActivity.width / 3
        view.setLayoutParams(lp)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        var image = imageFiles[position]
        val drawable: Drawable = Drawable.createFromPath(image.path)!!
        holder.view.findViewById<ImageView>(R.id.imageView).background = drawable

        holder.itemView.setOnClickListener { view ->
            ImageViewActivity.image = drawable
            val intent: Intent = Intent(holder.itemView.context, ImageViewActivity::class.java)
            holder.itemView.context.startActivity(intent)

        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = imageFiles.size


}