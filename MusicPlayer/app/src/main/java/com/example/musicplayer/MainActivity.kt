package com.example.musicplayer

import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.musicplayer.databinding.ActivityMainBinding
import java.io.File
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
//    private lateinit var musicRecycler: RecyclerView
    private lateinit var musicAdapter: MusicAdapter

    companion object {
        var MusicListMA: ArrayList<MusicData> = ArrayList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        setupNavigationDrawer()

        // Check for runtime permissions and initialize layout if granted
        if (requestRunTimePermission()) {
            initializeLayout()
        }

        viewBinding.shufflcon.setOnClickListener {
            startActivity(
                Intent(this, PlayerActivity::class.java).putExtra(
                    "Index", 0
                ).putExtra("class", "MainActivity")
            )
        }

        viewBinding.favouriteIcon.setOnClickListener {
            startActivity(Intent(this, FavouiteActivity::class.java))
        }

        viewBinding.playlistIcon.setOnClickListener {
            startActivity(Intent(this, PlaylistActivity::class.java))
        }
    }


    private fun initializeLayout() {
        MusicListMA.addAll(getAllAudio())
        musicAdapter = MusicAdapter(this, MusicListMA)
        viewBinding.musicRv.adapter = musicAdapter
        viewBinding.textSong.text = "Total Songs : " + musicAdapter.itemCount
    }

    private fun setupNavigationDrawer() {
        toggle =
            ActionBarDrawerToggle(this, viewBinding.drawerLayout, R.string.open, R.string.close)

        viewBinding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewBinding.navView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navFeedback -> Toast.makeText(this, "Feedback", Toast.LENGTH_SHORT).show()
                R.id.navSettings -> Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show()
                R.id.navAbout -> Toast.makeText(this, "About", Toast.LENGTH_SHORT).show()
                R.id.navExit -> exitProcess(1)
            }
            true
        }

    }

    //for requesting permission
    private fun requestRunTimePermission(): Boolean {
        // Check if the permission is not granted
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {

            // Request the permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                11
            )
            return false
        }
        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 11) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
           initializeLayout()
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                // Request the permission
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    11
                )
//                requestRunTimePermission()
            }
        }
    }

    @Suppress("UNREACHABLE_CODE")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item))
            return true
        return super.onOptionsItemSelected(item)
    }

    @SuppressLint("Range")
    private fun getAllAudio(): ArrayList<MusicData> {
        val tempList = ArrayList<MusicData>()

        val selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0"
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ALBUM,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATE_ADDED,
            MediaStore.Audio.Media.DATA,
            MediaStore.Audio.Media.ALBUM_ID

        )
        val cursor = this.contentResolver.query(
            MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            null,
            MediaStore.Audio.Media.DATE_ADDED + " DESC", null
        )

        if (cursor != null && cursor.moveToFirst()) {
            do {
                val title = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                val id = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                val album = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM))
                val artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                val path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                val duration =
                    cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                val albumId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID))
                    .toString()
                val uri = Uri.parse("content://media/external/audio/albumart")
                val artUri = Uri.withAppendedPath(uri, albumId).toString()
                // Create a MusicData object and add it to the tempList
                val musicData = MusicData(id, title, album, artist, path, duration, artUri)
                val file = File(musicData.path)
                if (file.exists())
                    tempList.add(musicData)
            } while (cursor.moveToNext())

            cursor.close()
        }

        return tempList
    }

}