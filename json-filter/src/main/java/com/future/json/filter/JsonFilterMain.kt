package com.future.json.filter

import com.future.json.filter.model.OneTab
import com.future.json.filter.model.Sheet1
import com.future.json.filter.model.TabRoot
import com.future.json.filter.model.TwoTab
import com.google.gson.Gson
import java.io.*
import java.lang.Exception
import java.nio.ByteBuffer
import java.nio.file.Files.readAllBytes
import java.nio.file.Paths


object JsonFilterMain {

    private const val USER_DIR = "user.dir"
    private const val ROOT_NAME = "json-filter"
    private const val JSON_PATH = "json"
    private const val TAG_FILE = "1.json"
    private const val RESULT_FILE = "result.json"

    @JvmStatic
    fun main(args: Array<String>) {
        println("=== main begin ===")
        val projectFile = File(getRootProjectFile(ROOT_NAME), ROOT_NAME)
        println("projectDir:${projectFile.absolutePath}")
        val jsonFile = getJsonFile(projectFile, TAG_FILE)
        if (jsonFile.exists()) {
            println("jsonFile: $jsonFile")
            val jsonContent = readAllBytesJava7(jsonFile.absolutePath)
            println("jsonContent: ${jsonContent.length}")
            if (jsonContent.isNotBlank()) {
                val sheet1 = Gson().fromJson(jsonContent, Sheet1::class.java)
                println("sheet1: $sheet1")
                if (sheet1 != null) {
                    val testList = sheet1.testList
                    println("testList: ${testList.size}")
                    if (testList.isNotEmpty()) {
                        val oneTabList: ArrayList<OneTab> = ArrayList()
                        var oneTab = OneTab()
                        var twoTab = TwoTab()
                        for (item in testList) {
                            if (item.oneTab.isNotBlank()) {
                                oneTab = OneTab()
                                oneTab.name = item.oneTab
                                oneTabList.add(oneTab)
                            }
                            if (item.twoTab.isNotBlank()) {
                                twoTab = TwoTab()
                                twoTab.name = item.twoTab
                                oneTab.twoTabList.add(twoTab)
                            }
                            if (item.questionContent.isNotBlank()) {
                                twoTab.questionList.add(item.questionContent)
                            }
                        }
                        val tabRoot = TabRoot()
                        tabRoot.oneTabList = oneTabList
                        val resultJson = Gson().toJson(tabRoot) ?: ""
                        val resultFile = getJsonFile(projectFile, RESULT_FILE)
                        println("resultJson:$resultJson")
                        println("resultFile:" + resultFile.absolutePath)
                        usingDataOutputStream(resultJson, resultFile)
                        println("usingDataOutputStream called...")
                    }
                }
            }
        }
        println("=== main end ====")
    }

    private fun usingDataOutputStream(content: String, file: File) {
        try {
            val stream = RandomAccessFile(file.absolutePath, "rw")
            val channel = stream.channel
            val strBytes = content.toByteArray()
            val buffer = ByteBuffer.allocate(strBytes.size)
            buffer.put(strBytes)
            buffer.flip()
            channel.write(buffer)
            stream.close()
            channel.close()
        } catch (e: Exception) {
            println("" + e.printStackTrace())
        }
    }

    private fun readAllBytesJava7(filePath: String): String {
        var content = ""
        try {
            content = String(readAllBytes(Paths.get(filePath)))
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return content
    }

    private fun getJsonFile(projectFile: File, fileName: String): File {
        val jsonFile = File(projectFile, JSON_PATH)
        if (!jsonFile.exists()) {
            jsonFile.mkdirs()
        }
        return File(jsonFile, fileName)
    }

    private fun getRootProjectFile(currentFileName: String): File {
        var projectFile = File(System.getProperty(USER_DIR))
        if (currentFileName.isNotBlank()) {
            projectFile = if (projectFile.absolutePath.contains(currentFileName))
                projectFile.parentFile else projectFile
        }
        return projectFile
    }
}
