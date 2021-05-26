package com.kostyanchikoff.movenpublich

import android.os.Bundle
import com.kostynchikoff.core_application.presentation.ui.activities.CoreAuthorizedActivity
import com.kostynchikoff.core_application.utils.extensions.loadImage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : CoreAuthorizedActivity(R.layout.activity_main) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        imageView.loadImage("https://beta.kcell.kz/uploads/2020/11/3/43cb23355ceb2379303240f8ae941052_original.541.svg")
    }
}