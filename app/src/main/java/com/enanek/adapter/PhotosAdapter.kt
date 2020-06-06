package com.enanek.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.enanek.R
import com.enanek.model.response.Photo
import com.squareup.picasso.Picasso


class PhotosAdapter(
    private var context: Context,
    var photo: MutableList<Photo>?
) : RecyclerView.Adapter<PhotosAdapter.PhotoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.photos_adapter_layout, parent, false)
        return PhotoViewHolder(view)
    }

    override fun getItemCount(): Int {
        return photo?.size!!
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val photoDetails = photo?.get(position)
        val imageUrl = context.getString(
            R.string.photo_url,
            photoDetails?.farm.toString().trim(),
            photoDetails?.server?.trim(),
            photoDetails?.id?.trim(),
            photoDetails?.secret
        )
        Picasso.with(context).load(imageUrl).into(holder.photo)

    }

    inner class PhotoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var photo: ImageView

        init {
            photo = itemView.findViewById(R.id.photo)
        }

    }
}