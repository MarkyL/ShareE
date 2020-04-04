package com.mark.sharee.utils

import android.text.TextUtils
import com.google.i18n.phonenumbers.NumberParseException
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.google.i18n.phonenumbers.Phonenumber

import java.util.regex.Pattern

class PhoneNumber private constructor(private val phoneNumber: Phonenumber.PhoneNumber) {

    fun equals(phoneNumber: PhoneNumber?): Boolean =
            phoneNumber != null && (this.phoneNumber == phoneNumber.phoneNumber || e164() == phoneNumber.e164())

    fun asUsername(): String? = if (!TextUtils.isEmpty(e164())) e164().substring(1) else null

    fun e164(): String = util.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164)

    override fun toString(): String =
            util.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL)

    companion object {

        private val util = PhoneNumberUtil.getInstance()
        private val REGION = "IL"
        private val VALID_PHONE_LENGTH_WITH_PREFIX = 12

        @JvmStatic
        fun create(number: String): PhoneNumber? {
            try {
                val phoneNumber = util.parse(number, REGION)
                if (isValidUserNumber(util.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.NATIONAL))) {
                    return PhoneNumber(phoneNumber)
                }
            } catch (e: NumberParseException) {
                return null
            }

            return null
        }

        @JvmStatic
        fun isValidNumber(number: String): Boolean {
            val regexStr = "^\\+?(972)?-?[0-9]{2}-?[0-9]{3}-?[0-9]{4}$"
            val regexStrSec = "^\\+?(972)[0-9]{9}$"
            val regexStrThird = "^[0-9]{3}-?[0-9]{3}-?[0-9]{4}$"
            val cleanNumber = number.replace("-".toRegex(), "").replace("\\+".toRegex(), "").replace(" ".toRegex(), "")
            return (Pattern.compile(regexStr).matcher(number).matches() ||
                    Pattern.compile(regexStrSec).matcher(number).matches()) &&
                    cleanNumber.startsWith("9725") &&
                    cleanNumber.length == VALID_PHONE_LENGTH_WITH_PREFIX ||
                    Pattern.compile(regexStrThird).matcher(number).matches() &&
                            number.startsWith("05")
        }

        @JvmStatic
        fun normalize(number: String): String {
            if (TextUtils.isEmpty(number)) {
                throw IllegalArgumentException("Phone number can't be empty")
            }

            val phoneNumber = PhoneNumber.create(number)
            if (phoneNumber != null) {
                return phoneNumber.e164()
            }

            throw IllegalArgumentException(String.format("Cannot normalize %s", number))
        }

        @JvmStatic
        private fun isValidUserNumber(number: String): Boolean {
            val regexStr = "^\\+?(972)?-?[0-9]{3}-?[0-9]{3}-?[0-9]{4}$"
            val astrixPattern = "^\\*[0-9]{4}$" //*5555
            val freeNumPattern = "^1-?8[0-9]{2}-?[0-9]{3}-?[0-9]{3}$" //1-800-800-700
            val orgNumPattern = "^1-?7[0-9]{2}-?[0-9]{2}-?[0-9]{2}-?[0-9]{2}$" //1-700-20-40-60

            val patterns = listOf(regexStr, astrixPattern, freeNumPattern, orgNumPattern)

            patterns.forEach {
                val foundMatch = Pattern.compile(it).matcher(number).matches()
                if(foundMatch) return true
            }
            return false
        }

        @JvmStatic
        fun showPrefixError(phoneNumber: String): Boolean =
                !phoneNumber.isEmpty() && !isValidPrefix(phoneNumber)

        @JvmStatic
        private fun isValidPrefix(phoneNumber: String): Boolean =
                phoneNumber == "0" || phoneNumber.startsWith("05")
    }
}