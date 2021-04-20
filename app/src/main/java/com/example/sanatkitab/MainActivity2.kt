package com.example.sanatkitab

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main2.*
import java.io.ByteArrayOutputStream

class MainActivity2 : AppCompatActivity() {

    var selectedPicture : Uri?=null
    var selectedbitmap:Bitmap?=null

    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        val intent =intent
        val info = intent.getStringExtra("info")
        if (info.equals("new")){
            sanatciText.setText("")
            sanatisimText.setText("")
            tarihText.setText("")
            Kaydetbutton.visibility=View.VISIBLE

            val secilenresim=BitmapFactory.decodeResource(applicationContext.resources, R.drawable.gorsel)
            imageView.setImageBitmap(secilenresim)
        }else{

            Kaydetbutton.visibility=View.INVISIBLE
            val selectedId =intent.getIntExtra("id", 1)

            val database =this.openOrCreateDatabase("Sanatlar", Context.MODE_PRIVATE, null)
            val cursor =database.rawQuery("SELECT*FROM sanatlar WHERE id = ?", arrayOf(selectedId.toString()))
            val sanatadi =cursor.getColumnIndex("sanatadi")
            val sanatci = cursor.getColumnIndex("sanatciadi")
            val tarih = cursor.getColumnIndex("tarih")
           val image = cursor.getColumnIndex("image")


            while (cursor.moveToNext()){

               sanatisimText.setText(cursor.getString(sanatadi))
                sanatciText.setText(cursor.getString(sanatci))
                tarihText.setText(cursor.getString(tarih))
                sanatciText.isEnabled=false
                sanatisimText.isEnabled=false

                tarihText.isEnabled=false
                sanatisimText.setTextColor(R.color.design_default_color_on_secondary)
                sanatciText.setTextColor(R.color.design_default_color_on_secondary)
              tarihText.setTextColor(R.color.design_default_color_on_secondary)

                



                val byteArray  = cursor.getBlob(image)
                val bitmap=BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
                imageView.setImageBitmap(bitmap)

            }
            cursor.close()

        }
    }
    fun gorselsec(view: View){
if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_EXTERNAL_STORAGE)!=PackageManager.PERMISSION_GRANTED){
    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1)


}else {
    val intentTogalary =Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
    startActivityForResult(intentTogalary, 2)
}
    }

    fun kaydet(view: View){

        val eseradi =sanatisimText.text.toString()
        val sanatci=sanatciText.text.toString()
        val tarih=tarihText.text.toString()

        if (selectedbitmap!=null) {
            val kucukBitmap = kucukbitmapyap(selectedbitmap!!, 300)

            val outputstream = ByteArrayOutputStream()
            kucukBitmap.compress(Bitmap.CompressFormat.PNG, 50, outputstream)
            val byteArray = outputstream.toByteArray()
try {


    val database = this.openOrCreateDatabase("Sanatlar", Context.MODE_PRIVATE, null)
    database.execSQL("CREATE TABLE IF NOT EXISTS sanatlar(id INTEGER PRIMARY KEY ,sanatadi VARCHAR ,sanatciadi VARCHAR ,tarih VARCHAR,image BLOB)")
    val sqlString = "INSERT INTO sanatlar(sanatadi,sanatciadi,tarih,image)VALUES (?,?,?,?)"
    val statement = database.compileStatement(sqlString)
    statement.bindString(1, eseradi)
    statement.bindString(2, sanatci)
    statement.bindString(3, tarih)
    statement.bindBlob(4, byteArray)
    statement.execute()
}catch (e: Exception){
    e.printStackTrace()
}
            var intent =Intent(this, MainActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            
        }

    }
    fun kucukbitmapyap(image: Bitmap, maximumsize: Int):Bitmap{
        var width =image.width
        var height=image.height
        val bitmapRatio:Double=width.toDouble()/height.toDouble()
        if (bitmapRatio>1){
            width=maximumsize
            var scaledHeight=width / bitmapRatio
            height=scaledHeight.toInt()

        }else{
            height=maximumsize
            val scaledWidht =height*bitmapRatio
            width=scaledWidht.toInt()
        }


        return  Bitmap.createScaledBitmap(image, width, height, true)
    }

    override fun onRequestPermissionsResult(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
    ) {

        if (requestCode==1){
            if (grantResults.size>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){
                val intentTogalary =Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                startActivityForResult(intentTogalary, 2)
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

if (requestCode==2&&resultCode== Activity.RESULT_OK&&data!=null){
    selectedPicture= data.data
    try {


if (selectedPicture!=null){
    if (Build.VERSION.SDK_INT>=28){
        val source =ImageDecoder.createSource(this.contentResolver, selectedPicture!!)
       selectedbitmap =ImageDecoder.decodeBitmap(source)
        imageView.setImageBitmap(selectedbitmap)
    }else {

    selectedbitmap =MediaStore.Images.Media.getBitmap(this.contentResolver, selectedPicture)
    imageView.setImageBitmap(selectedbitmap)
}
}

}catch (e: Exception){

}
}
        super.onActivityResult(requestCode, resultCode, data)
}

}


