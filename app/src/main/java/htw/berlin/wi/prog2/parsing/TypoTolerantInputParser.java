package htw.berlin.wi.prog2.parsing;

import java.util.Map;

public class TypoTolerantInputParser implements ExtendableInputParser {

    private CountingInputParser parser = new CountingInputParser();

    @Override
    public Map<Long, Integer> idsAndCountFromInput(String inputLine, Map<String, Long> keywordsToIds) {
        return parser.idsAndCountFromInput(inputLine, keywordsToIds);
    }
}