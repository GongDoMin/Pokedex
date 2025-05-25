package co.kr.mvisample.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import co.kr.mvisample.common.base.DialogAction
import co.kr.mvisample.design.PokemonTheme

@Composable
fun PokemonDialog(
    title: String,
    content: String,
    onSendAction: (DialogAction.BasicDialogAction) -> Unit,
    modifier: Modifier = Modifier,
    dialogProperties: DialogProperties = DialogProperties(),
) {
    Dialog(
        onDismissRequest = { onSendAction(DialogAction.BasicDialogAction.OnDismissDialog) },
        properties = dialogProperties
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .wrapContentHeight(),
            shape = RoundedCornerShape(16.dp),
            color = Color.White,
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = PokemonTheme.typography.titleMedium,
                    color = Color.Black
                )
                HeightSpacer(16.dp)
                Text(
                    text = content,
                    style = PokemonTheme.typography.bodyMedium,
                    color = Color.Black
                )
                HeightSpacer(24.dp)
                Row(
                    modifier = Modifier.fillMaxWidth().size(10.dp).requiredSize(20.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = {
                            onSendAction(DialogAction.BasicDialogAction.OnClickPositiveButton)
                        }
                    ) {
                        Text(
                            text = "확인",
                            style = PokemonTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun PokemonDialogPreview() {
    PokemonTheme {
        PokemonDialog(
            title = "포켓몬이 선택되지 않았습니다.",
            content = "포켓몬을 선택한 후 다시 시도해주세요.",
            onSendAction = {}
        )
    }
}