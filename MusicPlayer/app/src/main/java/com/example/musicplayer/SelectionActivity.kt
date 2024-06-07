package com.example.musicplayer

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.musicplayer.databinding.ActivityPlaylistDetailBinding
import com.example.musicplayer.databinding.ActivitySelectionBinding

class SelectionActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySelectionBinding
    private lateinit var adapter: MusicAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectionBinding.inflate(layoutInflater)

        setContentView(binding.root)


        binding.selectionRv.setItemViewCacheSize(10)
        binding.selectionRv.setHasFixedSize(true)

        adapter = MusicAdapter(
            this,
            MainActivity.MusicListMA, selectionActivity = true
        )
        binding.selectionRv.adapter = adapter


        binding.searchSelectionSA.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean = true

            override fun onQueryTextChange(newText: String?): Boolean {
                MainActivity.musicSearchList = ArrayList()
                if (newText != null) {
                    val userInput = newText.lowercase()
                    for (song in MainActivity.MusicListMA)
                        if (song.title.lowercase().contains(userInput))
                            MainActivity.musicSearchList.add(song)
                    MainActivity.search = true
                    adapter.updateMusicList(MainActivity.musicSearchList)
                }
                return true
            }
        })

        binding.selectionBackIcon.setOnClickListener {
            finish()
        }
    }
}