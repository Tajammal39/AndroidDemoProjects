package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.databinding.FavouriteViewBinding

class FavouriteAdapter(private val context: Context, private var musicList: ArrayList<MusicData>) :
    RecyclerView.Adapter<FavouriteAdapter.MyViewHolder>() {
    class MyViewHolder(binding: FavouriteViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.songImgFv
        val name = binding.songNameFv
        val root = binding.root

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            FavouriteViewBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }


    override fun getItemCount(): Int {
        return musicList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = musicList[position].title
        Glide
            .with(context)
            .load(musicList[position].artUrl)
            .centerCrop()
            .placeholder(R.drawable.musical_player).centerCrop()
            .into(holder.image)

        holder.root.setOnClickListener {
            val intent = Intent(context, PlayerActivity::class.java)
            intent.putExtra("Index", position)
            intent.putExtra("class", "FavouriteAdapter")
            ContextCompat.startActivity(context, intent, null)
        }
    }

    /*  fun updateMusicList(searchList: ArrayList<MusicData>) {
          musicList = ArrayList()
          musicList.addAll(searchList)
          notifyDataSetChanged()
      }

      private fun sendIntent(ref: String, position: Int) {
          val intent = Intent(context, PlayerActivity::class.java)
          intent.putExtra("Index", position)
          intent.putExtra("class", ref)
          ContextCompat.startActivity(context, intent, null)
      }*/
}