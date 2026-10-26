package com.bytheour.utils

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import network.chaintech.kmp_date_time_picker.ui.datetimepicker.WheelDateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.DateTimePickerView
import network.chaintech.kmp_date_time_picker.utils.TimeFormat
import network.chaintech.kmp_date_time_picker.utils.WheelPickerDefaults
import network.chaintech.kmp_date_time_picker.utils.dateTimeToString


@Composable
fun WheelDateTimePickerBottomSheet(
    showDatePicker: Boolean,
    onDoneClick: (String) -> Unit,
    onDismiss: () -> Unit,
) {
    if (showDatePicker) {

        val now = remember { Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()) }

        WheelDateTimePickerView(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 22.dp, bottom = 26.dp),
            showDatePicker = showDatePicker,
            titleStyle = TextStyle(
                fontSize = 18.sp,
                fontFamily = JtBtoFonts.getOpenSansBold(),
                color = Color(0xFF301E69),
            ),
            doneLabelStyle = TextStyle(
                fontSize = 16.sp,
                fontFamily = JtBtoFonts.getInterSemiBoldFont(),
                color = Color(0xFF301E69),
            ),

            minDate = now,

            selectorProperties = WheelPickerDefaults.selectorProperties(
                borderColor = Color.Black,
            ),
            timeFormat = TimeFormat.AM_PM,
            rowCount = 5,
            height = 180.dp,
            dragHandle = {
                HorizontalDivider(
                    modifier = Modifier.padding(top = 8.dp).width(50.dp).clip(CircleShape),
                    thickness = 4.dp,
                    color = Color(0xFF301E69)
                )
            },
            shape = RoundedCornerShape(15.dp),
//            shape = RoundedCornerShape(topEnd = 18.dp, topStart = 18.dp),
            onDoneClick = {
                val formattedDate = dateTimeToString(it, "yyyy-MM-dd HH:mm:ss")
                onDoneClick(formattedDate)
            },
            dateTimePickerView = DateTimePickerView.DIALOG_VIEW,
            onDismiss = { onDismiss() },
        )
    }
}
