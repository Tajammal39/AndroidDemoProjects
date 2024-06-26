package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.databinding.PlaylistViewBinding

class PlaylistAdapterView(private val context: Context, private var playListList: ArrayList<Playlist>) :
    RecyclerView.Adapter<PlaylistAdapterView.MyViewHolder>() {
    class MyViewHolder(binding: PlaylistViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val image = binding.playlistImage
        val name = binding.playlistName
        val root = binding.root
        val delete = binding.playlistDeleteBtn
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            PlaylistViewBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return playListList.size
    }

   /* override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.name.text = playListList[position].name
        holder.name.isSelected = true
        holder.delete.setOnClickListener {

            val builder = AlertDialog.Builder(context)
                .setTitle(playListList[position].name)
                .setMessage("Do You Want to delete playlist?")
            //builder.setPositiveButton("OK", DialogInterface.OnClickListener(function = x))

            builder.setPositiveButton("Yes") {dialog, _ ->
                PlaylistActivity.musicPlaylist.ref.removeAt(position)
                refreshPlaylist()
                dialog.dismiss()
            }

            builder.setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

        holder.root.setOnClickListener {
            context.startActivity(Intent(context,PlaylistDetail::class.java)
                .putExtra("index",position))
        }

        if (PlaylistActivity.musicPlaylist.ref[position].playlist.size > 0){
            Glide
                .with(context)
                .load(PlaylistActivity.musicPlaylist.ref[PlaylistDetail.currentPlaylistPosition].playlist[0].artUrl)
                .centerCrop()
                .placeholder(R.drawable.musical_player).centerCrop()
                .into(holder.image)
        }


    }
*/

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        if (position >= 0 && position < playListList.size) {
            holder.name.text = playListList[position].name
            holder.name.isSelected = true
            holder.delete.setOnClickListener {
                val builder = AlertDialog.Builder(context)
                    .setTitle(playListList[position].name)
                    .setMessage("Do You Want to delete playlist?")
                builder.setPositiveButton("Yes") { dialog, _ ->
                    PlaylistActivity.musicPlaylist.ref.removeAt(position)
                    refreshPlaylist()
                    dialog.dismiss()
                }
                builder.setNegativeButton("No") { dialog, _ ->
                    dialog.dismiss()
                }
                builder.show()
            }

            holder.root.setOnClickListener {
                context.startActivity(Intent(context, PlaylistDetail::class.java)
                    .putExtra("index", position))
            }

            if (PlaylistActivity.musicPlaylist.ref[position].playlist.size > 0) {
                Glide
                    .with(context)
                    .load(PlaylistActivity.musicPlaylist.ref[position].playlist[0].artUrl)
                    .centerCrop()
                    .placeholder(R.drawable.musical_player)
                    .into(holder.image)
            }
        }
    }

    fun refreshPlaylist(){
        playListList = ArrayList()
        playListList.addAll(PlaylistActivity.musicPlaylist.ref)
        notifyDataSetChanged()
    }

}