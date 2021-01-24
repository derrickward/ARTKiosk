package com.artsampleApp
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.artsampleApp.models.Kiosk

interface KioskItemListener
{
    fun onSelected(item : Int)
}

class KioskItemViewHolder(val view : View) :
        RecyclerView.ViewHolder(view)
{
    var nameTextView : TextView
    var addressTextView : TextView

    init
    {
        nameTextView = itemView.findViewById(R.id.nameTextView)
        addressTextView = itemView.findViewById(R.id.addressTextView)
    }
}

class KioskItemAdapter(private var kiosks: List<Kiosk>,var listener : KioskItemListener? = null) : RecyclerView.Adapter<KioskItemViewHolder>()
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): KioskItemViewHolder {

        val inflater = LayoutInflater.from(parent.getContext())
        return KioskItemViewHolder(
                inflater.inflate(
                        R.layout.item_kiosk, // Custom view/ layout
                        parent, // Root layout to attach the view
                        false // Attach with root layout or not
                ))
    }

    override fun onBindViewHolder(holder: KioskItemViewHolder, position: Int) {

        holder.nameTextView.text = kiosks[position].name
        holder.addressTextView.text = kiosks[position].address

        holder.itemView.setOnClickListener {
            listener?.onSelected(position)
        }
    }

    override fun getItemCount() = kiosks.size
}