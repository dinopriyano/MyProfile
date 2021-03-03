package com.dupat.layouttest.network

import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder
import javax.net.ssl.HttpsURLConnection

class APIHandler {
    companion object{
        fun sendPostRequest(requestURL: String?, postDataParams: HashMap<String, String>?): String? {
            //Creating a URL
            val url: URL

            //StringBuilder object to store the message retrieved from the server
            var sb = StringBuilder()
            try {
                //Initializing Url
                url = URL(requestURL)

                //Creating an httmlurl connection
                val conn: HttpURLConnection = url.openConnection() as HttpURLConnection

                //Configuring connection properties
                conn.readTimeout = 15000
                conn.connectTimeout = 15000
                conn.requestMethod = "POST"
                conn.doInput = true
                conn.doOutput = true

                //Creating an output stream
                val os: OutputStream = conn.getOutputStream()

                //Writing parameters to the request
                //We are using a method getPostDataString which is defined below
                val writer = BufferedWriter(OutputStreamWriter(os, "UTF-8"))
                writer.write(getPostDataString(postDataParams!!))
                writer.flush()
                writer.close()
                os.close()
                val responseCode: Int = conn.responseCode
                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    val br = BufferedReader(InputStreamReader(conn.inputStream))
                    sb = StringBuilder()
                    var response: String? = null

                    //Reading server response
                    while (br.readLine().also { response = it } != null) {
                        sb.append(response)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return sb.toString()
        }

        fun sendGetRequest(requestURL: String?): String? {
            val sb = StringBuilder()
            try {
                val url = URL(requestURL)
                val con: HttpURLConnection = url.openConnection() as HttpURLConnection
                val bufferedReader = BufferedReader(InputStreamReader(con.inputStream))
                var s: String
                while (bufferedReader.readLine().also { s = it } != null) {
                    sb.append("$s\n")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return sb.toString()
        }

        @Throws(UnsupportedEncodingException::class)
        fun getPostDataString(params: HashMap<String, String>): String? {
            val result = java.lang.StringBuilder()
            var first = true
            for ((key, value) in params.entries) {
                if (first) first = false else result.append("&")
                result.append(URLEncoder.encode(key, "UTF-8"))
                result.append("=")
                result.append(URLEncoder.encode(value, "UTF-8"))
            }
            return result.toString()
        }
    }
}