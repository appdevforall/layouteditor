package com.itsvks.layouteditor.utils

import android.content.Context
import android.content.Intent
import androidx.core.net.toUri
import com.itsaky.androidide.utils.DialogUtils
import com.itsvks.layouteditor.R
import com.itsvks.layouteditor.activities.HelpActivity
import com.itsvks.layouteditor.utils.ContactDetails.EMAIL_SUPPORT
import org.adfa.constants.CONTENT_KEY
import org.adfa.constants.HELP_PAGE_URL

fun showContactDialog(context: Context) {
    val builder = DialogUtils.newMaterialDialogBuilder(context)

    builder.setTitle(R.string.msg_contact_app_dev_title)
        .setMessage(R.string.msg_contact_app_dev_description)
        .setNegativeButton(R.string.go_to_help) { dialog, _ ->
            val intent = Intent(context, HelpActivity::class.java)
            intent.putExtra(CONTENT_KEY, HELP_PAGE_URL)
            context.startActivity(intent)
            dialog.dismiss()
        }
        .setPositiveButton(R.string.send_email) { dialog, _ ->
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data =
                    "mailto:$EMAIL_SUPPORT?subject=${context.getString(R.string.feedback_email_subject)}".toUri()
            }
            context.startActivity(intent)
            dialog.dismiss()
        }
        .setNeutralButton(android.R.string.cancel) { dialog, _ ->
            dialog.dismiss()
        }
        .create()
        .show()
}

object ContactDetails {
    const val EMAIL_SUPPORT = "feedback@appdevforall.org"
}
