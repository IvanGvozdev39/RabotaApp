package com.test.rabotaapp.presentation.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.test.domain.models.Offer
import com.test.rabotaapp.R


class OfferAdapter(
    private val context: Context?,
    private val arrayList: ArrayList<Offer>
) : RecyclerView.Adapter<OfferAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val titleTV: TextView = view.findViewById(R.id.recommendation_title)
        val iconIV: ImageView = view.findViewById(R.id.recommendation_icon)
        val raiseButton: Button = view.findViewById(R.id.raise_button)
        val parentConstraintLayout: ConstraintLayout = view.findViewById(R.id.parent_constraint_layout)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recommendation_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val offer = arrayList[position]
        holder.titleTV.text = offer.title
        if (offer.button != null) {
            holder.raiseButton.visibility = View.VISIBLE
            holder.raiseButton.text = offer.button!!.text
        } else
            holder.raiseButton.visibility = View.GONE

        when (offer.id) {
            "near_vacancies" -> {
                holder.iconIV.backgroundTintList = context?.getColorStateList(R.color.dark_blue)
                holder.iconIV.setImageResource(R.drawable.ic_location)
            }
            "level_up_resume" -> {
                holder.iconIV.backgroundTintList = context?.getColorStateList(R.color.dark_green)
                holder.iconIV.setImageResource(R.drawable.ic_star)
            }
            "temporary_job" -> {
                holder.iconIV.backgroundTintList = context?.getColorStateList(R.color.dark_green)
                holder.iconIV.setImageResource(R.drawable.ic_temporary_job)
            }
            else -> holder.iconIV.visibility = View.GONE
        }

        holder.parentConstraintLayout.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(offer.link)
            holder.itemView.context.startActivity(intent)
        }


    }

    override fun getItemCount(): Int = arrayList.size

    fun updateData(newData: List<Offer>) {
        arrayList.clear()
        arrayList.addAll(newData)
        notifyDataSetChanged()
    }
}