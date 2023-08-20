package com.batman.app.util

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.provider.ContactsContract
import android.text.Html
import android.text.Spannable
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.util.DisplayMetrics
import android.util.TypedValue
import android.view.WindowManager
import org.json.JSONException
import org.json.JSONObject

import java.util.regex.Pattern


object PublicFunction {

    private const val CLIP_DATA_LABEL = "clipData"

    @JvmStatic
    fun copyToClipBoard(context: Context, text: CharSequence) {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText(CLIP_DATA_LABEL, text)
        clipboard.setPrimaryClip(clip)
    }

    @JvmStatic
    fun getDrawable(id: Int, context: Context): Drawable? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getDrawable(id)
        } else {
            context.resources.getDrawable(id)
        }
    }

    @JvmStatic
    fun getColor(id: Int, context: Context): Int {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            context.getColor(id)
        } else {
            context.resources.getColor(id)
        }
    }

    @JvmStatic
    fun handleContactResult(data: Intent, context: Context): String? {

        var phoneNumber: String? = null
        val contactData = data.data

        val cursor1 = context.contentResolver.query(contactData!!, null, null, null, null)
        if (cursor1!!.moveToFirst()) {

            val tempContactID =
                cursor1.getString(cursor1.getColumnIndex(ContactsContract.Contacts._ID))
            val cursor2 = context.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                null,
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + tempContactID,
                null,
                null
            )

            while (cursor2!!.moveToNext()) {
                val tempNumberHolder =
                    cursor2.getString(cursor2.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))

                if (tempNumberHolder.substring(0, 3) == "+98") {
                    phoneNumber = tempNumberHolder
                } else
                    phoneNumber = tempNumberHolder
            }
        }
        return phoneNumber
    }

    @JvmStatic
    fun convertPixelsToDp(px: Float, context: Context): Float {
        val displayMetrics = context.resources.displayMetrics
        val dp = Math.round(px / (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT))
        return dp.toFloat()
    }

    @JvmStatic
    fun convertDpToPixels(dp: Float, context: Context): Int {
        val resources = context.resources
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            dp,
            resources.displayMetrics
        ).toInt()
    }

    @JvmStatic
    fun shareLinkDialog(context: Context, text: String) {
        val sendIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, text)
//            putExtra(Intent.EXTRA_SUBJECT, "hello")
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    @JvmStatic
    fun openUrlInBrowser(context: Context, url: String) {
        var webUrl = url
        if (!webUrl.startsWith("http://") && !webUrl.startsWith("https://"))
            webUrl = "http://$webUrl"

        val browserIntent =
            Intent(Intent.ACTION_VIEW, Uri.parse(webUrl))
        context.startActivity(browserIntent)
    }

    @JvmStatic
    fun shareLocationDialog(context: Context, latitude: Double, longitude: Double) {
        val uri = ("geo:" + latitude.toString() + ","
                + longitude.toString() + "?q=" + latitude
            .toString() + "," + longitude)
        context.startActivity(
            Intent(
                Intent.ACTION_VIEW,
                Uri.parse(uri)
            )
        )

    }


    @JvmStatic
    fun isValidEmailAddress(email: String): Boolean {
        val ePattern =
            "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$"
        val p = Pattern.compile(ePattern)
        val m = p.matcher(email)
        return m.matches()
    }

    @JvmStatic
    fun isValidPostCode(postCode: String): Boolean {
        return postCode.length == 10
    }

    @JvmStatic
    fun fromHtml(text: String): String {
        val t = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(
                text,
                Html.FROM_HTML_MODE_COMPACT
            )
        } else {
            Html.fromHtml(text)
        }
        return t.toString()
    }

    @JvmStatic
    fun addPriceSeparator(price: String): String {

        if (!price.contains(",")) {

            var a = price.split(" ")

            if (a.size > 1) {
                var priceSeparated = String.format("%,d", a[0].toLong())
                return priceSeparated + " " + a[1]
            } else {
                return String.format("%,d", price.toLong())
            }
        } else {
            return price
        }

    }

    @JvmStatic
    fun increaseFontSizeForPath(
        spannable: Spannable,
        path: String,
        increaseTime: Float
    ): Spannable {
        val startIndexOfPath = spannable.toString().indexOf(path)
        spannable.setSpan(
            RelativeSizeSpan(increaseTime), startIndexOfPath,
            startIndexOfPath + path.length, 0
        )
        return spannable
    }

    @JvmStatic
    fun increaseFontSizeForPathWithColor(
        spannable: Spannable,
        path: String,
        colorSpanAmount : ForegroundColorSpan,
        increaseTime: Float
    ): Spannable {
        val startIndexOfPath = spannable.toString().indexOf(path)
        spannable.setSpan(
            RelativeSizeSpan(increaseTime), startIndexOfPath,
            startIndexOfPath + path.length, 0
        )
        spannable.setSpan(
            colorSpanAmount,
            startIndexOfPath,
            startIndexOfPath + path.length,
            0
        )
        return spannable
    }

    @JvmStatic
    fun actionCall(context: Context, phoneNumber: String) {
        context.startActivity(
            Intent(
                Intent.ACTION_DIAL,
                Uri.fromParts("tel", phoneNumber, null)
            )
        )
    }

    @JvmStatic
    fun getDeviceName(): String? {
        val manufacturer = Build.MANUFACTURER
        val model = Build.MODEL
        return if (model.startsWith(manufacturer)) {
            capitalize(model)
        } else capitalize(manufacturer) + " " + model
    }


    @JvmStatic
    private fun capitalize(str: String): String {
        if (TextUtils.isEmpty(str)) {
            return str
        }
        val arr = str.toCharArray()
        var capitalizeNext = true
        val phrase = StringBuilder()
        for (c in arr) {
            if (capitalizeNext && Character.isLetter(c)) {
                phrase.append(Character.toUpperCase(c))
                capitalizeNext = false
                continue
            } else if (Character.isWhitespace(c)) {
                capitalizeNext = true
            }
            phrase.append(c)
        }
        return phrase.toString()
    }

    @JvmStatic
    fun getScreenResolution(context: Context): String? {
        val wm =
            context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val metrics = DisplayMetrics()
        display.getMetrics(metrics)
        val width = metrics.widthPixels
        val height = metrics.heightPixels
        return "{$width,$height}"
    }

    @JvmStatic
    fun generateJSONObject(jsonString:String): JSONObject? {
        val newStr =
            jsonString.replace(Pattern.quote("\\").toRegex(), "")
        val kk = newStr.replace("\"\\[".toRegex(), "[")
        val jj = kk.replace("]\"".toRegex(), "\\]")
        return try {
            JSONObject(jj)
        } catch (e: JSONException) {
            e.printStackTrace()
            null
        }
    }

}