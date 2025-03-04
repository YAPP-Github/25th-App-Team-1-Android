package com.yapp.fortune.page.content

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yapp.fortune.component.CodyBox

/**
 * 코디 정보
 */
@Composable
fun CodyContent(
    luckyOutfitTop: String,
    luckyOutfitBottom: String,
    luckyOutfitShoes: String,
    luckyOutfitAccessory: String,
) {
    data class CodyItem(val resId: Int, val categoryLabel: String, val clothsNameLabel: String)
    val codyList = listOf(
        CodyItem(core.designsystem.R.drawable.ic_top, "상의", luckyOutfitTop),
        CodyItem(core.designsystem.R.drawable.ic_pants, "하의", luckyOutfitBottom),
        CodyItem(core.designsystem.R.drawable.ic_shoes, "신발", luckyOutfitShoes),
        CodyItem(core.designsystem.R.drawable.ic_glove, "액세서리", luckyOutfitAccessory),
    )

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        codyList.chunked(2).forEach { rowItems ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                rowItems.forEach { item ->
                    CodyBox(
                        resId = item.resId,
                        categoryLabel = item.categoryLabel,
                        clothsNameLabel = item.clothsNameLabel,
                    )
                }
            }
        }
    }
}
