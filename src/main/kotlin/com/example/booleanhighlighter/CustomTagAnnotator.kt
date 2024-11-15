package com.example.booleanhighlighter

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.markup.TextAttributes
import com.intellij.psi.PsiElement
import com.intellij.psi.xml.XmlTag
import com.intellij.psi.xml.XmlToken
import com.intellij.psi.xml.XmlTokenType
import java.awt.Color

class CustomTagAnnotator : Annotator {
    companion object {
        private val FORCE_INCLUDE_TAGS = setOf(
            "template",
            "slot",
            "component",
            "transition"
        )

        private val CUSTOM_TAG_ATTRIBUTES = TextAttributes().apply {
            foregroundColor = Color(0x23, 0xA2, 0x7F)  // 23A27F (Vue.js green)
        }

        val CUSTOM_TAG = TextAttributesKey.createTextAttributesKey(
            "VUE_CUSTOM_TAG",
            CUSTOM_TAG_ATTRIBUTES
        )
    }

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        if (element is XmlTag && isCustomComponent(element.name)) {
            element.children.forEach { child ->
                if (child is XmlToken && child.tokenType == XmlTokenType.XML_NAME) {
                    holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .range(child.textRange)
                        .textAttributes(CUSTOM_TAG)
                        .create()
                }
            }
        }
    }

    private fun isCustomComponent(tagName: String): Boolean {
        val tag = tagName.lowercase()
        return tag in FORCE_INCLUDE_TAGS || 
               tagName.contains("-") || 
               (tagName.isNotEmpty() && tagName[0].isUpperCase())
    }
} 