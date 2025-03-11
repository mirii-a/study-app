//package com.japanese.study_app.service.word;
//
//import com.japanese.study_app.dto.WordDto;
//import com.japanese.study_app.model.Category;
//import com.japanese.study_app.model.EnglishWord;
//import com.japanese.study_app.model.Word;
//import com.japanese.study_app.repository.*;
//import com.japanese.study_app.request.AddWordRequest;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.*;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.junit.jupiter.api.BeforeEach;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import static org.mockito.Mockito.*;
//import static org.junit.jupiter.api.Assertions.*;
//
//import java.util.*;
//
//import static org.springframework.test.util.AssertionErrors.assertEquals;
//
//@SpringBootTest
//@ExtendWith(MockitoExtension.class)
//public class WordServiceTest {
//
//    @Mock
//    private WordRepository wordRepository;
//    @Mock
//    private CategoryRepository categoryRepository;
//    @Mock
//    private EnglishWordRepository englishWordRepository;
//    @Mock
//    private WordDefinitionRepository wordDefinitionRepository;
//    @Mock
//    private ExampleSentenceRepository exampleSentenceRepository;
//
//    @InjectMocks
//    private WordService wordService;
//
//    @Test
//    public void convertWordToDto(){
//
//        Collection<EnglishWord> englishWords = new ArrayList<>();
//        EnglishWord allegorical = new EnglishWord("allegorical");
//        EnglishWord emblematic = new EnglishWord("emblematic");
//        englishWords.add(allegorical);
//        englishWords.add(emblematic);
//
//        Set<Category> categories = new HashSet<>();
//        Category art = new Category("art");
//        Category adj = new Category("adj");
//        categories.add(art);
//        categories.add(adj);
//
//        Word testWord = new Word("寓意的", englishWords, "ぐういてき", categories);
//        WordDto convertedWord = wordService.convertWordToDto(testWord);
//
//        assertEquals("The object is of the correct type.", WordDto.class, convertedWord.getClass());
//    }
//
//    @Test
//    public void addWordTest(){
////        assertNotNull(wordRepository);
//
//        Collection<EnglishWord> englishWords = new ArrayList<>();
//        EnglishWord allegorical = new EnglishWord("allegorical");
//        EnglishWord emblematic = new EnglishWord("emblematic");
//        englishWords.add(allegorical);
//        englishWords.add(emblematic);
//        Set<Category> categories = new HashSet<>();
//        Category art = new Category("art");
//        art.setId(1L);
//        Category adj = new Category("adj");
//        adj.setId(2L);
//        categories.add(art);
//        categories.add(adj);
//
//        Word testWord = new Word("寓意的", englishWords, "ぐういてき", categories);
//
//        Set<EnglishWord> englishWordsForRequest = Set.of(allegorical, emblematic);
//        AddWordRequest testRequest = new AddWordRequest();
//        testRequest.setJapaneseWord("寓意的");
//        testRequest.setEnglishWord(englishWordsForRequest);
//        testRequest.setCategory(categories);
//        testRequest.setHiragana("ぐういてき");
//
//        when(englishWordRepository.existsByEnglishWord("allegorical")).thenReturn(false);
//        when(englishWordRepository.existsByEnglishWord("emblematic")).thenReturn(false);
//        when(englishWordRepository.save(allegorical)).thenReturn(allegorical);
//        when(englishWordRepository.save(emblematic)).thenReturn(emblematic);
//
//        when(categoryRepository.existsByName("art")).thenReturn(false);
//        when(categoryRepository.existsByName("adj")).thenReturn(false);
//        when(categoryRepository.save(art)).thenReturn(art);
//        when(categoryRepository.save(adj)).thenReturn(adj);
//        verify(categoryRepository).save(adj);
//        Word testWordWithId = new Word("寓意的", englishWords, "ぐういてき", categories);
//        testWordWithId.setId(1L);
//        when(wordRepository.save(testWord)).thenReturn(testWordWithId);
//        when(wordRepository.findByJapaneseWord("寓意的")).thenReturn(testWordWithId);
//        Collection<Word> wordsCollection = new HashSet<>();
//        wordsCollection.add(testWord);
//        when(englishWordRepository.findById(1L)).thenReturn(Optional.of(allegorical));
//        when(englishWordRepository.findById(2L)).thenReturn(Optional.of(emblematic));
//        allegorical.setWord(wordsCollection);
//        emblematic.setWord(wordsCollection);
//        when(englishWordRepository.save(allegorical)).thenReturn(allegorical);
//        when(englishWordRepository.save(emblematic)).thenReturn(emblematic);
//
//        when(categoryRepository.findById(1L)).thenReturn(Optional.of(art));
//        when(categoryRepository.findById(2L)).thenReturn(Optional.of(adj));
//        art.setWords(wordsCollection);
//        adj.setWords(wordsCollection);
//        when(categoryRepository.save(art)).thenReturn(art);
//        when(categoryRepository.save(adj)).thenReturn(adj);
//
//        WordDto testResult = wordService.addWord(testRequest);
//
//        assertEquals("The object is of the correct type.", WordDto.class, testResult.getClass());
//    }
//
//    @Test
//    public void testGetWordById(){
//        Collection<EnglishWord> englishWords = new ArrayList<>();
//        EnglishWord allegorical = new EnglishWord("allegorical");
//        EnglishWord emblematic = new EnglishWord("emblematic");
//        englishWords.add(allegorical);
//        englishWords.add(emblematic);
//
//        Set<Category> categories = new HashSet<>();
//        Category art = new Category("art");
//        Category adj = new Category("adj");
//        categories.add(art);
//        categories.add(adj);
//
//        Word testWord = new Word("寓意的", englishWords, "ぐういてき", categories);
//        testWord.setId(1L);
//
//        when(wordRepository.findById(1L)).thenReturn(Optional.of(testWord));
//
//        WordDto retrievedWord = wordService.getWordById(1L);
//
//        verify(wordRepository).findById(1L);
////        assertEquals("The object is of the correct type.", WordDto.class, retrievedWord.getClass());
//
//    }
}
