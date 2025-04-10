package co.kr.mvisample.ui.detail.compose

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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.kr.mvisample.ui.detail.model.DetailAction
import co.kr.mvisample.ui.detail.model.DetailUiState

/**
 * OnClickSpecialButton 의 email 은 uiState 가 아니지만 비즈니스 로직을 수행하기 위해서 필요한 프로퍼티이다.
 *
 * 즉, SpecialButton 을 눌렀을 때는 email 을 알 수 없기 때문에 onClickSpecialButton 파라미터를 따로 분리했다.
 */
@Composable
fun DetailContent(
    uiState: DetailUiState,
    sendAction: (DetailAction) -> Unit,
    onClickSpecialButton: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = uiState.text,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = uiState.specialText,
                fontSize = 16.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { sendAction(DetailAction.OnClickFirstButton) }
            ) {
                Text(
                    text = "First",
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { sendAction(DetailAction.OnClickSecondButton) }
            ) {
                Text(
                    text = "Second",
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { sendAction(DetailAction.OnClickThirdButton) }
            ) {
                Text(
                    text = "Third",
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = onClickSpecialButton
            ) {
                Text(
                    text = "Special",
                    fontSize = 16.sp
                )
            }
        }
    }
}