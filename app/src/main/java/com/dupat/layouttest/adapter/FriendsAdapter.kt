package com.dupat.layouttest.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.*
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.dupat.layouttest.R
import com.dupat.layouttest.model.FriendsModel

class FriendsAdapter(private var list: MutableList<FriendsModel>, private var ctx: Context, private var listener:OnMenuClickCallback) : RecyclerView.Adapter<FriendsAdapter.ViewHolder>(), View.OnClickListener {

    private var onMenuClickCallback: OnMenuClickCallback = listener

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendsAdapter.ViewHolder {
        return ViewHolder(LayoutInflater.from(ctx).inflate(R.layout.item_list_friends,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: FriendsAdapter.ViewHolder, position: Int){
        return holder.bind(list[position],this)
    }

    interface OnMenuClickCallback{
        fun onMenuClicked(model: FriendsModel,action: String)
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView),View.OnClickListener, PopupMenu.OnMenuItemClickListener {
        lateinit var txtName: TextView
        lateinit var txtEmail: TextView
        lateinit var txtPhone: TextView
        lateinit var txtComment: TextView
        lateinit var ivNote: ImageView
        lateinit var btnPopupMenu: ImageButton
        lateinit var context: FriendsAdapter
        lateinit var FriendsModel: FriendsModel

        fun bind(model: FriendsModel,ctx: FriendsAdapter){
            txtName = itemView.findViewById(R.id.txtName)
            txtEmail = itemView.findViewById(R.id.txtEmail)
            txtPhone = itemView.findViewById(R.id.txtPhone)
            txtComment = itemView.findViewById(R.id.txtComment)
            ivNote = itemView.findViewById(R.id.ivNote)
            btnPopupMenu = itemView.findViewById(R.id.btn_popup_menu)

            context = ctx
            FriendsModel = model

            txtName.text = model.name
            txtEmail.text = ": "+model.email
            txtPhone.text = ": "+model.phone
            txtComment.text = ": "+model.comment
            when(model.comment){
                "Teman"->{
                    ivNote.setImageResource(R.drawable.ic_teman)
                }
                "Biasa saja"->{
                    ivNote.setImageResource(R.drawable.ic_biasa_saja)
                }
                "Bagaimana ya?"->{
                    ivNote.setImageResource(R.drawable.ic_bagaimana_ya)
                }
                "No comment"->{
                    ivNote.setImageResource(R.drawable.ic_no_comment)
                }
            }
            btnPopupMenu.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            showPopupMenu(v!!)
        }

        @SuppressLint("NewApi")
        private fun showPopupMenu(v: View){
            val popupMenu: PopupMenu = PopupMenu(v.context,v)
            popupMenu.gravity = Gravity.END
            popupMenu.inflate(R.menu.item_menu)
            popupMenu.setOnMenuItemClickListener(this)
            popupMenu.show()
        }

        override fun onMenuItemClick(item: MenuItem?): Boolean {
            when(item!!.itemId){
                R.id.menu_edit -> {
                    (context as FriendsAdapter).onMenuClickCallback.onMenuClicked(FriendsModel,"edit")
                }
                R.id.menu_delete -> {
                    (context as FriendsAdapter).onMenuClickCallback.onMenuClicked(FriendsModel,"delete")
                }
            }
            return true
        }
    }

    override fun onClick(v: View?) {
    }
}