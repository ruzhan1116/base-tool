package com.future.basetool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.frag_main.*

class MainFragment : Fragment() {

    companion object {

        @JvmStatic
        fun newInstance() = MainFragment()
    }

    var onMainPageListener: OnMainPageListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mailBtn.setOnClickListener {
            onMainPageListener?.showMailPage()
        }
        imageLoaderBtn.setOnClickListener {
            onMainPageListener?.showImageLoaderPage()
        }
        fontBtn.setOnClickListener {
            onMainPageListener?.showFontPage()
        }
    }
}