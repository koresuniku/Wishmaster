package com.koresuniku.wishmaster_v4.core.util.text

import android.content.res.Configuration
import android.text.*
import android.text.style.LeadingMarginSpan
import android.util.Log
import com.koresuniku.wishmaster_v4.application.shared_preferences.UiParams
import com.koresuniku.wishmaster_v4.core.data.model.boards.BoardModel
import com.koresuniku.wishmaster_v4.core.data.model.threads.File
import com.koresuniku.wishmaster_v4.core.gallery.ImageItemData
import com.koresuniku.wishmaster_v4.ui.util.UiUtils
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by koresuniku on 04.01.18.
 */

class WishmasterTextUtils @Inject constructor() {

    fun obtainBoardIdDashName(boardModel: BoardModel): String {
        return "/${boardModel.getBoardId()}/ - ${boardModel.getBoardName()}"
    }

    fun obtainBoardIdDashName(boardId: String, boardName: String): String {
        return "/$boardId/ - $boardName"
    }

    fun obtainImageResume(file: File): String {
        val width = file.width
        val height = file.height
        val size = file.size
        val format = file.thumbnail
                .removePrefix(file.thumbnail.subSequence(0, file.thumbnail.indexOf(".") + 1))
                .toUpperCase()
        return "$width * $height\n$size, $format"
    }

    fun getSpannedFromHtml(input: String): Spanned { return Html.fromHtml(input) }

    fun getSubjectSpanned(subject: String, boardId: String): Spanned {
        return getSpannedFromHtml(if (boardId == "b") "" else subject)
    }

    fun getCommentForSingleImageItemTemp(rawComment: String,
                                     uiParams: UiParams,
                                     imageItemData: ImageItemData): Single<Spannable> {
        return Single.create({
            val comment = SpannableString(Html.fromHtml(rawComment))
            var layout = StaticLayout(comment, TextPaint(),
                    if (uiParams.orientation == Configuration.ORIENTATION_LANDSCAPE)
                        uiParams.threadPostItemSingleImageHorizontalWidth
                    else uiParams.threadPostItemSingleImageVerticalWidth,
                    Layout.Alignment.ALIGN_NORMAL,
                    1.0f, 0f, false)

            val endIndex = try {
                if (layout.lineCount < uiParams.commentMaxLines) comment.length
                else layout.getLineEnd(uiParams.commentMaxLines - 1)
            } catch (e: ArrayIndexOutOfBoundsException) { comment.length }
            var cutComment = SpannableString(comment.subSequence(0, endIndex))

            if (endIndex != comment.length) {
                val builder = SpannableStringBuilder(cutComment)
                //builder.insert(endIndex, "ХУЙ")

                cutComment = SpannableString(builder)
            }
            layout = StaticLayout(cutComment.toString(), TextPaint(),
                    if (uiParams.orientation == Configuration.ORIENTATION_LANDSCAPE)
                        uiParams.threadPostItemSingleImageHorizontalWidth
                    else uiParams.threadPostItemSingleImageVerticalWidth,
                    Layout.Alignment.ALIGN_NORMAL,
                    1.0f, 0f, false)
            val lineHeight = layout.height / layout.lineCount
            val linesToSpan = (imageItemData.dimensions.heightInPx +
                    uiParams.threadPostItemShortInfoHeight) / lineHeight
            Log.d("WTU", "firstLine: ${cutComment.substring(
                    layout.getLineStart(0),
                    layout.getLineEnd(0))}")

            val endMarginIndex = try {
                if (layout.lineCount < linesToSpan) cutComment.length
                else layout.getLineEnd(linesToSpan - 1)
            } catch (e: ArrayIndexOutOfBoundsException) { cutComment.length }
            cutComment.setSpan(
                    LeadingMarginSpan.Standard(uiParams.commentMarginWidth),
                    0, endMarginIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)



            it.onSuccess(cutComment)
        })
    }

    fun getCommentDefault(rawComment: String, uiParams: UiParams): Single<Spannable> {
        return Single.create({
            val comment = SpannableString(Html.fromHtml(rawComment))
            val layout = StaticLayout(comment, TextPaint(),
                if (uiParams.orientation == Configuration.ORIENTATION_LANDSCAPE)
                    uiParams.threadPostItemHorizontalWidth
                else uiParams.threadPostItemVerticalWidth,
                Layout.Alignment.ALIGN_NORMAL,
                1.0f, 0f, false)
        try {
            val endIndex = if (layout.lineCount < uiParams.commentMaxLines) comment.length
            else layout.getLineEnd(uiParams.commentMaxLines - 1)
            it.onSuccess(SpannableString(comment.subSequence(0, endIndex)))
        } catch (e: ArrayIndexOutOfBoundsException) {
            it.onSuccess(comment)
        }})
    }

    fun getShortInfo(postCount: Int, fileCount: Int): String {
        var result = ""
        result += getCorrectRussianEndings(
                count = postCount, stringForZeroOrMultiple = "постов",
                stringForOne = "пост", stringForTwoThreeFour = "поста")
        result += ", "
        result += getCorrectRussianEndings(
                count = fileCount, stringForZeroOrMultiple = "файлов",
                stringForOne = "файл", stringForTwoThreeFour = "файла")
        return result
    }

    private fun getCorrectRussianEndings(count: Int, stringForZeroOrMultiple: String,
                                         stringForOne: String, stringForTwoThreeFour: String): String {
        if (count == 0) return count.toString() + " " + stringForZeroOrMultiple
        val lastNumber: Int
        var signsCount = -1
        if (count < 10) {
            lastNumber = count
        } else {
            signsCount = count.toString().length
            lastNumber = Integer.parseInt(count.toString().substring(signsCount - 1, signsCount))
        }
        if (signsCount >= 2 && count.toString()
                        .substring(signsCount - 2, signsCount)
                        .matches(Regex("1[0-9]"))) {
            return count.toString() + " " + stringForZeroOrMultiple
        }
        if (lastNumber == 1) {
            if (count >= 10 && count.toString().substring(signsCount - 2, signsCount) == "11") {
                return count.toString() + " " + stringForZeroOrMultiple
            }
            return count.toString() + " " + stringForOne
        }
        if (lastNumber == 2 || lastNumber == 3 || lastNumber == 4) {
            return count.toString() + " " + stringForTwoThreeFour
        }
        return count.toString() + " " + stringForZeroOrMultiple
    }
}