package com.example.booleanhighlighter

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.psi.PsiElement
import java.awt.Color

class BooleanAnnotator : Annotator {
    companion object {
        private val BOOLEAN_ATTRIBUTES = TextAttributes().apply {
            foregroundColor = Color(0xFF, 0x9E, 0x64)  // FF9E64 (Orange)
        }

        val BOOLEAN_LITERAL = TextAttributesKey.createTextAttributesKey(
            "CUSTOM_JS_LITERAL",
            BOOLEAN_ATTRIBUTES
        )
    }

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element.text in setOf("true", "false", "null")) {
            holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                .textAttributes(BOOLEAN_LITERAL)
                .create()
        }
    }
} 