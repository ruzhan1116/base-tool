package com.future.email

import java.io.File

class MailController {

    companion object {
        private const val EMAIL_TITLE = "来自 xxx 用户反馈"

        private const val HOST = "smtp.qq.com"
        private const val PORT = "587"
        private const val FROM_ADD = "ruzhan666@foxmail.com" // send email
        private const val FROM_PSW = "mmrhffjtghlsdjjd" // send email pwd
        private const val TO_ADD = "dev19921116@gmail.com" // receive email

        private var INSTANCE: MailController? = null

        @JvmStatic
        fun get() = INSTANCE ?: synchronized(MailController::class.java) {
            INSTANCE ?: MailController().also {
                INSTANCE = it
            }
        }
    }

    fun send(file: File, contentData: String) {
        val mailModel = createMail(contentData)
        MailExecutors.get().executeOnDiskIO(Runnable {
            MailSenderHelper.sendFileMail(mailModel, file)
        })
    }

    fun send(contentData: String) {
        val mailModel = createMail(contentData)
        MailExecutors.get().executeOnDiskIO(Runnable {
            MailSenderHelper.sendTextMail(mailModel)
        })
    }

    private fun createMail(contentData: String): MailModel {
        val mailModel = MailModel()
        mailModel.mailServerHost = HOST
        mailModel.mailServerPort = PORT
        mailModel.isValidate = true
        mailModel.userName = FROM_ADD
        mailModel.password = FROM_PSW
        mailModel.fromAddress = FROM_ADD
        mailModel.toAddress = TO_ADD
        mailModel.subject = EMAIL_TITLE
        mailModel.content = contentData
        return mailModel
    }
}