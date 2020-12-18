package com.example.bflorian.texttranslator

import android.Manifest.*
import android.content.pm.PackageManager
import android.media.Image
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File


class GalleryActivity : AppCompatActivity() {


    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: GalleryAdapter
    private lateinit var viewManager: RecyclerView.LayoutManager

    private lateinit var gallery_number: TextView

    private var MY_READ_PERMISSION_CODE = 101;

    companion object {
        var imageFiles = ArrayList<File>()
        var width = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        width = displayMetrics.widthPixels

        viewManager  = GridLayoutManager(this, 3, GridLayoutManager.VERTICAL,false)

        viewAdapter = GalleryAdapter(imageFiles)

        recyclerView = findViewById<RecyclerView>(R.id.recyclerview_gallery).apply {
            // use this setting to improve performance if you know that changes
            // in content do not change the layout size of the RecyclerView
            //setHasFixedSize(true)

            // use a linear layout manager
            layoutManager = viewManager

            // specify an viewAdapter (see also next example)
            adapter = viewAdapter
        }

        gallery_number = findViewById<TextView>(R.id.gallery_number)
        recyclerView = findViewById(R.id.recyclerview_gallery)

        if(ContextCompat.checkSelfPermission(
                        this,
                        permission.READ_EXTERNAL_STORAGE
                ) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,
                    arrayOf(permission.READ_EXTERNAL_STORAGE), MY_READ_PERMISSION_CODE)
        }
        else{
            loadImages()
        }
    }

    @Override
    public override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == MY_READ_PERMISSION_CODE){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Read external storage permission granted", Toast.LENGTH_LONG).show()
                loadImages()
            }
            else{
                Toast.makeText(this, "Read external storage permission denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    fun loadImages(){

        recyclerView.hasFixedSize()

        val path: String = externalMediaDirs.firstOrNull()?.toString() + "/TextTranslator"
        val directory = File(path)
        val files: Array<File> = directory.listFiles()

        imageFiles.clear()

        for (i in files.indices) {
            imageFiles.add(files[i])
        }
    }

    fun deleteImage(file: File){
        val path: String = externalMediaDirs.firstOrNull()?.toString() + "/TextTranslator"
        val directory = File(path)
        if (file.exists()) {
            if (file.delete()) {

                System.out.println("file Deleted :" + file.path);
            } else {
                System.out.println("file not Deleted :" + file.path);
            }
        }
    }

    fun onButtonReturn(view: View){
        finish()
    }
}