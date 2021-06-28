package com.dicoding.githubuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class CardViewUserAdapter(private var listUser: ArrayList<User>) :
    RecyclerView.Adapter<CardViewUserAdapter.ViewHolder>() {

    var onClickItem: ((User) -> Unit)? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CardViewUserAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: CardViewUserAdapter.ViewHolder, position: Int) {
        val users = listUser[position]

        Glide.with(holder.itemView.context)
            .load(users.avatar)
            .apply(RequestOptions())
            .into(holder.itemAvatar)
        holder.itemUserName.text = users.username
        holder.itemId.text = users.id.toString()
        holder.itemType.text = users.type
    }

    fun addUser(newUsers: ArrayList<User>) {
        listUser.clear()
        listUser = newUsers
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = listUser.size
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemAvatar: ImageView = itemView.findViewById(R.id.img_item_avatar)
        var itemUserName: TextView = itemView.findViewById(R.id.tv_item_userName)
        var itemId: TextView = itemView.findViewById(R.id.tv_item_id)
        var itemType: TextView = itemView.findViewById(R.id.tv_item_type)

        init {
            itemView.setOnClickListener {
                onClickItem?.let { it1 -> it1(listUser[adapterPosition]) }
            }
        }
    }
}


