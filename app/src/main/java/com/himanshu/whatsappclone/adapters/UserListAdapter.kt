package com.himanshu.whatsappclone.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.himanshu.whatsappclone.R
import com.himanshu.whatsappclone.models.UserObject

class UserListAdapter(private var userList: List<UserObject>) :
    RecyclerView.Adapter<UserListAdapter.UserListRecyclerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserListRecyclerViewHolder {
        val layoutView : View = LayoutInflater.from(parent.context).inflate(R.layout.item_user, null, false)
        val lp : RecyclerView.LayoutParams = RecyclerView.LayoutParams (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        layoutView.layoutParams = lp

        return UserListRecyclerViewHolder(layoutView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserListRecyclerViewHolder, position: Int) {
        holder.mNameTextView.setText(userList.get (position).name)
        holder.mPhoneTextView.setText(userList.get (position).phone)
    }

    class UserListRecyclerViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var mNameTextView : TextView = itemView.findViewById(R.id.text_view_user_name)
        var mPhoneTextView : TextView = itemView.findViewById(R.id.text_view_user_phone)
    }

}