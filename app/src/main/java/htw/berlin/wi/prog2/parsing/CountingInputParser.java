package htw.berlin.wi.prog2.parsing;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CountingInputParser implements ExtendableInputParser {

    @Override
    public Map<Long, Integer> idsAndCountFromInput(String inputLine, Map<String, Long> keywordsToIds) {
        var result = new HashMap<Long, Integer>();
        var words = inputLine.split("[ ,.]+");
        for (String word : words) {
            var id = keywordsToIds.get(word);
            if(id==null) continue;
            if(result.containsKey(id)) {
                var count = result.get(id);
                result.put(id, count+1);
            } else result.put(id, 1);
        }
        return result;
    }
}
