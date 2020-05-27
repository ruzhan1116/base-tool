package com.future.email

import java.io.File
import java.util.*
import javax.activation.DataHandler
import javax.activation.FileDataSource
import javax.mail.*
import javax.mail.internet.*

object MailSenderHelper {

    private const val TEXT_HTML_UTF8 = "text/html;charset=UTF-8"
    private const val MIXED = "mixed"

    @JvmStatic
    fun sendHtmlMail(mailModel: MailModel): Boolean {
        var authenticator: MailAuthenticator? = null
        val properties = mailModel.properties
        if (mailModel.isValidate) {
            authenticator = MailAuthenticator(mailModel.userName, mailModel.password)
        }
        val sendMailSession = Session.getDefaultInstance(properties, authenticator)
        try {
            val mimeMessage = MimeMessage(sendMailSession)
            val fromInternetAddress = InternetAddress(mailModel.fromAddress)
            mimeMessage.setFrom(fromInternetAddress)
            val toInternetAddress = InternetAddress(mailModel.toAddress)
            mimeMessage.setRecipient(Message.RecipientType.TO, toInternetAddress)
            mimeMessage.subject = mailModel.subject
            mimeMessage.sentDate = Date()

            val mainPart = MimeMultipart()
            val html = MimeBodyPart()
            html.setContent(mailModel.content, "text/html; charset=utf-8")
            mainPart.addBodyPart(html)
            mimeMessage.setContent(mainPart)
            Transport.send(mimeMessage)
            return true
        } catch (ex: MessagingException) {
            ex.printStackTrace()
        }
        return false
    }

    @JvmStatic
    fun sendTextMail(mailModel: MailModel): Boolean {
        var authenticator: MailAuthenticator? = null
        val properties = mailModel.properties
        if (mailModel.isValidate) {
            authenticator = MailAuthenticator(mailModel.userName, mailModel.password)
        }
        val sendMailSession = Session.getDefaultInstance(properties, authenticator)
        try {
            val mimeMessage = MimeMessage(sendMailSession)
            val fromInternetAddress = InternetAddress(mailModel.fromAddress)
            mimeMessage.setFrom(fromInternetAddress)
            val toInternetAddress = InternetAddress(mailModel.toAddress)
            mimeMessage.setRecipient(Message.RecipientType.TO, toInternetAddress)
            mimeMessage.subject = mailModel.subject
            mimeMessage.sentDate = Date()
            val mailContent = mailModel.content
            mimeMessage.setText(mailContent)
            Transport.send(mimeMessage)
            return true
        } catch (ex: MessagingException) {
            ex.printStackTrace()
        }

        return false
    }

    @JvmStatic
    fun sendFileMail(mailModel: MailModel, file: File): Boolean {
        val attachmentMail = createAttachmentMail(mailModel, file)
        return try {
            Transport.send(attachmentMail)
            true
        } catch (e: MessagingException) {
            e.printStackTrace()
            false
        }
    }

    @JvmStatic
    private fun createAttachmentMail(mailModel: MailModel, file: File): Message? {
        var mimeMessage: MimeMessage? = null
        val properties = mailModel.properties
        try {
            val sendMailSession = Session.getInstance(properties, object : Authenticator() {
                override fun getPasswordAuthentication(): PasswordAuthentication {
                    return PasswordAuthentication(mailModel.userName, mailModel.password)
                }
            })
            mimeMessage = MimeMessage(sendMailSession)
            val fromInternetAddress = InternetAddress(mailModel.fromAddress)
            mimeMessage.setFrom(fromInternetAddress)
            val toInternetAddress = InternetAddress(mailModel.toAddress)
            mimeMessage.setRecipient(Message.RecipientType.TO, toInternetAddress)
            mimeMessage.subject = mailModel.subject

            val mimeBodyPartText = MimeBodyPart()
            mimeBodyPartText.setContent(mailModel.content, TEXT_HTML_UTF8)
            val mimeMultipart = MimeMultipart()
            mimeMultipart.addBodyPart(mimeBodyPartText)

            val attachMimeBodyPart = MimeBodyPart()
            val fileDataSource = FileDataSource(file)
            val dataHandler = DataHandler(fileDataSource)
            attachMimeBodyPart.dataHandler = dataHandler
            attachMimeBodyPart.fileName = MimeUtility.encodeText(dataHandler.name)

            mimeMultipart.addBodyPart(attachMimeBodyPart)
            mimeMultipart.setSubType(MIXED)
            mimeMessage.setContent(mimeMultipart)
            mimeMessage.saveChanges()
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return mimeMessage
    }
}