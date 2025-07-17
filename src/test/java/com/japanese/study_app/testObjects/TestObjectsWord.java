package com.japanese.study_app.testObjects;

import com.japanese.study_app.model.*;

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

        HashSet<ExampleSentence> exampleSentences = getExampleSentences();

        koteikannen.setExampleSentences(exampleSentences);
        return koteikannen;
    }

    private static HashSet<ExampleSentence> getExampleSentences() {
        ExampleSentence exampleSentences1 = new ExampleSentence();
        exampleSentences1.setJapaneseSentence("国民に関する固定観念がどれほど有害かを理解するには、少数の外国人と親しくなるだけで十分だ。");
        exampleSentences1.setEnglishSentence("One only needs to become acquainted with a small number of foreigners to realise how harmful national stereotypes can be.");

        ExampleSentence exampleSentences2 = new ExampleSentence();
        exampleSentences2.setJapaneseSentence("年下は年上に敬語を使うべきだというのが、あの世代の固定観念らしい。");
        exampleSentences2.setEnglishSentence("It seems to be a stereotype of that generation that younger people should use honorific language when speaking to older people.");

        ExampleSentence exampleSentences3 = new ExampleSentence();
        exampleSentences3.setJapaneseSentence("彼女は男性がより良いリーダーであるという固定観念を打破することを目指している。");
        exampleSentences3.setEnglishSentence("She aims to break the stereotype that men are better leaders.");

        HashSet<ExampleSentence> exampleSentences = new HashSet<>();
        exampleSentences.add(exampleSentences1);
        exampleSentences.add(exampleSentences2);
        exampleSentences.add(exampleSentences3);
        return exampleSentences;
    }
}
