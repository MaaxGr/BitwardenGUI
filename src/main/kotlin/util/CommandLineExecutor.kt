package de.maaxgr.passwordmanager.util

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.InputStreamReader
import java.util.ArrayList



class CommandLineExecutor(private val script: String) {

    companion object {
        fun run(script: String): String {
            return CommandLineExecutor(script)
                    .run()
                    .outputText
        }
    }

    private var outputText: String = ""

    fun run(): CommandLineExecutor {
        val random = System.nanoTime()

        //pipe into temp file
        val pb = ProcessBuilder("/bin/sh", "-c", "$script >> /tmp/$random.txt")

        //redirect error stream to output stream
        pb.redirectErrorStream(true)

        val proc = pb.start()
        proc.waitFor()

        //get data from tmp file
        val stdInput = FileReader("/tmp/$random.txt")

        // Read the output from the command
        outputText = stdInput.readText()

        //delete tmp file
        File("/tmp/$random.txt").delete()

        return this
    }


}