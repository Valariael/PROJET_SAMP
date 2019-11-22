package fr.ledermann.axel.projet_samp.controller

import android.os.AsyncTask
import android.widget.Toast
import fr.ledermann.axel.projet_samp.R
import fr.ledermann.axel.projet_samp.model.Answer
import fr.ledermann.axel.projet_samp.model.Question
import fr.ledermann.axel.projet_samp.model.Quizz
import kotlinx.android.synthetic.main.activity_highscore.*
import org.w3c.dom.Element
import java.io.BufferedReader
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import javax.xml.parsers.DocumentBuilderFactory

class XmlHttpQuizz (var qma : QuizzManageActivity){
    private var httpTask = HttpTask()

    fun execute() {
        httpTask.execute()
    }

    inner class HttpTask : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void): Boolean {
            val bufferedReader: BufferedReader? = null
            var urlConnection: HttpURLConnection? = null
            var everythingWentWell = true

            try {
                val url = URL("https://dept-info.univ-fcomte.fr/joomla/images/CR0700/Quizzs.xml")
                urlConnection = url.openConnection() as HttpURLConnection
                val responseCode = urlConnection.responseCode

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    val inputStream = urlConnection.inputStream

                    val dbf = DocumentBuilderFactory.newInstance()
                    val db = dbf.newDocumentBuilder()
                    val doc = db.parse(inputStream)
                    doc.documentElement.normalize()

                    val allQuizzs = doc.getElementsByTagName("Quizzs").item(0) as Element
                    val quizzNodes = allQuizzs.getElementsByTagName("Quizz")

                    var i = 0
                    while (i < quizzNodes.length) {
                        val quizzEl = quizzNodes.item(i) as Element
                        val quizzName = quizzEl.getAttribute("type")
                        val idQuizz = qma.db.newQuizz(
                            Quizz(
                                quizzName
                            )
                        )

                        val questionNodes = quizzEl.getElementsByTagName("Question")

                        var j = 0
                        while (j < questionNodes.length) {
                            val questionEl = questionNodes.item(j) as Element
                            val questionName = questionEl.childNodes.item(0).textContent.trim()
                            val idQuestion = qma.db.newQuestion(
                                Question(
                                    questionName,
                                    idQuizz
                                )
                            )

                            val goodAnswerNode = questionEl.getElementsByTagName("Reponse").item(0) as Element
                            val goodAnswer = Integer.parseInt(goodAnswerNode.getAttribute("valeur"))-1
                            val answerNodes = questionEl.getElementsByTagName("Proposition")

                            var k = 0
                            while(k < answerNodes.length) {
                                val answerEl = answerNodes.item(k) as Element
                                val answerName = answerEl.childNodes.item(0).textContent.trim()

                                if(k == goodAnswer) qma.db.newAnswer(
                                    Answer(
                                        answerName,
                                        true,
                                        idQuestion
                                    )
                                )
                                else qma.db.newAnswer(
                                    Answer(
                                        answerName,
                                        false,
                                        idQuestion
                                    )
                                )

                                k++
                            }

                            j++
                        }

                        i++
                    }

                } else {
                    everythingWentWell = false
                }
            } catch (e: Exception) {
                everythingWentWell = false
            } finally {
                if (bufferedReader != null) {
                    try {
                        bufferedReader.close()
                    } catch (e: IOException) {
                        everythingWentWell = false
                    }

                }
                urlConnection?.disconnect()
            }

            return everythingWentWell
        }

        override fun onPostExecute(result: Boolean) {
            if(result) {
                qma.quizzList.clear()
                qma.quizzList.addAll(qma.db.getQuizzs())
                qma.updateList()
            } else {
                Toast.makeText(qma, R.string.http_error_text, Toast.LENGTH_LONG).show()
            }
        }
    }
}