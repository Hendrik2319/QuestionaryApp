package net.schwarzbaer.spring.questionary.models.questionary;

import lombok.NonNull;

public record QuestionIndex(int index, Integer subIndex) implements Comparable<QuestionIndex>
{
    public QuestionIndex createSubIndex(int subIndex)
    {
        return new QuestionIndex(index, subIndex);
    }

    public static QuestionIndex createIndex(int index)
    {
        return new QuestionIndex(index, null);
    }

    public boolean isBefore(@NonNull QuestionIndex other)
    {
        if (this.index < other.index) return true;
        if (this.index > other.index) return false;
        if (this.subIndex == null && other.subIndex != null) return true;
        if (this.subIndex != null && other.subIndex == null) return false;
        if (this.subIndex == null && other.subIndex == null) return false;
        return this.subIndex < other.subIndex;
    }

    @Override
    public int compareTo(QuestionIndex other)
    {
        if (other == null  ) return -1;
        if (isBefore(other)) return -1;
        if (equals  (other)) return 0;
        return 1;
    }
}
