@file:Suppress("EnumEntryName")

package com.mark.sharee.utils

import android.graphics.Typeface
import android.util.AttributeSet
import android.widget.TextView

class FontManager private constructor(private val attrs: IntArray, private val index: Int) {

    // values need to be the same as font attr in attrs.xml
    enum class Font(@Suppress("unused") private val attrValue: Int) {
        orionRegular(ORION_REGULAR) {
            override val typeface: Typeface?
                get() {
                    assertInit()
                    return orionRegularTypeface
                }
        },
        orionBold(ORION_BOLD) {
            override val typeface: Typeface?
                get() {
                    assertInit()
                    return orionBoldTypeface
                }
        },
        orionExtraBold(ORION_EXTRA_BOLD) {
            override val typeface: Typeface?
                get() {
                    assertInit()
                    return orionExtraBoldTypeface
                }
        },
        orionBlack(ORION_BLACK) {
            override val typeface: Typeface?
                get() {
                    assertInit()
                    return orionBlackTypeface
                }
        },
        clanOffcUltra(CLAN_OFFC_ULTRA) {
            override val typeface: Typeface?
                get() {
                    assertInit()
                    return clanOffcUltraTypeface
                }
        },
        clanOTUltra(CLAN_OT_ULTRA) {
            override val typeface: Typeface?
                get() {
                    assertInit()
                    return clanOTUltraTypeface
                }
        };

        abstract val typeface: Typeface?

        protected fun assertInit() {
            if (!isInit) {
                throw IllegalStateException("FontManager must be initialized")
            }
        }

        companion object {
            internal fun fromValue(value: Int): Font {
                return when (value) {
                    ORION_REGULAR -> orionRegular
                    ORION_BOLD -> orionBold
                    ORION_EXTRA_BOLD -> orionExtraBold
                    ORION_BLACK -> orionBlack
                    CLAN_OFFC_ULTRA -> clanOffcUltra
                    CLAN_OT_ULTRA -> clanOTUltra
                    else -> throw IllegalArgumentException("Unsupported font value: " + value)
                }
            }
        }
    }

    fun apply(v: TextView, set: AttributeSet?, defStyleAttr: Int) {
        if (set == null) {
            return
        }

        val typedArray = v.context.theme.obtainStyledAttributes(set, attrs,
                defStyleAttr, 0) ?: return

        if (typedArray.hasValue(index)) {
            val font = Font.fromValue(typedArray.getInt(index, -1))
            v.typeface = font.typeface
        }

        typedArray.recycle()
    }

    companion object {
        private val ORION_REGULAR = 0
        private val ORION_BOLD = 1
        private val ORION_EXTRA_BOLD = 2
        private val ORION_BLACK = 3
        private val CLAN_OFFC_ULTRA = 4
        private val CLAN_OT_ULTRA = 5
        private var orionRegularTypeface: Typeface? = null
        private var orionBoldTypeface: Typeface? = null
        private var orionExtraBoldTypeface: Typeface? = null
        private var orionBlackTypeface: Typeface? = null
        private var clanOffcUltraTypeface: Typeface? = null
        private var clanOTUltraTypeface: Typeface? = null
        private var isInit: Boolean = false

        @JvmStatic
        fun initialize(regular: Typeface, bold: Typeface, extra: Typeface, black: Typeface, clanOffcUltra: Typeface, clanOTUltra: Typeface) {
            orionRegularTypeface = regular
            orionBoldTypeface = bold
            orionExtraBoldTypeface = extra
            orionBlackTypeface = black
            clanOffcUltraTypeface = clanOffcUltra
            clanOTUltraTypeface = clanOTUltra
            isInit = true
        }

        @JvmStatic
        fun forAttrs(attrs: IntArray, index: Int): FontManager = FontManager(attrs, index)
    }
}



