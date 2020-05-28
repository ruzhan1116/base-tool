package com.future.basetool

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), OnMainPageListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null) {
            val frag = MainFragment.newInstance()
            frag.onMainPageListener = this
            supportFragmentManager
                .beginTransaction()
                .add(R.id.containerFl, frag, "MainFragment")
                .addToBackStack(null)
                .commit()
        }
    }

    override fun showMailPage() {
        val frag = MailFragment.newInstance()
        supportFragmentManager
            .beginTransaction()
            .add(R.id.containerFl, frag, "MailFragment")
            .addToBackStack(null)
            .commit()
    }

    override fun showImageLoaderPage() {

    }

    override fun showFontPage() {

    }
}
