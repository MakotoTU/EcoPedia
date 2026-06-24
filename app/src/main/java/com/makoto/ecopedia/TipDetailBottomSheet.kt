package com.makoto.ecopedia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class TipDetailBottomSheet : BottomSheetDialogFragment() {

    companion object {
        fun newInstance(category: String, title: String, content: String, imageResId: Int): TipDetailBottomSheet {
            val args = Bundle().apply {
                putString("CATEGORY", category)
                putString("TITLE", title)
                putString("CONTENT", content)
                putInt("IMAGE_RES", imageResId)
            }
            return TipDetailBottomSheet().apply { arguments = args }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.layout_tip_detail_bottom_sheet, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val category = arguments?.getString("CATEGORY") ?: ""
        val title = arguments?.getString("TITLE") ?: ""
        val content = arguments?.getString("CONTENT") ?: ""
        val imageResId = arguments?.getInt("IMAGE_RES") ?: R.drawable.leaf

        view.findViewById<TextView>(R.id.tvTipCategory).text = category
        view.findViewById<TextView>(R.id.tvTipTitle).text = title
        view.findViewById<TextView>(R.id.tvTipContent).text = content
        view.findViewById<ImageView>(R.id.ivTipImage).setImageResource(imageResId)
    }
}
