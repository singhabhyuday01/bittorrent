package bencode.coders.model;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class CodedResult {
    int length;
    Object result;
}
