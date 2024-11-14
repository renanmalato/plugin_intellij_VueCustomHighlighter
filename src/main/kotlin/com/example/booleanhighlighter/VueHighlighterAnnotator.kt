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

class VueHighlighterAnnotator : Annotator {
    companion object {
        private val STANDARD_HTML_TAGS = setOf(
            "script", 
            "style"
        )

        private val FORCE_INCLUDE_TAGS = setOf(
            "template"  // Tags we want to treat as custom components
        )

        val CUSTOM_TAG_ATTRIBUTES = TextAttributes().apply {
            foregroundColor = Color(0x23, 0xA2, 0x7F)  // 23A27F (Vue.js green)
        }

        val CUSTOM_TAG = TextAttributesKey.createTextAttributesKey(
            "VUE_CUSTOM_TAG",
            CUSTOM_TAG_ATTRIBUTES
        )
    }

    override fun annotate(element: PsiElement, holder: AnnotationHolder) {
        when (element) {
            is XmlTag -> {
                if (isCustomComponent(element.name)) {
                    element.children.forEach { child ->
                        if (child is XmlToken) {
                            when (child.tokenType) {
                                XmlTokenType.XML_START_TAG_START,  // <
                                XmlTokenType.XML_END_TAG_START,   // </
                                XmlTokenType.XML_TAG_END,         // >
                                XmlTokenType.XML_EMPTY_ELEMENT_END, // />
                                XmlTokenType.XML_NAME -> {        // tag name
                                    holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                                        .range(child.textRange)
                                        .textAttributes(CUSTOM_TAG)
                                        .create()
                                }
                            }
                        }
                    }
                }
            }
            is XmlToken -> {
                if (element.tokenType == XmlTokenType.XML_DATA_CHARACTERS &&
                    element.text in setOf("true", "false", "null")) {
                    holder.newSilentAnnotation(HighlightSeverity.INFORMATION)
                        .textAttributes(CUSTOM_TAG)
                        .create()
                }
            }
        }
    }

    private fun isCustomComponent(tagName: String): Boolean {
        val tag = tagName.lowercase()
        
        // First check if it's in our force include list
        if (tag in FORCE_INCLUDE_TAGS) {
            return true
        }
        
        // Then check if it's a standard HTML tag we want to exclude
        if (tag in STANDARD_HTML_TAGS) {
            return false
        }
        
        // Finally check for custom component patterns
        return tagName.contains("-") || 
               (tagName.isNotEmpty() && tagName[0].isUpperCase())
    }
}