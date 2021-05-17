package id.paniclabs.matchips.validator;

import androidx.annotation.NonNull;
import android.text.SpannableStringBuilder;
import android.util.Pair;

import id.paniclabs.matchips.token.ChipTokenizer;

import java.util.List;

/**
 * A {@link ChipsValidator} that deems text to be invalid if it contains
 * unterminated tokens and fixes the text by chipifying all the unterminated tokens.
 */
public class ChipifyingChipsValidator implements ChipsValidator {

    @Override
    public boolean isValid(@NonNull ChipTokenizer chipTokenizer, CharSequence text) {

        // The text is considered valid if there are no unterminated tokens (everything is a chip)
        List<Pair<Integer, Integer>> unterminatedTokens = chipTokenizer.findAllTokens(text);
        return unterminatedTokens.isEmpty();
    }

    @Override
    public CharSequence fixText(@NonNull ChipTokenizer chipTokenizer, CharSequence invalidText) {
        SpannableStringBuilder newText = new SpannableStringBuilder(invalidText);
        chipTokenizer.terminateAllTokens(newText);
        return newText;
    }
}
