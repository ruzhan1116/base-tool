package com.future.basetool

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.future.email.MailController
import kotlinx.android.synthetic.main.frag_mail.*

class MailFragment : Fragment() {

    companion object {

        @JvmStatic
        fun newInstance() = MailFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.frag_mail, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mailFromAddressEt.setText("")
        mailFromPwdEt.setText("")
        mailToAddressEt.setText("")
        mailContentEt.setText("test mail module... content")

        sendMailBtn.setOnClickListener {
            val mailFromAddress = mailFromAddressEt.text.toString()
            val mailFromPwd = mailFromPwdEt.text.toString()
            val mailToAddress = mailToAddressEt.text.toString()
            val mailContent = mailContentEt.text.toString()

            if (mailFromAddress.isNotBlank() && mailFromPwd.isNotBlank() &&
                mailToAddress.isNotBlank() && mailContent.isNotBlank()
            ) {
                MailController.get()
                    .setMailFromAddress(mailFromAddress)
                    .setMailFromPwd(mailFromPwd)
                    .setMailToAddress(mailToAddress)
                    .setMailTitle("mail: test mail module...")
                    .send(mailContent)
                Toast.makeText(requireContext(), "send mail finish", Toast.LENGTH_SHORT).show()
            }
        }
    }
}