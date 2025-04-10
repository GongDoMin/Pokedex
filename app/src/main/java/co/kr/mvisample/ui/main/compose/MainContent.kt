package co.kr.mvisample.ui.main.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.kr.mvisample.ui.main.model.MainAction
import co.kr.mvisample.ui.main.model.MainUiState

@Composable
fun MainContent(
    uiState: MainUiState,
    sendAction: (MainAction) -> Unit
) {
    val background =
        if (uiState.isLoading) Color.DarkGray.copy(alpha = 0.2f)
        else if (uiState.isError) Color.Red.copy(alpha = 0.2f)
        else Color.White

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiState.name,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${uiState.age} years old",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${uiState.height} cm",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "${uiState.weight} kg",
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                enabled = uiState.isEnabledButton,
                onClick = { sendAction(MainAction.OnClickPlusAgeButton) }
            ) {
                Text(
                    text = "Plus Age",
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                enabled = uiState.isEnabledButton,
                onClick = { sendAction(MainAction.OnClickPlusHeightButton) }
            ) {
                Text(
                    text = "Plus Height",
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                enabled = uiState.isEnabledButton,
                onClick = { sendAction(MainAction.OnClickPlusWeightButton) }
            ) {
                Text(
                    text = "Plus Weight",
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                enabled = uiState.isEnabledButton,
                onClick = { sendAction(MainAction.OnClickErrorButton) }
            ) {
                Text(
                    text = "Error",
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                enabled = uiState.isEnabledButton,
                onClick = { sendAction(MainAction.OnClickLoadingButton) }
            ) {
                Text(
                    text = "Loading",
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                enabled = uiState.isEnabledButton,
                onClick = { sendAction(MainAction.OnClickNextButton) }
            ) {
                Text(
                    text = "Next",
                    fontSize = 16.sp
                )
            }
        }
    }
}