package com.awonar.app.dialog.menu

import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.awonar.app.databinding.AwonarDialogBottomMenuBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.parcelize.Parcelize

class MenuDialogButtonSheet(private val listener: MenuDialogButtonSheetListener?) :
    BottomSheetDialogFragment() {

    private var menus: ArrayList<MenuDialog> = arrayListOf()

    private var binding: AwonarDialogBottomMenuBinding? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = AwonarDialogBottomMenuBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        menus = arguments?.getParcelableArrayList(EXTRA_MENUS) ?: arrayListOf()
        val adapterDialog = MenuDialogButtonSheetAdapter {
            listener?.onMenuClick(it)
            dialog?.dismiss()
        }
        binding?.awonarDialogBottomMenuContainer?.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            this.adapter = adapterDialog
        }
        adapterDialog.itemList = menus
    }

    class Builder {
        private var listener: MenuDialogButtonSheetListener? = null
        private var menus: ArrayList<MenuDialog> = arrayListOf()

        fun setListener(listener: MenuDialogButtonSheetListener): Builder = this.apply {
            this.listener = listener
        }

        fun setMenus(menus: ArrayList<MenuDialog>): Builder = this.apply {
            this.menus = menus
        }

        fun build(): MenuDialogButtonSheet = newInstance(listener, menus)
    }

    interface MenuDialogButtonSheetListener {

        fun onMenuClick(menu: MenuDialog)
    }

    companion object {
        private const val EXTRA_MENUS = "com.awonar.app.dialog.bottom.menu.extra.menus"
        const val TAG = "ModalBottomSheet"

        fun newInstance(listener: MenuDialogButtonSheetListener?, menus: ArrayList<MenuDialog>) =
            MenuDialogButtonSheet(listener).apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(EXTRA_MENUS, menus)
                }
            }
    }

    override fun dismiss() {
        super.dismiss()
        binding = null
    }

}

@Parcelize
data class MenuDialog(
    val key: String? = null,
    val icon: String? = null,
    val iconRes: Int = 0,
    val text: String? = null,
) : Parcelable