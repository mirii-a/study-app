package com.japanese.study_app.testObjects;

import com.japanese.study_app.model.Category;
import com.japanese.study_app.model.EnglishWord;
import com.japanese.study_app.model.Word;
import com.japanese.study_app.model.WordDefinition;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class TestObjectsWord {

    public Word getKoteiKanNen() {
        Collection<EnglishWord> englishWords = new ArrayList<>();
        englishWords.add(new EnglishWord("stereotype"));
        englishWords.add(new EnglishWord("prejudice"));
        englishWords.add(new EnglishWord("fixed idea"));

        Set<Category> categories = new HashSet<>();
        categories.add(new Category("noun"));
        categories.add(new Category("opinion"));

        Word koteikannen = new Word("固定観念", englishWords, "こていかんねん", categories);

        WordDefinition wordDefinitions = new WordDefinition();
        wordDefinitions.setDefinitionJapanese("いつも頭から離れないで、その人の思考を拘束するような考え;特定の考えや価値観が凝り固まり、思考を束縛するような考え方");
        wordDefinitions.setDefinitionEnglish("An idea that is always stuck in one's head and restricts one's thinking;A way of thinking in which certain ideas or values become rigid and restrict one's thinking");

        wordDefinitions.setWord(koteikannen);
        koteikannen.setDefinitions(wordDefinitions);

        return koteikannen;
    }
}
