package com.teejay.weathertrack.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.teejay.weathertrack.Model.CityModel
import com.teejay.weathertrack.databinding.ViewholderCityBinding

class OtherCityAdapter (private val items:ArrayList<CityModel>):
RecyclerView.Adapter<OtherCityAdapter.Viewholder>()
{
    private lateinit var context:Context

    class Viewholder (val binding: ViewholderCityBinding)
        :RecyclerView.ViewHolder(binding.root)



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OtherCityAdapter.Viewholder {
        context=parent.context
        val binding=ViewholderCityBinding.inflate(LayoutInflater.from(context)
            ,parent,false
        )
        return Viewholder(binding)
    }

    override fun onBindViewHolder(holder: OtherCityAdapter.Viewholder, position: Int) {
        val item=items[position]
        holder.binding.apply {
            cityTxt.text=item.cityName
            windTxt.text="${item.wind} Km/h"
            humidityTxt.text="${item.humidity}%"
            tempTxt.text="${item.temp}°"


            val drawableResourceId=holder.itemView.resources.getIdentifier(
                item.picPath,
                "drawable",
                context.packageName
            )

            Glide.with(context)
                .load(drawableResourceId)
                .into(pic)
        }
    }

    override fun getItemCount(): Int =items.size

}