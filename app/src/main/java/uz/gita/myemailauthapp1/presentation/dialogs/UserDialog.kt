package uz.gita.myemailauthapp1.presentation.dialogs

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import uz.gita.myemailauthapp1.data.UserModel
import uz.gita.myemailauthapp1.presentation.adapters.UserAdapter
import uz.gita.myemailauthapp1.R
import uz.gita.myemailauthapp1.databinding.DialogUserBinding

class UserDialog(
    private val list: List<UserModel>,
    private val onClick : ((data: UserModel) -> Unit)
): DialogFragment(R.layout.dialog_user) {
  private val binding by viewBinding(DialogUserBinding::bind)
    private val adapter = UserAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         binding.recycler.adapter = adapter
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        adapter.submitList(list)
        adapter.setonItemClickListener {
            onClick.invoke(it)
        }
    }
}