package uz.gita.myemailauthapp1.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import uz.gita.myemailauthapp1.data.UserModel
import uz.gita.myemailauthapp1.databinding.ItemUserBinding

class UserAdapter: RecyclerView.Adapter<UserAdapter.ViewHolder>() {
   lateinit var list: List<UserModel>
    private var onItemClickListener: ((data: UserModel) -> Unit)? = null

    fun setonItemClickListener(block: ((data: UserModel) -> Unit)) {
        onItemClickListener = block
    }
    inner class ViewHolder(private val binding: ItemUserBinding): RecyclerView.ViewHolder(binding.root){
        init {
            binding.emailText.setOnClickListener {
                onItemClickListener?.invoke(list[absoluteAdapterPosition])
            }
        }
        fun binding(){
          val data =   list[absoluteAdapterPosition]
            binding.emailText.text = data.email
        }
    }

    fun submitList(list_: List<UserModel>){
        list = list_
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
     return ViewHolder(ItemUserBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      holder.binding()
    }

    override fun getItemCount(): Int = list.size
}