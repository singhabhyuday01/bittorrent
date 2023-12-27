package bittorrent.bencode.coders.model;

import bittorrent.bencode.coders.Coder;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class DecodedResult {
    int length;
    Object result;
    Coder coder;
}
