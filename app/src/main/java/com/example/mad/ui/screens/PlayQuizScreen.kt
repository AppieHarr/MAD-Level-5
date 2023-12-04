package com.example.mad.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mad.R
import com.example.mad.viewmodel.QuizViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun PlayQuizScreen(navController: NavController, viewModel: QuizViewModel) {
    val context = LocalContext.current

    viewModel.getQuiz()

    val quiz by viewModel.quiz.observeAsState()
    val errorMsg by viewModel.errorText.observeAsState()

    var answer by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = context.getString(R.string.title_play_quiz)) },
                // "Arrow back" implementation.
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() } ) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back to homescreen"
                        )
                    }
                }
            )
        },
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            var quizQuestion = quiz?.question
            if (quizQuestion.isNullOrEmpty()) quizQuestion = ""
            Text(text = quizQuestion,
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center,
            )
            OutlinedTextField(
                value = answer,
                // below line is used to add placeholder ("hint") for our text field.
                placeholder = { Text(text = stringResource(id = R.string.answer)) },
                onValueChange = {
                    answer = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.LightGray)
                    .padding(8.dp),
                label = { Text(stringResource(R.string.answer)) }
            )
            Button(modifier = Modifier.fillMaxWidth(),
                onClick = {
                    // VERIFY ANSWER
                    if (viewModel.isAnswerCorrect(answer)) {
                        Toast.makeText(context, R.string.answer_correct, Toast.LENGTH_SHORT).show()
                        answer = ""
                        navController.popBackStack()
                    } else {
                        val msg = context.getString(R.string.answer_incorrect, quiz?.answer)
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                })
            {
                Text(text = context.getString(R.string.confirm_answer))
            }
            if (!errorMsg.isNullOrEmpty()) {
                Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
                viewModel.reset()
            }
        }
    }
}