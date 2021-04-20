package com.example.sanatkitab

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.AdapterView
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val sanatlarListesi =ArrayList<String>()
        val sanatIdListesi =ArrayList<Int>()
        val yemeklerBitmap= ArrayList<Bitmap>()
       // val arrayAdapter =ArrayAdapter(this,android.R.layout.simple_list_item_1,sanatlarListesi)
        val yemekAdapter =yemeklerArrayAdapter(this,R.layout.tek_satir,R.id.imgTekSatir,sanatlarListesi,yemeklerBitmap)
        listView.adapter=yemekAdapter
try {
val database =this.openOrCreateDatabase("Sanatlar", Context.MODE_PRIVATE,null)

    val cursor =database.rawQuery("SELECT * FROM sanatlar",null)
    val sanatadiIndex =cursor.getColumnIndex("sanatadi")
    val idIndex =cursor.getColumnIndex("id")
    val image = cursor.getColumnIndex("image")

    while (cursor.moveToNext()){
        sanatlarListesi.add(cursor.getString(sanatadiIndex))
        sanatIdListesi.add(cursor.getInt(idIndex))

        val byteArray  = cursor.getBlob(image)
        val bitmap=BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
        yemeklerBitmap.add(bitmap)

    }
    yemekAdapter.notifyDataSetChanged()

cursor.close()

} catch (e:Exception){
    e.printStackTrace()
}

listView.onItemClickListener= AdapterView.OnItemClickListener{parent, view, position, id ->

    val intent =Intent(this,MainActivity2::class.java)
    intent.putExtra("info","old")
    intent.putExtra("id",sanatIdListesi[position])
    startActivity(intent)

}




    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        val menuInflater =menuInflater
        menuInflater.inflate(R.menu.ekle,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId==R.id.ekle){
            val intent = Intent(this,MainActivity2::class.java)
            intent.putExtra("info","new")
            startActivity(intent)
        }
        return super.onOptionsItemSelected(item)
    }
}