package com.future.email

import java.io.File

class MailController {

    companion object {
        private const val EMAIL_TITLE = "email: a feedback message"

        private const val QQ_HOST = "smtp.qq.com"
        private const val QQ_PORT = "587"

        @Volatile
        private var INSTANCE: MailController? = null

        @JvmStatic
        fun get() = INSTANCE ?: synchronized(MailController::class.java) {
            INSTANCE ?: MailController().also {
                INSTANCE = it
            }
        }
    }

    private var mailHost = QQ_HOST
    private var mailPost = QQ_PORT
    private var mailTitle = EMAIL_TITLE
    private var mailFromAddress = ""
    private var mailFromPwd = ""
    private var mailToAddress = ""

    fun send(file: File, contentData: String) {
        val mailModel = createMailModel(contentData)
        MailExecutors.get().executeOnDiskIO(Runnable {
            MailSenderHelper.sendFileMail(mailModel, file)
        })
    }

    fun send(contentData: String) {
        val mailModel = createMailModel(contentData)
        MailExecutors.get().executeOnDiskIO(Runnable {
            MailSenderHelper.sendTextMail(mailModel)
        })
    }

    private fun createMailModel(contentData: String): MailModel {
        val mailModel = MailModel()
        mailModel.mailServerHost = mailHost
        mailModel.mailServerPort = mailPost
        mailModel.isValidate = true
        mailModel.userName = mailFromAddress
        mailModel.password = mailFromPwd
        mailModel.fromAddress = mailFromAddress
        mailModel.toAddress = mailToAddress
        mailModel.subject = mailTitle
        mailModel.content = contentData
        mailModel.initProperty()
        return mailModel
    }

    fun setMailHost(host: String): MailController {
        mailHost = host
        return this
    }

    fun setMailPost(post: String): MailController {
        mailPost = post
        return this
    }

    fun setMailTitle(title: String): MailController {
        mailTitle = title
        return this
    }

    fun setMailFromAddress(fromAddress: String): MailController {
        mailFromAddress = fromAddress
        return this
    }

    fun setMailFromPwd(fromPwd: String): MailController {
        mailFromPwd = fromPwd
        return this
    }

    fun setMailToAddress(toAddress: String): MailController {
        mailToAddress = toAddress
        return this
    }
}