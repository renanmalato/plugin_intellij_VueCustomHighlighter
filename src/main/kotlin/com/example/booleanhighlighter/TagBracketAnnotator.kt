package com.example.booleanhighlighter

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlToken
import com.intellij.psi.xml.XmlTokenType
import java.awt.Color

class TagBracketAnnotator : Annotator {
    companion object {
        private val BRACKET_ATTRIBUTES = TextAttributes().apply {
            foregroundColor = Color(0x89, 0xDD, 0xFF)  // 89DDFF (Light blue - you can change this color)
        }

        val BRACKET_HIGHLIGHT = TextAttributesKey.createTextAttributesKey(
            "VUE_TAG_BRACKETS",
            BRACKET_ATTRIBUTES
        )
    }

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is XmlToken) {
            when (element.tokenType) {
                XmlTokenType.XML_START_TAG_START,  // <
                XmlTokenType.XML_END_TAG_START,   // </
                XmlTokenType.XML_TAG_END,         // >
                XmlTokenType.XML_EMPTY_ELEMENT_END -> {  // />
                    holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(element.textRange)
                        .textAttributes(BRACKET_HIGHLIGHT)
                        .create()
                }
                else -> {}
            }
        }
    }
} 