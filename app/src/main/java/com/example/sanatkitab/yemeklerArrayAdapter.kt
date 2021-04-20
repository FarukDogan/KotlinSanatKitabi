package com.example.sanatkitab

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import kotlinx.android.synthetic.main.tek_satir.view.*

class yemeklerArrayAdapter(var gelencontext: Context,
                           resource: Int,
                           textViewResourceId: Int,
                           var yemeklerArray: ArrayList<String>,
                           var yemekResimleri: ArrayList<Bitmap>
) : ArrayAdapter<String>(gelencontext, resource, textViewResourceId, yemeklerArray) {

    @SuppressLint("ViewHolder")
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var inflater = LayoutInflater.from(gelencontext)
        var list_roww = inflater.inflate(R.layout.tek_satir, parent, false)
        var sehirfoto = list_roww.imgTekSatir
        var sehiradi = list_roww.tvTekSatir

        sehirfoto.setImageBitmap(yemekResimleri[position])
        sehiradi.setText(yemeklerArray[position])





        return list_roww
    }
}
