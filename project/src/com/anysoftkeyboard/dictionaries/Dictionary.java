package com.anysoftkeyboard.dictionaries;

import com.anysoftkeyboard.WordComposer;

/**
 * Abstract base class for a dictionary that can do a fuzzy search for words based on a set of key
 * strokes.
 */
abstract public class Dictionary {
    /**
     * Whether or not to replicate the typed word in the suggested list, even if it's valid.
     */
    protected static final boolean INCLUDE_TYPED_WORD_IF_VALID = false;

    /**
     * The weight to give to a word if it's length is the same as the number of typed characters.
     */
    protected static final int FULL_WORD_FREQ_MULTIPLIER = 2;

    /**
     * Interface to be implemented by classes requesting words to be fetched from the dictionary.
     * @see #getWords(WordComposer, WordCallback)
     */
    public interface WordCallback {
        /**
         * Adds a word to a list of suggestions. The word is expected to be ordered based on
         * the provided frequency.
         * @param word the character array containing the word
         * @param wordOffset starting offset of the word in the character array
         * @param wordLength length of valid characters in the character array
         * @param frequency the frequency of occurence. This is normalized between 1 and 255, but
         * can exceed those limits
         * @return true if the word was added, false if no more words are required
         */
        boolean addWord(char[] word, int wordOffset, int wordLength, int frequency);
    }
    
    private final String mDictionaryName;
    
    protected Dictionary(String dictionaryName)
    {
    	mDictionaryName = dictionaryName;
    }

	/**
     * Searches for words in the dictionary that match the characters in the composer. Matched
     * words are added through the callback object.
     * @param composer the key sequence to match
     * @param callback the callback object to send matched words to as possible candidates
     * @see WordCallback#addWord(char[], int, int)
     */
    abstract public void getWords(final WordComposer composer, final WordCallback callback);

    /**
     * Checks if the given word occurs in the dictionary
     * @param word the word to search for. The search should be case-insensitive.
     * @return true if the word exists, false otherwise
     */
    abstract public boolean isValidWord(CharSequence word);

    /**
     * Compares the contents of the character array with the typed word and returns true if they
     * are the same.
     * @param word the array of characters that make up the word
     * @param length the number of valid characters in the character array
     * @param typedWord the word to compare with
     * @return true if they are the same, false otherwise.
     */
    protected boolean same(final char[] word, final int length, final CharSequence typedWord) {
        if (typedWord.length() != length) {
            return false;
        }
        for (int i = 0; i < length; i++) {
            if (word[i] != typedWord.charAt(i)) {
                return false;
            }
        }
        return true;
    }

    public abstract void close();

    public abstract void loadDictionary();
    
    public String getDictionaryName() {return mDictionaryName;}
}
