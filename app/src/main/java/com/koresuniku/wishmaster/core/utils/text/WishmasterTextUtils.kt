package com.koresuniku.wishmaster.core.utils.text

import android.content.Context
import android.content.res.Configuration
import android.text.*
import android.text.style.ForegroundColorSpan
import android.util.Log
import com.koresuniku.wishmaster.R
import com.koresuniku.wishmaster.application.preferences.UiParams
import com.koresuniku.wishmaster.core.data.model.boards.BoardModel
import com.koresuniku.wishmaster.core.data.model.posts.Post
import com.koresuniku.wishmaster.core.data.model.threads.File
import com.koresuniku.wishmaster.core.data.model.threads.Thread
import com.koresuniku.wishmaster.core.modules.gallery.ImageItemData
import com.koresuniku.wishmaster.core.utils.text.markup.SingleImageCommentMarginSpan
import io.reactivex.Single
import javax.inject.Inject

/**
 * Created by koresuniku on 04.01.18.
 */

class WishmasterTextUtils @Inject constructor() {

    fun obtainBoardIdDashName(boardModel: BoardModel) =
            "/${boardModel.getBoardId()}/ - ${boardModel.getBoardName()}"

    fun obtainBoardIdDashName(boardId: String, boardName: String) = "/$boardId/ - $boardName"

    fun obtainImageShortInfo(file: File): String {
        val width = file.width
        val height = file.height
        val size = file.size
        val format = file.thumbnail
                .removePrefix(file.thumbnail.subSequence(0, file.thumbnail.indexOf(".") + 1))
                .toUpperCase()
        return "$width * $height\n$size kb, $format"
    }

    fun obtainPostHeader(post: Post, position: Int, context: Context): SpannableString {
        val builder = SpannableStringBuilder()
        builder.append("#")
        builder.append("${position + 1}")
        if (post.op == "1") {
            builder.append(" ")
            builder.append("OP")
        }
        builder.setSpan(
                ForegroundColorSpan(context.resources.getColor(R.color.colorOp)),
                0, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        builder.append(" ")
        builder.append("№")
        builder.append(post.num)
        if (!post.name.isEmpty() && !post.name.isBlank()) {
            builder.append(" ")
            builder.append(post.name)
        }
        if (!post.trip.isEmpty() && !post.trip.isBlank()) {
            val tripSpanStart = builder.length
            builder.append(" ")
            builder.append(post.trip)
            builder.setSpan(
                    ForegroundColorSpan(context.resources.getColor(R.color.colorTrip)),
                    tripSpanStart, builder.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
        builder.append(" ")
        builder.append(post.date)


        return SpannableString(builder)
    }

    fun getSubjectSpanned(subject: String, boardId: String): Spanned {
        return Html.fromHtml(if (boardId == "b") "" else subject)
    }

    fun getCommentForSingleImageItemTemp(rawComment: String,
                                     uiParams: UiParams,
                                     imageItemData: ImageItemData): Single<Spannable> {
        return Single.create({
            val comment = SpannableString(Html.fromHtml(rawComment))

            val layout = StaticLayout(comment, uiParams.commentTextPaint,
                    if (uiParams.orientation == Configuration.ORIENTATION_LANDSCAPE)
                        uiParams.threadPostItemSingleImageHorizontalWidth
                    else uiParams.threadPostItemSingleImageVerticalWidth,
                    Layout.Alignment.ALIGN_NORMAL,
                    1.0f, 0f, false)

            val builder = SpannableStringBuilder(comment)

            var linesToSpan = 0
            while (linesToSpan < layout.lineCount &&
                    layout.getLineBottom(linesToSpan) <
                    imageItemData.dimensions.heightInPx +
                    uiParams.threadPostItemShortInfoHeight) ++linesToSpan
            ++linesToSpan

            var commentEndPosition = builder.length
            if (linesToSpan < layout.lineCount) {
                commentEndPosition = layout.getLineStart(linesToSpan)

                val lastChar = builder.substring(commentEndPosition - 1, commentEndPosition)
                if (lastChar != "\n" && lastChar != "\r") {
                    if (lastChar == " ") {
                        builder.replace(commentEndPosition - 1, commentEndPosition, "\n")
                    } else {
                        builder.insert(commentEndPosition - 1, "\n")
                    }
                }
            }

            builder.setSpan(SingleImageCommentMarginSpan(linesToSpan, uiParams.commentMarginWidth),
                    0, commentEndPosition, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

            it.onSuccess(builder)
        })
    }

    fun getCommentDefault(rawComment: String): Single<Spannable> {
        return Single.create({
            it.onSuccess(SpannableString(Html.fromHtml(rawComment)))
        })
    }

    fun getThreadBriefInfo(postCount: Int, fileCount: Int): String {
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

        if (count < 10) lastNumber = count
        else {
            signsCount = count.toString().length
            lastNumber = Integer.parseInt(count.toString().substring(signsCount - 1, signsCount))
        }

        if (signsCount >= 2 && count.toString()
                        .substring(signsCount - 2, signsCount)
                        .matches(Regex("1[0-9]"))) {
            return count.toString() + " " + stringForZeroOrMultiple
        } else if (lastNumber == 1) {
            return count.toString() + " " + stringForOne
        } else if (lastNumber == 2 || lastNumber == 3 || lastNumber == 4) {
            return count.toString() + " " + stringForTwoThreeFour
        }
        return count.toString() + " " + stringForZeroOrMultiple
    }
}