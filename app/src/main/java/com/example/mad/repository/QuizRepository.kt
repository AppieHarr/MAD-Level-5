package com.example.mad.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mad.data.Quiz
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withTimeout

class QuizRepository {
    private var firestore: FirebaseFirestore = FirebaseFirestore.getInstance()
    private var quizDocument =
        firestore.collection("quizzes").document("quiz")

    private val _quiz: MutableLiveData<Quiz> = MutableLiveData()

    val quiz: LiveData<Quiz>
        get() = _quiz

    //the CreateQuizScreen can use this to see if creation succeeded
    private val _createSuccess: MutableLiveData<Boolean> = MutableLiveData()

    val createSuccess: LiveData<Boolean>
        get() = _createSuccess

    suspend fun getQuiz() {
        try {
            //firestore has support for coroutines via the extra dependency we've added :)
            withTimeout(5_000) {
                val data = quizDocument
                    .get()
                    .await()

                val question = data.getString("question").toString()
                val answer = data.getString("answer").toString()

                _quiz.value = Quiz(question, answer)
            }
        } catch (e: Exception) {
            throw QuizRetrievalError("Retrieval-firebase-task was unsuccessful")
        }
    }

    suspend fun createQuiz(quiz: Quiz) {
        reset()
        // persist data to firestore
        try {
            //firestore has support for coroutines via the extra dependency we've added :)
            withTimeout(5_000) {
                quizDocument
                    .set(quiz)
                    .await()

                _createSuccess.value = true
            }

        } catch (e: Exception) {
            throw QuizSaveError(e.message.toString(), e)
        }
    }

    fun reset() {
        _createSuccess.value = false
    }

    class QuizSaveError(message: String, cause: Throwable) : Exception(message, cause)
    class QuizRetrievalError(message: String) : Exception(message)
}