package com.bytheour.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import bytheour.composeapp.generated.resources.Res
import bytheour.composeapp.generated.resources.inter
import bytheour.composeapp.generated.resources.inter_Regular
import bytheour.composeapp.generated.resources.inter_bold
import bytheour.composeapp.generated.resources.inter_light
import bytheour.composeapp.generated.resources.inter_medium
import bytheour.composeapp.generated.resources.inter_semibold
import bytheour.composeapp.generated.resources.openSans_Bold
import bytheour.composeapp.generated.resources.openSans_ExtraBold
import bytheour.composeapp.generated.resources.openSans_LightItalic
import bytheour.composeapp.generated.resources.openSans_Regular
import bytheour.composeapp.generated.resources.openSans_Semibold
import org.jetbrains.compose.resources.Font


object JtBtoFonts {

    @Composable
    fun getOpenSansLightItalic(): FontFamily {
        return FontFamily(Font(Res.font.openSans_LightItalic))
    }
    @Composable
    fun getOpenSansRegular(): FontFamily {
        return FontFamily(Font(Res.font.openSans_Regular))
    }

    @Composable
    fun getOpenSansSemiBold(): FontFamily {
        return FontFamily(Font(Res.font.openSans_Semibold))
    }

    @Composable
    fun getOpenSansBold(): FontFamily {
        return FontFamily(Font(Res.font.openSans_Bold))
    }

    @Composable
    fun getOpenSansExtraBold(): FontFamily {
        return FontFamily(Font(Res.font.openSans_ExtraBold))
    }
    @Composable
    fun getInterFont(): FontFamily {
        return FontFamily(Font(Res.font.inter))
    }
    @Composable
    fun getInterBoldFont(): FontFamily {
        return FontFamily(Font(Res.font.inter_bold))
    }

    @Composable
    fun getInterLightFont(): FontFamily {
        return FontFamily(Font(Res.font.inter_light))
    }

    @Composable
    fun getInterMediumFont(): FontFamily {
        return FontFamily(Font(Res.font.inter_medium))
    }

    @Composable
    fun getInterSemiBoldFont(): FontFamily {
        return FontFamily(Font(Res.font.inter_semibold))
    }


    @Composable
    fun getInterRegular(): FontFamily {
        return FontFamily(Font(Res.font.inter_Regular))
    }
}

