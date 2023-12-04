package com.example.mad.ui.screens

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.mad.R
import com.example.mad.viewmodel.QuizViewModel

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun HomeScreen(navController: NavController,viewModel: QuizViewModel) {
    val context = LocalContext.current
    val scaffoldState = rememberScaffoldState()

    val errorMsg by viewModel.errorText.observeAsState()
    val successfullyCreatedQuiz by viewModel.createSuccess.observeAsState()

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = stringResource(id = R.string.app_name))
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
            Text(text = context.getString(R.string.welcome),
                modifier = Modifier.padding(bottom = 16.dp),
                textAlign = TextAlign.Center,
            )
            Button(modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(Screen.CreateQuizScreen.route)
                }) {
                Text(text = context.getString(R.string.create_quiz))
            }
            Button(modifier = Modifier.fillMaxWidth(),
                onClick = { navController.navigate(Screen.PlayQuizScreen.route) }) {
                Text(text = context.getString(R.string.start_quiz))
            }
        }
        if (!errorMsg.isNullOrEmpty()) {
            Toast.makeText(context, errorMsg, Toast.LENGTH_LONG).show()
            viewModel.reset()
        }
        if (successfullyCreatedQuiz != null) {
            if (successfullyCreatedQuiz!!) {
                Toast.makeText(
                    context,
                    stringResource(id = R.string.successfully_created_quiz),
                    Toast.LENGTH_LONG
                ).show()
                viewModel.reset()
            }
        }
    }
}