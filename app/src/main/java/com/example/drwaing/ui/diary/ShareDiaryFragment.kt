package com.example.drwaing.ui.diary

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.drwaing.R
import com.example.drwaing.databinding.FragmentShareDiaryBinding
import com.example.drwaing.extension.viewBinding


class ShareDiaryFragment : Fragment() {
    private val binding by viewBinding(FragmentShareDiaryBinding::bind)
    private val navController: NavController
        get() = Navigation.findNavController(
            requireActivity(),
            R.id.nav_diary_host
        )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//
//        val stickerAssetUri: Uri = Uri.parse(R.drawable.ic_rainy_enabled.toString())
//        val sourceApplication = "com.example.drwaing"
//
//// Instantiate implicit intent with ADD_TO_STORY action,
//// sticker asset, and background colors
//
//// Instantiate implicit intent with ADD_TO_STORY action,
//// sticker asset, and background colors
//        val intent = Intent("com.instagram.share.ADD_TO_STORY")
//        intent.putExtra("source_application", sourceApplication)
//
//        intent.setType("image/*")
//        intent.putExtra("interactive_asset_uri", stickerAssetUri)
//        intent.putExtra("top_background_color", "#EDEDED")
//        intent.putExtra("bottom_background_color", "#EDEDED")
//
//        val activity: Activity? = activity
//        activity!!.grantUriPermission("com.instagram.android", stickerAssetUri, Intent.FLAG_GRANT_READ_URI_PERMISSION)
//        if (activity!!.packageManager.resolveActivity(intent, 0) != null) {
//            activity!!.startActivityForResult(intent, 0)
    }
}
