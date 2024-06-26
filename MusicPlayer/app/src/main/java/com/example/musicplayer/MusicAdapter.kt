package com.example.musicplayer

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.databinding.MusicViewBinding
import kotlinx.coroutines.currentCoroutineContext

class MusicAdapter(
    private val context: Context,
    private var musicList: ArrayList<MusicData>,
    private var playlistDetail: Boolean = false,
    private var selectionActivity: Boolean = false
) :
    RecyclerView.Adapter<MusicAdapter.MyViewHolder>() {
    class MyViewHolder(binding: MusicViewBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.songName
        val album = binding.songDescription
        val image = binding.imageMv
        val duration = binding.timeDuration
        val root = binding.root
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(MusicViewBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun getItemCount(): Int {
        return musicList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = musicList[position].title
        holder.album.text = musicList[position].album
        holder.duration.text = formatDuration(musicList[position].duration)
        Glide
            .with(context)
            .load(musicList[position].artUrl)
            .centerCrop()
            .placeholder(R.drawable.musical_player).centerCrop()
            .into(holder.image)
        when {
            playlistDetail -> {
                holder.root.setOnClickListener {
                    sendIntent("PlaylistDetailsAdapter", position)
                }
            }

            selectionActivity -> {
                holder.root.setOnClickListener {
                    if (addSong(musicList[position])) {
                        holder.root.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.cool_pink
                            )
                        )
                    } else
                        holder.root.setBackgroundColor(
                            ContextCompat.getColor(
                                context,
                                R.color.cool_pink
                            )
                        )
                }
            }

            else -> {
                holder.root.setOnClickListener {
                    when {
                        MainActivity.search -> sendIntent("MusicAdapterSearch", position)
                        else -> sendIntent("MusicAdapter", position)
                    }
                }
            }
        }
    }

    fun updateMusicList(searchList: ArrayList<MusicData>) {
        musicList = ArrayList()
        musicList.addAll(searchList)
        notifyDataSetChanged()
    }

    private fun sendIntent(ref: String, position: Int) {
        val intent = Intent(context, PlayerActivity::class.java)
        intent.putExtra("Index", position)
        intent.putExtra("class", ref)
        ContextCompat.startActivity(context, intent, null)
    }

    private fun addSong(song: MusicData): Boolean {
        PlaylistActivity.musicPlaylist.ref[PlaylistDetail.currentPlaylistPosition].playlist.forEachIndexed { index, musicData ->
            if (song.id == musicData.id) {
                PlaylistActivity.musicPlaylist.ref[PlaylistDetail.currentPlaylistPosition].playlist.removeAt(
                    index
                )
                return false
            }
        }
        PlaylistActivity.musicPlaylist.ref[PlaylistDetail.currentPlaylistPosition].playlist.add(song)
        return true
    }

    fun refreshPlaylist(){

        musicList = ArrayList()
        musicList = PlaylistActivity.musicPlaylist.ref[PlaylistDetail.currentPlaylistPosition].playlist
        notifyDataSetChanged()
    }

}
