package com.yandex.money.api.model.showcase.components;

import com.yandex.money.api.utils.ToStringBuilder;

/**
 * Block of plain text.
 *
 * @author Aleksandr Ershov (asershov@yamoney.com)
 */
public class TextBlock {

    /**
     * Content.
     */
    public final String content;

    public TextBlock(String content) {
        if (content == null) {
            throw new NullPointerException("content is null");
        }
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TextBlock textBlock = (TextBlock) o;

        return content.equals(textBlock.content);
    }

    @Override
    public int hashCode() {
        return content.hashCode();
    }

    @Override
    public String toString() {
        return "TextBlock{" +
                "content='" + content + '\'' +
                '}';
    }

    /**
     * Link.
     */
    public static final class WithLink extends TextBlock {

        public final String link;

        public WithLink(String label, String link) {
            super(label);
            if (link == null) {
                throw new NullPointerException("link is null");
            }
            this.link = link;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            if (!super.equals(o)) return false;

            WithLink withLink = (WithLink) o;

            return link.equals(withLink.link);
        }

        @Override
        public int hashCode() {
            int result = super.hashCode();
            result = 31 * result + link.hashCode();
            return result;
        }

        @Override
        public String toString() {
            return new ToStringBuilder("WithLink")
                    .append("link", link)
                    .append("content", content).toString();
        }
    }
}
