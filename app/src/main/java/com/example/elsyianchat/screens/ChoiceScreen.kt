package com.example.elsyianchat.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.elsyianchat.appComponent.VerticalSpace
import com.example.elsyianchat.ui.theme.inter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChoiceScreen(navController: NavHostController) {

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Elsyian AI",
                        fontFamily = inter
                    )
                }
            )
        }
    ) {

        Column(
            Modifier
                .background(MaterialTheme.colorScheme.primary)
                .fillMaxSize()
                .padding(it)
        ) {

            Text(
                text = "Select an option to continue\nwith Elsyian AI",
                textAlign = TextAlign.Center,
                fontFamily = inter,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )


            ElevatedCard(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 15.dp,
                    pressedElevation = 25.dp
                ),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                )
            ) {
                Text(
                    text = "Chat with Elsyian AI",
                    fontFamily = inter,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(10.dp)
                )

                Text(
                    text = "Chat with Elysian AI to get answers to your questions",
                    fontFamily = inter,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                )

                TextButton(
                    onClick = {
                        navController.navigate(Screens.ChatScreen.route)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "Start Chat",
                        fontFamily = inter,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.padding(10.dp)
                    )

                }

            }

            ElevatedCard(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 15.dp,
                    pressedElevation = 25.dp
                ),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                )
            ) {
                Text(
                    text = "Photo-Reasoning with Elsyian AI",
                    fontFamily = inter,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(10.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = "Upload an image and Elysian AI will try to answers to your questions",
                    fontFamily = inter,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.onSecondary,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                )

                TextButton(
                    onClick = {
                        navController.navigate(Screens.PhotoReasoningScreen.route)
                    },
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                        text = "Start Photo-Reasoning",
                        fontFamily = inter,
                        fontSize = 16.sp,
                        color = MaterialTheme.colorScheme.onSecondary,
                        modifier = Modifier.padding(10.dp)
                    )

                }

            }

        }
    }

}